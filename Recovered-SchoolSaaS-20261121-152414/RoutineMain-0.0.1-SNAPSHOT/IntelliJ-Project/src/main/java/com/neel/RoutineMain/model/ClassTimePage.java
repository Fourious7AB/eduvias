/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.RoutineMain.model.ClassTimePage
 *  lombok.Generated
 *  org.springframework.data.annotation.Id
 *  org.springframework.data.mongodb.core.mapping.Document
 */
package com.neel.RoutineMain.model;

import lombok.Generated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="class_time_page")
public class ClassTimePage {
    @Id
    private String id;
    private String schoolId;
    private String periods;
    private String startTime;
    private String endTime;

    @Generated
    public String getId() {
        return this.id;
    }

    @Generated
    public String getSchoolId() {
        return this.schoolId;
    }

    @Generated
    public String getPeriods() {
        return this.periods;
    }

    @Generated
    public String getStartTime() {
        return this.startTime;
    }

    @Generated
    public String getEndTime() {
        return this.endTime;
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
    public void setPeriods(String periods) {
        this.periods = periods;
    }

    @Generated
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    @Generated
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ClassTimePage)) {
            return false;
        }
        ClassTimePage other = (ClassTimePage)o;
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
        String this$periods = this.getPeriods();
        String other$periods = other.getPeriods();
        if (this$periods == null ? other$periods != null : !this$periods.equals(other$periods)) {
            return false;
        }
        String this$startTime = this.getStartTime();
        String other$startTime = other.getStartTime();
        if (this$startTime == null ? other$startTime != null : !this$startTime.equals(other$startTime)) {
            return false;
        }
        String this$endTime = this.getEndTime();
        String other$endTime = other.getEndTime();
        return !(this$endTime == null ? other$endTime != null : !this$endTime.equals(other$endTime));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof ClassTimePage;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        String $schoolId = this.getSchoolId();
        result = result * 59 + ($schoolId == null ? 43 : $schoolId.hashCode());
        String $periods = this.getPeriods();
        result = result * 59 + ($periods == null ? 43 : $periods.hashCode());
        String $startTime = this.getStartTime();
        result = result * 59 + ($startTime == null ? 43 : $startTime.hashCode());
        String $endTime = this.getEndTime();
        result = result * 59 + ($endTime == null ? 43 : $endTime.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "ClassTimePage(id=" + this.getId() + ", schoolId=" + this.getSchoolId() + ", periods=" + this.getPeriods() + ", startTime=" + this.getStartTime() + ", endTime=" + this.getEndTime() + ")";
    }

    @Generated
    public ClassTimePage() {
    }

    @Generated
    public ClassTimePage(String id, String schoolId, String periods, String startTime, String endTime) {
        this.id = id;
        this.schoolId = schoolId;
        this.periods = periods;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}

