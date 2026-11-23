/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.schoolaccount.SchoolAccount.model.AccountDetails
 *  com.schoolaccount.SchoolAccount.model.BankTransaction
 *  com.schoolaccount.SchoolAccount.model.Expense
 *  com.schoolaccount.SchoolAccount.model.ResponseMessage
 *  com.schoolaccount.SchoolAccount.repo.AccountDetailsRepository
 *  com.schoolaccount.SchoolAccount.repo.BankTransactionRepository
 *  com.schoolaccount.SchoolAccount.repo.CashInHandOnlineRepository
 *  com.schoolaccount.SchoolAccount.repo.ExpenseRepository
 *  com.schoolaccount.SchoolAccount.service.ExpenseService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 */
package com.schoolaccount.SchoolAccount.service;

import com.schoolaccount.SchoolAccount.model.AccountDetails;
import com.schoolaccount.SchoolAccount.model.BankTransaction;
import com.schoolaccount.SchoolAccount.model.Expense;
import com.schoolaccount.SchoolAccount.model.ResponseMessage;
import com.schoolaccount.SchoolAccount.repo.AccountDetailsRepository;
import com.schoolaccount.SchoolAccount.repo.BankTransactionRepository;
import com.schoolaccount.SchoolAccount.repo.CashInHandOnlineRepository;
import com.schoolaccount.SchoolAccount.repo.ExpenseRepository;
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
public class ExpenseService {
    @Autowired
    private ExpenseRepository expenseRepository;
    @Autowired
    private CashInHandOnlineRepository cashInHandOnlineRepository;
    @Autowired
    private BankTransactionRepository bankTransactionRepository;
    @Autowired
    private AccountDetailsRepository accountDetailsRepository;

    public ResponseMessage addExpense(Expense expense) {
        expense.setSchoolId("67ca9167d56ec91fc9539234");
        Expense savedExpense = (Expense)this.expenseRepository.save(expense);
        AccountDetails accountDetails = (AccountDetails)this.accountDetailsRepository.findFirstByOrderByIdAsc().orElseThrow(() -> new RuntimeException("No account details found"));
        if ("online".equalsIgnoreCase(expense.getPaymentMethod())) {
            BankTransaction bankTransaction = new BankTransaction();
            bankTransaction.setTransactionType("WITHDRAW");
            bankTransaction.setAmount(expense.getAmount());
            bankTransaction.setDate(expense.getDate());
            bankTransaction.setAccountNumber(accountDetails.getAccountNumber());
            bankTransaction.setDescription(expense.getDescription());
            this.bankTransactionRepository.save(bankTransaction);
            this.updateBankOpeningAmount(expense.getAmount());
        }
        return new ResponseMessage("Expense added successfully", (Object)savedExpense);
    }

    private void updateBankOpeningAmount(double depositAmount) {
        AccountDetails bankAccount = this.accountDetailsRepository.findFirstByOrderByIdAsc().orElse(new AccountDetails());
        double newOpeningAmount = bankAccount.getOpeningAmount() - depositAmount;
        bankAccount.setOpeningAmount(newOpeningAmount);
        this.accountDetailsRepository.save(bankAccount);
    }

    public ResponseMessage getAllExpenses() {
        return new ResponseMessage("Fetched all expenses", this.expenseRepository.findAll());
    }

    public ResponseMessage getExpenseById(String id) {
        Optional<Expense> expense = this.expenseRepository.findById(id);
        return expense.map(value -> new ResponseMessage("Expense found", value)).orElse(new ResponseMessage("Expense not found", null));
    }

    public ResponseMessage updateExpense(String id, Expense updatedExpense) {
        Optional expenseOpt = this.expenseRepository.findById(id);
        if (expenseOpt.isPresent()) {
            updatedExpense.setId(id);
            Expense savedExpense = (Expense)this.expenseRepository.save(updatedExpense);
            return new ResponseMessage("Expense updated successfully", (Object)savedExpense);
        }
        return new ResponseMessage("Expense not found", null);
    }

    public ResponseMessage deleteExpense(String id) {
        if (this.expenseRepository.existsById(id)) {
            this.expenseRepository.deleteById(id);
            return new ResponseMessage("Expense deleted successfully", null);
        }
        return new ResponseMessage("Expense not found", null);
    }

    public ResponseMessage getExpensesByMonth(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        List expenses = this.expenseRepository.findByDateBetween(startDate, endDate);
        return new ResponseMessage("Fetched expenses for " + year + "-" + month, (Object)expenses);
    }

    public ResponseMessage getYearlyExpenseBreakdown(int year) {
        LinkedHashMap<String, Double> monthlyExpense = new LinkedHashMap<String, Double>();
        String[] monthNames = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        for (int month = 1; month <= 12; ++month) {
            YearMonth yearMonth = YearMonth.of(year, month);
            LocalDate startDate = yearMonth.atDay(1);
            LocalDate endDate = yearMonth.atEndOfMonth();
            double monthTotal = this.expenseRepository.findByDateBetween(startDate, endDate).stream().mapToDouble(Expense::getAmount).sum();
            monthlyExpense.put(monthNames[month - 1], monthTotal);
        }
        return new ResponseMessage("Expense breakdown for " + year, monthlyExpense);
    }

    public ResponseMessage getTotalExpense() {
        double totalExpense = this.expenseRepository.findAll().stream().mapToDouble(Expense::getAmount).sum();
        return new ResponseMessage("Total Expense in database", (Object)totalExpense);
    }

    public double calculateCashExpenses() {
        Double cashTotal = this.expenseRepository.sumByPaymentMethod("cash");
        return cashTotal != null ? cashTotal : 0.0;
    }

    public double calculateOnlineExpenses() {
        Double onlineTotal = this.expenseRepository.sumByPaymentMethodNot("cash");
        return onlineTotal != null ? onlineTotal : 0.0;
    }

    public ResponseMessage addExpenseWithSchool(Expense expense, String schoolId) {
        expense.setSchoolId(schoolId);
        Expense savedExpense = (Expense)this.expenseRepository.save(expense);
        AccountDetails accountDetails = (AccountDetails)this.accountDetailsRepository.findFirstByOrderByIdAsc().orElseThrow(() -> new RuntimeException("No account details found"));
        if ("online".equalsIgnoreCase(expense.getPaymentMethod())) {
            BankTransaction bankTransaction = new BankTransaction();
            bankTransaction.setTransactionType("WITHDRAW");
            bankTransaction.setAmount(expense.getAmount());
            bankTransaction.setDate(expense.getDate());
            bankTransaction.setAccountNumber(accountDetails.getAccountNumber());
            bankTransaction.setDescription(expense.getDescription());
            this.bankTransactionRepository.save(bankTransaction);
            this.updateBankOpeningAmount(expense.getAmount());
        }
        return new ResponseMessage("Expense added successfully", (Object)savedExpense);
    }

    public ResponseMessage getAllExpensesBySchool(String schoolId) {
        List expenses = this.expenseRepository.findBySchoolId(schoolId);
        return new ResponseMessage("Fetched all expenses for school", (Object)expenses);
    }

    public ResponseMessage getExpenseByIdAndSchool(String id, String schoolId) {
        Optional expenseOpt = this.expenseRepository.findById(id);
        if (expenseOpt.isPresent() && ((Expense)expenseOpt.get()).getSchoolId().equals(schoolId)) {
            return new ResponseMessage("Expense found", expenseOpt.get());
        }
        return new ResponseMessage("Expense not found or does not belong to this school", null);
    }

    public ResponseMessage updateExpenseWithSchool(String id, Expense updatedExpense, String schoolId) {
        Optional expenseOpt = this.expenseRepository.findById(id);
        if (expenseOpt.isPresent() && ((Expense)expenseOpt.get()).getSchoolId().equals(schoolId)) {
            updatedExpense.setId(id);
            updatedExpense.setSchoolId(schoolId);
            Expense savedExpense = (Expense)this.expenseRepository.save(updatedExpense);
            return new ResponseMessage("Expense updated successfully", (Object)savedExpense);
        }
        return new ResponseMessage("Expense not found or does not belong to this school", null);
    }

    public ResponseMessage deleteExpenseWithSchool(String id, String schoolId) {
        Optional expenseOpt = this.expenseRepository.findById(id);
        if (expenseOpt.isPresent() && ((Expense)expenseOpt.get()).getSchoolId().equals(schoolId)) {
            this.expenseRepository.deleteById(id);
            return new ResponseMessage("Expense deleted successfully", null);
        }
        return new ResponseMessage("Expense not found or does not belong to this school", null);
    }

    public ResponseMessage getExpensesByMonthWithSchool(int year, int month, String schoolId) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        List expenses = this.expenseRepository.findByDateBetweenAndSchoolId(startDate, endDate, schoolId);
        return new ResponseMessage("Fetched expenses for " + year + "-" + month + " for school", (Object)expenses);
    }

    public ResponseMessage getYearlyExpenseBreakdownWithSchool(int year, String schoolId) {
        LinkedHashMap<String, Double> monthlyExpense = new LinkedHashMap<String, Double>();
        String[] monthNames = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        for (int month = 1; month <= 12; ++month) {
            YearMonth yearMonth = YearMonth.of(year, month);
            LocalDate startDate = yearMonth.atDay(1);
            LocalDate endDate = yearMonth.atEndOfMonth();
            double monthTotal = this.expenseRepository.findByDateBetweenAndSchoolId(startDate, endDate, schoolId).stream().mapToDouble(Expense::getAmount).sum();
            monthlyExpense.put(monthNames[month - 1], monthTotal);
        }
        return new ResponseMessage("Expense breakdown for " + year + " for school", monthlyExpense);
    }

    public double getTotalExpenseWithSchool(String schoolId) {
        double totalExpense = this.expenseRepository.findBySchoolId(schoolId).stream().mapToDouble(Expense::getAmount).sum();
        return totalExpense;
    }

    public double calculateCashExpensesWithSchool(String schoolId) {
        Double cashTotal = this.expenseRepository.sumByPaymentMethodAndSchoolId("cash", schoolId);
        return cashTotal != null ? cashTotal : 0.0;
    }

    public double calculateOnlineExpensesWithSchool(String schoolId) {
        Double onlineTotal = this.expenseRepository.sumByPaymentMethodNotAndSchoolId("cash", schoolId);
        return onlineTotal != null ? onlineTotal : 0.0;
    }

    public Map<String, Double> getExpenseSummaryWithSchool(String schoolId) {
        HashMap<String, Double> summary = new HashMap<String, Double>();
        summary.put("cashExpenses", this.calculateCashExpensesWithSchool(schoolId));
        summary.put("onlineExpenses", this.calculateOnlineExpensesWithSchool(schoolId));
        return summary;
    }
}

