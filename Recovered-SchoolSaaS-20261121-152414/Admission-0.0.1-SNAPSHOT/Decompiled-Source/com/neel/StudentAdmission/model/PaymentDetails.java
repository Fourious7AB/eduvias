/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.StudentAdmission.model.PaymentDetails
 *  lombok.Generated
 */
package com.neel.StudentAdmission.model;

import java.time.LocalDateTime;
import lombok.Generated;

public class PaymentDetails {
    private String paymentId;
    private String orderId;
    private Double amount;
    private String currency;
    private LocalDateTime timestamp;
    private String status;

    @Generated
    public String getPaymentId() {
        return this.paymentId;
    }

    @Generated
    public String getOrderId() {
        return this.orderId;
    }

    @Generated
    public Double getAmount() {
        return this.amount;
    }

    @Generated
    public String getCurrency() {
        return this.currency;
    }

    @Generated
    public LocalDateTime getTimestamp() {
        return this.timestamp;
    }

    @Generated
    public String getStatus() {
        return this.status;
    }

    @Generated
    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    @Generated
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Generated
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Generated
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Generated
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Generated
    public void setStatus(String status) {
        this.status = status;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PaymentDetails)) {
            return false;
        }
        PaymentDetails other = (PaymentDetails)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Double this$amount = this.getAmount();
        Double other$amount = other.getAmount();
        if (this$amount == null ? other$amount != null : !((Object)this$amount).equals(other$amount)) {
            return false;
        }
        String this$paymentId = this.getPaymentId();
        String other$paymentId = other.getPaymentId();
        if (this$paymentId == null ? other$paymentId != null : !this$paymentId.equals(other$paymentId)) {
            return false;
        }
        String this$orderId = this.getOrderId();
        String other$orderId = other.getOrderId();
        if (this$orderId == null ? other$orderId != null : !this$orderId.equals(other$orderId)) {
            return false;
        }
        String this$currency = this.getCurrency();
        String other$currency = other.getCurrency();
        if (this$currency == null ? other$currency != null : !this$currency.equals(other$currency)) {
            return false;
        }
        LocalDateTime this$timestamp = this.getTimestamp();
        LocalDateTime other$timestamp = other.getTimestamp();
        if (this$timestamp == null ? other$timestamp != null : !((Object)this$timestamp).equals(other$timestamp)) {
            return false;
        }
        String this$status = this.getStatus();
        String other$status = other.getStatus();
        return !(this$status == null ? other$status != null : !this$status.equals(other$status));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof PaymentDetails;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Double $amount = this.getAmount();
        result = result * 59 + ($amount == null ? 43 : ((Object)$amount).hashCode());
        String $paymentId = this.getPaymentId();
        result = result * 59 + ($paymentId == null ? 43 : $paymentId.hashCode());
        String $orderId = this.getOrderId();
        result = result * 59 + ($orderId == null ? 43 : $orderId.hashCode());
        String $currency = this.getCurrency();
        result = result * 59 + ($currency == null ? 43 : $currency.hashCode());
        LocalDateTime $timestamp = this.getTimestamp();
        result = result * 59 + ($timestamp == null ? 43 : ((Object)$timestamp).hashCode());
        String $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : $status.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "PaymentDetails(paymentId=" + this.getPaymentId() + ", orderId=" + this.getOrderId() + ", amount=" + this.getAmount() + ", currency=" + this.getCurrency() + ", timestamp=" + this.getTimestamp() + ", status=" + this.getStatus() + ")";
    }

    @Generated
    public PaymentDetails() {
    }

    @Generated
    public PaymentDetails(String paymentId, String orderId, Double amount, String currency, LocalDateTime timestamp, String status) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.amount = amount;
        this.currency = currency;
        this.timestamp = timestamp;
        this.status = status;
    }
}

