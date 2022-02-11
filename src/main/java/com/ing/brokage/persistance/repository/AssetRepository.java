package com.ing.brokage.persistance.repository;

import com.ing.brokage.persistance.entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {

    Optional<Asset> findByCustomerIdAndAssetName(Long customerId, String assetName);

    List<Asset> findAllByCustomerId(Long customerId);
}

