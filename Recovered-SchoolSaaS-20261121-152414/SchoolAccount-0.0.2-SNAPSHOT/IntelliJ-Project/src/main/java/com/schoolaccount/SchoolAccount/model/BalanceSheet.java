package com.schoolaccount.SchoolAccount.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "balance_sheets")
public class BalanceSheet {
    @Id
    private String id;
    private String schoolId;
    private String schoolName;
    private LocalDate asOfDate;
    private LocalDateTime generatedAt;
    private String generatedBy;
    private double totalAssets;
    private double totalLiabilities;
    private double totalEquity;
    private List<AssetCategory> assetCategories = new ArrayList<>();
    private List<LiabilityCategory> liabilityCategories = new ArrayList<>();
    private List<EquityItem> equityItems = new ArrayList<>();

    public BalanceSheet() {}

    public BalanceSheet(String id, String schoolId, String schoolName, LocalDate asOfDate, LocalDateTime generatedAt, String generatedBy, double totalAssets, double totalLiabilities, double totalEquity, List<AssetCategory> assetCategories, List<LiabilityCategory> liabilityCategories, List<EquityItem> equityItems) {
        this.id = id;
        this.schoolId = schoolId;
        this.schoolName = schoolName;
        this.asOfDate = asOfDate;
        this.generatedAt = generatedAt;
        this.generatedBy = generatedBy;
        this.totalAssets = totalAssets;
        this.totalLiabilities = totalLiabilities;
        this.totalEquity = totalEquity;
        this.assetCategories = assetCategories != null ? assetCategories : new ArrayList<>();
        this.liabilityCategories = liabilityCategories != null ? liabilityCategories : new ArrayList<>();
        this.equityItems = equityItems != null ? equityItems : new ArrayList<>();
    }

    public static BalanceSheetBuilder builder() {
        return new BalanceSheetBuilder();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getSchoolId() { return schoolId; }
    public void setSchoolId(String schoolId) { this.schoolId = schoolId; }
    public String getSchoolName() { return schoolName; }
    public void setSchoolName(String schoolName) { this.schoolName = schoolName; }
    public LocalDate getAsOfDate() { return asOfDate; }
    public void setAsOfDate(LocalDate asOfDate) { this.asOfDate = asOfDate; }
    public LocalDateTime getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; }
    public String getGeneratedBy() { return generatedBy; }
    public void setGeneratedBy(String generatedBy) { this.generatedBy = generatedBy; }
    public double getTotalAssets() { return totalAssets; }
    public void setTotalAssets(double totalAssets) { this.totalAssets = totalAssets; }
    public double getTotalLiabilities() { return totalLiabilities; }
    public void setTotalLiabilities(double totalLiabilities) { this.totalLiabilities = totalLiabilities; }
    public double getTotalEquity() { return totalEquity; }
    public void setTotalEquity(double totalEquity) { this.totalEquity = totalEquity; }
    public List<AssetCategory> getAssetCategories() { return assetCategories; }
    public void setAssetCategories(List<AssetCategory> assetCategories) { this.assetCategories = assetCategories; }
    public List<LiabilityCategory> getLiabilityCategories() { return liabilityCategories; }
    public void setLiabilityCategories(List<LiabilityCategory> liabilityCategories) { this.liabilityCategories = liabilityCategories; }
    public List<EquityItem> getEquityItems() { return equityItems; }
    public void setEquityItems(List<EquityItem> equityItems) { this.equityItems = equityItems; }

    public static class BalanceSheetBuilder {
        private String id;
        private String schoolId;
        private String schoolName;
        private LocalDate asOfDate;
        private LocalDateTime generatedAt;
        private String generatedBy;
        private double totalAssets;
        private double totalLiabilities;
        private double totalEquity;
        private List<AssetCategory> assetCategories = new ArrayList<>();
        private List<LiabilityCategory> liabilityCategories = new ArrayList<>();
        private List<EquityItem> equityItems = new ArrayList<>();

        public BalanceSheetBuilder id(String id) { this.id = id; return this; }
        public BalanceSheetBuilder schoolId(String schoolId) { this.schoolId = schoolId; return this; }
        public BalanceSheetBuilder schoolName(String schoolName) { this.schoolName = schoolName; return this; }
        public BalanceSheetBuilder asOfDate(LocalDate asOfDate) { this.asOfDate = asOfDate; return this; }
        public BalanceSheetBuilder generatedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; return this; }
        public BalanceSheetBuilder generatedBy(String generatedBy) { this.generatedBy = generatedBy; return this; }
        public BalanceSheetBuilder totalAssets(double totalAssets) { this.totalAssets = totalAssets; return this; }
        public BalanceSheetBuilder totalLiabilities(double totalLiabilities) { this.totalLiabilities = totalLiabilities; return this; }
        public BalanceSheetBuilder totalEquity(double totalEquity) { this.totalEquity = totalEquity; return this; }
        public BalanceSheetBuilder assetCategories(List<AssetCategory> assetCategories) { this.assetCategories = assetCategories; return this; }
        public BalanceSheetBuilder liabilityCategories(List<LiabilityCategory> liabilityCategories) { this.liabilityCategories = liabilityCategories; return this; }
        public BalanceSheetBuilder equityItems(List<EquityItem> equityItems) { this.equityItems = equityItems; return this; }

        public BalanceSheet build() {
            return new BalanceSheet(id, schoolId, schoolName, asOfDate, generatedAt, generatedBy, totalAssets, totalLiabilities, totalEquity, assetCategories, liabilityCategories, equityItems);
        }
    }

    public static class AssetItem {
        private String name;
        private String description;
        private double amount;
        private LocalDate acquisitionDate;
        private double depreciationValue;

        public AssetItem() {}
        public AssetItem(String name, String description, double amount, LocalDate acquisitionDate, double depreciationValue) {
            this.name = name; this.description = description; this.amount = amount; this.acquisitionDate = acquisitionDate; this.depreciationValue = depreciationValue;
        }

        public static AssetItemBuilder builder() { return new AssetItemBuilder(); }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public double getAmount() { return amount; }
        public void setAmount(double amount) { this.amount = amount; }
        public LocalDate getAcquisitionDate() { return acquisitionDate; }
        public void setAcquisitionDate(LocalDate acquisitionDate) { this.acquisitionDate = acquisitionDate; }
        public double getDepreciationValue() { return depreciationValue; }
        public void setDepreciationValue(double depreciationValue) { this.depreciationValue = depreciationValue; }

        public static class AssetItemBuilder {
            private String name;
            private String description;
            private double amount;
            private LocalDate acquisitionDate;
            private double depreciationValue;

            public AssetItemBuilder name(String name) { this.name = name; return this; }
            public AssetItemBuilder description(String description) { this.description = description; return this; }
            public AssetItemBuilder amount(double amount) { this.amount = amount; return this; }
            public AssetItemBuilder acquisitionDate(LocalDate acquisitionDate) { this.acquisitionDate = acquisitionDate; return this; }
            public AssetItemBuilder depreciationValue(double depreciationValue) { this.depreciationValue = depreciationValue; return this; }
            public AssetItem build() {
                return new AssetItem(name, description, amount, acquisitionDate, depreciationValue);
            }
        }
    }

    public static class AssetCategory {
        private String category;
        private double amount;
        private double percentageOfTotal;
        private List<AssetItem> items = new ArrayList<>();

        public AssetCategory() {}
        public AssetCategory(String category, double amount, double percentageOfTotal, List<AssetItem> items) {
            this.category = category; this.amount = amount; this.percentageOfTotal = percentageOfTotal; this.items = items != null ? items : new ArrayList<>();
        }

        public static AssetCategoryBuilder builder() { return new AssetCategoryBuilder(); }

        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public double getAmount() { return amount; }
        public void setAmount(double amount) { this.amount = amount; }
        public double getPercentageOfTotal() { return percentageOfTotal; }
        public void setPercentageOfTotal(double percentageOfTotal) { this.percentageOfTotal = percentageOfTotal; }
        public List<AssetItem> getItems() { return items; }
        public void setItems(List<AssetItem> items) { this.items = items; }

        public static class AssetCategoryBuilder {
            private String category;
            private double amount;
            private double percentageOfTotal;
            private List<AssetItem> items = new ArrayList<>();

            public AssetCategoryBuilder category(String category) { this.category = category; return this; }
            public AssetCategoryBuilder amount(double amount) { this.amount = amount; return this; }
            public AssetCategoryBuilder percentageOfTotal(double percentageOfTotal) { this.percentageOfTotal = percentageOfTotal; return this; }
            public AssetCategoryBuilder items(List<AssetItem> items) { this.items = items; return this; }
            public AssetCategory build() {
                return new AssetCategory(category, amount, percentageOfTotal, items);
            }
        }
    }

    public static class LiabilityItem {
        private String name;
        private String description;
        private double amount;
        private LocalDate dueDate;

        public LiabilityItem() {}
        public LiabilityItem(String name, String description, double amount, LocalDate dueDate) {
            this.name = name; this.description = description; this.amount = amount; this.dueDate = dueDate;
        }

        public static LiabilityItemBuilder builder() { return new LiabilityItemBuilder(); }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public double getAmount() { return amount; }
        public void setAmount(double amount) { this.amount = amount; }
        public LocalDate getDueDate() { return dueDate; }
        public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

        public static class LiabilityItemBuilder {
            private String name;
            private String description;
            private double amount;
            private LocalDate dueDate;

            public LiabilityItemBuilder name(String name) { this.name = name; return this; }
            public LiabilityItemBuilder description(String description) { this.description = description; return this; }
            public LiabilityItemBuilder amount(double amount) { this.amount = amount; return this; }
            public LiabilityItemBuilder dueDate(LocalDate dueDate) { this.dueDate = dueDate; return this; }
            public LiabilityItem build() {
                return new LiabilityItem(name, description, amount, dueDate);
            }
        }
    }

    public static class LiabilityCategory {
        private String category;
        private double amount;
        private double percentageOfTotal;
        private List<LiabilityItem> items = new ArrayList<>();

        public LiabilityCategory() {}
        public LiabilityCategory(String category, double amount, double percentageOfTotal, List<LiabilityItem> items) {
            this.category = category; this.amount = amount; this.percentageOfTotal = percentageOfTotal; this.items = items != null ? items : new ArrayList<>();
        }

        public static LiabilityCategoryBuilder builder() { return new LiabilityCategoryBuilder(); }

        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public double getAmount() { return amount; }
        public void setAmount(double amount) { this.amount = amount; }
        public double getPercentageOfTotal() { return percentageOfTotal; }
        public void setPercentageOfTotal(double percentageOfTotal) { this.percentageOfTotal = percentageOfTotal; }
        public List<LiabilityItem> getItems() { return items; }
        public void setItems(List<LiabilityItem> items) { this.items = items; }

        public static class LiabilityCategoryBuilder {
            private String category;
            private double amount;
            private double percentageOfTotal;
            private List<LiabilityItem> items = new ArrayList<>();

            public LiabilityCategoryBuilder category(String category) { this.category = category; return this; }
            public LiabilityCategoryBuilder amount(double amount) { this.amount = amount; return this; }
            public LiabilityCategoryBuilder percentageOfTotal(double percentageOfTotal) { this.percentageOfTotal = percentageOfTotal; return this; }
            public LiabilityCategoryBuilder items(List<LiabilityItem> items) { this.items = items; return this; }
            public LiabilityCategory build() {
                return new LiabilityCategory(category, amount, percentageOfTotal, items);
            }
        }
    }

    public static class EquityItem {
        private String name;
        private String description;
        private double amount;

        public EquityItem() {}
        public EquityItem(String name, String description, double amount) {
            this.name = name; this.description = description; this.amount = amount;
        }

        public static EquityItemBuilder builder() { return new EquityItemBuilder(); }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public double getAmount() { return amount; }
        public void setAmount(double amount) { this.amount = amount; }

        public static class EquityItemBuilder {
            private String name;
            private String description;
            private double amount;

            public EquityItemBuilder name(String name) { this.name = name; return this; }
            public EquityItemBuilder description(String description) { this.description = description; return this; }
            public EquityItemBuilder amount(double amount) { this.amount = amount; return this; }
            public EquityItem build() {
                return new EquityItem(name, description, amount);
            }
        }
    }
}
