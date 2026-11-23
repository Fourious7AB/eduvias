/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.schoolaccount.SchoolAccount.model.CashInHandOnline
 *  com.schoolaccount.SchoolAccount.model.IncomePage
 *  com.schoolaccount.SchoolAccount.repo.CashInHandOnlineRepository
 *  com.schoolaccount.SchoolAccount.repo.IncomePageRepository
 *  com.schoolaccount.SchoolAccount.service.CashInHandOnlineService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 */
package com.schoolaccount.SchoolAccount.service;

import com.schoolaccount.SchoolAccount.model.CashInHandOnline;
import com.schoolaccount.SchoolAccount.model.IncomePage;
import com.schoolaccount.SchoolAccount.repo.CashInHandOnlineRepository;
import com.schoolaccount.SchoolAccount.repo.IncomePageRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CashInHandOnlineService {
    @Autowired
    private IncomePageRepository incomeRepo;
    @Autowired
    private CashInHandOnlineRepository cashRepo;

    public String updateCashAndOnline() {
        CashInHandOnline record;
        List<IncomePage> incomes = this.incomeRepo.findAll();
        double cashInHand = incomes.stream().filter(income -> "cash".equalsIgnoreCase(income.getPaymentMethod())).mapToDouble(IncomePage::getAmount).sum();
        double onlineAmount = incomes.stream().filter(income -> !"cash".equalsIgnoreCase(income.getPaymentMethod())).mapToDouble(IncomePage::getAmount).sum();
        List<CashInHandOnline> existingRecords = this.cashRepo.findAll();
        if (!existingRecords.isEmpty()) {
            record = (CashInHandOnline)existingRecords.get(0);
            record.setCashInHand(cashInHand);
            record.setOnlineAmount(onlineAmount);
        } else {
            record = new CashInHandOnline();
            record.setCashInHand(cashInHand);
            record.setOnlineAmount(onlineAmount);
        }
        this.cashRepo.save(record);
        return "Cash in hand and online amounts updated successfully!";
    }

    public CashInHandOnline getCashAndOnline() {
        return this.cashRepo.findAll().stream().findFirst().orElse(new CashInHandOnline());
    }
}

