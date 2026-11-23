/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.StudentAdmission.model.AuthUser
 *  com.neel.StudentAdmission.model.SignUp
 *  com.neel.StudentAdmission.repo.SignUpRepository
 *  com.neel.StudentAdmission.service.SignUpService
 *  com.razorpay.Order
 *  com.razorpay.Payment
 *  com.razorpay.RazorpayClient
 *  org.apache.commons.codec.digest.DigestUtils
 *  org.json.JSONObject
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.beans.factory.annotation.Value
 *  org.springframework.stereotype.Service
 *  org.springframework.web.client.RestTemplate
 */
package com.neel.StudentAdmission.service;

import com.neel.StudentAdmission.model.AuthUser;
import com.neel.StudentAdmission.model.SignUp;
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
import org.springframework.stereotype.Service;
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
        this.saveAuthUser(signUp.getEmail(), signUp.getPassword());
        return (SignUp)this.signUpRepository.save((Object)signUp);
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

    private void saveAuthUser(String email, String password) {
        AuthUser authUser = new AuthUser();
        authUser.setUsername(email);
        authUser.setPassword(password);
        authUser.setRole("admin");
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForEntity("https://collage.vaisacademy.com/service4/api/auth/register", (Object)authUser, String.class, new Object[0]);
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
}

