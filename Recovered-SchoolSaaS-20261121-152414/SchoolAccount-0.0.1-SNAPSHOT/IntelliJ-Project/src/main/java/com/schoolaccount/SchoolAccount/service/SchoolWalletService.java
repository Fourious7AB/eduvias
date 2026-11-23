/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.schoolaccount.SchoolAccount.model.PaymentTransaction
 *  com.schoolaccount.SchoolAccount.model.PayoutRequest
 *  com.schoolaccount.SchoolAccount.model.ResponseMessage
 *  com.schoolaccount.SchoolAccount.model.SchoolWallet
 *  com.schoolaccount.SchoolAccount.model.SchoolWallet$SubWallet
 *  com.schoolaccount.SchoolAccount.repo.PaymentTransactionRepository
 *  com.schoolaccount.SchoolAccount.repo.PayoutRequestRepository
 *  com.schoolaccount.SchoolAccount.repo.SchoolWalletRepository
 *  com.schoolaccount.SchoolAccount.service.SchoolWalletService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 */
package com.schoolaccount.SchoolAccount.service;

import com.schoolaccount.SchoolAccount.model.PaymentTransaction;
import com.schoolaccount.SchoolAccount.model.PayoutRequest;
import com.schoolaccount.SchoolAccount.model.ResponseMessage;
import com.schoolaccount.SchoolAccount.model.SchoolWallet;
import com.schoolaccount.SchoolAccount.repo.PaymentTransactionRepository;
import com.schoolaccount.SchoolAccount.repo.PayoutRequestRepository;
import com.schoolaccount.SchoolAccount.repo.SchoolWalletRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SchoolWalletService {
    @Autowired
    private SchoolWalletRepository schoolWalletRepository;
    @Autowired
    private PaymentTransactionRepository paymentTransactionRepository;
    @Autowired
    private PayoutRequestRepository payoutRequestRepository;

    public ResponseMessage getOrCreateSchoolWallet(String schoolId, String schoolName, String accountType) {
        Optional walletOpt = this.schoolWalletRepository.findBySchoolId(schoolId);
        if (walletOpt.isPresent()) {
            return new ResponseMessage("School wallet found", walletOpt.get());
        }
        SchoolWallet newWallet = new SchoolWallet();
        newWallet.setSchoolId(schoolId);
        newWallet.setSchoolName(schoolName);
        newWallet.setBalance(0.0);
        newWallet.setCreatedAt(LocalDateTime.now());
        newWallet.setUpdatedAt(LocalDateTime.now());
        newWallet.setAccountType(accountType != null ? accountType : "PRIMARY");
        SchoolWallet savedWallet = (SchoolWallet)this.schoolWalletRepository.save((Object)newWallet);
        return new ResponseMessage("School wallet created successfully", (Object)savedWallet);
    }

    public ResponseMessage getWalletBalance(String schoolId) {
        Optional walletOpt = this.schoolWalletRepository.findBySchoolId(schoolId);
        if (walletOpt.isPresent()) {
            SchoolWallet wallet = (SchoolWallet)walletOpt.get();
            return new ResponseMessage("Wallet balance retrieved successfully", (Object)wallet.getBalance());
        }
        return new ResponseMessage("School wallet not found", null);
    }

    public ResponseMessage createSubWallet(String schoolId, String name, String description) {
        Optional walletOpt = this.schoolWalletRepository.findBySchoolId(schoolId);
        if (walletOpt.isPresent()) {
            SchoolWallet wallet = (SchoolWallet)walletOpt.get();
            boolean subWalletExists = wallet.getSubWallets().stream().anyMatch(sw -> sw.getName().equalsIgnoreCase(name));
            if (subWalletExists) {
                return new ResponseMessage("Sub-wallet with this name already exists", null);
            }
            SchoolWallet.SubWallet subWallet = new SchoolWallet.SubWallet();
            subWallet.setId(UUID.randomUUID().toString());
            subWallet.setName(name);
            subWallet.setDescription(description);
            subWallet.setBalance(0.0);
            subWallet.setCreatedAt(LocalDateTime.now());
            wallet.getSubWallets().add(subWallet);
            wallet.setUpdatedAt(LocalDateTime.now());
            this.schoolWalletRepository.save((Object)wallet);
            return new ResponseMessage("Sub-wallet created successfully", (Object)subWallet);
        }
        return new ResponseMessage("School wallet not found", null);
    }

    public ResponseMessage distributePaymentToSchool(String transactionId) {
        Optional transactionOpt = this.paymentTransactionRepository.findById((Object)transactionId);
        if (transactionOpt.isEmpty()) {
            return new ResponseMessage("Transaction not found", null);
        }
        PaymentTransaction transaction = (PaymentTransaction)transactionOpt.get();
        if (transaction.isDistributedToSchool()) {
            return new ResponseMessage("Transaction already distributed to school", null);
        }
        Optional walletOpt = this.schoolWalletRepository.findBySchoolId(transaction.getSchoolId());
        if (walletOpt.isEmpty()) {
            return new ResponseMessage("School wallet not found", null);
        }
        SchoolWallet wallet = (SchoolWallet)walletOpt.get();
        wallet.setBalance(wallet.getBalance() + transaction.getAmount());
        wallet.setUpdatedAt(LocalDateTime.now());
        if (transaction.getSubWalletId() != null && !transaction.getSubWalletId().isEmpty()) {
            wallet.getSubWallets().stream().filter(sw -> sw.getId().equals(transaction.getSubWalletId())).findFirst().ifPresent(sw -> sw.setBalance(sw.getBalance() + transaction.getAmount()));
        }
        transaction.setDistributedToSchool(true);
        transaction.setDistributionDate(LocalDateTime.now());
        transaction.setUpdatedAt(LocalDateTime.now());
        this.schoolWalletRepository.save((Object)wallet);
        this.paymentTransactionRepository.save((Object)transaction);
        return new ResponseMessage("Payment distributed to school wallet successfully", (Object)transaction);
    }

    public ResponseMessage requestPayout(PayoutRequest payoutRequest) {
        Optional walletOpt = this.schoolWalletRepository.findBySchoolId(payoutRequest.getSchoolId());
        if (walletOpt.isEmpty()) {
            return new ResponseMessage("School wallet not found", null);
        }
        SchoolWallet wallet = (SchoolWallet)walletOpt.get();
        if (wallet.getBalance() < payoutRequest.getAmount()) {
            return new ResponseMessage("Insufficient balance in wallet", null);
        }
        payoutRequest.setStatus("PENDING");
        payoutRequest.setRequestedAt(LocalDateTime.now());
        PayoutRequest savedRequest = (PayoutRequest)this.payoutRequestRepository.save((Object)payoutRequest);
        return new ResponseMessage("Payout request submitted successfully", (Object)savedRequest);
    }

    public ResponseMessage processPayout(String payoutRequestId, String status, String adminUserId) {
        Optional walletOpt;
        Optional requestOpt = this.payoutRequestRepository.findById((Object)payoutRequestId);
        if (requestOpt.isEmpty()) {
            return new ResponseMessage("Payout request not found", null);
        }
        PayoutRequest request = (PayoutRequest)requestOpt.get();
        if (!request.getStatus().equals("PENDING")) {
            return new ResponseMessage("Payout request already processed", null);
        }
        request.setStatus(status);
        request.setProcessedAt(LocalDateTime.now());
        request.setProcessedBy(adminUserId);
        if ((status.equals("APPROVED") || status.equals("COMPLETED")) && (walletOpt = this.schoolWalletRepository.findBySchoolId(request.getSchoolId())).isPresent()) {
            SchoolWallet wallet = (SchoolWallet)walletOpt.get();
            if (wallet.getBalance() < request.getAmount()) {
                return new ResponseMessage("Insufficient balance in wallet", null);
            }
            wallet.setBalance(wallet.getBalance() - request.getAmount());
            wallet.setUpdatedAt(LocalDateTime.now());
            this.schoolWalletRepository.save((Object)wallet);
        }
        PayoutRequest savedRequest = (PayoutRequest)this.payoutRequestRepository.save((Object)request);
        return new ResponseMessage("Payout request processed successfully", (Object)savedRequest);
    }

    public ResponseMessage getTransactionHistory(String schoolId) {
        List transactions = this.paymentTransactionRepository.findBySchoolId(schoolId);
        return new ResponseMessage("Transaction history retrieved successfully", (Object)transactions);
    }

    public ResponseMessage getPayoutRequests(String schoolId) {
        List requests = this.payoutRequestRepository.findBySchoolId(schoolId);
        return new ResponseMessage("Payout requests retrieved successfully", (Object)requests);
    }
}

