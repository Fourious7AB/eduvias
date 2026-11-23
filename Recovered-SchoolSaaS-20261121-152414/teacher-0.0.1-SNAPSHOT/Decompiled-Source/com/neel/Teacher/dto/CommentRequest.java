/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.Teacher.dto.CommentRequest
 *  lombok.Generated
 */
package com.neel.Teacher.dto;

import lombok.Generated;

public class CommentRequest {
    private String content;

    @Generated
    public CommentRequest() {
    }

    @Generated
    public String getContent() {
        return this.content;
    }

    @Generated
    public void setContent(String content) {
        this.content = content;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CommentRequest)) {
            return false;
        }
        CommentRequest other = (CommentRequest)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        String this$content = this.getContent();
        String other$content = other.getContent();
        return !(this$content == null ? other$content != null : !this$content.equals(other$content));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof CommentRequest;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $content = this.getContent();
        result = result * 59 + ($content == null ? 43 : $content.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "CommentRequest(content=" + this.getContent() + ")";
    }
}

