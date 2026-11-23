/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.schoolaccount.SchoolAccount.controller.BankTransactionController
 *  com.schoolaccount.SchoolAccount.model.BankTransaction
 *  com.schoolaccount.SchoolAccount.model.ResponseMessage
 *  com.schoolaccount.SchoolAccount.service.BankTransactionService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.http.ResponseEntity
 *  org.springframework.web.bind.annotation.CrossOrigin
 *  org.springframework.web.bind.annotation.DeleteMapping
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.PutMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.schoolaccount.SchoolAccount.controller;

import com.schoolaccount.SchoolAccount.model.BankTransaction;
import com.schoolaccount.SchoolAccount.model.ResponseMessage;
import com.schoolaccount.SchoolAccount.service.BankTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins={"*"})
@RequestMapping(value={"/bank-transactions"})
public class BankTransactionController {
    @Autowired
    private BankTransactionService bankTransactionService;

    @PostMapping
    public ResponseEntity<ResponseMessage> addBankTransaction(@RequestBody BankTransaction bankTransaction) {
        return ResponseEntity.ok(this.bankTransactionService.addBankTransaction(bankTransaction));
    }

    @GetMapping
    public ResponseEntity<ResponseMessage> getAllBankTransactions() {
        return ResponseEntity.ok(this.bankTransactionService.getAllBankTransactions());
    }

    @GetMapping(value={"/{id}"})
    public ResponseEntity<ResponseMessage> getBankTransactionById(@PathVariable String id) {
        return ResponseEntity.ok(this.bankTransactionService.getBankTransactionById(id));
    }

    @PutMapping(value={"/{id}"})
    public ResponseEntity<ResponseMessage> updateBankTransaction(@PathVariable String id, @RequestBody BankTransaction bankTransaction) {
        return ResponseEntity.ok(this.bankTransactionService.updateBankTransaction(id, bankTransaction));
    }

    @DeleteMapping(value={"/{id}"})
    public ResponseEntity<ResponseMessage> deleteBankTransaction(@PathVariable String id) {
        return ResponseEntity.ok(this.bankTransactionService.deleteBankTransaction(id));
    }

    @GetMapping(value={"/month/{year}/{month}"})
    public ResponseEntity<ResponseMessage> getBankTransactionsByMonth(@PathVariable int year, @PathVariable int month) {
        return ResponseEntity.ok(this.bankTransactionService.getBankTransactionsByMonth(year, month));
    }

    @PostMapping(value={"/{schoolId}"})
    public ResponseEntity<ResponseMessage> addBankTransactionWithSchool(@RequestBody BankTransaction bankTransaction, @PathVariable String schoolId) {
        return ResponseEntity.ok(this.bankTransactionService.addBankTransactionWithSchool(bankTransaction, schoolId));
    }

    @GetMapping(value={"/all/{schoolId}"})
    public ResponseEntity<ResponseMessage> getAllBankTransactionsBySchool(@PathVariable String schoolId) {
        return ResponseEntity.ok(this.bankTransactionService.getAllBankTransactionsBySchool(schoolId));
    }

    @GetMapping(value={"/id/{id}/{schoolId}"})
    public ResponseEntity<ResponseMessage> getBankTransactionByIdAndSchool(@PathVariable String id, @PathVariable String schoolId) {
        return ResponseEntity.ok(this.bankTransactionService.getBankTransactionByIdAndSchool(id, schoolId));
    }

    @PutMapping(value={"/{schoolId}/{id}"})
    public ResponseEntity<ResponseMessage> updateBankTransactionWithSchool(@PathVariable String id, @RequestBody BankTransaction updatedTransaction, @PathVariable String schoolId) {
        return ResponseEntity.ok(this.bankTransactionService.updateBankTransactionWithSchool(id, updatedTransaction, schoolId));
    }

    @DeleteMapping(value={"/delete/{id}/{schoolId}", "/{id}/{schoolId}"})
    public ResponseEntity<ResponseMessage> deleteBankTransactionWithSchool(@PathVariable String id, @PathVariable String schoolId) {
        return ResponseEntity.ok(this.bankTransactionService.deleteBankTransactionWithSchool(id, schoolId));
    }

    @GetMapping(value={"/month/{year}/{month}/{schoolId}"})
    public ResponseEntity<ResponseMessage> getBankTransactionsByMonthWithSchool(@PathVariable int year, @PathVariable int month, @PathVariable String schoolId) {
        return ResponseEntity.ok(this.bankTransactionService.getBankTransactionsByMonthWithSchool(year, month, schoolId));
    }
}

