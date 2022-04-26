package io.wodo.bscengine.model.request.wallet;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class WalletBalanceRequest {

    @NotNull
    private Long walletId;

}
