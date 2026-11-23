/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.schoolaccount.SchoolAccount.model.FeesStructure
 *  com.schoolaccount.SchoolAccount.repo.FeesStructureRepository
 *  org.springframework.data.mongodb.repository.MongoRepository
 *  org.springframework.stereotype.Repository
 */
package com.schoolaccount.SchoolAccount.repo;

import com.schoolaccount.SchoolAccount.model.FeesStructure;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeesStructureRepository
extends MongoRepository<FeesStructure, String> {
    public Optional<FeesStructure> findByClassNameAndSection(String var1, String var2);

    public List<FeesStructure> findByClassName(String var1);

    public Optional<FeesStructure> findByClassNameIgnoreCaseAndSchoolId(String var1, String var2);

    public List<FeesStructure> findBySchoolId(String var1);

    public boolean existsByClassNameAndSchoolId(String var1, String var2);

    public Optional<FeesStructure> findByClassNameAndSectionAndSchoolId(String var1, String var2, String var3);
}

