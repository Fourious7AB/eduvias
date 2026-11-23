/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.StudentAdmission.model.AuthUser
 *  com.neel.StudentAdmission.model.SignUp
 *  com.neel.StudentAdmission.model.SubscriptionPlan
 *  com.neel.StudentAdmission.repo.SignUpRepository
 *  com.neel.StudentAdmission.service.SignUpService
 *  com.razorpay.Order
 *  com.razorpay.Payment
 *  com.razorpay.RazorpayClient
 *  org.apache.commons.codec.digest.DigestUtils
 *  org.json.JSONObject
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.beans.factory.annotation.Value
 *  org.springframework.http.HttpEntity
 *  org.springframework.http.HttpHeaders
 *  org.springframework.http.MediaType
 *  org.springframework.http.ResponseEntity
 *  org.springframework.stereotype.Service
 *  org.springframework.util.MultiValueMap
 *  org.springframework.web.client.RestTemplate
 */
package com.neel.StudentAdmission.service;

import com.neel.StudentAdmission.model.AuthUser;
import com.neel.StudentAdmission.model.SignUp;
import com.neel.StudentAdmission.model.SubscriptionPlan;
import com.neel.StudentAdmission.repo.SignUpRepository;
import com.razorpay.Order;
import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import java.util.List;
import java.util.Optional;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class SignUpService {
    @Value(value="${razorpay.keyId}")
    private String keyId;
    @Value(value="${razorpay.keySecret}")
    private String keySecret;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_+=<>?";
    private static final int PASSWORD_LENGTH = 8;
    @Autowired
    private SignUpRepository signUpRepository;

    public List<SignUp> getAllSignUps() {
        return this.signUpRepository.findAll();
    }

    public Optional<SignUp> getSignUpById(String id) {
        return this.signUpRepository.findById((Object)id);
    }

    public SignUp createSignUp(SignUp signUp) {
        if (this.signUpRepository.findByEmail(signUp.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists!");
        }
        if (this.signUpRepository.findByPhoneNumber(signUp.getPhoneNumber()).isPresent()) {
            throw new IllegalArgumentException("Phone number already exists!");
        }
        try {
            this.saveAuthUser(signUp.getEmail(), signUp.getPassword());
        }
        catch (Exception e) {
            System.err.println("Failed to register AuthUser: " + e.getMessage());
            throw new RuntimeException("Failed to register auth user: " + e.getMessage());
        }
        return (SignUp)this.signUpRepository.save((Object)signUp);
    }

    public Optional<SignUp> toggleBlockedSchool(String id) {
        Optional existSchool = this.signUpRepository.findById((Object)id);
        if (existSchool.isPresent()) {
            SignUp updateSchool = (SignUp)existSchool.get();
            String currentStatus = updateSchool.getStatus();
            if ("BLOCKED".equalsIgnoreCase(currentStatus)) {
                updateSchool.setStatus("UNBLOCKED");
            } else {
                updateSchool.setStatus("BLOCKED");
            }
            this.signUpRepository.save((Object)updateSchool);
        }
        return existSchool;
    }

    public SignUp updateSignUp(String id, SignUp updatedSignUp) {
        SignUp existingSignUp = (SignUp)this.signUpRepository.findById((Object)id).orElseThrow(() -> new IllegalArgumentException("SignUp not found!"));
        this.signUpRepository.findByEmail(updatedSignUp.getEmail()).filter(signUp -> !signUp.getId().equals(id)).ifPresent(signUp -> {
            throw new IllegalArgumentException("Email already exists!");
        });
        this.signUpRepository.findByPhoneNumber(updatedSignUp.getPhoneNumber()).filter(signUp -> !signUp.getId().equals(id)).ifPresent(signUp -> {
            throw new IllegalArgumentException("Phone number already exists!");
        });
        existingSignUp.setUserName(updatedSignUp.getUserName());
        existingSignUp.setEmail(updatedSignUp.getEmail());
        existingSignUp.setPhoneNumber(updatedSignUp.getPhoneNumber());
        existingSignUp.setSchoolName(updatedSignUp.getSchoolName());
        existingSignUp.setAddress(updatedSignUp.getAddress());
        existingSignUp.setInstituteType(updatedSignUp.getInstituteType());
        return (SignUp)this.signUpRepository.save((Object)existingSignUp);
    }

    public void deleteSignUp(String id) {
        this.signUpRepository.deleteById((Object)id);
    }

    private String saveAuthUser(String email, String password) {
        AuthUser authUser = new AuthUser();
        authUser.setUsername(email);
        authUser.setPassword(password);
        authUser.setRole("admin");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity request = new HttpEntity((Object)authUser, (MultiValueMap)headers);
        System.out.println("req : " + request);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity response = restTemplate.postForEntity("https://collage.vaisacademy.com/service4/api/auth/register", (Object)request, String.class, new Object[0]);
        System.out.println("Response from AuthService: " + (String)response.getBody());
        return (String)response.getBody();
    }

    public boolean isPasswordStrong(String password) {
        return password.matches("^(?=.*[A-Z])(?=.*[0-9])(?=.*[@#$%^&+=]).{8,}$");
    }

    public Order createOrder(int amount) throws Exception {
        RazorpayClient client = new RazorpayClient(this.keyId, this.keySecret);
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amount * 100);
        orderRequest.put("currency", (Object)"INR");
        orderRequest.put("receipt", (Object)"order_receipt_123");
        return client.orders.create(orderRequest);
    }

    public String capturePayment(String paymentId) throws Exception {
        RazorpayClient client = new RazorpayClient(this.keyId, this.keySecret);
        Payment payment = client.payments.fetch(paymentId);
        long amount = (Long)payment.get("amount");
        JSONObject captureRequest = new JSONObject();
        captureRequest.put("amount", amount);
        Payment capturedPayment = client.payments.capture(paymentId, captureRequest);
        return "Payment captured successfully. Details: " + capturedPayment.toString();
    }

    private boolean verifySignature(String paymentId, String signature, String orderId) {
        String data = orderId + "|" + paymentId;
        String expectedSignature = DigestUtils.sha256Hex((String)(data + "|" + this.keySecret));
        return expectedSignature.equals(signature);
    }

    public Optional<SignUp> getLibrarianByEmail(String email) {
        return this.signUpRepository.findByEmail(email);
    }

    public Optional<SignUp> getBySchoolId(String id) {
        return this.signUpRepository.findById((Object)id);
    }

    public void updateSubsPlan(String id, SubscriptionPlan plan) {
        Optional existSchool = this.signUpRepository.findById((Object)id);
        if (existSchool.isPresent()) {
            SignUp signUp = (SignUp)existSchool.get();
            SubscriptionPlan subscriptionPlan = new SubscriptionPlan();
            subscriptionPlan.setId(plan.getId());
            subscriptionPlan.setPlanId(plan.getId());
            subscriptionPlan.setPlanName(plan.getPlanName());
            subscriptionPlan.setFeatures(plan.getFeatures());
            subscriptionPlan.setDuration(plan.getDuration());
            subscriptionPlan.setAmount(plan.getAmount());
            subscriptionPlan.setStartDate(plan.getStartDate());
            subscriptionPlan.setEndDate(plan.getEndDate());
            subscriptionPlan.setStatus(plan.getStatus());
            signUp.setActive(plan.getStatus());
            signUp.setPlanDetails(subscriptionPlan);
            this.signUpRepository.save((Object)signUp);
        }
    }

    public SubscriptionPlan viewPlanBySchool(String id) {
        Optional existSchool = this.signUpRepository.findById((Object)id);
        if (existSchool.isPresent()) {
            SignUp planDetail = (SignUp)existSchool.get();
            return planDetail.getPlanDetails();
        }
        return null;
    }
}

