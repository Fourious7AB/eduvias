/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.AcademicMain.models.Subject
 *  lombok.Generated
 *  org.springframework.data.annotation.Id
 *  org.springframework.data.mongodb.core.mapping.Document
 */
package com.neel.AcademicMain.models;

import lombok.Generated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="subject")
public class Subject {
    @Id
    private String id;
    private String schoolId;
    private int subjectId;
    private String subjectName;
    private String subject_index;
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
    public int getSubjectId() {
        return this.subjectId;
    }

    @Generated
    public String getSubjectName() {
        return this.subjectName;
    }

    @Generated
    public String getSubject_index() {
        return this.subject_index;
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
    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    @Generated
    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    @Generated
    public void setSubject_index(String subject_index) {
        this.subject_index = subject_index;
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
        if (!(o instanceof Subject)) {
            return false;
        }
        Subject other = (Subject)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        if (this.getSubjectId() != other.getSubjectId()) {
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
        String this$subjectName = this.getSubjectName();
        String other$subjectName = other.getSubjectName();
        if (this$subjectName == null ? other$subjectName != null : !this$subjectName.equals(other$subjectName)) {
            return false;
        }
        String this$subject_index = this.getSubject_index();
        String other$subject_index = other.getSubject_index();
        return !(this$subject_index == null ? other$subject_index != null : !this$subject_index.equals(other$subject_index));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof Subject;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSubjectId();
        result = result * 59 + (this.isUniversal() ? 79 : 97);
        String $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        String $schoolId = this.getSchoolId();
        result = result * 59 + ($schoolId == null ? 43 : $schoolId.hashCode());
        String $subjectName = this.getSubjectName();
        result = result * 59 + ($subjectName == null ? 43 : $subjectName.hashCode());
        String $subject_index = this.getSubject_index();
        result = result * 59 + ($subject_index == null ? 43 : $subject_index.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "Subject(id=" + this.getId() + ", schoolId=" + this.getSchoolId() + ", subjectId=" + this.getSubjectId() + ", subjectName=" + this.getSubjectName() + ", subject_index=" + this.getSubject_index() + ", universal=" + this.isUniversal() + ")";
    }

    @Generated
    public Subject() {
    }
}

