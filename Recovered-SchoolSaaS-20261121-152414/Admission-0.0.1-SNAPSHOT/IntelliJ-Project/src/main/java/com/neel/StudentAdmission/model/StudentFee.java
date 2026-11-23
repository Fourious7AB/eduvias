/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.StudentAdmission.model.StudentFee
 *  lombok.Generated
 */
package com.neel.StudentAdmission.model;

import lombok.Generated;

public class StudentFee {
    private String admissionNumber;
    private String newClassName;
    private String newSection;
    private String academicYear;

    @Generated
    public StudentFee() {
    }

    @Generated
    public String getAdmissionNumber() {
        return this.admissionNumber;
    }

    @Generated
    public String getNewClassName() {
        return this.newClassName;
    }

    @Generated
    public String getNewSection() {
        return this.newSection;
    }

    @Generated
    public String getAcademicYear() {
        return this.academicYear;
    }

    @Generated
    public void setAdmissionNumber(String admissionNumber) {
        this.admissionNumber = admissionNumber;
    }

    @Generated
    public void setNewClassName(String newClassName) {
        this.newClassName = newClassName;
    }

    @Generated
    public void setNewSection(String newSection) {
        this.newSection = newSection;
    }

    @Generated
    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof StudentFee)) {
            return false;
        }
        StudentFee other = (StudentFee)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        String this$admissionNumber = this.getAdmissionNumber();
        String other$admissionNumber = other.getAdmissionNumber();
        if (this$admissionNumber == null ? other$admissionNumber != null : !this$admissionNumber.equals(other$admissionNumber)) {
            return false;
        }
        String this$newClassName = this.getNewClassName();
        String other$newClassName = other.getNewClassName();
        if (this$newClassName == null ? other$newClassName != null : !this$newClassName.equals(other$newClassName)) {
            return false;
        }
        String this$newSection = this.getNewSection();
        String other$newSection = other.getNewSection();
        if (this$newSection == null ? other$newSection != null : !this$newSection.equals(other$newSection)) {
            return false;
        }
        String this$academicYear = this.getAcademicYear();
        String other$academicYear = other.getAcademicYear();
        return !(this$academicYear == null ? other$academicYear != null : !this$academicYear.equals(other$academicYear));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof StudentFee;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $admissionNumber = this.getAdmissionNumber();
        result = result * 59 + ($admissionNumber == null ? 43 : $admissionNumber.hashCode());
        String $newClassName = this.getNewClassName();
        result = result * 59 + ($newClassName == null ? 43 : $newClassName.hashCode());
        String $newSection = this.getNewSection();
        result = result * 59 + ($newSection == null ? 43 : $newSection.hashCode());
        String $academicYear = this.getAcademicYear();
        result = result * 59 + ($academicYear == null ? 43 : $academicYear.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "StudentFee(admissionNumber=" + this.getAdmissionNumber() + ", newClassName=" + this.getNewClassName() + ", newSection=" + this.getNewSection() + ", academicYear=" + this.getAcademicYear() + ")";
    }
}

