/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.StudentAdmission.dto.PaymentVerificationRequest
 *  lombok.Generated
 */
package com.neel.StudentAdmission.dto;

import lombok.Generated;

public class PaymentVerificationRequest {
    private String orderId;
    private String paymentId;
    private String signature;

    @Generated
    public PaymentVerificationRequest() {
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
    public String getSignature() {
        return this.signature;
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
    public void setSignature(String signature) {
        this.signature = signature;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PaymentVerificationRequest)) {
            return false;
        }
        PaymentVerificationRequest other = (PaymentVerificationRequest)o;
        if (!other.canEqual((Object)this)) {
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
        String this$signature = this.getSignature();
        String other$signature = other.getSignature();
        return !(this$signature == null ? other$signature != null : !this$signature.equals(other$signature));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof PaymentVerificationRequest;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $orderId = this.getOrderId();
        result = result * 59 + ($orderId == null ? 43 : $orderId.hashCode());
        String $paymentId = this.getPaymentId();
        result = result * 59 + ($paymentId == null ? 43 : $paymentId.hashCode());
        String $signature = this.getSignature();
        result = result * 59 + ($signature == null ? 43 : $signature.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "PaymentVerificationRequest(orderId=" + this.getOrderId() + ", paymentId=" + this.getPaymentId() + ", signature=" + this.getSignature() + ")";
    }
}

