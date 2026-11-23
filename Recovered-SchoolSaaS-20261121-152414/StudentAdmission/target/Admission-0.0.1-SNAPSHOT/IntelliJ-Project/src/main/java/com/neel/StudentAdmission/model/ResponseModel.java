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
    private String message;
    private T data;

    @Generated
    public String getMessage() {
        return this.message;
    }

    @Generated
    public T getData() {
        return (T)this.data;
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
        String $message = this.getMessage();
        result = result * 59 + ($message == null ? 43 : $message.hashCode());
        Object $data = this.getData();
        result = result * 59 + ($data == null ? 43 : $data.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "ResponseModel(message=" + this.getMessage() + ", data=" + this.getData() + ")";
    }

    @Generated
    public ResponseModel(String message, T data) {
        this.message = message;
        this.data = data;
    }
}

