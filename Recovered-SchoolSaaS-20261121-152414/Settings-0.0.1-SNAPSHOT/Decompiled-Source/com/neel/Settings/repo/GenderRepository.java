/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.Settings.model.Gender
 *  com.neel.Settings.repo.GenderRepository
 *  org.springframework.data.mongodb.repository.MongoRepository
 */
package com.neel.Settings.repo;

import com.neel.Settings.model.Gender;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GenderRepository
extends MongoRepository<Gender, String> {
    public Optional<Gender> findByGenderName(String var1);

    public Optional<Gender> findByGenderNameAndSchoolId(String var1, String var2);

    public List<Gender> findBySchoolId(String var1);

    public List<Gender> findByUniversalTrue();

    public Optional<Gender> findByGenderNameAndUniversalTrue(String var1);
}

