/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.schoolaccount.SchoolAccount.model.ProfitAndLossStatement
 *  com.schoolaccount.SchoolAccount.model.ProfitAndLossStatement$ExpenseCategory
 *  com.schoolaccount.SchoolAccount.model.ProfitAndLossStatement$ProfitAndLossStatementBuilder
 *  com.schoolaccount.SchoolAccount.model.ProfitAndLossStatement$RevenueCategory
 *  lombok.Generated
 *  org.springframework.data.annotation.Id
 *  org.springframework.data.mongodb.core.mapping.Document
 */
package com.schoolaccount.SchoolAccount.model;

import com.schoolaccount.SchoolAccount.model.ProfitAndLossStatement;
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
@Document(collection="profit_loss_statements")
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
    private List<RevenueCategory> revenueCategories;
    private List<ExpenseCategory> expenseCategories;
    private Map<String, Double> previousPeriodComparison;

    @Generated
    private static List<RevenueCategory> $default$revenueCategories() {
        return new ArrayList<RevenueCategory>();
    }

    @Generated
    private static List<ExpenseCategory> $default$expenseCategories() {
        return new ArrayList<ExpenseCategory>();
    }

    @Generated
    private static Map<String, Double> $default$previousPeriodComparison() {
        return new HashMap<String, Double>();
    }

    @Generated
    public static ProfitAndLossStatementBuilder builder() {
        return new ProfitAndLossStatementBuilder();
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
    public String getSchoolName() {
        return this.schoolName;
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
    public double getTotalRevenue() {
        return this.totalRevenue;
    }

    @Generated
    public double getTotalExpenses() {
        return this.totalExpenses;
    }

    @Generated
    public double getNetProfit() {
        return this.netProfit;
    }

    @Generated
    public List<RevenueCategory> getRevenueCategories() {
        return this.revenueCategories;
    }

    @Generated
    public List<ExpenseCategory> getExpenseCategories() {
        return this.expenseCategories;
    }

    @Generated
    public Map<String, Double> getPreviousPeriodComparison() {
        return this.previousPeriodComparison;
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
    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
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
    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    @Generated
    public void setTotalExpenses(double totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    @Generated
    public void setNetProfit(double netProfit) {
        this.netProfit = netProfit;
    }

    @Generated
    public void setRevenueCategories(List<RevenueCategory> revenueCategories) {
        this.revenueCategories = revenueCategories;
    }

    @Generated
    public void setExpenseCategories(List<ExpenseCategory> expenseCategories) {
        this.expenseCategories = expenseCategories;
    }

    @Generated
    public void setPreviousPeriodComparison(Map<String, Double> previousPeriodComparison) {
        this.previousPeriodComparison = previousPeriodComparison;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ProfitAndLossStatement)) {
            return false;
        }
        ProfitAndLossStatement other = (ProfitAndLossStatement)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        if (Double.compare(this.getTotalRevenue(), other.getTotalRevenue()) != 0) {
            return false;
        }
        if (Double.compare(this.getTotalExpenses(), other.getTotalExpenses()) != 0) {
            return false;
        }
        if (Double.compare(this.getNetProfit(), other.getNetProfit()) != 0) {
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
        String this$schoolName = this.getSchoolName();
        String other$schoolName = other.getSchoolName();
        if (this$schoolName == null ? other$schoolName != null : !this$schoolName.equals(other$schoolName)) {
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
        List this$revenueCategories = this.getRevenueCategories();
        List other$revenueCategories = other.getRevenueCategories();
        if (this$revenueCategories == null ? other$revenueCategories != null : !((Object)this$revenueCategories).equals(other$revenueCategories)) {
            return false;
        }
        List this$expenseCategories = this.getExpenseCategories();
        List other$expenseCategories = other.getExpenseCategories();
        if (this$expenseCategories == null ? other$expenseCategories != null : !((Object)this$expenseCategories).equals(other$expenseCategories)) {
            return false;
        }
        Map this$previousPeriodComparison = this.getPreviousPeriodComparison();
        Map other$previousPeriodComparison = other.getPreviousPeriodComparison();
        return !(this$previousPeriodComparison == null ? other$previousPeriodComparison != null : !((Object)this$previousPeriodComparison).equals(other$previousPeriodComparison));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof ProfitAndLossStatement;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        long $totalRevenue = Double.doubleToLongBits(this.getTotalRevenue());
        result = result * 59 + (int)($totalRevenue >>> 32 ^ $totalRevenue);
        long $totalExpenses = Double.doubleToLongBits(this.getTotalExpenses());
        result = result * 59 + (int)($totalExpenses >>> 32 ^ $totalExpenses);
        long $netProfit = Double.doubleToLongBits(this.getNetProfit());
        result = result * 59 + (int)($netProfit >>> 32 ^ $netProfit);
        String $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        String $schoolId = this.getSchoolId();
        result = result * 59 + ($schoolId == null ? 43 : $schoolId.hashCode());
        String $schoolName = this.getSchoolName();
        result = result * 59 + ($schoolName == null ? 43 : $schoolName.hashCode());
        LocalDate $periodStart = this.getPeriodStart();
        result = result * 59 + ($periodStart == null ? 43 : ((Object)$periodStart).hashCode());
        LocalDate $periodEnd = this.getPeriodEnd();
        result = result * 59 + ($periodEnd == null ? 43 : ((Object)$periodEnd).hashCode());
        LocalDateTime $generatedAt = this.getGeneratedAt();
        result = result * 59 + ($generatedAt == null ? 43 : ((Object)$generatedAt).hashCode());
        String $generatedBy = this.getGeneratedBy();
        result = result * 59 + ($generatedBy == null ? 43 : $generatedBy.hashCode());
        List $revenueCategories = this.getRevenueCategories();
        result = result * 59 + ($revenueCategories == null ? 43 : ((Object)$revenueCategories).hashCode());
        List $expenseCategories = this.getExpenseCategories();
        result = result * 59 + ($expenseCategories == null ? 43 : ((Object)$expenseCategories).hashCode());
        Map $previousPeriodComparison = this.getPreviousPeriodComparison();
        result = result * 59 + ($previousPeriodComparison == null ? 43 : ((Object)$previousPeriodComparison).hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "ProfitAndLossStatement(id=" + this.getId() + ", schoolId=" + this.getSchoolId() + ", schoolName=" + this.getSchoolName() + ", periodStart=" + this.getPeriodStart() + ", periodEnd=" + this.getPeriodEnd() + ", generatedAt=" + this.getGeneratedAt() + ", generatedBy=" + this.getGeneratedBy() + ", totalRevenue=" + this.getTotalRevenue() + ", totalExpenses=" + this.getTotalExpenses() + ", netProfit=" + this.getNetProfit() + ", revenueCategories=" + this.getRevenueCategories() + ", expenseCategories=" + this.getExpenseCategories() + ", previousPeriodComparison=" + this.getPreviousPeriodComparison() + ")";
    }

    @Generated
    public ProfitAndLossStatement() {
        this.revenueCategories = ProfitAndLossStatement.$default$revenueCategories();
        this.expenseCategories = ProfitAndLossStatement.$default$expenseCategories();
        this.previousPeriodComparison = ProfitAndLossStatement.$default$previousPeriodComparison();
    }

    @Generated
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
        this.revenueCategories = revenueCategories;
        this.expenseCategories = expenseCategories;
        this.previousPeriodComparison = previousPeriodComparison;
    }
}

