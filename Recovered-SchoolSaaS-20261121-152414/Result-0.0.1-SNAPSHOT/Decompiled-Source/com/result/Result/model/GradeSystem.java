/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.result.Result.model.GradeSystem
 *  lombok.Generated
 *  org.springframework.data.annotation.Id
 *  org.springframework.data.mongodb.core.mapping.Document
 */
package com.result.Result.model;

import lombok.Generated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="grades")
public class GradeSystem {
    @Id
    private String id;
    private String schoolId;
    private int scaleStartMarks;
    private int scaleEndMarks;
    private String letterGrade;
    private String performanceIndicator;

    @Generated
    public GradeSystem() {
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
    public int getScaleStartMarks() {
        return this.scaleStartMarks;
    }

    @Generated
    public int getScaleEndMarks() {
        return this.scaleEndMarks;
    }

    @Generated
    public String getLetterGrade() {
        return this.letterGrade;
    }

    @Generated
    public String getPerformanceIndicator() {
        return this.performanceIndicator;
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
    public void setScaleStartMarks(int scaleStartMarks) {
        this.scaleStartMarks = scaleStartMarks;
    }

    @Generated
    public void setScaleEndMarks(int scaleEndMarks) {
        this.scaleEndMarks = scaleEndMarks;
    }

    @Generated
    public void setLetterGrade(String letterGrade) {
        this.letterGrade = letterGrade;
    }

    @Generated
    public void setPerformanceIndicator(String performanceIndicator) {
        this.performanceIndicator = performanceIndicator;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof GradeSystem)) {
            return false;
        }
        GradeSystem other = (GradeSystem)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        if (this.getScaleStartMarks() != other.getScaleStartMarks()) {
            return false;
        }
        if (this.getScaleEndMarks() != other.getScaleEndMarks()) {
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
        String this$letterGrade = this.getLetterGrade();
        String other$letterGrade = other.getLetterGrade();
        if (this$letterGrade == null ? other$letterGrade != null : !this$letterGrade.equals(other$letterGrade)) {
            return false;
        }
        String this$performanceIndicator = this.getPerformanceIndicator();
        String other$performanceIndicator = other.getPerformanceIndicator();
        return !(this$performanceIndicator == null ? other$performanceIndicator != null : !this$performanceIndicator.equals(other$performanceIndicator));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof GradeSystem;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getScaleStartMarks();
        result = result * 59 + this.getScaleEndMarks();
        String $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        String $schoolId = this.getSchoolId();
        result = result * 59 + ($schoolId == null ? 43 : $schoolId.hashCode());
        String $letterGrade = this.getLetterGrade();
        result = result * 59 + ($letterGrade == null ? 43 : $letterGrade.hashCode());
        String $performanceIndicator = this.getPerformanceIndicator();
        result = result * 59 + ($performanceIndicator == null ? 43 : $performanceIndicator.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "GradeSystem(id=" + this.getId() + ", schoolId=" + this.getSchoolId() + ", scaleStartMarks=" + this.getScaleStartMarks() + ", scaleEndMarks=" + this.getScaleEndMarks() + ", letterGrade=" + this.getLetterGrade() + ", performanceIndicator=" + this.getPerformanceIndicator() + ")";
    }
}

