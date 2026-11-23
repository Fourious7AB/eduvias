/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.schoolaccount.SchoolAccount.controller.StudentAdmissionFeeController
 *  com.schoolaccount.SchoolAccount.model.ResponseMessage
 *  com.schoolaccount.SchoolAccount.model.Services
 *  com.schoolaccount.SchoolAccount.model.StudentAdmissionFee
 *  com.schoolaccount.SchoolAccount.model.StudentAdmissionFee$TuitionFeePayment
 *  com.schoolaccount.SchoolAccount.service.StudentAdmissionFeeService
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
package com.schoolaccount.SchoolAccount.controller;

import com.schoolaccount.SchoolAccount.model.ResponseMessage;
import com.schoolaccount.SchoolAccount.model.Services;
import com.schoolaccount.SchoolAccount.model.StudentAdmissionFee;
import com.schoolaccount.SchoolAccount.service.StudentAdmissionFeeService;
import java.util.List;
import java.util.Map;
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
@RequestMapping
public class StudentAdmissionFeeController {
    @Autowired
    private StudentAdmissionFeeService studentAdmissionFeeService;

    @PostMapping(value={"/student-admission-fee/create"})
    public ResponseEntity<ResponseMessage> createFee(@RequestBody StudentAdmissionFee fee) {
        return ResponseEntity.ok((Object)this.studentAdmissionFeeService.createStudentAdmissionFee(fee));
    }

    @GetMapping(value={"/student-admission-fee/all"})
    public ResponseEntity<ResponseMessage> getAllFees() {
        return ResponseEntity.ok((Object)this.studentAdmissionFeeService.getAllStudentAdmissionFees());
    }

    @GetMapping(value={"/student-admission-fee/{id}"})
    public ResponseEntity<ResponseMessage> getFeeById(@PathVariable String id) {
        return ResponseEntity.ok((Object)this.studentAdmissionFeeService.getStudentAdmissionFeeById(id));
    }

    @DeleteMapping(value={"/student-admission-fee/{id}"})
    public ResponseEntity<ResponseMessage> deleteFeeById(@PathVariable String id) {
        return ResponseEntity.ok((Object)this.studentAdmissionFeeService.deleteStudentAdmissionFeeById(id));
    }

    @PutMapping(value={"/student-admission-fee/update-tuition/{id}"})
    public ResponseEntity<ResponseMessage> updateTuitionFees(@PathVariable String id, @RequestBody Map<String, Double> requestBody) {
        double tuitionFees = requestBody.get("tuitionFees");
        return ResponseEntity.ok((Object)this.studentAdmissionFeeService.updateTuitionFees(id, tuitionFees));
    }

    @PutMapping(value={"/student-admission-fee/add-tuition-payment/{id}"})
    public ResponseEntity<ResponseMessage> addTuitionPayment(@PathVariable String id, @RequestBody StudentAdmissionFee.TuitionFeePayment payment) {
        return ResponseEntity.ok((Object)this.studentAdmissionFeeService.addTuitionFeePayment(id, payment));
    }

    @GetMapping(value={"/student-admission-fee/admissionNumber/{admissionNumber}"})
    public ResponseEntity<ResponseMessage> getByAdmissionNumber(@PathVariable String admissionNumber) {
        return ResponseEntity.ok((Object)this.studentAdmissionFeeService.getStudentAdmissionFeeByAdmissionNumber(admissionNumber));
    }

    @PostMapping(value={"/student-admission-fee/create/{schoolId}"})
    public ResponseEntity<ResponseMessage> createFeeWithSchool(@PathVariable String schoolId, @RequestBody StudentAdmissionFee fee) {
        return ResponseEntity.ok((Object)this.studentAdmissionFeeService.createStudentAdmissionFeeWithSchool(fee, schoolId));
    }

    @GetMapping(value={"/student-admission-fee/all/{schoolId}"})
    public ResponseEntity<ResponseMessage> getAllFeesBySchool(@PathVariable String schoolId) {
        return ResponseEntity.ok((Object)this.studentAdmissionFeeService.getAllStudentAdmissionFeesBySchool(schoolId));
    }

    @GetMapping(value={"/student-admission-fee/{id}/{schoolId}"})
    public ResponseEntity<ResponseMessage> getFeeByIdAndSchool(@PathVariable String schoolId, @PathVariable String id) {
        return ResponseEntity.ok((Object)this.studentAdmissionFeeService.getStudentAdmissionFeeByIdAndSchool(id, schoolId));
    }

    @PutMapping(value={"/student-admission-fee/add-multiple-services/{admissionNumber}/{schoolId}"})
    public ResponseEntity<ResponseMessage> addMultipleServicesToStudentFee(@PathVariable String admissionNumber, @PathVariable String schoolId, @RequestParam(required=false) String academicYear, @RequestBody List<Services> services) {
        return ResponseEntity.ok((Object)this.studentAdmissionFeeService.addMultipleServicesToStudentFee(admissionNumber, schoolId, academicYear, services));
    }

    @GetMapping(value={"/student-admission-fee/calculate-dues/{admissionNumber}/{schoolId}"})
    public ResponseEntity<ResponseMessage> calculateTotalDues(@PathVariable String admissionNumber, @PathVariable String schoolId) {
        return ResponseEntity.ok((Object)this.studentAdmissionFeeService.calculateTotalDues(admissionNumber, schoolId));
    }

    @DeleteMapping(value={"/student-admission-fee/{id}/{schoolId}"})
    public ResponseEntity<ResponseMessage> deleteFeeByIdAndSchool(@PathVariable String schoolId, @PathVariable String id) {
        return ResponseEntity.ok((Object)this.studentAdmissionFeeService.deleteStudentAdmissionFeeByIdAndSchool(id, schoolId));
    }

    @PutMapping(value={"/student-admission-fee/update-tuition/{id}/{schoolId}"})
    public ResponseEntity<ResponseMessage> updateTuitionFeesWithSchool(@PathVariable String schoolId, @PathVariable String id, @RequestBody Map<String, Double> requestBody) {
        double tuitionFees = requestBody.get("tuitionFees");
        return ResponseEntity.ok((Object)this.studentAdmissionFeeService.updateTuitionFeesWithSchool(id, tuitionFees, schoolId));
    }

    @PutMapping(value={"/student-admission-fee/add-tuition-payment/{id}/{schoolId}"})
    public ResponseEntity<ResponseMessage> addTuitionPaymentWithSchool(@PathVariable String schoolId, @PathVariable String id, @RequestBody StudentAdmissionFee.TuitionFeePayment payment) {
        return ResponseEntity.ok((Object)this.studentAdmissionFeeService.addTuitionFeePaymentWithSchool(id, payment, schoolId));
    }

    @GetMapping(value={"/student-admission-fee/history/{admissionNumber}/{schoolId}"})
    public ResponseEntity<ResponseMessage> getStudentFeeHistory(@PathVariable String admissionNumber, @PathVariable String schoolId) {
        return ResponseEntity.ok((Object)this.studentAdmissionFeeService.getStudentFeeHistory(admissionNumber, schoolId));
    }

    @PutMapping(value={"/student-admission-fee/update-class/{admissionNumber}/{schoolId}"})
    public ResponseEntity<ResponseMessage> updateStudentClass(@PathVariable String admissionNumber, @PathVariable String schoolId, @RequestParam(required=false) String academicYear, @RequestBody Map<String, String> requestBody) {
        String newClassName = requestBody.get("newClassName");
        String newSection = requestBody.get("newSection");
        String newStudentName = requestBody.get("studentName");
        return ResponseEntity.ok((Object)this.studentAdmissionFeeService.updateStudentClassAndFees(admissionNumber, schoolId, academicYear, newClassName, newSection, newStudentName));
    }

    @PostMapping(value={"/student-admission-fee/promote/{schoolId}"})
    public ResponseEntity<ResponseMessage> promoteStudentSingle(@PathVariable String schoolId, @RequestBody Map<String, Object> requestBody) {
        String admissionNumber = (String)requestBody.get("admissionNumber");
        String newClassName = (String)requestBody.get("newClassName");
        String newSection = (String)requestBody.get("newSection");
        String academicYear = (String)requestBody.get("academicYear");
        Boolean includePreviousDue = requestBody.get("includePreviousDue") != null ? (Boolean)requestBody.get("includePreviousDue") : false;
        return ResponseEntity.ok((Object)this.studentAdmissionFeeService.promoteStudentSimple(admissionNumber, newClassName, newSection, academicYear, includePreviousDue.booleanValue(), schoolId));
    }

    @PostMapping(value={"/students/bulk-promote-fee/schools/{schoolId}"})
    public ResponseEntity<ResponseMessage> bulkPromoteStudents(@PathVariable String schoolId, @RequestBody Map<String, Object> requestBody) {
        List admissionNumbers = (List)requestBody.get("admissionNumbers");
        String newClass = (String)requestBody.get("newClass");
        String currentAcademicYear = (String)requestBody.get("currentAcademicYear");
        String newAcademicYear = (String)requestBody.get("newAcademicYear");
        Boolean includePreviousDue = requestBody.get("includePreviousDue") != null ? (Boolean)requestBody.get("includePreviousDue") : false;
        return ResponseEntity.ok((Object)this.studentAdmissionFeeService.promoteBatchStudents(admissionNumbers, newClass, currentAcademicYear, newAcademicYear, includePreviousDue.booleanValue(), schoolId));
    }

    @GetMapping(value={"/student-admission-fee/previous-due/{admissionNumber}/{schoolId}"})
    public ResponseMessage getPreviousDuesByAcademicYear(@PathVariable String admissionNumber, @PathVariable String schoolId) {
        return this.studentAdmissionFeeService.getPreviousDuesByAcademicYear(admissionNumber, schoolId);
    }

    @GetMapping(value={"/student-admission-fee/by-academic-year/{schoolId}/{admissionNumber}"})
    public ResponseEntity<ResponseMessage> getFeesByAcademicYearAndAdmissionNumber(@PathVariable String schoolId, @PathVariable String admissionNumber, @RequestParam String academicYear) {
        return ResponseEntity.ok((Object)this.studentAdmissionFeeService.getStudentAdmissionFeesByAcademicYearAndAdmissionNumber(schoolId, academicYear, admissionNumber));
    }

    @PutMapping(value={"/student-admission-fee/add-service/{admissionNumber}/{schoolId}"})
    public ResponseEntity<ResponseMessage> addServiceToStudentFee(@PathVariable String admissionNumber, @PathVariable String schoolId, @RequestParam(required=false) String academicYear, @RequestBody Services service) {
        return ResponseEntity.ok((Object)this.studentAdmissionFeeService.addServiceToStudentFee(admissionNumber, schoolId, academicYear, service));
    }
}

