/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.schoolaccount.SchoolAccount.model.PayoutRequest
 *  lombok.Generated
 *  org.springframework.data.annotation.Id
 *  org.springframework.data.mongodb.core.mapping.Document
 */
package com.schoolaccount.SchoolAccount.model;

import java.time.LocalDateTime;
import lombok.Generated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="payout_requests")
public class PayoutRequest {
    @Id
    private String id;
    private String schoolId;
    private String schoolName;
    private double amount;
    private String status;
    private String bankAccountNumber;
    private String ifscCode;
    private String accountHolderName;
    private String remarks;
    private LocalDateTime requestedAt;
    private LocalDateTime processedAt;
    private String processedBy;

    @Generated
    public String getId() {
        return this.id;
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
    public double getAmount() {
        return this.amount;
    }

    @Generated
    public String getStatus() {
        return this.status;
    }

    @Generated
    public String getBankAccountNumber() {
        return this.bankAccountNumber;
    }

    @Generated
    public String getIfscCode() {
        return this.ifscCode;
    }

    @Generated
    public String getAccountHolderName() {
        return this.accountHolderName;
    }

    @Generated
    public String getRemarks() {
        return this.remarks;
    }

    @Generated
    public LocalDateTime getRequestedAt() {
        return this.requestedAt;
    }

    @Generated
    public LocalDateTime getProcessedAt() {
        return this.processedAt;
    }

    @Generated
    public String getProcessedBy() {
        return this.processedBy;
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
    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    @Generated
    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Generated
    public void setStatus(String status) {
        this.status = status;
    }

    @Generated
    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    @Generated
    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    @Generated
    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    @Generated
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Generated
    public void setRequestedAt(LocalDateTime requestedAt) {
        this.requestedAt = requestedAt;
    }

    @Generated
    public void setProcessedAt(LocalDateTime processedAt) {
        this.processedAt = processedAt;
    }

    @Generated
    public void setProcessedBy(String processedBy) {
        this.processedBy = processedBy;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PayoutRequest)) {
            return false;
        }
        PayoutRequest other = (PayoutRequest)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        if (Double.compare(this.getAmount(), other.getAmount()) != 0) {
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
        String this$schoolName = this.getSchoolName();
        String other$schoolName = other.getSchoolName();
        if (this$schoolName == null ? other$schoolName != null : !this$schoolName.equals(other$schoolName)) {
            return false;
        }
        String this$status = this.getStatus();
        String other$status = other.getStatus();
        if (this$status == null ? other$status != null : !this$status.equals(other$status)) {
            return false;
        }
        String this$bankAccountNumber = this.getBankAccountNumber();
        String other$bankAccountNumber = other.getBankAccountNumber();
        if (this$bankAccountNumber == null ? other$bankAccountNumber != null : !this$bankAccountNumber.equals(other$bankAccountNumber)) {
            return false;
        }
        String this$ifscCode = this.getIfscCode();
        String other$ifscCode = other.getIfscCode();
        if (this$ifscCode == null ? other$ifscCode != null : !this$ifscCode.equals(other$ifscCode)) {
            return false;
        }
        String this$accountHolderName = this.getAccountHolderName();
        String other$accountHolderName = other.getAccountHolderName();
        if (this$accountHolderName == null ? other$accountHolderName != null : !this$accountHolderName.equals(other$accountHolderName)) {
            return false;
        }
        String this$remarks = this.getRemarks();
        String other$remarks = other.getRemarks();
        if (this$remarks == null ? other$remarks != null : !this$remarks.equals(other$remarks)) {
            return false;
        }
        LocalDateTime this$requestedAt = this.getRequestedAt();
        LocalDateTime other$requestedAt = other.getRequestedAt();
        if (this$requestedAt == null ? other$requestedAt != null : !((Object)this$requestedAt).equals(other$requestedAt)) {
            return false;
        }
        LocalDateTime this$processedAt = this.getProcessedAt();
        LocalDateTime other$processedAt = other.getProcessedAt();
        if (this$processedAt == null ? other$processedAt != null : !((Object)this$processedAt).equals(other$processedAt)) {
            return false;
        }
        String this$processedBy = this.getProcessedBy();
        String other$processedBy = other.getProcessedBy();
        return !(this$processedBy == null ? other$processedBy != null : !this$processedBy.equals(other$processedBy));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof PayoutRequest;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        long $amount = Double.doubleToLongBits(this.getAmount());
        result = result * 59 + (int)($amount >>> 32 ^ $amount);
        String $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        String $schoolId = this.getSchoolId();
        result = result * 59 + ($schoolId == null ? 43 : $schoolId.hashCode());
        String $schoolName = this.getSchoolName();
        result = result * 59 + ($schoolName == null ? 43 : $schoolName.hashCode());
        String $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : $status.hashCode());
        String $bankAccountNumber = this.getBankAccountNumber();
        result = result * 59 + ($bankAccountNumber == null ? 43 : $bankAccountNumber.hashCode());
        String $ifscCode = this.getIfscCode();
        result = result * 59 + ($ifscCode == null ? 43 : $ifscCode.hashCode());
        String $accountHolderName = this.getAccountHolderName();
        result = result * 59 + ($accountHolderName == null ? 43 : $accountHolderName.hashCode());
        String $remarks = this.getRemarks();
        result = result * 59 + ($remarks == null ? 43 : $remarks.hashCode());
        LocalDateTime $requestedAt = this.getRequestedAt();
        result = result * 59 + ($requestedAt == null ? 43 : ((Object)$requestedAt).hashCode());
        LocalDateTime $processedAt = this.getProcessedAt();
        result = result * 59 + ($processedAt == null ? 43 : ((Object)$processedAt).hashCode());
        String $processedBy = this.getProcessedBy();
        result = result * 59 + ($processedBy == null ? 43 : $processedBy.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "PayoutRequest(id=" + this.getId() + ", schoolId=" + this.getSchoolId() + ", schoolName=" + this.getSchoolName() + ", amount=" + this.getAmount() + ", status=" + this.getStatus() + ", bankAccountNumber=" + this.getBankAccountNumber() + ", ifscCode=" + this.getIfscCode() + ", accountHolderName=" + this.getAccountHolderName() + ", remarks=" + this.getRemarks() + ", requestedAt=" + this.getRequestedAt() + ", processedAt=" + this.getProcessedAt() + ", processedBy=" + this.getProcessedBy() + ")";
    }

    @Generated
    public PayoutRequest() {
    }

    @Generated
    public PayoutRequest(String id, String schoolId, String schoolName, double amount, String status, String bankAccountNumber, String ifscCode, String accountHolderName, String remarks, LocalDateTime requestedAt, LocalDateTime processedAt, String processedBy) {
        this.id = id;
        this.schoolId = schoolId;
        this.schoolName = schoolName;
        this.amount = amount;
        this.status = status;
        this.bankAccountNumber = bankAccountNumber;
        this.ifscCode = ifscCode;
        this.accountHolderName = accountHolderName;
        this.remarks = remarks;
        this.requestedAt = requestedAt;
        this.processedAt = processedAt;
        this.processedBy = processedBy;
    }
}

