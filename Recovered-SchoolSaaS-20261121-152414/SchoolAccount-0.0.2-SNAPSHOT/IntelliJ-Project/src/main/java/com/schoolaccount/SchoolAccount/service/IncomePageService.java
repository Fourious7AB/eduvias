/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.schoolaccount.SchoolAccount.model.AccountDetails
 *  com.schoolaccount.SchoolAccount.model.BankTransaction
 *  com.schoolaccount.SchoolAccount.model.CashInHandOnline
 *  com.schoolaccount.SchoolAccount.model.IncomePage
 *  com.schoolaccount.SchoolAccount.model.ResponseMessage
 *  com.schoolaccount.SchoolAccount.repo.AccountDetailsRepository
 *  com.schoolaccount.SchoolAccount.repo.BankTransactionRepository
 *  com.schoolaccount.SchoolAccount.repo.CashInHandOnlineRepository
 *  com.schoolaccount.SchoolAccount.repo.IncomePageRepository
 *  com.schoolaccount.SchoolAccount.service.IncomePageService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 */
package com.schoolaccount.SchoolAccount.service;

import com.schoolaccount.SchoolAccount.model.AccountDetails;
import com.schoolaccount.SchoolAccount.model.BankTransaction;
import com.schoolaccount.SchoolAccount.model.CashInHandOnline;
import com.schoolaccount.SchoolAccount.model.IncomePage;
import com.schoolaccount.SchoolAccount.model.ResponseMessage;
import com.schoolaccount.SchoolAccount.repo.AccountDetailsRepository;
import com.schoolaccount.SchoolAccount.repo.BankTransactionRepository;
import com.schoolaccount.SchoolAccount.repo.CashInHandOnlineRepository;
import com.schoolaccount.SchoolAccount.repo.IncomePageRepository;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IncomePageService {
    @Autowired
    private IncomePageRepository incomePageRepository;
    @Autowired
    private CashInHandOnlineRepository cashInHandOnlineRepository;
    @Autowired
    private BankTransactionRepository bankTransactionRepository;
    @Autowired
    private AccountDetailsRepository accountDetailsRepository;

    public ResponseMessage createIncome(IncomePage income) {
        income.setSchoolId("67ca9167d56ec91fc9539234");
        IncomePage savedIncome = (IncomePage)this.incomePageRepository.save(income);
        AccountDetails accountDetails = (AccountDetails)this.accountDetailsRepository.findFirstByOrderByIdAsc().orElseThrow(() -> new RuntimeException("No account details found"));
        if ("online".equalsIgnoreCase(income.getPaymentMethod())) {
            BankTransaction bankTransaction = new BankTransaction();
            bankTransaction.setTransactionType("DEPOSIT");
            bankTransaction.setAmount(income.getAmount());
            bankTransaction.setDate(income.getDate());
            bankTransaction.setAccountNumber(accountDetails.getAccountNumber());
            bankTransaction.setDescription(income.getDescription());
            this.bankTransactionRepository.save(bankTransaction);
            this.updateBankOpeningAmount(income.getAmount());
        }
        return new ResponseMessage("Income added successfully", (Object)savedIncome);
    }

    private void updateBankOpeningAmount(double depositAmount) {
        AccountDetails bankAccount = this.accountDetailsRepository.findFirstByOrderByIdAsc().orElse(new AccountDetails());
        double newOpeningAmount = bankAccount.getOpeningAmount() + depositAmount;
        bankAccount.setOpeningAmount(newOpeningAmount);
        this.accountDetailsRepository.save(bankAccount);
    }

    public ResponseMessage getAllIncomes() {
        return new ResponseMessage("Fetched all income records", this.incomePageRepository.findAll());
    }

    public ResponseMessage getIncomeById(String id) {
        Optional<IncomePage> incomeOpt = this.incomePageRepository.findById(id);
        return incomeOpt.map(income -> new ResponseMessage("Income found", income)).orElse(new ResponseMessage("Income not found", null));
    }

    public ResponseMessage updateIncome(String id, IncomePage updatedIncome) {
        Optional existingIncomeOpt = this.incomePageRepository.findById(id);
        updatedIncome.setId(id);
        this.incomePageRepository.save(updatedIncome);
        return new ResponseMessage("Income updated successfully", (Object)updatedIncome);
    }

    public ResponseMessage deleteIncome(String id) {
        Optional existingIncomeOpt = this.incomePageRepository.findById(id);
        if (existingIncomeOpt.isPresent()) {
            this.incomePageRepository.deleteById(id);
            return new ResponseMessage("Income deleted successfully", null);
        }
        return new ResponseMessage("Income not found", null);
    }

    public ResponseMessage getTotalIncomeByMonth(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        double totalIncome = this.incomePageRepository.findByDateBetween(startDate, endDate).stream().mapToDouble(IncomePage::getAmount).sum();
        return new ResponseMessage("Total income for " + year + "-" + month, (Object)totalIncome);
    }

    public ResponseMessage getYearlyIncomeBreakdown(int year) {
        LinkedHashMap<String, Double> monthlyIncome = new LinkedHashMap<String, Double>();
        String[] monthNames = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        for (int month = 1; month <= 12; ++month) {
            YearMonth yearMonth = YearMonth.of(year, month);
            LocalDate startDate = yearMonth.atDay(1);
            LocalDate endDate = yearMonth.atEndOfMonth();
            double monthTotal = this.incomePageRepository.findByDateBetween(startDate, endDate).stream().mapToDouble(IncomePage::getAmount).sum();
            monthlyIncome.put(monthNames[month - 1], monthTotal);
        }
        return new ResponseMessage("Income breakdown for " + year, monthlyIncome);
    }

    public ResponseMessage getTotalIncome() {
        double totalIncome = this.incomePageRepository.findAll().stream().mapToDouble(IncomePage::getAmount).sum();
        return new ResponseMessage("Total income in database", (Object)totalIncome);
    }

    public double calculateCashInHand() {
        double cashTotal = this.incomePageRepository.sumByPaymentMethod("cash");
        this.updateSummary(Double.valueOf(cashTotal), null);
        return cashTotal;
    }

    public double calculateOnlinePayments() {
        double onlineTotal = this.incomePageRepository.sumByPaymentMethodNot("cash");
        this.updateSummary(null, Double.valueOf(onlineTotal));
        return onlineTotal;
    }

    private void updateSummary(Double cash, Double online) {
        CashInHandOnline summary = this.cashInHandOnlineRepository.findFirstByOrderByIdAsc().orElse(new CashInHandOnline());
        if (cash != null) {
            summary.setCashInHand(cash.doubleValue());
        }
        if (online != null) {
            summary.setOnlineAmount(online.doubleValue());
        }
        this.cashInHandOnlineRepository.save(summary);
    }

    public CashInHandOnline getSummary() {
        return this.cashInHandOnlineRepository.findFirstByOrderByIdAsc().orElse(new CashInHandOnline());
    }

    public ResponseMessage createIncomeWithSchool(IncomePage income, String schoolId) {
        income.setSchoolId(schoolId);
        IncomePage savedIncome = (IncomePage)this.incomePageRepository.save(income);
        AccountDetails accountDetails = (AccountDetails)this.accountDetailsRepository.findFirstByOrderByIdAsc().orElseThrow(() -> new RuntimeException("No account details found"));
        if ("online".equalsIgnoreCase(income.getPaymentMethod())) {
            BankTransaction bankTransaction = new BankTransaction();
            bankTransaction.setTransactionType("DEPOSIT");
            bankTransaction.setAmount(income.getAmount());
            bankTransaction.setDate(income.getDate());
            bankTransaction.setAccountNumber(accountDetails.getAccountNumber());
            bankTransaction.setDescription(income.getDescription());
            this.bankTransactionRepository.save(bankTransaction);
            this.updateBankOpeningAmount(income.getAmount());
        }
        return new ResponseMessage("Income added successfully", (Object)savedIncome);
    }

    public ResponseMessage getAllIncomesBySchool(String schoolId) {
        List incomes = this.incomePageRepository.findBySchoolId(schoolId);
        return new ResponseMessage("Fetched all income records for school", (Object)incomes);
    }

    public ResponseMessage getIncomeByIdAndSchool(String id, String schoolId) {
        Optional<IncomePage> incomeOpt = this.incomePageRepository.findById(id);
        if (incomeOpt.isPresent() && ((IncomePage)incomeOpt.get()).getSchoolId().equals(schoolId)) {
            return new ResponseMessage("Income found", incomeOpt.get());
        }
        return new ResponseMessage("Income not found or does not belong to this school", null);
    }

    public ResponseMessage updateIncomeWithSchool(String id, IncomePage updatedIncome, String schoolId) {
        Optional existingIncomeOpt = this.incomePageRepository.findById(id);
        if (existingIncomeOpt.isPresent() && ((IncomePage)existingIncomeOpt.get()).getSchoolId().equals(schoolId)) {
            updatedIncome.setId(id);
            updatedIncome.setSchoolId(schoolId);
            this.incomePageRepository.save(updatedIncome);
            return new ResponseMessage("Income updated successfully", (Object)updatedIncome);
        }
        return new ResponseMessage("Income not found or does not belong to this school", null);
    }

    public ResponseMessage deleteIncomeWithSchool(String id, String schoolId) {
        Optional existingIncomeOpt = this.incomePageRepository.findById(id);
        if (existingIncomeOpt.isPresent() && ((IncomePage)existingIncomeOpt.get()).getSchoolId().equals(schoolId)) {
            this.incomePageRepository.deleteById(id);
            return new ResponseMessage("Income deleted successfully", null);
        }
        return new ResponseMessage("Income not found or does not belong to this school", null);
    }

    public ResponseMessage getTotalIncomeByMonthWithSchool(int year, int month, String schoolId) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        double totalIncome = this.incomePageRepository.findByDateBetweenAndSchoolId(startDate, endDate, schoolId).stream().mapToDouble(IncomePage::getAmount).sum();
        return new ResponseMessage("Total income for " + year + "-" + month + " for school", (Object)totalIncome);
    }

    public ResponseMessage getYearlyIncomeBreakdownWithSchool(int year, String schoolId) {
        LinkedHashMap<String, Double> monthlyIncome = new LinkedHashMap<String, Double>();
        String[] monthNames = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        for (int month = 1; month <= 12; ++month) {
            YearMonth yearMonth = YearMonth.of(year, month);
            LocalDate startDate = yearMonth.atDay(1);
            LocalDate endDate = yearMonth.atEndOfMonth();
            double monthTotal = this.incomePageRepository.findByDateBetweenAndSchoolId(startDate, endDate, schoolId).stream().mapToDouble(IncomePage::getAmount).sum();
            monthlyIncome.put(monthNames[month - 1], monthTotal);
        }
        return new ResponseMessage("Income breakdown for " + year + " for school", monthlyIncome);
    }

    public double getTotalIncomeWithSchool(String schoolId) {
        double totalIncome = this.incomePageRepository.findBySchoolId(schoolId).stream().mapToDouble(IncomePage::getAmount).sum();
        return totalIncome;
    }

    public double calculateCashInHandWithSchool(String schoolId) {
        double cashTotal = this.incomePageRepository.sumByPaymentMethodAndSchoolId("cash", schoolId);
        return cashTotal;
    }

    public double calculateOnlinePaymentsWithSchool(String schoolId) {
        double onlineTotal = this.incomePageRepository.sumByPaymentMethodNotAndSchoolId("cash", schoolId);
        return onlineTotal;
    }

    public Map<String, Double> getSummaryWithSchool(String schoolId) {
        HashMap<String, Double> summary = new HashMap<String, Double>();
        summary.put("cashInHand", this.calculateCashInHandWithSchool(schoolId));
        summary.put("onlineAmount", this.calculateOnlinePaymentsWithSchool(schoolId));
        return summary;
    }
}

