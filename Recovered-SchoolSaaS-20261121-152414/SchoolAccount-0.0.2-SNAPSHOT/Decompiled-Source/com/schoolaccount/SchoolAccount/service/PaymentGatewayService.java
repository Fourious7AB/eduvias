/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.razorpay.Order
 *  com.razorpay.RazorpayClient
 *  com.razorpay.RazorpayException
 *  com.schoolaccount.SchoolAccount.model.PaymentTransaction
 *  com.schoolaccount.SchoolAccount.model.ResponseMessage
 *  com.schoolaccount.SchoolAccount.model.StudentAdmissionFee
 *  com.schoolaccount.SchoolAccount.model.StudentAdmissionFee$TuitionFeePayment
 *  com.schoolaccount.SchoolAccount.repo.PaymentTransactionRepository
 *  com.schoolaccount.SchoolAccount.repo.StudentAdmissionFeeRepository
 *  com.schoolaccount.SchoolAccount.service.PaymentGatewayService
 *  com.schoolaccount.SchoolAccount.service.SchoolWalletService
 *  com.schoolaccount.SchoolAccount.service.StudentAdmissionFeeService
 *  org.json.JSONObject
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.beans.factory.annotation.Value
 *  org.springframework.stereotype.Service
 */
package com.schoolaccount.SchoolAccount.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.schoolaccount.SchoolAccount.model.PaymentTransaction;
import com.schoolaccount.SchoolAccount.model.ResponseMessage;
import com.schoolaccount.SchoolAccount.model.StudentAdmissionFee;
import com.schoolaccount.SchoolAccount.repo.PaymentTransactionRepository;
import com.schoolaccount.SchoolAccount.repo.StudentAdmissionFeeRepository;
import com.schoolaccount.SchoolAccount.service.SchoolWalletService;
import com.schoolaccount.SchoolAccount.service.StudentAdmissionFeeService;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentGatewayService {
    @Autowired
    private PaymentTransactionRepository paymentTransactionRepository;
    @Autowired
    private StudentAdmissionFeeRepository studentAdmissionFeeRepository;
    @Autowired
    private SchoolWalletService schoolWalletService;
    @Autowired
    private StudentAdmissionFeeService studentAdmissionFeeService;
    @Value(value="${razorpay.key.id}")
    private String keyId;
    @Value(value="${razorpay.key.secret}")
    private String keySecret;

    public ResponseMessage initializePayment(String admissionNumber, String schoolId, String paymentType, double amount) {
        Optional studentFeeOpt = this.studentAdmissionFeeRepository.findByAdmissionNumberAndSchoolId(admissionNumber, schoolId);
        if (studentFeeOpt.isEmpty()) {
            return new ResponseMessage("Student not found", null);
        }
        StudentAdmissionFee studentFee = (StudentAdmissionFee)studentFeeOpt.get();
        PaymentTransaction transaction = new PaymentTransaction();
        transaction.setTransactionId(this.generateTransactionId());
        transaction.setStudentId(studentFee.getId());
        transaction.setAdmissionNumber(admissionNumber);
        transaction.setSchoolId(schoolId);
        transaction.setPaymentType(paymentType);
        transaction.setAmount(amount);
        transaction.setCurrency("INR");
        transaction.setStatus("PENDING");
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setUpdatedAt(LocalDateTime.now());
        transaction.setDescription("Payment for " + paymentType);
        transaction.setDistributedToSchool(false);
        PaymentTransaction savedTransaction = (PaymentTransaction)this.paymentTransactionRepository.save((Object)transaction);
        Map paymentGatewayResponse = this.createRazorpayOrder(savedTransaction, studentFee);
        return new ResponseMessage("Payment initialized successfully", (Object)paymentGatewayResponse);
    }

    public ResponseMessage handlePaymentWebhook(Map<String, Object> webhookData) {
        String transactionId = (String)webhookData.get("transactionId");
        String status = (String)webhookData.get("status");
        Optional transactionOpt = this.paymentTransactionRepository.findById((Object)transactionId);
        if (transactionOpt.isEmpty()) {
            return new ResponseMessage("Transaction not found", null);
        }
        PaymentTransaction transaction = (PaymentTransaction)transactionOpt.get();
        transaction.setStatus(status);
        transaction.setUpdatedAt(LocalDateTime.now());
        transaction.setPaymentMethod((String)webhookData.get("paymentMethod"));
        if (status.equals("COMPLETED")) {
            this.updateStudentFeeRecord(transaction);
            this.schoolWalletService.distributePaymentToSchool(transaction.getId());
        }
        PaymentTransaction savedTransaction = (PaymentTransaction)this.paymentTransactionRepository.save((Object)transaction);
        return new ResponseMessage("Payment webhook processed successfully", (Object)savedTransaction);
    }

    private void updateStudentFeeRecord(PaymentTransaction transaction) {
        Optional studentFeeOpt = this.studentAdmissionFeeRepository.findByAdmissionNumberAndSchoolId(transaction.getAdmissionNumber(), transaction.getSchoolId());
        if (studentFeeOpt.isPresent()) {
            StudentAdmissionFee studentFee = (StudentAdmissionFee)studentFeeOpt.get();
            if (transaction.getPaymentType().equals("ADMISSION_FEE")) {
                studentFee.setAdmissionFees(studentFee.getAdmissionFees() + transaction.getAmount());
            } else if (transaction.getPaymentType().equals("TUITION_FEE")) {
                StudentAdmissionFee.TuitionFeePayment payment = new StudentAdmissionFee.TuitionFeePayment();
                payment.setFee(transaction.getAmount());
                payment.setPaymentType(transaction.getPaymentMethod());
                payment.setTransactionNo(transaction.getTransactionId());
                payment.setCreatedAt(LocalDateTime.now());
                this.studentAdmissionFeeService.addTuitionFeePaymentWithSchool(studentFee.getId(), payment, transaction.getSchoolId());
            }
        }
    }

    private String generateTransactionId() {
        return "TXN" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10).toUpperCase();
    }

    private Map<String, Object> createRazorpayOrder(PaymentTransaction transaction, StudentAdmissionFee studentFee) {
        try {
            int amountInPaise = (int)(transaction.getAmount() * 100.0);
            String receiptId = transaction.getTransactionId();
            RazorpayClient razorpayClient = new RazorpayClient(this.keyId, this.keySecret);
            JSONObject notesObject = new JSONObject();
            notesObject.put("admissionNumber", (Object)transaction.getAdmissionNumber());
            notesObject.put("studentName", (Object)studentFee.getStudentName());
            notesObject.put("className", (Object)studentFee.getClassName());
            notesObject.put("schoolId", (Object)transaction.getSchoolId());
            notesObject.put("paymentType", (Object)transaction.getPaymentType());
            notesObject.put("amount", (Object)String.valueOf(transaction.getAmount()));
            notesObject.put("transactionId", (Object)transaction.getId());
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amountInPaise);
            orderRequest.put("currency", (Object)transaction.getCurrency());
            orderRequest.put("receipt", (Object)receiptId);
            orderRequest.put("notes", (Object)notesObject);
            Order order = razorpayClient.orders.create(orderRequest);
            JSONObject orderJson = new JSONObject(order.toString());
            HashMap<String, Object> orderMap = new HashMap<String, Object>();
            for (String key : orderJson.keySet()) {
                orderMap.put(key, orderJson.get(key));
            }
            HashMap<String, Object> response = new HashMap<String, Object>();
            response.put("transactionId", transaction.getId());
            response.put("gatewayTransactionId", transaction.getTransactionId());
            response.put("amount", transaction.getAmount());
            response.put("currency", transaction.getCurrency());
            response.put("status", "PENDING");
            response.put("orderId", orderMap.get("id"));
            response.put("razorpayKeyId", this.keyId);
            response.put("studentName", studentFee.getStudentName());
            response.put("admissionNumber", transaction.getAdmissionNumber());
            response.put("className", studentFee.getClassName());
            response.put("paymentType", transaction.getPaymentType());
            return response;
        }
        catch (RazorpayException e) {
            HashMap<String, Object> response = new HashMap<String, Object>();
            response.put("transactionId", transaction.getId());
            response.put("gatewayTransactionId", transaction.getTransactionId());
            response.put("amount", transaction.getAmount());
            response.put("currency", transaction.getCurrency());
            response.put("status", "ERROR");
            response.put("error", e.getMessage());
            return response;
        }
    }
}

