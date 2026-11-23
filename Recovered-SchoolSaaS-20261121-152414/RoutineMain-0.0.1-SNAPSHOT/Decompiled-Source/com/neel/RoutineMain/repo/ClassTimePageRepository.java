/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.RoutineMain.model.ClassTimePage
 *  com.neel.RoutineMain.repo.ClassTimePageRepository
 *  org.springframework.data.mongodb.repository.MongoRepository
 */
package com.neel.RoutineMain.repo;

import com.neel.RoutineMain.model.ClassTimePage;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClassTimePageRepository
extends MongoRepository<ClassTimePage, String> {
    public List<ClassTimePage> findBySchoolId(String var1);

    public Optional<ClassTimePage> findByIdAndSchoolId(String var1, String var2);

    public Optional<ClassTimePage> findBySchoolIdAndPeriods(String var1, String var2);
}

