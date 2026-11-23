/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.RoutineMain.model.ExamTypePage
 *  lombok.Generated
 *  org.springframework.data.mongodb.core.mapping.Document
 */
package com.neel.RoutineMain.model;

import lombok.Generated;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="exam_type_page")
public class ExamTypePage {
    private String id;
    private String examTypeName;
    private String examMarks;
    private String schoolId;

    @Generated
    public String getId() {
        return this.id;
    }

    @Generated
    public String getExamTypeName() {
        return this.examTypeName;
    }

    @Generated
    public String getExamMarks() {
        return this.examMarks;
    }

    @Generated
    public String getSchoolId() {
        return this.schoolId;
    }

    @Generated
    public void setId(String id) {
        this.id = id;
    }

    @Generated
    public void setExamTypeName(String examTypeName) {
        this.examTypeName = examTypeName;
    }

    @Generated
    public void setExamMarks(String examMarks) {
        this.examMarks = examMarks;
    }

    @Generated
    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ExamTypePage)) {
            return false;
        }
        ExamTypePage other = (ExamTypePage)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        String this$id = this.getId();
        String other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) {
            return false;
        }
        String this$examTypeName = this.getExamTypeName();
        String other$examTypeName = other.getExamTypeName();
        if (this$examTypeName == null ? other$examTypeName != null : !this$examTypeName.equals(other$examTypeName)) {
            return false;
        }
        String this$examMarks = this.getExamMarks();
        String other$examMarks = other.getExamMarks();
        if (this$examMarks == null ? other$examMarks != null : !this$examMarks.equals(other$examMarks)) {
            return false;
        }
        String this$schoolId = this.getSchoolId();
        String other$schoolId = other.getSchoolId();
        return !(this$schoolId == null ? other$schoolId != null : !this$schoolId.equals(other$schoolId));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof ExamTypePage;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        String $examTypeName = this.getExamTypeName();
        result = result * 59 + ($examTypeName == null ? 43 : $examTypeName.hashCode());
        String $examMarks = this.getExamMarks();
        result = result * 59 + ($examMarks == null ? 43 : $examMarks.hashCode());
        String $schoolId = this.getSchoolId();
        result = result * 59 + ($schoolId == null ? 43 : $schoolId.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "ExamTypePage(id=" + this.getId() + ", examTypeName=" + this.getExamTypeName() + ", examMarks=" + this.getExamMarks() + ", schoolId=" + this.getSchoolId() + ")";
    }

    @Generated
    public ExamTypePage() {
    }

    @Generated
    public ExamTypePage(String id, String examTypeName, String examMarks, String schoolId) {
        this.id = id;
        this.examTypeName = examTypeName;
        this.examMarks = examMarks;
        this.schoolId = schoolId;
    }
}

