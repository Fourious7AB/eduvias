/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.RoutineMain.model.ExamRoutine
 *  com.neel.RoutineMain.repo.ExamRoutineRepository
 *  org.springframework.data.mongodb.repository.MongoRepository
 */
package com.neel.RoutineMain.repo;

import com.neel.RoutineMain.model.ExamRoutine;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ExamRoutineRepository
extends MongoRepository<ExamRoutine, String> {
    public List<ExamRoutine> findByClassName(String var1);

    public boolean existsByClassName(String var1);

    public List<ExamRoutine> findByClassNameAndExamType(String var1, String var2);

    public boolean existsByClassNameAndExamType(String var1, String var2);

    public List<ExamRoutine> findBySchoolId(String var1);

    public Optional<ExamRoutine> findByIdAndSchoolId(String var1, String var2);

    public List<ExamRoutine> findByClassNameAndSchoolId(String var1, String var2);

    public List<ExamRoutine> findByClassNameAndExamTypeAndSchoolId(String var1, String var2, String var3);

    public boolean existsByClassNameAndExamTypeAndSchoolId(String var1, String var2, String var3);
}

