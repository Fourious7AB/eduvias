/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.schoolaccount.SchoolAccount.model.TuitionFee
 *  lombok.Generated
 *  org.springframework.data.annotation.Id
 *  org.springframework.data.mongodb.core.mapping.Document
 */
package com.schoolaccount.SchoolAccount.model;

import lombok.Generated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="tuition_fees")
public class TuitionFee {
    @Id
    private String id;
    private String schoolId;
    private String className;
    private double monthlyFee;
    private double quarterlyFee;
    private double halfYearlyFee;
    private double yearlyFee;

    public TuitionFee(String className, double monthlyFee) {
        this.className = className;
        this.monthlyFee = monthlyFee;
        this.quarterlyFee = monthlyFee * 3.0;
        this.halfYearlyFee = monthlyFee * 6.0;
        this.yearlyFee = monthlyFee * 12.0;
    }

    public TuitionFee(String className, double monthlyFee, String schoolId) {
        this.className = className;
        this.monthlyFee = monthlyFee;
        this.quarterlyFee = monthlyFee * 3.0;
        this.halfYearlyFee = monthlyFee * 6.0;
        this.yearlyFee = monthlyFee * 12.0;
        this.schoolId = schoolId;
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
    public String getClassName() {
        return this.className;
    }

    @Generated
    public double getMonthlyFee() {
        return this.monthlyFee;
    }

    @Generated
    public double getQuarterlyFee() {
        return this.quarterlyFee;
    }

    @Generated
    public double getHalfYearlyFee() {
        return this.halfYearlyFee;
    }

    @Generated
    public double getYearlyFee() {
        return this.yearlyFee;
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
    public void setClassName(String className) {
        this.className = className;
    }

    @Generated
    public void setMonthlyFee(double monthlyFee) {
        this.monthlyFee = monthlyFee;
    }

    @Generated
    public void setQuarterlyFee(double quarterlyFee) {
        this.quarterlyFee = quarterlyFee;
    }

    @Generated
    public void setHalfYearlyFee(double halfYearlyFee) {
        this.halfYearlyFee = halfYearlyFee;
    }

    @Generated
    public void setYearlyFee(double yearlyFee) {
        this.yearlyFee = yearlyFee;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TuitionFee)) {
            return false;
        }
        TuitionFee other = (TuitionFee)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        if (Double.compare(this.getMonthlyFee(), other.getMonthlyFee()) != 0) {
            return false;
        }
        if (Double.compare(this.getQuarterlyFee(), other.getQuarterlyFee()) != 0) {
            return false;
        }
        if (Double.compare(this.getHalfYearlyFee(), other.getHalfYearlyFee()) != 0) {
            return false;
        }
        if (Double.compare(this.getYearlyFee(), other.getYearlyFee()) != 0) {
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
        String this$className = this.getClassName();
        String other$className = other.getClassName();
        return !(this$className == null ? other$className != null : !this$className.equals(other$className));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof TuitionFee;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        long $monthlyFee = Double.doubleToLongBits(this.getMonthlyFee());
        result = result * 59 + (int)($monthlyFee >>> 32 ^ $monthlyFee);
        long $quarterlyFee = Double.doubleToLongBits(this.getQuarterlyFee());
        result = result * 59 + (int)($quarterlyFee >>> 32 ^ $quarterlyFee);
        long $halfYearlyFee = Double.doubleToLongBits(this.getHalfYearlyFee());
        result = result * 59 + (int)($halfYearlyFee >>> 32 ^ $halfYearlyFee);
        long $yearlyFee = Double.doubleToLongBits(this.getYearlyFee());
        result = result * 59 + (int)($yearlyFee >>> 32 ^ $yearlyFee);
        String $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        String $schoolId = this.getSchoolId();
        result = result * 59 + ($schoolId == null ? 43 : $schoolId.hashCode());
        String $className = this.getClassName();
        result = result * 59 + ($className == null ? 43 : $className.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "TuitionFee(id=" + this.getId() + ", schoolId=" + this.getSchoolId() + ", className=" + this.getClassName() + ", monthlyFee=" + this.getMonthlyFee() + ", quarterlyFee=" + this.getQuarterlyFee() + ", halfYearlyFee=" + this.getHalfYearlyFee() + ", yearlyFee=" + this.getYearlyFee() + ")";
    }

    @Generated
    public TuitionFee() {
    }
}

