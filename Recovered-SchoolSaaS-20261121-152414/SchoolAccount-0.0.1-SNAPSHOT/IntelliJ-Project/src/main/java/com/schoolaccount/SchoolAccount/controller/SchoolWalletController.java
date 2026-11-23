/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.schoolaccount.SchoolAccount.controller.SchoolWalletController
 *  com.schoolaccount.SchoolAccount.model.PayoutRequest
 *  com.schoolaccount.SchoolAccount.model.ResponseMessage
 *  com.schoolaccount.SchoolAccount.service.SchoolWalletService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.http.ResponseEntity
 *  org.springframework.web.bind.annotation.CrossOrigin
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.PutMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.RestController
 */
package com.schoolaccount.SchoolAccount.controller;

import com.schoolaccount.SchoolAccount.model.PayoutRequest;
import com.schoolaccount.SchoolAccount.model.ResponseMessage;
import com.schoolaccount.SchoolAccount.service.SchoolWalletService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@RequestMapping(value={"/wallet"})
public class SchoolWalletController {
    @Autowired
    private SchoolWalletService schoolWalletService;

    @GetMapping(value={"/{schoolId}"})
    public ResponseEntity<ResponseMessage> getOrCreateWallet(@PathVariable String schoolId, @RequestParam String schoolName, @RequestParam(required=false) String accountType) {
        return ResponseEntity.ok((Object)this.schoolWalletService.getOrCreateSchoolWallet(schoolId, schoolName, accountType));
    }

    @GetMapping(value={"/{schoolId}/balance"})
    public ResponseEntity<ResponseMessage> getWalletBalance(@PathVariable String schoolId) {
        return ResponseEntity.ok((Object)this.schoolWalletService.getWalletBalance(schoolId));
    }

    @PostMapping(value={"/{schoolId}/sub-wallet"})
    public ResponseEntity<ResponseMessage> createSubWallet(@PathVariable String schoolId, @RequestBody Map<String, String> request) {
        String name = request.get("name");
        String description = request.get("description");
        return ResponseEntity.ok((Object)this.schoolWalletService.createSubWallet(schoolId, name, description));
    }

    @PostMapping(value={"/{schoolId}/payout"})
    public ResponseEntity<ResponseMessage> requestPayout(@PathVariable String schoolId, @RequestBody PayoutRequest payoutRequest) {
        payoutRequest.setSchoolId(schoolId);
        return ResponseEntity.ok((Object)this.schoolWalletService.requestPayout(payoutRequest));
    }

    @GetMapping(value={"/{schoolId}/transactions"})
    public ResponseEntity<ResponseMessage> getTransactionHistory(@PathVariable String schoolId) {
        return ResponseEntity.ok((Object)this.schoolWalletService.getTransactionHistory(schoolId));
    }

    @GetMapping(value={"/{schoolId}/payouts"})
    public ResponseEntity<ResponseMessage> getPayoutRequests(@PathVariable String schoolId) {
        return ResponseEntity.ok((Object)this.schoolWalletService.getPayoutRequests(schoolId));
    }

    @PutMapping(value={"/admin/payout/{payoutRequestId}"})
    public ResponseEntity<ResponseMessage> processPayout(@PathVariable String payoutRequestId, @RequestBody Map<String, String> request) {
        String status = request.get("status");
        String adminUserId = request.get("adminUserId");
        return ResponseEntity.ok((Object)this.schoolWalletService.processPayout(payoutRequestId, status, adminUserId));
    }
}

