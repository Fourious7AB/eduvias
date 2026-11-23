/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.schoolaccount.SchoolAccount.model.SchoolWallet
 *  com.schoolaccount.SchoolAccount.repo.SchoolWalletRepository
 *  org.springframework.data.mongodb.repository.MongoRepository
 *  org.springframework.stereotype.Repository
 */
package com.schoolaccount.SchoolAccount.repo;

import com.schoolaccount.SchoolAccount.model.SchoolWallet;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchoolWalletRepository
extends MongoRepository<SchoolWallet, String> {
    public Optional<SchoolWallet> findBySchoolId(String var1);
}

