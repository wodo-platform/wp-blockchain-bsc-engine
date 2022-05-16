package io.wodo.service;

import io.wodo.domain.Transaction;
import io.wodo.enumtype.EnumStatus;
import io.wodo.enumtype.EnumTxStatus;
import io.wodo.exception.NotFoundException;
import io.wodo.exception.UnexpectedVersionException;
import io.wodo.model.request.entity.CreateTransactionEntityRequest;
import io.wodo.model.request.entity.UpdateTransactionEntityRequest;
import io.wodo.model.request.tx.TxSendFundRequest;
import io.wodo.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.BigInteger;

@Service
@RequiredArgsConstructor
@Log4j2
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final Web3j web3j;

    //TODO
    private void calculateSufficientFunds() {

    }

    public Mono<Transaction> findTransactionEntityById(Long id) {
        long startTime = System.currentTimeMillis();

        return Mono.deferContextual(ctx -> Mono.just(ctx.get("REQUEST_ID")))
                .doOnNext(reqId -> log.info(reqId + " findById repository started with parameters"))
                .flatMap(reqId -> transactionRepository.findById(id)
                        .switchIfEmpty(Mono.error(new NotFoundException("There is no 'Transaction' with id: " + id)))
                        .doOnSuccess(txEntity -> log.info(reqId + " findById repository completed Elapsed time: " + (System.currentTimeMillis() - startTime))));

    }

    @Transactional
    public Mono<Transaction> updateTransactionEntity(Long id, UpdateTransactionEntityRequest updateTransactionEntityRequest) {
        long startTime = System.currentTimeMillis();

        return Mono.deferContextual(ctx -> Mono.just(ctx.get("REQUEST_ID")))
                .doOnNext(reqId -> log.info(reqId + " update repository started with parameters"))
                .flatMap(reqId -> findTransactionEntityById(id)
                        .map(item -> updateTransactionEntityParameters(item, updateTransactionEntityRequest))
                        .flatMap(transactionRepository::save)
                        .doOnSuccess(entity -> log.info(reqId + " update repository  Elapsed time: " + (System.currentTimeMillis() - startTime))));
    }

    @Transactional
    public Mono<Transaction> sendFundTransaction(TxSendFundRequest sendFundRequest) {
        Credentials credentials = Credentials.create(sendFundRequest.getSecret());
        long startTime = System.currentTimeMillis();

        return Mono.deferContextual(ctx -> Mono.just(ctx.get("REQUEST_ID")))
                .doOnNext(reqId -> log.info(reqId + " sendFundTransaction request started with parameters"))
                .flatMap(reqId -> getLatestNonce(credentials)
                        .flatMap(nonce -> sendFunds(credentials, sendFundRequest.getTo(), sendFundRequest.getAmount(), nonce)
                                .flatMap(transaction -> createTransactionEntity(transaction.getTransactionHash(), sendFundRequest.getFrom(), sendFundRequest.getTo(), sendFundRequest.getAmount(), nonce))
                                .doOnSuccess(txEntity -> log.info(reqId + " sendFundTransaction request completed Elapsed time: " + (System.currentTimeMillis() - startTime)))));

    }

    private Mono<EthSendTransaction> sendFunds(Credentials credentials, String to, BigDecimal amount, BigInteger nonce) {
        int chainId = 97;
        long startTime = System.currentTimeMillis();
        RawTransaction rawTransaction = createRawTransaction(nonce, to, amount.toString());
        String hexValue = signRawTransaction(rawTransaction, credentials, (byte) chainId);

        return Mono.deferContextual(ctx -> Mono.just(ctx.get("REQUEST_ID")))
                .doOnNext(reqId -> log.info(reqId + " SendRawTransaction request started with parameters"))
                .flatMap(reqId -> Mono.fromFuture(web3j.ethSendRawTransaction(hexValue).sendAsync()
                                                          .whenComplete((r, t) -> log.info(reqId + " SendRawTransaction request completed Elapsed time: " + (System.currentTimeMillis() - startTime)))));

    }

    private Mono<BigInteger> getLatestNonce(Credentials credentials) {
        long startTime = System.currentTimeMillis();

        return Mono.deferContextual(ctx -> Mono.just(ctx.get("REQUEST_ID")))
                .doOnNext(reqId -> log.info(reqId + " GetTransactionCount request started with parameters"))
                .flatMap(reqId -> Mono.fromFuture(web3j.ethGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.PENDING).sendAsync()
                                                          .whenComplete((r, t) -> log.info(reqId + " GetTransactionCount request completed Elapsed time: " + (System.currentTimeMillis() - startTime))))
                        .flatMap(txCount -> Mono.just(txCount.getTransactionCount())));
    }

    private RawTransaction createRawTransaction(BigInteger nonce, String to, String amountToBeSent) {
        BigInteger amount = Convert.toWei(amountToBeSent, Convert.Unit.GWEI).toBigInteger();
        BigInteger gasLimit = BigInteger.valueOf(21000);
        return RawTransaction.createEtherTransaction(nonce, DefaultGasProvider.GAS_PRICE, gasLimit, to, amount);
    }

    private String signRawTransaction(RawTransaction rawTransaction, Credentials credentials, byte chainId) {
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, chainId, credentials);
        return Numeric.toHexString(signedMessage);
    }

    public Mono<Transaction> createTransactionEntity(CreateTransactionEntityRequest createTransactionEntityRequest) {
        return createTransactionEntity(createTransactionEntityRequest.getTransactionHash(), createTransactionEntityRequest.getFrom(), createTransactionEntityRequest.getTo(),
                                       createTransactionEntityRequest.getAmount(), createTransactionEntityRequest.getNonce());
    }

    private Mono<Transaction> createTransactionEntity(String transactionHash, String from, String to, BigDecimal amount, BigInteger nonce) {
        Transaction transaction = Transaction.builder()
                .status(EnumStatus.ACTIVE)
                .txHash(transactionHash)
                .txStatus(EnumTxStatus.PENDING)
                .sender(from)
                .receiver(to)
                .gasPrice(BigDecimal.ZERO)
                .gasLimit(BigDecimal.ONE)
                .amount(amount)
                .nonce(nonce.longValue())
                .build();
        return transactionRepository.save(transaction);
    }

    private Transaction updateTransactionEntityParameters(Transaction transaction, UpdateTransactionEntityRequest updateTransactionEntityRequest) {

        return updateTransactionEntityParameters(transaction, updateTransactionEntityRequest.getVersion(), updateTransactionEntityRequest.getStatus(), updateTransactionEntityRequest.getTxStatus(), updateTransactionEntityRequest.getCumulativeGasUsed(),
                                                 updateTransactionEntityRequest.getGasUsed(), updateTransactionEntityRequest.getTxIndex(), updateTransactionEntityRequest.getBlockHash(),
                                                 updateTransactionEntityRequest.getBlockNumber());

    }

    private Transaction updateTransactionEntityParameters(Transaction transaction, Long version, EnumStatus status, EnumTxStatus txStatus, BigDecimal cumulativeGasUsed,
                                                          BigDecimal gasUsed, String txIndex, String blockHash, String blockNumber) {

        if (!transaction.getVersion().equals(version))
            throw new UnexpectedVersionException("Transaction", version, transaction.getVersion());

        transaction.setStatus(EnumStatus.valueOf(status.toString()));
        transaction.setTxStatus(EnumTxStatus.valueOf(txStatus.toString()));
        transaction.setCumulativeGasUsed(cumulativeGasUsed);
        transaction.setGasUsed(gasUsed);
        transaction.setTxIndex(txIndex);
        transaction.setBlockHash(blockHash);
        transaction.setBlockNumber(blockNumber);

        return transaction;
    }
}
