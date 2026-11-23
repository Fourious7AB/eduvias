/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.schoolaccount.SchoolAccount.model.SchoolWallet
 *  com.schoolaccount.SchoolAccount.model.SchoolWallet$SubWallet
 *  lombok.Generated
 *  org.springframework.data.annotation.Id
 *  org.springframework.data.mongodb.core.mapping.Document
 */
package com.schoolaccount.SchoolAccount.model;

import com.schoolaccount.SchoolAccount.model.SchoolWallet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="school_wallets")
public class SchoolWallet {
    @Id
    private String id;
    private String schoolId;
    private String schoolName;
    private double balance;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<SubWallet> subWallets = new ArrayList();
    private String accountType;

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
    public double getBalance() {
        return this.balance;
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
    public List<SubWallet> getSubWallets() {
        return this.subWallets;
    }

    @Generated
    public String getAccountType() {
        return this.accountType;
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
    public void setBalance(double balance) {
        this.balance = balance;
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
    public void setSubWallets(List<SubWallet> subWallets) {
        this.subWallets = subWallets;
    }

    @Generated
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SchoolWallet)) {
            return false;
        }
        SchoolWallet other = (SchoolWallet)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        if (Double.compare(this.getBalance(), other.getBalance()) != 0) {
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
        List this$subWallets = this.getSubWallets();
        List other$subWallets = other.getSubWallets();
        if (this$subWallets == null ? other$subWallets != null : !((Object)this$subWallets).equals(other$subWallets)) {
            return false;
        }
        String this$accountType = this.getAccountType();
        String other$accountType = other.getAccountType();
        return !(this$accountType == null ? other$accountType != null : !this$accountType.equals(other$accountType));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof SchoolWallet;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        long $balance = Double.doubleToLongBits(this.getBalance());
        result = result * 59 + (int)($balance >>> 32 ^ $balance);
        String $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        String $schoolId = this.getSchoolId();
        result = result * 59 + ($schoolId == null ? 43 : $schoolId.hashCode());
        String $schoolName = this.getSchoolName();
        result = result * 59 + ($schoolName == null ? 43 : $schoolName.hashCode());
        LocalDateTime $createdAt = this.getCreatedAt();
        result = result * 59 + ($createdAt == null ? 43 : ((Object)$createdAt).hashCode());
        LocalDateTime $updatedAt = this.getUpdatedAt();
        result = result * 59 + ($updatedAt == null ? 43 : ((Object)$updatedAt).hashCode());
        List $subWallets = this.getSubWallets();
        result = result * 59 + ($subWallets == null ? 43 : ((Object)$subWallets).hashCode());
        String $accountType = this.getAccountType();
        result = result * 59 + ($accountType == null ? 43 : $accountType.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "SchoolWallet(id=" + this.getId() + ", schoolId=" + this.getSchoolId() + ", schoolName=" + this.getSchoolName() + ", balance=" + this.getBalance() + ", createdAt=" + this.getCreatedAt() + ", updatedAt=" + this.getUpdatedAt() + ", subWallets=" + this.getSubWallets() + ", accountType=" + this.getAccountType() + ")";
    }

    @Generated
    public SchoolWallet() {
    }

    @Generated
    public SchoolWallet(String id, String schoolId, String schoolName, double balance, LocalDateTime createdAt, LocalDateTime updatedAt, List<SubWallet> subWallets, String accountType) {
        this.id = id;
        this.schoolId = schoolId;
        this.schoolName = schoolName;
        this.balance = balance;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.subWallets = subWallets;
        this.accountType = accountType;
    }
}

