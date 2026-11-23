/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.schoolaccount.SchoolAccount.model.AccountDetails
 *  com.schoolaccount.SchoolAccount.model.BankTransaction
 *  com.schoolaccount.SchoolAccount.model.ResponseMessage
 *  com.schoolaccount.SchoolAccount.repo.AccountDetailsRepository
 *  com.schoolaccount.SchoolAccount.repo.BankTransactionRepository
 *  com.schoolaccount.SchoolAccount.service.BankTransactionService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 */
package com.schoolaccount.SchoolAccount.service;

import com.schoolaccount.SchoolAccount.model.AccountDetails;
import com.schoolaccount.SchoolAccount.model.BankTransaction;
import com.schoolaccount.SchoolAccount.model.ResponseMessage;
import com.schoolaccount.SchoolAccount.repo.AccountDetailsRepository;
import com.schoolaccount.SchoolAccount.repo.BankTransactionRepository;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankTransactionService {
    @Autowired
    private BankTransactionRepository bankTransactionRepository;
    @Autowired
    private AccountDetailsRepository accountDetailsRepository;

    public ResponseMessage addBankTransaction(BankTransaction bankTransaction) {
        bankTransaction.setSchoolId("67ca9167d56ec91fc9539234");
        Optional accountOpt = this.accountDetailsRepository.findAll().stream().findFirst();
        if (accountOpt.isPresent()) {
            AccountDetails account = (AccountDetails)accountOpt.get();
            if ("DEPOSIT".equalsIgnoreCase(bankTransaction.getTransactionType())) {
                account.setOpeningAmount(account.getOpeningAmount() + bankTransaction.getAmount());
            } else if ("WITHDRAW".equalsIgnoreCase(bankTransaction.getTransactionType())) {
                if (account.getOpeningAmount() < bankTransaction.getAmount()) {
                    return new ResponseMessage("Insufficient balance for withdrawal!", null);
                }
                account.setOpeningAmount(account.getOpeningAmount() - bankTransaction.getAmount());
            } else {
                return new ResponseMessage("Invalid transaction type! Use 'DEPOSIT' or 'WITHDRAW'.", null);
            }
            this.accountDetailsRepository.save((Object)account);
            BankTransaction savedTransaction = (BankTransaction)this.bankTransactionRepository.save((Object)bankTransaction);
            return new ResponseMessage("Transaction successful!", (Object)savedTransaction);
        }
        return new ResponseMessage("No account found!", null);
    }

    public ResponseMessage getAllBankTransactions() {
        return new ResponseMessage("Fetched all bank transactions", (Object)this.bankTransactionRepository.findAll());
    }

    public ResponseMessage getBankTransactionById(String id) {
        Optional transaction = this.bankTransactionRepository.findById((Object)id);
        return transaction.map(value -> new ResponseMessage("Bank transaction found", value)).orElse(new ResponseMessage("Bank transaction not found", null));
    }

    public ResponseMessage updateBankTransaction(String id, BankTransaction updatedTransaction) {
        Optional transactionOpt = this.bankTransactionRepository.findById((Object)id);
        if (transactionOpt.isPresent()) {
            updatedTransaction.setId(id);
            BankTransaction savedTransaction = (BankTransaction)this.bankTransactionRepository.save((Object)updatedTransaction);
            return new ResponseMessage("Bank transaction updated successfully", (Object)savedTransaction);
        }
        return new ResponseMessage("Bank transaction not found", null);
    }

    public ResponseMessage deleteBankTransaction(String id) {
        if (this.bankTransactionRepository.existsById((Object)id)) {
            this.bankTransactionRepository.deleteById((Object)id);
            return new ResponseMessage("Bank transaction deleted successfully", null);
        }
        return new ResponseMessage("Bank transaction not found", null);
    }

    public ResponseMessage getBankTransactionsByMonth(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        List transactions = this.bankTransactionRepository.findByDateBetween(startDate, endDate);
        return new ResponseMessage("Fetched bank transactions for " + year + "-" + month, (Object)transactions);
    }

    public ResponseMessage addBankTransactionWithSchool(BankTransaction bankTransaction, String schoolId) {
        bankTransaction.setSchoolId(schoolId);
        Optional accountOpt = this.accountDetailsRepository.findAll().stream().findFirst();
        if (accountOpt.isPresent()) {
            AccountDetails account = (AccountDetails)accountOpt.get();
            if ("DEPOSIT".equalsIgnoreCase(bankTransaction.getTransactionType())) {
                account.setOpeningAmount(account.getOpeningAmount() + bankTransaction.getAmount());
            } else if ("WITHDRAW".equalsIgnoreCase(bankTransaction.getTransactionType())) {
                if (account.getOpeningAmount() < bankTransaction.getAmount()) {
                    return new ResponseMessage("Insufficient balance for withdrawal!", null);
                }
                account.setOpeningAmount(account.getOpeningAmount() - bankTransaction.getAmount());
            } else {
                return new ResponseMessage("Invalid transaction type! Use 'DEPOSIT' or 'WITHDRAW'.", null);
            }
            this.accountDetailsRepository.save((Object)account);
            BankTransaction savedTransaction = (BankTransaction)this.bankTransactionRepository.save((Object)bankTransaction);
            return new ResponseMessage("Transaction successful!", (Object)savedTransaction);
        }
        return new ResponseMessage("No account found!", null);
    }

    public ResponseMessage getAllBankTransactionsBySchool(String schoolId) {
        List transactions = this.bankTransactionRepository.findBySchoolId(schoolId);
        return new ResponseMessage("Fetched all bank transactions for school", (Object)transactions);
    }

    public ResponseMessage getBankTransactionByIdAndSchool(String id, String schoolId) {
        Optional transactionOpt = this.bankTransactionRepository.findById((Object)id);
        if (transactionOpt.isPresent() && ((BankTransaction)transactionOpt.get()).getSchoolId().equals(schoolId)) {
            return new ResponseMessage("Bank transaction found", transactionOpt.get());
        }
        return new ResponseMessage("Bank transaction not found or does not belong to this school", null);
    }

    public ResponseMessage updateBankTransactionWithSchool(String id, BankTransaction updatedTransaction, String schoolId) {
        Optional transactionOpt = this.bankTransactionRepository.findById((Object)id);
        if (transactionOpt.isPresent() && ((BankTransaction)transactionOpt.get()).getSchoolId().equals(schoolId)) {
            updatedTransaction.setId(id);
            updatedTransaction.setSchoolId(schoolId);
            BankTransaction savedTransaction = (BankTransaction)this.bankTransactionRepository.save((Object)updatedTransaction);
            return new ResponseMessage("Bank transaction updated successfully", (Object)savedTransaction);
        }
        return new ResponseMessage("Bank transaction not found or does not belong to this school", null);
    }

    public ResponseMessage deleteBankTransactionWithSchool(String id, String schoolId) {
        Optional transactionOpt = this.bankTransactionRepository.findById((Object)id);
        if (transactionOpt.isPresent() && ((BankTransaction)transactionOpt.get()).getSchoolId().equals(schoolId)) {
            this.bankTransactionRepository.deleteById((Object)id);
            return new ResponseMessage("Bank transaction deleted successfully", null);
        }
        return new ResponseMessage("Bank transaction not found or does not belong to this school", null);
    }

    public ResponseMessage getBankTransactionsByMonthWithSchool(int year, int month, String schoolId) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        List transactions = this.bankTransactionRepository.findByDateBetweenAndSchoolId(startDate, endDate, schoolId);
        return new ResponseMessage("Fetched bank transactions for " + year + "-" + month + " for school", (Object)transactions);
    }
}

