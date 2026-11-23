/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.StudentAdmission.model.PaymentHistory
 *  lombok.Generated
 *  org.springframework.data.mongodb.core.mapping.Document
 */
package com.neel.StudentAdmission.model;

import java.time.LocalDateTime;
import lombok.Generated;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="subscription-payment-history")
public class PaymentHistory {
    private String id;
    private String orderId;
    private String paymentId;
    private Double paidAmount;
    private String planName;
    private String schoolId;
    private String schoolName;
    private LocalDateTime paymentDate;
    private String paymentStatus;

    @Generated
    public String getId() {
        return this.id;
    }

    @Generated
    public String getOrderId() {
        return this.orderId;
    }

    @Generated
    public String getPaymentId() {
        return this.paymentId;
    }

    @Generated
    public Double getPaidAmount() {
        return this.paidAmount;
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
    public String getSchoolName() {
        return this.schoolName;
    }

    @Generated
    public LocalDateTime getPaymentDate() {
        return this.paymentDate;
    }

    @Generated
    public String getPaymentStatus() {
        return this.paymentStatus;
    }

    @Generated
    public void setId(String id) {
        this.id = id;
    }

    @Generated
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Generated
    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    @Generated
    public void setPaidAmount(Double paidAmount) {
        this.paidAmount = paidAmount;
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
    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    @Generated
    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    @Generated
    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PaymentHistory)) {
            return false;
        }
        PaymentHistory other = (PaymentHistory)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Double this$paidAmount = this.getPaidAmount();
        Double other$paidAmount = other.getPaidAmount();
        if (this$paidAmount == null ? other$paidAmount != null : !((Object)this$paidAmount).equals(other$paidAmount)) {
            return false;
        }
        String this$id = this.getId();
        String other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) {
            return false;
        }
        String this$orderId = this.getOrderId();
        String other$orderId = other.getOrderId();
        if (this$orderId == null ? other$orderId != null : !this$orderId.equals(other$orderId)) {
            return false;
        }
        String this$paymentId = this.getPaymentId();
        String other$paymentId = other.getPaymentId();
        if (this$paymentId == null ? other$paymentId != null : !this$paymentId.equals(other$paymentId)) {
            return false;
        }
        String this$planName = this.getPlanName();
        String other$planName = other.getPlanName();
        if (this$planName == null ? other$planName != null : !this$planName.equals(other$planName)) {
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
        LocalDateTime this$paymentDate = this.getPaymentDate();
        LocalDateTime other$paymentDate = other.getPaymentDate();
        if (this$paymentDate == null ? other$paymentDate != null : !((Object)this$paymentDate).equals(other$paymentDate)) {
            return false;
        }
        String this$paymentStatus = this.getPaymentStatus();
        String other$paymentStatus = other.getPaymentStatus();
        return !(this$paymentStatus == null ? other$paymentStatus != null : !this$paymentStatus.equals(other$paymentStatus));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof PaymentHistory;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Double $paidAmount = this.getPaidAmount();
        result = result * 59 + ($paidAmount == null ? 43 : ((Object)$paidAmount).hashCode());
        String $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        String $orderId = this.getOrderId();
        result = result * 59 + ($orderId == null ? 43 : $orderId.hashCode());
        String $paymentId = this.getPaymentId();
        result = result * 59 + ($paymentId == null ? 43 : $paymentId.hashCode());
        String $planName = this.getPlanName();
        result = result * 59 + ($planName == null ? 43 : $planName.hashCode());
        String $schoolId = this.getSchoolId();
        result = result * 59 + ($schoolId == null ? 43 : $schoolId.hashCode());
        String $schoolName = this.getSchoolName();
        result = result * 59 + ($schoolName == null ? 43 : $schoolName.hashCode());
        LocalDateTime $paymentDate = this.getPaymentDate();
        result = result * 59 + ($paymentDate == null ? 43 : ((Object)$paymentDate).hashCode());
        String $paymentStatus = this.getPaymentStatus();
        result = result * 59 + ($paymentStatus == null ? 43 : $paymentStatus.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "PaymentHistory(id=" + this.getId() + ", orderId=" + this.getOrderId() + ", paymentId=" + this.getPaymentId() + ", paidAmount=" + this.getPaidAmount() + ", planName=" + this.getPlanName() + ", schoolId=" + this.getSchoolId() + ", schoolName=" + this.getSchoolName() + ", paymentDate=" + this.getPaymentDate() + ", paymentStatus=" + this.getPaymentStatus() + ")";
    }

    @Generated
    public PaymentHistory(String id, String orderId, String paymentId, Double paidAmount, String planName, String schoolId, String schoolName, LocalDateTime paymentDate, String paymentStatus) {
        this.id = id;
        this.orderId = orderId;
        this.paymentId = paymentId;
        this.paidAmount = paidAmount;
        this.planName = planName;
        this.schoolId = schoolId;
        this.schoolName = schoolName;
        this.paymentDate = paymentDate;
        this.paymentStatus = paymentStatus;
    }

    @Generated
    public PaymentHistory() {
    }
}

