/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.AcademicMain.models.Subject_Assign
 *  com.neel.AcademicMain.repo.SubjectAssignRepository
 *  org.springframework.data.mongodb.repository.MongoRepository
 */
package com.neel.AcademicMain.repo;

import com.neel.AcademicMain.models.Subject_Assign;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SubjectAssignRepository
extends MongoRepository<Subject_Assign, String> {
    public Subject_Assign findTopByOrderBySubjectAssignIdDesc();

    public List<Subject_Assign> findByClassId(String var1);

    public List<Subject_Assign> findBySchoolId(String var1);

    public List<Subject_Assign> findByClassIdAndSchoolId(String var1, String var2);
}

