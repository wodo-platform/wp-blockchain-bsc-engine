package io.wodo.bscengine.service;

import io.wodo.bscengine.domain.Transaction;
import io.wodo.bscengine.enumtype.EnumStatus;
import io.wodo.bscengine.enumtype.EnumTxStatus;
import io.wodo.bscengine.exception.NotFoundException;
import io.wodo.bscengine.model.request.tx.TxSendFundRequest;
import io.wodo.bscengine.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Service
public class TransactionService {

    @Autowired
    private Web3j web3j;

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    public String sendFundTransaction(TxSendFundRequest sendFundRequest) throws Exception {
        String transactionHash = sendFunds(sendFundRequest.getSecret(), sendFundRequest.getTo(), sendFundRequest.getAmount());
        createTransactionEntity(transactionHash, sendFundRequest.getFrom(), sendFundRequest.getTo());
        return transactionHash;
    }


    private String sendFunds(String secret, String to, BigDecimal amount) throws IOException, InterruptedException, ExecutionException {
        Credentials credentials = Credentials.create(secret);
        BigInteger nonce = getLatestNonce(credentials);
        RawTransaction rawTransaction = createRawTransaction(nonce, to, amount.toString());
        Integer chainId = 97;
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, chainId.byteValue(), credentials);
        String hexValue = Numeric.toHexString(signedMessage);
        // Send transaction
        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
        //get TransactionHash
        return ethSendTransaction.getTransactionHash();

        /*  //check status
        Optional<TransactionReceipt> transactionReceipt = null;
        do {

            EthGetTransactionReceipt ethGetTransactionReceiptResp = web3j.ethGetTransactionReceipt(transactionHash).send();
            transactionReceipt = ethGetTransactionReceiptResp.getTransactionReceipt();
            Thread.sleep(3000); // Wait for 3 sec
        } while (!transactionReceipt.isPresent());*/

    }

    private BigInteger getLatestNonce(Credentials credentials) {
        BigInteger nonce;
        try {
            EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.PENDING).send();
            nonce = ethGetTransactionCount.getTransactionCount();

        } catch (Exception e) {
            return null;
        }

        return nonce;
    }

    private RawTransaction createRawTransaction(BigInteger nonce, String to, String amountToBeSent) {
        BigInteger amount = Convert.toWei(amountToBeSent, Convert.Unit.GWEI).toBigInteger();
        BigInteger gasLimit = BigInteger.valueOf(21000);
        BigInteger gasPrice = Convert.toWei("22", Convert.Unit.GWEI).toBigInteger();
        return RawTransaction.createEtherTransaction(nonce, DefaultGasProvider.GAS_PRICE, gasLimit, to, amount);
    }

    private void createTransactionEntity(String transactionHash, String from, String to) {
        Transaction transaction = Transaction.builder()
                .txHash(transactionHash)
                .txStatus(EnumTxStatus.PENDING)
                .from(from)
                .to(to)
                .gasPrice(BigDecimal.ZERO)
                .gasLimit(BigDecimal.ONE)
                .build();
        transactionRepository.save(transaction);
    }

    public Transaction findById(Long id) {
        Optional<Transaction> transaction = transactionRepository.findById(id);
        if (transaction.isPresent() && transaction.get().getStatus() == EnumStatus.ACTIVE) {
            return transaction.get();
        } else {
            throw new NotFoundException("There is no 'Transaction' with id: " + id);
        }
    }
}
