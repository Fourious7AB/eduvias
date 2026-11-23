/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.schoolaccount.SchoolAccount.model.BankTransaction
 *  lombok.Generated
 *  org.springframework.data.annotation.Id
 *  org.springframework.data.mongodb.core.mapping.Document
 */
package com.schoolaccount.SchoolAccount.model;

import java.time.LocalDate;
import lombok.Generated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="bank_transactions")
public class BankTransaction {
    @Id
    private String id;
    private String schoolId;
    private String transactionType;
    private String accountNumber;
    private double amount;
    private LocalDate date;
    private String description;

    @Generated
    public String getId() {
        return this.id;
    }

    @Generated
    public String getSchoolId() {
        return this.schoolId;
    }

    @Generated
    public String getTransactionType() {
        return this.transactionType;
    }

    @Generated
    public String getAccountNumber() {
        return this.accountNumber;
    }

    @Generated
    public double getAmount() {
        return this.amount;
    }

    @Generated
    public LocalDate getDate() {
        return this.date;
    }

    @Generated
    public String getDescription() {
        return this.description;
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
    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    @Generated
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @Generated
    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Generated
    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Generated
    public void setDescription(String description) {
        this.description = description;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof BankTransaction)) {
            return false;
        }
        BankTransaction other = (BankTransaction)o;
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
        String this$transactionType = this.getTransactionType();
        String other$transactionType = other.getTransactionType();
        if (this$transactionType == null ? other$transactionType != null : !this$transactionType.equals(other$transactionType)) {
            return false;
        }
        String this$accountNumber = this.getAccountNumber();
        String other$accountNumber = other.getAccountNumber();
        if (this$accountNumber == null ? other$accountNumber != null : !this$accountNumber.equals(other$accountNumber)) {
            return false;
        }
        LocalDate this$date = this.getDate();
        LocalDate other$date = other.getDate();
        if (this$date == null ? other$date != null : !((Object)this$date).equals(other$date)) {
            return false;
        }
        String this$description = this.getDescription();
        String other$description = other.getDescription();
        return !(this$description == null ? other$description != null : !this$description.equals(other$description));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof BankTransaction;
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
        String $transactionType = this.getTransactionType();
        result = result * 59 + ($transactionType == null ? 43 : $transactionType.hashCode());
        String $accountNumber = this.getAccountNumber();
        result = result * 59 + ($accountNumber == null ? 43 : $accountNumber.hashCode());
        LocalDate $date = this.getDate();
        result = result * 59 + ($date == null ? 43 : ((Object)$date).hashCode());
        String $description = this.getDescription();
        result = result * 59 + ($description == null ? 43 : $description.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "BankTransaction(id=" + this.getId() + ", schoolId=" + this.getSchoolId() + ", transactionType=" + this.getTransactionType() + ", accountNumber=" + this.getAccountNumber() + ", amount=" + this.getAmount() + ", date=" + this.getDate() + ", description=" + this.getDescription() + ")";
    }

    @Generated
    public BankTransaction() {
    }

    @Generated
    public BankTransaction(String id, String schoolId, String transactionType, String accountNumber, double amount, LocalDate date, String description) {
        this.id = id;
        this.schoolId = schoolId;
        this.transactionType = transactionType;
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.date = date;
        this.description = description;
    }
}

