/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.databind.ObjectMapper
 *  com.razorpay.Order
 *  com.razorpay.RazorpayClient
 *  com.razorpay.RazorpayException
 *  com.schoolaccount.SchoolAccount.controller.SuperAdminAccountController
 *  com.schoolaccount.SchoolAccount.model.ResponseMessage
 *  com.schoolaccount.SchoolAccount.service.SuperAdminAccountService
 *  javax.xml.bind.DatatypeConverter
 *  org.apache.commons.codec.binary.Hex
 *  org.json.JSONObject
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.beans.factory.annotation.Value
 *  org.springframework.http.ResponseEntity
 *  org.springframework.web.bind.annotation.CrossOrigin
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestHeader
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.schoolaccount.SchoolAccount.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.schoolaccount.SchoolAccount.model.ResponseMessage;
import com.schoolaccount.SchoolAccount.service.SuperAdminAccountService;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import org.apache.commons.codec.binary.Hex;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins={"*"})
@RequestMapping(value={"/super-admin"})
public class SuperAdminAccountController {
    @Autowired
    private SuperAdminAccountService superAdminAccountService;
    @Value(value="${razorpay.key.id}")
    private String razorpayKeyId;
    @Value(value="${razorpay.key.secret}")
    private String razorpayKeySecret;

    @GetMapping(value={"/account"})
    public ResponseEntity<ResponseMessage> getAccountSummary() {
        return ResponseEntity.ok((Object)this.superAdminAccountService.getAccountSummary());
    }

    @GetMapping(value={"/pending-distributions"})
    public ResponseEntity<ResponseMessage> getPendingDistributions() {
        return ResponseEntity.ok((Object)this.superAdminAccountService.getPendingDistributions());
    }

    @PostMapping(value={"/distribute/{transactionId}"})
    public ResponseEntity<ResponseMessage> distributePayment(@PathVariable String transactionId) {
        return ResponseEntity.ok((Object)this.superAdminAccountService.distributePaymentToSchool(transactionId));
    }

    @PostMapping(value={"/distribute-all"})
    public ResponseEntity<ResponseMessage> distributeAllPendingPayments() {
        return ResponseEntity.ok((Object)this.superAdminAccountService.distributeAllPendingPayments());
    }

    @PostMapping(value={"/razorpay/create-order"})
    public ResponseEntity<ResponseMessage> createRazorpayOrder(@RequestBody Map<String, Object> request) {
        try {
            double amount = Double.parseDouble(request.get("amount").toString());
            String currency = (String)request.get("currency");
            String receipt = (String)request.get("receipt");
            String notes = (String)request.get("notes");
            String studentId = (String)request.get("studentId");
            String admissionNumber = (String)request.get("admissionNumber");
            String schoolId = (String)request.get("schoolId");
            String paymentType = (String)request.get("paymentType");
            RazorpayClient razorpayClient = new RazorpayClient(this.razorpayKeyId, this.razorpayKeySecret);
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", (int)(amount * 100.0));
            orderRequest.put("currency", (Object)currency);
            orderRequest.put("receipt", (Object)receipt);
            JSONObject notesObject = new JSONObject();
            notesObject.put("studentId", (Object)studentId);
            notesObject.put("admissionNumber", (Object)admissionNumber);
            notesObject.put("schoolId", (Object)schoolId);
            notesObject.put("paymentType", (Object)paymentType);
            notesObject.put("notes", (Object)notes);
            orderRequest.put("notes", (Object)notesObject);
            Order order = razorpayClient.orders.create(orderRequest);
            String generatedPaymentId = "pay_" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 14);
            HashMap<String, Object> response = new HashMap<String, Object>();
            response.put("orderId", order.get("id"));
            response.put("amount", order.get("amount"));
            response.put("currency", order.get("currency"));
            response.put("paymentId", generatedPaymentId);
            response.put("razorpayKeyId", this.razorpayKeyId);
            return ResponseEntity.ok((Object)new ResponseMessage("Razorpay order created successfully", response));
        }
        catch (RazorpayException e) {
            return ResponseEntity.badRequest().body((Object)new ResponseMessage("Error creating Razorpay order: " + e.getMessage(), null));
        }
    }

    @PostMapping(value={"/razorpay/verify-signature"})
    public ResponseEntity<ResponseMessage> verifyRazorpaySignature(@RequestBody Map<String, String> request) {
        try {
            String orderId = request.get("razorpay_order_id");
            String paymentId = request.get("razorpay_payment_id");
            String signature = request.get("razorpay_signature");
            System.out.println(" i m signature : " + signature);
            JSONObject jsonObject = new JSONObject();
            String data = orderId + "|" + paymentId;
            boolean isValidSignature = this.verifySignature(data, signature, this.razorpayKeySecret);
            if (isValidSignature) {
                HashMap<String, String> paymentDetails = new HashMap<String, String>();
                paymentDetails.put("orderId", orderId);
                paymentDetails.put("paymentId", paymentId);
                this.superAdminAccountService.recordRazorpayPayment(paymentDetails);
                return ResponseEntity.ok((Object)new ResponseMessage("Payment verified successfully", paymentDetails));
            }
            return ResponseEntity.badRequest().body((Object)new ResponseMessage("Invalid payment signature", null));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body((Object)new ResponseMessage("Error verifying payment: " + e.getMessage(), null));
        }
    }

    @PostMapping(value={"/razorpay/webhook"})
    public ResponseEntity<ResponseMessage> handleRazorpayWebhook(@RequestBody Map<String, Object> webhookData, @RequestHeader(value="X-Razorpay-Signature") String signature) {
        try {
            String payload = new ObjectMapper().writeValueAsString(webhookData);
            System.out.println(" calling from webhook: " + webhookData);
            boolean isValidSignature = this.verifyWebhookSignature(payload, signature, this.razorpayKeySecret);
            if (isValidSignature) {
                String event = (String)webhookData.get("event");
                if ("payment.authorized".equals(event)) {
                    Map paymentEntity = (Map)((Map)webhookData.get("payload")).get("payment");
                    String paymentId = (String)paymentEntity.get("id");
                    Map orderEntity = (Map)paymentEntity.get("order");
                    String orderId = (String)orderEntity.get("id");
                    Map notes = (Map)orderEntity.get("notes");
                    this.superAdminAccountService.recordRazorpayPayment(paymentEntity);
                    return ResponseEntity.ok((Object)new ResponseMessage("Webhook processed successfully", (Object)paymentId));
                }
                return ResponseEntity.ok((Object)new ResponseMessage("Webhook received but no action taken for event: " + event, null));
            }
            return ResponseEntity.badRequest().body((Object)new ResponseMessage("Invalid webhook signature", null));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body((Object)new ResponseMessage("Error processing webhook: " + e.getMessage(), null));
        }
    }

    private boolean verifySignature(String data, String expectedSignature, String secret) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(keySpec);
            byte[] hashBytes = mac.doFinal(data.getBytes());
            String generatedSignature = Hex.encodeHexString((byte[])hashBytes);
            System.out.println("i m signature in method : " + generatedSignature);
            return generatedSignature.equals(expectedSignature);
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean verifyWebhookSignature(String payload, String signature, String secret) throws SignatureException {
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secretKey);
            String computedSignature = DatatypeConverter.printHexBinary((byte[])sha256_HMAC.doFinal(payload.getBytes())).toLowerCase();
            return computedSignature.equals(signature.toLowerCase());
        }
        catch (Exception e) {
            throw new SignatureException("Failed to verify webhook signature: " + e.getMessage());
        }
    }
}

