/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.schoolaccount.SchoolAccount.controller.ExpenseController
 *  com.schoolaccount.SchoolAccount.model.Expense
 *  com.schoolaccount.SchoolAccount.model.ResponseMessage
 *  com.schoolaccount.SchoolAccount.service.ExpenseService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.http.ResponseEntity
 *  org.springframework.web.bind.annotation.CrossOrigin
 *  org.springframework.web.bind.annotation.DeleteMapping
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.PutMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.schoolaccount.SchoolAccount.controller;

import com.schoolaccount.SchoolAccount.model.Expense;
import com.schoolaccount.SchoolAccount.model.ResponseMessage;
import com.schoolaccount.SchoolAccount.service.ExpenseService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins={"*"})
@RequestMapping(value={"/expenses"})
public class ExpenseController {
    @Autowired
    private ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<ResponseMessage> addExpense(@RequestBody Expense expense) {
        return ResponseEntity.ok(this.expenseService.addExpense(expense));
    }

    @GetMapping
    public ResponseEntity<ResponseMessage> getAllExpenses() {
        return ResponseEntity.ok(this.expenseService.getAllExpenses());
    }

    @GetMapping(value={"/{id}"})
    public ResponseEntity<ResponseMessage> getExpenseById(@PathVariable String id) {
        return ResponseEntity.ok(this.expenseService.getExpenseById(id));
    }

    @PutMapping(value={"/{id}"})
    public ResponseEntity<ResponseMessage> updateExpense(@PathVariable String id, @RequestBody Expense expense) {
        return ResponseEntity.ok(this.expenseService.updateExpense(id, expense));
    }

    @DeleteMapping(value={"/{id}"})
    public ResponseEntity<ResponseMessage> deleteExpense(@PathVariable String id) {
        return ResponseEntity.ok(this.expenseService.deleteExpense(id));
    }

    @GetMapping(value={"/month/{year}/{month}"})
    public ResponseEntity<ResponseMessage> getExpensesByMonth(@PathVariable int year, @PathVariable int month) {
        return ResponseEntity.ok(this.expenseService.getExpensesByMonth(year, month));
    }

    @GetMapping(value={"/yearly-breakdown/{year}"})
    public ResponseEntity<ResponseMessage> getYearlyExpenseBreakdown(@PathVariable int year) {
        return ResponseEntity.ok(this.expenseService.getYearlyExpenseBreakdown(year));
    }

    @GetMapping(value={"/total"})
    public ResponseEntity<ResponseMessage> getTotalExpense() {
        return ResponseEntity.ok(this.expenseService.getTotalExpense());
    }

    @GetMapping(value={"/cash-expenses"})
    public double getCashExpenses() {
        return this.expenseService.calculateCashExpenses();
    }

    @GetMapping(value={"/online-expenses"})
    public double getOnlineExpenses() {
        return this.expenseService.calculateOnlineExpenses();
    }

    @PostMapping(value={"/{schoolId}"})
    public ResponseEntity<ResponseMessage> addExpenseWithSchool(@RequestBody Expense expense, @PathVariable String schoolId) {
        return ResponseEntity.ok(this.expenseService.addExpenseWithSchool(expense, schoolId));
    }

    @GetMapping(value={"/all/{schoolId}"})
    public ResponseEntity<ResponseMessage> getAllExpensesBySchool(@PathVariable String schoolId) {
        return ResponseEntity.ok(this.expenseService.getAllExpensesBySchool(schoolId));
    }

    @GetMapping(value={"/id/{id}/{schoolId}"})
    public ResponseEntity<ResponseMessage> getExpenseByIdAndSchool(@PathVariable String id, @PathVariable String schoolId) {
        return ResponseEntity.ok(this.expenseService.getExpenseByIdAndSchool(id, schoolId));
    }

    @PutMapping(value={"/{id}/{schoolId}"})
    public ResponseEntity<ResponseMessage> updateExpenseWithSchool(@PathVariable String id, @RequestBody Expense updatedExpense, @PathVariable String schoolId) {
        return ResponseEntity.ok(this.expenseService.updateExpenseWithSchool(id, updatedExpense, schoolId));
    }

    @DeleteMapping(value={"/de/{id}/{schoolId}", "/{id}/{schoolId}"})
    public ResponseEntity<ResponseMessage> deleteExpenseWithSchool(@PathVariable String id, @PathVariable String schoolId) {
        return ResponseEntity.ok(this.expenseService.deleteExpenseWithSchool(id, schoolId));
    }

    @GetMapping(value={"/month/{year}/{month}/{schoolId}"})
    public ResponseEntity<ResponseMessage> getExpensesByMonthWithSchool(@PathVariable int year, @PathVariable int month, @PathVariable String schoolId) {
        return ResponseEntity.ok(this.expenseService.getExpensesByMonthWithSchool(year, month, schoolId));
    }

    @GetMapping(value={"/yearly-breakdown/{year}/{schoolId}"})
    public ResponseEntity<ResponseMessage> getYearlyExpenseBreakdownWithSchool(@PathVariable int year, @PathVariable String schoolId) {
        return ResponseEntity.ok(this.expenseService.getYearlyExpenseBreakdownWithSchool(year, schoolId));
    }

    @GetMapping(value={"/cash-expenses/{schoolId}"})
    public double getCashExpensesWithSchool(@PathVariable String schoolId) {
        return this.expenseService.calculateCashExpensesWithSchool(schoolId);
    }

    @GetMapping(value={"/online-expenses/{schoolId}"})
    public double getOnlineExpensesWithSchool(@PathVariable String schoolId) {
        return this.expenseService.calculateOnlineExpensesWithSchool(schoolId);
    }

    @GetMapping(value={"/summary/{schoolId}"})
    public Map<String, Double> getExpenseSummaryWithSchool(@PathVariable String schoolId) {
        return this.expenseService.getExpenseSummaryWithSchool(schoolId);
    }
}

