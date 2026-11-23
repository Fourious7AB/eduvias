/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.schoolaccount.SchoolAccount.model.PaymentTransaction
 *  lombok.Generated
 *  org.springframework.data.annotation.Id
 *  org.springframework.data.mongodb.core.mapping.Document
 */
package com.schoolaccount.SchoolAccount.model;

import java.time.LocalDateTime;
import lombok.Generated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="payment_transactions")
public class PaymentTransaction {
    @Id
    private String id;
    private String transactionId;
    private String studentId;
    private String admissionNumber;
    private String schoolId;
    private String schoolName;
    private String paymentType;
    private double amount;
    private String currency;
    private String status;
    private String paymentMethod;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String description;
    private boolean distributedToSchool;
    private LocalDateTime distributionDate;
    private String subWalletId;

    @Generated
    public String getId() {
        return this.id;
    }

    @Generated
    public String getTransactionId() {
        return this.transactionId;
    }

    @Generated
    public String getStudentId() {
        return this.studentId;
    }

    @Generated
    public String getAdmissionNumber() {
        return this.admissionNumber;
    }

    @Generated
    public String getSchoolId() {
        return this.schoolId;
    }

    @Generated
    public String getSchoolName() {
        return this.schoolName;
    }

    @Generated
    public String getPaymentType() {
        return this.paymentType;
    }

    @Generated
    public double getAmount() {
        return this.amount;
    }

    @Generated
    public String getCurrency() {
        return this.currency;
    }

    @Generated
    public String getStatus() {
        return this.status;
    }

    @Generated
    public String getPaymentMethod() {
        return this.paymentMethod;
    }

    @Generated
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    @Generated
    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    @Generated
    public String getDescription() {
        return this.description;
    }

    @Generated
    public boolean isDistributedToSchool() {
        return this.distributedToSchool;
    }

    @Generated
    public LocalDateTime getDistributionDate() {
        return this.distributionDate;
    }

    @Generated
    public String getSubWalletId() {
        return this.subWalletId;
    }

    @Generated
    public void setId(String id) {
        this.id = id;
    }

    @Generated
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    @Generated
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    @Generated
    public void setAdmissionNumber(String admissionNumber) {
        this.admissionNumber = admissionNumber;
    }

    @Generated
    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    @Generated
    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    @Generated
    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    @Generated
    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Generated
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Generated
    public void setStatus(String status) {
        this.status = status;
    }

    @Generated
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    @Generated
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Generated
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Generated
    public void setDescription(String description) {
        this.description = description;
    }

    @Generated
    public void setDistributedToSchool(boolean distributedToSchool) {
        this.distributedToSchool = distributedToSchool;
    }

    @Generated
    public void setDistributionDate(LocalDateTime distributionDate) {
        this.distributionDate = distributionDate;
    }

    @Generated
    public void setSubWalletId(String subWalletId) {
        this.subWalletId = subWalletId;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PaymentTransaction)) {
            return false;
        }
        PaymentTransaction other = (PaymentTransaction)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        if (Double.compare(this.getAmount(), other.getAmount()) != 0) {
            return false;
        }
        if (this.isDistributedToSchool() != other.isDistributedToSchool()) {
            return false;
        }
        String this$id = this.getId();
        String other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) {
            return false;
        }
        String this$transactionId = this.getTransactionId();
        String other$transactionId = other.getTransactionId();
        if (this$transactionId == null ? other$transactionId != null : !this$transactionId.equals(other$transactionId)) {
            return false;
        }
        String this$studentId = this.getStudentId();
        String other$studentId = other.getStudentId();
        if (this$studentId == null ? other$studentId != null : !this$studentId.equals(other$studentId)) {
            return false;
        }
        String this$admissionNumber = this.getAdmissionNumber();
        String other$admissionNumber = other.getAdmissionNumber();
        if (this$admissionNumber == null ? other$admissionNumber != null : !this$admissionNumber.equals(other$admissionNumber)) {
            return false;
        }
        String this$schoolId = this.getSchoolId();
        String other$schoolId = other.getSchoolId();
        if (this$schoolId == null ? other$schoolId != null : !this$schoolId.equals(other$schoolId)) {
            return false;
        }
        String this$schoolName = this.getSchoolName();
        String other$schoolName = other.getSchoolName();
        if (this$schoolName == null ? other$schoolName != null : !this$schoolName.equals(other$schoolName)) {
            return false;
        }
        String this$paymentType = this.getPaymentType();
        String other$paymentType = other.getPaymentType();
        if (this$paymentType == null ? other$paymentType != null : !this$paymentType.equals(other$paymentType)) {
            return false;
        }
        String this$currency = this.getCurrency();
        String other$currency = other.getCurrency();
        if (this$currency == null ? other$currency != null : !this$currency.equals(other$currency)) {
            return false;
        }
        String this$status = this.getStatus();
        String other$status = other.getStatus();
        if (this$status == null ? other$status != null : !this$status.equals(other$status)) {
            return false;
        }
        String this$paymentMethod = this.getPaymentMethod();
        String other$paymentMethod = other.getPaymentMethod();
        if (this$paymentMethod == null ? other$paymentMethod != null : !this$paymentMethod.equals(other$paymentMethod)) {
            return false;
        }
        LocalDateTime this$createdAt = this.getCreatedAt();
        LocalDateTime other$createdAt = other.getCreatedAt();
        if (this$createdAt == null ? other$createdAt != null : !((Object)this$createdAt).equals(other$createdAt)) {
            return false;
        }
        LocalDateTime this$updatedAt = this.getUpdatedAt();
        LocalDateTime other$updatedAt = other.getUpdatedAt();
        if (this$updatedAt == null ? other$updatedAt != null : !((Object)this$updatedAt).equals(other$updatedAt)) {
            return false;
        }
        String this$description = this.getDescription();
        String other$description = other.getDescription();
        if (this$description == null ? other$description != null : !this$description.equals(other$description)) {
            return false;
        }
        LocalDateTime this$distributionDate = this.getDistributionDate();
        LocalDateTime other$distributionDate = other.getDistributionDate();
        if (this$distributionDate == null ? other$distributionDate != null : !((Object)this$distributionDate).equals(other$distributionDate)) {
            return false;
        }
        String this$subWalletId = this.getSubWalletId();
        String other$subWalletId = other.getSubWalletId();
        return !(this$subWalletId == null ? other$subWalletId != null : !this$subWalletId.equals(other$subWalletId));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof PaymentTransaction;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        long $amount = Double.doubleToLongBits(this.getAmount());
        result = result * 59 + (int)($amount >>> 32 ^ $amount);
        result = result * 59 + (this.isDistributedToSchool() ? 79 : 97);
        String $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        String $transactionId = this.getTransactionId();
        result = result * 59 + ($transactionId == null ? 43 : $transactionId.hashCode());
        String $studentId = this.getStudentId();
        result = result * 59 + ($studentId == null ? 43 : $studentId.hashCode());
        String $admissionNumber = this.getAdmissionNumber();
        result = result * 59 + ($admissionNumber == null ? 43 : $admissionNumber.hashCode());
        String $schoolId = this.getSchoolId();
        result = result * 59 + ($schoolId == null ? 43 : $schoolId.hashCode());
        String $schoolName = this.getSchoolName();
        result = result * 59 + ($schoolName == null ? 43 : $schoolName.hashCode());
        String $paymentType = this.getPaymentType();
        result = result * 59 + ($paymentType == null ? 43 : $paymentType.hashCode());
        String $currency = this.getCurrency();
        result = result * 59 + ($currency == null ? 43 : $currency.hashCode());
        String $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : $status.hashCode());
        String $paymentMethod = this.getPaymentMethod();
        result = result * 59 + ($paymentMethod == null ? 43 : $paymentMethod.hashCode());
        LocalDateTime $createdAt = this.getCreatedAt();
        result = result * 59 + ($createdAt == null ? 43 : ((Object)$createdAt).hashCode());
        LocalDateTime $updatedAt = this.getUpdatedAt();
        result = result * 59 + ($updatedAt == null ? 43 : ((Object)$updatedAt).hashCode());
        String $description = this.getDescription();
        result = result * 59 + ($description == null ? 43 : $description.hashCode());
        LocalDateTime $distributionDate = this.getDistributionDate();
        result = result * 59 + ($distributionDate == null ? 43 : ((Object)$distributionDate).hashCode());
        String $subWalletId = this.getSubWalletId();
        result = result * 59 + ($subWalletId == null ? 43 : $subWalletId.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "PaymentTransaction(id=" + this.getId() + ", transactionId=" + this.getTransactionId() + ", studentId=" + this.getStudentId() + ", admissionNumber=" + this.getAdmissionNumber() + ", schoolId=" + this.getSchoolId() + ", schoolName=" + this.getSchoolName() + ", paymentType=" + this.getPaymentType() + ", amount=" + this.getAmount() + ", currency=" + this.getCurrency() + ", status=" + this.getStatus() + ", paymentMethod=" + this.getPaymentMethod() + ", createdAt=" + this.getCreatedAt() + ", updatedAt=" + this.getUpdatedAt() + ", description=" + this.getDescription() + ", distributedToSchool=" + this.isDistributedToSchool() + ", distributionDate=" + this.getDistributionDate() + ", subWalletId=" + this.getSubWalletId() + ")";
    }

    @Generated
    public PaymentTransaction() {
    }

    @Generated
    public PaymentTransaction(String id, String transactionId, String studentId, String admissionNumber, String schoolId, String schoolName, String paymentType, double amount, String currency, String status, String paymentMethod, LocalDateTime createdAt, LocalDateTime updatedAt, String description, boolean distributedToSchool, LocalDateTime distributionDate, String subWalletId) {
        this.id = id;
        this.transactionId = transactionId;
        this.studentId = studentId;
        this.admissionNumber = admissionNumber;
        this.schoolId = schoolId;
        this.schoolName = schoolName;
        this.paymentType = paymentType;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.description = description;
        this.distributedToSchool = distributedToSchool;
        this.distributionDate = distributionDate;
        this.subWalletId = subWalletId;
    }
}

