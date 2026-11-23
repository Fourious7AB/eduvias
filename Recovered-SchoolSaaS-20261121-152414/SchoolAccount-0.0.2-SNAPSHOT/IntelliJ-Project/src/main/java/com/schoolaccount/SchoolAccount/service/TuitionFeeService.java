/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.schoolaccount.SchoolAccount.model.FeeSelection
 *  com.schoolaccount.SchoolAccount.model.ResponseMessage
 *  com.schoolaccount.SchoolAccount.model.TuitionFee
 *  com.schoolaccount.SchoolAccount.repo.FeeSelectionRepository
 *  com.schoolaccount.SchoolAccount.repo.TuitionFeeRepository
 *  com.schoolaccount.SchoolAccount.service.TuitionFeeService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 */
package com.schoolaccount.SchoolAccount.service;

import com.schoolaccount.SchoolAccount.model.FeeSelection;
import com.schoolaccount.SchoolAccount.model.ResponseMessage;
import com.schoolaccount.SchoolAccount.model.TuitionFee;
import com.schoolaccount.SchoolAccount.repo.FeeSelectionRepository;
import com.schoolaccount.SchoolAccount.repo.TuitionFeeRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TuitionFeeService {
    @Autowired
    private TuitionFeeRepository repository;
    @Autowired
    private FeeSelectionRepository feeSelectionRepository;

    public ResponseMessage createTuitionFee(String className, double monthlyFee) {
        if (this.repository.findByClassName(className).isPresent()) {
            return new ResponseMessage("Tuition fee for this class already exists!", null);
        }
        TuitionFee tuitionFee = new TuitionFee(className, monthlyFee);
        tuitionFee.setSchoolId("67ca9167d56ec91fc9539234");
        this.repository.save(tuitionFee);
        return new ResponseMessage("Tuition fee created successfully!", (Object)tuitionFee);
    }

    public ResponseMessage getAllTuitionFees() {
        List<TuitionFee> fees = this.repository.findAll();
        return new ResponseMessage("Fetched all tuition fees!", (Object)fees);
    }

    public ResponseMessage getTuitionFeeById(String id) {
        Optional<TuitionFee> tuitionFee = this.repository.findById(id);
        return tuitionFee.map(fee -> new ResponseMessage("Tuition fee found!", fee)).orElse(new ResponseMessage("Tuition fee not found!", null));
    }

    public ResponseMessage updateTuitionFee(String id, double monthlyFee) {
        Optional<TuitionFee> existingFee = this.repository.findById(id);
        if (existingFee.isPresent()) {
            TuitionFee tuitionFee = (TuitionFee)existingFee.get();
            tuitionFee.setMonthlyFee(monthlyFee);
            tuitionFee.setQuarterlyFee(monthlyFee * 3.0);
            tuitionFee.setHalfYearlyFee(monthlyFee * 6.0);
            tuitionFee.setYearlyFee(monthlyFee * 12.0);
            this.repository.save(tuitionFee);
            return new ResponseMessage("Tuition fee updated successfully!", (Object)tuitionFee);
        }
        return new ResponseMessage("Tuition fee not found!", null);
    }

    public ResponseMessage deleteTuitionFee(String id) {
        if (this.repository.existsById(id)) {
            this.repository.deleteById(id);
            return new ResponseMessage("Tuition fee deleted successfully!", null);
        }
        return new ResponseMessage("Tuition fee not found!", null);
    }

    public ResponseMessage getFeeByClassName(String className) {
        Optional feeSelectionOpt = this.feeSelectionRepository.findAll().stream().findFirst();
        if (!feeSelectionOpt.isPresent()) {
            return new ResponseMessage("No FeeSelection found!", null);
        }
        String feeType = ((FeeSelection)feeSelectionOpt.get()).getFeeType();
        Optional tuitionFeeOpt = this.repository.findByClassName(className);
        if (!tuitionFeeOpt.isPresent()) {
            return new ResponseMessage("No tuition fee found for class: " + className, null);
        }
        TuitionFee fee = (TuitionFee)tuitionFeeOpt.get();
        double selectedFee = 0.0;
        switch (feeType.toLowerCase()) {
            case "monthly": {
                selectedFee = fee.getMonthlyFee();
                break;
            }
            case "quarterly": {
                selectedFee = fee.getQuarterlyFee();
                break;
            }
            case "halfyearly": {
                selectedFee = fee.getHalfYearlyFee();
                break;
            }
            case "yearly": {
                selectedFee = fee.getYearlyFee();
                break;
            }
            default: {
                return new ResponseMessage("Invalid fee type!", null);
            }
        }
        return new ResponseMessage("Fetched fee successfully!", (Object)selectedFee);
    }

    public ResponseMessage createTuitionFeeWithSchool(String className, double monthlyFee, String schoolId) {
        if (this.repository.findByClassNameAndSchoolId(className, schoolId).isPresent()) {
            return new ResponseMessage("Tuition fee for this class already exists in this school!", null);
        }
        TuitionFee tuitionFee = new TuitionFee(className, monthlyFee, schoolId);
        this.repository.save(tuitionFee);
        return new ResponseMessage("Tuition fee created successfully!", (Object)tuitionFee);
    }

    public ResponseMessage getAllTuitionFeesBySchool(String schoolId) {
        List fees = this.repository.findBySchoolId(schoolId);
        return new ResponseMessage("Fetched all tuition fees for school!", (Object)fees);
    }

    public ResponseMessage updateTuitionFeeWithSchool(String id, double monthlyFee, String schoolId) {
        Optional<TuitionFee> existingFee = this.repository.findById(id);
        if (existingFee.isPresent() && ((TuitionFee)existingFee.get()).getSchoolId().equals(schoolId)) {
            TuitionFee tuitionFee = (TuitionFee)existingFee.get();
            tuitionFee.setMonthlyFee(monthlyFee);
            tuitionFee.setQuarterlyFee(monthlyFee * 3.0);
            tuitionFee.setHalfYearlyFee(monthlyFee * 6.0);
            tuitionFee.setYearlyFee(monthlyFee * 12.0);
            this.repository.save(tuitionFee);
            return new ResponseMessage("Tuition fee updated successfully!", (Object)tuitionFee);
        }
        return new ResponseMessage("Tuition fee not found or does not belong to this school!", null);
    }

    public ResponseMessage deleteTuitionFeeWithSchool(String id, String schoolId) {
        Optional<TuitionFee> existingFee = this.repository.findById(id);
        if (existingFee.isPresent() && ((TuitionFee)existingFee.get()).getSchoolId().equals(schoolId)) {
            this.repository.deleteById(id);
            return new ResponseMessage("Tuition fee deleted successfully!", null);
        }
        return new ResponseMessage("Tuition fee not found or does not belong to this school!", null);
    }

    public ResponseMessage getFeeByClassNameAndSchool(String className, String schoolId) {
        Optional feeSelectionOpt = this.feeSelectionRepository.findBySchoolId(schoolId);
        if (!feeSelectionOpt.isPresent()) {
            return new ResponseMessage("No FeeSelection found!", null);
        }
        String feeType = ((FeeSelection)feeSelectionOpt.get()).getFeeType();
        Optional tuitionFeeOpt = this.repository.findByClassNameAndSchoolId(className, schoolId);
        if (!tuitionFeeOpt.isPresent()) {
            return new ResponseMessage("No tuition fee found for class: " + className + " in this school", null);
        }
        TuitionFee fee = (TuitionFee)tuitionFeeOpt.get();
        double selectedFee = 0.0;
        switch (feeType.toLowerCase()) {
            case "monthly": {
                selectedFee = fee.getMonthlyFee();
                break;
            }
            case "quarterly": {
                selectedFee = fee.getQuarterlyFee();
                break;
            }
            case "halfyearly": {
                selectedFee = fee.getHalfYearlyFee();
                break;
            }
            case "yearly": {
                selectedFee = fee.getYearlyFee();
                break;
            }
            default: {
                return new ResponseMessage("Invalid fee type!", null);
            }
        }
        return new ResponseMessage("Fetched fee successfully!", (Object)selectedFee);
    }
}

