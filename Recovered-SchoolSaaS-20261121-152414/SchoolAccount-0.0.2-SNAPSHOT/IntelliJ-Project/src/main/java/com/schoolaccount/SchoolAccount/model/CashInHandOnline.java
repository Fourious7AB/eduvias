/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.schoolaccount.SchoolAccount.model.CashInHandOnline
 *  lombok.Generated
 *  org.springframework.data.annotation.Id
 *  org.springframework.data.mongodb.core.mapping.Document
 */
package com.schoolaccount.SchoolAccount.model;

import lombok.Generated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="cash_in_hand_online")
public class CashInHandOnline {
    @Id
    private String id;
    private String schoolId;
    private double cashInHand;
    private double onlineAmount;

    @Generated
    public CashInHandOnline() {
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
    public double getCashInHand() {
        return this.cashInHand;
    }

    @Generated
    public double getOnlineAmount() {
        return this.onlineAmount;
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
    public void setCashInHand(double cashInHand) {
        this.cashInHand = cashInHand;
    }

    @Generated
    public void setOnlineAmount(double onlineAmount) {
        this.onlineAmount = onlineAmount;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CashInHandOnline)) {
            return false;
        }
        CashInHandOnline other = (CashInHandOnline)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        if (Double.compare(this.getCashInHand(), other.getCashInHand()) != 0) {
            return false;
        }
        if (Double.compare(this.getOnlineAmount(), other.getOnlineAmount()) != 0) {
            return false;
        }
        String this$id = this.getId();
        String other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) {
            return false;
        }
        String this$schoolId = this.getSchoolId();
        String other$schoolId = other.getSchoolId();
        return !(this$schoolId == null ? other$schoolId != null : !this$schoolId.equals(other$schoolId));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof CashInHandOnline;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        long $cashInHand = Double.doubleToLongBits(this.getCashInHand());
        result = result * 59 + (int)($cashInHand >>> 32 ^ $cashInHand);
        long $onlineAmount = Double.doubleToLongBits(this.getOnlineAmount());
        result = result * 59 + (int)($onlineAmount >>> 32 ^ $onlineAmount);
        String $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        String $schoolId = this.getSchoolId();
        result = result * 59 + ($schoolId == null ? 43 : $schoolId.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "CashInHandOnline(id=" + this.getId() + ", schoolId=" + this.getSchoolId() + ", cashInHand=" + this.getCashInHand() + ", onlineAmount=" + this.getOnlineAmount() + ")";
    }
}

