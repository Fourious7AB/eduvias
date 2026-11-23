/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.schoolaccount.SchoolAccount.model.FeeSelection
 *  com.schoolaccount.SchoolAccount.model.ResponseMessage
 *  com.schoolaccount.SchoolAccount.repo.FeeSelectionRepository
 *  com.schoolaccount.SchoolAccount.service.FeeSelectionService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 */
package com.schoolaccount.SchoolAccount.service;

import com.schoolaccount.SchoolAccount.model.FeeSelection;
import com.schoolaccount.SchoolAccount.model.ResponseMessage;
import com.schoolaccount.SchoolAccount.repo.FeeSelectionRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeeSelectionService {
    @Autowired
    private FeeSelectionRepository repository;

    public ResponseMessage saveFeeType(FeeSelection newFeeSelection) {
        newFeeSelection.setSchoolId("67ca9167d56ec91fc9539234");
        Optional existingSelection = this.repository.findBySchoolId(newFeeSelection.getSchoolId());
        if (existingSelection.isPresent()) {
            FeeSelection feeSelection = (FeeSelection)existingSelection.get();
            feeSelection.setFeeType(newFeeSelection.getFeeType());
            this.repository.save((Object)feeSelection);
            return new ResponseMessage("Fee type updated successfully!", (Object)feeSelection);
        }
        this.repository.save((Object)newFeeSelection);
        return new ResponseMessage("Fee type saved successfully!", (Object)newFeeSelection);
    }

    public ResponseMessage getCurrentFeeType() {
        String schoolId = "67ca9167d56ec91fc9539234";
        Optional existingSelection = this.repository.findBySchoolId(schoolId);
        if (existingSelection.isPresent()) {
            return new ResponseMessage("Current fee type retrieved successfully!", existingSelection.get());
        }
        return new ResponseMessage("No fee type found!", null);
    }

    public ResponseMessage saveFeeTypeWithSchool(FeeSelection newFeeSelection, String schoolId) {
        newFeeSelection.setSchoolId(schoolId);
        Optional existingSelection = this.repository.findBySchoolId(schoolId);
        if (existingSelection.isPresent()) {
            FeeSelection feeSelection = (FeeSelection)existingSelection.get();
            feeSelection.setFeeType(newFeeSelection.getFeeType());
            this.repository.save((Object)feeSelection);
            return new ResponseMessage("Fee type updated successfully for school!", (Object)feeSelection);
        }
        this.repository.save((Object)newFeeSelection);
        return new ResponseMessage("Fee type saved successfully for school!", (Object)newFeeSelection);
    }

    public ResponseMessage getCurrentFeeTypeWithSchool(String schoolId) {
        Optional existingSelection = this.repository.findBySchoolId(schoolId);
        if (existingSelection.isPresent()) {
            return new ResponseMessage("Current fee type retrieved successfully for school!", existingSelection.get());
        }
        return new ResponseMessage("No fee type found for this school!", null);
    }
}

