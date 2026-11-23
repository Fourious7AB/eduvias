/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.schoolaccount.SchoolAccount.model.FeesStructure
 *  com.schoolaccount.SchoolAccount.model.FeesStructure$AdditionalCharge
 *  com.schoolaccount.SchoolAccount.model.ResponseMessage
 *  com.schoolaccount.SchoolAccount.repo.FeesStructureRepository
 *  com.schoolaccount.SchoolAccount.service.FeesStructureService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 */
package com.schoolaccount.SchoolAccount.service;

import com.schoolaccount.SchoolAccount.model.FeesStructure;
import com.schoolaccount.SchoolAccount.model.ResponseMessage;
import com.schoolaccount.SchoolAccount.repo.FeesStructureRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeesStructureService {
    @Autowired
    private FeesStructureRepository repository;

    public ResponseMessage createFeesStructure(FeesStructure feesStructure) {
        if (this.repository.findByClassNameAndSection(feesStructure.getClassName(), feesStructure.getSection()).isPresent()) {
            return new ResponseMessage("Fees structure for this class and section already exists!", null);
        }
        feesStructure.setSchoolId("67ca9167d56ec91fc9539234");
        feesStructure.setTotalFees(feesStructure.calculateTotalFees());
        this.repository.save((Object)feesStructure);
        return new ResponseMessage("Fees structure created successfully!", (Object)feesStructure);
    }

    public ResponseMessage getAllFeesStructures() {
        List structures = this.repository.findAll();
        return new ResponseMessage("Fetched all fees structures!", (Object)structures);
    }

    public ResponseMessage getFeesStructureById(String id) {
        Optional structure = this.repository.findById((Object)id);
        return structure.map(s -> new ResponseMessage("Fees structure found!", s)).orElse(new ResponseMessage("Fees structure not found!", null));
    }

    public ResponseMessage getFeesStructureByClassAndSection(String className, String section) {
        Optional structure = this.repository.findByClassNameAndSection(className, section);
        return structure.map(s -> new ResponseMessage("Fees structure found!", s)).orElse(new ResponseMessage("Fees structure not found for this class and section!", null));
    }

    public ResponseMessage updateFeesStructure(String id, FeesStructure updatedFeesStructure) {
        Optional existingStructure = this.repository.findById((Object)id);
        if (existingStructure.isPresent()) {
            FeesStructure structure = (FeesStructure)existingStructure.get();
            structure.setAdmission_Fees(updatedFeesStructure.getAdmission_Fees());
            structure.setBooks(updatedFeesStructure.getBooks());
            structure.setDonation(updatedFeesStructure.getDonation());
            structure.setFine(updatedFeesStructure.getFine());
            structure.setId_Card_Charges(updatedFeesStructure.getId_Card_Charges());
            structure.setLate_Fees(updatedFeesStructure.getLate_Fees());
            structure.setMiscellaneous(updatedFeesStructure.getMiscellaneous());
            structure.setTransportationalFees(updatedFeesStructure.getTransportationalFees());
            structure.setTuitionFees(updatedFeesStructure.getTuitionFees());
            structure.setUniform_Charges(updatedFeesStructure.getUniform_Charges());
            structure.setOther(updatedFeesStructure.getOther());
            structure.setAdditionalCharges(updatedFeesStructure.getAdditionalCharges());
            structure.setTotalFees(structure.calculateTotalFees());
            this.repository.save((Object)structure);
            return new ResponseMessage("Fees structure updated successfully!", (Object)structure);
        }
        return new ResponseMessage("Fees structure not found!", null);
    }

    public ResponseMessage deleteFeesStructure(String id) {
        if (this.repository.existsById((Object)id)) {
            this.repository.deleteById((Object)id);
            return new ResponseMessage("Fees structure deleted successfully!", null);
        }
        return new ResponseMessage("Fees structure not found!", null);
    }

    public ResponseMessage createFeesStructureWithSchool(FeesStructure feesStructure, String schoolId) {
        if (this.repository.findByClassNameIgnoreCaseAndSchoolId(feesStructure.getClassName(), schoolId).isPresent()) {
            return new ResponseMessage("Fees structure for this class already exists in this school!", null);
        }
        double additionalChargesTotal = feesStructure.getAdditionalCharges() != null ? feesStructure.getAdditionalCharges().stream().mapToDouble(FeesStructure.AdditionalCharge::getCharges).sum() : 0.0;
        feesStructure.setOther(additionalChargesTotal);
        feesStructure.setSchoolId(schoolId);
        feesStructure.setTotalFees(feesStructure.calculateTotalFees());
        this.repository.save((Object)feesStructure);
        return new ResponseMessage("Fees structure created successfully!", (Object)feesStructure);
    }

    public ResponseMessage getAllFeesStructuresBySchool(String schoolId) {
        List structures = this.repository.findBySchoolId(schoolId);
        return new ResponseMessage("Fetched all fees structures for school!", (Object)structures);
    }

    public ResponseMessage getFeesStructureByClassAndSchool(String className, String schoolId) {
        Optional structure = this.repository.findByClassNameIgnoreCaseAndSchoolId(className, schoolId);
        System.out.println(" structure: " + structure);
        return structure.map(s -> new ResponseMessage("Fees structure found!", s)).orElse(new ResponseMessage("Fees structure not found for this class in this school!", null));
    }

    public ResponseMessage updateFeesStructureWithSchool(String id, FeesStructure updatedFeesStructure, String schoolId) {
        Optional existingStructure = this.repository.findById((Object)id);
        if (existingStructure.isPresent() && ((FeesStructure)existingStructure.get()).getSchoolId().equals(schoolId)) {
            FeesStructure structure = (FeesStructure)existingStructure.get();
            structure.setAdmission_Fees(updatedFeesStructure.getAdmission_Fees());
            structure.setBooks(updatedFeesStructure.getBooks());
            structure.setDonation(updatedFeesStructure.getDonation());
            structure.setFine(updatedFeesStructure.getFine());
            structure.setId_Card_Charges(updatedFeesStructure.getId_Card_Charges());
            structure.setLate_Fees(updatedFeesStructure.getLate_Fees());
            structure.setMiscellaneous(updatedFeesStructure.getMiscellaneous());
            structure.setTransportationalFees(updatedFeesStructure.getTransportationalFees());
            structure.setTuitionFees(updatedFeesStructure.getTuitionFees());
            structure.setUniform_Charges(updatedFeesStructure.getUniform_Charges());
            structure.setOther(updatedFeesStructure.getOther());
            structure.setAdditionalCharges(updatedFeesStructure.getAdditionalCharges());
            structure.setTotalFees(structure.calculateTotalFees());
            this.repository.save((Object)structure);
            return new ResponseMessage("Fees structure updated successfully!", (Object)structure);
        }
        return new ResponseMessage("Fees structure not found or does not belong to this school!", null);
    }

    public ResponseMessage deleteFeesStructureWithSchool(String id, String schoolId) {
        Optional existingStructure = this.repository.findById((Object)id);
        if (existingStructure.isPresent() && ((FeesStructure)existingStructure.get()).getSchoolId().equals(schoolId)) {
            this.repository.deleteById((Object)id);
            return new ResponseMessage("Fees structure deleted successfully!", null);
        }
        return new ResponseMessage("Fees structure not found or does not belong to this school!", null);
    }
}

