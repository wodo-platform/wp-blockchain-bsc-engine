package io.wodo.bscengine.service;

import io.wodo.bscengine.domain.Wallet;
import io.wodo.bscengine.enumtype.EnumStatus;
import io.wodo.bscengine.exception.NotFoundException;
import io.wodo.bscengine.model.request.wallet.WalletCreateRequest;
import io.wodo.bscengine.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    public Wallet createWallet(WalletCreateRequest createWalletRequest) {
        Wallet wallet = new Wallet(createWalletRequest.getStatus(), createWalletRequest.getAmount());
        return walletRepository.save(wallet);
    }

    public Page<Wallet> getAllWallets(Pageable pageable) {
        return walletRepository.findAll(pageable);
    }

    public Wallet findById(Long walletId) {
        Optional<Wallet> wallet = walletRepository.findById(walletId);
        if (wallet.isPresent() && wallet.get().getStatus() == EnumStatus.ACTIVE) {
            return wallet.get();
        } else {
            throw new NotFoundException("There is no 'Wallet' with id: " + walletId);
        }
    }

    public Wallet deleteWallet(Long walletId) {
        Wallet wallet = findById(walletId);
        wallet.setStatus(EnumStatus.DELETED);
        return walletRepository.save(wallet);
    }
}