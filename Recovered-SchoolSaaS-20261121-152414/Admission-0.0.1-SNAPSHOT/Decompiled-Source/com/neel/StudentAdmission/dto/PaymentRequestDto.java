/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.StudentAdmission.dto.PaymentRequestDto
 *  jakarta.validation.constraints.NotNull
 *  lombok.Generated
 */
package com.neel.StudentAdmission.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Generated;

public class PaymentRequestDto {
    private String planId;
    private String planName;
    private String schoolId;
    private Integer duration;
    @NotNull(message="Plan amount is required")
    private @NotNull(message="Plan amount is required") Double amount;

    @Generated
    public String getPlanId() {
        return this.planId;
    }

    @Generated
    public String getPlanName() {
        return this.planName;
    }

    @Generated
    public String getSchoolId() {
        return this.schoolId;
    }

    @Generated
    public Integer getDuration() {
        return this.duration;
    }

    @Generated
    public Double getAmount() {
        return this.amount;
    }

    @Generated
    public void setPlanId(String planId) {
        this.planId = planId;
    }

    @Generated
    public void setPlanName(String planName) {
        this.planName = planName;
    }

    @Generated
    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    @Generated
    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    @Generated
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PaymentRequestDto)) {
            return false;
        }
        PaymentRequestDto other = (PaymentRequestDto)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Integer this$duration = this.getDuration();
        Integer other$duration = other.getDuration();
        if (this$duration == null ? other$duration != null : !((Object)this$duration).equals(other$duration)) {
            return false;
        }
        Double this$amount = this.getAmount();
        Double other$amount = other.getAmount();
        if (this$amount == null ? other$amount != null : !((Object)this$amount).equals(other$amount)) {
            return false;
        }
        String this$planId = this.getPlanId();
        String other$planId = other.getPlanId();
        if (this$planId == null ? other$planId != null : !this$planId.equals(other$planId)) {
            return false;
        }
        String this$planName = this.getPlanName();
        String other$planName = other.getPlanName();
        if (this$planName == null ? other$planName != null : !this$planName.equals(other$planName)) {
            return false;
        }
        String this$schoolId = this.getSchoolId();
        String other$schoolId = other.getSchoolId();
        return !(this$schoolId == null ? other$schoolId != null : !this$schoolId.equals(other$schoolId));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof PaymentRequestDto;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $duration = this.getDuration();
        result = result * 59 + ($duration == null ? 43 : ((Object)$duration).hashCode());
        Double $amount = this.getAmount();
        result = result * 59 + ($amount == null ? 43 : ((Object)$amount).hashCode());
        String $planId = this.getPlanId();
        result = result * 59 + ($planId == null ? 43 : $planId.hashCode());
        String $planName = this.getPlanName();
        result = result * 59 + ($planName == null ? 43 : $planName.hashCode());
        String $schoolId = this.getSchoolId();
        result = result * 59 + ($schoolId == null ? 43 : $schoolId.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "PaymentRequestDto(planId=" + this.getPlanId() + ", planName=" + this.getPlanName() + ", schoolId=" + this.getSchoolId() + ", duration=" + this.getDuration() + ", amount=" + this.getAmount() + ")";
    }

    @Generated
    public PaymentRequestDto() {
    }

    @Generated
    public PaymentRequestDto(String planId, String planName, String schoolId, Integer duration, Double amount) {
        this.planId = planId;
        this.planName = planName;
        this.schoolId = schoolId;
        this.duration = duration;
        this.amount = amount;
    }
}

