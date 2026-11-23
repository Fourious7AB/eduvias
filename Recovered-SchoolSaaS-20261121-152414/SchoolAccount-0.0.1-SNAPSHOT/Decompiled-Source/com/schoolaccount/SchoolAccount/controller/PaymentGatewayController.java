/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.databind.ObjectMapper
 *  com.razorpay.Utils
 *  com.schoolaccount.SchoolAccount.config.PaymentConfig
 *  com.schoolaccount.SchoolAccount.controller.PaymentGatewayController
 *  com.schoolaccount.SchoolAccount.model.ResponseMessage
 *  com.schoolaccount.SchoolAccount.service.PaymentGatewayService
 *  javax.xml.bind.DatatypeConverter
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.beans.factory.annotation.Value
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
import com.razorpay.Utils;
import com.schoolaccount.SchoolAccount.config.PaymentConfig;
import com.schoolaccount.SchoolAccount.model.ResponseMessage;
import com.schoolaccount.SchoolAccount.service.PaymentGatewayService;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins={"*"})
@RequestMapping(value={"/payment"})
public class PaymentGatewayController {
    @Autowired
    private PaymentGatewayService paymentGatewayService;
    @Autowired
    private PaymentConfig paymentConfig;
    @Autowired
    private ObjectMapper objectMapper;
    @Value(value="${razorpay.key.secret}")
    private String razorpayKeySecret;

    @PostMapping(value={"/initialize"})
    public ResponseEntity<ResponseMessage> initializePayment(@RequestBody Map<String, Object> request) {
        String admissionNumber = (String)request.get("admissionNumber");
        String schoolId = (String)request.get("schoolId");
        String paymentType = (String)request.get("paymentType");
        double amount = Double.parseDouble(request.get("amount").toString());
        return ResponseEntity.ok((Object)this.paymentGatewayService.initializePayment(admissionNumber, schoolId, paymentType, amount));
    }

    @PostMapping(value={"/webhook"})
    public ResponseEntity<ResponseMessage> handleWebhook(@RequestBody Map<String, Object> webhookData, @RequestHeader(value="X-Razorpay-Signature", required=false) String signature) {
        try {
            String payload;
            boolean isValidSignature;
            if (signature != null && !signature.isEmpty() && !(isValidSignature = this.verifyWebhookSignature(payload = this.objectMapper.writeValueAsString(webhookData), signature, this.razorpayKeySecret))) {
                return ResponseEntity.status((int)401).body((Object)new ResponseMessage("Invalid signature", null));
            }
            return ResponseEntity.ok((Object)this.paymentGatewayService.handlePaymentWebhook(webhookData));
        }
        catch (Exception e) {
            return ResponseEntity.status((int)500).body((Object)new ResponseMessage("Error processing webhook: " + e.getMessage(), null));
        }
    }

    @PostMapping(value={"/verify"})
    public ResponseEntity<ResponseMessage> verifyPayment(@RequestBody Map<String, String> request) {
        try {
            String orderId = request.get("razorpay_order_id");
            String paymentId = request.get("razorpay_payment_id");
            String signature = request.get("razorpay_signature");
            String data = orderId + "|" + paymentId;
            boolean isValidSignature = this.verifySignature(data, signature, this.razorpayKeySecret);
            if (isValidSignature) {
                return ResponseEntity.ok((Object)new ResponseMessage("Payment verified successfully", (Object)true));
            }
            return ResponseEntity.status((int)400).body((Object)new ResponseMessage("Invalid payment signature", (Object)false));
        }
        catch (Exception e) {
            return ResponseEntity.status((int)500).body((Object)new ResponseMessage("Error verifying payment: " + e.getMessage(), (Object)false));
        }
    }

    private boolean verifyWebhookSignature(String payload, String signature, String secret) {
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secretKey);
            String computedSignature = DatatypeConverter.printHexBinary((byte[])sha256_HMAC.doFinal(payload.getBytes())).toLowerCase();
            return computedSignature.equals(signature.toLowerCase());
        }
        catch (Exception e) {
            return false;
        }
    }

    private boolean verifySignature(String data, String signature, String secret) {
        try {
            return Utils.verifySignature((String)data, (String)signature, (String)secret);
        }
        catch (Exception e) {
            return false;
        }
    }
}

