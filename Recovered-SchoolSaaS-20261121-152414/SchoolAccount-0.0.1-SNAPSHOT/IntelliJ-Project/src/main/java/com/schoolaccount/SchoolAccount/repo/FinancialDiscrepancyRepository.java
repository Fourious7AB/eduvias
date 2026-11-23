/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.schoolaccount.SchoolAccount.model.FinancialDiscrepancy
 *  com.schoolaccount.SchoolAccount.repo.FinancialDiscrepancyRepository
 *  org.springframework.data.domain.Page
 *  org.springframework.data.domain.Pageable
 *  org.springframework.data.mongodb.repository.MongoRepository
 *  org.springframework.stereotype.Repository
 */
package com.schoolaccount.SchoolAccount.repo;

import com.schoolaccount.SchoolAccount.model.FinancialDiscrepancy;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinancialDiscrepancyRepository
extends MongoRepository<FinancialDiscrepancy, String> {
    public List<FinancialDiscrepancy> findBySchoolId(String var1);

    public List<FinancialDiscrepancy> findBySchoolIdAndStatus(String var1, String var2);

    public List<FinancialDiscrepancy> findBySchoolIdAndDiscrepancyType(String var1, String var2);

    public List<FinancialDiscrepancy> findBySchoolIdAndPeriodStartGreaterThanEqualAndPeriodEndLessThanEqual(String var1, LocalDate var2, LocalDate var3);

    public Page<FinancialDiscrepancy> findBySchoolId(String var1, Pageable var2);

    public long countBySchoolIdAndStatus(String var1, String var2);

    public long countBySchoolIdAndDiscrepancyType(String var1, String var2);

    public List<FinancialDiscrepancy> findBySchoolIdAndDetectedAtBetween(String var1, LocalDateTime var2, LocalDateTime var3);
}

