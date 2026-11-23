/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.StudentAdmission.dto.BulkAdmissionResponse
 *  com.neel.StudentAdmission.dto.FailedAdmissionDto
 *  com.neel.StudentAdmission.model.Admission
 *  lombok.Generated
 */
package com.neel.StudentAdmission.dto;

import com.neel.StudentAdmission.dto.FailedAdmissionDto;
import com.neel.StudentAdmission.model.Admission;
import java.util.List;
import lombok.Generated;

public class BulkAdmissionResponse {
    private List<Admission> successfulAdmissions;
    private List<FailedAdmissionDto> failedAdmissions;

    @Generated
    public List<Admission> getSuccessfulAdmissions() {
        return this.successfulAdmissions;
    }

    @Generated
    public List<FailedAdmissionDto> getFailedAdmissions() {
        return this.failedAdmissions;
    }

    @Generated
    public void setSuccessfulAdmissions(List<Admission> successfulAdmissions) {
        this.successfulAdmissions = successfulAdmissions;
    }

    @Generated
    public void setFailedAdmissions(List<FailedAdmissionDto> failedAdmissions) {
        this.failedAdmissions = failedAdmissions;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof BulkAdmissionResponse)) {
            return false;
        }
        BulkAdmissionResponse other = (BulkAdmissionResponse)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        List this$successfulAdmissions = this.getSuccessfulAdmissions();
        List other$successfulAdmissions = other.getSuccessfulAdmissions();
        if (this$successfulAdmissions == null ? other$successfulAdmissions != null : !((Object)this$successfulAdmissions).equals(other$successfulAdmissions)) {
            return false;
        }
        List this$failedAdmissions = this.getFailedAdmissions();
        List other$failedAdmissions = other.getFailedAdmissions();
        return !(this$failedAdmissions == null ? other$failedAdmissions != null : !((Object)this$failedAdmissions).equals(other$failedAdmissions));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof BulkAdmissionResponse;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        List $successfulAdmissions = this.getSuccessfulAdmissions();
        result = result * 59 + ($successfulAdmissions == null ? 43 : ((Object)$successfulAdmissions).hashCode());
        List $failedAdmissions = this.getFailedAdmissions();
        result = result * 59 + ($failedAdmissions == null ? 43 : ((Object)$failedAdmissions).hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "BulkAdmissionResponse(successfulAdmissions=" + this.getSuccessfulAdmissions() + ", failedAdmissions=" + this.getFailedAdmissions() + ")";
    }

    @Generated
    public BulkAdmissionResponse(List<Admission> successfulAdmissions, List<FailedAdmissionDto> failedAdmissions) {
        this.successfulAdmissions = successfulAdmissions;
        this.failedAdmissions = failedAdmissions;
    }
}

