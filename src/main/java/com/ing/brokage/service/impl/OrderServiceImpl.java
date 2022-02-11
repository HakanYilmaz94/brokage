package com.ing.brokage.service.impl;

import com.ing.brokage.constant.ExceptionConstants;
import com.ing.brokage.exception.CustomException;
import com.ing.brokage.modal.dto.OrderDto;
import com.ing.brokage.modal.request.CreateOrderRequest;
import com.ing.brokage.persistance.entity.Asset;
import com.ing.brokage.persistance.entity.Customer;
import com.ing.brokage.persistance.entity.Order;
import com.ing.brokage.persistance.enums.OrderStatus;
import com.ing.brokage.persistance.enums.SideEnum;
import com.ing.brokage.persistance.enums.Status;
import com.ing.brokage.persistance.repository.OrderRepository;
import com.ing.brokage.service.AssetService;
import com.ing.brokage.service.CustomerService;
import com.ing.brokage.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final AssetService assetService;
    private final CustomerService customerService;
    private final ModelMapper modelMapper;

    private static final String TRY_ASSET_NAME = "TRY";


    @Override
    @Transactional
    public void createOrder(CreateOrderRequest request, long customerId) {
        Customer customer = customerService.getCustomer(customerId);

        if (request.getSide() == SideEnum.BUY) {
            handleBuyOrder(request, customer);
        } else {
            handleSellOrder(request, customer);
        }
    }

    @Override
    @Transactional
    public void cancelOrder(Long orderId, long customerId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ExceptionConstants.ORDER_NOT_FOUND_ERROR_MSG, ExceptionConstants.ORDER_NOT_FOUND_ERROR_CODE));

        if (order.getOrderStatus() != OrderStatus.PENDING) {
            throw new CustomException(ExceptionConstants.ONLY_PENDING_ORDERS_CANCELED_MSG, ExceptionConstants.ONLY_PENDING_ORDERS_CANCELED_CODE);
        }

        Customer customer = order.getCustomer();

        Asset tryAsset = assetService.getAssetByCustomerAndName(customer.getId(), TRY_ASSET_NAME);
        BigDecimal totalPrice = order.getPrice().multiply(BigDecimal.valueOf(order.getSize()));
        if (order.getSide() == SideEnum.BUY) {
            tryAsset.setUsableSize(tryAsset.getUsableSize() + totalPrice.longValue());
            assetService.save(tryAsset);
        } else {
            tryAsset.setUsableSize(tryAsset.getUsableSize() - totalPrice.longValue());
            assetService.save(tryAsset);

            Asset soldAsset = order.getAsset();
            soldAsset.setUsableSize(soldAsset.getUsableSize() + order.getSize());
            assetService.save(soldAsset);
        }

        order.setOrderStatus(OrderStatus.CANCELED);
        orderRepository.save(order);
    }


    public List<OrderDto> listOrders(long customerId, LocalDateTime startDate, LocalDateTime endDate) {
        Customer customer = customerService.getCustomer(customerId);
        return modelMapper.map(orderRepository.findAllByCustomerAndCreatedAtBetween(customer, startDate, endDate), new TypeToken<List<OrderDto>>() {}.getType());
    }

    @Override
    @Transactional
    public void matchOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ExceptionConstants.ORDER_NOT_FOUND_ERROR_MSG, ExceptionConstants.ORDER_NOT_FOUND_ERROR_CODE));

        if (order.getOrderStatus() != OrderStatus.PENDING) {
            throw new CustomException(ExceptionConstants.ONLY_PENDING_ORDERS_MATCHED_MSG, ExceptionConstants.ONLY_PENDING_ORDERS_MATCHED_CODE);
        }

        if (order.getSide() == SideEnum.BUY) {
            handleBuyMatch(order);
        } else {
            handleSellMatch(order);
        }

        order.setOrderStatus(OrderStatus.MATCHED);
        orderRepository.save(order);
    }

    private void handleBuyMatch(Order order) {
        Customer customer = order.getCustomer();

        // Update TRY asset
        Asset tryAsset = assetService.getAssetByCustomerAndName(customer.getId(), TRY_ASSET_NAME);
        BigDecimal totalPrice = order.getPrice().multiply(BigDecimal.valueOf(order.getSize()));
        tryAsset.setSize(tryAsset.getSize() - totalPrice.longValue());

        // Update purchased asset
        Asset purchasedAsset = getOrCreateAsset(customer, order.getAssetName());
        purchasedAsset.setUsableSize(purchasedAsset.getUsableSize() + order.getSize());
        purchasedAsset.setSize(purchasedAsset.getSize() + order.getSize());

        assetService.save(tryAsset);
        assetService.save(purchasedAsset);
    }

    private void handleSellMatch(Order order) {
        Customer customer = order.getCustomer();

        // Update TRY asset
        Asset tryAsset = assetService.getAssetByCustomerAndName(customer.getId(), TRY_ASSET_NAME);
        BigDecimal totalPrice = order.getPrice().multiply(BigDecimal.valueOf(order.getSize()));
        tryAsset.setSize(tryAsset.getSize() + totalPrice.longValue());

        // Update sold asset
        Asset soldAsset = order.getAsset();
        soldAsset.setSize(soldAsset.getSize() - order.getSize());

        assetService.save(tryAsset);
        assetService.save(soldAsset);
    }

    private Asset getOrCreateAsset(Customer customer, String assetName) {
        try {
            return assetService.getAssetByCustomerAndName(customer.getId(), assetName);
        } catch (CustomException e) {
            Asset newAsset = new Asset();
            newAsset.setCustomer(customer);
            newAsset.setAssetName(assetName);
            newAsset.setSize(0L);
            newAsset.setUsableSize(0L);
            newAsset.setStatus(Status.ACTIVE);
            return newAsset;
        }
    }


    private void handleBuyOrder(CreateOrderRequest request, Customer customer) {
        Asset tryAsset = assetService.getAssetByCustomerAndName(customer.getId(), "TRY");

        BigDecimal totalCost = request.getPrice().multiply(BigDecimal.valueOf(request.getSize()));
        validateTryBalance(tryAsset, totalCost);

        lockTryBalance(tryAsset, totalCost);

        Order order = buildOrder(request, customer, tryAsset);
        orderRepository.save(order);
    }

    private void handleSellOrder(CreateOrderRequest request, Customer customer) {
        Asset asset = assetService.getAssetByCustomerAndName(customer.getId(), request.getAssetName());

        validateStockBalance(asset, request.getSize());
        unlockTRYBalance(request, customer);
        lockStockBalance(asset, request.getSize());

        Order order = buildOrder(request, customer, asset);
        orderRepository.save(order);
    }

    private void validateTryBalance(Asset tryAsset, BigDecimal totalCost) {
        if (BigDecimal.valueOf(tryAsset.getUsableSize()).compareTo(totalCost) < 0) {
            throw new CustomException(ExceptionConstants.INSUFFICIENT_TRY_BALANCE_MSG, ExceptionConstants.INSUFFICIENT_TRY_BALANCE_CODE);
        }
    }

    private void lockTryBalance(Asset tryAsset, BigDecimal totalCost) {
        long lockAmount = totalCost.longValue();
        tryAsset.setUsableSize(tryAsset.getUsableSize() - lockAmount);
        assetService.save(tryAsset);
    }

    private void validateStockBalance(Asset asset, long requestedSize) {
        if (asset.getUsableSize() < requestedSize) {
            throw new CustomException(ExceptionConstants.INSUFFICIENT_STOCK_SIZE_MSG, ExceptionConstants.INSUFFICIENT_STOCK_SIZE_CODE);
        }
    }

    private void lockStockBalance(Asset asset, long lockSize) {
        asset.setUsableSize(asset.getUsableSize() - lockSize);
        assetService.save(asset);
    }

    private void unlockTRYBalance(CreateOrderRequest request, Customer customer) {
        Asset tryAsset = assetService.getAssetByCustomerAndName(customer.getId(), "TRY");
        BigDecimal totalPrice = request.getPrice().multiply(BigDecimal.valueOf(request.getSize()));
        tryAsset.setUsableSize(tryAsset.getUsableSize() + totalPrice.longValue());
        assetService.save(tryAsset);
    }

    private Order buildOrder(CreateOrderRequest request, Customer customer, Asset asset) {
        Order order = new Order();
        order.setCustomer(customer);
        order.setAsset(asset);
        order.setSide(request.getSide());
        order.setSize(request.getSize());
        order.setPrice(request.getPrice());
        order.setAssetName(request.getAssetName());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setStatus(Status.ACTIVE);
        return order;
    }
}
