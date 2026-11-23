/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.schoolaccount.SchoolAccount.model.ReportSchedule
 *  com.schoolaccount.SchoolAccount.repo.ReportScheduleRepository
 *  org.springframework.data.mongodb.repository.MongoRepository
 *  org.springframework.stereotype.Repository
 */
package com.schoolaccount.SchoolAccount.repo;

import com.schoolaccount.SchoolAccount.model.ReportSchedule;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportScheduleRepository
extends MongoRepository<ReportSchedule, String> {
    public List<ReportSchedule> findBySchoolId(String var1);

    public List<ReportSchedule> findBySchoolIdAndReportType(String var1, String var2);

    public List<ReportSchedule> findByActiveTrue();

    public List<ReportSchedule> findByActiveTrueAndNextRunAtBefore(LocalDateTime var1);
}

