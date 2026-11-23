/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.result.Result.model.GradeSystem
 *  com.result.Result.repo.GradeSystemRepository
 *  org.springframework.data.mongodb.repository.MongoRepository
 *  org.springframework.data.mongodb.repository.Query
 */
package com.result.Result.repo;

import com.result.Result.model.GradeSystem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface GradeSystemRepository
extends MongoRepository<GradeSystem, String> {
    public Optional<GradeSystem> findByPerformanceIndicator(String var1);

    @Query(sort="{ 'scaleStartMarks': -1 }")
    public List<GradeSystem> findBySchoolId(String var1);

    public Optional<GradeSystem> findByPerformanceIndicatorAndSchoolId(String var1, String var2);

    public Optional<GradeSystem> findByIdAndSchoolId(String var1, String var2);
}

