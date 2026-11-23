/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.schoolaccount.SchoolAccount.model.PayoutRequest
 *  com.schoolaccount.SchoolAccount.repo.PayoutRequestRepository
 *  org.springframework.data.mongodb.repository.MongoRepository
 *  org.springframework.stereotype.Repository
 */
package com.schoolaccount.SchoolAccount.repo;

import com.schoolaccount.SchoolAccount.model.PayoutRequest;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PayoutRequestRepository
extends MongoRepository<PayoutRequest, String> {
    public List<PayoutRequest> findBySchoolId(String var1);

    public List<PayoutRequest> findByStatus(String var1);
}

