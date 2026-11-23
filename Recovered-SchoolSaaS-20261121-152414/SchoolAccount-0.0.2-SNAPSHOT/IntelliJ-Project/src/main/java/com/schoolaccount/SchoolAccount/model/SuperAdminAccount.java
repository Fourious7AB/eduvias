/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.schoolaccount.SchoolAccount.model.SuperAdminAccount
 *  com.schoolaccount.SchoolAccount.model.SuperAdminAccount$Transaction
 *  lombok.Generated
 *  org.springframework.data.annotation.Id
 *  org.springframework.data.mongodb.core.mapping.Document
 */
package com.schoolaccount.SchoolAccount.model;

import com.schoolaccount.SchoolAccount.model.SuperAdminAccount;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="super_admin_account")
public class SuperAdminAccount {
    @Id
    private String id;
    private double totalBalance;
    private double pendingDistribution;
    private double commissionEarned;
    private LocalDateTime lastUpdated;
    private List<Transaction> recentTransactions = new ArrayList();

    @Generated
    public String getId() {
        return this.id;
    }

    @Generated
    public double getTotalBalance() {
        return this.totalBalance;
    }

    @Generated
    public double getPendingDistribution() {
        return this.pendingDistribution;
    }

    @Generated
    public double getCommissionEarned() {
        return this.commissionEarned;
    }

    @Generated
    public LocalDateTime getLastUpdated() {
        return this.lastUpdated;
    }

    @Generated
    public List<Transaction> getRecentTransactions() {
        return this.recentTransactions;
    }

    @Generated
    public void setId(String id) {
        this.id = id;
    }

    @Generated
    public void setTotalBalance(double totalBalance) {
        this.totalBalance = totalBalance;
    }

    @Generated
    public void setPendingDistribution(double pendingDistribution) {
        this.pendingDistribution = pendingDistribution;
    }

    @Generated
    public void setCommissionEarned(double commissionEarned) {
        this.commissionEarned = commissionEarned;
    }

    @Generated
    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Generated
    public void setRecentTransactions(List<Transaction> recentTransactions) {
        this.recentTransactions = recentTransactions;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SuperAdminAccount)) {
            return false;
        }
        SuperAdminAccount other = (SuperAdminAccount)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        if (Double.compare(this.getTotalBalance(), other.getTotalBalance()) != 0) {
            return false;
        }
        if (Double.compare(this.getPendingDistribution(), other.getPendingDistribution()) != 0) {
            return false;
        }
        if (Double.compare(this.getCommissionEarned(), other.getCommissionEarned()) != 0) {
            return false;
        }
        String this$id = this.getId();
        String other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) {
            return false;
        }
        LocalDateTime this$lastUpdated = this.getLastUpdated();
        LocalDateTime other$lastUpdated = other.getLastUpdated();
        if (this$lastUpdated == null ? other$lastUpdated != null : !this$lastUpdated.equals(other$lastUpdated)) {
            return false;
        }
        List this$recentTransactions = this.getRecentTransactions();
        List other$recentTransactions = other.getRecentTransactions();
        return !(this$recentTransactions == null ? other$recentTransactions != null : !this$recentTransactions.equals(other$recentTransactions));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof SuperAdminAccount;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        long $totalBalance = Double.doubleToLongBits(this.getTotalBalance());
        result = result * 59 + (int)($totalBalance >>> 32 ^ $totalBalance);
        long $pendingDistribution = Double.doubleToLongBits(this.getPendingDistribution());
        result = result * 59 + (int)($pendingDistribution >>> 32 ^ $pendingDistribution);
        long $commissionEarned = Double.doubleToLongBits(this.getCommissionEarned());
        result = result * 59 + (int)($commissionEarned >>> 32 ^ $commissionEarned);
        String $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        LocalDateTime $lastUpdated = this.getLastUpdated();
        result = result * 59 + ($lastUpdated == null ? 43 : $lastUpdated.hashCode());
        List $recentTransactions = this.getRecentTransactions();
        result = result * 59 + ($recentTransactions == null ? 43 : $recentTransactions.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "SuperAdminAccount(id=" + this.getId() + ", totalBalance=" + this.getTotalBalance() + ", pendingDistribution=" + this.getPendingDistribution() + ", commissionEarned=" + this.getCommissionEarned() + ", lastUpdated=" + this.getLastUpdated() + ", recentTransactions=" + this.getRecentTransactions() + ")";
    }

    @Generated
    public SuperAdminAccount() {
    }

    @Generated
    public SuperAdminAccount(String id, double totalBalance, double pendingDistribution, double commissionEarned, LocalDateTime lastUpdated, List<Transaction> recentTransactions) {
        this.id = id;
        this.totalBalance = totalBalance;
        this.pendingDistribution = pendingDistribution;
        this.commissionEarned = commissionEarned;
        this.lastUpdated = lastUpdated;
        this.recentTransactions = recentTransactions;
    }

    public static class Transaction {
        private String transactionId;
        private String studentId;
        private String admissionNumber;
        private String schoolId;
        private String schoolName;
        private double amount;
        private double commissionAmount;
        private double schoolAmount;
        private String status;
        private LocalDateTime timestamp;

        public Transaction() {}

        public Transaction(String transactionId, String studentId, String admissionNumber, String schoolId, String schoolName, double amount, double commissionAmount, double schoolAmount, String status, LocalDateTime timestamp) {
            this.transactionId = transactionId;
            this.studentId = studentId;
            this.admissionNumber = admissionNumber;
            this.schoolId = schoolId;
            this.schoolName = schoolName;
            this.amount = amount;
            this.commissionAmount = commissionAmount;
            this.schoolAmount = schoolAmount;
            this.status = status;
            this.timestamp = timestamp;
        }

        public String getTransactionId() { return transactionId; }
        public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
        public String getStudentId() { return studentId; }
        public void setStudentId(String studentId) { this.studentId = studentId; }
        public String getAdmissionNumber() { return admissionNumber; }
        public void setAdmissionNumber(String admissionNumber) { this.admissionNumber = admissionNumber; }
        public String getSchoolId() { return schoolId; }
        public void setSchoolId(String schoolId) { this.schoolId = schoolId; }
        public String getSchoolName() { return schoolName; }
        public void setSchoolName(String schoolName) { this.schoolName = schoolName; }
        public double getAmount() { return amount; }
        public void setAmount(double amount) { this.amount = amount; }
        public double getCommissionAmount() { return commissionAmount; }
        public void setCommissionAmount(double commissionAmount) { this.commissionAmount = commissionAmount; }
        public double getSchoolAmount() { return schoolAmount; }
        public void setSchoolAmount(double schoolAmount) { this.schoolAmount = schoolAmount; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    }
}
