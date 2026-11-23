/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.StudentAdmission.model.Plan
 *  com.neel.StudentAdmission.model.Plan$Features
 *  jakarta.validation.constraints.Min
 *  lombok.Generated
 *  org.hibernate.validator.constraints.UniqueElements
 *  org.springframework.data.mongodb.core.mapping.Document
 */
package com.neel.StudentAdmission.model;

import com.neel.StudentAdmission.model.Plan;
import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Generated;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="plan")
public class Plan {
    private String id;
    @UniqueElements
    private String planName;
    private Double price;
    @Min(value=1L, message="Duration must be greater than 0")
    private @Min(value=1L, message="Duration must be greater than 0") int duration;
    private List<Features> features;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private boolean status;

    @Generated
    public String getId() {
        return this.id;
    }

    @Generated
    public String getPlanName() {
        return this.planName;
    }

    @Generated
    public Double getPrice() {
        return this.price;
    }

    @Generated
    public int getDuration() {
        return this.duration;
    }

    @Generated
    public List<Features> getFeatures() {
        return this.features;
    }

    @Generated
    public LocalDateTime getCreateAt() {
        return this.createAt;
    }

    @Generated
    public LocalDateTime getUpdateAt() {
        return this.updateAt;
    }

    @Generated
    public boolean isStatus() {
        return this.status;
    }

    @Generated
    public void setId(String id) {
        this.id = id;
    }

    @Generated
    public void setPlanName(String planName) {
        this.planName = planName;
    }

    @Generated
    public void setPrice(Double price) {
        this.price = price;
    }

    @Generated
    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Generated
    public void setFeatures(List<Features> features) {
        this.features = features;
    }

    @Generated
    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    @Generated
    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }

    @Generated
    public void setStatus(boolean status) {
        this.status = status;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Plan)) {
            return false;
        }
        Plan other = (Plan)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        if (this.getDuration() != other.getDuration()) {
            return false;
        }
        if (this.isStatus() != other.isStatus()) {
            return false;
        }
        Double this$price = this.getPrice();
        Double other$price = other.getPrice();
        if (this$price == null ? other$price != null : !((Object)this$price).equals(other$price)) {
            return false;
        }
        String this$id = this.getId();
        String other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) {
            return false;
        }
        String this$planName = this.getPlanName();
        String other$planName = other.getPlanName();
        if (this$planName == null ? other$planName != null : !this$planName.equals(other$planName)) {
            return false;
        }
        List this$features = this.getFeatures();
        List other$features = other.getFeatures();
        if (this$features == null ? other$features != null : !((Object)this$features).equals(other$features)) {
            return false;
        }
        LocalDateTime this$createAt = this.getCreateAt();
        LocalDateTime other$createAt = other.getCreateAt();
        if (this$createAt == null ? other$createAt != null : !((Object)this$createAt).equals(other$createAt)) {
            return false;
        }
        LocalDateTime this$updateAt = this.getUpdateAt();
        LocalDateTime other$updateAt = other.getUpdateAt();
        return !(this$updateAt == null ? other$updateAt != null : !((Object)this$updateAt).equals(other$updateAt));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof Plan;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getDuration();
        result = result * 59 + (this.isStatus() ? 79 : 97);
        Double $price = this.getPrice();
        result = result * 59 + ($price == null ? 43 : ((Object)$price).hashCode());
        String $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        String $planName = this.getPlanName();
        result = result * 59 + ($planName == null ? 43 : $planName.hashCode());
        List $features = this.getFeatures();
        result = result * 59 + ($features == null ? 43 : ((Object)$features).hashCode());
        LocalDateTime $createAt = this.getCreateAt();
        result = result * 59 + ($createAt == null ? 43 : ((Object)$createAt).hashCode());
        LocalDateTime $updateAt = this.getUpdateAt();
        result = result * 59 + ($updateAt == null ? 43 : ((Object)$updateAt).hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "Plan(id=" + this.getId() + ", planName=" + this.getPlanName() + ", price=" + this.getPrice() + ", duration=" + this.getDuration() + ", features=" + this.getFeatures() + ", createAt=" + this.getCreateAt() + ", updateAt=" + this.getUpdateAt() + ", status=" + this.isStatus() + ")";
    }

    @Generated
    public Plan() {
    }

    @Generated
    public Plan(String id, String planName, Double price, int duration, List<Features> features, LocalDateTime createAt, LocalDateTime updateAt, boolean status) {
        this.id = id;
        this.planName = planName;
        this.price = price;
        this.duration = duration;
        this.features = features;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.status = status;
    }
}

