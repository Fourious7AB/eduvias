package com.schoolaccount.SchoolAccount.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "financial_discrepancies")
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

    public FinancialDiscrepancy() {}

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

    public static FinancialDiscrepancyBuilder builder() {
        return new FinancialDiscrepancyBuilder();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getSchoolId() { return schoolId; }
    public void setSchoolId(String schoolId) { this.schoolId = schoolId; }
    public String getDiscrepancyType() { return discrepancyType; }
    public void setDiscrepancyType(String discrepancyType) { this.discrepancyType = discrepancyType; }
    public String getRelatedEntityId() { return relatedEntityId; }
    public void setRelatedEntityId(String relatedEntityId) { this.relatedEntityId = relatedEntityId; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getExpectedAmount() { return expectedAmount; }
    public void setExpectedAmount(double expectedAmount) { this.expectedAmount = expectedAmount; }
    public double getActualAmount() { return actualAmount; }
    public void setActualAmount(double actualAmount) { this.actualAmount = actualAmount; }
    public double getVarianceAmount() { return varianceAmount; }
    public void setVarianceAmount(double varianceAmount) { this.varianceAmount = varianceAmount; }
    public double getVariancePercentage() { return variancePercentage; }
    public void setVariancePercentage(double variancePercentage) { this.variancePercentage = variancePercentage; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDate getPeriodStart() { return periodStart; }
    public void setPeriodStart(LocalDate periodStart) { this.periodStart = periodStart; }
    public LocalDate getPeriodEnd() { return periodEnd; }
    public void setPeriodEnd(LocalDate periodEnd) { this.periodEnd = periodEnd; }
    public LocalDateTime getDetectedAt() { return detectedAt; }
    public void setDetectedAt(LocalDateTime detectedAt) { this.detectedAt = detectedAt; }
    public LocalDateTime getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(LocalDateTime resolvedAt) { this.resolvedAt = resolvedAt; }
    public String getResolvedBy() { return resolvedBy; }
    public void setResolvedBy(String resolvedBy) { this.resolvedBy = resolvedBy; }
    public String getResolutionNotes() { return resolutionNotes; }
    public void setResolutionNotes(String resolutionNotes) { this.resolutionNotes = resolutionNotes; }

    public static class FinancialDiscrepancyBuilder {
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

        public FinancialDiscrepancyBuilder id(String id) { this.id = id; return this; }
        public FinancialDiscrepancyBuilder schoolId(String schoolId) { this.schoolId = schoolId; return this; }
        public FinancialDiscrepancyBuilder discrepancyType(String discrepancyType) { this.discrepancyType = discrepancyType; return this; }
        public FinancialDiscrepancyBuilder relatedEntityId(String relatedEntityId) { this.relatedEntityId = relatedEntityId; return this; }
        public FinancialDiscrepancyBuilder description(String description) { this.description = description; return this; }
        public FinancialDiscrepancyBuilder expectedAmount(double expectedAmount) { this.expectedAmount = expectedAmount; return this; }
        public FinancialDiscrepancyBuilder actualAmount(double actualAmount) { this.actualAmount = actualAmount; return this; }
        public FinancialDiscrepancyBuilder varianceAmount(double varianceAmount) { this.varianceAmount = varianceAmount; return this; }
        public FinancialDiscrepancyBuilder variancePercentage(double variancePercentage) { this.variancePercentage = variancePercentage; return this; }
        public FinancialDiscrepancyBuilder status(String status) { this.status = status; return this; }
        public FinancialDiscrepancyBuilder periodStart(LocalDate periodStart) { this.periodStart = periodStart; return this; }
        public FinancialDiscrepancyBuilder periodEnd(LocalDate periodEnd) { this.periodEnd = periodEnd; return this; }
        public FinancialDiscrepancyBuilder detectedAt(LocalDateTime detectedAt) { this.detectedAt = detectedAt; return this; }
        public FinancialDiscrepancyBuilder resolvedAt(LocalDateTime resolvedAt) { this.resolvedAt = resolvedAt; return this; }
        public FinancialDiscrepancyBuilder resolvedBy(String resolvedBy) { this.resolvedBy = resolvedBy; return this; }
        public FinancialDiscrepancyBuilder resolutionNotes(String resolutionNotes) { this.resolutionNotes = resolutionNotes; return this; }

        public FinancialDiscrepancy build() {
            return new FinancialDiscrepancy(id, schoolId, discrepancyType, relatedEntityId, description, expectedAmount, actualAmount, varianceAmount, variancePercentage, status, periodStart, periodEnd, detectedAt, resolvedAt, resolvedBy, resolutionNotes);
        }
    }
}
