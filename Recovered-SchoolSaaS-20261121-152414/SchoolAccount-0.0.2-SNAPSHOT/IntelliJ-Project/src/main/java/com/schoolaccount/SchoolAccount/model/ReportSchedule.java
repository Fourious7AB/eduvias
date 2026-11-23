package com.schoolaccount.SchoolAccount.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "report_schedules")
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
    private List<String> recipients = new ArrayList<>();
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime lastRunAt;
    private LocalDateTime nextRunAt;
    private boolean active;

    public ReportSchedule() {}

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
        this.recipients = recipients != null ? recipients : new ArrayList<>();
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.lastRunAt = lastRunAt;
        this.nextRunAt = nextRunAt;
        this.active = active;
    }

    public static ReportScheduleBuilder builder() {
        return new ReportScheduleBuilder();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getSchoolId() { return schoolId; }
    public void setSchoolId(String schoolId) { this.schoolId = schoolId; }
    public String getReportType() { return reportType; }
    public void setReportType(String reportType) { this.reportType = reportType; }
    public String getFrequency() { return frequency; }
    public void setFrequency(String frequency) { this.frequency = frequency; }
    public String getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(String dayOfWeek) { this.dayOfWeek = dayOfWeek; }
    public Integer getDayOfMonth() { return dayOfMonth; }
    public void setDayOfMonth(Integer dayOfMonth) { this.dayOfMonth = dayOfMonth; }
    public Integer getMonth() { return month; }
    public void setMonth(Integer month) { this.month = month; }
    public Integer getQuarter() { return quarter; }
    public void setQuarter(Integer quarter) { this.quarter = quarter; }
    public String getFormat() { return format; }
    public void setFormat(String format) { this.format = format; }
    public List<String> getRecipients() { return recipients; }
    public void setRecipients(List<String> recipients) { this.recipients = recipients; }
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getLastRunAt() { return lastRunAt; }
    public void setLastRunAt(LocalDateTime lastRunAt) { this.lastRunAt = lastRunAt; }
    public LocalDateTime getNextRunAt() { return nextRunAt; }
    public void setNextRunAt(LocalDateTime nextRunAt) { this.nextRunAt = nextRunAt; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public static class ReportScheduleBuilder {
        private String id;
        private String schoolId;
        private String reportType;
        private String frequency;
        private String dayOfWeek;
        private Integer dayOfMonth;
        private Integer month;
        private Integer quarter;
        private String format;
        private List<String> recipients = new ArrayList<>();
        private String createdBy;
        private LocalDateTime createdAt;
        private LocalDateTime lastRunAt;
        private LocalDateTime nextRunAt;
        private boolean active;

        public ReportScheduleBuilder id(String id) { this.id = id; return this; }
        public ReportScheduleBuilder schoolId(String schoolId) { this.schoolId = schoolId; return this; }
        public ReportScheduleBuilder reportType(String reportType) { this.reportType = reportType; return this; }
        public ReportScheduleBuilder frequency(String frequency) { this.frequency = frequency; return this; }
        public ReportScheduleBuilder dayOfWeek(String dayOfWeek) { this.dayOfWeek = dayOfWeek; return this; }
        public ReportScheduleBuilder dayOfMonth(Integer dayOfMonth) { this.dayOfMonth = dayOfMonth; return this; }
        public ReportScheduleBuilder month(Integer month) { this.month = month; return this; }
        public ReportScheduleBuilder quarter(Integer quarter) { this.quarter = quarter; return this; }
        public ReportScheduleBuilder format(String format) { this.format = format; return this; }
        public ReportScheduleBuilder recipients(List<String> recipients) { this.recipients = recipients; return this; }
        public ReportScheduleBuilder createdBy(String createdBy) { this.createdBy = createdBy; return this; }
        public ReportScheduleBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public ReportScheduleBuilder lastRunAt(LocalDateTime lastRunAt) { this.lastRunAt = lastRunAt; return this; }
        public ReportScheduleBuilder nextRunAt(LocalDateTime nextRunAt) { this.nextRunAt = nextRunAt; return this; }
        public ReportScheduleBuilder active(boolean active) { this.active = active; return this; }

        public ReportSchedule build() {
            return new ReportSchedule(id, schoolId, reportType, frequency, dayOfWeek, dayOfMonth, month, quarter, format, recipients, createdBy, createdAt, lastRunAt, nextRunAt, active);
        }
    }
}
