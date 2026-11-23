/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.schoolaccount.SchoolAccount.model.BalanceSheet
 *  com.schoolaccount.SchoolAccount.repo.BalanceSheetRepository
 *  org.springframework.data.domain.Page
 *  org.springframework.data.domain.Pageable
 *  org.springframework.data.mongodb.repository.MongoRepository
 *  org.springframework.stereotype.Repository
 */
package com.schoolaccount.SchoolAccount.repo;

import com.schoolaccount.SchoolAccount.model.BalanceSheet;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BalanceSheetRepository
extends MongoRepository<BalanceSheet, String> {
    public List<BalanceSheet> findBySchoolId(String var1);

    public Page<BalanceSheet> findBySchoolId(String var1, Pageable var2);

    public Optional<BalanceSheet> findBySchoolIdAndAsOfDate(String var1, LocalDate var2);

    public List<BalanceSheet> findBySchoolIdAndAsOfDateBetween(String var1, LocalDate var2, LocalDate var3);
}

