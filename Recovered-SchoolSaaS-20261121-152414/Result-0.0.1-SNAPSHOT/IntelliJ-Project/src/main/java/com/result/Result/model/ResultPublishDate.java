package com.result.Result.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "result_publish_dates")
public class ResultPublishDate {
    @Id
    private String id;
    private String schoolId;
    private String className;
    private String examType;
    private String resultPublishDate;

    public ResultPublishDate() {}

    public ResultPublishDate(String id, String schoolId, String className, String examType, String resultPublishDate) {
        this.id = id;
        this.schoolId = schoolId;
        this.className = className;
        this.examType = examType;
        this.resultPublishDate = resultPublishDate;
    }

    public String getId() { return this.id; }
    public String getSchoolId() { return this.schoolId; }
    public String getClassName() { return this.className; }
    public String getExamType() { return this.examType; }
    public String getResultPublishDate() { return this.resultPublishDate; }

    public void setId(String id) { this.id = id; }
    public void setSchoolId(String schoolId) { this.schoolId = schoolId; }
    public void setClassName(String className) { this.className = className; }
    public void setExamType(String examType) { this.examType = examType; }
    public void setResultPublishDate(String resultPublishDate) { this.resultPublishDate = resultPublishDate; }
}
