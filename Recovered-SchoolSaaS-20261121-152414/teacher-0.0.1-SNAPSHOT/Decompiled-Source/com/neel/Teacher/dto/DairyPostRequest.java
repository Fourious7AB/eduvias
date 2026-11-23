/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.Teacher.dto.DairyPostRequest
 *  com.neel.Teacher.model.DairyPost$Tag
 *  lombok.Generated
 */
package com.neel.Teacher.dto;

import com.neel.Teacher.model.DairyPost;
import java.util.List;
import lombok.Generated;

public class DairyPostRequest {
    private String content;
    private List<String> mediaUrls;
    private List<DairyPost.Tag> tags;
    private String schoolId;

    @Generated
    public DairyPostRequest() {
    }

    @Generated
    public String getContent() {
        return this.content;
    }

    @Generated
    public List<String> getMediaUrls() {
        return this.mediaUrls;
    }

    @Generated
    public List<DairyPost.Tag> getTags() {
        return this.tags;
    }

    @Generated
    public String getSchoolId() {
        return this.schoolId;
    }

    @Generated
    public void setContent(String content) {
        this.content = content;
    }

    @Generated
    public void setMediaUrls(List<String> mediaUrls) {
        this.mediaUrls = mediaUrls;
    }

    @Generated
    public void setTags(List<DairyPost.Tag> tags) {
        this.tags = tags;
    }

    @Generated
    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DairyPostRequest)) {
            return false;
        }
        DairyPostRequest other = (DairyPostRequest)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        String this$content = this.getContent();
        String other$content = other.getContent();
        if (this$content == null ? other$content != null : !this$content.equals(other$content)) {
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
        String this$schoolId = this.getSchoolId();
        String other$schoolId = other.getSchoolId();
        return !(this$schoolId == null ? other$schoolId != null : !this$schoolId.equals(other$schoolId));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof DairyPostRequest;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $content = this.getContent();
        result = result * 59 + ($content == null ? 43 : $content.hashCode());
        List $mediaUrls = this.getMediaUrls();
        result = result * 59 + ($mediaUrls == null ? 43 : ((Object)$mediaUrls).hashCode());
        List $tags = this.getTags();
        result = result * 59 + ($tags == null ? 43 : ((Object)$tags).hashCode());
        String $schoolId = this.getSchoolId();
        result = result * 59 + ($schoolId == null ? 43 : $schoolId.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "DairyPostRequest(content=" + this.getContent() + ", mediaUrls=" + this.getMediaUrls() + ", tags=" + this.getTags() + ", schoolId=" + this.getSchoolId() + ")";
    }
}

