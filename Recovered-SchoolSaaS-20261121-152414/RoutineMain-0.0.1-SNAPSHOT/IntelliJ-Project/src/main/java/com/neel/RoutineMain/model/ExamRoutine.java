/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.RoutineMain.model.ExamRoutine
 *  com.neel.RoutineMain.model.ExamRoutine$SubjectExam
 *  lombok.Generated
 *  org.springframework.data.annotation.Id
 *  org.springframework.data.mongodb.core.mapping.Document
 */
package com.neel.RoutineMain.model;

import com.neel.RoutineMain.model.ExamRoutine;
import java.util.List;
import lombok.Generated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="exam_routines")
public class ExamRoutine {
    @Id
    private String id;
    private String schoolId;
    private String className;
    private String examType;
    private List<SubjectExam> subjects;

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
    public String getExamType() {
        return this.examType;
    }

    @Generated
    public List<SubjectExam> getSubjects() {
        return this.subjects;
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
    public void setExamType(String examType) {
        this.examType = examType;
    }

    @Generated
    public void setSubjects(List<SubjectExam> subjects) {
        this.subjects = subjects;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ExamRoutine)) {
            return false;
        }
        ExamRoutine other = (ExamRoutine)o;
        if (!other.canEqual((Object)this)) {
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
        return other instanceof ExamRoutine;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        String $schoolId = this.getSchoolId();
        result = result * 59 + ($schoolId == null ? 43 : $schoolId.hashCode());
        String $className = this.getClassName();
        result = result * 59 + ($className == null ? 43 : $className.hashCode());
        String $examType = this.getExamType();
        result = result * 59 + ($examType == null ? 43 : $examType.hashCode());
        List $subjects = this.getSubjects();
        result = result * 59 + ($subjects == null ? 43 : ((Object)$subjects).hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "ExamRoutine(id=" + this.getId() + ", schoolId=" + this.getSchoolId() + ", className=" + this.getClassName() + ", examType=" + this.getExamType() + ", subjects=" + this.getSubjects() + ")";
    }

    @Generated
    public ExamRoutine() {
    }

    @Generated
    public ExamRoutine(String id, String schoolId, String className, String examType, List<SubjectExam> subjects) {
        this.id = id;
        this.schoolId = schoolId;
        this.className = className;
        this.examType = examType;
        this.subjects = subjects;
    }

    public static class SubjectExam {
        private String subjectName;
        private String examDate;
        private String startTime;
        private String endTime;

        public SubjectExam() {}
        public SubjectExam(String subjectName, String examDate, String startTime, String endTime) {
            this.subjectName = subjectName;
            this.examDate = examDate;
            this.startTime = startTime;
            this.endTime = endTime;
        }
        public String getSubjectName() { return this.subjectName; }
        public String getExamDate() { return this.examDate; }
        public String getStartTime() { return this.startTime; }
        public String getEndTime() { return this.endTime; }
        public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
        public void setExamDate(String examDate) { this.examDate = examDate; }
        public void setStartTime(String startTime) { this.startTime = startTime; }
        public void setEndTime(String endTime) { this.endTime = endTime; }
    }
}
