package io.wodo.bscengine.model.request.wallet;

import io.wodo.bscengine.enumtype.EnumStatus;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
public class WalletCreateRequest {

    @NotNull
    private EnumStatus status;

    @NotNull(message = "amount size 0 - 999999999,9999")
    @Min(0)
    @DecimalMax("999999999.9999")
    private BigDecimal amount;
}