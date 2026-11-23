/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.schoolaccount.SchoolAccount.model.FeeSelection
 *  com.schoolaccount.SchoolAccount.repo.FeeSelectionRepository
 *  org.springframework.data.mongodb.repository.MongoRepository
 */
package com.schoolaccount.SchoolAccount.repo;

import com.schoolaccount.SchoolAccount.model.FeeSelection;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FeeSelectionRepository
extends MongoRepository<FeeSelection, String> {
    public Optional<FeeSelection> findByClassName(String var1);

    public Optional<FeeSelection> findByFeeType(String var1);

    public Optional<FeeSelection> findBySchoolId(String var1);

    public Optional<FeeSelection> findByFeeTypeAndSchoolId(String var1, String var2);
}

