package com.schoolaccount.SchoolAccount.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "profit_and_loss_statements")
public class ProfitAndLossStatement {
    @Id
    private String id;
    private String schoolId;
    private String schoolName;
    private LocalDate periodStart;
    private LocalDate periodEnd;
    private LocalDateTime generatedAt;
    private String generatedBy;
    private double totalRevenue;
    private double totalExpenses;
    private double netProfit;
    private List<RevenueCategory> revenueCategories = new ArrayList<>();
    private List<ExpenseCategory> expenseCategories = new ArrayList<>();
    private Map<String, Double> previousPeriodComparison = new HashMap<>();

    public ProfitAndLossStatement() {}

    public ProfitAndLossStatement(String id, String schoolId, String schoolName, LocalDate periodStart, LocalDate periodEnd, LocalDateTime generatedAt, String generatedBy, double totalRevenue, double totalExpenses, double netProfit, List<RevenueCategory> revenueCategories, List<ExpenseCategory> expenseCategories, Map<String, Double> previousPeriodComparison) {
        this.id = id;
        this.schoolId = schoolId;
        this.schoolName = schoolName;
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
        this.generatedAt = generatedAt;
        this.generatedBy = generatedBy;
        this.totalRevenue = totalRevenue;
        this.totalExpenses = totalExpenses;
        this.netProfit = netProfit;
        this.revenueCategories = revenueCategories != null ? revenueCategories : new ArrayList<>();
        this.expenseCategories = expenseCategories != null ? expenseCategories : new ArrayList<>();
        this.previousPeriodComparison = previousPeriodComparison != null ? previousPeriodComparison : new HashMap<>();
    }

    public static ProfitAndLossStatementBuilder builder() {
        return new ProfitAndLossStatementBuilder();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getSchoolId() { return schoolId; }
    public void setSchoolId(String schoolId) { this.schoolId = schoolId; }
    public String getSchoolName() { return schoolName; }
    public void setSchoolName(String schoolName) { this.schoolName = schoolName; }
    public LocalDate getPeriodStart() { return periodStart; }
    public void setPeriodStart(LocalDate periodStart) { this.periodStart = periodStart; }
    public LocalDate getPeriodEnd() { return periodEnd; }
    public void setPeriodEnd(LocalDate periodEnd) { this.periodEnd = periodEnd; }
    public LocalDateTime getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; }
    public String getGeneratedBy() { return generatedBy; }
    public void setGeneratedBy(String generatedBy) { this.generatedBy = generatedBy; }
    public double getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(double totalRevenue) { this.totalRevenue = totalRevenue; }
    public double getTotalExpenses() { return totalExpenses; }
    public void setTotalExpenses(double totalExpenses) { this.totalExpenses = totalExpenses; }
    public double getNetProfit() { return netProfit; }
    public void setNetProfit(double netProfit) { this.netProfit = netProfit; }
    public List<RevenueCategory> getRevenueCategories() { return revenueCategories; }
    public void setRevenueCategories(List<RevenueCategory> revenueCategories) { this.revenueCategories = revenueCategories; }
    public List<ExpenseCategory> getExpenseCategories() { return expenseCategories; }
    public void setExpenseCategories(List<ExpenseCategory> expenseCategories) { this.expenseCategories = expenseCategories; }
    public Map<String, Double> getPreviousPeriodComparison() { return previousPeriodComparison; }
    public void setPreviousPeriodComparison(Map<String, Double> previousPeriodComparison) { this.previousPeriodComparison = previousPeriodComparison; }

    public static class ProfitAndLossStatementBuilder {
        private String id;
        private String schoolId;
        private String schoolName;
        private LocalDate periodStart;
        private LocalDate periodEnd;
        private LocalDateTime generatedAt;
        private String generatedBy;
        private double totalRevenue;
        private double totalExpenses;
        private double netProfit;
        private List<RevenueCategory> revenueCategories = new ArrayList<>();
        private List<ExpenseCategory> expenseCategories = new ArrayList<>();
        private Map<String, Double> previousPeriodComparison = new HashMap<>();

        public ProfitAndLossStatementBuilder id(String id) { this.id = id; return this; }
        public ProfitAndLossStatementBuilder schoolId(String schoolId) { this.schoolId = schoolId; return this; }
        public ProfitAndLossStatementBuilder schoolName(String schoolName) { this.schoolName = schoolName; return this; }
        public ProfitAndLossStatementBuilder periodStart(LocalDate periodStart) { this.periodStart = periodStart; return this; }
        public ProfitAndLossStatementBuilder periodEnd(LocalDate periodEnd) { this.periodEnd = periodEnd; return this; }
        public ProfitAndLossStatementBuilder generatedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; return this; }
        public ProfitAndLossStatementBuilder generatedBy(String generatedBy) { this.generatedBy = generatedBy; return this; }
        public ProfitAndLossStatementBuilder totalRevenue(double totalRevenue) { this.totalRevenue = totalRevenue; return this; }
        public ProfitAndLossStatementBuilder totalExpenses(double totalExpenses) { this.totalExpenses = totalExpenses; return this; }
        public ProfitAndLossStatementBuilder netProfit(double netProfit) { this.netProfit = netProfit; return this; }
        public ProfitAndLossStatementBuilder revenueCategories(List<RevenueCategory> revenueCategories) { this.revenueCategories = revenueCategories; return this; }
        public ProfitAndLossStatementBuilder expenseCategories(List<ExpenseCategory> expenseCategories) { this.expenseCategories = expenseCategories; return this; }
        public ProfitAndLossStatementBuilder previousPeriodComparison(Map<String, Double> previousPeriodComparison) { this.previousPeriodComparison = previousPeriodComparison; return this; }

        public ProfitAndLossStatement build() {
            return new ProfitAndLossStatement(id, schoolId, schoolName, periodStart, periodEnd, generatedAt, generatedBy, totalRevenue, totalExpenses, netProfit, revenueCategories, expenseCategories, previousPeriodComparison);
        }
    }

    public static class RevenueItem {
        private String source;
        private String description;
        private double amount;
        private LocalDate date;

        public RevenueItem() {}
        public RevenueItem(String source, String description, double amount, LocalDate date) {
            this.source = source; this.description = description; this.amount = amount; this.date = date;
        }

        public static RevenueItemBuilder builder() { return new RevenueItemBuilder(); }

        public String getSource() { return source; }
        public void setSource(String source) { this.source = source; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public double getAmount() { return amount; }
        public void setAmount(double amount) { this.amount = amount; }
        public LocalDate getDate() { return date; }
        public void setDate(LocalDate date) { this.date = date; }

        public static class RevenueItemBuilder {
            private String source;
            private String description;
            private double amount;
            private LocalDate date;

            public RevenueItemBuilder source(String source) { this.source = source; return this; }
            public RevenueItemBuilder description(String description) { this.description = description; return this; }
            public RevenueItemBuilder amount(double amount) { this.amount = amount; return this; }
            public RevenueItemBuilder date(LocalDate date) { this.date = date; return this; }
            public RevenueItem build() {
                return new RevenueItem(source, description, amount, date);
            }
        }
    }

    public static class RevenueCategory {
        private String category;
        private double amount;
        private double percentageOfTotal;
        private List<RevenueItem> items = new ArrayList<>();

        public RevenueCategory() {}
        public RevenueCategory(String category, double amount, double percentageOfTotal, List<RevenueItem> items) {
            this.category = category; this.amount = amount; this.percentageOfTotal = percentageOfTotal; this.items = items != null ? items : new ArrayList<>();
        }

        public static RevenueCategoryBuilder builder() { return new RevenueCategoryBuilder(); }

        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public double getAmount() { return amount; }
        public void setAmount(double amount) { this.amount = amount; }
        public double getPercentageOfTotal() { return percentageOfTotal; }
        public void setPercentageOfTotal(double percentageOfTotal) { this.percentageOfTotal = percentageOfTotal; }
        public List<RevenueItem> getItems() { return items; }
        public void setItems(List<RevenueItem> items) { this.items = items; }

        public static class RevenueCategoryBuilder {
            private String category;
            private double amount;
            private double percentageOfTotal;
            private List<RevenueItem> items = new ArrayList<>();

            public RevenueCategoryBuilder category(String category) { this.category = category; return this; }
            public RevenueCategoryBuilder amount(double amount) { this.amount = amount; return this; }
            public RevenueCategoryBuilder percentageOfTotal(double percentageOfTotal) { this.percentageOfTotal = percentageOfTotal; return this; }
            public RevenueCategoryBuilder items(List<RevenueItem> items) { this.items = items; return this; }
            public RevenueCategory build() {
                return new RevenueCategory(category, amount, percentageOfTotal, items);
            }
        }
    }

    public static class ExpenseItem {
        private String type;
        private String description;
        private double amount;
        private LocalDate date;

        public ExpenseItem() {}
        public ExpenseItem(String type, String description, double amount, LocalDate date) {
            this.type = type; this.description = description; this.amount = amount; this.date = date;
        }

        public static ExpenseItemBuilder builder() { return new ExpenseItemBuilder(); }

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public double getAmount() { return amount; }
        public void setAmount(double amount) { this.amount = amount; }
        public LocalDate getDate() { return date; }
        public void setDate(LocalDate date) { this.date = date; }

        public static class ExpenseItemBuilder {
            private String type;
            private String description;
            private double amount;
            private LocalDate date;

            public ExpenseItemBuilder type(String type) { this.type = type; return this; }
            public ExpenseItemBuilder description(String description) { this.description = description; return this; }
            public ExpenseItemBuilder amount(double amount) { this.amount = amount; return this; }
            public ExpenseItemBuilder date(LocalDate date) { this.date = date; return this; }
            public ExpenseItem build() {
                return new ExpenseItem(type, description, amount, date);
            }
        }
    }

    public static class ExpenseCategory {
        private String category;
        private double amount;
        private double percentageOfTotal;
        private List<ExpenseItem> items = new ArrayList<>();

        public ExpenseCategory() {}
        public ExpenseCategory(String category, double amount, double percentageOfTotal, List<ExpenseItem> items) {
            this.category = category; this.amount = amount; this.percentageOfTotal = percentageOfTotal; this.items = items != null ? items : new ArrayList<>();
        }

        public static ExpenseCategoryBuilder builder() { return new ExpenseCategoryBuilder(); }

        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public double getAmount() { return amount; }
        public void setAmount(double amount) { this.amount = amount; }
        public double getPercentageOfTotal() { return percentageOfTotal; }
        public void setPercentageOfTotal(double percentageOfTotal) { this.percentageOfTotal = percentageOfTotal; }
        public List<ExpenseItem> getItems() { return items; }
        public void setItems(List<ExpenseItem> items) { this.items = items; }

        public static class ExpenseCategoryBuilder {
            private String category;
            private double amount;
            private double percentageOfTotal;
            private List<ExpenseItem> items = new ArrayList<>();

            public ExpenseCategoryBuilder category(String category) { this.category = category; return this; }
            public ExpenseCategoryBuilder amount(double amount) { this.amount = amount; return this; }
            public ExpenseCategoryBuilder percentageOfTotal(double percentageOfTotal) { this.percentageOfTotal = percentageOfTotal; return this; }
            public ExpenseCategoryBuilder items(List<ExpenseItem> items) { this.items = items; return this; }
            public ExpenseCategory build() {
                return new ExpenseCategory(category, amount, percentageOfTotal, items);
            }
        }
    }
}
