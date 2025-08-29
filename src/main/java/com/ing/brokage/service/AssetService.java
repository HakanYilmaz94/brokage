package com.ing.brokage.service;

import com.ing.brokage.modal.dto.AssetDto;
import com.ing.brokage.persistance.entity.Asset;

import java.util.List;

public interface AssetService {

    Asset getAssetByCustomerAndName(Long customerId, String assetName);
    Asset save(Asset asset);
    List<AssetDto> listAssetsByCustomer(Long customerId);
}
