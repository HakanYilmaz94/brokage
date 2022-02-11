package com.ing.brokage.controller;

import com.ing.brokage.persistance.entity.Asset;
import com.ing.brokage.persistance.entity.Customer;
import com.ing.brokage.service.AssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/asset")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('USER')")
public class AssetController {

    private final AssetService assetService;

    @GetMapping("/list")
    public List<Asset> listAssets(@AuthenticationPrincipal Customer customer) {
        return assetService.listAssetsByCustomer(customer.getId());
    }
}
