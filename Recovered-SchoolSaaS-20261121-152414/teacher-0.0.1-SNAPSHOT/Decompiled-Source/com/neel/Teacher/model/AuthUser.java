/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.Teacher.model.AuthUser
 *  lombok.Generated
 */
package com.neel.Teacher.model;

import lombok.Generated;

public class AuthUser {
    private String username;
    private String password;
    private String role;

    @Generated
    public AuthUser() {
    }

    @Generated
    public String getUsername() {
        return this.username;
    }

    @Generated
    public String getPassword() {
        return this.password;
    }

    @Generated
    public String getRole() {
        return this.role;
    }

    @Generated
    public void setUsername(String username) {
        this.username = username;
    }

    @Generated
    public void setPassword(String password) {
        this.password = password;
    }

    @Generated
    public void setRole(String role) {
        this.role = role;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AuthUser)) {
            return false;
        }
        AuthUser other = (AuthUser)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        String this$username = this.getUsername();
        String other$username = other.getUsername();
        if (this$username == null ? other$username != null : !this$username.equals(other$username)) {
            return false;
        }
        String this$password = this.getPassword();
        String other$password = other.getPassword();
        if (this$password == null ? other$password != null : !this$password.equals(other$password)) {
            return false;
        }
        String this$role = this.getRole();
        String other$role = other.getRole();
        return !(this$role == null ? other$role != null : !this$role.equals(other$role));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof AuthUser;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $username = this.getUsername();
        result = result * 59 + ($username == null ? 43 : $username.hashCode());
        String $password = this.getPassword();
        result = result * 59 + ($password == null ? 43 : $password.hashCode());
        String $role = this.getRole();
        result = result * 59 + ($role == null ? 43 : $role.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "AuthUser(username=" + this.getUsername() + ", password=" + this.getPassword() + ", role=" + this.getRole() + ")";
    }
}

