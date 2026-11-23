/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.schoolaccount.SchoolAccount.model.AuditReport
 *  com.schoolaccount.SchoolAccount.model.AuditReport$AuditReportBuilder
 *  com.schoolaccount.SchoolAccount.model.AuditReport$DiscrepancySummary
 *  com.schoolaccount.SchoolAccount.model.AuditReport$TransactionSummary
 *  lombok.Generated
 *  org.springframework.data.annotation.Id
 *  org.springframework.data.mongodb.core.mapping.Document
 */
package com.schoolaccount.SchoolAccount.model;

import com.schoolaccount.SchoolAccount.model.AuditReport;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Generated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/*
 * Exception performing whole class analysis ignored.
 */
@Document(collection="audit_reports")
public class AuditReport {
    @Id
    private String id;
    private String schoolId;
    private String reportType;
    private String reportName;
    private LocalDate periodStart;
    private LocalDate periodEnd;
    private LocalDateTime generatedAt;
    private String generatedBy;
    private String status;
    private Map<String, Double> financialSummary;
    private List<DiscrepancySummary> discrepancies;
    private List<TransactionSummary> transactions;
    private Double totalRevenue;
    private Double totalExpenses;
    private Double netProfit;
    private Double totalTransactions;
    private Double totalDiscrepancies;

    @Generated
    private static Map<String, Double> $default$financialSummary() {
        return new HashMap<String, Double>();
    }

    @Generated
    private static List<DiscrepancySummary> $default$discrepancies() {
        return new ArrayList<DiscrepancySummary>();
    }

    @Generated
    private static List<TransactionSummary> $default$transactions() {
        return new ArrayList<TransactionSummary>();
    }

    @Generated
    public static AuditReportBuilder builder() {
        return new AuditReportBuilder();
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
    public String getReportType() {
        return this.reportType;
    }

    @Generated
    public String getReportName() {
        return this.reportName;
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
    public LocalDateTime getGeneratedAt() {
        return this.generatedAt;
    }

    @Generated
    public String getGeneratedBy() {
        return this.generatedBy;
    }

    @Generated
    public String getStatus() {
        return this.status;
    }

    @Generated
    public Map<String, Double> getFinancialSummary() {
        return this.financialSummary;
    }

    @Generated
    public List<DiscrepancySummary> getDiscrepancies() {
        return this.discrepancies;
    }

    @Generated
    public List<TransactionSummary> getTransactions() {
        return this.transactions;
    }

    @Generated
    public Double getTotalRevenue() {
        return this.totalRevenue;
    }

    @Generated
    public Double getTotalExpenses() {
        return this.totalExpenses;
    }

    @Generated
    public Double getNetProfit() {
        return this.netProfit;
    }

    @Generated
    public Double getTotalTransactions() {
        return this.totalTransactions;
    }

    @Generated
    public Double getTotalDiscrepancies() {
        return this.totalDiscrepancies;
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
    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    @Generated
    public void setReportName(String reportName) {
        this.reportName = reportName;
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
    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    @Generated
    public void setGeneratedBy(String generatedBy) {
        this.generatedBy = generatedBy;
    }

    @Generated
    public void setStatus(String status) {
        this.status = status;
    }

    @Generated
    public void setFinancialSummary(Map<String, Double> financialSummary) {
        this.financialSummary = financialSummary;
    }

    @Generated
    public void setDiscrepancies(List<DiscrepancySummary> discrepancies) {
        this.discrepancies = discrepancies;
    }

    @Generated
    public void setTransactions(List<TransactionSummary> transactions) {
        this.transactions = transactions;
    }

    @Generated
    public void setTotalRevenue(Double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    @Generated
    public void setTotalExpenses(Double totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    @Generated
    public void setNetProfit(Double netProfit) {
        this.netProfit = netProfit;
    }

    @Generated
    public void setTotalTransactions(Double totalTransactions) {
        this.totalTransactions = totalTransactions;
    }

    @Generated
    public void setTotalDiscrepancies(Double totalDiscrepancies) {
        this.totalDiscrepancies = totalDiscrepancies;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AuditReport)) {
            return false;
        }
        AuditReport other = (AuditReport)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        Double this$totalRevenue = this.getTotalRevenue();
        Double other$totalRevenue = other.getTotalRevenue();
        if (this$totalRevenue == null ? other$totalRevenue != null : !((Object)this$totalRevenue).equals(other$totalRevenue)) {
            return false;
        }
        Double this$totalExpenses = this.getTotalExpenses();
        Double other$totalExpenses = other.getTotalExpenses();
        if (this$totalExpenses == null ? other$totalExpenses != null : !((Object)this$totalExpenses).equals(other$totalExpenses)) {
            return false;
        }
        Double this$netProfit = this.getNetProfit();
        Double other$netProfit = other.getNetProfit();
        if (this$netProfit == null ? other$netProfit != null : !((Object)this$netProfit).equals(other$netProfit)) {
            return false;
        }
        Double this$totalTransactions = this.getTotalTransactions();
        Double other$totalTransactions = other.getTotalTransactions();
        if (this$totalTransactions == null ? other$totalTransactions != null : !((Object)this$totalTransactions).equals(other$totalTransactions)) {
            return false;
        }
        Double this$totalDiscrepancies = this.getTotalDiscrepancies();
        Double other$totalDiscrepancies = other.getTotalDiscrepancies();
        if (this$totalDiscrepancies == null ? other$totalDiscrepancies != null : !((Object)this$totalDiscrepancies).equals(other$totalDiscrepancies)) {
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
        String this$reportType = this.getReportType();
        String other$reportType = other.getReportType();
        if (this$reportType == null ? other$reportType != null : !this$reportType.equals(other$reportType)) {
            return false;
        }
        String this$reportName = this.getReportName();
        String other$reportName = other.getReportName();
        if (this$reportName == null ? other$reportName != null : !this$reportName.equals(other$reportName)) {
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
        LocalDateTime this$generatedAt = this.getGeneratedAt();
        LocalDateTime other$generatedAt = other.getGeneratedAt();
        if (this$generatedAt == null ? other$generatedAt != null : !((Object)this$generatedAt).equals(other$generatedAt)) {
            return false;
        }
        String this$generatedBy = this.getGeneratedBy();
        String other$generatedBy = other.getGeneratedBy();
        if (this$generatedBy == null ? other$generatedBy != null : !this$generatedBy.equals(other$generatedBy)) {
            return false;
        }
        String this$status = this.getStatus();
        String other$status = other.getStatus();
        if (this$status == null ? other$status != null : !this$status.equals(other$status)) {
            return false;
        }
        Map this$financialSummary = this.getFinancialSummary();
        Map other$financialSummary = other.getFinancialSummary();
        if (this$financialSummary == null ? other$financialSummary != null : !((Object)this$financialSummary).equals(other$financialSummary)) {
            return false;
        }
        List this$discrepancies = this.getDiscrepancies();
        List other$discrepancies = other.getDiscrepancies();
        if (this$discrepancies == null ? other$discrepancies != null : !((Object)this$discrepancies).equals(other$discrepancies)) {
            return false;
        }
        List this$transactions = this.getTransactions();
        List other$transactions = other.getTransactions();
        return !(this$transactions == null ? other$transactions != null : !((Object)this$transactions).equals(other$transactions));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof AuditReport;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Double $totalRevenue = this.getTotalRevenue();
        result = result * 59 + ($totalRevenue == null ? 43 : ((Object)$totalRevenue).hashCode());
        Double $totalExpenses = this.getTotalExpenses();
        result = result * 59 + ($totalExpenses == null ? 43 : ((Object)$totalExpenses).hashCode());
        Double $netProfit = this.getNetProfit();
        result = result * 59 + ($netProfit == null ? 43 : ((Object)$netProfit).hashCode());
        Double $totalTransactions = this.getTotalTransactions();
        result = result * 59 + ($totalTransactions == null ? 43 : ((Object)$totalTransactions).hashCode());
        Double $totalDiscrepancies = this.getTotalDiscrepancies();
        result = result * 59 + ($totalDiscrepancies == null ? 43 : ((Object)$totalDiscrepancies).hashCode());
        String $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        String $schoolId = this.getSchoolId();
        result = result * 59 + ($schoolId == null ? 43 : $schoolId.hashCode());
        String $reportType = this.getReportType();
        result = result * 59 + ($reportType == null ? 43 : $reportType.hashCode());
        String $reportName = this.getReportName();
        result = result * 59 + ($reportName == null ? 43 : $reportName.hashCode());
        LocalDate $periodStart = this.getPeriodStart();
        result = result * 59 + ($periodStart == null ? 43 : ((Object)$periodStart).hashCode());
        LocalDate $periodEnd = this.getPeriodEnd();
        result = result * 59 + ($periodEnd == null ? 43 : ((Object)$periodEnd).hashCode());
        LocalDateTime $generatedAt = this.getGeneratedAt();
        result = result * 59 + ($generatedAt == null ? 43 : ((Object)$generatedAt).hashCode());
        String $generatedBy = this.getGeneratedBy();
        result = result * 59 + ($generatedBy == null ? 43 : $generatedBy.hashCode());
        String $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : $status.hashCode());
        Map $financialSummary = this.getFinancialSummary();
        result = result * 59 + ($financialSummary == null ? 43 : ((Object)$financialSummary).hashCode());
        List $discrepancies = this.getDiscrepancies();
        result = result * 59 + ($discrepancies == null ? 43 : ((Object)$discrepancies).hashCode());
        List $transactions = this.getTransactions();
        result = result * 59 + ($transactions == null ? 43 : ((Object)$transactions).hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "AuditReport(id=" + this.getId() + ", schoolId=" + this.getSchoolId() + ", reportType=" + this.getReportType() + ", reportName=" + this.getReportName() + ", periodStart=" + this.getPeriodStart() + ", periodEnd=" + this.getPeriodEnd() + ", generatedAt=" + this.getGeneratedAt() + ", generatedBy=" + this.getGeneratedBy() + ", status=" + this.getStatus() + ", financialSummary=" + this.getFinancialSummary() + ", discrepancies=" + this.getDiscrepancies() + ", transactions=" + this.getTransactions() + ", totalRevenue=" + this.getTotalRevenue() + ", totalExpenses=" + this.getTotalExpenses() + ", netProfit=" + this.getNetProfit() + ", totalTransactions=" + this.getTotalTransactions() + ", totalDiscrepancies=" + this.getTotalDiscrepancies() + ")";
    }

    @Generated
    public AuditReport() {
        this.financialSummary = AuditReport.$default$financialSummary();
        this.discrepancies = AuditReport.$default$discrepancies();
        this.transactions = AuditReport.$default$transactions();
    }

    @Generated
    public AuditReport(String id, String schoolId, String reportType, String reportName, LocalDate periodStart, LocalDate periodEnd, LocalDateTime generatedAt, String generatedBy, String status, Map<String, Double> financialSummary, List<DiscrepancySummary> discrepancies, List<TransactionSummary> transactions, Double totalRevenue, Double totalExpenses, Double netProfit, Double totalTransactions, Double totalDiscrepancies) {
        this.id = id;
        this.schoolId = schoolId;
        this.reportType = reportType;
        this.reportName = reportName;
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
        this.generatedAt = generatedAt;
        this.generatedBy = generatedBy;
        this.status = status;
        this.financialSummary = financialSummary;
        this.discrepancies = discrepancies;
        this.transactions = transactions;
        this.totalRevenue = totalRevenue;
        this.totalExpenses = totalExpenses;
        this.netProfit = netProfit;
        this.totalTransactions = totalTransactions;
        this.totalDiscrepancies = totalDiscrepancies;
    }
}

