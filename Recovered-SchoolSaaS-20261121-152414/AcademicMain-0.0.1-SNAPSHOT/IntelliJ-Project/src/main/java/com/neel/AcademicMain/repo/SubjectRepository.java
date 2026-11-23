/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.AcademicMain.models.Subject
 *  com.neel.AcademicMain.repo.SubjectRepository
 *  org.springframework.data.mongodb.repository.MongoRepository
 */
package com.neel.AcademicMain.repo;

import com.neel.AcademicMain.models.Subject;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SubjectRepository
extends MongoRepository<Subject, String> {
    public Subject findTopByOrderBySubjectIdDesc();

    public Optional<Subject> findBySubjectName(String var1);

    public Optional<Subject> findBySubjectNameAndSchoolId(String var1, String var2);

    public List<Subject> findBySchoolId(String var1);

    public List<Subject> findByUniversalIsTrue();

    public List<Subject> findBySchoolIdOrUniversalIsTrue(String var1, boolean var2);
}

