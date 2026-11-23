/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.result.Result.model.Criteria
 *  com.result.Result.model.Criteria$Subject
 *  lombok.Generated
 *  org.springframework.data.mongodb.core.mapping.Document
 */
package com.result.Result.model;

import com.result.Result.model.Criteria;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="exam-criteria")
public class Criteria {
    private String id;
    private String schoolId;
    private String className;
    private String section;
    private String examType;
    private List<Subject> subjects = new ArrayList();
    private Double passingMark;

    @Generated
    public String getId() {
        return this.id;
    }

    @Generated
    public String getSchoolId() {
        return this.schoolId;
    }

    @Generated
    public String getClassName() {
        return this.className;
    }

    @Generated
    public String getSection() {
        return this.section;
    }

    @Generated
    public String getExamType() {
        return this.examType;
    }

    @Generated
    public List<Subject> getSubjects() {
        return this.subjects;
    }

    @Generated
    public Double getPassingMark() {
        return this.passingMark;
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
    public void setClassName(String className) {
        this.className = className;
    }

    @Generated
    public void setSection(String section) {
        this.section = section;
    }

    @Generated
    public void setExamType(String examType) {
        this.examType = examType;
    }

    @Generated
    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    @Generated
    public void setPassingMark(Double passingMark) {
        this.passingMark = passingMark;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Criteria)) {
            return false;
        }
        Criteria other = (Criteria)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Double this$passingMark = this.getPassingMark();
        Double other$passingMark = other.getPassingMark();
        if (this$passingMark == null ? other$passingMark != null : !((Object)this$passingMark).equals(other$passingMark)) {
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
        String this$className = this.getClassName();
        String other$className = other.getClassName();
        if (this$className == null ? other$className != null : !this$className.equals(other$className)) {
            return false;
        }
        String this$section = this.getSection();
        String other$section = other.getSection();
        if (this$section == null ? other$section != null : !this$section.equals(other$section)) {
            return false;
        }
        String this$examType = this.getExamType();
        String other$examType = other.getExamType();
        if (this$examType == null ? other$examType != null : !this$examType.equals(other$examType)) {
            return false;
        }
        List this$subjects = this.getSubjects();
        List other$subjects = other.getSubjects();
        return !(this$subjects == null ? other$subjects != null : !((Object)this$subjects).equals(other$subjects));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof Criteria;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Double $passingMark = this.getPassingMark();
        result = result * 59 + ($passingMark == null ? 43 : ((Object)$passingMark).hashCode());
        String $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        String $schoolId = this.getSchoolId();
        result = result * 59 + ($schoolId == null ? 43 : $schoolId.hashCode());
        String $className = this.getClassName();
        result = result * 59 + ($className == null ? 43 : $className.hashCode());
        String $section = this.getSection();
        result = result * 59 + ($section == null ? 43 : $section.hashCode());
        String $examType = this.getExamType();
        result = result * 59 + ($examType == null ? 43 : $examType.hashCode());
        List $subjects = this.getSubjects();
        result = result * 59 + ($subjects == null ? 43 : ((Object)$subjects).hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "Criteria(id=" + this.getId() + ", schoolId=" + this.getSchoolId() + ", className=" + this.getClassName() + ", section=" + this.getSection() + ", examType=" + this.getExamType() + ", subjects=" + this.getSubjects() + ", passingMark=" + this.getPassingMark() + ")";
    }

    @Generated
    public Criteria() {
    }

    @Generated
    public Criteria(String id, String schoolId, String className, String section, String examType, List<Subject> subjects, Double passingMark) {
        this.id = id;
        this.schoolId = schoolId;
        this.className = className;
        this.section = section;
        this.examType = examType;
        this.subjects = subjects;
        this.passingMark = passingMark;
    }

    public static class Subject {
        private String subjectName;
        private Double subjectMark;
        private Double projectMark;
        private Double fullMark;
        private java.time.LocalDate createAt;

        public Subject() {}
        public Double getFullMark() { return this.fullMark; }
        public String getSubjectName() { return this.subjectName; }
        public Double getSubjectMark() { return this.subjectMark; }
        public Double getProjectMark() { return this.projectMark; }
        public java.time.LocalDate getCreateAt() { return this.createAt; }
        public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
        public void setSubjectMark(Double subjectMark) { this.subjectMark = subjectMark; }
        public void setProjectMark(Double projectMark) { this.projectMark = projectMark; }
        public void setFullMark(Double fullMark) { this.fullMark = fullMark; }
        public void setCreateAt(java.time.LocalDate createAt) { this.createAt = createAt; }
    }
}
