package com.result.Result.model;

import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "additional_marks_register")
public class AdditionalMarksRegister {
    @Id
    private String id;
    private String schoolId;
    private String className;
    private String section;
    private String examType;
    private String subject;
    private double subjectFullMark;
    private double projectFullMark;
    private double fullMarks;
    private String teacherName;
    private String userRole;
    private List<MarksRegister.StudentMark> marks;

    public AdditionalMarksRegister() {}

    public String getId() { return this.id; }
    public String getSchoolId() { return this.schoolId; }
    public String getClassName() { return this.className; }
    public String getSection() { return this.section; }
    public String getExamType() { return this.examType; }
    public String getSubject() { return this.subject; }
    public double getSubjectFullMark() { return this.subjectFullMark; }
    public double getProjectFullMark() { return this.projectFullMark; }
    public double getFullMarks() { return this.fullMarks; }
    public String getTeacherName() { return this.teacherName; }
    public String getUserRole() { return this.userRole; }
    public List<MarksRegister.StudentMark> getMarks() { return this.marks; }

    public void setId(String id) { this.id = id; }
    public void setSchoolId(String schoolId) { this.schoolId = schoolId; }
    public void setClassName(String className) { this.className = className; }
    public void setSection(String section) { this.section = section; }
    public void setExamType(String examType) { this.examType = examType; }
    public void setSubject(String subject) { this.subject = subject; }
    public void setSubjectFullMark(double subjectFullMark) { this.subjectFullMark = subjectFullMark; }
    public void setProjectFullMark(double projectFullMark) { this.projectFullMark = projectFullMark; }
    public void setFullMarks(double fullMarks) { this.fullMarks = fullMarks; }
    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }
    public void setUserRole(String userRole) { this.userRole = userRole; }
    public void setMarks(List<MarksRegister.StudentMark> marks) { this.marks = marks; }
}
