/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.schoolaccount.SchoolAccount.controller.IncomePageController
 *  com.schoolaccount.SchoolAccount.model.CashInHandOnline
 *  com.schoolaccount.SchoolAccount.model.IncomePage
 *  com.schoolaccount.SchoolAccount.model.ResponseMessage
 *  com.schoolaccount.SchoolAccount.service.ExpenseService
 *  com.schoolaccount.SchoolAccount.service.IncomePageService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.http.HttpStatus
 *  org.springframework.http.HttpStatusCode
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

import com.schoolaccount.SchoolAccount.model.CashInHandOnline;
import com.schoolaccount.SchoolAccount.model.IncomePage;
import com.schoolaccount.SchoolAccount.model.ResponseMessage;
import com.schoolaccount.SchoolAccount.service.ExpenseService;
import com.schoolaccount.SchoolAccount.service.IncomePageService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
@RequestMapping(value={"/income"})
public class IncomePageController {
    @Autowired
    private IncomePageService incomePageService;
    @Autowired
    private ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<ResponseMessage> createIncome(@RequestBody IncomePage income) {
        return ResponseEntity.ok((Object)this.incomePageService.createIncome(income));
    }

    @GetMapping
    public ResponseEntity<ResponseMessage> getAllIncomes() {
        return ResponseEntity.ok((Object)this.incomePageService.getAllIncomes());
    }

    @GetMapping(value={"/{id}"})
    public ResponseEntity<ResponseMessage> getIncomeById(@PathVariable String id) {
        return ResponseEntity.ok((Object)this.incomePageService.getIncomeById(id));
    }

    @PutMapping(value={"/{id}"})
    public ResponseEntity<ResponseMessage> updateIncome(@PathVariable String id, @RequestBody IncomePage updatedIncome) {
        return ResponseEntity.ok((Object)this.incomePageService.updateIncome(id, updatedIncome));
    }

    @DeleteMapping(value={"/{id}"})
    public ResponseEntity<ResponseMessage> deleteIncome(@PathVariable String id) {
        return ResponseEntity.ok((Object)this.incomePageService.deleteIncome(id));
    }

    @GetMapping(value={"/total/{year}/{month}"})
    public ResponseEntity<ResponseMessage> getTotalIncomeByMonth(@PathVariable int year, @PathVariable int month) {
        return ResponseEntity.ok((Object)this.incomePageService.getTotalIncomeByMonth(year, month));
    }

    @GetMapping(value={"/yearly-breakdown/{year}"})
    public ResponseEntity<ResponseMessage> getYearlyIncomeBreakdown(@PathVariable int year) {
        return ResponseEntity.ok((Object)this.incomePageService.getYearlyIncomeBreakdown(year));
    }

    @GetMapping(value={"/total"})
    public ResponseEntity<ResponseMessage> getTotalIncome() {
        return ResponseEntity.ok((Object)this.incomePageService.getTotalIncome());
    }

    @GetMapping(value={"/cash-in-hand"})
    public double getCashInHand() {
        return this.incomePageService.calculateCashInHand();
    }

    @GetMapping(value={"/online-payments"})
    public double getOnlinePayments() {
        return this.incomePageService.calculateOnlinePayments();
    }

    @GetMapping(value={"/summary"})
    public CashInHandOnline getSummary() {
        return this.incomePageService.getSummary();
    }

    @PostMapping(value={"/{schoolId}"})
    public ResponseEntity<ResponseMessage> createIncomeWithSchool(@RequestBody IncomePage income, @PathVariable String schoolId) {
        return ResponseEntity.ok((Object)this.incomePageService.createIncomeWithSchool(income, schoolId));
    }

    @GetMapping(value={"/all/{schoolId}"})
    public ResponseEntity<ResponseMessage> getAllIncomesBySchool(@PathVariable String schoolId) {
        return ResponseEntity.ok((Object)this.incomePageService.getAllIncomesBySchool(schoolId));
    }

    @GetMapping(value={"/{id}/{schoolId}"})
    public ResponseEntity<ResponseMessage> getIncomeByIdAndSchool(@PathVariable String id, @PathVariable String schoolId) {
        return ResponseEntity.ok((Object)this.incomePageService.getIncomeByIdAndSchool(id, schoolId));
    }

    @PutMapping(value={"/{id}/{schoolId}"})
    public ResponseEntity<ResponseMessage> updateIncomeWithSchool(@PathVariable String id, @RequestBody IncomePage updatedIncome, @PathVariable String schoolId) {
        return ResponseEntity.ok((Object)this.incomePageService.updateIncomeWithSchool(id, updatedIncome, schoolId));
    }

    @DeleteMapping(value={"/{id}/{schoolId}"})
    public ResponseEntity<ResponseMessage> deleteIncomeWithSchool(@PathVariable String id, @PathVariable String schoolId) {
        return ResponseEntity.ok((Object)this.incomePageService.deleteIncomeWithSchool(id, schoolId));
    }

    @GetMapping(value={"/total/{year}/{month}/{schoolId}"})
    public ResponseEntity<ResponseMessage> getTotalIncomeByMonthWithSchool(@PathVariable int year, @PathVariable int month, @PathVariable String schoolId) {
        return ResponseEntity.ok((Object)this.incomePageService.getTotalIncomeByMonthWithSchool(year, month, schoolId));
    }

    @GetMapping(value={"/yearly-breakdown/{year}/{schoolId}"})
    public ResponseEntity<ResponseMessage> getYearlyIncomeBreakdownWithSchool(@PathVariable int year, @PathVariable String schoolId) {
        return ResponseEntity.ok((Object)this.incomePageService.getYearlyIncomeBreakdownWithSchool(year, schoolId));
    }

    @GetMapping(value={"/edp-summary/{schoolId}"})
    public ResponseEntity<ResponseMessage> countIncomeAndExpense(@PathVariable String schoolId) {
        HashMap<String, Double> counts = new HashMap<String, Double>();
        counts.put("totalIncome", this.incomePageService.getTotalIncomeWithSchool(schoolId));
        counts.put("totalExpense", this.expenseService.getTotalExpenseWithSchool(schoolId));
        return new ResponseEntity((Object)new ResponseMessage("get two things one is totalIncome, and one is totalExpense", counts), (HttpStatusCode)HttpStatus.OK);
    }

    @GetMapping(value={"/cash-in-hand/{schoolId}"})
    public double getCashInHandWithSchool(@PathVariable String schoolId) {
        return this.incomePageService.calculateCashInHandWithSchool(schoolId);
    }

    @GetMapping(value={"/online-payments/{schoolId}"})
    public double getOnlinePaymentsWithSchool(@PathVariable String schoolId) {
        return this.incomePageService.calculateOnlinePaymentsWithSchool(schoolId);
    }

    @GetMapping(value={"/summary/{schoolId}"})
    public Map<String, Double> getSummaryWithSchool(@PathVariable String schoolId) {
        return this.incomePageService.getSummaryWithSchool(schoolId);
    }
}

