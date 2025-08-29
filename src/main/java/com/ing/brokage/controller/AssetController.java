package com.ing.brokage.controller;

import com.ing.brokage.annotation.IsUserOrAdmin;
import com.ing.brokage.modal.dto.AssetDto;
import com.ing.brokage.persistance.entity.Customer;
import com.ing.brokage.service.AssetService;
import com.ing.brokage.util.CustomerUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.ing.brokage.constant.UrlConstants.ASSET;
import static com.ing.brokage.constant.UrlConstants.LIST;

@RestController
@RequestMapping(path = ASSET)
@RequiredArgsConstructor
@IsUserOrAdmin
public class AssetController {

    private final AssetService assetService;

    @GetMapping(LIST)
    public List<AssetDto> listAssets(@AuthenticationPrincipal Customer customer, @RequestParam(required = false) Long customerId) {
        return assetService.listAssetsByCustomer(CustomerUtils.resolveCustomerId(customer, customerId));
    }
}
