/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.Settings.model.ResponseMessage
 *  lombok.Generated
 */
package com.neel.Settings.model;

import lombok.Generated;

public class ResponseMessage {
    private String message;
    private Object data;

    @Generated
    public String getMessage() {
        return this.message;
    }

    @Generated
    public Object getData() {
        return this.data;
    }

    @Generated
    public void setMessage(String message) {
        this.message = message;
    }

    @Generated
    public void setData(Object data) {
        this.data = data;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ResponseMessage)) {
            return false;
        }
        ResponseMessage other = (ResponseMessage)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        String this$message = this.getMessage();
        String other$message = other.getMessage();
        if (this$message == null ? other$message != null : !this$message.equals(other$message)) {
            return false;
        }
        Object this$data = this.getData();
        Object other$data = other.getData();
        return !(this$data == null ? other$data != null : !this$data.equals(other$data));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof ResponseMessage;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $message = this.getMessage();
        result = result * 59 + ($message == null ? 43 : $message.hashCode());
        Object $data = this.getData();
        result = result * 59 + ($data == null ? 43 : $data.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "ResponseMessage(message=" + this.getMessage() + ", data=" + this.getData() + ")";
    }

    @Generated
    public ResponseMessage(String message, Object data) {
        this.message = message;
        this.data = data;
    }

    @Generated
    public ResponseMessage() {
    }
}

