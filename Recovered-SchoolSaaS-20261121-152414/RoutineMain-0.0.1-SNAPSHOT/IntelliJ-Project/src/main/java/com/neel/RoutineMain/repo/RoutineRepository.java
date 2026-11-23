/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.RoutineMain.model.Routine
 *  com.neel.RoutineMain.repo.RoutineRepository
 *  org.springframework.data.mongodb.repository.MongoRepository
 *  org.springframework.data.mongodb.repository.Query
 */
package com.neel.RoutineMain.repo;

import com.neel.RoutineMain.model.Routine;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface RoutineRepository
extends MongoRepository<Routine, String> {
    public List<Routine> findByClassNameAndSection(String var1, String var2);

    @Query(value="{'className': {$regex: ?0, $options: 'i'}, 'section': {$regex: ?1, $options: 'i'}, 'day': {$regex: ?2, $options: 'i'}}")
    public boolean existsByClassNameAndSectionAndDayIgnoreCase(String var1, String var2, String var3);

    public List<Routine> findByPeriodsTeacherName(String var1);

    public List<Routine> findByClassNameAndPeriodsTeacherName(String var1, String var2);

    public List<Routine> findBySchoolId(String var1);

    public Optional<Routine> findByIdAndSchoolId(String var1, String var2);

    public List<Routine> findByClassNameAndSectionAndSchoolId(String var1, String var2, String var3);

    public List<Routine> findByClassNameIgnoreCaseAndSectionIgnoreCaseAndDayIgnoreCaseAndSchoolId(String var1, String var2, String var3, String var4);

    public List<Routine> findByPeriodsTeacherNameAndSchoolId(String var1, String var2);
}

