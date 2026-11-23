/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.Teacher.model.EntityLookupResult
 *  com.neel.Teacher.model.EntityType
 *  lombok.Generated
 */
package com.neel.Teacher.model;

import com.neel.Teacher.model.EntityType;
import lombok.Generated;

public class EntityLookupResult {
    private boolean found;
    private EntityType role;
    private Object user;

    public static EntityLookupResult notFound() {
        return new EntityLookupResult(false, EntityType.NONE, null);
    }

    public static EntityLookupResult found(EntityType type, Object entity) {
        return new EntityLookupResult(true, type, entity);
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
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof EntityLookupResult)) {
            return false;
        }
        EntityLookupResult other = (EntityLookupResult)o;
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
        return !(this$user == null ? other$user != null : !this$user.equals(other$user));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof EntityLookupResult;
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
        return result;
    }

    @Generated
    public String toString() {
        return "EntityLookupResult(found=" + this.isFound() + ", role=" + this.getRole() + ", user=" + this.getUser() + ")";
    }

    @Generated
    public EntityLookupResult() {
    }

    @Generated
    public EntityLookupResult(boolean found, EntityType role, Object user) {
        this.found = found;
        this.role = role;
        this.user = user;
    }
}

