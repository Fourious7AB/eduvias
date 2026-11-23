/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.AcademicMain.models.Section
 *  lombok.Generated
 *  org.springframework.data.annotation.Id
 *  org.springframework.data.mongodb.core.mapping.Document
 */
package com.neel.AcademicMain.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="sections")
public class Section {
    @Id
    private String id;
    private String schoolId;
    private int sectionId;
    private String sectionName;
    private String section_Code;
    private boolean universal;

    public Section() {
    }

    public String getId() {
        return this.id;
    }

    public String getSchoolId() {
        return this.schoolId;
    }

    public int getSectionId() {
        return this.sectionId;
    }

    public String getSectionName() {
        return this.sectionName;
    }

    public String getSection_Code() {
        return this.section_Code;
    }

    public boolean isUniversal() {
        return this.universal;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public void setSection_Code(String section_Code) {
        this.section_Code = section_Code;
    }

    public void setUniversal(boolean universal) {
        this.universal = universal;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Section)) {
            return false;
        }
        Section other = (Section)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        if (this.getSectionId() != other.getSectionId()) {
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
        String this$sectionName = this.getSectionName();
        String other$sectionName = other.getSectionName();
        if (this$sectionName == null ? other$sectionName != null : !this$sectionName.equals(other$sectionName)) {
            return false;
        }
        String this$section_Code = this.getSection_Code();
        String other$section_Code = other.getSection_Code();
        return !(this$section_Code == null ? other$section_Code != null : !this$section_Code.equals(other$section_Code));
    }

    protected boolean canEqual(Object other) {
        return other instanceof Section;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSectionId();
        result = result * 59 + (this.isUniversal() ? 79 : 97);
        String $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        String $schoolId = this.getSchoolId();
        result = result * 59 + ($schoolId == null ? 43 : $schoolId.hashCode());
        String $sectionName = this.getSectionName();
        result = result * 59 + ($sectionName == null ? 43 : $sectionName.hashCode());
        String $section_Code = this.getSection_Code();
        result = result * 59 + ($section_Code == null ? 43 : $section_Code.hashCode());
        return result;
    }

    public String toString() {
        return "Section(id=" + this.getId() + ", schoolId=" + this.getSchoolId() + ", sectionId=" + this.getSectionId() + ", sectionName=" + this.getSectionName() + ", section_Code=" + this.getSection_Code() + ", universal=" + this.isUniversal() + ")";
    }
}

