/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.Teacher.model.Assignment
 *  com.neel.Teacher.repo.AssignmentRepository
 *  org.springframework.data.mongodb.repository.MongoRepository
 *  org.springframework.stereotype.Repository
 */
package com.neel.Teacher.repo;

import com.neel.Teacher.model.Assignment;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignmentRepository
extends MongoRepository<Assignment, String> {
    public List<Assignment> findBySchoolId(String var1);

    public List<Assignment> findByTeacherId(String var1);

    public List<Assignment> findBySchoolIdAndTeacherId(String var1, String var2);

    public List<Assignment> findBySchoolIdAndClassNameIgnoreCaseAndSectionIgnoreCase(String var1, String var2, String var3);

    public List<Assignment> findBySchoolIdAndClassNameIgnoreCaseAndSectionIgnoreCaseAndSubjectIgnoreCase(String var1, String var2, String var3, String var4);

    public List<Assignment> findByAssignmentDateBetween(LocalDate var1, LocalDate var2);

    public List<Assignment> findBySchoolIdAndAssignmentDate(String var1, LocalDate var2);

    public List<Assignment> findBySchoolIdAndTeacherIdAndDueDate(String var1, String var2, LocalDate var3);

    public Optional<Assignment> findByIdAndSchoolId(String var1, String var2);
}

