/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.result.Result.model.Criteria
 *  com.result.Result.repo.CriteriaRepository
 *  org.springframework.data.mongodb.repository.MongoRepository
 *  org.springframework.stereotype.Repository
 */
package com.result.Result.repo;

import com.result.Result.model.Criteria;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CriteriaRepository
extends MongoRepository<Criteria, String> {
    public List<Criteria> findAllBySchoolId(String var1);

    public Optional<Criteria> findByIdAndSchoolId(String var1, String var2);

    public Optional<Criteria> findBySchoolIdAndClassNameAndExamType(String var1, String var2, String var3);
}

