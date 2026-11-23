/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.databind.ObjectMapper
 *  com.schoolaccount.SchoolAccount.config.PaymentConfig
 *  com.schoolaccount.SchoolAccount.controller.PaymentController
 *  com.schoolaccount.SchoolAccount.model.ResponseMessage
 *  com.schoolaccount.SchoolAccount.service.StudentAdmissionFeeService
 *  javax.xml.bind.DatatypeConverter
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.http.ResponseEntity
 *  org.springframework.web.bind.annotation.CrossOrigin
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestHeader
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.schoolaccount.SchoolAccount.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.schoolaccount.SchoolAccount.config.PaymentConfig;
import com.schoolaccount.SchoolAccount.model.ResponseMessage;
import com.schoolaccount.SchoolAccount.service.StudentAdmissionFeeService;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins={"*"})
@RequestMapping(value={"/api/payments"})
public class PaymentController {
    @Autowired
    private StudentAdmissionFeeService studentAdmissionFeeService;
    @Autowired
    private PaymentConfig paymentConfig;
    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping(value={"/generate-link"})
    public ResponseEntity<ResponseMessage> generatePaymentLink(@RequestBody Map<String, Object> request) {
        String admissionNumber = (String)request.get("admissionNumber");
        String schoolId = (String)request.get("schoolId");
        String paymentType = (String)request.get("paymentType");
        double amount = Double.parseDouble(request.get("amount").toString());
        System.out.println(" amount : " + amount);
        return ResponseEntity.ok((Object)this.studentAdmissionFeeService.generatePaymentLink(admissionNumber, schoolId, paymentType, amount));
    }

    @PostMapping(value={"/callback"})
    public ResponseEntity<ResponseMessage> processPaymentCallback(@RequestBody Map<String, Object> paymentDetails) {
        return ResponseEntity.ok((Object)this.studentAdmissionFeeService.processRazorpayPayment(paymentDetails));
    }

    @PostMapping(value={"/verify"})
    public ResponseEntity<ResponseMessage> verifyPayment(@RequestBody Map<String, String> request) {
        String orderId = request.get("razorpay_order_id");
        String paymentId = request.get("razorpay_payment_id");
        String signature = request.get("razorpay_signature");
        return ResponseEntity.ok((Object)this.studentAdmissionFeeService.verifyRazorpayPayment(orderId, paymentId, signature));
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @PostMapping(value={"/webhook"})
    public ResponseEntity<String> handleWebhook(@RequestBody Map<String, Object> webhookData, @RequestHeader(value="X-Razorpay-Signature") String signature) {
        try {
            Map order;
            String webhookSecret = this.paymentConfig.getWebhookSecret();
            String payload = this.objectMapper.writeValueAsString(webhookData);
            boolean isValidSignature = this.verifyWebhookSignature(payload, signature, webhookSecret);
            if (!isValidSignature) {
                return ResponseEntity.status((int)401).body((Object)"Invalid signature");
            }
            String event = (String)webhookData.get("event");
            if (!"payment.authorized".equals(event)) return ResponseEntity.ok((Object)"Webhook processed successfully");
            Map payloadMap = (Map)webhookData.get("payload");
            if (payloadMap == null) return ResponseEntity.status((int)400).body((Object)"Payload data not found");
            Map paymentMap = (Map)payloadMap.get("payment");
            Map payment = paymentMap != null ? (Map)paymentMap.get("entity") : null;
            Map orderMap = (Map)payloadMap.get("order");
            Map map = order = orderMap != null ? (Map)orderMap.get("entity") : null;
            if (payment == null || order == null) return ResponseEntity.status((int)400).body((Object)"Payment or order data not found");
            Map notes = (Map)order.get("notes");
            if (notes == null) return ResponseEntity.status((int)400).body((Object)"Order notes not found");
            String admissionNumber = (String)notes.get("admissionNumber");
            String schoolId = (String)notes.get("schoolId");
            String paymentType = (String)notes.get("paymentType");
            String amountStr = (String)notes.get("amount");
            if (admissionNumber == null || schoolId == null || paymentType == null || amountStr == null) return ResponseEntity.status((int)400).body((Object)"Missing required payment information in notes");
            double d = Double.parseDouble(amountStr);
            return ResponseEntity.ok((Object)"Webhook processed successfully");
        }
        catch (Exception e) {
            return ResponseEntity.status((int)500).body((Object)("Error processing webhook: " + e.getMessage()));
        }
    }

    private boolean verifyWebhookSignature(String payload, String signature, String secret) {
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            System.out.println(" i m secret key : " + secretKey);
            sha256_HMAC.init(secretKey);
            String computedSignature = DatatypeConverter.printHexBinary((byte[])sha256_HMAC.doFinal(payload.getBytes())).toLowerCase();
            return computedSignature.equals(signature.toLowerCase());
        }
        catch (Exception e) {
            return false;
        }
    }
}

