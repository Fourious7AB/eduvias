/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.schoolaccount.SchoolAccount.config.PaymentConfig
 *  com.schoolaccount.SchoolAccount.model.PaymentTransaction
 *  com.schoolaccount.SchoolAccount.model.ResponseMessage
 *  com.schoolaccount.SchoolAccount.model.SchoolWallet
 *  com.schoolaccount.SchoolAccount.model.SuperAdminAccount
 *  com.schoolaccount.SchoolAccount.model.SuperAdminAccount$Transaction
 *  com.schoolaccount.SchoolAccount.repo.PaymentTransactionRepository
 *  com.schoolaccount.SchoolAccount.repo.SchoolWalletRepository
 *  com.schoolaccount.SchoolAccount.repo.SuperAdminAccountRepository
 *  com.schoolaccount.SchoolAccount.service.SuperAdminAccountService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 *  org.springframework.transaction.annotation.Transactional
 */
package com.schoolaccount.SchoolAccount.service;

import com.schoolaccount.SchoolAccount.config.PaymentConfig;
import com.schoolaccount.SchoolAccount.model.PaymentTransaction;
import com.schoolaccount.SchoolAccount.model.ResponseMessage;
import com.schoolaccount.SchoolAccount.model.SchoolWallet;
import com.schoolaccount.SchoolAccount.model.SuperAdminAccount;
import com.schoolaccount.SchoolAccount.repo.PaymentTransactionRepository;
import com.schoolaccount.SchoolAccount.repo.SchoolWalletRepository;
import com.schoolaccount.SchoolAccount.repo.SuperAdminAccountRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SuperAdminAccountService {
    @Autowired
    private SuperAdminAccountRepository superAdminAccountRepository;
    @Autowired
    private SchoolWalletRepository schoolWalletRepository;
    @Autowired
    private PaymentTransactionRepository paymentTransactionRepository;
    @Autowired
    private PaymentConfig paymentConfig;

    public SuperAdminAccount initializeSuperAdminAccount() {
        Optional accountOpt = this.superAdminAccountRepository.findFirstByOrderByIdAsc();
        if (accountOpt.isPresent()) {
            return (SuperAdminAccount)accountOpt.get();
        }
        SuperAdminAccount newAccount = new SuperAdminAccount();
        newAccount.setTotalBalance(0.0);
        newAccount.setPendingDistribution(0.0);
        newAccount.setCommissionEarned(0.0);
        newAccount.setLastUpdated(LocalDateTime.now());
        return (SuperAdminAccount)this.superAdminAccountRepository.save(newAccount);
    }

    @Transactional
    public ResponseMessage recordPaymentReceived(PaymentTransaction payment) {
        SuperAdminAccount account = this.initializeSuperAdminAccount();
        double commissionRate = this.paymentConfig.getSuperAdminCommissionPercentage() / 100.0;
        double commissionAmount = payment.getAmount() * commissionRate;
        double schoolAmount = payment.getAmount() - commissionAmount;
        SuperAdminAccount.Transaction transaction = new SuperAdminAccount.Transaction();
        transaction.setTransactionId(payment.getTransactionId());
        transaction.setStudentId(payment.getStudentId());
        transaction.setAdmissionNumber(payment.getAdmissionNumber());
        transaction.setSchoolId(payment.getSchoolId());
        transaction.setSchoolName(payment.getSchoolName());
        transaction.setAmount(payment.getAmount());
        transaction.setCommissionAmount(commissionAmount);
        transaction.setSchoolAmount(schoolAmount);
        transaction.setStatus("RECEIVED");
        transaction.setTimestamp(LocalDateTime.now());
        account.setTotalBalance(account.getTotalBalance() + payment.getAmount());
        account.setPendingDistribution(account.getPendingDistribution() + schoolAmount);
        account.setCommissionEarned(account.getCommissionEarned() + commissionAmount);
        account.getRecentTransactions().add(transaction);
        account.setLastUpdated(LocalDateTime.now());
        this.superAdminAccountRepository.save(account);
        if (this.paymentConfig.isAutoDistribution()) {
            this.distributePaymentToSchool(transaction.getTransactionId());
        }
        return new ResponseMessage("Payment recorded successfully", (Object)transaction);
    }

    @Transactional
    public ResponseMessage distributePaymentToSchool(String transactionId) {
        SchoolWallet wallet;
        SuperAdminAccount account = this.initializeSuperAdminAccount();
        Optional<SuperAdminAccount.Transaction> transactionOpt = account.getRecentTransactions().stream().filter(t -> t.getTransactionId().equals(transactionId) && "RECEIVED".equals(t.getStatus())).findFirst();
        if (transactionOpt.isEmpty()) {
            return new ResponseMessage("Transaction not found or already distributed", null);
        }
        SuperAdminAccount.Transaction transaction = transactionOpt.get();
        Optional walletOpt = this.schoolWalletRepository.findBySchoolId(transaction.getSchoolId());
        if (walletOpt.isPresent()) {
            wallet = (SchoolWallet)walletOpt.get();
        } else {
            wallet = new SchoolWallet();
            wallet.setSchoolId(transaction.getSchoolId());
            wallet.setSchoolName(transaction.getSchoolName());
            wallet.setBalance(0.0);
            wallet.setCreatedAt(LocalDateTime.now());
        }
        wallet.setBalance(wallet.getBalance() + transaction.getSchoolAmount());
        wallet.setUpdatedAt(LocalDateTime.now());
        this.schoolWalletRepository.save(wallet);
        transaction.setStatus("DISTRIBUTED");
        account.setPendingDistribution(account.getPendingDistribution() - transaction.getSchoolAmount());
        account.setLastUpdated(LocalDateTime.now());
        this.superAdminAccountRepository.save(account);
        return new ResponseMessage("Payment distributed to school wallet successfully", (Object)transaction);
    }

    public ResponseMessage getPendingDistributions() {
        List<SuperAdminAccount.Transaction> pendingTransactions = this.superAdminAccountRepository.findPendingDistributions();
        return new ResponseMessage("Pending distributions retrieved successfully", (Object)pendingTransactions);
    }

    public ResponseMessage getAccountSummary() {
        SuperAdminAccount account = this.initializeSuperAdminAccount();
        return new ResponseMessage("Account summary retrieved successfully", (Object)account);
    }

    @Transactional
    public ResponseMessage distributeAllPendingPayments() {
        List<SuperAdminAccount.Transaction> pendingTransactions = this.superAdminAccountRepository.findPendingDistributions();
        int successCount = 0;
        for (SuperAdminAccount.Transaction transaction : pendingTransactions) {
            ResponseMessage result = this.distributePaymentToSchool(transaction.getTransactionId());
            if (result.getData() == null) continue;
            ++successCount;
        }
        return new ResponseMessage("Distributed " + successCount + " out of " + pendingTransactions.size() + " pending payments", (Object)successCount);
    }

    @Transactional
    public ResponseMessage recordRazorpayPayment(Map<String, Object> paymentDetails) {
        try {
            String paymentId = (String)paymentDetails.get("id");
            Map orderDetails = (Map)paymentDetails.get("order");
            Map notes = (Map)orderDetails.get("notes");
            String studentId = (String)notes.get("studentId");
            String admissionNumber = (String)notes.get("admissionNumber");
            String schoolId = (String)notes.get("schoolId");
            String paymentType = (String)notes.get("paymentType");
            int amountInPaise = (Integer)paymentDetails.get("amount");
            double amount = (double)amountInPaise / 100.0;
            PaymentTransaction transaction = new PaymentTransaction();
            transaction.setTransactionId(paymentId);
            transaction.setStudentId(studentId);
            transaction.setAdmissionNumber(admissionNumber);
            transaction.setSchoolId(schoolId);
            transaction.setSchoolName(this.getSchoolName(schoolId));
            transaction.setPaymentType(paymentType);
            transaction.setAmount(amount);
            transaction.setCurrency("INR");
            transaction.setStatus("COMPLETED");
            transaction.setPaymentMethod("RAZORPAY");
            transaction.setCreatedAt(LocalDateTime.now());
            transaction.setUpdatedAt(LocalDateTime.now());
            transaction.setDescription("Payment via Razorpay for " + paymentType);
            transaction.setDistributedToSchool(false);
            this.paymentTransactionRepository.save(transaction);
            return this.recordPaymentReceived(transaction);
        }
        catch (Exception e) {
            return new ResponseMessage("Error recording Razorpay payment: " + e.getMessage(), null);
        }
    }

    private String getSchoolName(String schoolId) {
        Optional<SchoolWallet> walletOpt = this.schoolWalletRepository.findBySchoolId(schoolId);
        return walletOpt.map(SchoolWallet::getSchoolName).orElse("Unknown School");
    }

    @Transactional
    private SchoolWallet ensureSchoolWalletExists(String schoolId, String schoolName) {
        Optional<SchoolWallet> walletOpt = this.schoolWalletRepository.findBySchoolId(schoolId);
        if (walletOpt.isPresent()) {
            return (SchoolWallet)walletOpt.get();
        }
        String accountType = this.determineSchoolAccountType(schoolId);
        SchoolWallet newWallet = new SchoolWallet();
        newWallet.setSchoolId(schoolId);
        newWallet.setSchoolName((String)(schoolName != null && !schoolName.isEmpty() ? schoolName : "School " + schoolId));
        newWallet.setBalance(0.0);
        newWallet.setCreatedAt(LocalDateTime.now());
        newWallet.setUpdatedAt(LocalDateTime.now());
        newWallet.setAccountType(accountType);
        return (SchoolWallet)this.schoolWalletRepository.save(newWallet);
    }

    private String determineSchoolAccountType(String schoolId) {
        return "PRIMARY";
    }
}

