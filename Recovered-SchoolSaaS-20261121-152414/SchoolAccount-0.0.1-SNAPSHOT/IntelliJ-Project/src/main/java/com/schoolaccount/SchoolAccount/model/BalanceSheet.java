/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.schoolaccount.SchoolAccount.model.BalanceSheet
 *  com.schoolaccount.SchoolAccount.model.BalanceSheet$AssetCategory
 *  com.schoolaccount.SchoolAccount.model.BalanceSheet$BalanceSheetBuilder
 *  com.schoolaccount.SchoolAccount.model.BalanceSheet$EquityItem
 *  com.schoolaccount.SchoolAccount.model.BalanceSheet$LiabilityCategory
 *  lombok.Generated
 *  org.springframework.data.annotation.Id
 *  org.springframework.data.mongodb.core.mapping.Document
 */
package com.schoolaccount.SchoolAccount.model;

import com.schoolaccount.SchoolAccount.model.BalanceSheet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/*
 * Exception performing whole class analysis ignored.
 */
@Document(collection="balance_sheets")
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
    private List<AssetCategory> assetCategories;
    private List<LiabilityCategory> liabilityCategories;
    private List<EquityItem> equityItems;

    @Generated
    private static List<AssetCategory> $default$assetCategories() {
        return new ArrayList<AssetCategory>();
    }

    @Generated
    private static List<LiabilityCategory> $default$liabilityCategories() {
        return new ArrayList<LiabilityCategory>();
    }

    @Generated
    private static List<EquityItem> $default$equityItems() {
        return new ArrayList<EquityItem>();
    }

    @Generated
    public static BalanceSheetBuilder builder() {
        return new BalanceSheetBuilder();
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
    public LocalDate getAsOfDate() {
        return this.asOfDate;
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
    public double getTotalAssets() {
        return this.totalAssets;
    }

    @Generated
    public double getTotalLiabilities() {
        return this.totalLiabilities;
    }

    @Generated
    public double getTotalEquity() {
        return this.totalEquity;
    }

    @Generated
    public List<AssetCategory> getAssetCategories() {
        return this.assetCategories;
    }

    @Generated
    public List<LiabilityCategory> getLiabilityCategories() {
        return this.liabilityCategories;
    }

    @Generated
    public List<EquityItem> getEquityItems() {
        return this.equityItems;
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
    public void setAsOfDate(LocalDate asOfDate) {
        this.asOfDate = asOfDate;
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
    public void setTotalAssets(double totalAssets) {
        this.totalAssets = totalAssets;
    }

    @Generated
    public void setTotalLiabilities(double totalLiabilities) {
        this.totalLiabilities = totalLiabilities;
    }

    @Generated
    public void setTotalEquity(double totalEquity) {
        this.totalEquity = totalEquity;
    }

    @Generated
    public void setAssetCategories(List<AssetCategory> assetCategories) {
        this.assetCategories = assetCategories;
    }

    @Generated
    public void setLiabilityCategories(List<LiabilityCategory> liabilityCategories) {
        this.liabilityCategories = liabilityCategories;
    }

    @Generated
    public void setEquityItems(List<EquityItem> equityItems) {
        this.equityItems = equityItems;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof BalanceSheet)) {
            return false;
        }
        BalanceSheet other = (BalanceSheet)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        if (Double.compare(this.getTotalAssets(), other.getTotalAssets()) != 0) {
            return false;
        }
        if (Double.compare(this.getTotalLiabilities(), other.getTotalLiabilities()) != 0) {
            return false;
        }
        if (Double.compare(this.getTotalEquity(), other.getTotalEquity()) != 0) {
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
        LocalDate this$asOfDate = this.getAsOfDate();
        LocalDate other$asOfDate = other.getAsOfDate();
        if (this$asOfDate == null ? other$asOfDate != null : !((Object)this$asOfDate).equals(other$asOfDate)) {
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
        List this$assetCategories = this.getAssetCategories();
        List other$assetCategories = other.getAssetCategories();
        if (this$assetCategories == null ? other$assetCategories != null : !((Object)this$assetCategories).equals(other$assetCategories)) {
            return false;
        }
        List this$liabilityCategories = this.getLiabilityCategories();
        List other$liabilityCategories = other.getLiabilityCategories();
        if (this$liabilityCategories == null ? other$liabilityCategories != null : !((Object)this$liabilityCategories).equals(other$liabilityCategories)) {
            return false;
        }
        List this$equityItems = this.getEquityItems();
        List other$equityItems = other.getEquityItems();
        return !(this$equityItems == null ? other$equityItems != null : !((Object)this$equityItems).equals(other$equityItems));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof BalanceSheet;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        long $totalAssets = Double.doubleToLongBits(this.getTotalAssets());
        result = result * 59 + (int)($totalAssets >>> 32 ^ $totalAssets);
        long $totalLiabilities = Double.doubleToLongBits(this.getTotalLiabilities());
        result = result * 59 + (int)($totalLiabilities >>> 32 ^ $totalLiabilities);
        long $totalEquity = Double.doubleToLongBits(this.getTotalEquity());
        result = result * 59 + (int)($totalEquity >>> 32 ^ $totalEquity);
        String $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        String $schoolId = this.getSchoolId();
        result = result * 59 + ($schoolId == null ? 43 : $schoolId.hashCode());
        String $schoolName = this.getSchoolName();
        result = result * 59 + ($schoolName == null ? 43 : $schoolName.hashCode());
        LocalDate $asOfDate = this.getAsOfDate();
        result = result * 59 + ($asOfDate == null ? 43 : ((Object)$asOfDate).hashCode());
        LocalDateTime $generatedAt = this.getGeneratedAt();
        result = result * 59 + ($generatedAt == null ? 43 : ((Object)$generatedAt).hashCode());
        String $generatedBy = this.getGeneratedBy();
        result = result * 59 + ($generatedBy == null ? 43 : $generatedBy.hashCode());
        List $assetCategories = this.getAssetCategories();
        result = result * 59 + ($assetCategories == null ? 43 : ((Object)$assetCategories).hashCode());
        List $liabilityCategories = this.getLiabilityCategories();
        result = result * 59 + ($liabilityCategories == null ? 43 : ((Object)$liabilityCategories).hashCode());
        List $equityItems = this.getEquityItems();
        result = result * 59 + ($equityItems == null ? 43 : ((Object)$equityItems).hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "BalanceSheet(id=" + this.getId() + ", schoolId=" + this.getSchoolId() + ", schoolName=" + this.getSchoolName() + ", asOfDate=" + this.getAsOfDate() + ", generatedAt=" + this.getGeneratedAt() + ", generatedBy=" + this.getGeneratedBy() + ", totalAssets=" + this.getTotalAssets() + ", totalLiabilities=" + this.getTotalLiabilities() + ", totalEquity=" + this.getTotalEquity() + ", assetCategories=" + this.getAssetCategories() + ", liabilityCategories=" + this.getLiabilityCategories() + ", equityItems=" + this.getEquityItems() + ")";
    }

    @Generated
    public BalanceSheet() {
        this.assetCategories = BalanceSheet.$default$assetCategories();
        this.liabilityCategories = BalanceSheet.$default$liabilityCategories();
        this.equityItems = BalanceSheet.$default$equityItems();
    }

    @Generated
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
        this.assetCategories = assetCategories;
        this.liabilityCategories = liabilityCategories;
        this.equityItems = equityItems;
    }
}

