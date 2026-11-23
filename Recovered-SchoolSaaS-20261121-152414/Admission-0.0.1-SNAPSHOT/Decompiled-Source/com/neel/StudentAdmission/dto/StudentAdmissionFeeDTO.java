/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.StudentAdmission.dto.StudentAdmissionFeeDTO
 *  com.neel.StudentAdmission.dto.StudentAdmissionFeeDTO$TuitionFeePayment
 *  lombok.Generated
 */
package com.neel.StudentAdmission.dto;

import com.neel.StudentAdmission.dto.StudentAdmissionFeeDTO;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;

public class StudentAdmissionFeeDTO {
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
    private double totalFees;
    private String paymentMethod;
    private String transactionNumber;
    private String status;
    private double yearFees;
    private double remainingFee;
    private LocalDateTime date;
    private List<TuitionFeePayment> tuitionFeePayments = new ArrayList();

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
    public double getTotalFees() {
        return this.totalFees;
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
    public LocalDateTime getDate() {
        return this.date;
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
    public void setTotalFees(double totalFees) {
        this.totalFees = totalFees;
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
    public void setDate(LocalDateTime date) {
        this.date = date;
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
        if (!(o instanceof StudentAdmissionFeeDTO)) {
            return false;
        }
        StudentAdmissionFeeDTO other = (StudentAdmissionFeeDTO)o;
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
        if (Double.compare(this.getTotalFees(), other.getTotalFees()) != 0) {
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
        LocalDateTime this$date = this.getDate();
        LocalDateTime other$date = other.getDate();
        if (this$date == null ? other$date != null : !((Object)this$date).equals(other$date)) {
            return false;
        }
        List this$tuitionFeePayments = this.getTuitionFeePayments();
        List other$tuitionFeePayments = other.getTuitionFeePayments();
        return !(this$tuitionFeePayments == null ? other$tuitionFeePayments != null : !((Object)this$tuitionFeePayments).equals(other$tuitionFeePayments));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof StudentAdmissionFeeDTO;
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
        long $totalFees = Double.doubleToLongBits(this.getTotalFees());
        result = result * 59 + (int)($totalFees >>> 32 ^ $totalFees);
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
        LocalDateTime $date = this.getDate();
        result = result * 59 + ($date == null ? 43 : ((Object)$date).hashCode());
        List $tuitionFeePayments = this.getTuitionFeePayments();
        result = result * 59 + ($tuitionFeePayments == null ? 43 : ((Object)$tuitionFeePayments).hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "StudentAdmissionFeeDTO(id=" + this.getId() + ", schoolId=" + this.getSchoolId() + ", admissionNumber=" + this.getAdmissionNumber() + ", studentName=" + this.getStudentName() + ", className=" + this.getClassName() + ", section=" + this.getSection() + ", admissionFees=" + this.getAdmissionFees() + ", books=" + this.getBooks() + ", donation=" + this.getDonation() + ", fine=" + this.getFine() + ", idCardCharges=" + this.getIdCardCharges() + ", lateFees=" + this.getLateFees() + ", miscellaneous=" + this.getMiscellaneous() + ", transportationalFees=" + this.getTransportationalFees() + ", tuitionFees=" + this.getTuitionFees() + ", uniformCharges=" + this.getUniformCharges() + ", other=" + this.getOther() + ", totalFees=" + this.getTotalFees() + ", paymentMethod=" + this.getPaymentMethod() + ", transactionNumber=" + this.getTransactionNumber() + ", status=" + this.getStatus() + ", yearFees=" + this.getYearFees() + ", remainingFee=" + this.getRemainingFee() + ", date=" + this.getDate() + ", tuitionFeePayments=" + this.getTuitionFeePayments() + ")";
    }

    @Generated
    public StudentAdmissionFeeDTO() {
    }

    @Generated
    public StudentAdmissionFeeDTO(String id, String schoolId, String admissionNumber, String studentName, String className, String section, double admissionFees, double books, double donation, double fine, double idCardCharges, double lateFees, double miscellaneous, double transportationalFees, double tuitionFees, double uniformCharges, double other, double totalFees, String paymentMethod, String transactionNumber, String status, double yearFees, double remainingFee, LocalDateTime date, List<TuitionFeePayment> tuitionFeePayments) {
        this.id = id;
        this.schoolId = schoolId;
        this.admissionNumber = admissionNumber;
        this.studentName = studentName;
        this.className = className;
        this.section = section;
        this.admissionFees = admissionFees;
        this.books = books;
        this.donation = donation;
        this.fine = fine;
        this.idCardCharges = idCardCharges;
        this.lateFees = lateFees;
        this.miscellaneous = miscellaneous;
        this.transportationalFees = transportationalFees;
        this.tuitionFees = tuitionFees;
        this.uniformCharges = uniformCharges;
        this.other = other;
        this.totalFees = totalFees;
        this.paymentMethod = paymentMethod;
        this.transactionNumber = transactionNumber;
        this.status = status;
        this.yearFees = yearFees;
        this.remainingFee = remainingFee;
        this.date = date;
        this.tuitionFeePayments = tuitionFeePayments;
    }
}

