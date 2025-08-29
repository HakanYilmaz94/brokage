package com.ing.brokage.service.impl;

import com.ing.brokage.constant.ExceptionConstants;
import com.ing.brokage.exception.CustomException;
import com.ing.brokage.modal.dto.AssetDto;
import com.ing.brokage.persistance.entity.Asset;
import com.ing.brokage.persistance.repository.AssetRepository;
import com.ing.brokage.service.AssetService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssetServiceImpl implements AssetService {

    private final AssetRepository assetRepository;
    private final ModelMapper modelMapper;

    @Override
    public Asset getAssetByCustomerAndName(Long customerId, String assetName) {
        return assetRepository.findByCustomerIdAndAssetName(customerId, assetName)
                .orElseThrow(() -> new CustomException(ExceptionConstants.ASSET_NOT_FOUND_ERROR_MSG, ExceptionConstants.ASSET_NOT_FOUND_ERROR_CODE));
    }

    @Override
    public Asset save(Asset asset) {
        return assetRepository.save(asset);
    }

    @Override
    public List<AssetDto> listAssetsByCustomer(Long customerId) {
        return modelMapper.map(assetRepository.findAllByCustomerId(customerId), new TypeToken<List<AssetDto>>() {}.getType());
    }
}
