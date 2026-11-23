/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.AcademicMain.models.ClassRoom
 *  lombok.Generated
 *  org.springframework.data.mongodb.core.mapping.Document
 */
package com.neel.AcademicMain.models;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="classroom")
public class ClassRoom {
    private String id;
    private int classRoomId;
    private String schoolId;
    private int roomNo;
    private String classId;
    private String sectionId;

    public String getId() {
        return this.id;
    }

    public int getClassRoomId() {
        return this.classRoomId;
    }

    public String getSchoolId() {
        return this.schoolId;
    }

    public int getRoomNo() {
        return this.roomNo;
    }

    public String getClassId() {
        return this.classId;
    }

    public String getSectionId() {
        return this.sectionId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setClassRoomId(int classRoomId) {
        this.classRoomId = classRoomId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public void setRoomNo(int roomNo) {
        this.roomNo = roomNo;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ClassRoom)) {
            return false;
        }
        ClassRoom other = (ClassRoom)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        if (this.getClassRoomId() != other.getClassRoomId()) {
            return false;
        }
        if (this.getRoomNo() != other.getRoomNo()) {
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
        String this$classId = this.getClassId();
        String other$classId = other.getClassId();
        if (this$classId == null ? other$classId != null : !this$classId.equals(other$classId)) {
            return false;
        }
        String this$sectionId = this.getSectionId();
        String other$sectionId = other.getSectionId();
        return !(this$sectionId == null ? other$sectionId != null : !this$sectionId.equals(other$sectionId));
    }

    protected boolean canEqual(Object other) {
        return other instanceof ClassRoom;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getClassRoomId();
        result = result * 59 + this.getRoomNo();
        String $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        String $schoolId = this.getSchoolId();
        result = result * 59 + ($schoolId == null ? 43 : $schoolId.hashCode());
        String $classId = this.getClassId();
        result = result * 59 + ($classId == null ? 43 : $classId.hashCode());
        String $sectionId = this.getSectionId();
        result = result * 59 + ($sectionId == null ? 43 : $sectionId.hashCode());
        return result;
    }

    public String toString() {
        return "ClassRoom(id=" + this.getId() + ", classRoomId=" + this.getClassRoomId() + ", schoolId=" + this.getSchoolId() + ", roomNo=" + this.getRoomNo() + ", classId=" + this.getClassId() + ", sectionId=" + this.getSectionId() + ")";
    }

    public ClassRoom() {
    }
}

