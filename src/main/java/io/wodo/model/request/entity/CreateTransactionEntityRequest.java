package io.wodo.model.request.entity;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.BigInteger;

@Getter
@Setter
public class CreateTransactionEntityRequest {

    @NotBlank
    private String transactionHash;

    @NotBlank
    private String from;

    @NotBlank
    private String to;

    @NotNull
    @DecimalMin("0.000000000001")
    private BigDecimal amount;

    @NotNull
    @Min(0)
    private BigInteger nonce;
}
