/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.Teacher.model.Accounts
 *  com.neel.Teacher.repo.AccountRepository
 *  org.springframework.data.mongodb.repository.MongoRepository
 *  org.springframework.stereotype.Repository
 */
package com.neel.Teacher.repo;

import com.neel.Teacher.model.Accounts;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository
extends MongoRepository<Accounts, String> {
    public boolean existsByEmail(String var1);

    public boolean existsByEmailAndSchoolId(String var1, String var2);

    public Optional<Accounts> findByEmail(String var1);

    public Optional<Accounts> findByEmailAndSchoolId(String var1, String var2);

    public List<Accounts> findBySchoolId(String var1);
}

