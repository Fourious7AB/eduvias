/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.schoolaccount.SchoolAccount.model.AuditReport
 *  com.schoolaccount.SchoolAccount.repo.AuditReportRepository
 *  org.springframework.data.domain.Page
 *  org.springframework.data.domain.Pageable
 *  org.springframework.data.mongodb.repository.MongoRepository
 *  org.springframework.stereotype.Repository
 */
package com.schoolaccount.SchoolAccount.repo;

import com.schoolaccount.SchoolAccount.model.AuditReport;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditReportRepository
extends MongoRepository<AuditReport, String> {
    public List<AuditReport> findBySchoolId(String var1);

    public List<AuditReport> findBySchoolIdAndReportType(String var1, String var2);

    public List<AuditReport> findBySchoolIdAndStatus(String var1, String var2);

    public List<AuditReport> findBySchoolIdAndPeriodStartGreaterThanEqualAndPeriodEndLessThanEqual(String var1, LocalDate var2, LocalDate var3);

    public Page<AuditReport> findBySchoolId(String var1, Pageable var2);
}

