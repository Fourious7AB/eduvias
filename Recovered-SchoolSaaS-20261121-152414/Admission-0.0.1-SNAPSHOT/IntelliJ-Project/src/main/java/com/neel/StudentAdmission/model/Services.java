/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.StudentAdmission.model.Services
 *  lombok.Generated
 *  org.hibernate.validator.constraints.UniqueElements
 */
package com.neel.StudentAdmission.model;

import lombok.Generated;
import org.hibernate.validator.constraints.UniqueElements;

public class Services {
    @UniqueElements(message="Service Type already exists")
    private String serviceType;
    private String description;
    private double charge;

    @Generated
    public Services() {
    }

    @Generated
    public String getServiceType() {
        return this.serviceType;
    }

    @Generated
    public String getDescription() {
        return this.description;
    }

    @Generated
    public double getCharge() {
        return this.charge;
    }

    @Generated
    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    @Generated
    public void setDescription(String description) {
        this.description = description;
    }

    @Generated
    public void setCharge(double charge) {
        this.charge = charge;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Services)) {
            return false;
        }
        Services other = (Services)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        if (Double.compare(this.getCharge(), other.getCharge()) != 0) {
            return false;
        }
        String this$serviceType = this.getServiceType();
        String other$serviceType = other.getServiceType();
        if (this$serviceType == null ? other$serviceType != null : !this$serviceType.equals(other$serviceType)) {
            return false;
        }
        String this$description = this.getDescription();
        String other$description = other.getDescription();
        return !(this$description == null ? other$description != null : !this$description.equals(other$description));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof Services;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        long $charge = Double.doubleToLongBits(this.getCharge());
        result = result * 59 + (int)($charge >>> 32 ^ $charge);
        String $serviceType = this.getServiceType();
        result = result * 59 + ($serviceType == null ? 43 : $serviceType.hashCode());
        String $description = this.getDescription();
        result = result * 59 + ($description == null ? 43 : $description.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "Services(serviceType=" + this.getServiceType() + ", description=" + this.getDescription() + ", charge=" + this.getCharge() + ")";
    }
}

