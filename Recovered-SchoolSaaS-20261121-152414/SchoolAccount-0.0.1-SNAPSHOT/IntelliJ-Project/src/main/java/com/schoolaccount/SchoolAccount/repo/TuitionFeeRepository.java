/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.schoolaccount.SchoolAccount.model.TuitionFee
 *  com.schoolaccount.SchoolAccount.repo.TuitionFeeRepository
 *  org.springframework.data.mongodb.repository.MongoRepository
 *  org.springframework.stereotype.Repository
 */
package com.schoolaccount.SchoolAccount.repo;

import com.schoolaccount.SchoolAccount.model.TuitionFee;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TuitionFeeRepository
extends MongoRepository<TuitionFee, String> {
    public Optional<TuitionFee> findByClassName(String var1);

    public Optional<TuitionFee> findByClassNameAndSchoolId(String var1, String var2);

    public List<TuitionFee> findBySchoolId(String var1);

    public boolean existsByClassNameAndSchoolId(String var1, String var2);
}

