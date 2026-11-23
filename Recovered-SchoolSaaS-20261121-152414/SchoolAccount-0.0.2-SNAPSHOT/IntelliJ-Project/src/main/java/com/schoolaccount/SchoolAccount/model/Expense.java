/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.schoolaccount.SchoolAccount.model.Expense
 *  lombok.Generated
 *  org.springframework.data.annotation.Id
 *  org.springframework.data.mongodb.core.mapping.Document
 */
package com.schoolaccount.SchoolAccount.model;

import java.time.LocalDate;
import lombok.Generated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="expenses")
public class Expense {
    @Id
    private String id;
    private String schoolId;
    private double amount;
    private String expenseType;
    private LocalDate date;
    private String paymentMethod;
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
    public double getAmount() {
        return this.amount;
    }

    @Generated
    public String getExpenseType() {
        return this.expenseType;
    }

    @Generated
    public LocalDate getDate() {
        return this.date;
    }

    @Generated
    public String getPaymentMethod() {
        return this.paymentMethod;
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
    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Generated
    public void setExpenseType(String expenseType) {
        this.expenseType = expenseType;
    }

    @Generated
    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Generated
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
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
        if (!(o instanceof Expense)) {
            return false;
        }
        Expense other = (Expense)o;
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
        String this$expenseType = this.getExpenseType();
        String other$expenseType = other.getExpenseType();
        if (this$expenseType == null ? other$expenseType != null : !this$expenseType.equals(other$expenseType)) {
            return false;
        }
        LocalDate this$date = this.getDate();
        LocalDate other$date = other.getDate();
        if (this$date == null ? other$date != null : !this$date.equals(other$date)) {
            return false;
        }
        String this$paymentMethod = this.getPaymentMethod();
        String other$paymentMethod = other.getPaymentMethod();
        if (this$paymentMethod == null ? other$paymentMethod != null : !this$paymentMethod.equals(other$paymentMethod)) {
            return false;
        }
        String this$description = this.getDescription();
        String other$description = other.getDescription();
        return !(this$description == null ? other$description != null : !this$description.equals(other$description));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof Expense;
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
        String $expenseType = this.getExpenseType();
        result = result * 59 + ($expenseType == null ? 43 : $expenseType.hashCode());
        LocalDate $date = this.getDate();
        result = result * 59 + ($date == null ? 43 : $date.hashCode());
        String $paymentMethod = this.getPaymentMethod();
        result = result * 59 + ($paymentMethod == null ? 43 : $paymentMethod.hashCode());
        String $description = this.getDescription();
        result = result * 59 + ($description == null ? 43 : $description.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "Expense(id=" + this.getId() + ", schoolId=" + this.getSchoolId() + ", amount=" + this.getAmount() + ", expenseType=" + this.getExpenseType() + ", date=" + this.getDate() + ", paymentMethod=" + this.getPaymentMethod() + ", description=" + this.getDescription() + ")";
    }

    @Generated
    public Expense() {
    }

    @Generated
    public Expense(String id, String schoolId, double amount, String expenseType, LocalDate date, String paymentMethod, String description) {
        this.id = id;
        this.schoolId = schoolId;
        this.amount = amount;
        this.expenseType = expenseType;
        this.date = date;
        this.paymentMethod = paymentMethod;
        this.description = description;
    }
}

