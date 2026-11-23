/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.StudentAdmission.controller.SignUpController
 *  com.neel.StudentAdmission.model.ResponseMessage
 *  com.neel.StudentAdmission.model.ResponseModel
 *  com.neel.StudentAdmission.model.SignUp
 *  com.neel.StudentAdmission.service.SignUpService
 *  com.razorpay.Order
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.http.HttpStatus
 *  org.springframework.http.HttpStatusCode
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
import com.neel.StudentAdmission.model.ResponseModel;
import com.neel.StudentAdmission.model.SignUp;
import com.neel.StudentAdmission.service.SignUpService;
import com.razorpay.Order;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
        return ResponseEntity.ok(new ResponseMessage("Sign-ups retrieved successfully", (Object)signUps));
    }

    @GetMapping(value={"/email/{email}"})
    public ResponseMessage getTeacherByEmail(@PathVariable String email) {
        Optional<SignUp> librarian = this.signUpService.getLibrarianByEmail(email);
        return librarian.map(value -> new ResponseMessage("School Details retrieved successfully", value)).orElseGet(() -> new ResponseMessage("School Details not found with email: " + email, null));
    }

    @GetMapping(value={"/{id}"})
    public ResponseEntity<ResponseModel> getSchoolBySchoolId(@PathVariable String id) {
        Optional existSchool = this.signUpService.getBySchoolId(id);
        if (existSchool.isPresent()) {
            return new ResponseEntity((Object)new ResponseModel(Boolean.valueOf(true), "school retrieve successfully", (Object)existSchool), (HttpStatusCode)HttpStatus.OK);
        }
        return new ResponseEntity((Object)new ResponseModel(Boolean.valueOf(false), "School not found ", null), (HttpStatusCode)HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<ResponseMessage> createSignUp(@RequestBody SignUp signUp) {
        try {
            SignUp createdSignUp = this.signUpService.createSignUp(signUp);
            return ResponseEntity.ok(new ResponseMessage("Sign-up created successfully", (Object)createdSignUp));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseMessage(e.getMessage(), null));
        }
    }

    @PutMapping(value={"/{id}"})
    public ResponseEntity<ResponseMessage> updateSignUp(@PathVariable String id, @RequestBody SignUp signUp) {
        try {
            SignUp updatedSignUp = this.signUpService.updateSignUp(id, signUp);
            return ResponseEntity.ok(new ResponseMessage("Sign-up updated successfully", (Object)updatedSignUp));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseMessage(e.getMessage(), null));
        }
    }

    @PutMapping(value={"/block/{id}"})
    public ResponseEntity<ResponseModel> blockSchool(@PathVariable String id) {
        Optional updatedSchool = this.signUpService.toggleBlockedSchool(id);
        if (updatedSchool.isPresent()) {
            String status = ((SignUp)updatedSchool.get()).getStatus();
            String message = "School " + (status.equalsIgnoreCase("BLOCKED") ? "blocked" : "unblocked") + " successfully";
            return new ResponseEntity((Object)new ResponseModel(Boolean.valueOf(true), message, (Object)updatedSchool), (HttpStatusCode)HttpStatus.OK);
        }
        return new ResponseEntity((Object)new ResponseModel(Boolean.valueOf(false), "School not found", null), (HttpStatusCode)HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value={"/{id}"})
    public ResponseEntity<ResponseMessage> deleteSignUp(@PathVariable String id) {
        this.signUpService.deleteSignUp(id);
        return ResponseEntity.ok(new ResponseMessage("Sign-up deleted successfully", null));
    }

    @PostMapping(value={"/create-order"})
    public ResponseEntity<?> createOrder(@RequestBody Map<String, Integer> paymentDetails) {
        try {
            if (!paymentDetails.containsKey("amount") || paymentDetails.get("amount") <= 0) {
                return ResponseEntity.badRequest().body("Invalid amount provided.");
            }
            int amount = paymentDetails.get("amount");
            Order order = this.signUpService.createOrder(amount);
            return ResponseEntity.ok(order);
        }
        catch (Exception e) {
            return ResponseEntity.status((int)500).body(("Error creating order: " + e.getMessage()));
        }
    }

    @PostMapping(value={"/capture"})
    public ResponseEntity<String> capturePayment(@RequestParam String paymentId) {
        try {
            if (paymentId == null || paymentId.isEmpty()) {
                return ResponseEntity.badRequest().body("Payment ID is required.");
            }
            String response = this.signUpService.capturePayment(paymentId);
            return ResponseEntity.ok(response);
        }
        catch (Exception e) {
            return ResponseEntity.status((int)500).body(("Error capturing payment: " + e.getMessage()));
        }
    }
}

