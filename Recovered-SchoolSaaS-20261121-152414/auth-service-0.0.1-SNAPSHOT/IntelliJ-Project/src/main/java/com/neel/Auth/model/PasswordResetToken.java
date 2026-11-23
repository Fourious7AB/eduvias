/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.Auth.model.PasswordResetToken
 */
package com.neel.Auth.model;

import java.time.LocalDateTime;

public class PasswordResetToken {
    private String token;
    private String email;
    private LocalDateTime expirationTime;

    public String getToken() {
        return this.token;
    }

    public String getEmail() {
        return this.email;
    }

    public LocalDateTime getExpirationTime() {
        return this.expirationTime;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setExpirationTime(LocalDateTime expirationTime) {
        this.expirationTime = expirationTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PasswordResetToken)) {
            return false;
        }
        PasswordResetToken other = (PasswordResetToken)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        String this$token = this.getToken();
        String other$token = other.getToken();
        if (this$token == null ? other$token != null : !this$token.equals(other$token)) {
            return false;
        }
        String this$email = this.getEmail();
        String other$email = other.getEmail();
        if (this$email == null ? other$email != null : !this$email.equals(other$email)) {
            return false;
        }
        LocalDateTime this$expirationTime = this.getExpirationTime();
        LocalDateTime other$expirationTime = other.getExpirationTime();
        return !(this$expirationTime == null ? other$expirationTime != null : !((Object)this$expirationTime).equals(other$expirationTime));
    }

    protected boolean canEqual(Object other) {
        return other instanceof PasswordResetToken;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $token = this.getToken();
        result = result * 59 + ($token == null ? 43 : $token.hashCode());
        String $email = this.getEmail();
        result = result * 59 + ($email == null ? 43 : $email.hashCode());
        LocalDateTime $expirationTime = this.getExpirationTime();
        result = result * 59 + ($expirationTime == null ? 43 : ((Object)$expirationTime).hashCode());
        return result;
    }

    public String toString() {
        return "PasswordResetToken(token=" + this.getToken() + ", email=" + this.getEmail() + ", expirationTime=" + this.getExpirationTime() + ")";
    }

    public PasswordResetToken(String token, String email, LocalDateTime expirationTime) {
        this.token = token;
        this.email = email;
        this.expirationTime = expirationTime;
    }

    public PasswordResetToken() {
    }
}

