/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.RoutineMain.model.NotificationResponse
 *  lombok.Generated
 */
package com.neel.RoutineMain.model;

import java.time.LocalDateTime;
import lombok.Generated;

public class NotificationResponse {
    private String id;
    private String eventName;
    private String description;
    private String eventDate;
    private LocalDateTime createdAt;
    private boolean newEvent;

    @Generated
    public String getId() {
        return this.id;
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
    public boolean isNewEvent() {
        return this.newEvent;
    }

    @Generated
    public void setId(String id) {
        this.id = id;
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
    public void setNewEvent(boolean newEvent) {
        this.newEvent = newEvent;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof NotificationResponse)) {
            return false;
        }
        NotificationResponse other = (NotificationResponse)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        if (this.isNewEvent() != other.isNewEvent()) {
            return false;
        }
        String this$id = this.getId();
        String other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) {
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
        return other instanceof NotificationResponse;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + (this.isNewEvent() ? 79 : 97);
        String $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
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
        return "NotificationResponse(id=" + this.getId() + ", eventName=" + this.getEventName() + ", description=" + this.getDescription() + ", eventDate=" + this.getEventDate() + ", createdAt=" + this.getCreatedAt() + ", newEvent=" + this.isNewEvent() + ")";
    }

    @Generated
    public NotificationResponse(String id, String eventName, String description, String eventDate, LocalDateTime createdAt, boolean newEvent) {
        this.id = id;
        this.eventName = eventName;
        this.description = description;
        this.eventDate = eventDate;
        this.createdAt = createdAt;
        this.newEvent = newEvent;
    }
}

