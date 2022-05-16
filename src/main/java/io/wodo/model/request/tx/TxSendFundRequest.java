package io.wodo.model.request.tx;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
public class TxSendFundRequest {

    @NotBlank
    private String secret;

    @NotBlank
    private String to;

    @NotBlank
    private String from;

    @NotNull
    @DecimalMin("0.000000000001")
    private BigDecimal amount;
}