package io.wodo.bscengine.controller;

import io.wodo.bscengine.domain.Transaction;
import io.wodo.bscengine.model.request.tx.TxSendFundRequest;
import io.wodo.bscengine.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<String> sendFund(@RequestBody @Valid TxSendFundRequest sendFundRequest) throws Exception {
        return ok(transactionService.sendFundTransaction(sendFundRequest));
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable Long transactionId) {
        Transaction transaction = transactionService.findById(transactionId);
        return ok(transaction);
    }
}