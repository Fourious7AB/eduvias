/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.schoolaccount.SchoolAccount.model.ReportSchedule
 *  com.schoolaccount.SchoolAccount.model.ReportSchedule$ReportScheduleBuilder
 *  lombok.Generated
 *  org.springframework.data.annotation.Id
 *  org.springframework.data.mongodb.core.mapping.Document
 */
package com.schoolaccount.SchoolAccount.model;

import com.schoolaccount.SchoolAccount.model.ReportSchedule;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/*
 * Exception performing whole class analysis ignored.
 */
@Document(collection="report_schedules")
public class ReportSchedule {
    @Id
    private String id;
    private String schoolId;
    private String reportType;
    private String frequency;
    private String dayOfWeek;
    private Integer dayOfMonth;
    private Integer month;
    private Integer quarter;
    private String format;
    private List<String> recipients;
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime lastRunAt;
    private LocalDateTime nextRunAt;
    private boolean active;

    @Generated
    private static List<String> $default$recipients() {
        return new ArrayList<String>();
    }

    @Generated
    public static ReportScheduleBuilder builder() {
        return new ReportScheduleBuilder();
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
    public String getFrequency() {
        return this.frequency;
    }

    @Generated
    public String getDayOfWeek() {
        return this.dayOfWeek;
    }

    @Generated
    public Integer getDayOfMonth() {
        return this.dayOfMonth;
    }

    @Generated
    public Integer getMonth() {
        return this.month;
    }

    @Generated
    public Integer getQuarter() {
        return this.quarter;
    }

    @Generated
    public String getFormat() {
        return this.format;
    }

    @Generated
    public List<String> getRecipients() {
        return this.recipients;
    }

    @Generated
    public String getCreatedBy() {
        return this.createdBy;
    }

    @Generated
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    @Generated
    public LocalDateTime getLastRunAt() {
        return this.lastRunAt;
    }

    @Generated
    public LocalDateTime getNextRunAt() {
        return this.nextRunAt;
    }

    @Generated
    public boolean isActive() {
        return this.active;
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
    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    @Generated
    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    @Generated
    public void setDayOfMonth(Integer dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    @Generated
    public void setMonth(Integer month) {
        this.month = month;
    }

    @Generated
    public void setQuarter(Integer quarter) {
        this.quarter = quarter;
    }

    @Generated
    public void setFormat(String format) {
        this.format = format;
    }

    @Generated
    public void setRecipients(List<String> recipients) {
        this.recipients = recipients;
    }

    @Generated
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Generated
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Generated
    public void setLastRunAt(LocalDateTime lastRunAt) {
        this.lastRunAt = lastRunAt;
    }

    @Generated
    public void setNextRunAt(LocalDateTime nextRunAt) {
        this.nextRunAt = nextRunAt;
    }

    @Generated
    public void setActive(boolean active) {
        this.active = active;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReportSchedule)) {
            return false;
        }
        ReportSchedule other = (ReportSchedule)o;
        if (!other.canEqual((Object)this)) {
            return false;
        }
        if (this.isActive() != other.isActive()) {
            return false;
        }
        Integer this$dayOfMonth = this.getDayOfMonth();
        Integer other$dayOfMonth = other.getDayOfMonth();
        if (this$dayOfMonth == null ? other$dayOfMonth != null : !((Object)this$dayOfMonth).equals(other$dayOfMonth)) {
            return false;
        }
        Integer this$month = this.getMonth();
        Integer other$month = other.getMonth();
        if (this$month == null ? other$month != null : !((Object)this$month).equals(other$month)) {
            return false;
        }
        Integer this$quarter = this.getQuarter();
        Integer other$quarter = other.getQuarter();
        if (this$quarter == null ? other$quarter != null : !((Object)this$quarter).equals(other$quarter)) {
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
        String this$frequency = this.getFrequency();
        String other$frequency = other.getFrequency();
        if (this$frequency == null ? other$frequency != null : !this$frequency.equals(other$frequency)) {
            return false;
        }
        String this$dayOfWeek = this.getDayOfWeek();
        String other$dayOfWeek = other.getDayOfWeek();
        if (this$dayOfWeek == null ? other$dayOfWeek != null : !this$dayOfWeek.equals(other$dayOfWeek)) {
            return false;
        }
        String this$format = this.getFormat();
        String other$format = other.getFormat();
        if (this$format == null ? other$format != null : !this$format.equals(other$format)) {
            return false;
        }
        List this$recipients = this.getRecipients();
        List other$recipients = other.getRecipients();
        if (this$recipients == null ? other$recipients != null : !((Object)this$recipients).equals(other$recipients)) {
            return false;
        }
        String this$createdBy = this.getCreatedBy();
        String other$createdBy = other.getCreatedBy();
        if (this$createdBy == null ? other$createdBy != null : !this$createdBy.equals(other$createdBy)) {
            return false;
        }
        LocalDateTime this$createdAt = this.getCreatedAt();
        LocalDateTime other$createdAt = other.getCreatedAt();
        if (this$createdAt == null ? other$createdAt != null : !((Object)this$createdAt).equals(other$createdAt)) {
            return false;
        }
        LocalDateTime this$lastRunAt = this.getLastRunAt();
        LocalDateTime other$lastRunAt = other.getLastRunAt();
        if (this$lastRunAt == null ? other$lastRunAt != null : !((Object)this$lastRunAt).equals(other$lastRunAt)) {
            return false;
        }
        LocalDateTime this$nextRunAt = this.getNextRunAt();
        LocalDateTime other$nextRunAt = other.getNextRunAt();
        return !(this$nextRunAt == null ? other$nextRunAt != null : !((Object)this$nextRunAt).equals(other$nextRunAt));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof ReportSchedule;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + (this.isActive() ? 79 : 97);
        Integer $dayOfMonth = this.getDayOfMonth();
        result = result * 59 + ($dayOfMonth == null ? 43 : ((Object)$dayOfMonth).hashCode());
        Integer $month = this.getMonth();
        result = result * 59 + ($month == null ? 43 : ((Object)$month).hashCode());
        Integer $quarter = this.getQuarter();
        result = result * 59 + ($quarter == null ? 43 : ((Object)$quarter).hashCode());
        String $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        String $schoolId = this.getSchoolId();
        result = result * 59 + ($schoolId == null ? 43 : $schoolId.hashCode());
        String $reportType = this.getReportType();
        result = result * 59 + ($reportType == null ? 43 : $reportType.hashCode());
        String $frequency = this.getFrequency();
        result = result * 59 + ($frequency == null ? 43 : $frequency.hashCode());
        String $dayOfWeek = this.getDayOfWeek();
        result = result * 59 + ($dayOfWeek == null ? 43 : $dayOfWeek.hashCode());
        String $format = this.getFormat();
        result = result * 59 + ($format == null ? 43 : $format.hashCode());
        List $recipients = this.getRecipients();
        result = result * 59 + ($recipients == null ? 43 : ((Object)$recipients).hashCode());
        String $createdBy = this.getCreatedBy();
        result = result * 59 + ($createdBy == null ? 43 : $createdBy.hashCode());
        LocalDateTime $createdAt = this.getCreatedAt();
        result = result * 59 + ($createdAt == null ? 43 : ((Object)$createdAt).hashCode());
        LocalDateTime $lastRunAt = this.getLastRunAt();
        result = result * 59 + ($lastRunAt == null ? 43 : ((Object)$lastRunAt).hashCode());
        LocalDateTime $nextRunAt = this.getNextRunAt();
        result = result * 59 + ($nextRunAt == null ? 43 : ((Object)$nextRunAt).hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "ReportSchedule(id=" + this.getId() + ", schoolId=" + this.getSchoolId() + ", reportType=" + this.getReportType() + ", frequency=" + this.getFrequency() + ", dayOfWeek=" + this.getDayOfWeek() + ", dayOfMonth=" + this.getDayOfMonth() + ", month=" + this.getMonth() + ", quarter=" + this.getQuarter() + ", format=" + this.getFormat() + ", recipients=" + this.getRecipients() + ", createdBy=" + this.getCreatedBy() + ", createdAt=" + this.getCreatedAt() + ", lastRunAt=" + this.getLastRunAt() + ", nextRunAt=" + this.getNextRunAt() + ", active=" + this.isActive() + ")";
    }

    @Generated
    public ReportSchedule() {
        this.recipients = ReportSchedule.$default$recipients();
    }

    @Generated
    public ReportSchedule(String id, String schoolId, String reportType, String frequency, String dayOfWeek, Integer dayOfMonth, Integer month, Integer quarter, String format, List<String> recipients, String createdBy, LocalDateTime createdAt, LocalDateTime lastRunAt, LocalDateTime nextRunAt, boolean active) {
        this.id = id;
        this.schoolId = schoolId;
        this.reportType = reportType;
        this.frequency = frequency;
        this.dayOfWeek = dayOfWeek;
        this.dayOfMonth = dayOfMonth;
        this.month = month;
        this.quarter = quarter;
        this.format = format;
        this.recipients = recipients;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.lastRunAt = lastRunAt;
        this.nextRunAt = nextRunAt;
        this.active = active;
    }
}

