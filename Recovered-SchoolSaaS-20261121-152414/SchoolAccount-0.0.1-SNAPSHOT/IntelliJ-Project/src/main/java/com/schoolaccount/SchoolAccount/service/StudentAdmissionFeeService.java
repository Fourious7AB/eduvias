/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.razorpay.Order
 *  com.razorpay.RazorpayClient
 *  com.razorpay.RazorpayException
 *  com.razorpay.Utils
 *  com.schoolaccount.SchoolAccount.model.AccountDetails
 *  com.schoolaccount.SchoolAccount.model.BankTransaction
 *  com.schoolaccount.SchoolAccount.model.FeesStructure
 *  com.schoolaccount.SchoolAccount.model.IncomePage
 *  com.schoolaccount.SchoolAccount.model.ResponseMessage
 *  com.schoolaccount.SchoolAccount.model.Services
 *  com.schoolaccount.SchoolAccount.model.StudentAdmissionFee
 *  com.schoolaccount.SchoolAccount.model.StudentAdmissionFee$TuitionFeePayment
 *  com.schoolaccount.SchoolAccount.model.TuitionFee
 *  com.schoolaccount.SchoolAccount.repo.AccountDetailsRepository
 *  com.schoolaccount.SchoolAccount.repo.BankTransactionRepository
 *  com.schoolaccount.SchoolAccount.repo.FeesStructureRepository
 *  com.schoolaccount.SchoolAccount.repo.IncomePageRepository
 *  com.schoolaccount.SchoolAccount.repo.StudentAdmissionFeeRepository
 *  com.schoolaccount.SchoolAccount.repo.TuitionFeeRepository
 *  com.schoolaccount.SchoolAccount.service.StudentAdmissionFeeService
 *  org.json.JSONObject
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.beans.factory.annotation.Value
 *  org.springframework.http.HttpEntity
 *  org.springframework.http.HttpHeaders
 *  org.springframework.http.HttpMethod
 *  org.springframework.http.MediaType
 *  org.springframework.stereotype.Service
 *  org.springframework.util.MultiValueMap
 *  org.springframework.web.client.RestTemplate
 */
package com.schoolaccount.SchoolAccount.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import com.schoolaccount.SchoolAccount.model.AccountDetails;
import com.schoolaccount.SchoolAccount.model.BankTransaction;
import com.schoolaccount.SchoolAccount.model.FeesStructure;
import com.schoolaccount.SchoolAccount.model.IncomePage;
import com.schoolaccount.SchoolAccount.model.ResponseMessage;
import com.schoolaccount.SchoolAccount.model.Services;
import com.schoolaccount.SchoolAccount.model.StudentAdmissionFee;
import com.schoolaccount.SchoolAccount.model.TuitionFee;
import com.schoolaccount.SchoolAccount.repo.AccountDetailsRepository;
import com.schoolaccount.SchoolAccount.repo.BankTransactionRepository;
import com.schoolaccount.SchoolAccount.repo.FeesStructureRepository;
import com.schoolaccount.SchoolAccount.repo.IncomePageRepository;
import com.schoolaccount.SchoolAccount.repo.StudentAdmissionFeeRepository;
import com.schoolaccount.SchoolAccount.repo.TuitionFeeRepository;
import java.io.Serializable;
import java.lang.invoke.CallSite;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class StudentAdmissionFeeService {
    @Autowired
    private StudentAdmissionFeeRepository studentAdmissionFeeRepository;
    @Autowired
    private TuitionFeeRepository tuitionFeeRepository;
    @Autowired
    private IncomePageRepository incomePageRepository;
    @Autowired
    private BankTransactionRepository bankTransactionRepository;
    @Autowired
    private AccountDetailsRepository accountDetailsRepository;
    @Autowired
    private FeesStructureRepository feesStructureRepository;
    @Value(value="${razorpay.key.id}")
    private String keyId;
    @Value(value="${razorpay.key.secret}")
    private String keySecret;
    @Value(value="${admission.service.url}")
    private String admissionUrl;

    public ResponseMessage createStudentAdmissionFee(StudentAdmissionFee fee) {
        if (this.studentAdmissionFeeRepository.findByAdmissionNumber(fee.getAdmissionNumber()).isPresent()) {
            return new ResponseMessage("Error: Admission Number already exists!", null);
        }
        Optional tuitionFeeOpt = this.tuitionFeeRepository.findByClassName(fee.getClassName());
        if (!tuitionFeeOpt.isPresent()) {
            return new ResponseMessage("Error: No Tuition Fees Found for this Class", null);
        }
        fee.setYearFees(((TuitionFee)tuitionFeeOpt.get()).getYearlyFee());
        if (fee.getTuitionFees() > 0.0) {
            fee.setRemainingFee(fee.getYearFees() - fee.getTuitionFees());
        } else {
            fee.setRemainingFee(fee.getYearFees());
        }
        fee.setStatus(fee.getRemainingFee() == 0.0 ? "Paid" : "Pending");
        fee.setSchoolId("67ca9167d56ec91fc9539234");
        StudentAdmissionFee savedFee = (StudentAdmissionFee)this.studentAdmissionFeeRepository.save((Object)fee);
        this.updateStudentAdmissionStatus(savedFee.getAdmissionNumber());
        this.createIncomeRecord(savedFee);
        this.handleBankTransaction(savedFee);
        return new ResponseMessage("Student Admission Fee Created Successfully", (Object)savedFee);
    }

    public ResponseMessage getAllStudentAdmissionFees() {
        return new ResponseMessage("Fetched All Student Admission Fees", (Object)this.studentAdmissionFeeRepository.findAll());
    }

    public ResponseMessage getStudentAdmissionFeeById(String id) {
        Optional fee = this.studentAdmissionFeeRepository.findById((Object)id);
        return fee.map(value -> new ResponseMessage("Student Admission Fee Found", value)).orElse(new ResponseMessage("Student Admission Fee Not Found", null));
    }

    public ResponseMessage deleteStudentAdmissionFeeById(String id) {
        if (this.studentAdmissionFeeRepository.existsById((Object)id)) {
            this.studentAdmissionFeeRepository.deleteById((Object)id);
            return new ResponseMessage("Student Admission Fee Deleted Successfully", null);
        }
        return new ResponseMessage("Student Admission Fee Not Found", null);
    }

    public ResponseMessage updateTuitionFees(String id, double tuitionFees) {
        Optional feeOpt = this.studentAdmissionFeeRepository.findById((Object)id);
        if (feeOpt.isPresent()) {
            StudentAdmissionFee fee = (StudentAdmissionFee)feeOpt.get();
            if ("Paid".equalsIgnoreCase(fee.getStatus())) {
                return new ResponseMessage("Update Failed: Tuition Fee is already fully paid", null);
            }
            double updatedTuitionFees = fee.getTuitionFees() + tuitionFees;
            if (updatedTuitionFees > fee.getYearFees()) {
                return new ResponseMessage("Error: Tuition Fees cannot exceed Yearly Fees. Current Tuition Fees: " + fee.getTuitionFees() + ", Yearly Fees: " + fee.getYearFees(), null);
            }
            fee.setTuitionFees(updatedTuitionFees);
            fee.setRemainingFee(fee.getYearFees() - updatedTuitionFees);
            if (fee.getRemainingFee() == 0.0) {
                fee.setStatus("Paid");
            } else {
                fee.setStatus("Pending");
            }
            this.studentAdmissionFeeRepository.save((Object)fee);
            if ("online".equalsIgnoreCase(fee.getPaymentMethod())) {
                this.handleOnlinePayment(fee.getAdmissionFees(), fee.getDate());
            }
            return new ResponseMessage("Tuition Fees Updated Successfully", (Object)fee);
        }
        return new ResponseMessage("Student Admission Fee Not Found", null);
    }

    public ResponseMessage addTuitionFeePayment(String id, StudentAdmissionFee.TuitionFeePayment payment) {
        Optional feeOpt = this.studentAdmissionFeeRepository.findById((Object)id);
        if (feeOpt.isPresent()) {
            StudentAdmissionFee fee = (StudentAdmissionFee)feeOpt.get();
            if (fee.getStatus().equals("Paid")) {
                return new ResponseMessage("Error: Tuition Fee already paid in full", null);
            }
            double newTotalPaid = fee.getTuitionFees() + payment.getFee();
            if (newTotalPaid > fee.getYearFees()) {
                return new ResponseMessage("Error: Payment exceeds Yearly Fees", null);
            }
            payment.setCreatedAt(LocalDateTime.now());
            fee.getTuitionFeePayments().add(payment);
            fee.setTuitionFees(newTotalPaid);
            fee.setRemainingFee(fee.getYearFees() - newTotalPaid);
            fee.setStatus(fee.getRemainingFee() == 0.0 ? "Paid" : "Pending");
            this.createTuitionPaymentIncomeRecord(payment, fee.getSchoolId());
            this.studentAdmissionFeeRepository.save((Object)fee);
            if ("online".equalsIgnoreCase(payment.getPaymentType())) {
                this.handleOnlinePayment(payment.getFee(), LocalDate.from(payment.getCreatedAt()));
            }
            return new ResponseMessage("Tuition Fee Payment Added Successfully", (Object)fee);
        }
        return new ResponseMessage("Error: Student Admission Fee Not Found", null);
    }

    public ResponseMessage updateStudentClassAndFees(String admissionNumber, String schoolId, String academicYear, String newClassName, String newSection, String newStudentName) {
        Optional<StudentAdmissionFee> feeOpt;
        if (admissionNumber == null) {
            return new ResponseMessage("Error: Admission number is required", null);
        }
        if (academicYear != null && !academicYear.isEmpty()) {
            feeOpt = this.studentAdmissionFeeRepository.findByAdmissionNumberAndAcademicYearAndSchoolId(admissionNumber, academicYear, schoolId);
        } else {
            List feeRecords = this.studentAdmissionFeeRepository.findAllByAdmissionNumberAndSchoolId(admissionNumber, schoolId);
            if (feeRecords.isEmpty()) {
                return new ResponseMessage("No fee records found for student with admission number " + admissionNumber, null);
            }
            feeRecords.sort((a, b) -> {
                if (a.getAcademicYear() == null || b.getAcademicYear() == null) {
                    return 0;
                }
                return b.getAcademicYear().compareTo(a.getAcademicYear());
            });
            feeOpt = Optional.of((StudentAdmissionFee)feeRecords.get(0));
        }
        if (feeOpt.isEmpty()) {
            return new ResponseMessage("No fee record found for student with admission number " + admissionNumber + " for the specified criteria", null);
        }
        StudentAdmissionFee fee = (StudentAdmissionFee)feeOpt.get();
        String oldClassName = fee.getClassName();
        String oldStudentName = fee.getStudentName();
        double oldYearFees = fee.getYearFees();
        boolean classUpdated = false;
        boolean nameUpdated = false;
        if (newStudentName != null && !newStudentName.isEmpty() && !newStudentName.equals(oldStudentName)) {
            fee.setStudentName(newStudentName);
            nameUpdated = true;
        }
        if (newClassName != null && !newClassName.isEmpty() && !newClassName.equals(oldClassName)) {
            Optional tuitionFeeOpt = this.tuitionFeeRepository.findByClassNameAndSchoolId(newClassName, schoolId);
            if (!tuitionFeeOpt.isPresent()) {
                return new ResponseMessage("No tuition fee found for the new class: " + newClassName, null);
            }
            TuitionFee tuitionFee = (TuitionFee)tuitionFeeOpt.get();
            double newYearFees = tuitionFee.getYearlyFee();
            double paidPercentage = 0.0;
            if (oldYearFees > 0.0) {
                paidPercentage = (oldYearFees - fee.getRemainingFee()) / oldYearFees;
            }
            fee.setClassName(newClassName);
            classUpdated = true;
            fee.setYearFees(newYearFees);
            double newRemainingFee = newYearFees * (1.0 - paidPercentage);
            fee.setRemainingFee(newRemainingFee);
            fee.setStatus(newRemainingFee == 0.0 ? "Paid" : "Pending");
            this.updateStudentClassInAdmissionService(admissionNumber, newClassName, schoolId);
        }
        if (newSection != null && !newSection.isEmpty()) {
            fee.setSection(newSection);
        }
        StudentAdmissionFee updatedFee = (StudentAdmissionFee)this.studentAdmissionFeeRepository.save((Object)fee);
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("updatedFee", updatedFee);
        if (classUpdated) {
            result.put("oldClassName", oldClassName);
            result.put("newClassName", newClassName);
            if (fee.getYearFees() != 0.0) {
                result.put("oldYearFees", oldYearFees);
                result.put("newYearFees", fee.getYearFees());
                result.put("feeChange", fee.getYearFees() - oldYearFees);
            }
        }
        if (nameUpdated) {
            result.put("oldStudentName", oldStudentName);
            result.put("newStudentName", newStudentName);
        }
        Object message = "";
        message = nameUpdated && classUpdated ? "Student name updated from '" + oldStudentName + "' to '" + newStudentName + "' and class updated from '" + oldClassName + "' to '" + newClassName + "'. Fees recalculated successfully." : (nameUpdated ? "Student name updated from '" + oldStudentName + "' to '" + newStudentName + "'." : (classUpdated ? "Student class updated from '" + oldClassName + "' to '" + newClassName + "' and fees recalculated successfully." : (newSection != null && !newSection.isEmpty() ? "Student section updated to '" + newSection + "'." : "No changes were made.")));
        return new ResponseMessage((String)message, result);
    }

    public ResponseMessage getStudentAdmissionFeeByAdmissionNumber(String admissionNumber) {
        Optional feeOpt = this.studentAdmissionFeeRepository.findByAdmissionNumber(admissionNumber);
        return feeOpt.map(fee -> new ResponseMessage("Student Admission Fee Found", fee)).orElse(new ResponseMessage("Student Admission Fee Not Found", null));
    }

    public ResponseMessage createStudentAdmissionFeeWithSchool(StudentAdmissionFee fee, String schoolId) {
        if (this.studentAdmissionFeeRepository.findByAdmissionNumberAndSchoolId(fee.getAdmissionNumber(), schoolId).isPresent()) {
            return new ResponseMessage("Error: Admission Number already exists in this school!", null);
        }
        Optional tuitionFeeOpt = this.tuitionFeeRepository.findByClassNameAndSchoolId(fee.getClassName(), schoolId);
        if (!tuitionFeeOpt.isPresent()) {
            return new ResponseMessage("Error: No Tuition Fees Found for this Class in this school", null);
        }
        fee.setYearFees(((TuitionFee)tuitionFeeOpt.get()).getYearlyFee());
        if (fee.getTuitionFees() > 0.0) {
            fee.setRemainingFee(fee.getYearFees() - fee.getTuitionFees());
        } else {
            fee.setRemainingFee(fee.getYearFees());
        }
        fee.setStatus(fee.getRemainingFee() == 0.0 ? "Paid" : "Pending");
        fee.setSchoolId(schoolId);
        StudentAdmissionFee savedFee = (StudentAdmissionFee)this.studentAdmissionFeeRepository.save((Object)fee);
        this.updateStudentAdmissionStatusWithSchool(savedFee.getAdmissionNumber(), schoolId);
        this.createIncomeRecord(savedFee);
        this.handleBankTransaction(savedFee);
        return new ResponseMessage("Student Admission Fee Created Successfully", (Object)savedFee);
    }

    public ResponseMessage getAllStudentAdmissionFeesBySchool(String schoolId) {
        List fees = this.studentAdmissionFeeRepository.findBySchoolId(schoolId);
        return new ResponseMessage("Fetched All Student Admission Fees for School", (Object)fees);
    }

    public ResponseMessage getStudentAdmissionFeeByIdAndSchool(String id, String schoolId) {
        Optional fee = this.studentAdmissionFeeRepository.findById((Object)id);
        if (fee.isPresent() && ((StudentAdmissionFee)fee.get()).getSchoolId().equals(schoolId)) {
            return new ResponseMessage("Student Admission Fee Found", fee.get());
        }
        return new ResponseMessage("Student Admission Fee Not Found or does not belong to this school", null);
    }

    public ResponseMessage deleteStudentAdmissionFeeByIdAndSchool(String id, String schoolId) {
        Optional fee = this.studentAdmissionFeeRepository.findById((Object)id);
        if (fee.isPresent() && ((StudentAdmissionFee)fee.get()).getSchoolId().equals(schoolId)) {
            this.studentAdmissionFeeRepository.deleteById((Object)id);
            return new ResponseMessage("Student Admission Fee Deleted Successfully", null);
        }
        return new ResponseMessage("Student Admission Fee Not Found or does not belong to this school", null);
    }

    private void updateStudentAdmissionStatusWithSchool(String admissionNumber, String schoolId) {
        System.out.println(admissionNumber + schoolId);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HashMap<String, String> requestBody = new HashMap<String, String>();
        requestBody.put("admissionNumber", admissionNumber);
        requestBody.put("status", "Complete");
        HttpEntity request = new HttpEntity(requestBody, (MultiValueMap)headers);
        String admissionServiceUrl = this.admissionUrl + schoolId;
        try {
            restTemplate.exchange(admissionServiceUrl, HttpMethod.PUT, request, String.class, new Object[0]);
            System.out.println("hit the api " + admissionServiceUrl);
        }
        catch (Exception e) {
            System.err.println("Error updating admission status: " + e.getMessage());
        }
    }

    private void updateStudentAdmissionStatus(String admissionNumber) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HashMap<String, String> requestBody = new HashMap<String, String>();
        requestBody.put("status", "Complete");
        HttpEntity request = new HttpEntity(requestBody, (MultiValueMap)headers);
        String admissionServiceUrl = this.admissionUrl + admissionNumber;
        try {
            restTemplate.exchange(admissionServiceUrl, HttpMethod.PUT, request, String.class, new Object[0]);
        }
        catch (Exception e) {
            System.err.println("Error updating admission status: " + e.getMessage());
        }
    }

    private void createIncomeRecord(StudentAdmissionFee fee) {
        IncomePage income = new IncomePage();
        double total = fee.getAdmissionFees() + fee.getTuitionFees() + fee.getLateFees() + fee.getBooks() + fee.getDonation() + fee.getIdCardCharges() + fee.getUniformCharges() + fee.getMiscellaneous() + fee.getFine() + fee.getLateFees() + fee.getTransportationalFees() + fee.getOther();
        income.setAmount(total);
        income.setIncomeSource("Student Admission Fee");
        income.setPaymentMethod(fee.getPaymentMethod());
        income.setDescription("Student Admission Fee Collection");
        income.setDate(fee.getDate());
        income.setSchoolId(fee.getSchoolId());
        this.incomePageRepository.save((Object)income);
    }

    private void handleBankTransaction(StudentAdmissionFee fee) {
        if ("online".equalsIgnoreCase(fee.getPaymentMethod())) {
            double total = fee.getAdmissionFees() + fee.getTuitionFees() + fee.getLateFees() + fee.getBooks() + fee.getDonation() + fee.getIdCardCharges() + fee.getUniformCharges() + fee.getMiscellaneous() + fee.getFine() + fee.getLateFees() + fee.getTransportationalFees() + fee.getOther();
            this.handleOnlinePayment(total, fee.getDate());
        }
    }

    private void handleOnlinePayment(double amount, LocalDate date) {
        AccountDetails accountDetails = (AccountDetails)this.accountDetailsRepository.findFirstByOrderByIdAsc().orElseThrow(() -> new RuntimeException("No account details found"));
        BankTransaction bankTransaction = new BankTransaction();
        bankTransaction.setTransactionType("DEPOSIT");
        bankTransaction.setAmount(amount);
        bankTransaction.setDate(date);
        bankTransaction.setAccountNumber(accountDetails.getAccountNumber());
        bankTransaction.setDescription("Student fee collection");
        this.bankTransactionRepository.save((Object)bankTransaction);
        this.updateBankOpeningAmount(amount);
    }

    private void updateBankOpeningAmount(double amount) {
        AccountDetails accountDetails = (AccountDetails)this.accountDetailsRepository.findFirstByOrderByIdAsc().orElseThrow(() -> new RuntimeException("No account details found"));
        accountDetails.setOpeningAmount(accountDetails.getOpeningAmount() + amount);
        this.accountDetailsRepository.save((Object)accountDetails);
    }

    private void createTuitionPaymentIncomeRecord(StudentAdmissionFee.TuitionFeePayment payment, String schoolId) {
        IncomePage income = new IncomePage();
        income.setAmount(payment.getFee());
        income.setIncomeSource("Tuition Fee Payment");
        income.setPaymentMethod(payment.getPaymentType());
        income.setDescription("Tuition Fee Payment");
        income.setDate(LocalDate.from(payment.getCreatedAt()));
        income.setSchoolId(schoolId);
        this.incomePageRepository.save((Object)income);
    }

    public ResponseMessage updateTuitionFeesWithSchool(String id, double tuitionFees, String schoolId) {
        Optional feeOpt = this.studentAdmissionFeeRepository.findById((Object)id);
        if (feeOpt.isPresent() && ((StudentAdmissionFee)feeOpt.get()).getSchoolId().equals(schoolId)) {
            StudentAdmissionFee fee = (StudentAdmissionFee)feeOpt.get();
            if ("Paid".equalsIgnoreCase(fee.getStatus())) {
                return new ResponseMessage("Update Failed: Tuition Fee is already fully paid", null);
            }
            double updatedTuitionFees = fee.getTuitionFees() + tuitionFees;
            if (updatedTuitionFees > fee.getYearFees()) {
                return new ResponseMessage("Error: Tuition Fees cannot exceed Yearly Fees. Current Tuition Fees: " + fee.getTuitionFees() + ", Yearly Fees: " + fee.getYearFees(), null);
            }
            fee.setTuitionFees(updatedTuitionFees);
            fee.setRemainingFee(fee.getYearFees() - updatedTuitionFees);
            if (fee.getRemainingFee() == 0.0) {
                fee.setStatus("Paid");
            } else {
                fee.setStatus("Pending");
            }
            this.studentAdmissionFeeRepository.save((Object)fee);
            if ("online".equalsIgnoreCase(fee.getPaymentMethod())) {
                this.handleOnlinePayment(tuitionFees, LocalDate.now());
            }
            return new ResponseMessage("Tuition Fees Updated Successfully", (Object)fee);
        }
        return new ResponseMessage("Student Admission Fee Not Found or does not belong to this school", null);
    }

    public ResponseMessage addTuitionFeePaymentWithSchool(String id, StudentAdmissionFee.TuitionFeePayment payment, String schoolId) {
        Optional feeOpt = this.studentAdmissionFeeRepository.findById((Object)id);
        if (feeOpt.isPresent() && ((StudentAdmissionFee)feeOpt.get()).getSchoolId().equals(schoolId)) {
            StudentAdmissionFee fee = (StudentAdmissionFee)feeOpt.get();
            if (fee.getStatus().equals("Paid")) {
                return new ResponseMessage("Error: Tuition Fee already paid in full", null);
            }
            double newTotalPaid = fee.getTuitionFees() + payment.getFee();
            if (newTotalPaid > fee.getYearFees()) {
                return new ResponseMessage("Error: Payment exceeds Yearly Fees", null);
            }
            payment.setCreatedAt(LocalDateTime.now());
            fee.getTuitionFeePayments().add(payment);
            fee.setTuitionFees(newTotalPaid);
            fee.setRemainingFee(fee.getYearFees() - newTotalPaid);
            fee.setStatus(fee.getRemainingFee() == 0.0 ? "Paid" : "Pending");
            IncomePage income = new IncomePage();
            income.setAmount(payment.getFee());
            income.setIncomeSource("Tuition Fee Payment");
            income.setPaymentMethod(payment.getPaymentType());
            income.setDescription("Tuition Fee Payment");
            income.setDate(LocalDate.from(payment.getCreatedAt()));
            income.setSchoolId(schoolId);
            this.incomePageRepository.save((Object)income);
            this.studentAdmissionFeeRepository.save((Object)fee);
            if ("online".equalsIgnoreCase(payment.getPaymentType())) {
                this.handleOnlinePayment(payment.getFee(), LocalDate.from(payment.getCreatedAt()));
            }
            return new ResponseMessage("Tuition Fee Payment Added Successfully", (Object)fee);
        }
        return new ResponseMessage("Error: Student Admission Fee Not Found or does not belong to this school", null);
    }

    public ResponseMessage getStudentFeeHistory(String admissionNumber, String schoolId) {
        List feeHistory = this.studentAdmissionFeeRepository.findAllByAdmissionNumberAndSchoolId(admissionNumber, schoolId);
        if (feeHistory.isEmpty()) {
            return new ResponseMessage("No fee records found for student with admission number " + admissionNumber, null);
        }
        feeHistory.sort((a, b) -> {
            if (a.getAcademicYear() == null || b.getAcademicYear() == null) {
                return 0;
            }
            return b.getAcademicYear().compareTo(a.getAcademicYear());
        });
        return new ResponseMessage("Student fee history retrieved successfully", (Object)feeHistory);
    }

    public ResponseMessage promoteStudentSimple(String admissionNumber, String newClassName, String newSection, String newAcademicYear, boolean includePreviousDue, String schoolId) {
        StudentAdmissionFee.TuitionFeePayment transferPayment;
        if (admissionNumber == null || newClassName == null || newAcademicYear == null) {
            return new ResponseMessage("Error: Required fields are missing", null);
        }
        List studentFees = this.studentAdmissionFeeRepository.findAllByAdmissionNumberAndSchoolId(admissionNumber, schoolId);
        if (studentFees.isEmpty()) {
            return new ResponseMessage("Error: No fee records found for this student", null);
        }
        studentFees.sort((a, b) -> {
            if (a.getAcademicYear() == null || b.getAcademicYear() == null) {
                return 0;
            }
            return b.getAcademicYear().compareTo(a.getAcademicYear());
        });
        StudentAdmissionFee currentFee = (StudentAdmissionFee)studentFees.get(0);
        if (currentFee.getRemainingFee() > 0.0 && !includePreviousDue) {
            return new ResponseMessage("Cannot promote student with outstanding dues of " + currentFee.getRemainingFee() + ". Please clear dues or enable 'includePreviousDue' ", null);
        }
        boolean recordExists = this.studentAdmissionFeeRepository.findByAdmissionNumberAndAcademicYearAndSchoolId(admissionNumber, newAcademicYear, schoolId).isPresent();
        if (recordExists) {
            return new ResponseMessage("Student already has a fee record for the new academic year", null);
        }
        Optional tuitionFeeOpt = this.tuitionFeeRepository.findByClassNameAndSchoolId(newClassName, schoolId);
        if (!tuitionFeeOpt.isPresent()) {
            return new ResponseMessage("No tuition fee found for the new class", null);
        }
        TuitionFee tuitionFee = (TuitionFee)tuitionFeeOpt.get();
        Optional feesStructureOpt = this.feesStructureRepository.findByClassNameIgnoreCaseAndSchoolId(newClassName, schoolId);
        StudentAdmissionFee newFee = new StudentAdmissionFee();
        newFee.setAdmissionNumber(admissionNumber);
        newFee.setStudentName(currentFee.getStudentName());
        newFee.setClassName(newClassName);
        newFee.setSection(newSection != null ? newSection : currentFee.getSection());
        newFee.setSchoolId(schoolId);
        newFee.setAcademicYear(newAcademicYear);
        newFee.setTuitionFeePayments(new ArrayList());
        double yearFees = tuitionFee.getYearlyFee();
        newFee.setYearFees(yearFees);
        double previousDue = 0.0;
        if (includePreviousDue && currentFee.getRemainingFee() > 0.0) {
            previousDue = currentFee.getRemainingFee();
            currentFee.setStatus("Transferred to " + newAcademicYear);
            currentFee.setRemainingFee(0.0);
            if (currentFee.getTuitionFeePayments() == null) {
                currentFee.setTuitionFeePayments(new ArrayList());
            }
            transferPayment = new StudentAdmissionFee.TuitionFeePayment();
            System.out.println("i m from transfer pa : " + transferPayment);
            transferPayment.setFee(previousDue);
            transferPayment.setPaymentType("Transfer");
            transferPayment.setTransactionNo("DueTransfer-" + newAcademicYear);
            transferPayment.setCreatedAt(LocalDateTime.now());
            currentFee.getTuitionFeePayments().add(transferPayment);
            System.out.println("Adding due transfer payment for " + admissionNumber + " to " + newAcademicYear);
            this.studentAdmissionFeeRepository.save((Object)currentFee);
            this.createDueTransferRecord(currentFee, newAcademicYear, previousDue, schoolId);
        }
        if (feesStructureOpt.isPresent()) {
            FeesStructure feesStructure = (FeesStructure)feesStructureOpt.get();
            newFee.setAdmissionFees(feesStructure.getAdmission_Fees());
            newFee.setBooks(feesStructure.getBooks());
            newFee.setDonation(feesStructure.getDonation());
            newFee.setFine(feesStructure.getFine());
            newFee.setIdCardCharges(feesStructure.getId_Card_Charges());
            newFee.setLateFees(feesStructure.getLate_Fees());
            newFee.setMiscellaneous(feesStructure.getMiscellaneous());
            newFee.setTransportationalFees(feesStructure.getTransportationalFees());
            newFee.setTuitionFees(feesStructure.getTuitionFees());
            newFee.setUniformCharges(feesStructure.getUniform_Charges());
            if (includePreviousDue && previousDue > 0.0) {
                newFee.setOther(feesStructure.getOther() + previousDue);
                StudentAdmissionFee.TuitionFeePayment transferPayment2 = new StudentAdmissionFee.TuitionFeePayment();
                transferPayment2.setFee(previousDue);
                transferPayment2.setPaymentType("Due Transfer");
                transferPayment2.setTransactionNo("FromPrevious-" + currentFee.getAcademicYear());
                transferPayment2.setCreatedAt(LocalDateTime.now());
                newFee.getTuitionFeePayments().add(transferPayment2);
            } else {
                newFee.setOther(feesStructure.getOther());
            }
        } else {
            newFee.setAdmissionFees(0.0);
            newFee.setBooks(0.0);
            newFee.setDonation(0.0);
            newFee.setFine(0.0);
            newFee.setIdCardCharges(0.0);
            newFee.setLateFees(0.0);
            newFee.setMiscellaneous(0.0);
            newFee.setTransportationalFees(0.0);
            newFee.setTuitionFees(0.0);
            newFee.setUniformCharges(0.0);
            if (includePreviousDue && previousDue > 0.0) {
                newFee.setOther(previousDue);
                transferPayment = new StudentAdmissionFee.TuitionFeePayment();
                transferPayment.setFee(previousDue);
                transferPayment.setPaymentType("Due Transfer");
                transferPayment.setTransactionNo("FromPrevious-" + currentFee.getAcademicYear());
                transferPayment.setCreatedAt(LocalDateTime.now());
                newFee.getTuitionFeePayments().add(transferPayment);
            } else {
                newFee.setOther(0.0);
            }
        }
        double totalFees = newFee.getAdmissionFees() + newFee.getBooks() + newFee.getDonation() + newFee.getIdCardCharges() + newFee.getUniformCharges() + newFee.getTransportationalFees() + newFee.getOther() + newFee.getFine() + newFee.getLateFees() + newFee.getMiscellaneous() + newFee.getTuitionFees() + yearFees;
        newFee.setRemainingFee(totalFees);
        newFee.setStatus("Pending");
        newFee.setDate(LocalDate.now());
        StudentAdmissionFee savedFee = (StudentAdmissionFee)this.studentAdmissionFeeRepository.save((Object)newFee);
        this.updateStudentClassInAdmissionService(admissionNumber, newClassName, schoolId);
        return new ResponseMessage("Student promoted successfully to " + newClassName + " for academic year " + newAcademicYear, (Object)savedFee);
    }

    private void createDueTransferRecord(StudentAdmissionFee oldFee, String newAcademicYear, double amount, String schoolId) {
        IncomePage transferRecord = new IncomePage();
        transferRecord.setAmount(amount);
        transferRecord.setIncomeSource("Due Transfer");
        transferRecord.setPaymentMethod("Transfer");
        transferRecord.setDescription("Due transferred from " + oldFee.getAcademicYear() + " to " + newAcademicYear + " for student " + oldFee.getStudentName() + " (" + oldFee.getAdmissionNumber() + ")");
        transferRecord.setDate(LocalDate.now());
        transferRecord.setSchoolId(schoolId);
        this.incomePageRepository.save((Object)transferRecord);
    }

    public ResponseMessage promoteBatchStudents(List<String> admissionNumbers, String newClass, String currentAcademicYear, String newAcademicYear, boolean includePreviousDue, String schoolId) {
        if (admissionNumbers == null || admissionNumbers.isEmpty() || newClass == null || currentAcademicYear == null || newAcademicYear == null) {
            return new ResponseMessage("Error: All fields are required", null);
        }
        Optional tuitionFeeOpt = this.tuitionFeeRepository.findByClassNameAndSchoolId(newClass, schoolId);
        if (!tuitionFeeOpt.isPresent()) {
            return new ResponseMessage("Error: No tuition fee found for the new class", null);
        }
        TuitionFee tuitionFee = (TuitionFee)tuitionFeeOpt.get();
        Optional feesStructureOpt = this.feesStructureRepository.findByClassNameIgnoreCaseAndSchoolId(newClass, schoolId);
        ArrayList<StudentAdmissionFee> promotedStudents = new ArrayList<StudentAdmissionFee>();
        ArrayList<CallSite> failedPromotions = new ArrayList<CallSite>();
        ArrayList<CallSite> dueTransfers = new ArrayList<CallSite>();
        ArrayList<CallSite> skippedDueToOutstandingDues = new ArrayList<CallSite>();
        for (String admissionNumber : admissionNumbers) {
            Optional currentFeeOpt = this.studentAdmissionFeeRepository.findByAdmissionNumberAndAcademicYearAndSchoolId(admissionNumber, currentAcademicYear, schoolId);
            if (!currentFeeOpt.isPresent()) {
                failedPromotions.add((CallSite)((Object)(admissionNumber + " - No fee record found for current academic year")));
                continue;
            }
            StudentAdmissionFee currentFee = (StudentAdmissionFee)currentFeeOpt.get();
            if (currentFee.getRemainingFee() > 0.0 && !includePreviousDue) {
                skippedDueToOutstandingDues.add((CallSite)((Object)(admissionNumber + " - Has outstanding dues of " + currentFee.getRemainingFee())));
                continue;
            }
            boolean recordExists = this.studentAdmissionFeeRepository.findByAdmissionNumberAndAcademicYearAndSchoolId(admissionNumber, newAcademicYear, schoolId).isPresent();
            if (recordExists) {
                failedPromotions.add((CallSite)((Object)(admissionNumber + " - Already has a fee record for the new academic year")));
                continue;
            }
            double previousDue = 0.0;
            if (includePreviousDue && currentFee.getRemainingFee() > 0.0) {
                previousDue = currentFee.getRemainingFee();
                currentFee.setStatus("Transferred to " + newAcademicYear);
                currentFee.setRemainingFee(0.0);
                if (currentFee.getTuitionFeePayments() == null) {
                    currentFee.setTuitionFeePayments(new ArrayList());
                }
                StudentAdmissionFee.TuitionFeePayment transferPayment = new StudentAdmissionFee.TuitionFeePayment();
                transferPayment.setFee(previousDue);
                transferPayment.setPaymentType("Transfer");
                transferPayment.setTransactionNo("DueTransfer-" + newAcademicYear);
                transferPayment.setCreatedAt(LocalDateTime.now());
                currentFee.getTuitionFeePayments().add(transferPayment);
                this.studentAdmissionFeeRepository.save((Object)currentFee);
                this.createDueTransferRecord(currentFee, newAcademicYear, previousDue, schoolId);
                dueTransfers.add((CallSite)((Object)(admissionNumber + " - Transferred due of " + previousDue)));
            }
            StudentAdmissionFee newFee = new StudentAdmissionFee();
            newFee.setAdmissionNumber(admissionNumber);
            newFee.setStudentName(currentFee.getStudentName());
            newFee.setClassName(newClass);
            newFee.setSection(currentFee.getSection());
            newFee.setSchoolId(schoolId);
            newFee.setAcademicYear(newAcademicYear);
            double yearFees = tuitionFee.getYearlyFee();
            newFee.setYearFees(yearFees);
            if (feesStructureOpt.isPresent()) {
                FeesStructure feesStructure = (FeesStructure)feesStructureOpt.get();
                newFee.setAdmissionFees(feesStructure.getAdmission_Fees());
                newFee.setBooks(feesStructure.getBooks());
                newFee.setDonation(feesStructure.getDonation());
                newFee.setFine(feesStructure.getFine());
                newFee.setIdCardCharges(feesStructure.getId_Card_Charges());
                newFee.setLateFees(feesStructure.getLate_Fees());
                newFee.setMiscellaneous(feesStructure.getMiscellaneous());
                newFee.setTransportationalFees(feesStructure.getTransportationalFees());
                newFee.setTuitionFees(feesStructure.getTuitionFees());
                newFee.setUniformCharges(feesStructure.getUniform_Charges());
                if (previousDue > 0.0) {
                    newFee.setOther(feesStructure.getOther() + previousDue);
                    StudentAdmissionFee.TuitionFeePayment transferPayment = new StudentAdmissionFee.TuitionFeePayment();
                    transferPayment.setFee(previousDue);
                    transferPayment.setPaymentType("Due Transfer");
                    transferPayment.setTransactionNo("FromPrevious-" + currentFee.getAcademicYear());
                    transferPayment.setCreatedAt(LocalDateTime.now());
                    newFee.getTuitionFeePayments().add(transferPayment);
                } else {
                    newFee.setOther(feesStructure.getOther());
                }
            } else {
                newFee.setAdmissionFees(currentFee.getAdmissionFees());
                newFee.setBooks(currentFee.getBooks());
                newFee.setDonation(currentFee.getDonation());
                newFee.setFine(currentFee.getFine());
                newFee.setIdCardCharges(currentFee.getIdCardCharges());
                newFee.setLateFees(currentFee.getLateFees());
                newFee.setMiscellaneous(currentFee.getMiscellaneous());
                newFee.setTransportationalFees(currentFee.getTransportationalFees());
                newFee.setTuitionFees(0.0);
                newFee.setUniformCharges(currentFee.getUniformCharges());
                if (previousDue > 0.0) {
                    newFee.setOther(currentFee.getOther() + previousDue);
                    StudentAdmissionFee.TuitionFeePayment transferPayment = new StudentAdmissionFee.TuitionFeePayment();
                    transferPayment.setFee(previousDue);
                    transferPayment.setPaymentType("Due Transfer");
                    transferPayment.setTransactionNo("FromPrevious-" + currentFee.getAcademicYear());
                    transferPayment.setCreatedAt(LocalDateTime.now());
                    newFee.getTuitionFeePayments().add(transferPayment);
                } else {
                    newFee.setOther(currentFee.getOther());
                }
            }
            double totalFees = newFee.getAdmissionFees() + newFee.getBooks() + newFee.getDonation() + newFee.getIdCardCharges() + newFee.getUniformCharges() + newFee.getTransportationalFees() + newFee.getOther() + newFee.getFine() + newFee.getLateFees() + newFee.getMiscellaneous() + yearFees;
            newFee.setRemainingFee(totalFees);
            newFee.setStatus("Pending");
            newFee.setDate(LocalDate.now());
            StudentAdmissionFee savedFee = (StudentAdmissionFee)this.studentAdmissionFeeRepository.save((Object)newFee);
            promotedStudents.add(savedFee);
            this.updateStudentClassInAdmissionService(admissionNumber, newClass, schoolId);
        }
        HashMap<String, Serializable> result = new HashMap<String, Serializable>();
        result.put("promotedStudents", promotedStudents);
        result.put("failedPromotions", failedPromotions);
        result.put("dueTransfers", dueTransfers);
        result.put("skippedDueToOutstandingDues", skippedDueToOutstandingDues);
        result.put("totalPromoted", Integer.valueOf(promotedStudents.size()));
        result.put("totalFailed", Integer.valueOf(failedPromotions.size()));
        result.put("totalDueTransfers", Integer.valueOf(dueTransfers.size()));
        result.put("totalSkippedDueToOutstandingDues", Integer.valueOf(skippedDueToOutstandingDues.size()));
        return new ResponseMessage("Batch promotion completed: " + promotedStudents.size() + " students promoted, " + failedPromotions.size() + " failed, " + dueTransfers.size() + " due transfers, " + skippedDueToOutstandingDues.size() + " skipped due to outstanding dues", result);
    }

    private void updateStudentClassInAdmissionService(String admissionNumber, String newClass, String schoolId) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HashMap<String, String> requestBody = new HashMap<String, String>();
        requestBody.put("className", newClass);
        HttpEntity request = new HttpEntity(requestBody, (MultiValueMap)headers);
        String admissionServiceUrl = this.admissionUrl + admissionNumber + "/" + schoolId;
        try {
            restTemplate.exchange(admissionServiceUrl, HttpMethod.PUT, request, String.class, new Object[0]);
            System.out.println("Updated student class in Admission Service: " + admissionNumber + " to " + newClass);
        }
        catch (Exception e) {
            System.err.println("Error updating student class in Admission Service: " + e.getMessage());
        }
    }

    public ResponseMessage getPreviousDuesByAcademicYear(String admissionNumber, String schoolId) {
        List feeRecords = this.studentAdmissionFeeRepository.findAllByAdmissionNumberAndSchoolId(admissionNumber, schoolId);
        if (feeRecords.isEmpty()) {
            return new ResponseMessage("No fee records found for student with admission number " + admissionNumber, null);
        }
        LinkedHashMap duesByYear = new LinkedHashMap();
        double totalOutstandingDue = 0.0;
        feeRecords.sort((a, b) -> {
            if (a.getAcademicYear() == null || b.getAcademicYear() == null) {
                return 0;
            }
            return a.getAcademicYear().compareTo(b.getAcademicYear());
        });
        for (StudentAdmissionFee fee : feeRecords) {
            if (!(fee.getRemainingFee() > 0.0)) continue;
            HashMap<String, Object> dueInfo = new HashMap<String, Object>();
            dueInfo.put("remainingFee", fee.getRemainingFee());
            dueInfo.put("className", fee.getClassName());
            dueInfo.put("section", fee.getSection());
            dueInfo.put("status", fee.getStatus());
            dueInfo.put("date", fee.getDate());
            dueInfo.put("studentName", fee.getStudentName());
            duesByYear.put(fee.getAcademicYear(), dueInfo);
            totalOutstandingDue += fee.getRemainingFee();
        }
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("admissionNumber", admissionNumber);
        result.put("studentName", ((StudentAdmissionFee)feeRecords.get(0)).getStudentName());
        result.put("totalOutstandingDue", totalOutstandingDue);
        result.put("duesByAcademicYear", duesByYear);
        return new ResponseMessage("Previous dues retrieved successfully", result);
    }

    public ResponseMessage getStudentAdmissionFeesByAcademicYearAndAdmissionNumber(String schoolId, String academicYear, String admissionNumber) {
        List fees = this.studentAdmissionFeeRepository.findBySchoolIdAndAcademicYearAndAdmissionNumber(schoolId, academicYear, admissionNumber);
        if (fees.isEmpty()) {
            return new ResponseMessage("No fee records found for student " + admissionNumber + " in academic year " + academicYear, null);
        }
        return new ResponseMessage("Retrieved fee records for student " + admissionNumber + " in academic year " + academicYear, (Object)fees);
    }

    public ResponseMessage addServiceToStudentFee(String admissionNumber, String schoolId, String academicYear, Services service) {
        Optional<StudentAdmissionFee> feeOpt;
        if (academicYear != null && !academicYear.isEmpty()) {
            feeOpt = this.studentAdmissionFeeRepository.findByAdmissionNumberAndAcademicYearAndSchoolId(admissionNumber, academicYear, schoolId);
        } else {
            List feeRecords = this.studentAdmissionFeeRepository.findAllByAdmissionNumberAndSchoolId(admissionNumber, schoolId);
            if (feeRecords.isEmpty()) {
                return new ResponseMessage("No fee records found for student with admission number " + admissionNumber, null);
            }
            feeRecords.sort((a, b) -> {
                if (a.getAcademicYear() == null || b.getAcademicYear() == null) {
                    return 0;
                }
                return b.getAcademicYear().compareTo(a.getAcademicYear());
            });
            feeOpt = Optional.of((StudentAdmissionFee)feeRecords.get(0));
        }
        if (feeOpt.isEmpty()) {
            return new ResponseMessage("No fee record found for student with admission number " + admissionNumber + " for the specified criteria", null);
        }
        StudentAdmissionFee fee = (StudentAdmissionFee)feeOpt.get();
        if (service.getDate() == null) {
            service.setDate(LocalDate.now());
        }
        fee.getServices().add(service);
        fee.setRemainingFee(fee.getRemainingFee() + service.getCharge());
        if ("Paid".equals(fee.getStatus()) && fee.getRemainingFee() > 0.0) {
            fee.setStatus("Pending");
        }
        StudentAdmissionFee updatedFee = (StudentAdmissionFee)this.studentAdmissionFeeRepository.save((Object)fee);
        this.createServiceIncomeRecord(service, fee.getSchoolId(), fee.getStudentName(), fee.getAdmissionNumber());
        return new ResponseMessage("Service added successfully to student fee", (Object)updatedFee);
    }

    private void createServiceIncomeRecord(Services service, String schoolId, String studentName, String admissionNumber) {
        IncomePage income = new IncomePage();
        income.setAmount(service.getCharge());
        income.setIncomeSource(service.getServiceType() + " Fee");
        income.setPaymentMethod("services fee");
        income.setDescription("Service: " + service.getDescription() + " for student " + studentName + " (" + admissionNumber + ")");
        income.setDate(service.getDate());
        income.setSchoolId(schoolId);
        this.incomePageRepository.save((Object)income);
    }

    public ResponseMessage calculateTotalDues(String admissionNumber, String schoolId) {
        List feeRecords = this.studentAdmissionFeeRepository.findAllByAdmissionNumberAndSchoolId(admissionNumber, schoolId);
        if (feeRecords.isEmpty()) {
            return new ResponseMessage("No fee records found for student with admission number " + admissionNumber, null);
        }
        double totalDues = 0.0;
        LinkedHashMap duesByYear = new LinkedHashMap();
        String studentName = "";
        for (StudentAdmissionFee fee : feeRecords) {
            if (fee.getRemainingFee() > 0.0) {
                totalDues += fee.getRemainingFee();
                HashMap<String, Object> yearData = new HashMap<String, Object>();
                yearData.put("remainingFee", fee.getRemainingFee());
                yearData.put("className", fee.getClassName());
                yearData.put("section", fee.getSection());
                yearData.put("status", fee.getStatus());
                String academicYear = fee.getAcademicYear();
                if (academicYear == null || academicYear.isEmpty()) {
                    academicYear = "Unknown Year";
                }
                duesByYear.put(academicYear, yearData);
            }
            if (!studentName.isEmpty() || fee.getStudentName() == null) continue;
            studentName = fee.getStudentName();
        }
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("admissionNumber", admissionNumber);
        result.put("studentName", studentName);
        result.put("totalDues", totalDues);
        result.put("duesByYear", duesByYear);
        return new ResponseMessage("Total dues calculated for student " + admissionNumber, result);
    }

    public ResponseMessage addMultipleServicesToStudentFee(String admissionNumber, String schoolId, String academicYear, List<Services> services) {
        Optional<StudentAdmissionFee> feeOpt;
        if (academicYear != null && !academicYear.isEmpty()) {
            feeOpt = this.studentAdmissionFeeRepository.findByAdmissionNumberAndAcademicYearAndSchoolId(admissionNumber, academicYear, schoolId);
        } else {
            List feeRecords = this.studentAdmissionFeeRepository.findAllByAdmissionNumberAndSchoolId(admissionNumber, schoolId);
            if (feeRecords.isEmpty()) {
                return new ResponseMessage("No fee records found for student with admission number " + admissionNumber, null);
            }
            feeRecords.sort((a, b) -> {
                if (a.getAcademicYear() == null || b.getAcademicYear() == null) {
                    return 0;
                }
                return b.getAcademicYear().compareTo(a.getAcademicYear());
            });
            feeOpt = Optional.of((StudentAdmissionFee)feeRecords.get(0));
        }
        if (feeOpt.isEmpty()) {
            return new ResponseMessage("No fee record found for student with admission number " + admissionNumber + " for the specified criteria", null);
        }
        StudentAdmissionFee fee = (StudentAdmissionFee)feeOpt.get();
        if (fee.getServices() == null) {
            fee.setServices(new ArrayList());
        }
        double totalServicesCost = 0.0;
        for (Services service : services) {
            if (service.getDate() == null) {
                service.setDate(LocalDate.now());
            }
            fee.getServices().add(service);
            totalServicesCost += service.getCharge();
            this.createServiceIncomeRecord(service, fee.getSchoolId(), fee.getStudentName(), fee.getAdmissionNumber());
        }
        fee.setRemainingFee(fee.getRemainingFee() + totalServicesCost);
        if ("Paid".equals(fee.getStatus()) && fee.getRemainingFee() > 0.0) {
            fee.setStatus("Pending");
        }
        StudentAdmissionFee updatedFee = (StudentAdmissionFee)this.studentAdmissionFeeRepository.save((Object)fee);
        return new ResponseMessage(services.size() + " services added successfully to student fee", (Object)updatedFee);
    }

    public ResponseMessage createRazorpayOrder(double amount, String currency, String receiptId, Map<String, Object> notes) {
        try {
            int amountInPaise = (int)(amount * 100.0);
            RazorpayClient razorpayClient = new RazorpayClient(this.keyId, this.keySecret);
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amountInPaise);
            orderRequest.put("currency", (Object)currency);
            orderRequest.put("receipt", (Object)receiptId);
            if (notes != null && !notes.isEmpty()) {
                JSONObject notesObject = new JSONObject();
                for (Map.Entry<String, Object> entry : notes.entrySet()) {
                    notesObject.put(entry.getKey(), entry.getValue());
                }
                orderRequest.put("notes", (Object)notesObject);
            }
            Order order = razorpayClient.orders.create(orderRequest);
            JSONObject orderJson = new JSONObject(order.toString());
            HashMap<String, Object> orderMap = new HashMap<String, Object>();
            for (String key : orderJson.keySet()) {
                orderMap.put(key, orderJson.get(key));
            }
            return new ResponseMessage("Razorpay order created successfully", orderMap);
        }
        catch (RazorpayException e) {
            return new ResponseMessage("Error creating Razorpay order: " + e.getMessage(), null);
        }
    }

    public ResponseMessage verifyRazorpayPayment(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature) {
        try {
            JSONObject options = new JSONObject();
            options.put("razorpay_order_id", (Object)razorpayOrderId);
            options.put("razorpay_payment_id", (Object)razorpayPaymentId);
            options.put("razorpay_signature", (Object)razorpaySignature);
            boolean isValidSignature = Utils.verifyPaymentSignature((JSONObject)options, (String)this.keySecret);
            if (isValidSignature) {
                return new ResponseMessage("Payment signature verified successfully", (Object)true);
            }
            return new ResponseMessage("Invalid payment signature", (Object)false);
        }
        catch (Exception e) {
            return new ResponseMessage("Error verifying payment signature: " + e.getMessage(), (Object)false);
        }
    }

    public ResponseMessage processRazorpayPayment(Map<String, Object> paymentDetails) {
        try {
            String orderId = (String)paymentDetails.get("razorpay_order_id");
            String paymentId = (String)paymentDetails.get("razorpay_payment_id");
            String signature = (String)paymentDetails.get("razorpay_signature");
            ResponseMessage verificationResult = this.verifyRazorpayPayment(orderId, paymentId, signature);
            if (!((Boolean)verificationResult.getData()).booleanValue()) {
                return new ResponseMessage("Payment verification failed", null);
            }
            Map notes = (Map)paymentDetails.get("notes");
            String admissionNumber = (String)notes.get("admissionNumber");
            String schoolId = (String)notes.get("schoolId");
            String paymentType = (String)notes.get("paymentType");
            double amount = Double.parseDouble((String)notes.get("amount"));
            Optional studentFeeOpt = this.studentAdmissionFeeRepository.findByAdmissionNumberAndSchoolId(admissionNumber, schoolId);
            if (studentFeeOpt.isEmpty()) {
                return new ResponseMessage("Student record not found", null);
            }
            StudentAdmissionFee studentFee = (StudentAdmissionFee)studentFeeOpt.get();
            if ("ADMISSION_FEE".equals(paymentType)) {
                studentFee.setAdmissionFees(studentFee.getAdmissionFees() + amount);
            } else if ("TUITION_FEE".equals(paymentType)) {
                StudentAdmissionFee.TuitionFeePayment payment = new StudentAdmissionFee.TuitionFeePayment();
                payment.setFee(amount);
                payment.setPaymentType("Razorpay");
                payment.setTransactionNo(paymentId);
                payment.setCreatedAt(LocalDateTime.now());
                if (studentFee.getTuitionFeePayments() == null) {
                    studentFee.setTuitionFeePayments(new ArrayList());
                }
                studentFee.getTuitionFeePayments().add(payment);
                studentFee.setTuitionFees(studentFee.getTuitionFees() + amount);
            }
            studentFee.setRemainingFee(studentFee.getYearFees() - studentFee.getTuitionFees());
            if (studentFee.getRemainingFee() <= 0.0) {
                studentFee.setStatus("Paid");
            } else {
                studentFee.setStatus("Pending");
            }
            StudentAdmissionFee updatedFee = (StudentAdmissionFee)this.studentAdmissionFeeRepository.save((Object)studentFee);
            IncomePage income = new IncomePage();
            income.setAmount(amount);
            income.setIncomeSource(paymentType);
            income.setPaymentMethod("Razorpay");
            income.setDescription("Online payment via Razorpay - " + paymentId);
            income.setDate(LocalDate.now());
            income.setSchoolId(schoolId);
            this.incomePageRepository.save((Object)income);
            this.handleOnlinePayment(amount, LocalDate.now());
            return new ResponseMessage("Payment processed successfully", (Object)updatedFee);
        }
        catch (Exception e) {
            return new ResponseMessage("Error processing payment: " + e.getMessage(), null);
        }
    }

    public ResponseMessage generatePaymentLink(String admissionNumber, String schoolId, String paymentType, double amount) {
        try {
            Optional studentFeeOpt = this.studentAdmissionFeeRepository.findByAdmissionNumberAndSchoolId(admissionNumber, schoolId);
            if (studentFeeOpt.isEmpty()) {
                return new ResponseMessage("Student not found", null);
            }
            StudentAdmissionFee studentFee = (StudentAdmissionFee)studentFeeOpt.get();
            String receiptId = "FEE_" + System.currentTimeMillis() + "_" + admissionNumber;
            HashMap<String, String> notes = new HashMap<String, String>();
            notes.put("admissionNumber", admissionNumber);
            notes.put("studentName", studentFee.getStudentName());
            notes.put("className", studentFee.getClassName());
            notes.put("schoolId", schoolId);
            notes.put("paymentType", paymentType);
            notes.put("amount", String.valueOf(amount));
            System.out.println("amount from generation link : " + notes);
            ResponseMessage orderResult = this.createRazorpayOrder(amount, "INR", receiptId, notes);
            if (orderResult.getData() == null) {
                return orderResult;
            }
            Map orderDetails = (Map)orderResult.getData();
            HashMap paymentLinkDetails = new HashMap();
            paymentLinkDetails.put("orderId", orderDetails.get("id"));
            paymentLinkDetails.put("amount", amount);
            paymentLinkDetails.put("currency", "INR");
            paymentLinkDetails.put("receiptId", receiptId);
            paymentLinkDetails.put("studentName", studentFee.getStudentName());
            paymentLinkDetails.put("admissionNumber", admissionNumber);
            paymentLinkDetails.put("className", studentFee.getClassName());
            paymentLinkDetails.put("paymentType", paymentType);
            paymentLinkDetails.put("razorpayKeyId", this.keyId);
            return new ResponseMessage("Payment link generated successfully", paymentLinkDetails);
        }
        catch (Exception e) {
            return new ResponseMessage("Error generating payment link: " + e.getMessage(), null);
        }
    }
}

