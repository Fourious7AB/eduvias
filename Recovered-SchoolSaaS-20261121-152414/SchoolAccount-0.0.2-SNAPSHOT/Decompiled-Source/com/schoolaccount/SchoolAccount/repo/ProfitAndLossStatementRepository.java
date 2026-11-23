/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.schoolaccount.SchoolAccount.model.ProfitAndLossStatement
 *  com.schoolaccount.SchoolAccount.repo.ProfitAndLossStatementRepository
 *  org.springframework.data.domain.Page
 *  org.springframework.data.domain.Pageable
 *  org.springframework.data.mongodb.repository.MongoRepository
 *  org.springframework.stereotype.Repository
 */
package com.schoolaccount.SchoolAccount.repo;

import com.schoolaccount.SchoolAccount.model.ProfitAndLossStatement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfitAndLossStatementRepository
extends MongoRepository<ProfitAndLossStatement, String> {
    public List<ProfitAndLossStatement> findBySchoolId(String var1);

    public Page<ProfitAndLossStatement> findBySchoolId(String var1, Pageable var2);

    public Optional<ProfitAndLossStatement> findBySchoolIdAndPeriodStartAndPeriodEnd(String var1, LocalDate var2, LocalDate var3);

    public List<ProfitAndLossStatement> findBySchoolIdAndPeriodStartGreaterThanEqualAndPeriodEndLessThanEqual(String var1, LocalDate var2, LocalDate var3);
}

