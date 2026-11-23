/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.schoolaccount.SchoolAccount.model.CashInHandOnline
 *  com.schoolaccount.SchoolAccount.repo.CashInHandOnlineRepository
 *  org.springframework.data.mongodb.repository.MongoRepository
 */
package com.schoolaccount.SchoolAccount.repo;

import com.schoolaccount.SchoolAccount.model.CashInHandOnline;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CashInHandOnlineRepository
extends MongoRepository<CashInHandOnline, String> {
    public Optional<CashInHandOnline> findFirstByOrderByIdAsc();
}

