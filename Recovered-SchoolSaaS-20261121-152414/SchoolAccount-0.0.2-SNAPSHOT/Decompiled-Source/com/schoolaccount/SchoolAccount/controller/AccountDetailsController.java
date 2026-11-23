/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.schoolaccount.SchoolAccount.controller.AccountDetailsController
 *  com.schoolaccount.SchoolAccount.model.AccountDetails
 *  com.schoolaccount.SchoolAccount.model.ResponseMessage
 *  com.schoolaccount.SchoolAccount.service.AccountDetailsService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.web.bind.annotation.CrossOrigin
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.PutMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.schoolaccount.SchoolAccount.controller;

import com.schoolaccount.SchoolAccount.model.AccountDetails;
import com.schoolaccount.SchoolAccount.model.ResponseMessage;
import com.schoolaccount.SchoolAccount.service.AccountDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins={"*"})
@RequestMapping(value={"/account"})
public class AccountDetailsController {
    @Autowired
    private AccountDetailsService service;

    @PostMapping(value={"/create"})
    public ResponseMessage createAccount(@RequestBody AccountDetails accountDetails) {
        return this.service.createAccount(accountDetails);
    }

    @GetMapping(value={"/get"})
    public ResponseMessage getAccount() {
        return this.service.getAccountDetails();
    }

    @PutMapping(value={"/update"})
    public ResponseMessage updateAccount(@RequestBody AccountDetails updatedAccount) {
        return this.service.updateAccount(updatedAccount);
    }

    @PostMapping(value={"/create/{schoolId}"})
    public ResponseMessage createAccountWithSchool(@RequestBody AccountDetails accountDetails, @PathVariable String schoolId) {
        return this.service.createAccountWithSchool(accountDetails, schoolId);
    }

    @GetMapping(value={"/get/{schoolId}"})
    public ResponseMessage getAccountBySchool(@PathVariable String schoolId) {
        return this.service.getAccountDetailsBySchool(schoolId);
    }

    @PutMapping(value={"/update/{schoolId}"})
    public ResponseMessage updateAccountWithSchool(@RequestBody AccountDetails updatedAccount, @PathVariable String schoolId) {
        return this.service.updateAccountWithSchool(updatedAccount, schoolId);
    }
}

