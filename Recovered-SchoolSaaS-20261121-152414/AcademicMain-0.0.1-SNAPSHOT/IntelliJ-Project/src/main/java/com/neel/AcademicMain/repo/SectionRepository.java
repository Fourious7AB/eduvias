/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.AcademicMain.models.Section
 *  com.neel.AcademicMain.repo.SectionRepository
 *  org.springframework.data.mongodb.repository.MongoRepository
 *  org.springframework.data.mongodb.repository.config.EnableMongoRepositories
 */
package com.neel.AcademicMain.repo;

import com.neel.AcademicMain.models.Section;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories
public interface SectionRepository
extends MongoRepository<Section, String> {
    public Section findTopByOrderBySectionIdDesc();

    public Optional<Section> findBySectionName(String var1);

    public Optional<Section> findBySectionNameAndSchoolId(String var1, String var2);

    public List<Section> findBySchoolId(String var1);

    public List<Section> findByUniversalIsTrue();

    public List<Section> findBySchoolIdOrUniversalIsTrue(String var1, boolean var2);
}

