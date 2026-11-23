/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.schoolaccount.SchoolAccount.model.Services
 *  com.schoolaccount.SchoolAccount.model.StudentAdmissionFee
 *  com.schoolaccount.SchoolAccount.model.StudentAdmissionFee$TuitionFeePayment
 *  lombok.Generated
 *  org.springframework.data.annotation.Id
 *  org.springframework.data.mongodb.core.mapping.Document
 */
package com.schoolaccount.SchoolAccount.model;

import com.schoolaccount.SchoolAccount.model.Services;
import com.schoolaccount.SchoolAccount.model.StudentAdmissionFee;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="student_admission_fees")
public class StudentAdmissionFee {
    @Id
    private String id;
    private String schoolId;
    private String admissionNumber;
    private String studentName;
    private String className;
    private String section;
    private double admissionFees;
    private double books;
    private double donation;
    private double fine;
    private double idCardCharges;
    private double lateFees;
    private double miscellaneous;
    private double transportationalFees;
    private double tuitionFees;
    private double uniformCharges;
    private double other;
    private String paymentMethod;
    private String transactionNumber;
    private String status;
    private double yearFees;
    private double remainingFee;
    private LocalDate date = LocalDate.now();
    private String academicYear;
    private List<Services> services = new ArrayList();
    private List<TuitionFeePayment> tuitionFeePayments = new ArrayList();

    @Generated
    public StudentAdmissionFee() {
    }

    @Generated
    public String getId() {
        return this.id;
    }

    @Generated
    public String getSchoolId() {
        return this.schoolId;
    }

    @Generated
    public String getAdmissionNumber() {
        return this.admissionNumber;
    }

    @Generated
    public String getStudentName() {
        return this.studentName;
    }

    @Generated
    public String getClassName() {
        return this.className;
    }

    @Generated
    public String getSection() {
        return this.section;
    }

    @Generated
    public double getAdmissionFees() {
        return this.admissionFees;
    }

    @Generated
    public double getBooks() {
        return this.books;
    }

    @Generated
    public double getDonation() {
        return this.donation;
    }

    @Generated
    public double getFine() {
        return this.fine;
    }

    @Generated
    public double getIdCardCharges() {
        return this.idCardCharges;
    }

    @Generated
    public double getLateFees() {
        return this.lateFees;
    }

    @Generated
    public double getMiscellaneous() {
        return this.miscellaneous;
    }

    @Generated
    public double getTransportationalFees() {
        return this.transportationalFees;
    }

    @Generated
    public double getTuitionFees() {
        return this.tuitionFees;
    }

    @Generated
    public double getUniformCharges() {
        return this.uniformCharges;
    }

    @Generated
    public double getOther() {
        return this.other;
    }

    @Generated
    public String getPaymentMethod() {
        return this.paymentMethod;
    }

    @Generated
    public String getTransactionNumber() {
        return this.transactionNumber;
    }

    @Generated
    public String getStatus() {
        return this.status;
    }

    @Generated
    public double getYearFees() {
        return this.yearFees;
    }

    @Generated
    public double getRemainingFee() {
        return this.remainingFee;
    }

    @Generated
    public LocalDate getDate() {
        return this.date;
    }

    @Generated
    public String getAcademicYear() {
        return this.academicYear;
    }

    @Generated
    public List<Services> getServices() {
        return this.services;
    }

    @Generated
    public List<TuitionFeePayment> getTuitionFeePayments() {
        return this.tuitionFeePayments;
    }

    @Generated
    public void setId(String id) {
        this.id = id;
    }

    @Generated
    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    @Generated
    public void setAdmissionNumber(String admissionNumber) {
        this.admissionNumber = admissionNumber;
    }

    @Generated
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    @Generated
    public void setClassName(String className) {
        this.className = className;
    }

    @Generated
    public void setSection(String section) {
        this.section = section;
    }

    @Generated
    public void setAdmissionFees(double admissionFees) {
        this.admissionFees = admissionFees;
    }

    @Generated
    public void setBooks(double books) {
        this.books = books;
    }

    @Generated
    public void setDonation(double donation) {
        this.donation = donation;
    }

    @Generated
    public void setFine(double fine) {
        this.fine = fine;
    }

    @Generated
    public void setIdCardCharges(double idCardCharges) {
        this.idCardCharges = idCardCharges;
    }

    @Generated
    public void setLateFees(double lateFees) {
        this.lateFees = lateFees;
    }

    @Generated
    public void setMiscellaneous(double miscellaneous) {
        this.miscellaneous = miscellaneous;
    }

    @Generated
    public void setTransportationalFees(double transportationalFees) {
        this.transportationalFees = transportationalFees;
    }

    @Generated
    public void setTuitionFees(double tuitionFees) {
        this.tuitionFees = tuitionFees;
    }

    @Generated
    public void setUniformCharges(double uniformCharges) {
        this.uniformCharges = uniformCharges;
    }

    @Generated
    public void setOther(double other) {
        this.other = other;
    }

    @Generated
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    @Generated
    public void setTransactionNumber(String transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

    @Generated
    public void setStatus(String status) {
        this.status = status;
    }

    @Generated
    public void setYearFees(double yearFees) {
        this.yearFees = yearFees;
    }

    @Generated
    public void setRemainingFee(double remainingFee) {
        this.remainingFee = remainingFee;
    }

    @Generated
    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Generated
    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    @Generated
    public void setServices(List<Services> services) {
        this.services = services;
    }

    @Generated
    public void setTuitionFeePayments(List<TuitionFeePayment> tuitionFeePayments) {
        this.tuitionFeePayments = tuitionFeePayments;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof StudentAdmissionFee)) {
            return false;
        }
        StudentAdmissionFee other = (StudentAdmissionFee)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        if (Double.compare(this.getAdmissionFees(), other.getAdmissionFees()) != 0) {
            return false;
        }
        if (Double.compare(this.getBooks(), other.getBooks()) != 0) {
            return false;
        }
        if (Double.compare(this.getDonation(), other.getDonation()) != 0) {
            return false;
        }
        if (Double.compare(this.getFine(), other.getFine()) != 0) {
            return false;
        }
        if (Double.compare(this.getIdCardCharges(), other.getIdCardCharges()) != 0) {
            return false;
        }
        if (Double.compare(this.getLateFees(), other.getLateFees()) != 0) {
            return false;
        }
        if (Double.compare(this.getMiscellaneous(), other.getMiscellaneous()) != 0) {
            return false;
        }
        if (Double.compare(this.getTransportationalFees(), other.getTransportationalFees()) != 0) {
            return false;
        }
        if (Double.compare(this.getTuitionFees(), other.getTuitionFees()) != 0) {
            return false;
        }
        if (Double.compare(this.getUniformCharges(), other.getUniformCharges()) != 0) {
            return false;
        }
        if (Double.compare(this.getOther(), other.getOther()) != 0) {
            return false;
        }
        if (Double.compare(this.getYearFees(), other.getYearFees()) != 0) {
            return false;
        }
        if (Double.compare(this.getRemainingFee(), other.getRemainingFee()) != 0) {
            return false;
        }
        String this$id = this.getId();
        String other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) {
            return false;
        }
        String this$schoolId = this.getSchoolId();
        String other$schoolId = other.getSchoolId();
        if (this$schoolId == null ? other$schoolId != null : !this$schoolId.equals(other$schoolId)) {
            return false;
        }
        String this$admissionNumber = this.getAdmissionNumber();
        String other$admissionNumber = other.getAdmissionNumber();
        if (this$admissionNumber == null ? other$admissionNumber != null : !this$admissionNumber.equals(other$admissionNumber)) {
            return false;
        }
        String this$studentName = this.getStudentName();
        String other$studentName = other.getStudentName();
        if (this$studentName == null ? other$studentName != null : !this$studentName.equals(other$studentName)) {
            return false;
        }
        String this$className = this.getClassName();
        String other$className = other.getClassName();
        if (this$className == null ? other$className != null : !this$className.equals(other$className)) {
            return false;
        }
        String this$section = this.getSection();
        String other$section = other.getSection();
        if (this$section == null ? other$section != null : !this$section.equals(other$section)) {
            return false;
        }
        String this$paymentMethod = this.getPaymentMethod();
        String other$paymentMethod = other.getPaymentMethod();
        if (this$paymentMethod == null ? other$paymentMethod != null : !this$paymentMethod.equals(other$paymentMethod)) {
            return false;
        }
        String this$transactionNumber = this.getTransactionNumber();
        String other$transactionNumber = other.getTransactionNumber();
        if (this$transactionNumber == null ? other$transactionNumber != null : !this$transactionNumber.equals(other$transactionNumber)) {
            return false;
        }
        String this$status = this.getStatus();
        String other$status = other.getStatus();
        if (this$status == null ? other$status != null : !this$status.equals(other$status)) {
            return false;
        }
        LocalDate this$date = this.getDate();
        LocalDate other$date = other.getDate();
        if (this$date == null ? other$date != null : !this$date.equals(other$date)) {
            return false;
        }
        String this$academicYear = this.getAcademicYear();
        String other$academicYear = other.getAcademicYear();
        if (this$academicYear == null ? other$academicYear != null : !this$academicYear.equals(other$academicYear)) {
            return false;
        }
        List this$services = this.getServices();
        List other$services = other.getServices();
        if (this$services == null ? other$services != null : !this$services.equals(other$services)) {
            return false;
        }
        List this$tuitionFeePayments = this.getTuitionFeePayments();
        List other$tuitionFeePayments = other.getTuitionFeePayments();
        return !(this$tuitionFeePayments == null ? other$tuitionFeePayments != null : !this$tuitionFeePayments.equals(other$tuitionFeePayments));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof StudentAdmissionFee;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        long $admissionFees = Double.doubleToLongBits(this.getAdmissionFees());
        result = result * 59 + (int)($admissionFees >>> 32 ^ $admissionFees);
        long $books = Double.doubleToLongBits(this.getBooks());
        result = result * 59 + (int)($books >>> 32 ^ $books);
        long $donation = Double.doubleToLongBits(this.getDonation());
        result = result * 59 + (int)($donation >>> 32 ^ $donation);
        long $fine = Double.doubleToLongBits(this.getFine());
        result = result * 59 + (int)($fine >>> 32 ^ $fine);
        long $idCardCharges = Double.doubleToLongBits(this.getIdCardCharges());
        result = result * 59 + (int)($idCardCharges >>> 32 ^ $idCardCharges);
        long $lateFees = Double.doubleToLongBits(this.getLateFees());
        result = result * 59 + (int)($lateFees >>> 32 ^ $lateFees);
        long $miscellaneous = Double.doubleToLongBits(this.getMiscellaneous());
        result = result * 59 + (int)($miscellaneous >>> 32 ^ $miscellaneous);
        long $transportationalFees = Double.doubleToLongBits(this.getTransportationalFees());
        result = result * 59 + (int)($transportationalFees >>> 32 ^ $transportationalFees);
        long $tuitionFees = Double.doubleToLongBits(this.getTuitionFees());
        result = result * 59 + (int)($tuitionFees >>> 32 ^ $tuitionFees);
        long $uniformCharges = Double.doubleToLongBits(this.getUniformCharges());
        result = result * 59 + (int)($uniformCharges >>> 32 ^ $uniformCharges);
        long $other = Double.doubleToLongBits(this.getOther());
        result = result * 59 + (int)($other >>> 32 ^ $other);
        long $yearFees = Double.doubleToLongBits(this.getYearFees());
        result = result * 59 + (int)($yearFees >>> 32 ^ $yearFees);
        long $remainingFee = Double.doubleToLongBits(this.getRemainingFee());
        result = result * 59 + (int)($remainingFee >>> 32 ^ $remainingFee);
        String $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        String $schoolId = this.getSchoolId();
        result = result * 59 + ($schoolId == null ? 43 : $schoolId.hashCode());
        String $admissionNumber = this.getAdmissionNumber();
        result = result * 59 + ($admissionNumber == null ? 43 : $admissionNumber.hashCode());
        String $studentName = this.getStudentName();
        result = result * 59 + ($studentName == null ? 43 : $studentName.hashCode());
        String $className = this.getClassName();
        result = result * 59 + ($className == null ? 43 : $className.hashCode());
        String $section = this.getSection();
        result = result * 59 + ($section == null ? 43 : $section.hashCode());
        String $paymentMethod = this.getPaymentMethod();
        result = result * 59 + ($paymentMethod == null ? 43 : $paymentMethod.hashCode());
        String $transactionNumber = this.getTransactionNumber();
        result = result * 59 + ($transactionNumber == null ? 43 : $transactionNumber.hashCode());
        String $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : $status.hashCode());
        LocalDate $date = this.getDate();
        result = result * 59 + ($date == null ? 43 : $date.hashCode());
        String $academicYear = this.getAcademicYear();
        result = result * 59 + ($academicYear == null ? 43 : $academicYear.hashCode());
        List $services = this.getServices();
        result = result * 59 + ($services == null ? 43 : $services.hashCode());
        List $tuitionFeePayments = this.getTuitionFeePayments();
        result = result * 59 + ($tuitionFeePayments == null ? 43 : $tuitionFeePayments.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "StudentAdmissionFee(id=" + this.getId() + ", schoolId=" + this.getSchoolId() + ", admissionNumber=" + this.getAdmissionNumber() + ", studentName=" + this.getStudentName() + ", className=" + this.getClassName() + ", section=" + this.getSection() + ", admissionFees=" + this.getAdmissionFees() + ", books=" + this.getBooks() + ", donation=" + this.getDonation() + ", fine=" + this.getFine() + ", idCardCharges=" + this.getIdCardCharges() + ", lateFees=" + this.getLateFees() + ", miscellaneous=" + this.getMiscellaneous() + ", transportationalFees=" + this.getTransportationalFees() + ", tuitionFees=" + this.getTuitionFees() + ", uniformCharges=" + this.getUniformCharges() + ", other=" + this.getOther() + ", paymentMethod=" + this.getPaymentMethod() + ", transactionNumber=" + this.getTransactionNumber() + ", status=" + this.getStatus() + ", yearFees=" + this.getYearFees() + ", remainingFee=" + this.getRemainingFee() + ", date=" + this.getDate() + ", academicYear=" + this.getAcademicYear() + ", services=" + this.getServices() + ", tuitionFeePayments=" + this.getTuitionFeePayments() + ")";
    }

    public static class TuitionFeePayment {
        private double fee;
        private String paymentType;
        private String transactionNo;
        private LocalDateTime createdAt;

        public TuitionFeePayment() {}

        public TuitionFeePayment(double fee, String paymentType, String transactionNo, LocalDateTime createdAt) {
            this.fee = fee;
            this.paymentType = paymentType;
            this.transactionNo = transactionNo;
            this.createdAt = createdAt;
        }

        public double getFee() { return fee; }
        public void setFee(double fee) { this.fee = fee; }
        public String getPaymentType() { return paymentType; }
        public void setPaymentType(String paymentType) { this.paymentType = paymentType; }
        public String getTransactionNo() { return transactionNo; }
        public void setTransactionNo(String transactionNo) { this.transactionNo = transactionNo; }
        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    }
}
