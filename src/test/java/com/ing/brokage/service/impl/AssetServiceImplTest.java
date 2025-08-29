package com.ing.brokage.service.impl;

import com.ing.brokage.constant.ExceptionConstants;
import com.ing.brokage.exception.CustomException;
import com.ing.brokage.modal.dto.AssetDto;
import com.ing.brokage.persistance.entity.Asset;
import com.ing.brokage.persistance.repository.AssetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AssetServiceImplTest {

    @Mock
    private AssetRepository assetRepository;

    @InjectMocks
    private AssetServiceImpl assetService;

    private Asset asset;

    private ModelMapper modelMapper = spy(new ModelMapper());


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        asset = new Asset();
        asset.setId(1L);
        asset.setAssetName("TRY");
    }

    @Test
    void testGetAssetByCustomerAndName_Success() {
        when(assetRepository.findByCustomerIdAndAssetName(1L, "TRY")).thenReturn(Optional.of(asset));

        Asset result = assetService.getAssetByCustomerAndName(1L, "TRY");

        assertNotNull(result);
        assertEquals("TRY", result.getAssetName());
        verify(assetRepository, times(1)).findByCustomerIdAndAssetName(1L, "TRY");
    }

    @Test
    void testGetAssetByCustomerAndName_NotFound() {
        when(assetRepository.findByCustomerIdAndAssetName(1L, "USD")).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class,
                () -> assetService.getAssetByCustomerAndName(1L, "USD"));

        assertEquals(ExceptionConstants.ASSET_NOT_FOUND_ERROR_MSG, exception.getMessage());
        verify(assetRepository, times(1)).findByCustomerIdAndAssetName(1L, "USD");
    }

    @Test
    void testSaveAsset() {
        when(assetRepository.save(asset)).thenReturn(asset);

        Asset saved = assetService.save(asset);

        assertNotNull(saved);
        assertEquals(asset, saved);
        verify(assetRepository, times(1)).save(asset);
    }

    @Test
    void testListAssetsByCustomer() {
        List<Asset> assets = List.of(asset);

        when(assetRepository.findAllByCustomerId(1L)).thenReturn(assets);

        List<AssetDto> result = assetService.listAssetsByCustomer(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }
}
