/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.StudentAdmission.model.AdmissionHistory
 *  com.neel.StudentAdmission.repo.AdmissionHistoryRepository
 *  org.springframework.data.mongodb.repository.MongoRepository
 *  org.springframework.stereotype.Repository
 */
package com.neel.StudentAdmission.repo;

import com.neel.StudentAdmission.model.AdmissionHistory;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdmissionHistoryRepository
extends MongoRepository<AdmissionHistory, String> {
    public List<AdmissionHistory> findByAdmissionNumberAndSchoolId(String var1, String var2);
}

