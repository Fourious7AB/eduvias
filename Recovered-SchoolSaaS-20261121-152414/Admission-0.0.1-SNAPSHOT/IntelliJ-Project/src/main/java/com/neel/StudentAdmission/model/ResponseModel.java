/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.StudentAdmission.model.ResponseModel
 *  lombok.Generated
 */
package com.neel.StudentAdmission.model;

import lombok.Generated;

public class ResponseModel<T> {
    private Boolean success;
    private String message;
    private T data;

    @Generated
    public Boolean getSuccess() {
        return this.success;
    }

    @Generated
    public String getMessage() {
        return this.message;
    }

    @Generated
    public T getData() {
        return (T)this.data;
    }

    @Generated
    public void setSuccess(Boolean success) {
        this.success = success;
    }

    @Generated
    public void setMessage(String message) {
        this.message = message;
    }

    @Generated
    public void setData(T data) {
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
        Boolean this$success = this.getSuccess();
        Boolean other$success = other.getSuccess();
        if (this$success == null ? other$success != null : !((Object)this$success).equals(other$success)) {
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
        Boolean $success = this.getSuccess();
        result = result * 59 + ($success == null ? 43 : ((Object)$success).hashCode());
        String $message = this.getMessage();
        result = result * 59 + ($message == null ? 43 : $message.hashCode());
        Object $data = this.getData();
        result = result * 59 + ($data == null ? 43 : $data.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "ResponseModel(success=" + this.getSuccess() + ", message=" + this.getMessage() + ", data=" + this.getData() + ")";
    }

    @Generated
    public ResponseModel(Boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
}

