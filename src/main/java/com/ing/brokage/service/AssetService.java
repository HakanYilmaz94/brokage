package com.ing.brokage.service;

import com.ing.brokage.persistance.entity.Asset;

import java.util.List;

public interface AssetService {

    Asset getAssetByCustomerAndName(Long customerId, String assetName);
    Asset save(Asset asset);
    List<Asset> listAssetsByCustomer(Long customerId);
}
