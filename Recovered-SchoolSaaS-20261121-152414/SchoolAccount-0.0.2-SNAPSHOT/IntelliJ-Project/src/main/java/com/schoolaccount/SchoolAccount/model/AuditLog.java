package com.schoolaccount.SchoolAccount.model;

import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "audit_logs")
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

    public AuditLog() {}

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

    public static AuditLogBuilder builder() {
        return new AuditLogBuilder();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getSchoolId() { return schoolId; }
    public void setSchoolId(String schoolId) { this.schoolId = schoolId; }
    public String getEntityType() { return entityType; }
    public void setEntityType(String entityType) { this.entityType = entityType; }
    public String getEntityId() { return entityId; }
    public void setEntityId(String entityId) { this.entityId = entityId; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getPerformedBy() { return performedBy; }
    public void setPerformedBy(String performedBy) { this.performedBy = performedBy; }
    public String getUserRole() { return userRole; }
    public void setUserRole(String userRole) { this.userRole = userRole; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Object getPreviousState() { return previousState; }
    public void setPreviousState(Object previousState) { this.previousState = previousState; }
    public Object getNewState() { return newState; }
    public void setNewState(Object newState) { this.newState = newState; }

    public static class AuditLogBuilder {
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

        public AuditLogBuilder id(String id) { this.id = id; return this; }
        public AuditLogBuilder schoolId(String schoolId) { this.schoolId = schoolId; return this; }
        public AuditLogBuilder entityType(String entityType) { this.entityType = entityType; return this; }
        public AuditLogBuilder entityId(String entityId) { this.entityId = entityId; return this; }
        public AuditLogBuilder action(String action) { this.action = action; return this; }
        public AuditLogBuilder performedBy(String performedBy) { this.performedBy = performedBy; return this; }
        public AuditLogBuilder userRole(String userRole) { this.userRole = userRole; return this; }
        public AuditLogBuilder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }
        public AuditLogBuilder description(String description) { this.description = description; return this; }
        public AuditLogBuilder previousState(Object previousState) { this.previousState = previousState; return this; }
        public AuditLogBuilder newState(Object newState) { this.newState = newState; return this; }

        public AuditLog build() {
            return new AuditLog(id, schoolId, entityType, entityId, action, performedBy, userRole, timestamp, description, previousState, newState);
        }
    }
}
