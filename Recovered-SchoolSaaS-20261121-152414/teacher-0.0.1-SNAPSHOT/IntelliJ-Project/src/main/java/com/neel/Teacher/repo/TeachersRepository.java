/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.Teacher.model.Teachers
 *  com.neel.Teacher.repo.TeachersRepository
 *  org.springframework.data.mongodb.repository.MongoRepository
 *  org.springframework.stereotype.Repository
 */
package com.neel.Teacher.repo;

import com.neel.Teacher.model.Teachers;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeachersRepository
extends MongoRepository<Teachers, String> {
    public Optional<Teachers> findByEmail(String var1);

    public Optional<Teachers> findByEmailAndSchoolId(String var1, String var2);

    public boolean existsByEmail(String var1);

    public boolean existsByEmailAndSchoolId(String var1, String var2);

    public List<Teachers> findBySubject(String var1);

    public List<Teachers> findBySubjectAndSchoolId(String var1, String var2);

    public List<Teachers> findBySchoolId(String var1);

    public Optional<Teachers> findByIdAndSchoolId(String var1, String var2);
}

