package com.schoolaccount.SchoolAccount.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "audit_reports")
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
    private Map<String, Double> financialSummary = new HashMap<>();
    private List<DiscrepancySummary> discrepancies = new ArrayList<>();
    private List<TransactionSummary> transactions = new ArrayList<>();
    private Double totalRevenue;
    private Double totalExpenses;
    private Double netProfit;
    private Double totalTransactions;
    private Double totalDiscrepancies;

    public AuditReport() {}

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
        this.financialSummary = financialSummary != null ? financialSummary : new HashMap<>();
        this.discrepancies = discrepancies != null ? discrepancies : new ArrayList<>();
        this.transactions = transactions != null ? transactions : new ArrayList<>();
        this.totalRevenue = totalRevenue;
        this.totalExpenses = totalExpenses;
        this.netProfit = netProfit;
        this.totalTransactions = totalTransactions;
        this.totalDiscrepancies = totalDiscrepancies;
    }

    public static AuditReportBuilder builder() {
        return new AuditReportBuilder();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getSchoolId() { return schoolId; }
    public void setSchoolId(String schoolId) { this.schoolId = schoolId; }
    public String getReportType() { return reportType; }
    public void setReportType(String reportType) { this.reportType = reportType; }
    public String getReportName() { return reportName; }
    public void setReportName(String reportName) { this.reportName = reportName; }
    public LocalDate getPeriodStart() { return periodStart; }
    public void setPeriodStart(LocalDate periodStart) { this.periodStart = periodStart; }
    public LocalDate getPeriodEnd() { return periodEnd; }
    public void setPeriodEnd(LocalDate periodEnd) { this.periodEnd = periodEnd; }
    public LocalDateTime getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; }
    public String getGeneratedBy() { return generatedBy; }
    public void setGeneratedBy(String generatedBy) { this.generatedBy = generatedBy; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Map<String, Double> getFinancialSummary() { return financialSummary; }
    public void setFinancialSummary(Map<String, Double> financialSummary) { this.financialSummary = financialSummary; }
    public List<DiscrepancySummary> getDiscrepancies() { return discrepancies; }
    public void setDiscrepancies(List<DiscrepancySummary> discrepancies) { this.discrepancies = discrepancies; }
    public List<TransactionSummary> getTransactions() { return transactions; }
    public void setTransactions(List<TransactionSummary> transactions) { this.transactions = transactions; }
    public Double getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(Double totalRevenue) { this.totalRevenue = totalRevenue; }
    public Double getTotalExpenses() { return totalExpenses; }
    public void setTotalExpenses(Double totalExpenses) { this.totalExpenses = totalExpenses; }
    public Double getNetProfit() { return netProfit; }
    public void setNetProfit(Double netProfit) { this.netProfit = netProfit; }
    public Double getTotalTransactions() { return totalTransactions; }
    public void setTotalTransactions(Double totalTransactions) { this.totalTransactions = totalTransactions; }
    public Double getTotalDiscrepancies() { return totalDiscrepancies; }
    public void setTotalDiscrepancies(Double totalDiscrepancies) { this.totalDiscrepancies = totalDiscrepancies; }

    public static class AuditReportBuilder {
        private String id;
        private String schoolId;
        private String reportType;
        private String reportName;
        private LocalDate periodStart;
        private LocalDate periodEnd;
        private LocalDateTime generatedAt;
        private String generatedBy;
        private String status;
        private Map<String, Double> financialSummary = new HashMap<>();
        private List<DiscrepancySummary> discrepancies = new ArrayList<>();
        private List<TransactionSummary> transactions = new ArrayList<>();
        private Double totalRevenue;
        private Double totalExpenses;
        private Double netProfit;
        private Double totalTransactions;
        private Double totalDiscrepancies;

        public AuditReportBuilder id(String id) { this.id = id; return this; }
        public AuditReportBuilder schoolId(String schoolId) { this.schoolId = schoolId; return this; }
        public AuditReportBuilder reportType(String reportType) { this.reportType = reportType; return this; }
        public AuditReportBuilder reportName(String reportName) { this.reportName = reportName; return this; }
        public AuditReportBuilder periodStart(LocalDate periodStart) { this.periodStart = periodStart; return this; }
        public AuditReportBuilder periodEnd(LocalDate periodEnd) { this.periodEnd = periodEnd; return this; }
        public AuditReportBuilder generatedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; return this; }
        public AuditReportBuilder generatedBy(String generatedBy) { this.generatedBy = generatedBy; return this; }
        public AuditReportBuilder status(String status) { this.status = status; return this; }
        public AuditReportBuilder financialSummary(Map<String, Double> financialSummary) { this.financialSummary = financialSummary; return this; }
        public AuditReportBuilder discrepancies(List<DiscrepancySummary> discrepancies) { this.discrepancies = discrepancies; return this; }
        public AuditReportBuilder transactions(List<TransactionSummary> transactions) { this.transactions = transactions; return this; }
        public AuditReportBuilder totalRevenue(Double totalRevenue) { this.totalRevenue = totalRevenue; return this; }
        public AuditReportBuilder totalExpenses(Double totalExpenses) { this.totalExpenses = totalExpenses; return this; }
        public AuditReportBuilder netProfit(Double netProfit) { this.netProfit = netProfit; return this; }
        public AuditReportBuilder totalTransactions(Double totalTransactions) { this.totalTransactions = totalTransactions; return this; }
        public AuditReportBuilder totalDiscrepancies(Double totalDiscrepancies) { this.totalDiscrepancies = totalDiscrepancies; return this; }

        public AuditReport build() {
            return new AuditReport(id, schoolId, reportType, reportName, periodStart, periodEnd, generatedAt, generatedBy, status, financialSummary, discrepancies, transactions, totalRevenue, totalExpenses, netProfit, totalTransactions, totalDiscrepancies);
        }
    }

    public static class DiscrepancySummary {
        private String discrepancyType;
        private int count;
        private double totalAmount;
        private List<String> discrepancyIds = new ArrayList<>();

        public DiscrepancySummary() {}
        public DiscrepancySummary(String discrepancyType, int count, double totalAmount, List<String> discrepancyIds) {
            this.discrepancyType = discrepancyType;
            this.count = count;
            this.totalAmount = totalAmount;
            this.discrepancyIds = discrepancyIds;
        }

        public String getDiscrepancyType() { return discrepancyType; }
        public void setDiscrepancyType(String discrepancyType) { this.discrepancyType = discrepancyType; }
        public int getCount() { return count; }
        public void setCount(int count) { this.count = count; }
        public double getTotalAmount() { return totalAmount; }
        public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
        public List<String> getDiscrepancyIds() { return discrepancyIds; }
        public void setDiscrepancyIds(List<String> discrepancyIds) { this.discrepancyIds = discrepancyIds; }
    }

    public static class TransactionSummary {
        private String transactionType;
        private int count;
        private double totalAmount;

        public TransactionSummary() {}
        public TransactionSummary(String transactionType, int count, double totalAmount) {
            this.transactionType = transactionType;
            this.count = count;
            this.totalAmount = totalAmount;
        }

        public String getTransactionType() { return transactionType; }
        public void setTransactionType(String transactionType) { this.transactionType = transactionType; }
        public int getCount() { return count; }
        public void setCount(int count) { this.count = count; }
        public double getTotalAmount() { return totalAmount; }
        public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    }
}
