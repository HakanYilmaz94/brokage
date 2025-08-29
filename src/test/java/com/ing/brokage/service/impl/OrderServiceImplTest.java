package com.ing.brokage.service.impl;

import com.ing.brokage.constant.ExceptionConstants;
import com.ing.brokage.exception.CustomException;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class OrderServiceImplTest {

    private OrderRepository orderRepository;
    private AssetService assetService;
    private CustomerService customerService;
    private OrderServiceImpl orderService;

    private Customer customer;
    private Asset tryAsset;
    private Order order;
    private Asset assetABC;
    private Order buyOrder;
    private Order sellOrder;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        assetService = mock(AssetService.class);
        customerService = mock(CustomerService.class);

        orderService = new OrderServiceImpl(orderRepository, assetService, customerService, Mockito.mock(org.modelmapper.ModelMapper.class));

        // Test customer ve TRY asset
        customer = new Customer();
        customer.setId(1L);

        tryAsset = new Asset();
        tryAsset.setCustomer(customer);
        tryAsset.setAssetName("TRY");
        tryAsset.setSize(1000L);
        tryAsset.setUsableSize(1000L);

        order = new Order();
        order.setId(1L);
        order.setCustomer(customer);
        order.setAsset(tryAsset);
        order.setSide(SideEnum.BUY);
        order.setPrice(BigDecimal.valueOf(10));
        order.setSize(10L);
        order.setStatus(Status.ACTIVE);
        order.setOrderStatus(com.ing.brokage.persistance.enums.OrderStatus.PENDING);

        assetABC = new Asset();
        assetABC.setCustomer(customer);
        assetABC.setAssetName("ABC");
        assetABC.setSize(100L);
        assetABC.setUsableSize(100L);

        buyOrder = new Order();
        buyOrder.setId(1L);
        buyOrder.setCustomer(customer);
        buyOrder.setSide(SideEnum.BUY);
        buyOrder.setAssetName("ABC");
        buyOrder.setSize(10L);
        buyOrder.setPrice(BigDecimal.TEN);
        buyOrder.setOrderStatus(OrderStatus.PENDING);
        buyOrder.setStatus(Status.ACTIVE);

        sellOrder = new Order();
        sellOrder.setId(2L);
        sellOrder.setCustomer(customer);
        sellOrder.setAsset(assetABC);
        sellOrder.setSide(SideEnum.SELL);
        sellOrder.setPrice(BigDecimal.valueOf(10));
        sellOrder.setSize(10L);
        sellOrder.setStatus(Status.ACTIVE);
        sellOrder.setOrderStatus(OrderStatus.PENDING);

    }

    @Test
    void testCreateOrder_Buy_Success() {
        when(customerService.getCustomer(1L)).thenReturn(customer);
        when(assetService.getAssetByCustomerAndName(1L, "TRY")).thenReturn(tryAsset);
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);

        CreateOrderRequest request = new CreateOrderRequest();
        request.setSide(SideEnum.BUY);
        request.setPrice(BigDecimal.valueOf(10));
        request.setSize(10L);
        request.setAssetName("ABC");

        orderService.createOrder(request, 1L);

        ArgumentCaptor<Asset> assetCaptor = ArgumentCaptor.forClass(Asset.class);
        verify(assetService, times(1)).save(assetCaptor.capture());
        Asset savedTry = assetCaptor.getValue();

        assertEquals(900L, savedTry.getUsableSize()); // 1000 - (10*10)
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testCancelOrder_Pending_Buy() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(assetService.getAssetByCustomerAndName(customer.getId(), "TRY")).thenReturn(tryAsset);
        when(assetService.save(any(Asset.class))).thenAnswer(i -> i.getArguments()[0]);

        orderService.cancelOrder(1L, 1L);

        assertEquals(com.ing.brokage.persistance.enums.OrderStatus.CANCELED, order.getOrderStatus());
        verify(assetService, times(1)).save(tryAsset);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void testCancelOrder_NotPending_ThrowsCustomException() {
        order.setOrderStatus(com.ing.brokage.persistance.enums.OrderStatus.MATCHED);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        CustomException ex = assertThrows(CustomException.class, () -> orderService.cancelOrder(1L, 1L));
        assertEquals(ExceptionConstants.ONLY_PENDING_ORDERS_CANCELED_CODE, ex.getStatusCode());
    }

    @Test
    void testHandleBuyOrder_AssetUpdates() {
        when(customerService.getCustomer(1L)).thenReturn(customer);
        when(assetService.getAssetByCustomerAndName(1L, "TRY")).thenReturn(tryAsset);
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);

        CreateOrderRequest request = new CreateOrderRequest();
        request.setSide(SideEnum.BUY);
        request.setPrice(BigDecimal.valueOf(10));
        request.setSize(10L);
        request.setAssetName("ABC");

        orderService.createOrder(request, 1L);

        assertEquals(900L, tryAsset.getUsableSize()); // TRY asset usable decreased
        verify(assetService, times(1)).save(tryAsset);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testHandleSellOrder_AssetUpdates() {
        when(customerService.getCustomer(1L)).thenReturn(customer);
        when(assetService.getAssetByCustomerAndName(1L, "ABC")).thenReturn(assetABC);
        when(assetService.getAssetByCustomerAndName(1L, "TRY")).thenReturn(tryAsset);
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);

        CreateOrderRequest request = new CreateOrderRequest();
        request.setSide(SideEnum.SELL);
        request.setPrice(BigDecimal.valueOf(10));
        request.setSize(10L);
        request.setAssetName("ABC");

        orderService.createOrder(request, 1L);

        // TRY balance should increase by totalPrice
        assertEquals(1100L, tryAsset.getUsableSize());
        // ABC asset usable size decreased
        assertEquals(90L, assetABC.getUsableSize());
        verify(assetService, times(2)).save(any(Asset.class)); // TRY + ABC saved
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testMatchOrder_Buy_OrderUpdates() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(buyOrder));
        when(assetService.getAssetByCustomerAndName(1L, "TRY")).thenReturn(tryAsset);
        when(assetService.getAssetByCustomerAndName(1L, "ABC")).thenReturn(assetABC);

        orderService.matchOrder(1L);

        // Order status updated
        assertEquals(OrderStatus.MATCHED, buyOrder.getOrderStatus());
        // TRY asset size decreased by totalPrice = price * size = 10 * 10 = 100
        assertEquals(900L, tryAsset.getSize());
        // Purchased asset increased
        assertEquals(110L, assetABC.getSize());
        assertEquals(110L, assetABC.getUsableSize());

        // Verify saves
        verify(assetService).save(tryAsset);
        verify(assetService).save(assetABC);
        verify(orderRepository).save(buyOrder);
    }

    @Test
    void testMatchOrder_Sell_OrderUpdates() {
        when(orderRepository.findById(2L)).thenReturn(Optional.of(sellOrder));
        when(assetService.getAssetByCustomerAndName(1L, "TRY")).thenReturn(tryAsset);

        orderService.matchOrder(2L);

        assertEquals(OrderStatus.MATCHED, sellOrder.getOrderStatus());
        // TRY size increased by totalPrice
        assertEquals(1100L, tryAsset.getSize());
        // Sold asset decreased
        assertEquals(90L, assetABC.getSize());

        verify(assetService).save(tryAsset);
        verify(assetService).save(assetABC);
        verify(orderRepository).save(sellOrder);
    }

    @Test
    void testMatchOrder_NotFound_ThrowsException() {
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        CustomException ex = assertThrows(CustomException.class, () -> orderService.matchOrder(999L));
        assertEquals(ExceptionConstants.ORDER_NOT_FOUND_ERROR_CODE, ex.getStatusCode());
    }

    @Test
    void testMatchOrder_NotPending_ThrowsException() {
        buyOrder.setOrderStatus(OrderStatus.MATCHED);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(buyOrder));

        CustomException ex = assertThrows(CustomException.class, () -> orderService.matchOrder(1L));
        assertEquals(ExceptionConstants.ONLY_PENDING_ORDERS_MATCHED_CODE, ex.getStatusCode());
    }
}
