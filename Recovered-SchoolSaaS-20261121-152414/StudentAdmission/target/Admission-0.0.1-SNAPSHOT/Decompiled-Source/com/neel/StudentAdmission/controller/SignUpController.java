/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.StudentAdmission.controller.SignUpController
 *  com.neel.StudentAdmission.model.ResponseMessage
 *  com.neel.StudentAdmission.model.SignUp
 *  com.neel.StudentAdmission.service.SignUpService
 *  com.razorpay.Order
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.http.ResponseEntity
 *  org.springframework.web.bind.annotation.CrossOrigin
 *  org.springframework.web.bind.annotation.DeleteMapping
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.PutMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.RestController
 */
package com.neel.StudentAdmission.controller;

import com.neel.StudentAdmission.model.ResponseMessage;
import com.neel.StudentAdmission.model.SignUp;
import com.neel.StudentAdmission.service.SignUpService;
import com.razorpay.Order;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins={"*"})
@RequestMapping(value={"/api/signups"})
public class SignUpController {
    @Autowired
    private SignUpService signUpService;

    @GetMapping
    public ResponseEntity<ResponseMessage> getAllSignUps() {
        List signUps = this.signUpService.getAllSignUps();
        return ResponseEntity.ok((Object)new ResponseMessage("Sign-ups retrieved successfully", (Object)signUps));
    }

    @GetMapping(value={"/email/{email}"})
    public ResponseMessage getTeacherByEmail(@PathVariable String email) {
        Optional librarian = this.signUpService.getLibrarianByEmail(email);
        return librarian.map(value -> new ResponseMessage("School Details retrieved successfully", value)).orElseGet(() -> new ResponseMessage("School Details not found with email: " + email, null));
    }

    @GetMapping(value={"/{id}"})
    public ResponseEntity<ResponseMessage> getSignUpById(@PathVariable String id) {
        return this.signUpService.getSignUpById(id).map(signUp -> ResponseEntity.ok((Object)new ResponseMessage("Sign-up retrieved successfully", signUp))).orElse(ResponseEntity.status((int)404).body((Object)new ResponseMessage("Sign-up not found", null)));
    }

    @PostMapping
    public ResponseEntity<ResponseMessage> createSignUp(@RequestBody SignUp signUp) {
        try {
            SignUp createdSignUp = this.signUpService.createSignUp(signUp);
            return ResponseEntity.ok((Object)new ResponseMessage("Sign-up created successfully", (Object)createdSignUp));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body((Object)new ResponseMessage(e.getMessage(), null));
        }
    }

    @PutMapping(value={"/{id}"})
    public ResponseEntity<ResponseMessage> updateSignUp(@PathVariable String id, @RequestBody SignUp signUp) {
        try {
            SignUp updatedSignUp = this.signUpService.updateSignUp(id, signUp);
            return ResponseEntity.ok((Object)new ResponseMessage("Sign-up updated successfully", (Object)updatedSignUp));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body((Object)new ResponseMessage(e.getMessage(), null));
        }
    }

    @DeleteMapping(value={"/{id}"})
    public ResponseEntity<ResponseMessage> deleteSignUp(@PathVariable String id) {
        this.signUpService.deleteSignUp(id);
        return ResponseEntity.ok((Object)new ResponseMessage("Sign-up deleted successfully", null));
    }

    @PostMapping(value={"/create-order"})
    public ResponseEntity<?> createOrder(@RequestBody Map<String, Integer> paymentDetails) {
        try {
            if (!paymentDetails.containsKey("amount") || paymentDetails.get("amount") <= 0) {
                return ResponseEntity.badRequest().body((Object)"Invalid amount provided.");
            }
            int amount = paymentDetails.get("amount");
            Order order = this.signUpService.createOrder(amount);
            return ResponseEntity.ok((Object)order);
        }
        catch (Exception e) {
            return ResponseEntity.status((int)500).body((Object)("Error creating order: " + e.getMessage()));
        }
    }

    @PostMapping(value={"/capture"})
    public ResponseEntity<String> capturePayment(@RequestParam String paymentId) {
        try {
            if (paymentId == null || paymentId.isEmpty()) {
                return ResponseEntity.badRequest().body((Object)"Payment ID is required.");
            }
            String response = this.signUpService.capturePayment(paymentId);
            return ResponseEntity.ok((Object)response);
        }
        catch (Exception e) {
            return ResponseEntity.status((int)500).body((Object)("Error capturing payment: " + e.getMessage()));
        }
    }
}

