/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.StudentAdmission.model.EntityType
 *  com.neel.StudentAdmission.model.LookUp
 *  lombok.Generated
 */
package com.neel.StudentAdmission.model;

import com.neel.StudentAdmission.model.EntityType;
import lombok.Generated;

public class LookUp {
    private boolean found;
    private EntityType role;
    private Object user;
    private String message;

    public static LookUp notFound() {
        return new LookUp(false, EntityType.NONE, null, "Details not found");
    }

    public static LookUp found(EntityType type, Object entity) {
        return new LookUp(true, type, entity, "Details found successfully");
    }

    @Generated
    public boolean isFound() {
        return this.found;
    }

    @Generated
    public EntityType getRole() {
        return this.role;
    }

    @Generated
    public Object getUser() {
        return this.user;
    }

    @Generated
    public String getMessage() {
        return this.message;
    }

    @Generated
    public void setFound(boolean found) {
        this.found = found;
    }

    @Generated
    public void setRole(EntityType role) {
        this.role = role;
    }

    @Generated
    public void setUser(Object user) {
        this.user = user;
    }

    @Generated
    public void setMessage(String message) {
        this.message = message;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof LookUp)) {
            return false;
        }
        LookUp other = (LookUp)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        if (this.isFound() != other.isFound()) {
            return false;
        }
        EntityType this$role = this.getRole();
        EntityType other$role = other.getRole();
        if (this$role == null ? other$role != null : !this$role.equals(other$role)) {
            return false;
        }
        Object this$user = this.getUser();
        Object other$user = other.getUser();
        if (this$user == null ? other$user != null : !this$user.equals(other$user)) {
            return false;
        }
        String this$message = this.getMessage();
        String other$message = other.getMessage();
        return !(this$message == null ? other$message != null : !this$message.equals(other$message));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof LookUp;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + (this.isFound() ? 79 : 97);
        EntityType $role = this.getRole();
        result = result * 59 + ($role == null ? 43 : $role.hashCode());
        Object $user = this.getUser();
        result = result * 59 + ($user == null ? 43 : $user.hashCode());
        String $message = this.getMessage();
        result = result * 59 + ($message == null ? 43 : $message.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "LookUp(found=" + this.isFound() + ", role=" + this.getRole() + ", user=" + this.getUser() + ", message=" + this.getMessage() + ")";
    }

    @Generated
    public LookUp(boolean found, EntityType role, Object user, String message) {
        this.found = found;
        this.role = role;
        this.user = user;
        this.message = message;
    }

    @Generated
    public LookUp() {
    }
}

