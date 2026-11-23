/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.result.Result.model.ProjectMarks
 *  com.result.Result.repo.ProjectMarkRepository
 *  org.springframework.data.mongodb.repository.MongoRepository
 *  org.springframework.stereotype.Repository
 */
package com.result.Result.repo;

import com.result.Result.model.ProjectMarks;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectMarkRepository
extends MongoRepository<ProjectMarks, String> {
    public List<ProjectMarks> findAllBySchoolId(String var1);

    public Optional<ProjectMarks> findByIdAndSchoolId(String var1, String var2);
}

