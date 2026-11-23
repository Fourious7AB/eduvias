/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.StudentAdmission.model.School
 *  com.neel.StudentAdmission.repo.SchoolRepository
 *  org.springframework.data.mongodb.repository.MongoRepository
 */
package com.neel.StudentAdmission.repo;

import com.neel.StudentAdmission.model.School;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SchoolRepository
extends MongoRepository<School, String> {
    public Optional<School> findBySchoolId(String var1);

    public Optional<School> findBySchoolEmail(String var1);

    public Optional<School> findBySchoolPrincipalEmail(String var1);
}

