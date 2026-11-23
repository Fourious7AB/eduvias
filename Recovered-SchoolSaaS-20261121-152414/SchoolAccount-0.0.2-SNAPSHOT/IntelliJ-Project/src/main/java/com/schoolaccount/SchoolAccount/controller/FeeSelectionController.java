/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.schoolaccount.SchoolAccount.controller.FeeSelectionController
 *  com.schoolaccount.SchoolAccount.model.FeeSelection
 *  com.schoolaccount.SchoolAccount.model.ResponseMessage
 *  com.schoolaccount.SchoolAccount.service.FeeSelectionService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.http.ResponseEntity
 *  org.springframework.web.bind.annotation.CrossOrigin
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.schoolaccount.SchoolAccount.controller;

import com.schoolaccount.SchoolAccount.model.FeeSelection;
import com.schoolaccount.SchoolAccount.model.ResponseMessage;
import com.schoolaccount.SchoolAccount.service.FeeSelectionService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping(value={"/api/feeSelection"})
public class FeeSelectionController {
    @Autowired
    private FeeSelectionService service;

    @PostMapping
    public ResponseEntity<ResponseMessage> setFeeType(@RequestBody FeeSelection feeSelection) {
        ResponseMessage response = this.service.saveFeeType(feeSelection);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ResponseMessage> getCurrentFeeType() {
        ResponseMessage response = this.service.getCurrentFeeType();
        return ResponseEntity.ok(response);
    }

    @PostMapping(value={"/{schoolId}"})
    public ResponseEntity<ResponseMessage> setFeeTypeWithSchool(@RequestBody FeeSelection feeSelection, @PathVariable String schoolId) {
        ResponseMessage response = this.service.saveFeeTypeWithSchool(feeSelection, schoolId);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value={"/{schoolId}"})
    public ResponseEntity<ResponseMessage> getCurrentFeeTypeWithSchool(@PathVariable String schoolId) {
        ResponseMessage response = this.service.getCurrentFeeTypeWithSchool(schoolId);
        return ResponseEntity.ok(response);
    }
}

