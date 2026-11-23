/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.StudentAdmission.model.PaymentDetails
 *  com.neel.StudentAdmission.model.Plan$Features
 *  com.neel.StudentAdmission.model.SubscriptionPlan
 *  lombok.Generated
 *  org.springframework.data.annotation.Id
 *  org.springframework.data.mongodb.core.mapping.Document
 */
package com.neel.StudentAdmission.model;

import com.neel.StudentAdmission.model.PaymentDetails;
import com.neel.StudentAdmission.model.Plan;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="subscription-plan")
public class SubscriptionPlan {
    @Id
    private String id;
    private String planId;
    private String planName;
    private Double amount;
    private Integer duration;
    private List<Plan.Features> features = new ArrayList();
    private String schoolId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime createAt;
    private PaymentDetails paymentDetails;
    private Boolean status;

    @Generated
    public String getId() {
        return this.id;
    }

    @Generated
    public String getPlanId() {
        return this.planId;
    }

    @Generated
    public String getPlanName() {
        return this.planName;
    }

    @Generated
    public Double getAmount() {
        return this.amount;
    }

    @Generated
    public Integer getDuration() {
        return this.duration;
    }

    @Generated
    public List<Plan.Features> getFeatures() {
        return this.features;
    }

    @Generated
    public String getSchoolId() {
        return this.schoolId;
    }

    @Generated
    public LocalDateTime getStartDate() {
        return this.startDate;
    }

    @Generated
    public LocalDateTime getEndDate() {
        return this.endDate;
    }

    @Generated
    public LocalDateTime getCreateAt() {
        return this.createAt;
    }

    @Generated
    public PaymentDetails getPaymentDetails() {
        return this.paymentDetails;
    }

    @Generated
    public Boolean getStatus() {
        return this.status;
    }

    @Generated
    public void setId(String id) {
        this.id = id;
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
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Generated
    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    @Generated
    public void setFeatures(List<Plan.Features> features) {
        this.features = features;
    }

    @Generated
    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    @Generated
    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    @Generated
    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    @Generated
    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    @Generated
    public void setPaymentDetails(PaymentDetails paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    @Generated
    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SubscriptionPlan)) {
            return false;
        }
        SubscriptionPlan other = (SubscriptionPlan)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Double this$amount = this.getAmount();
        Double other$amount = other.getAmount();
        if (this$amount == null ? other$amount != null : !((Object)this$amount).equals(other$amount)) {
            return false;
        }
        Integer this$duration = this.getDuration();
        Integer other$duration = other.getDuration();
        if (this$duration == null ? other$duration != null : !((Object)this$duration).equals(other$duration)) {
            return false;
        }
        Boolean this$status = this.getStatus();
        Boolean other$status = other.getStatus();
        if (this$status == null ? other$status != null : !((Object)this$status).equals(other$status)) {
            return false;
        }
        String this$id = this.getId();
        String other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) {
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
        List this$features = this.getFeatures();
        List other$features = other.getFeatures();
        if (this$features == null ? other$features != null : !((Object)this$features).equals(other$features)) {
            return false;
        }
        String this$schoolId = this.getSchoolId();
        String other$schoolId = other.getSchoolId();
        if (this$schoolId == null ? other$schoolId != null : !this$schoolId.equals(other$schoolId)) {
            return false;
        }
        LocalDateTime this$startDate = this.getStartDate();
        LocalDateTime other$startDate = other.getStartDate();
        if (this$startDate == null ? other$startDate != null : !((Object)this$startDate).equals(other$startDate)) {
            return false;
        }
        LocalDateTime this$endDate = this.getEndDate();
        LocalDateTime other$endDate = other.getEndDate();
        if (this$endDate == null ? other$endDate != null : !((Object)this$endDate).equals(other$endDate)) {
            return false;
        }
        LocalDateTime this$createAt = this.getCreateAt();
        LocalDateTime other$createAt = other.getCreateAt();
        if (this$createAt == null ? other$createAt != null : !((Object)this$createAt).equals(other$createAt)) {
            return false;
        }
        PaymentDetails this$paymentDetails = this.getPaymentDetails();
        PaymentDetails other$paymentDetails = other.getPaymentDetails();
        return !(this$paymentDetails == null ? other$paymentDetails != null : !this$paymentDetails.equals(other$paymentDetails));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof SubscriptionPlan;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Double $amount = this.getAmount();
        result = result * 59 + ($amount == null ? 43 : ((Object)$amount).hashCode());
        Integer $duration = this.getDuration();
        result = result * 59 + ($duration == null ? 43 : ((Object)$duration).hashCode());
        Boolean $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : ((Object)$status).hashCode());
        String $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        String $planId = this.getPlanId();
        result = result * 59 + ($planId == null ? 43 : $planId.hashCode());
        String $planName = this.getPlanName();
        result = result * 59 + ($planName == null ? 43 : $planName.hashCode());
        List $features = this.getFeatures();
        result = result * 59 + ($features == null ? 43 : ((Object)$features).hashCode());
        String $schoolId = this.getSchoolId();
        result = result * 59 + ($schoolId == null ? 43 : $schoolId.hashCode());
        LocalDateTime $startDate = this.getStartDate();
        result = result * 59 + ($startDate == null ? 43 : ((Object)$startDate).hashCode());
        LocalDateTime $endDate = this.getEndDate();
        result = result * 59 + ($endDate == null ? 43 : ((Object)$endDate).hashCode());
        LocalDateTime $createAt = this.getCreateAt();
        result = result * 59 + ($createAt == null ? 43 : ((Object)$createAt).hashCode());
        PaymentDetails $paymentDetails = this.getPaymentDetails();
        result = result * 59 + ($paymentDetails == null ? 43 : $paymentDetails.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "SubscriptionPlan(id=" + this.getId() + ", planId=" + this.getPlanId() + ", planName=" + this.getPlanName() + ", amount=" + this.getAmount() + ", duration=" + this.getDuration() + ", features=" + this.getFeatures() + ", schoolId=" + this.getSchoolId() + ", startDate=" + this.getStartDate() + ", endDate=" + this.getEndDate() + ", createAt=" + this.getCreateAt() + ", paymentDetails=" + this.getPaymentDetails() + ", status=" + this.getStatus() + ")";
    }

    @Generated
    public SubscriptionPlan() {
    }

    @Generated
    public SubscriptionPlan(String id, String planId, String planName, Double amount, Integer duration, List<Plan.Features> features, String schoolId, LocalDateTime startDate, LocalDateTime endDate, LocalDateTime createAt, PaymentDetails paymentDetails, Boolean status) {
        this.id = id;
        this.planId = planId;
        this.planName = planName;
        this.amount = amount;
        this.duration = duration;
        this.features = features;
        this.schoolId = schoolId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.createAt = createAt;
        this.paymentDetails = paymentDetails;
        this.status = status;
    }
}

