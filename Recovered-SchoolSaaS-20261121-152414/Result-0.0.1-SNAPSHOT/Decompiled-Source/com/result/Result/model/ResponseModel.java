/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.result.Result.model.ResponseModel
 *  lombok.Generated
 */
package com.result.Result.model;

import lombok.Generated;

public class ResponseModel {
    private boolean success;
    private String message;
    private Object data;

    @Generated
    public boolean isSuccess() {
        return this.success;
    }

    @Generated
    public String getMessage() {
        return this.message;
    }

    @Generated
    public Object getData() {
        return this.data;
    }

    @Generated
    public void setSuccess(boolean success) {
        this.success = success;
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
        if (!(o instanceof ResponseModel)) {
            return false;
        }
        ResponseModel other = (ResponseModel)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        if (this.isSuccess() != other.isSuccess()) {
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
        return other instanceof ResponseModel;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + (this.isSuccess() ? 79 : 97);
        String $message = this.getMessage();
        result = result * 59 + ($message == null ? 43 : $message.hashCode());
        Object $data = this.getData();
        result = result * 59 + ($data == null ? 43 : $data.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "ResponseModel(success=" + this.isSuccess() + ", message=" + this.getMessage() + ", data=" + this.getData() + ")";
    }

    @Generated
    public ResponseModel(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
}

