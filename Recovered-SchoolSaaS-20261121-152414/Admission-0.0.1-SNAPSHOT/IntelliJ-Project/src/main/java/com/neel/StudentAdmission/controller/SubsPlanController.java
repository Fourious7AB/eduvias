/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.StudentAdmission.controller.SubsPlanController
 *  com.neel.StudentAdmission.dto.PaymentRequestDto
 *  com.neel.StudentAdmission.dto.PaymentVerificationRequest
 *  com.neel.StudentAdmission.model.PaymentDetails
 *  com.neel.StudentAdmission.model.PaymentHistory
 *  com.neel.StudentAdmission.model.ResponseModel
 *  com.neel.StudentAdmission.model.SignUp
 *  com.neel.StudentAdmission.model.SubscriptionPlan
 *  com.neel.StudentAdmission.repo.PaymentHistoryRepo
 *  com.neel.StudentAdmission.service.SignUpService
 *  com.neel.StudentAdmission.service.SubsPlanService
 *  com.razorpay.Order
 *  com.razorpay.RazorpayClient
 *  org.json.JSONObject
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.beans.factory.annotation.Value
 *  org.springframework.http.HttpStatus
 *  org.springframework.http.HttpStatusCode
 *  org.springframework.http.ResponseEntity
 *  org.springframework.web.bind.annotation.CrossOrigin
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.neel.StudentAdmission.controller;

import com.neel.StudentAdmission.dto.PaymentRequestDto;
import com.neel.StudentAdmission.dto.PaymentVerificationRequest;
import com.neel.StudentAdmission.model.PaymentDetails;
import com.neel.StudentAdmission.model.PaymentHistory;
import com.neel.StudentAdmission.model.ResponseModel;
import com.neel.StudentAdmission.model.SignUp;
import com.neel.StudentAdmission.model.SubscriptionPlan;
import com.neel.StudentAdmission.repo.PaymentHistoryRepo;
import com.neel.StudentAdmission.service.SignUpService;
import com.neel.StudentAdmission.service.SubsPlanService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins={"*"})
@RequestMapping(value={"/api/v1/subscription"})
public class SubsPlanController {
    @Autowired
    private SubsPlanService subscriptionService;
    @Autowired
    private SignUpService signUpService;
    @Autowired
    private PaymentHistoryRepo paymentHistoryRepo;
    @Value(value="${RAZORPAY_KEY_ID}")
    private String accessKey;
    @Value(value="${RAZORPAY_KEY_SECRET}")
    private String secretKey;

    @PostMapping(value={"/create-Payment/{schoolId}"})
    public ResponseEntity<?> createSubscription(@PathVariable String schoolId, @RequestBody PaymentRequestDto requestDto) {
        try {
            RazorpayClient client = new RazorpayClient(this.accessKey, this.secretKey);
            long amountInPaise = (long)(requestDto.getAmount() * 100.0);
            System.out.println(" i m from planid :" + requestDto.getPlanId());
            System.out.println(" i m from schoolId  acs :" + requestDto.getSchoolId());
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amountInPaise);
            orderRequest.put("currency", (Object)"INR");
            orderRequest.put("receipt", (Object)("txn_" + System.currentTimeMillis()));
            Order order = client.orders.create(orderRequest);
            SubscriptionPlan subscription = this.subscriptionService.createSubscription(requestDto.getPlanId(), schoolId);
            System.out.println(" i m from school id : " + schoolId);
            PaymentDetails paymentDetails = new PaymentDetails();
            paymentDetails.setPaymentId("pay_" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 14));
            paymentDetails.setOrderId((String)order.get("id"));
            paymentDetails.setAmount(requestDto.getAmount());
            paymentDetails.setCurrency("INR");
            paymentDetails.setStatus("Failed");
            subscription.setPaymentDetails(paymentDetails);
            subscription = this.subscriptionService.updatePayments(subscription);
            JSONObject response = new JSONObject();
            response.put("order", (Object)new JSONObject(order.toString()));
            response.put("subscription", (Object)subscription);
            return ResponseEntity.ok(response.toString());
        }
        catch (Exception e) {
            return ResponseEntity.status((int)500).body(("Error creating subscription: " + e.getMessage()));
        }
    }

    @GetMapping(value={"/all/payments"})
    public ResponseEntity<ResponseModel> allSubscriptionPayments() {
        List allPayments = this.subscriptionService.allSubscriptionPayment();
        if (allPayments.isEmpty()) {
            return new ResponseEntity((Object)new ResponseModel(Boolean.valueOf(false), "Subscription payment not available in database.", null), (HttpStatusCode)HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity((Object)new ResponseModel(Boolean.valueOf(false), "All subscription payment retrieved successfully.", (Object)allPayments), (HttpStatusCode)HttpStatus.OK);
    }

    @GetMapping(value={"/all/payments/{schoolId}"})
    public ResponseEntity<ResponseModel> allSubscriptionPaymentsHistoryBySchool(@PathVariable String schoolId) {
        List allPayments = this.subscriptionService.getAllPlanHistoryBySchool(schoolId);
        if (allPayments.isEmpty()) {
            return new ResponseEntity((Object)new ResponseModel(Boolean.valueOf(false), "Subscription payment history not available in database.", null), (HttpStatusCode)HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity((Object)new ResponseModel(Boolean.valueOf(true), "All subscription payment history retrieved successfully.", (Object)allPayments), (HttpStatusCode)HttpStatus.OK);
    }

    @PostMapping(value={"/renew/{schoolId}"})
    public ResponseEntity<?> renewSubscription(@PathVariable String schoolId, @RequestBody PaymentRequestDto requestDto) {
        try {
            SubscriptionPlan renewedSubscription = this.subscriptionService.renewSubscription(requestDto.getPlanId(), schoolId);
            RazorpayClient client = new RazorpayClient(this.accessKey, this.secretKey);
            long amountInPaise = (long)(requestDto.getAmount() * 100.0);
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amountInPaise);
            orderRequest.put("currency", (Object)"INR");
            orderRequest.put("receipt", (Object)("txn_" + System.currentTimeMillis()));
            Order order = client.orders.create(orderRequest);
            PaymentDetails paymentDetails = new PaymentDetails();
            paymentDetails.setPaymentId("pay_" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 14));
            paymentDetails.setOrderId((String)order.get("id"));
            paymentDetails.setAmount(requestDto.getAmount());
            paymentDetails.setCurrency("INR");
            renewedSubscription.setPaymentDetails(paymentDetails);
            renewedSubscription = this.subscriptionService.updatePayments(renewedSubscription);
            JSONObject response = new JSONObject();
            response.put("order", (Object)new JSONObject(order.toString()));
            response.put("subscription", (Object)renewedSubscription);
            return ResponseEntity.ok(response.toString());
        }
        catch (Exception e) {
            return ResponseEntity.status((int)500).body(("Error renewing subscription: " + e.getMessage()));
        }
    }

    @GetMapping(value={"/view-plan/{id}"})
    public ResponseEntity<ResponseModel> viewPlanBySchool(@PathVariable String id) {
        SubscriptionPlan viewPlan = this.signUpService.viewPlanBySchool(id);
        if (viewPlan == null) {
            return new ResponseEntity((Object)new ResponseModel(Boolean.valueOf(false), "You don't have any active plan", null), (HttpStatusCode)HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity((Object)new ResponseModel(Boolean.valueOf(true), "Your plan is retrieve successfully", (Object)viewPlan), (HttpStatusCode)HttpStatus.OK);
    }

    @PostMapping(value={"/verify-payment"})
    public ResponseEntity<ResponseModel> verifyPayment(@RequestBody PaymentVerificationRequest verificationRequest) {
        try {
            String data = verificationRequest.getOrderId() + "|" + verificationRequest.getPaymentId();
            String generatedSignature = this.generateHmacSHA256Signature(data, this.secretKey);
            System.out.println("i m a signature : " + generatedSignature);
            if (generatedSignature.equals(verificationRequest.getSignature())) {
                SubscriptionPlan updatedPayment = this.subscriptionService.updatePaymentStatus(verificationRequest.getOrderId(), verificationRequest.getPaymentId());
                String schoolName = null;
                Optional schoolDetails = this.signUpService.getBySchoolId(updatedPayment.getSchoolId());
                if (schoolDetails.isPresent()) {
                    SignUp details = (SignUp)schoolDetails.get();
                    schoolName = details.getSchoolName();
                }
                if (updatedPayment.getPaymentDetails() != null) {
                    updatedPayment.getPaymentDetails().setStatus("Paid");
                    PaymentHistory paymentHistory = new PaymentHistory();
                    paymentHistory.setOrderId(verificationRequest.getOrderId());
                    paymentHistory.setPaymentId(verificationRequest.getPaymentId());
                    paymentHistory.setPaidAmount(updatedPayment.getPaymentDetails().getAmount());
                    paymentHistory.setPaymentStatus("Paid");
                    paymentHistory.setPaymentDate(LocalDateTime.now());
                    paymentHistory.setSchoolId(updatedPayment.getSchoolId());
                    paymentHistory.setSchoolName(schoolName);
                    paymentHistory.setPlanName(updatedPayment.getPlanName());
                    this.paymentHistoryRepo.save(paymentHistory);
                    this.subscriptionService.updatePayments(updatedPayment);
                }
                return ResponseEntity.status((HttpStatusCode)HttpStatus.OK).body(new ResponseModel(Boolean.valueOf(true), "Payment verified successfully", (Object)updatedPayment));
            }
            return ResponseEntity.status((int)400).body(new ResponseModel(Boolean.valueOf(false), "Invalid payment signature", null));
        }
        catch (Exception e) {
            return ResponseEntity.status((int)500).body(new ResponseModel(Boolean.valueOf(false), "Error: " + e.getMessage(), null));
        }
    }

    private String generateHmacSHA256Signature(String data, String key) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "HmacSHA256");
        sha256_HMAC.init(secretKeySpec);
        byte[] hash = sha256_HMAC.doFinal(data.getBytes());
        return this.bytesToHex(hash);
    }

    private String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xFF & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}

