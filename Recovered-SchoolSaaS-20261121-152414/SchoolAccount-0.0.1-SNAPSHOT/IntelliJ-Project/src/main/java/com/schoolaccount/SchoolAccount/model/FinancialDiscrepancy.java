/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.schoolaccount.SchoolAccount.model.FinancialDiscrepancy
 *  com.schoolaccount.SchoolAccount.model.FinancialDiscrepancy$FinancialDiscrepancyBuilder
 *  lombok.Generated
 *  org.springframework.data.annotation.Id
 *  org.springframework.data.mongodb.core.mapping.Document
 */
package com.schoolaccount.SchoolAccount.model;

import com.schoolaccount.SchoolAccount.model.FinancialDiscrepancy;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Generated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="financial_discrepancies")
public class FinancialDiscrepancy {
    @Id
    private String id;
    private String schoolId;
    private String discrepancyType;
    private String relatedEntityId;
    private String description;
    private double expectedAmount;
    private double actualAmount;
    private double varianceAmount;
    private double variancePercentage;
    private String status;
    private LocalDate periodStart;
    private LocalDate periodEnd;
    private LocalDateTime detectedAt;
    private LocalDateTime resolvedAt;
    private String resolvedBy;
    private String resolutionNotes;

    @Generated
    public static FinancialDiscrepancyBuilder builder() {
        return new FinancialDiscrepancyBuilder();
    }

    @Generated
    public String getId() {
        return this.id;
    }

    @Generated
    public String getSchoolId() {
        return this.schoolId;
    }

    @Generated
    public String getDiscrepancyType() {
        return this.discrepancyType;
    }

    @Generated
    public String getRelatedEntityId() {
        return this.relatedEntityId;
    }

    @Generated
    public String getDescription() {
        return this.description;
    }

    @Generated
    public double getExpectedAmount() {
        return this.expectedAmount;
    }

    @Generated
    public double getActualAmount() {
        return this.actualAmount;
    }

    @Generated
    public double getVarianceAmount() {
        return this.varianceAmount;
    }

    @Generated
    public double getVariancePercentage() {
        return this.variancePercentage;
    }

    @Generated
    public String getStatus() {
        return this.status;
    }

    @Generated
    public LocalDate getPeriodStart() {
        return this.periodStart;
    }

    @Generated
    public LocalDate getPeriodEnd() {
        return this.periodEnd;
    }

    @Generated
    public LocalDateTime getDetectedAt() {
        return this.detectedAt;
    }

    @Generated
    public LocalDateTime getResolvedAt() {
        return this.resolvedAt;
    }

    @Generated
    public String getResolvedBy() {
        return this.resolvedBy;
    }

    @Generated
    public String getResolutionNotes() {
        return this.resolutionNotes;
    }

    @Generated
    public void setId(String id) {
        this.id = id;
    }

    @Generated
    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    @Generated
    public void setDiscrepancyType(String discrepancyType) {
        this.discrepancyType = discrepancyType;
    }

    @Generated
    public void setRelatedEntityId(String relatedEntityId) {
        this.relatedEntityId = relatedEntityId;
    }

    @Generated
    public void setDescription(String description) {
        this.description = description;
    }

    @Generated
    public void setExpectedAmount(double expectedAmount) {
        this.expectedAmount = expectedAmount;
    }

    @Generated
    public void setActualAmount(double actualAmount) {
        this.actualAmount = actualAmount;
    }

    @Generated
    public void setVarianceAmount(double varianceAmount) {
        this.varianceAmount = varianceAmount;
    }

    @Generated
    public void setVariancePercentage(double variancePercentage) {
        this.variancePercentage = variancePercentage;
    }

    @Generated
    public void setStatus(String status) {
        this.status = status;
    }

    @Generated
    public void setPeriodStart(LocalDate periodStart) {
        this.periodStart = periodStart;
    }

    @Generated
    public void setPeriodEnd(LocalDate periodEnd) {
        this.periodEnd = periodEnd;
    }

    @Generated
    public void setDetectedAt(LocalDateTime detectedAt) {
        this.detectedAt = detectedAt;
    }

    @Generated
    public void setResolvedAt(LocalDateTime resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    @Generated
    public void setResolvedBy(String resolvedBy) {
        this.resolvedBy = resolvedBy;
    }

    @Generated
    public void setResolutionNotes(String resolutionNotes) {
        this.resolutionNotes = resolutionNotes;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof FinancialDiscrepancy)) {
            return false;
        }
        FinancialDiscrepancy other = (FinancialDiscrepancy)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        if (Double.compare(this.getExpectedAmount(), other.getExpectedAmount()) != 0) {
            return false;
        }
        if (Double.compare(this.getActualAmount(), other.getActualAmount()) != 0) {
            return false;
        }
        if (Double.compare(this.getVarianceAmount(), other.getVarianceAmount()) != 0) {
            return false;
        }
        if (Double.compare(this.getVariancePercentage(), other.getVariancePercentage()) != 0) {
            return false;
        }
        String this$id = this.getId();
        String other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) {
            return false;
        }
        String this$schoolId = this.getSchoolId();
        String other$schoolId = other.getSchoolId();
        if (this$schoolId == null ? other$schoolId != null : !this$schoolId.equals(other$schoolId)) {
            return false;
        }
        String this$discrepancyType = this.getDiscrepancyType();
        String other$discrepancyType = other.getDiscrepancyType();
        if (this$discrepancyType == null ? other$discrepancyType != null : !this$discrepancyType.equals(other$discrepancyType)) {
            return false;
        }
        String this$relatedEntityId = this.getRelatedEntityId();
        String other$relatedEntityId = other.getRelatedEntityId();
        if (this$relatedEntityId == null ? other$relatedEntityId != null : !this$relatedEntityId.equals(other$relatedEntityId)) {
            return false;
        }
        String this$description = this.getDescription();
        String other$description = other.getDescription();
        if (this$description == null ? other$description != null : !this$description.equals(other$description)) {
            return false;
        }
        String this$status = this.getStatus();
        String other$status = other.getStatus();
        if (this$status == null ? other$status != null : !this$status.equals(other$status)) {
            return false;
        }
        LocalDate this$periodStart = this.getPeriodStart();
        LocalDate other$periodStart = other.getPeriodStart();
        if (this$periodStart == null ? other$periodStart != null : !((Object)this$periodStart).equals(other$periodStart)) {
            return false;
        }
        LocalDate this$periodEnd = this.getPeriodEnd();
        LocalDate other$periodEnd = other.getPeriodEnd();
        if (this$periodEnd == null ? other$periodEnd != null : !((Object)this$periodEnd).equals(other$periodEnd)) {
            return false;
        }
        LocalDateTime this$detectedAt = this.getDetectedAt();
        LocalDateTime other$detectedAt = other.getDetectedAt();
        if (this$detectedAt == null ? other$detectedAt != null : !((Object)this$detectedAt).equals(other$detectedAt)) {
            return false;
        }
        LocalDateTime this$resolvedAt = this.getResolvedAt();
        LocalDateTime other$resolvedAt = other.getResolvedAt();
        if (this$resolvedAt == null ? other$resolvedAt != null : !((Object)this$resolvedAt).equals(other$resolvedAt)) {
            return false;
        }
        String this$resolvedBy = this.getResolvedBy();
        String other$resolvedBy = other.getResolvedBy();
        if (this$resolvedBy == null ? other$resolvedBy != null : !this$resolvedBy.equals(other$resolvedBy)) {
            return false;
        }
        String this$resolutionNotes = this.getResolutionNotes();
        String other$resolutionNotes = other.getResolutionNotes();
        return !(this$resolutionNotes == null ? other$resolutionNotes != null : !this$resolutionNotes.equals(other$resolutionNotes));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof FinancialDiscrepancy;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        long $expectedAmount = Double.doubleToLongBits(this.getExpectedAmount());
        result = result * 59 + (int)($expectedAmount >>> 32 ^ $expectedAmount);
        long $actualAmount = Double.doubleToLongBits(this.getActualAmount());
        result = result * 59 + (int)($actualAmount >>> 32 ^ $actualAmount);
        long $varianceAmount = Double.doubleToLongBits(this.getVarianceAmount());
        result = result * 59 + (int)($varianceAmount >>> 32 ^ $varianceAmount);
        long $variancePercentage = Double.doubleToLongBits(this.getVariancePercentage());
        result = result * 59 + (int)($variancePercentage >>> 32 ^ $variancePercentage);
        String $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        String $schoolId = this.getSchoolId();
        result = result * 59 + ($schoolId == null ? 43 : $schoolId.hashCode());
        String $discrepancyType = this.getDiscrepancyType();
        result = result * 59 + ($discrepancyType == null ? 43 : $discrepancyType.hashCode());
        String $relatedEntityId = this.getRelatedEntityId();
        result = result * 59 + ($relatedEntityId == null ? 43 : $relatedEntityId.hashCode());
        String $description = this.getDescription();
        result = result * 59 + ($description == null ? 43 : $description.hashCode());
        String $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : $status.hashCode());
        LocalDate $periodStart = this.getPeriodStart();
        result = result * 59 + ($periodStart == null ? 43 : ((Object)$periodStart).hashCode());
        LocalDate $periodEnd = this.getPeriodEnd();
        result = result * 59 + ($periodEnd == null ? 43 : ((Object)$periodEnd).hashCode());
        LocalDateTime $detectedAt = this.getDetectedAt();
        result = result * 59 + ($detectedAt == null ? 43 : ((Object)$detectedAt).hashCode());
        LocalDateTime $resolvedAt = this.getResolvedAt();
        result = result * 59 + ($resolvedAt == null ? 43 : ((Object)$resolvedAt).hashCode());
        String $resolvedBy = this.getResolvedBy();
        result = result * 59 + ($resolvedBy == null ? 43 : $resolvedBy.hashCode());
        String $resolutionNotes = this.getResolutionNotes();
        result = result * 59 + ($resolutionNotes == null ? 43 : $resolutionNotes.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "FinancialDiscrepancy(id=" + this.getId() + ", schoolId=" + this.getSchoolId() + ", discrepancyType=" + this.getDiscrepancyType() + ", relatedEntityId=" + this.getRelatedEntityId() + ", description=" + this.getDescription() + ", expectedAmount=" + this.getExpectedAmount() + ", actualAmount=" + this.getActualAmount() + ", varianceAmount=" + this.getVarianceAmount() + ", variancePercentage=" + this.getVariancePercentage() + ", status=" + this.getStatus() + ", periodStart=" + this.getPeriodStart() + ", periodEnd=" + this.getPeriodEnd() + ", detectedAt=" + this.getDetectedAt() + ", resolvedAt=" + this.getResolvedAt() + ", resolvedBy=" + this.getResolvedBy() + ", resolutionNotes=" + this.getResolutionNotes() + ")";
    }

    @Generated
    public FinancialDiscrepancy() {
    }

    @Generated
    public FinancialDiscrepancy(String id, String schoolId, String discrepancyType, String relatedEntityId, String description, double expectedAmount, double actualAmount, double varianceAmount, double variancePercentage, String status, LocalDate periodStart, LocalDate periodEnd, LocalDateTime detectedAt, LocalDateTime resolvedAt, String resolvedBy, String resolutionNotes) {
        this.id = id;
        this.schoolId = schoolId;
        this.discrepancyType = discrepancyType;
        this.relatedEntityId = relatedEntityId;
        this.description = description;
        this.expectedAmount = expectedAmount;
        this.actualAmount = actualAmount;
        this.varianceAmount = varianceAmount;
        this.variancePercentage = variancePercentage;
        this.status = status;
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
        this.detectedAt = detectedAt;
        this.resolvedAt = resolvedAt;
        this.resolvedBy = resolvedBy;
        this.resolutionNotes = resolutionNotes;
    }
}

