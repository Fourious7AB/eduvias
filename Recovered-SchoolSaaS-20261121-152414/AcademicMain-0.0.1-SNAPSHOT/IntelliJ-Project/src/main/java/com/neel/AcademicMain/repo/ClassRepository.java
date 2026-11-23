/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.AcademicMain.models.ClassEntity
 *  com.neel.AcademicMain.repo.ClassRepository
 *  org.springframework.data.mongodb.repository.MongoRepository
 */
package com.neel.AcademicMain.repo;

import com.neel.AcademicMain.models.ClassEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClassRepository
extends MongoRepository<ClassEntity, String> {
    public ClassEntity findTopByOrderByClassIdDesc();

    public List<ClassEntity> findBySchoolId(String var1);

    public Optional<ClassEntity> findByClassName(String var1);

    public Optional<ClassEntity> findByClassNameAndSchoolId(String var1, String var2);

    public Optional<ClassEntity> findTopBySchoolIdOrderByClassIdDesc(String var1);

    public long countBySchoolId(String var1);

    public List<ClassEntity> findByUniversalTrue();

    public List<ClassEntity> findByUniversalTrueOrSchoolId(String var1);

    public List<ClassEntity> findByUniversal(boolean var1);

    public Optional<ClassEntity> findByClassNameIgnoreCaseAndUniversal(String var1, boolean var2);

    public List<ClassEntity> findByUniversalTrueOrSchoolIdOrClassNameIgnoreCase(String var1, String var2);
}

