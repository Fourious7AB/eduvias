/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.schoolaccount.SchoolAccount.model.AuditLog
 *  com.schoolaccount.SchoolAccount.model.AuditLog$AuditLogBuilder
 *  lombok.Generated
 *  org.springframework.data.annotation.Id
 *  org.springframework.data.mongodb.core.mapping.Document
 */
package com.schoolaccount.SchoolAccount.model;

import com.schoolaccount.SchoolAccount.model.AuditLog;
import java.time.LocalDateTime;
import lombok.Generated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="audit_logs")
public class AuditLog {
    @Id
    private String id;
    private String schoolId;
    private String entityType;
    private String entityId;
    private String action;
    private String performedBy;
    private String userRole;
    private LocalDateTime timestamp;
    private String description;
    private Object previousState;
    private Object newState;

    @Generated
    public static AuditLogBuilder builder() {
        return new AuditLogBuilder();
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
    public String getEntityType() {
        return this.entityType;
    }

    @Generated
    public String getEntityId() {
        return this.entityId;
    }

    @Generated
    public String getAction() {
        return this.action;
    }

    @Generated
    public String getPerformedBy() {
        return this.performedBy;
    }

    @Generated
    public String getUserRole() {
        return this.userRole;
    }

    @Generated
    public LocalDateTime getTimestamp() {
        return this.timestamp;
    }

    @Generated
    public String getDescription() {
        return this.description;
    }

    @Generated
    public Object getPreviousState() {
        return this.previousState;
    }

    @Generated
    public Object getNewState() {
        return this.newState;
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
    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    @Generated
    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    @Generated
    public void setAction(String action) {
        this.action = action;
    }

    @Generated
    public void setPerformedBy(String performedBy) {
        this.performedBy = performedBy;
    }

    @Generated
    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    @Generated
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Generated
    public void setDescription(String description) {
        this.description = description;
    }

    @Generated
    public void setPreviousState(Object previousState) {
        this.previousState = previousState;
    }

    @Generated
    public void setNewState(Object newState) {
        this.newState = newState;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AuditLog)) {
            return false;
        }
        AuditLog other = (AuditLog)o;
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
        String this$entityType = this.getEntityType();
        String other$entityType = other.getEntityType();
        if (this$entityType == null ? other$entityType != null : !this$entityType.equals(other$entityType)) {
            return false;
        }
        String this$entityId = this.getEntityId();
        String other$entityId = other.getEntityId();
        if (this$entityId == null ? other$entityId != null : !this$entityId.equals(other$entityId)) {
            return false;
        }
        String this$action = this.getAction();
        String other$action = other.getAction();
        if (this$action == null ? other$action != null : !this$action.equals(other$action)) {
            return false;
        }
        String this$performedBy = this.getPerformedBy();
        String other$performedBy = other.getPerformedBy();
        if (this$performedBy == null ? other$performedBy != null : !this$performedBy.equals(other$performedBy)) {
            return false;
        }
        String this$userRole = this.getUserRole();
        String other$userRole = other.getUserRole();
        if (this$userRole == null ? other$userRole != null : !this$userRole.equals(other$userRole)) {
            return false;
        }
        LocalDateTime this$timestamp = this.getTimestamp();
        LocalDateTime other$timestamp = other.getTimestamp();
        if (this$timestamp == null ? other$timestamp != null : !((Object)this$timestamp).equals(other$timestamp)) {
            return false;
        }
        String this$description = this.getDescription();
        String other$description = other.getDescription();
        if (this$description == null ? other$description != null : !this$description.equals(other$description)) {
            return false;
        }
        Object this$previousState = this.getPreviousState();
        Object other$previousState = other.getPreviousState();
        if (this$previousState == null ? other$previousState != null : !this$previousState.equals(other$previousState)) {
            return false;
        }
        Object this$newState = this.getNewState();
        Object other$newState = other.getNewState();
        return !(this$newState == null ? other$newState != null : !this$newState.equals(other$newState));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof AuditLog;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        String $schoolId = this.getSchoolId();
        result = result * 59 + ($schoolId == null ? 43 : $schoolId.hashCode());
        String $entityType = this.getEntityType();
        result = result * 59 + ($entityType == null ? 43 : $entityType.hashCode());
        String $entityId = this.getEntityId();
        result = result * 59 + ($entityId == null ? 43 : $entityId.hashCode());
        String $action = this.getAction();
        result = result * 59 + ($action == null ? 43 : $action.hashCode());
        String $performedBy = this.getPerformedBy();
        result = result * 59 + ($performedBy == null ? 43 : $performedBy.hashCode());
        String $userRole = this.getUserRole();
        result = result * 59 + ($userRole == null ? 43 : $userRole.hashCode());
        LocalDateTime $timestamp = this.getTimestamp();
        result = result * 59 + ($timestamp == null ? 43 : ((Object)$timestamp).hashCode());
        String $description = this.getDescription();
        result = result * 59 + ($description == null ? 43 : $description.hashCode());
        Object $previousState = this.getPreviousState();
        result = result * 59 + ($previousState == null ? 43 : $previousState.hashCode());
        Object $newState = this.getNewState();
        result = result * 59 + ($newState == null ? 43 : $newState.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "AuditLog(id=" + this.getId() + ", schoolId=" + this.getSchoolId() + ", entityType=" + this.getEntityType() + ", entityId=" + this.getEntityId() + ", action=" + this.getAction() + ", performedBy=" + this.getPerformedBy() + ", userRole=" + this.getUserRole() + ", timestamp=" + this.getTimestamp() + ", description=" + this.getDescription() + ", previousState=" + this.getPreviousState() + ", newState=" + this.getNewState() + ")";
    }

    @Generated
    public AuditLog() {
    }

    @Generated
    public AuditLog(String id, String schoolId, String entityType, String entityId, String action, String performedBy, String userRole, LocalDateTime timestamp, String description, Object previousState, Object newState) {
        this.id = id;
        this.schoolId = schoolId;
        this.entityType = entityType;
        this.entityId = entityId;
        this.action = action;
        this.performedBy = performedBy;
        this.userRole = userRole;
        this.timestamp = timestamp;
        this.description = description;
        this.previousState = previousState;
        this.newState = newState;
    }
}

