/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.Settings.model.Religion
 *  com.neel.Settings.repo.ReligionRepository
 *  org.springframework.data.mongodb.repository.MongoRepository
 */
package com.neel.Settings.repo;

import com.neel.Settings.model.Religion;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReligionRepository
extends MongoRepository<Religion, String> {
    public Optional<Religion> findByReligionName(String var1);

    public Optional<Religion> findByReligionNameAndSchoolId(String var1, String var2);

    public List<Religion> findBySchoolId(String var1);

    public List<Religion> findByUniversalTrue();

    public Optional<Religion> findByReligionNameAndUniversalTrue(String var1);
}

