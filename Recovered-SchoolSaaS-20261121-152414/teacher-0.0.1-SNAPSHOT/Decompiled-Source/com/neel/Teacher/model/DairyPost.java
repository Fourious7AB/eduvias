/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.Teacher.model.DairyPost
 *  com.neel.Teacher.model.DairyPost$Comment
 *  com.neel.Teacher.model.DairyPost$Tag
 *  lombok.Generated
 *  org.springframework.data.annotation.Id
 *  org.springframework.data.mongodb.core.mapping.Document
 */
package com.neel.Teacher.model;

import com.neel.Teacher.model.DairyPost;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="dairy_posts")
public class DairyPost {
    @Id
    private String id;
    private String content;
    private String authorId;
    private String authorName;
    private String authorType;
    private String schoolId;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;
    private List<String> mediaUrls = new ArrayList();
    private List<Tag> tags = new ArrayList();
    private List<Comment> comments = new ArrayList();
    private List<String> likes = new ArrayList();

    @Generated
    public String getId() {
        return this.id;
    }

    @Generated
    public String getContent() {
        return this.content;
    }

    @Generated
    public String getAuthorId() {
        return this.authorId;
    }

    @Generated
    public String getAuthorName() {
        return this.authorName;
    }

    @Generated
    public String getAuthorType() {
        return this.authorType;
    }

    @Generated
    public String getSchoolId() {
        return this.schoolId;
    }

    @Generated
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    @Generated
    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    @Generated
    public List<String> getMediaUrls() {
        return this.mediaUrls;
    }

    @Generated
    public List<Tag> getTags() {
        return this.tags;
    }

    @Generated
    public List<Comment> getComments() {
        return this.comments;
    }

    @Generated
    public List<String> getLikes() {
        return this.likes;
    }

    @Generated
    public void setId(String id) {
        this.id = id;
    }

    @Generated
    public void setContent(String content) {
        this.content = content;
    }

    @Generated
    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    @Generated
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    @Generated
    public void setAuthorType(String authorType) {
        this.authorType = authorType;
    }

    @Generated
    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    @Generated
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Generated
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Generated
    public void setMediaUrls(List<String> mediaUrls) {
        this.mediaUrls = mediaUrls;
    }

    @Generated
    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    @Generated
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Generated
    public void setLikes(List<String> likes) {
        this.likes = likes;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DairyPost)) {
            return false;
        }
        DairyPost other = (DairyPost)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        String this$id = this.getId();
        String other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) {
            return false;
        }
        String this$content = this.getContent();
        String other$content = other.getContent();
        if (this$content == null ? other$content != null : !this$content.equals(other$content)) {
            return false;
        }
        String this$authorId = this.getAuthorId();
        String other$authorId = other.getAuthorId();
        if (this$authorId == null ? other$authorId != null : !this$authorId.equals(other$authorId)) {
            return false;
        }
        String this$authorName = this.getAuthorName();
        String other$authorName = other.getAuthorName();
        if (this$authorName == null ? other$authorName != null : !this$authorName.equals(other$authorName)) {
            return false;
        }
        String this$authorType = this.getAuthorType();
        String other$authorType = other.getAuthorType();
        if (this$authorType == null ? other$authorType != null : !this$authorType.equals(other$authorType)) {
            return false;
        }
        String this$schoolId = this.getSchoolId();
        String other$schoolId = other.getSchoolId();
        if (this$schoolId == null ? other$schoolId != null : !this$schoolId.equals(other$schoolId)) {
            return false;
        }
        LocalDateTime this$createdAt = this.getCreatedAt();
        LocalDateTime other$createdAt = other.getCreatedAt();
        if (this$createdAt == null ? other$createdAt != null : !((Object)this$createdAt).equals(other$createdAt)) {
            return false;
        }
        LocalDateTime this$updatedAt = this.getUpdatedAt();
        LocalDateTime other$updatedAt = other.getUpdatedAt();
        if (this$updatedAt == null ? other$updatedAt != null : !((Object)this$updatedAt).equals(other$updatedAt)) {
            return false;
        }
        List this$mediaUrls = this.getMediaUrls();
        List other$mediaUrls = other.getMediaUrls();
        if (this$mediaUrls == null ? other$mediaUrls != null : !((Object)this$mediaUrls).equals(other$mediaUrls)) {
            return false;
        }
        List this$tags = this.getTags();
        List other$tags = other.getTags();
        if (this$tags == null ? other$tags != null : !((Object)this$tags).equals(other$tags)) {
            return false;
        }
        List this$comments = this.getComments();
        List other$comments = other.getComments();
        if (this$comments == null ? other$comments != null : !((Object)this$comments).equals(other$comments)) {
            return false;
        }
        List this$likes = this.getLikes();
        List other$likes = other.getLikes();
        return !(this$likes == null ? other$likes != null : !((Object)this$likes).equals(other$likes));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof DairyPost;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        String $content = this.getContent();
        result = result * 59 + ($content == null ? 43 : $content.hashCode());
        String $authorId = this.getAuthorId();
        result = result * 59 + ($authorId == null ? 43 : $authorId.hashCode());
        String $authorName = this.getAuthorName();
        result = result * 59 + ($authorName == null ? 43 : $authorName.hashCode());
        String $authorType = this.getAuthorType();
        result = result * 59 + ($authorType == null ? 43 : $authorType.hashCode());
        String $schoolId = this.getSchoolId();
        result = result * 59 + ($schoolId == null ? 43 : $schoolId.hashCode());
        LocalDateTime $createdAt = this.getCreatedAt();
        result = result * 59 + ($createdAt == null ? 43 : ((Object)$createdAt).hashCode());
        LocalDateTime $updatedAt = this.getUpdatedAt();
        result = result * 59 + ($updatedAt == null ? 43 : ((Object)$updatedAt).hashCode());
        List $mediaUrls = this.getMediaUrls();
        result = result * 59 + ($mediaUrls == null ? 43 : ((Object)$mediaUrls).hashCode());
        List $tags = this.getTags();
        result = result * 59 + ($tags == null ? 43 : ((Object)$tags).hashCode());
        List $comments = this.getComments();
        result = result * 59 + ($comments == null ? 43 : ((Object)$comments).hashCode());
        List $likes = this.getLikes();
        result = result * 59 + ($likes == null ? 43 : ((Object)$likes).hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "DairyPost(id=" + this.getId() + ", content=" + this.getContent() + ", authorId=" + this.getAuthorId() + ", authorName=" + this.getAuthorName() + ", authorType=" + this.getAuthorType() + ", schoolId=" + this.getSchoolId() + ", createdAt=" + this.getCreatedAt() + ", updatedAt=" + this.getUpdatedAt() + ", mediaUrls=" + this.getMediaUrls() + ", tags=" + this.getTags() + ", comments=" + this.getComments() + ", likes=" + this.getLikes() + ")";
    }

    @Generated
    public DairyPost() {
    }

    @Generated
    public DairyPost(String id, String content, String authorId, String authorName, String authorType, String schoolId, LocalDateTime createdAt, LocalDateTime updatedAt, List<String> mediaUrls, List<Tag> tags, List<Comment> comments, List<String> likes) {
        this.id = id;
        this.content = content;
        this.authorId = authorId;
        this.authorName = authorName;
        this.authorType = authorType;
        this.schoolId = schoolId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.mediaUrls = mediaUrls;
        this.tags = tags;
        this.comments = comments;
        this.likes = likes;
    }
}

