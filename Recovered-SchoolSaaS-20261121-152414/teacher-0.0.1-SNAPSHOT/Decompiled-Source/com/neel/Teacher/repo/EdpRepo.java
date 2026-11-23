/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.Teacher.model.Edp
 *  com.neel.Teacher.repo.EdpRepo
 *  org.springframework.data.mongodb.repository.MongoRepository
 *  org.springframework.stereotype.Repository
 */
package com.neel.Teacher.repo;

import com.neel.Teacher.model.Edp;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EdpRepo
extends MongoRepository<Edp, String> {
    public Optional<Edp> findByEmail(String var1);

    public Optional<Edp> findByEmailAndSchoolId(String var1, String var2);

    public boolean existsByEmail(String var1);

    public boolean existsByEmailAndSchoolId(String var1, String var2);

    public List<Edp> findBySchoolId(String var1);
}

