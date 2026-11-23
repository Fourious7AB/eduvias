/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.RoutineMain.model.Notification
 *  lombok.Generated
 *  org.springframework.data.annotation.CreatedDate
 *  org.springframework.data.annotation.Id
 *  org.springframework.data.mongodb.core.mapping.Document
 */
package com.neel.RoutineMain.model;

import java.time.LocalDateTime;
import lombok.Generated;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="notifications")
public class Notification {
    @Id
    private String id;
    private String schoolId;
    private String eventName;
    private String description;
    private String eventDate;
    @CreatedDate
    private LocalDateTime createdAt;

    @Generated
    public String getId() {
        return this.id;
    }

    @Generated
    public String getSchoolId() {
        return this.schoolId;
    }

    @Generated
    public String getEventName() {
        return this.eventName;
    }

    @Generated
    public String getDescription() {
        return this.description;
    }

    @Generated
    public String getEventDate() {
        return this.eventDate;
    }

    @Generated
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
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
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    @Generated
    public void setDescription(String description) {
        this.description = description;
    }

    @Generated
    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    @Generated
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Notification)) {
            return false;
        }
        Notification other = (Notification)o;
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
        String this$eventName = this.getEventName();
        String other$eventName = other.getEventName();
        if (this$eventName == null ? other$eventName != null : !this$eventName.equals(other$eventName)) {
            return false;
        }
        String this$description = this.getDescription();
        String other$description = other.getDescription();
        if (this$description == null ? other$description != null : !this$description.equals(other$description)) {
            return false;
        }
        String this$eventDate = this.getEventDate();
        String other$eventDate = other.getEventDate();
        if (this$eventDate == null ? other$eventDate != null : !this$eventDate.equals(other$eventDate)) {
            return false;
        }
        LocalDateTime this$createdAt = this.getCreatedAt();
        LocalDateTime other$createdAt = other.getCreatedAt();
        return !(this$createdAt == null ? other$createdAt != null : !((Object)this$createdAt).equals(other$createdAt));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof Notification;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        String $schoolId = this.getSchoolId();
        result = result * 59 + ($schoolId == null ? 43 : $schoolId.hashCode());
        String $eventName = this.getEventName();
        result = result * 59 + ($eventName == null ? 43 : $eventName.hashCode());
        String $description = this.getDescription();
        result = result * 59 + ($description == null ? 43 : $description.hashCode());
        String $eventDate = this.getEventDate();
        result = result * 59 + ($eventDate == null ? 43 : $eventDate.hashCode());
        LocalDateTime $createdAt = this.getCreatedAt();
        result = result * 59 + ($createdAt == null ? 43 : ((Object)$createdAt).hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "Notification(id=" + this.getId() + ", schoolId=" + this.getSchoolId() + ", eventName=" + this.getEventName() + ", description=" + this.getDescription() + ", eventDate=" + this.getEventDate() + ", createdAt=" + this.getCreatedAt() + ")";
    }

    @Generated
    public Notification(String id, String schoolId, String eventName, String description, String eventDate, LocalDateTime createdAt) {
        this.id = id;
        this.schoolId = schoolId;
        this.eventName = eventName;
        this.description = description;
        this.eventDate = eventDate;
        this.createdAt = createdAt;
    }

    @Generated
    public Notification() {
    }
}

