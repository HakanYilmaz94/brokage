package com.ing.brokage.modal.request;

import com.ing.brokage.persistance.enums.SideEnum;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

import static com.ing.brokage.constant.ValidationErrorConstants.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrderRequest {

    @NotBlank(message = NOT_BLANK_ERROR_MSG)
    private String assetName;

    @NotNull(message = NOT_NULL_MSG)
    private SideEnum side;

    @NotNull(message = NOT_NULL_MSG)
    @DecimalMin(value = "0.01", message = POSITIVE_ALLOWED_MSG)
    private BigDecimal price;

    @NotNull(message = NOT_NULL_MSG)
    @Positive(message = POSITIVE_ALLOWED_MSG)
    private Long size;

    private Long customerId;
}
