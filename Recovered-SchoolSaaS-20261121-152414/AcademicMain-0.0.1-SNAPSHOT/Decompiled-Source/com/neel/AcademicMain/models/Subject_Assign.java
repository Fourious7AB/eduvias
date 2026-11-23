/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.AcademicMain.models.Subject_Assign
 *  lombok.Generated
 *  org.springframework.data.annotation.Id
 *  org.springframework.data.mongodb.core.mapping.Document
 */
package com.neel.AcademicMain.models;

import lombok.Generated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="subject_assign")
public class Subject_Assign {
    @Id
    private String id;
    private String schoolId;
    private int subjectAssignId;
    private String subjectId;
    private String classId;
    private String subjectCode;

    @Generated
    public Subject_Assign() {
    }

    @Generated
    public String getId() {
        return this.id;
    }

    @Generated
    public String getSchoolId() {
        return this.schoolId;
    }

    @Generated
    public int getSubjectAssignId() {
        return this.subjectAssignId;
    }

    @Generated
    public String getSubjectId() {
        return this.subjectId;
    }

    @Generated
    public String getClassId() {
        return this.classId;
    }

    @Generated
    public String getSubjectCode() {
        return this.subjectCode;
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
    public void setSubjectAssignId(int subjectAssignId) {
        this.subjectAssignId = subjectAssignId;
    }

    @Generated
    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    @Generated
    public void setClassId(String classId) {
        this.classId = classId;
    }

    @Generated
    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Subject_Assign)) {
            return false;
        }
        Subject_Assign other = (Subject_Assign)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        if (this.getSubjectAssignId() != other.getSubjectAssignId()) {
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
        String this$subjectId = this.getSubjectId();
        String other$subjectId = other.getSubjectId();
        if (this$subjectId == null ? other$subjectId != null : !this$subjectId.equals(other$subjectId)) {
            return false;
        }
        String this$classId = this.getClassId();
        String other$classId = other.getClassId();
        if (this$classId == null ? other$classId != null : !this$classId.equals(other$classId)) {
            return false;
        }
        String this$subjectCode = this.getSubjectCode();
        String other$subjectCode = other.getSubjectCode();
        return !(this$subjectCode == null ? other$subjectCode != null : !this$subjectCode.equals(other$subjectCode));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof Subject_Assign;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSubjectAssignId();
        String $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        String $schoolId = this.getSchoolId();
        result = result * 59 + ($schoolId == null ? 43 : $schoolId.hashCode());
        String $subjectId = this.getSubjectId();
        result = result * 59 + ($subjectId == null ? 43 : $subjectId.hashCode());
        String $classId = this.getClassId();
        result = result * 59 + ($classId == null ? 43 : $classId.hashCode());
        String $subjectCode = this.getSubjectCode();
        result = result * 59 + ($subjectCode == null ? 43 : $subjectCode.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "Subject_Assign(id=" + this.getId() + ", schoolId=" + this.getSchoolId() + ", subjectAssignId=" + this.getSubjectAssignId() + ", subjectId=" + this.getSubjectId() + ", classId=" + this.getClassId() + ", subjectCode=" + this.getSubjectCode() + ")";
    }
}

