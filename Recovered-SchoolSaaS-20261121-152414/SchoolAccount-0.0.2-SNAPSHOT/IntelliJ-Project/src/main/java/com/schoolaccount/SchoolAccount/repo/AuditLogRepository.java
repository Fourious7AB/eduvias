/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.schoolaccount.SchoolAccount.model.AuditLog
 *  com.schoolaccount.SchoolAccount.repo.AuditLogRepository
 *  org.springframework.data.domain.Page
 *  org.springframework.data.domain.Pageable
 *  org.springframework.data.mongodb.repository.MongoRepository
 *  org.springframework.stereotype.Repository
 */
package com.schoolaccount.SchoolAccount.repo;

import com.schoolaccount.SchoolAccount.model.AuditLog;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogRepository
extends MongoRepository<AuditLog, String> {
    public List<AuditLog> findBySchoolId(String var1);

    public List<AuditLog> findBySchoolIdAndEntityType(String var1, String var2);

    public List<AuditLog> findBySchoolIdAndEntityId(String var1, String var2);

    public List<AuditLog> findBySchoolIdAndTimestampBetween(String var1, LocalDateTime var2, LocalDateTime var3);

    public Page<AuditLog> findBySchoolId(String var1, Pageable var2);

    public Page<AuditLog> findBySchoolIdAndEntityType(String var1, String var2, Pageable var3);
}

