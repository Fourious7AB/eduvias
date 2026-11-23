/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.RoutineMain.model.Routine
 *  com.neel.RoutineMain.model.Routine$Period
 *  lombok.Generated
 *  org.springframework.data.annotation.Id
 *  org.springframework.data.mongodb.core.mapping.Document
 */
package com.neel.RoutineMain.model;

import com.neel.RoutineMain.model.Routine;
import java.util.List;
import lombok.Generated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="routine")
public class Routine {
    @Id
    private String id;
    private String schoolId;
    private String className;
    private String section;
    private String day;
    private List<Period> periods;

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
    public String getDay() {
        return this.day;
    }

    @Generated
    public List<Period> getPeriods() {
        return this.periods;
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
    public void setDay(String day) {
        this.day = day;
    }

    @Generated
    public void setPeriods(List<Period> periods) {
        this.periods = periods;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Routine)) {
            return false;
        }
        Routine other = (Routine)o;
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
        String this$section = this.getSection();
        String other$section = other.getSection();
        if (this$section == null ? other$section != null : !this$section.equals(other$section)) {
            return false;
        }
        String this$day = this.getDay();
        String other$day = other.getDay();
        if (this$day == null ? other$day != null : !this$day.equals(other$day)) {
            return false;
        }
        List this$periods = this.getPeriods();
        List other$periods = other.getPeriods();
        return !(this$periods == null ? other$periods != null : !((Object)this$periods).equals(other$periods));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof Routine;
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
        String $section = this.getSection();
        result = result * 59 + ($section == null ? 43 : $section.hashCode());
        String $day = this.getDay();
        result = result * 59 + ($day == null ? 43 : $day.hashCode());
        List $periods = this.getPeriods();
        result = result * 59 + ($periods == null ? 43 : ((Object)$periods).hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "Routine(id=" + this.getId() + ", schoolId=" + this.getSchoolId() + ", className=" + this.getClassName() + ", section=" + this.getSection() + ", day=" + this.getDay() + ", periods=" + this.getPeriods() + ")";
    }

    @Generated
    public Routine() {
    }

    @Generated
    public Routine(String id, String schoolId, String className, String section, String day, List<Period> periods) {
        this.id = id;
        this.schoolId = schoolId;
        this.className = className;
        this.section = section;
        this.day = day;
        this.periods = periods;
    }
}

