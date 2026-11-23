/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.Settings.model.Gender
 *  lombok.Generated
 *  org.springframework.data.annotation.Id
 *  org.springframework.data.mongodb.core.mapping.Document
 */
package com.neel.Settings.model;

import lombok.Generated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="gender")
public class Gender {
    @Id
    private String id;
    private String schoolId;
    private String genderName;
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
    public String getGenderName() {
        return this.genderName;
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
    public void setGenderName(String genderName) {
        this.genderName = genderName;
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
        if (!(o instanceof Gender)) {
            return false;
        }
        Gender other = (Gender)o;
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
        String this$genderName = this.getGenderName();
        String other$genderName = other.getGenderName();
        return !(this$genderName == null ? other$genderName != null : !this$genderName.equals(other$genderName));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof Gender;
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
        String $genderName = this.getGenderName();
        result = result * 59 + ($genderName == null ? 43 : $genderName.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "Gender(id=" + this.getId() + ", schoolId=" + this.getSchoolId() + ", genderName=" + this.getGenderName() + ", universal=" + this.isUniversal() + ")";
    }

    @Generated
    public Gender() {
    }
}

