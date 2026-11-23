/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.schoolaccount.SchoolAccount.model.BankTransaction
 *  com.schoolaccount.SchoolAccount.repo.BankTransactionRepository
 *  org.springframework.data.mongodb.repository.MongoRepository
 */
package com.schoolaccount.SchoolAccount.repo;

import com.schoolaccount.SchoolAccount.model.BankTransaction;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BankTransactionRepository
extends MongoRepository<BankTransaction, String> {
    public List<BankTransaction> findByDateBetween(LocalDate var1, LocalDate var2);

    public List<BankTransaction> findBySchoolId(String var1);

    public List<BankTransaction> findByDateBetweenAndSchoolId(LocalDate var1, LocalDate var2, String var3);
}

