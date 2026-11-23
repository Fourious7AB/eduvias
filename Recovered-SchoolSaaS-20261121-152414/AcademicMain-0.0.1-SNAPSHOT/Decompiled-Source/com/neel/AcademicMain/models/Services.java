/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.AcademicMain.models.Services
 *  lombok.Generated
 *  org.springframework.data.mongodb.core.mapping.Document
 */
package com.neel.AcademicMain.models;

import lombok.Generated;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="services")
public class Services {
    private String id;
    private String serviceType;
    private String description;
    private double charge;
    private String schoolId;
    private boolean universal;

    @Generated
    public String getId() {
        return this.id;
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
    public String getSchoolId() {
        return this.schoolId;
    }

    @Generated
    public boolean isUniversal() {
        return this.universal;
    }

    @Generated
    public void setId(String id) {
        this.id = id;
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
    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    @Generated
    public void setUniversal(boolean universal) {
        this.universal = universal;
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
        if (this.isUniversal() != other.isUniversal()) {
            return false;
        }
        String this$id = this.getId();
        String other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) {
            return false;
        }
        String this$serviceType = this.getServiceType();
        String other$serviceType = other.getServiceType();
        if (this$serviceType == null ? other$serviceType != null : !this$serviceType.equals(other$serviceType)) {
            return false;
        }
        String this$description = this.getDescription();
        String other$description = other.getDescription();
        if (this$description == null ? other$description != null : !this$description.equals(other$description)) {
            return false;
        }
        String this$schoolId = this.getSchoolId();
        String other$schoolId = other.getSchoolId();
        return !(this$schoolId == null ? other$schoolId != null : !this$schoolId.equals(other$schoolId));
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
        result = result * 59 + (this.isUniversal() ? 79 : 97);
        String $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        String $serviceType = this.getServiceType();
        result = result * 59 + ($serviceType == null ? 43 : $serviceType.hashCode());
        String $description = this.getDescription();
        result = result * 59 + ($description == null ? 43 : $description.hashCode());
        String $schoolId = this.getSchoolId();
        result = result * 59 + ($schoolId == null ? 43 : $schoolId.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "Services(id=" + this.getId() + ", serviceType=" + this.getServiceType() + ", description=" + this.getDescription() + ", charge=" + this.getCharge() + ", schoolId=" + this.getSchoolId() + ", universal=" + this.isUniversal() + ")";
    }

    @Generated
    public Services(String id, String serviceType, String description, double charge, String schoolId, boolean universal) {
        this.id = id;
        this.serviceType = serviceType;
        this.description = description;
        this.charge = charge;
        this.schoolId = schoolId;
        this.universal = universal;
    }

    @Generated
    public Services() {
    }
}

