/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.schoolaccount.SchoolAccount.model.AccountDetails
 *  lombok.Generated
 *  org.springframework.data.annotation.Id
 *  org.springframework.data.mongodb.core.mapping.Document
 */
package com.schoolaccount.SchoolAccount.model;

import lombok.Generated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="account_details")
public class AccountDetails {
    @Id
    private String id;
    private String schoolId;
    private String accountNumber;
    private String ifscCode;
    private String bankName;
    private String branch;
    private String accountHolderName;
    private double openingAmount;

    @Generated
    public AccountDetails() {
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
    public String getAccountNumber() {
        return this.accountNumber;
    }

    @Generated
    public String getIfscCode() {
        return this.ifscCode;
    }

    @Generated
    public String getBankName() {
        return this.bankName;
    }

    @Generated
    public String getBranch() {
        return this.branch;
    }

    @Generated
    public String getAccountHolderName() {
        return this.accountHolderName;
    }

    @Generated
    public double getOpeningAmount() {
        return this.openingAmount;
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
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @Generated
    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    @Generated
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    @Generated
    public void setBranch(String branch) {
        this.branch = branch;
    }

    @Generated
    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    @Generated
    public void setOpeningAmount(double openingAmount) {
        this.openingAmount = openingAmount;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AccountDetails)) {
            return false;
        }
        AccountDetails other = (AccountDetails)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        if (Double.compare(this.getOpeningAmount(), other.getOpeningAmount()) != 0) {
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
        String this$accountNumber = this.getAccountNumber();
        String other$accountNumber = other.getAccountNumber();
        if (this$accountNumber == null ? other$accountNumber != null : !this$accountNumber.equals(other$accountNumber)) {
            return false;
        }
        String this$ifscCode = this.getIfscCode();
        String other$ifscCode = other.getIfscCode();
        if (this$ifscCode == null ? other$ifscCode != null : !this$ifscCode.equals(other$ifscCode)) {
            return false;
        }
        String this$bankName = this.getBankName();
        String other$bankName = other.getBankName();
        if (this$bankName == null ? other$bankName != null : !this$bankName.equals(other$bankName)) {
            return false;
        }
        String this$branch = this.getBranch();
        String other$branch = other.getBranch();
        if (this$branch == null ? other$branch != null : !this$branch.equals(other$branch)) {
            return false;
        }
        String this$accountHolderName = this.getAccountHolderName();
        String other$accountHolderName = other.getAccountHolderName();
        return !(this$accountHolderName == null ? other$accountHolderName != null : !this$accountHolderName.equals(other$accountHolderName));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof AccountDetails;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        long $openingAmount = Double.doubleToLongBits(this.getOpeningAmount());
        result = result * 59 + (int)($openingAmount >>> 32 ^ $openingAmount);
        String $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        String $schoolId = this.getSchoolId();
        result = result * 59 + ($schoolId == null ? 43 : $schoolId.hashCode());
        String $accountNumber = this.getAccountNumber();
        result = result * 59 + ($accountNumber == null ? 43 : $accountNumber.hashCode());
        String $ifscCode = this.getIfscCode();
        result = result * 59 + ($ifscCode == null ? 43 : $ifscCode.hashCode());
        String $bankName = this.getBankName();
        result = result * 59 + ($bankName == null ? 43 : $bankName.hashCode());
        String $branch = this.getBranch();
        result = result * 59 + ($branch == null ? 43 : $branch.hashCode());
        String $accountHolderName = this.getAccountHolderName();
        result = result * 59 + ($accountHolderName == null ? 43 : $accountHolderName.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "AccountDetails(id=" + this.getId() + ", schoolId=" + this.getSchoolId() + ", accountNumber=" + this.getAccountNumber() + ", ifscCode=" + this.getIfscCode() + ", bankName=" + this.getBankName() + ", branch=" + this.getBranch() + ", accountHolderName=" + this.getAccountHolderName() + ", openingAmount=" + this.getOpeningAmount() + ")";
    }
}

