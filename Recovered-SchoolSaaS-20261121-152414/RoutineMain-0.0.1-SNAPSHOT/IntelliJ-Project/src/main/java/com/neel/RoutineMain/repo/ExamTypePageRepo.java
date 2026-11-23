/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.RoutineMain.model.ExamTypePage
 *  com.neel.RoutineMain.repo.ExamTypePageRepo
 *  org.springframework.data.mongodb.repository.MongoRepository
 */
package com.neel.RoutineMain.repo;

import com.neel.RoutineMain.model.ExamTypePage;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ExamTypePageRepo
extends MongoRepository<ExamTypePage, String> {
    public List<ExamTypePage> findBySchoolId(String var1);

    public Optional<ExamTypePage> findByIdAndSchoolId(String var1, String var2);
}

