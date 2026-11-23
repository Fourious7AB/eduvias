/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.StudentAdmission.dto.FailedAdmissionDto
 *  lombok.Generated
 */
package com.neel.StudentAdmission.dto;

import lombok.Generated;

public class FailedAdmissionDto {
    private String studentName;
    private String className;
    private String reason;

    @Generated
    public String getStudentName() {
        return this.studentName;
    }

    @Generated
    public String getClassName() {
        return this.className;
    }

    @Generated
    public String getReason() {
        return this.reason;
    }

    @Generated
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    @Generated
    public void setClassName(String className) {
        this.className = className;
    }

    @Generated
    public void setReason(String reason) {
        this.reason = reason;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof FailedAdmissionDto)) {
            return false;
        }
        FailedAdmissionDto other = (FailedAdmissionDto)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        String this$studentName = this.getStudentName();
        String other$studentName = other.getStudentName();
        if (this$studentName == null ? other$studentName != null : !this$studentName.equals(other$studentName)) {
            return false;
        }
        String this$className = this.getClassName();
        String other$className = other.getClassName();
        if (this$className == null ? other$className != null : !this$className.equals(other$className)) {
            return false;
        }
        String this$reason = this.getReason();
        String other$reason = other.getReason();
        return !(this$reason == null ? other$reason != null : !this$reason.equals(other$reason));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof FailedAdmissionDto;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $studentName = this.getStudentName();
        result = result * 59 + ($studentName == null ? 43 : $studentName.hashCode());
        String $className = this.getClassName();
        result = result * 59 + ($className == null ? 43 : $className.hashCode());
        String $reason = this.getReason();
        result = result * 59 + ($reason == null ? 43 : $reason.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "FailedAdmissionDto(studentName=" + this.getStudentName() + ", className=" + this.getClassName() + ", reason=" + this.getReason() + ")";
    }

    @Generated
    public FailedAdmissionDto(String studentName, String className, String reason) {
        this.studentName = studentName;
        this.className = className;
        this.reason = reason;
    }

    @Generated
    public FailedAdmissionDto() {
    }
}

