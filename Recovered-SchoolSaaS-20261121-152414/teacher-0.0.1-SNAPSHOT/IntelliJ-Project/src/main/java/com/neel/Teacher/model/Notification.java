/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.Teacher.model.Notification
 *  lombok.Generated
 *  org.springframework.data.annotation.Id
 *  org.springframework.data.mongodb.core.mapping.Document
 */
package com.neel.Teacher.model;

import java.time.LocalDateTime;
import lombok.Generated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="notifications")
public class Notification {
    @Id
    private String id;
    private String recipientId;
    private String recipientType;
    private String senderId;
    private String senderName;
    private String senderType;
    private String content;
    private String type;
    private String referenceId;
    private boolean read;
    private LocalDateTime createdAt = LocalDateTime.now();

    @Generated
    public String getId() {
        return this.id;
    }

    @Generated
    public String getRecipientId() {
        return this.recipientId;
    }

    @Generated
    public String getRecipientType() {
        return this.recipientType;
    }

    @Generated
    public String getSenderId() {
        return this.senderId;
    }

    @Generated
    public String getSenderName() {
        return this.senderName;
    }

    @Generated
    public String getSenderType() {
        return this.senderType;
    }

    @Generated
    public String getContent() {
        return this.content;
    }

    @Generated
    public String getType() {
        return this.type;
    }

    @Generated
    public String getReferenceId() {
        return this.referenceId;
    }

    @Generated
    public boolean isRead() {
        return this.read;
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
    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    @Generated
    public void setRecipientType(String recipientType) {
        this.recipientType = recipientType;
    }

    @Generated
    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    @Generated
    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    @Generated
    public void setSenderType(String senderType) {
        this.senderType = senderType;
    }

    @Generated
    public void setContent(String content) {
        this.content = content;
    }

    @Generated
    public void setType(String type) {
        this.type = type;
    }

    @Generated
    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    @Generated
    public void setRead(boolean read) {
        this.read = read;
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
        if (this.isRead() != other.isRead()) {
            return false;
        }
        String this$id = this.getId();
        String other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) {
            return false;
        }
        String this$recipientId = this.getRecipientId();
        String other$recipientId = other.getRecipientId();
        if (this$recipientId == null ? other$recipientId != null : !this$recipientId.equals(other$recipientId)) {
            return false;
        }
        String this$recipientType = this.getRecipientType();
        String other$recipientType = other.getRecipientType();
        if (this$recipientType == null ? other$recipientType != null : !this$recipientType.equals(other$recipientType)) {
            return false;
        }
        String this$senderId = this.getSenderId();
        String other$senderId = other.getSenderId();
        if (this$senderId == null ? other$senderId != null : !this$senderId.equals(other$senderId)) {
            return false;
        }
        String this$senderName = this.getSenderName();
        String other$senderName = other.getSenderName();
        if (this$senderName == null ? other$senderName != null : !this$senderName.equals(other$senderName)) {
            return false;
        }
        String this$senderType = this.getSenderType();
        String other$senderType = other.getSenderType();
        if (this$senderType == null ? other$senderType != null : !this$senderType.equals(other$senderType)) {
            return false;
        }
        String this$content = this.getContent();
        String other$content = other.getContent();
        if (this$content == null ? other$content != null : !this$content.equals(other$content)) {
            return false;
        }
        String this$type = this.getType();
        String other$type = other.getType();
        if (this$type == null ? other$type != null : !this$type.equals(other$type)) {
            return false;
        }
        String this$referenceId = this.getReferenceId();
        String other$referenceId = other.getReferenceId();
        if (this$referenceId == null ? other$referenceId != null : !this$referenceId.equals(other$referenceId)) {
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
        result = result * 59 + (this.isRead() ? 79 : 97);
        String $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        String $recipientId = this.getRecipientId();
        result = result * 59 + ($recipientId == null ? 43 : $recipientId.hashCode());
        String $recipientType = this.getRecipientType();
        result = result * 59 + ($recipientType == null ? 43 : $recipientType.hashCode());
        String $senderId = this.getSenderId();
        result = result * 59 + ($senderId == null ? 43 : $senderId.hashCode());
        String $senderName = this.getSenderName();
        result = result * 59 + ($senderName == null ? 43 : $senderName.hashCode());
        String $senderType = this.getSenderType();
        result = result * 59 + ($senderType == null ? 43 : $senderType.hashCode());
        String $content = this.getContent();
        result = result * 59 + ($content == null ? 43 : $content.hashCode());
        String $type = this.getType();
        result = result * 59 + ($type == null ? 43 : $type.hashCode());
        String $referenceId = this.getReferenceId();
        result = result * 59 + ($referenceId == null ? 43 : $referenceId.hashCode());
        LocalDateTime $createdAt = this.getCreatedAt();
        result = result * 59 + ($createdAt == null ? 43 : ((Object)$createdAt).hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "Notification(id=" + this.getId() + ", recipientId=" + this.getRecipientId() + ", recipientType=" + this.getRecipientType() + ", senderId=" + this.getSenderId() + ", senderName=" + this.getSenderName() + ", senderType=" + this.getSenderType() + ", content=" + this.getContent() + ", type=" + this.getType() + ", referenceId=" + this.getReferenceId() + ", read=" + this.isRead() + ", createdAt=" + this.getCreatedAt() + ")";
    }

    @Generated
    public Notification() {
    }

    @Generated
    public Notification(String id, String recipientId, String recipientType, String senderId, String senderName, String senderType, String content, String type, String referenceId, boolean read, LocalDateTime createdAt) {
        this.id = id;
        this.recipientId = recipientId;
        this.recipientType = recipientType;
        this.senderId = senderId;
        this.senderName = senderName;
        this.senderType = senderType;
        this.content = content;
        this.type = type;
        this.referenceId = referenceId;
        this.read = read;
        this.createdAt = createdAt;
    }
}

