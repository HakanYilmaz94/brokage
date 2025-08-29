package com.ing.brokage.modal.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetDto {
    private Long id;
    private String assetName;
    private Long size;
    private Long usableSize;
}
