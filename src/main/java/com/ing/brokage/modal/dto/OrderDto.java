package com.ing.brokage.modal.dto;

import com.ing.brokage.persistance.enums.OrderStatus;
import com.ing.brokage.persistance.enums.SideEnum;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {

    private Long orderId;

    private Long customerId;

    private Long assetId;

    private String assetName;

    private SideEnum side;

    private BigDecimal price;

    private Long size;

    private OrderStatus orderStatus;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
