/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.schoolaccount.SchoolAccount.config.PaymentConfig
 *  lombok.Generated
 *  org.springframework.beans.factory.annotation.Value
 *  org.springframework.context.annotation.Configuration
 */
package com.schoolaccount.SchoolAccount.config;

import lombok.Generated;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentConfig {
    @Value(value="${payment.distribution.auto}")
    private boolean autoDistribution;
    @Value(value="${payment.notification.enabled}")
    private boolean notificationsEnabled;
    @Value(value="${payment.webhook.secret}")
    private String webhookSecret;
    @Value(value="${payment.superadmin.commission.percentage}")
    private double superAdminCommissionPercentage;
    @Value(value="${payment.minimum.withdrawal}")
    private double minimumWithdrawalAmount;
    @Value(value="${payment.processing.days}")
    private int payoutProcessingDays;

    @Generated
    public PaymentConfig() {
    }

    @Generated
    public boolean isAutoDistribution() {
        return this.autoDistribution;
    }

    @Generated
    public boolean isNotificationsEnabled() {
        return this.notificationsEnabled;
    }

    @Generated
    public String getWebhookSecret() {
        return this.webhookSecret;
    }

    @Generated
    public double getSuperAdminCommissionPercentage() {
        return this.superAdminCommissionPercentage;
    }

    @Generated
    public double getMinimumWithdrawalAmount() {
        return this.minimumWithdrawalAmount;
    }

    @Generated
    public int getPayoutProcessingDays() {
        return this.payoutProcessingDays;
    }

    @Generated
    public void setAutoDistribution(boolean autoDistribution) {
        this.autoDistribution = autoDistribution;
    }

    @Generated
    public void setNotificationsEnabled(boolean notificationsEnabled) {
        this.notificationsEnabled = notificationsEnabled;
    }

    @Generated
    public void setWebhookSecret(String webhookSecret) {
        this.webhookSecret = webhookSecret;
    }

    @Generated
    public void setSuperAdminCommissionPercentage(double superAdminCommissionPercentage) {
        this.superAdminCommissionPercentage = superAdminCommissionPercentage;
    }

    @Generated
    public void setMinimumWithdrawalAmount(double minimumWithdrawalAmount) {
        this.minimumWithdrawalAmount = minimumWithdrawalAmount;
    }

    @Generated
    public void setPayoutProcessingDays(int payoutProcessingDays) {
        this.payoutProcessingDays = payoutProcessingDays;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PaymentConfig)) {
            return false;
        }
        PaymentConfig other = (PaymentConfig)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        if (this.isAutoDistribution() != other.isAutoDistribution()) {
            return false;
        }
        if (this.isNotificationsEnabled() != other.isNotificationsEnabled()) {
            return false;
        }
        if (Double.compare(this.getSuperAdminCommissionPercentage(), other.getSuperAdminCommissionPercentage()) != 0) {
            return false;
        }
        if (Double.compare(this.getMinimumWithdrawalAmount(), other.getMinimumWithdrawalAmount()) != 0) {
            return false;
        }
        if (this.getPayoutProcessingDays() != other.getPayoutProcessingDays()) {
            return false;
        }
        String this$webhookSecret = this.getWebhookSecret();
        String other$webhookSecret = other.getWebhookSecret();
        return !(this$webhookSecret == null ? other$webhookSecret != null : !this$webhookSecret.equals(other$webhookSecret));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof PaymentConfig;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + (this.isAutoDistribution() ? 79 : 97);
        result = result * 59 + (this.isNotificationsEnabled() ? 79 : 97);
        long $superAdminCommissionPercentage = Double.doubleToLongBits(this.getSuperAdminCommissionPercentage());
        result = result * 59 + (int)($superAdminCommissionPercentage >>> 32 ^ $superAdminCommissionPercentage);
        long $minimumWithdrawalAmount = Double.doubleToLongBits(this.getMinimumWithdrawalAmount());
        result = result * 59 + (int)($minimumWithdrawalAmount >>> 32 ^ $minimumWithdrawalAmount);
        result = result * 59 + this.getPayoutProcessingDays();
        String $webhookSecret = this.getWebhookSecret();
        result = result * 59 + ($webhookSecret == null ? 43 : $webhookSecret.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "PaymentConfig(autoDistribution=" + this.isAutoDistribution() + ", notificationsEnabled=" + this.isNotificationsEnabled() + ", webhookSecret=" + this.getWebhookSecret() + ", superAdminCommissionPercentage=" + this.getSuperAdminCommissionPercentage() + ", minimumWithdrawalAmount=" + this.getMinimumWithdrawalAmount() + ", payoutProcessingDays=" + this.getPayoutProcessingDays() + ")";
    }
}

