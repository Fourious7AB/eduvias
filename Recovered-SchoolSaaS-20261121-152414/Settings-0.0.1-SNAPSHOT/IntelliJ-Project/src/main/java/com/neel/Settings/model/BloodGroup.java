/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.Settings.model.BloodGroup
 *  lombok.Generated
 *  org.springframework.data.annotation.Id
 *  org.springframework.data.mongodb.core.mapping.Document
 */
package com.neel.Settings.model;

import lombok.Generated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="bloodgroups")
public class BloodGroup {
    @Id
    private String id;
    private String schoolId;
    private String bloodGroupName;
    private String bloodGroupType;
    private String bloodGroup;
    private boolean universal;

    @Generated
    public String getId() {
        return this.id;
    }

    @Generated
    public String getSchoolId() {
        return this.schoolId;
    }

    @Generated
    public String getBloodGroupName() {
        return this.bloodGroupName;
    }

    @Generated
    public String getBloodGroupType() {
        return this.bloodGroupType;
    }

    @Generated
    public String getBloodGroup() {
        return this.bloodGroup;
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
    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    @Generated
    public void setBloodGroupName(String bloodGroupName) {
        this.bloodGroupName = bloodGroupName;
    }

    @Generated
    public void setBloodGroupType(String bloodGroupType) {
        this.bloodGroupType = bloodGroupType;
    }

    @Generated
    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
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
        if (!(o instanceof BloodGroup)) {
            return false;
        }
        BloodGroup other = (BloodGroup)o;
        if (!other.canEqual((Object)this)) {
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
        String this$schoolId = this.getSchoolId();
        String other$schoolId = other.getSchoolId();
        if (this$schoolId == null ? other$schoolId != null : !this$schoolId.equals(other$schoolId)) {
            return false;
        }
        String this$bloodGroupName = this.getBloodGroupName();
        String other$bloodGroupName = other.getBloodGroupName();
        if (this$bloodGroupName == null ? other$bloodGroupName != null : !this$bloodGroupName.equals(other$bloodGroupName)) {
            return false;
        }
        String this$bloodGroupType = this.getBloodGroupType();
        String other$bloodGroupType = other.getBloodGroupType();
        if (this$bloodGroupType == null ? other$bloodGroupType != null : !this$bloodGroupType.equals(other$bloodGroupType)) {
            return false;
        }
        String this$bloodGroup = this.getBloodGroup();
        String other$bloodGroup = other.getBloodGroup();
        return !(this$bloodGroup == null ? other$bloodGroup != null : !this$bloodGroup.equals(other$bloodGroup));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof BloodGroup;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + (this.isUniversal() ? 79 : 97);
        String $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        String $schoolId = this.getSchoolId();
        result = result * 59 + ($schoolId == null ? 43 : $schoolId.hashCode());
        String $bloodGroupName = this.getBloodGroupName();
        result = result * 59 + ($bloodGroupName == null ? 43 : $bloodGroupName.hashCode());
        String $bloodGroupType = this.getBloodGroupType();
        result = result * 59 + ($bloodGroupType == null ? 43 : $bloodGroupType.hashCode());
        String $bloodGroup = this.getBloodGroup();
        result = result * 59 + ($bloodGroup == null ? 43 : $bloodGroup.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "BloodGroup(id=" + this.getId() + ", schoolId=" + this.getSchoolId() + ", bloodGroupName=" + this.getBloodGroupName() + ", bloodGroupType=" + this.getBloodGroupType() + ", bloodGroup=" + this.getBloodGroup() + ", universal=" + this.isUniversal() + ")";
    }

    @Generated
    public BloodGroup() {
    }
}

