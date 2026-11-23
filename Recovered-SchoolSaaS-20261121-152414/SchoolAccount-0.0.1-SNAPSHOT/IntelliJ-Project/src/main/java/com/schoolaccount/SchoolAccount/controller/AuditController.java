/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.schoolaccount.SchoolAccount.controller.AuditController
 *  com.schoolaccount.SchoolAccount.model.AuditLog
 *  com.schoolaccount.SchoolAccount.model.FinancialDiscrepancy
 *  com.schoolaccount.SchoolAccount.model.ResponseMessage
 *  com.schoolaccount.SchoolAccount.service.AuditService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.data.domain.PageRequest
 *  org.springframework.data.domain.Pageable
 *  org.springframework.data.domain.Sort
 *  org.springframework.format.annotation.DateTimeFormat
 *  org.springframework.format.annotation.DateTimeFormat$ISO
 *  org.springframework.http.MediaType
 *  org.springframework.http.ResponseEntity
 *  org.springframework.http.ResponseEntity$BodyBuilder
 *  org.springframework.web.bind.annotation.CrossOrigin
 *  org.springframework.web.bind.annotation.DeleteMapping
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.PutMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.RestController
 */
package com.schoolaccount.SchoolAccount.controller;

import com.schoolaccount.SchoolAccount.model.AuditLog;
import com.schoolaccount.SchoolAccount.model.FinancialDiscrepancy;
import com.schoolaccount.SchoolAccount.model.ResponseMessage;
import com.schoolaccount.SchoolAccount.service.AuditService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins={"*"})
@RequestMapping(value={"/audit"})
public class AuditController {
    @Autowired
    private AuditService auditService;

    @PostMapping(value={"/logs"})
    public ResponseEntity<ResponseMessage> createAuditLog(@RequestBody AuditLog auditLog) {
        return ResponseEntity.ok((Object)this.auditService.createAuditLog(auditLog));
    }

    @GetMapping(value={"/logs/school/{schoolId}"})
    public ResponseEntity<ResponseMessage> getAuditLogs(@PathVariable String schoolId, @RequestParam(defaultValue="0") int page, @RequestParam(defaultValue="20") int size) {
        PageRequest pageable = PageRequest.of((int)page, (int)size, (Sort)Sort.by((String[])new String[]{"timestamp"}).descending());
        return ResponseEntity.ok((Object)this.auditService.getAuditLogs(schoolId, (Pageable)pageable));
    }

    @GetMapping(value={"/logs/school/{schoolId}/type/{entityType}"})
    public ResponseEntity<ResponseMessage> getAuditLogsByEntityType(@PathVariable String schoolId, @PathVariable String entityType, @RequestParam(defaultValue="0") int page, @RequestParam(defaultValue="20") int size) {
        PageRequest pageable = PageRequest.of((int)page, (int)size, (Sort)Sort.by((String[])new String[]{"timestamp"}).descending());
        return ResponseEntity.ok((Object)this.auditService.getAuditLogsByEntityType(schoolId, entityType, (Pageable)pageable));
    }

    @GetMapping(value={"/logs/school/{schoolId}/daterange"})
    public ResponseEntity<ResponseMessage> getAuditLogsByDateRange(@PathVariable String schoolId, @RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate, @RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok((Object)this.auditService.getAuditLogsByDateRange(schoolId, startDate, endDate));
    }

    @PostMapping(value={"/discrepancies"})
    public ResponseEntity<ResponseMessage> createDiscrepancy(@RequestBody FinancialDiscrepancy discrepancy) {
        return ResponseEntity.ok((Object)this.auditService.createDiscrepancy(discrepancy));
    }

    @GetMapping(value={"/discrepancies/school/{schoolId}"})
    public ResponseEntity<ResponseMessage> getDiscrepancies(@PathVariable String schoolId, @RequestParam(defaultValue="0") int page, @RequestParam(defaultValue="20") int size) {
        PageRequest pageable = PageRequest.of((int)page, (int)size, (Sort)Sort.by((String[])new String[]{"detectedAt"}).descending());
        return ResponseEntity.ok((Object)this.auditService.getDiscrepancies(schoolId, (Pageable)pageable));
    }

    @GetMapping(value={"/discrepancies/school/{schoolId}/status/{status}"})
    public ResponseEntity<ResponseMessage> getDiscrepanciesByStatus(@PathVariable String schoolId, @PathVariable String status) {
        return ResponseEntity.ok((Object)this.auditService.getDiscrepanciesByStatus(schoolId, status));
    }

    @GetMapping(value={"/discrepancies/school/{schoolId}/type/{discrepancyType}"})
    public ResponseEntity<ResponseMessage> getDiscrepanciesByType(@PathVariable String schoolId, @PathVariable String discrepancyType) {
        return ResponseEntity.ok((Object)this.auditService.getDiscrepanciesByType(schoolId, discrepancyType));
    }

    @PutMapping(value={"/discrepancies/{discrepancyId}/status"})
    public ResponseEntity<ResponseMessage> updateDiscrepancyStatus(@PathVariable String discrepancyId, @RequestBody Map<String, String> statusUpdate) {
        String status = statusUpdate.get("status");
        String resolvedBy = statusUpdate.get("resolvedBy");
        String resolutionNotes = statusUpdate.get("resolutionNotes");
        return ResponseEntity.ok((Object)this.auditService.updateDiscrepancyStatus(discrepancyId, status, resolvedBy, resolutionNotes));
    }

    @GetMapping(value={"/reports/school/{schoolId}"})
    public ResponseEntity<ResponseMessage> getAuditReports(@PathVariable String schoolId, @RequestParam(defaultValue="0") int page, @RequestParam(defaultValue="20") int size) {
        PageRequest pageable = PageRequest.of((int)page, (int)size, (Sort)Sort.by((String[])new String[]{"generatedAt"}).descending());
        return ResponseEntity.ok((Object)this.auditService.getAuditReports(schoolId, (Pageable)pageable));
    }

    @GetMapping(value={"/reports/school/{schoolId}/monthly/{year}/{month}"})
    public ResponseEntity<ResponseMessage> generateMonthlyReport(@PathVariable String schoolId, @PathVariable int year, @PathVariable int month) {
        return ResponseEntity.ok((Object)this.auditService.generateMonthlyAuditReport(schoolId, year, month));
    }

    @GetMapping(value={"/reports/school/{schoolId}/quarterly/{year}/{quarter}"})
    public ResponseEntity<ResponseMessage> generateQuarterlyReport(@PathVariable String schoolId, @PathVariable int year, @PathVariable int quarter) {
        return ResponseEntity.ok((Object)this.auditService.generateQuarterlyAuditReport(schoolId, year, quarter));
    }

    @GetMapping(value={"/reports/school/{schoolId}/yearly/{year}"})
    public ResponseEntity<ResponseMessage> generateYearlyReport(@PathVariable String schoolId, @PathVariable int year) {
        return ResponseEntity.ok((Object)this.auditService.generateYearlyAuditReport(schoolId, year));
    }

    @GetMapping(value={"/reports/school/{schoolId}/custom"})
    public ResponseEntity<ResponseMessage> generateCustomReport(@PathVariable String schoolId, @RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate startDate, @RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate endDate, @RequestParam String reportName) {
        return ResponseEntity.ok((Object)this.auditService.generateCustomAuditReport(schoolId, reportName, startDate, endDate));
    }

    @GetMapping(value={"/statements/pl/school/{schoolId}"})
    public ResponseEntity<ResponseMessage> getProfitAndLossStatement(@PathVariable String schoolId, @RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate startDate, @RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok((Object)this.auditService.generateProfitAndLossStatement(schoolId, startDate, endDate));
    }

    @GetMapping(value={"/statements/balance-sheet/school/{schoolId}"})
    public ResponseEntity<ResponseMessage> getBalanceSheet(@PathVariable String schoolId, @RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate asOfDate) {
        return ResponseEntity.ok((Object)this.auditService.generateBalanceSheet(schoolId, asOfDate));
    }

    @GetMapping(value={"/export/pdf/pl/school/{schoolId}"})
    public ResponseEntity<byte[]> exportProfitAndLossStatementPdf(@PathVariable String schoolId, @RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate startDate, @RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate endDate) {
        byte[] pdfBytes = this.auditService.exportProfitAndLossStatementPdf(schoolId, startDate, endDate);
        return ((ResponseEntity.BodyBuilder)ResponseEntity.ok().header("Content-Disposition", new String[]{"attachment; filename=pl_statement.pdf"})).contentType(MediaType.APPLICATION_PDF).body((Object)pdfBytes);
    }

    @GetMapping(value={"/export/pdf/balance-sheet/school/{schoolId}"})
    public ResponseEntity<byte[]> exportBalanceSheetPdf(@PathVariable String schoolId, @RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate asOfDate) {
        byte[] pdfBytes = this.auditService.exportBalanceSheetPdf(schoolId, asOfDate);
        return ((ResponseEntity.BodyBuilder)ResponseEntity.ok().header("Content-Disposition", new String[]{"attachment; filename=balance_sheet.pdf"})).contentType(MediaType.APPLICATION_PDF).body((Object)pdfBytes);
    }

    @GetMapping(value={"/export/excel/pl/school/{schoolId}"})
    public ResponseEntity<byte[]> exportProfitAndLossStatementExcel(@PathVariable String schoolId, @RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate startDate, @RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate endDate) {
        byte[] excelBytes = this.auditService.exportProfitAndLossStatementExcel(schoolId, startDate, endDate);
        return ((ResponseEntity.BodyBuilder)ResponseEntity.ok().header("Content-Disposition", new String[]{"attachment; filename=pl_statement.xlsx"})).contentType(MediaType.parseMediaType((String)"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")).body((Object)excelBytes);
    }

    @GetMapping(value={"/export/excel/balance-sheet/school/{schoolId}"})
    public ResponseEntity<byte[]> exportBalanceSheetExcel(@PathVariable String schoolId, @RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate asOfDate) {
        byte[] excelBytes = this.auditService.exportBalanceSheetExcel(schoolId, asOfDate);
        return ((ResponseEntity.BodyBuilder)ResponseEntity.ok().header("Content-Disposition", new String[]{"attachment; filename=balance_sheet.xlsx"})).contentType(MediaType.parseMediaType((String)"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")).body((Object)excelBytes);
    }

    @PostMapping(value={"/schedule/report/{schoolId}"})
    public ResponseEntity<ResponseMessage> scheduleReport(@PathVariable String schoolId, @RequestBody Map<String, Object> scheduleConfig) {
        return ResponseEntity.ok((Object)this.auditService.scheduleReport(schoolId, scheduleConfig));
    }

    @GetMapping(value={"/schedule/report/{schoolId}"})
    public ResponseEntity<ResponseMessage> getScheduledReports(@PathVariable String schoolId) {
        return ResponseEntity.ok((Object)this.auditService.getScheduledReports(schoolId));
    }

    @DeleteMapping(value={"/schedule/report/{scheduleId}"})
    public ResponseEntity<ResponseMessage> deleteScheduledReport(@PathVariable String scheduleId) {
        return ResponseEntity.ok((Object)this.auditService.deleteScheduledReport(scheduleId));
    }
}

