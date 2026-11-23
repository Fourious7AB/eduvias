/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.StudentAdmission.model.Subjects
 *  lombok.Generated
 */
package com.neel.StudentAdmission.model;

import lombok.Generated;

public class Subjects {
    private String subjectId;
    private String subjectName;
    private String subject_index;

    @Generated
    public Subjects() {
    }

    @Generated
    public String getSubjectId() {
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
    public void setSubjectId(String subjectId) {
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
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Subjects)) {
            return false;
        }
        Subjects other = (Subjects)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        String this$subjectId = this.getSubjectId();
        String other$subjectId = other.getSubjectId();
        if (this$subjectId == null ? other$subjectId != null : !this$subjectId.equals(other$subjectId)) {
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
        return other instanceof Subjects;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $subjectId = this.getSubjectId();
        result = result * 59 + ($subjectId == null ? 43 : $subjectId.hashCode());
        String $subjectName = this.getSubjectName();
        result = result * 59 + ($subjectName == null ? 43 : $subjectName.hashCode());
        String $subject_index = this.getSubject_index();
        result = result * 59 + ($subject_index == null ? 43 : $subject_index.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "Subjects(subjectId=" + this.getSubjectId() + ", subjectName=" + this.getSubjectName() + ", subject_index=" + this.getSubject_index() + ")";
    }
}

