package io.wodo.bscengine.model.response.tx;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class TxSendFundResponse {

    private String txHash;
}
