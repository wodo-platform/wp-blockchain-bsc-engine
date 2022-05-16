package io.wodo.model.request.entity;

import io.wodo.enumtype.EnumStatus;
import io.wodo.enumtype.EnumTxStatus;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
public class UpdateTransactionEntityRequest {

    @NotNull
    private Long version;

    @NotBlank
    private EnumStatus status;

    @NotBlank
    private EnumTxStatus txStatus;

    @NotNull
    private BigDecimal cumulativeGasUsed;

    @NotNull
    private BigDecimal gasUsed;

    @NotBlank
    private String txIndex;

    @NotBlank
    private String blockHash;

    @NotBlank
    private String blockNumber;
}
