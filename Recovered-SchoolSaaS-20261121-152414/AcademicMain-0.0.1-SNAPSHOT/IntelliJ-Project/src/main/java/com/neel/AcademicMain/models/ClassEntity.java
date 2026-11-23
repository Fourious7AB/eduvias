/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.AcademicMain.models.ClassEntity
 *  lombok.Generated
 *  org.springframework.data.annotation.Id
 *  org.springframework.data.mongodb.core.mapping.Document
 */
package com.neel.AcademicMain.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="classes")
public class ClassEntity {
    @Id
    private String id;
    private String schoolId;
    private int classId;
    private String className;
    private String class_Code;
    private boolean universal;

    public String getId() {
        return this.id;
    }

    public String getSchoolId() {
        return this.schoolId;
    }

    public int getClassId() {
        return this.classId;
    }

    public String getClassName() {
        return this.className;
    }

    public String getClass_Code() {
        return this.class_Code;
    }

    public boolean isUniversal() {
        return this.universal;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setClass_Code(String class_Code) {
        this.class_Code = class_Code;
    }

    public void setUniversal(boolean universal) {
        this.universal = universal;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ClassEntity)) {
            return false;
        }
        ClassEntity other = (ClassEntity)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        if (this.getClassId() != other.getClassId()) {
            return false;
        }
        if (this.isUniversal() != other.isUniversal()) {
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
        String this$class_Code = this.getClass_Code();
        String other$class_Code = other.getClass_Code();
        return !(this$class_Code == null ? other$class_Code != null : !this$class_Code.equals(other$class_Code));
    }

    protected boolean canEqual(Object other) {
        return other instanceof ClassEntity;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getClassId();
        result = result * 59 + (this.isUniversal() ? 79 : 97);
        String $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        String $schoolId = this.getSchoolId();
        result = result * 59 + ($schoolId == null ? 43 : $schoolId.hashCode());
        String $className = this.getClassName();
        result = result * 59 + ($className == null ? 43 : $className.hashCode());
        String $class_Code = this.getClass_Code();
        result = result * 59 + ($class_Code == null ? 43 : $class_Code.hashCode());
        return result;
    }

    public String toString() {
        return "ClassEntity(id=" + this.getId() + ", schoolId=" + this.getSchoolId() + ", classId=" + this.getClassId() + ", className=" + this.getClassName() + ", class_Code=" + this.getClass_Code() + ", universal=" + this.isUniversal() + ")";
    }

    public ClassEntity() {
    }
}

