/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.databind.ObjectMapper
 *  com.schoolaccount.SchoolAccount.model.AuditLog
 *  com.schoolaccount.SchoolAccount.model.AuditReport
 *  com.schoolaccount.SchoolAccount.model.BalanceSheet
 *  com.schoolaccount.SchoolAccount.model.BalanceSheet$AssetCategory
 *  com.schoolaccount.SchoolAccount.model.BalanceSheet$AssetItem
 *  com.schoolaccount.SchoolAccount.model.BalanceSheet$EquityItem
 *  com.schoolaccount.SchoolAccount.model.BalanceSheet$LiabilityCategory
 *  com.schoolaccount.SchoolAccount.model.Expense
 *  com.schoolaccount.SchoolAccount.model.FinancialDiscrepancy
 *  com.schoolaccount.SchoolAccount.model.IncomePage
 *  com.schoolaccount.SchoolAccount.model.PaymentTransaction
 *  com.schoolaccount.SchoolAccount.model.ProfitAndLossStatement
 *  com.schoolaccount.SchoolAccount.model.ProfitAndLossStatement$ExpenseCategory
 *  com.schoolaccount.SchoolAccount.model.ProfitAndLossStatement$ExpenseItem
 *  com.schoolaccount.SchoolAccount.model.ProfitAndLossStatement$RevenueCategory
 *  com.schoolaccount.SchoolAccount.model.ProfitAndLossStatement$RevenueItem
 *  com.schoolaccount.SchoolAccount.model.ReportSchedule
 *  com.schoolaccount.SchoolAccount.model.ResponseMessage
 *  com.schoolaccount.SchoolAccount.model.SchoolWallet
 *  com.schoolaccount.SchoolAccount.model.SchoolWallet$SubWallet
 *  com.schoolaccount.SchoolAccount.repo.AuditLogRepository
 *  com.schoolaccount.SchoolAccount.repo.AuditReportRepository
 *  com.schoolaccount.SchoolAccount.repo.BalanceSheetRepository
 *  com.schoolaccount.SchoolAccount.repo.ExpenseRepository
 *  com.schoolaccount.SchoolAccount.repo.FinancialDiscrepancyRepository
 *  com.schoolaccount.SchoolAccount.repo.IncomePageRepository
 *  com.schoolaccount.SchoolAccount.repo.PaymentTransactionRepository
 *  com.schoolaccount.SchoolAccount.repo.PayoutRequestRepository
 *  com.schoolaccount.SchoolAccount.repo.ProfitAndLossStatementRepository
 *  com.schoolaccount.SchoolAccount.repo.ReportScheduleRepository
 *  com.schoolaccount.SchoolAccount.repo.SchoolWalletRepository
 *  com.schoolaccount.SchoolAccount.service.AuditService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.data.domain.Page
 *  org.springframework.data.domain.Pageable
 *  org.springframework.stereotype.Service
 */
package com.schoolaccount.SchoolAccount.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.schoolaccount.SchoolAccount.model.AuditLog;
import com.schoolaccount.SchoolAccount.model.AuditReport;
import com.schoolaccount.SchoolAccount.model.BalanceSheet;
import com.schoolaccount.SchoolAccount.model.Expense;
import com.schoolaccount.SchoolAccount.model.FinancialDiscrepancy;
import com.schoolaccount.SchoolAccount.model.IncomePage;
import com.schoolaccount.SchoolAccount.model.PaymentTransaction;
import com.schoolaccount.SchoolAccount.model.ProfitAndLossStatement;
import com.schoolaccount.SchoolAccount.model.ReportSchedule;
import com.schoolaccount.SchoolAccount.model.ResponseMessage;
import com.schoolaccount.SchoolAccount.model.SchoolWallet;
import com.schoolaccount.SchoolAccount.repo.AuditLogRepository;
import com.schoolaccount.SchoolAccount.repo.AuditReportRepository;
import com.schoolaccount.SchoolAccount.repo.BalanceSheetRepository;
import com.schoolaccount.SchoolAccount.repo.ExpenseRepository;
import com.schoolaccount.SchoolAccount.repo.FinancialDiscrepancyRepository;
import com.schoolaccount.SchoolAccount.repo.IncomePageRepository;
import com.schoolaccount.SchoolAccount.repo.PaymentTransactionRepository;
import com.schoolaccount.SchoolAccount.repo.PayoutRequestRepository;
import com.schoolaccount.SchoolAccount.repo.ProfitAndLossStatementRepository;
import com.schoolaccount.SchoolAccount.repo.ReportScheduleRepository;
import com.schoolaccount.SchoolAccount.repo.SchoolWalletRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AuditService {
    @Autowired
    private AuditLogRepository auditLogRepository;
    @Autowired
    private FinancialDiscrepancyRepository discrepancyRepository;
    @Autowired
    private AuditReportRepository reportRepository;
    @Autowired
    private PaymentTransactionRepository paymentTransactionRepository;
    @Autowired
    private ExpenseRepository expenseRepository;
    @Autowired
    private IncomePageRepository incomePageRepository;
    @Autowired
    private SchoolWalletRepository schoolWalletRepository;
    @Autowired
    private PayoutRequestRepository payoutRequestRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ProfitAndLossStatementRepository plRepository;
    @Autowired
    private BalanceSheetRepository balanceSheetRepository;
    @Autowired
    private ReportScheduleRepository reportScheduleRepository;

    public ResponseMessage createAuditLog(AuditLog auditLog) {
        auditLog.setTimestamp(LocalDateTime.now());
        AuditLog savedLog = (AuditLog)this.auditLogRepository.save(auditLog);
        return new ResponseMessage("Audit log created successfully", (Object)savedLog);
    }

    public ResponseMessage getAuditLogs(String schoolId, Pageable pageable) {
        Page logs = this.auditLogRepository.findBySchoolId(schoolId, pageable);
        return new ResponseMessage("Audit logs retrieved successfully", (Object)logs);
    }

    public ResponseMessage getAuditLogsByEntityType(String schoolId, String entityType, Pageable pageable) {
        Page logs = this.auditLogRepository.findBySchoolIdAndEntityType(schoolId, entityType, pageable);
        return new ResponseMessage("Audit logs retrieved successfully", (Object)logs);
    }

    public ResponseMessage getAuditLogsByDateRange(String schoolId, LocalDateTime start, LocalDateTime end) {
        List logs = this.auditLogRepository.findBySchoolIdAndTimestampBetween(schoolId, start, end);
        return new ResponseMessage("Audit logs retrieved successfully", (Object)logs);
    }

    public ResponseMessage createDiscrepancy(FinancialDiscrepancy discrepancy) {
        discrepancy.setDetectedAt(LocalDateTime.now());
        if (discrepancy.getStatus() == null) {
            discrepancy.setStatus("OPEN");
        }
        if (discrepancy.getExpectedAmount() != 0.0) {
            double variancePercentage = Math.abs(discrepancy.getVarianceAmount()) / discrepancy.getExpectedAmount() * 100.0;
            discrepancy.setVariancePercentage(variancePercentage);
        }
        FinancialDiscrepancy savedDiscrepancy = (FinancialDiscrepancy)this.discrepancyRepository.save(discrepancy);
        return new ResponseMessage("Financial discrepancy recorded successfully", (Object)savedDiscrepancy);
    }

    public ResponseMessage getDiscrepancies(String schoolId, Pageable pageable) {
        Page discrepancies = this.discrepancyRepository.findBySchoolId(schoolId, pageable);
        return new ResponseMessage("Financial discrepancies retrieved successfully", (Object)discrepancies);
    }

    public ResponseMessage getDiscrepanciesByStatus(String schoolId, String status) {
        List discrepancies = this.discrepancyRepository.findBySchoolIdAndStatus(schoolId, status);
        return new ResponseMessage("Financial discrepancies retrieved successfully", (Object)discrepancies);
    }

    public ResponseMessage getDiscrepanciesByType(String schoolId, String discrepancyType) {
        List discrepancies = this.discrepancyRepository.findBySchoolIdAndDiscrepancyType(schoolId, discrepancyType);
        return new ResponseMessage("Financial discrepancies retrieved successfully", (Object)discrepancies);
    }

    public ResponseMessage updateDiscrepancyStatus(String discrepancyId, String status, String resolvedBy, String resolutionNotes) {
        Optional discrepancyOpt = this.discrepancyRepository.findById(discrepancyId);
        if (discrepancyOpt.isPresent()) {
            FinancialDiscrepancy discrepancy = (FinancialDiscrepancy)discrepancyOpt.get();
            discrepancy.setStatus(status);
            if ("RESOLVED".equals(status) || "CLOSED".equals(status)) {
                discrepancy.setResolvedAt(LocalDateTime.now());
                discrepancy.setResolvedBy(resolvedBy);
                discrepancy.setResolutionNotes(resolutionNotes);
            }
            FinancialDiscrepancy updatedDiscrepancy = (FinancialDiscrepancy)this.discrepancyRepository.save(discrepancy);
            return new ResponseMessage("Discrepancy status updated successfully", (Object)updatedDiscrepancy);
        }
        return new ResponseMessage("Discrepancy not found", null);
    }

    public ResponseMessage generateProfitAndLossStatement(String schoolId, LocalDate startDate, LocalDate endDate) {
        Optional existingStatement = this.plRepository.findBySchoolIdAndPeriodStartAndPeriodEnd(schoolId, startDate, endDate);
        if (existingStatement.isPresent()) {
            return new ResponseMessage("P&L statement already exists for this period", existingStatement.get());
        }
        Optional<SchoolWallet> walletOpt = this.schoolWalletRepository.findBySchoolId(schoolId);
        String schoolName = walletOpt.map(SchoolWallet::getSchoolName).orElse("Unknown School");
        ProfitAndLossStatement statement = ProfitAndLossStatement.builder().schoolId(schoolId).schoolName(schoolName).periodStart(startDate).periodEnd(endDate).generatedAt(LocalDateTime.now()).generatedBy("SYSTEM").build();
        List<ProfitAndLossStatement.RevenueCategory> revenueCategories = this.calculateRevenueCategories(schoolId, startDate, endDate);
        statement.setRevenueCategories(revenueCategories);
        List<ProfitAndLossStatement.ExpenseCategory> expenseCategories = this.calculateExpenseCategories(schoolId, startDate, endDate);
        statement.setExpenseCategories(expenseCategories);
        double totalRevenue = revenueCategories.stream().mapToDouble(ProfitAndLossStatement.RevenueCategory::getAmount).sum();
        double totalExpenses = expenseCategories.stream().mapToDouble(ProfitAndLossStatement.ExpenseCategory::getAmount).sum();
        double netProfit = totalRevenue - totalExpenses;
        statement.setTotalRevenue(totalRevenue);
        statement.setTotalExpenses(totalExpenses);
        statement.setNetProfit(netProfit);
        ProfitAndLossStatement savedStatement = (ProfitAndLossStatement)this.plRepository.save(statement);
        return new ResponseMessage("P&L statement generated successfully", (Object)savedStatement);
    }

    private List<ProfitAndLossStatement.RevenueCategory> calculateRevenueCategories(String schoolId, LocalDate startDate, LocalDate endDate) {
        HashMap<String, ProfitAndLossStatement.RevenueCategory> categoryMap = new HashMap<String, ProfitAndLossStatement.RevenueCategory>();
        List<PaymentTransaction> payments = this.paymentTransactionRepository.findBySchoolIdAndCreatedAtBetween(schoolId, startDate.atStartOfDay(), endDate.plusDays(1L).atStartOfDay().minusNanos(1L));
        for (PaymentTransaction payment : payments) {
            if (!"COMPLETED".equals(payment.getStatus())) continue;
            String category = payment.getPaymentType();
            ProfitAndLossStatement.RevenueCategory revenueCategory = categoryMap.computeIfAbsent(category, k -> ProfitAndLossStatement.RevenueCategory.builder().category(category).amount(0.0).items(new ArrayList()).build());
            revenueCategory.setAmount(revenueCategory.getAmount() + payment.getAmount());
            ProfitAndLossStatement.RevenueItem item = ProfitAndLossStatement.RevenueItem.builder().source(payment.getAdmissionNumber()).description(payment.getDescription()).amount(payment.getAmount()).date(payment.getCreatedAt().toLocalDate()).build();
            revenueCategory.getItems().add(item);
        }
        List<IncomePage> incomes = this.incomePageRepository.findBySchoolIdAndDateBetween(schoolId, startDate, endDate);
        for (IncomePage income : incomes) {
            String category = income.getIncomeSource();
            ProfitAndLossStatement.RevenueCategory revenueCategory = categoryMap.computeIfAbsent(category, k -> ProfitAndLossStatement.RevenueCategory.builder().category(category).amount(0.0).items(new ArrayList()).build());
            revenueCategory.setAmount(revenueCategory.getAmount() + income.getAmount());
            ProfitAndLossStatement.RevenueItem item = ProfitAndLossStatement.RevenueItem.builder().source(income.getIncomeSource()).description(income.getDescription()).amount(income.getAmount()).date(income.getDate()).build();
            revenueCategory.getItems().add(item);
        }
        double totalRevenue = categoryMap.values().stream().mapToDouble(ProfitAndLossStatement.RevenueCategory::getAmount).sum();
        if (totalRevenue > 0.0) {
            for (ProfitAndLossStatement.RevenueCategory category : categoryMap.values()) {
                category.setPercentageOfTotal(category.getAmount() / totalRevenue * 100.0);
            }
        }
        return new ArrayList<ProfitAndLossStatement.RevenueCategory>(categoryMap.values());
    }

    private List<ProfitAndLossStatement.ExpenseCategory> calculateExpenseCategories(String schoolId, LocalDate startDate, LocalDate endDate) {
        HashMap<String, ProfitAndLossStatement.ExpenseCategory> categoryMap = new HashMap<String, ProfitAndLossStatement.ExpenseCategory>();
        List<Expense> expenses = this.expenseRepository.findBySchoolIdAndDateBetween(schoolId, startDate, endDate);
        for (Expense expense : expenses) {
            String category = expense.getExpenseType();
            ProfitAndLossStatement.ExpenseCategory expenseCategory = categoryMap.computeIfAbsent(category, k -> ProfitAndLossStatement.ExpenseCategory.builder().category(category).amount(0.0).items(new ArrayList()).build());
            expenseCategory.setAmount(expenseCategory.getAmount() + expense.getAmount());
            ProfitAndLossStatement.ExpenseItem item = ProfitAndLossStatement.ExpenseItem.builder().type(expense.getExpenseType()).description(expense.getDescription()).amount(expense.getAmount()).date(expense.getDate()).build();
            expenseCategory.getItems().add(item);
        }
        double totalExpenses = categoryMap.values().stream().mapToDouble(ProfitAndLossStatement.ExpenseCategory::getAmount).sum();
        if (totalExpenses > 0.0) {
            for (ProfitAndLossStatement.ExpenseCategory category : categoryMap.values()) {
                category.setPercentageOfTotal(category.getAmount() / totalExpenses * 100.0);
            }
        }
        return new ArrayList<ProfitAndLossStatement.ExpenseCategory>(categoryMap.values());
    }

    public ResponseMessage generateBalanceSheet(String schoolId, LocalDate asOfDate) {
        Optional<BalanceSheet> existingSheet = this.balanceSheetRepository.findBySchoolIdAndAsOfDate(schoolId, asOfDate);
        if (existingSheet.isPresent()) {
            return new ResponseMessage("Balance sheet already exists for this date", existingSheet.get());
        }
        Optional<SchoolWallet> walletOpt = this.schoolWalletRepository.findBySchoolId(schoolId);
        String schoolName = walletOpt.map(SchoolWallet::getSchoolName).orElse("Unknown School");
        BalanceSheet balanceSheet = BalanceSheet.builder().schoolId(schoolId).schoolName(schoolName).asOfDate(asOfDate).generatedAt(LocalDateTime.now()).generatedBy("SYSTEM").build();
        List<BalanceSheet.AssetCategory> assetCategories = this.calculateAssetCategories(schoolId, asOfDate);
        balanceSheet.setAssetCategories(assetCategories);
        List<BalanceSheet.LiabilityCategory> liabilityCategories = this.calculateLiabilityCategories(schoolId, asOfDate);
        balanceSheet.setLiabilityCategories(liabilityCategories);
        double totalAssets = assetCategories.stream().mapToDouble(BalanceSheet.AssetCategory::getAmount).sum();
        double totalLiabilities = liabilityCategories.stream().mapToDouble(BalanceSheet.LiabilityCategory::getAmount).sum();
        double totalEquity = totalAssets - totalLiabilities;
        balanceSheet.setTotalAssets(totalAssets);
        balanceSheet.setTotalLiabilities(totalLiabilities);
        balanceSheet.setTotalEquity(totalEquity);
        ArrayList<BalanceSheet.EquityItem> equityItems = new ArrayList<BalanceSheet.EquityItem>();
        equityItems.add(BalanceSheet.EquityItem.builder().name("Net Assets").description("Total assets minus total liabilities").amount(totalEquity).build());
        balanceSheet.setEquityItems(equityItems);
        BalanceSheet savedSheet = (BalanceSheet)this.balanceSheetRepository.save(balanceSheet);
        return new ResponseMessage("Balance sheet generated successfully", (Object)savedSheet);
    }

    private List<BalanceSheet.AssetCategory> calculateAssetCategories(String schoolId, LocalDate asOfDate) {
        double totalAssets;
        ArrayList<BalanceSheet.AssetCategory> assetCategories = new ArrayList<BalanceSheet.AssetCategory>();
        Optional<SchoolWallet> walletOpt = this.schoolWalletRepository.findBySchoolId(schoolId);
        if (walletOpt.isPresent()) {
            SchoolWallet wallet = (SchoolWallet)walletOpt.get();
            BalanceSheet.AssetCategory cashCategory = BalanceSheet.AssetCategory.builder().category("Cash & Bank").amount(wallet.getBalance()).items(new ArrayList()).build();
            BalanceSheet.AssetItem mainWalletItem = BalanceSheet.AssetItem.builder().name("Main Wallet").description("School's main wallet balance").amount(wallet.getBalance()).build();
            cashCategory.getItems().add(mainWalletItem);
            for (SchoolWallet.SubWallet subWallet : wallet.getSubWallets()) {
                BalanceSheet.AssetItem subWalletItem = BalanceSheet.AssetItem.builder().name(subWallet.getName()).description(subWallet.getDescription()).amount(subWallet.getBalance()).build();
                cashCategory.getItems().add(subWalletItem);
            }
            assetCategories.add(cashCategory);
        }
        if ((totalAssets = assetCategories.stream().mapToDouble(BalanceSheet.AssetCategory::getAmount).sum()) > 0.0) {
            for (BalanceSheet.AssetCategory category : assetCategories) {
                category.setPercentageOfTotal(category.getAmount() / totalAssets * 100.0);
            }
        }
        return assetCategories;
    }

    private List<BalanceSheet.LiabilityCategory> calculateLiabilityCategories(String schoolId, LocalDate asOfDate) {
        ArrayList<BalanceSheet.LiabilityCategory> liabilityCategories = new ArrayList<BalanceSheet.LiabilityCategory>();
        return liabilityCategories;
    }

    public byte[] exportProfitAndLossStatementPdf(String schoolId, LocalDate startDate, LocalDate endDate) {
        ResponseMessage response = this.generateProfitAndLossStatement(schoolId, startDate, endDate);
        ProfitAndLossStatement statement = (ProfitAndLossStatement)response.getData();
        return new byte[0];
    }

    public byte[] exportBalanceSheetPdf(String schoolId, LocalDate asOfDate) {
        ResponseMessage response = this.generateBalanceSheet(schoolId, asOfDate);
        BalanceSheet balanceSheet = (BalanceSheet)response.getData();
        return new byte[0];
    }

    public byte[] exportProfitAndLossStatementExcel(String schoolId, LocalDate startDate, LocalDate endDate) {
        ResponseMessage response = this.generateProfitAndLossStatement(schoolId, startDate, endDate);
        ProfitAndLossStatement statement = (ProfitAndLossStatement)response.getData();
        return new byte[0];
    }

    public byte[] exportBalanceSheetExcel(String schoolId, LocalDate asOfDate) {
        ResponseMessage response = this.generateBalanceSheet(schoolId, asOfDate);
        BalanceSheet balanceSheet = (BalanceSheet)response.getData();
        return new byte[0];
    }

    public ResponseMessage scheduleReport(String schoolId, Map<String, Object> scheduleConfig) {
        String reportType = (String)scheduleConfig.get("reportType");
        String frequency = (String)scheduleConfig.get("frequency");
        String format = (String)scheduleConfig.get("format");
        List recipients = (List)scheduleConfig.get("recipients");
        ReportSchedule schedule = ReportSchedule.builder().schoolId(schoolId).reportType(reportType).frequency(frequency).format(format).recipients(recipients).createdAt(LocalDateTime.now()).createdBy("SYSTEM").active(true).build();
        if ("WEEKLY".equals(frequency)) {
            schedule.setDayOfWeek((String)scheduleConfig.get("dayOfWeek"));
        } else if ("MONTHLY".equals(frequency)) {
            schedule.setDayOfMonth((Integer)scheduleConfig.get("dayOfMonth"));
        } else if ("QUARTERLY".equals(frequency)) {
            schedule.setQuarter((Integer)scheduleConfig.get("quarter"));
        } else if ("YEARLY".equals(frequency)) {
            schedule.setMonth((Integer)scheduleConfig.get("month"));
        }
        LocalDateTime nextRunAt = this.calculateNextRunDate(schedule);
        schedule.setNextRunAt(nextRunAt);
        ReportSchedule savedSchedule = (ReportSchedule)this.reportScheduleRepository.save(schedule);
        return new ResponseMessage("Report schedule created successfully", (Object)savedSchedule);
    }

    private LocalDateTime calculateNextRunDate(ReportSchedule schedule) {
        return LocalDateTime.now().plusDays(1L);
    }

    public ResponseMessage getScheduledReports(String schoolId) {
        List schedules = this.reportScheduleRepository.findBySchoolId(schoolId);
        return new ResponseMessage("Scheduled reports retrieved successfully", (Object)schedules);
    }

    public ResponseMessage deleteScheduledReport(String scheduleId) {
        Optional scheduleOpt = this.reportScheduleRepository.findById(scheduleId);
        if (scheduleOpt.isPresent()) {
            this.reportScheduleRepository.deleteById(scheduleId);
            return new ResponseMessage("Scheduled report deleted successfully", null);
        }
        return new ResponseMessage("Scheduled report not found", null);
    }

    public ResponseMessage getAuditReports(String schoolId, Pageable pageable) {
        Page reports = this.reportRepository.findBySchoolId(schoolId, pageable);
        return new ResponseMessage("Audit reports retrieved successfully", (Object)reports);
    }

    public ResponseMessage generateMonthlyAuditReport(String schoolId, int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        return this.generateAuditReport(schoolId, "MONTHLY", "Monthly Audit Report - " + yearMonth.toString(), startDate, endDate);
    }

    public ResponseMessage generateQuarterlyAuditReport(String schoolId, int year, int quarter) {
        int startMonth = (quarter - 1) * 3 + 1;
        LocalDate startDate = LocalDate.of(year, startMonth, 1);
        LocalDate endDate = LocalDate.of(year, startMonth + 2, 1).plusMonths(1L).minusDays(1L);
        return this.generateAuditReport(schoolId, "QUARTERLY", "Quarterly Audit Report - Q" + quarter + " " + year, startDate, endDate);
    }

    public ResponseMessage generateYearlyAuditReport(String schoolId, int year) {
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, 12, 31);
        return this.generateAuditReport(schoolId, "YEARLY", "Yearly Audit Report - " + year, startDate, endDate);
    }

    public ResponseMessage generateCustomAuditReport(String schoolId, String reportName, LocalDate startDate, LocalDate endDate) {
        return this.generateAuditReport(schoolId, "CUSTOM", reportName, startDate, endDate);
    }

    private ResponseMessage generateAuditReport(String schoolId, String reportType, String reportName, LocalDate startDate, LocalDate endDate) {
        Optional<SchoolWallet> walletOpt = this.schoolWalletRepository.findBySchoolId(schoolId);
        String schoolName = walletOpt.map(SchoolWallet::getSchoolName).orElse("Unknown School");
        AuditReport report = AuditReport.builder().schoolId(schoolId).reportType(reportType).reportName(reportName).periodStart(startDate).periodEnd(endDate).generatedAt(LocalDateTime.now()).generatedBy("SYSTEM").build();
        List<PaymentTransaction> payments = this.paymentTransactionRepository.findBySchoolIdAndCreatedAtBetween(schoolId, startDate.atStartOfDay(), endDate.plusDays(1L).atStartOfDay().minusNanos(1L));
        List<Expense> expenses = this.expenseRepository.findBySchoolIdAndDateBetween(schoolId, startDate, endDate);
        List<FinancialDiscrepancy> discrepancies = this.discrepancyRepository.findBySchoolIdAndDetectedAtBetween(schoolId, startDate.atStartOfDay(), endDate.plusDays(1L).atStartOfDay().minusNanos(1L));
        double totalRevenue = payments.stream().filter(p -> "COMPLETED".equals(p.getStatus())).mapToDouble(PaymentTransaction::getAmount).sum();
        double totalExpenses = expenses.stream().mapToDouble(Expense::getAmount).sum();
        double totalTransactions = payments.size();
        double totalDiscrepancies = discrepancies.size();
        report.setTotalRevenue(Double.valueOf(totalRevenue));
        report.setTotalExpenses(Double.valueOf(totalExpenses));
        report.setNetProfit(Double.valueOf(totalRevenue - totalExpenses));
        report.setTotalTransactions(Double.valueOf(totalTransactions));
        report.setTotalDiscrepancies(Double.valueOf(totalDiscrepancies));
        AuditReport savedReport = (AuditReport)this.reportRepository.save(report);
        return new ResponseMessage("Audit report generated successfully", (Object)savedReport);
    }
}

