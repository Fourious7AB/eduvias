/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.result.Result.model.ProjectMarks
 *  lombok.Generated
 *  org.springframework.data.mongodb.core.mapping.Document
 */
package com.result.Result.model;

import lombok.Generated;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="project-mark")
public class ProjectMarks {
    private String id;
    private String subjectType;
    private String projectName;
    private Double projectMarks;
    private Double fullMarks;
    private String schoolId;

    @Generated
    public String getId() {
        return this.id;
    }

    @Generated
    public String getSubjectType() {
        return this.subjectType;
    }

    @Generated
    public String getProjectName() {
        return this.projectName;
    }

    @Generated
    public Double getProjectMarks() {
        return this.projectMarks;
    }

    @Generated
    public Double getFullMarks() {
        return this.fullMarks;
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
    public void setSubjectType(String subjectType) {
        this.subjectType = subjectType;
    }

    @Generated
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Generated
    public void setProjectMarks(Double projectMarks) {
        this.projectMarks = projectMarks;
    }

    @Generated
    public void setFullMarks(Double fullMarks) {
        this.fullMarks = fullMarks;
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
        if (!(o instanceof ProjectMarks)) {
            return false;
        }
        ProjectMarks other = (ProjectMarks)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Double this$projectMarks = this.getProjectMarks();
        Double other$projectMarks = other.getProjectMarks();
        if (this$projectMarks == null ? other$projectMarks != null : !((Object)this$projectMarks).equals(other$projectMarks)) {
            return false;
        }
        Double this$fullMarks = this.getFullMarks();
        Double other$fullMarks = other.getFullMarks();
        if (this$fullMarks == null ? other$fullMarks != null : !((Object)this$fullMarks).equals(other$fullMarks)) {
            return false;
        }
        String this$id = this.getId();
        String other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) {
            return false;
        }
        String this$subjectType = this.getSubjectType();
        String other$subjectType = other.getSubjectType();
        if (this$subjectType == null ? other$subjectType != null : !this$subjectType.equals(other$subjectType)) {
            return false;
        }
        String this$projectName = this.getProjectName();
        String other$projectName = other.getProjectName();
        if (this$projectName == null ? other$projectName != null : !this$projectName.equals(other$projectName)) {
            return false;
        }
        String this$schoolId = this.getSchoolId();
        String other$schoolId = other.getSchoolId();
        return !(this$schoolId == null ? other$schoolId != null : !this$schoolId.equals(other$schoolId));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof ProjectMarks;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Double $projectMarks = this.getProjectMarks();
        result = result * 59 + ($projectMarks == null ? 43 : ((Object)$projectMarks).hashCode());
        Double $fullMarks = this.getFullMarks();
        result = result * 59 + ($fullMarks == null ? 43 : ((Object)$fullMarks).hashCode());
        String $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        String $subjectType = this.getSubjectType();
        result = result * 59 + ($subjectType == null ? 43 : $subjectType.hashCode());
        String $projectName = this.getProjectName();
        result = result * 59 + ($projectName == null ? 43 : $projectName.hashCode());
        String $schoolId = this.getSchoolId();
        result = result * 59 + ($schoolId == null ? 43 : $schoolId.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "ProjectMarks(id=" + this.getId() + ", subjectType=" + this.getSubjectType() + ", projectName=" + this.getProjectName() + ", projectMarks=" + this.getProjectMarks() + ", fullMarks=" + this.getFullMarks() + ", schoolId=" + this.getSchoolId() + ")";
    }

    @Generated
    public ProjectMarks() {
    }

    @Generated
    public ProjectMarks(String id, String subjectType, String projectName, Double projectMarks, Double fullMarks, String schoolId) {
        this.id = id;
        this.subjectType = subjectType;
        this.projectName = projectName;
        this.projectMarks = projectMarks;
        this.fullMarks = fullMarks;
        this.schoolId = schoolId;
    }
}

