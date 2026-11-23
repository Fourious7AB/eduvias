/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.Settings.model.BloodGroup
 *  com.neel.Settings.repo.BloodGroupRepository
 *  org.springframework.data.mongodb.repository.MongoRepository
 */
package com.neel.Settings.repo;

import com.neel.Settings.model.BloodGroup;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BloodGroupRepository
extends MongoRepository<BloodGroup, String> {
    public Optional<BloodGroup> findByBloodGroupNameAndBloodGroupType(String var1, String var2);

    public Optional<BloodGroup> findByBloodGroupNameAndBloodGroupTypeAndSchoolId(String var1, String var2, String var3);

    public List<BloodGroup> findBySchoolId(String var1);

    public List<BloodGroup> findByUniversalTrue();

    public Optional<BloodGroup> findByBloodGroupNameAndBloodGroupTypeAndUniversalTrue(String var1, String var2);
}

