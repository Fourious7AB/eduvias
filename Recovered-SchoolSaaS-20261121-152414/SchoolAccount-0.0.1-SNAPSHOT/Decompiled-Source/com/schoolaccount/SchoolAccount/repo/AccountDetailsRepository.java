/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.schoolaccount.SchoolAccount.model.AccountDetails
 *  com.schoolaccount.SchoolAccount.repo.AccountDetailsRepository
 *  org.springframework.data.mongodb.repository.MongoRepository
 */
package com.schoolaccount.SchoolAccount.repo;

import com.schoolaccount.SchoolAccount.model.AccountDetails;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccountDetailsRepository
extends MongoRepository<AccountDetails, String> {
    public Optional<AccountDetails> findFirstByOrderByIdAsc();

    public Optional<AccountDetails> findBySchoolId(String var1);
}

