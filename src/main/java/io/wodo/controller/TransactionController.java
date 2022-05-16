package io.wodo.controller;

import io.wodo.domain.Transaction;
import io.wodo.model.request.entity.CreateTransactionEntityRequest;
import io.wodo.model.request.entity.UpdateTransactionEntityRequest;
import io.wodo.model.request.tx.TxSendFundRequest;
import io.wodo.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;


    @PostMapping("/sendFund")
    public Mono<Transaction> sendFund(@RequestBody @Valid TxSendFundRequest sendFundRequest) {
        return transactionService.sendFundTransaction(sendFundRequest);
    }

    @GetMapping("/{transactionId}")
    public Mono<Transaction> getTransactionEntityById(@PathVariable Long transactionId) {
        return transactionService.findTransactionEntityById(transactionId);
    }

    @PostMapping
    public Mono<Transaction> createTransactionEntity(@RequestBody @Valid CreateTransactionEntityRequest createTransactionEntityRequest) {
        return transactionService.createTransactionEntity(createTransactionEntityRequest);
    }

    @PutMapping("/{transactionId}")
    public Mono<Transaction> updateTransactionEntity(@RequestBody @Valid UpdateTransactionEntityRequest updateTransactionEntityRequest, @PathVariable Long transactionId) {
        return transactionService.updateTransactionEntity(transactionId, updateTransactionEntityRequest);
    }

}