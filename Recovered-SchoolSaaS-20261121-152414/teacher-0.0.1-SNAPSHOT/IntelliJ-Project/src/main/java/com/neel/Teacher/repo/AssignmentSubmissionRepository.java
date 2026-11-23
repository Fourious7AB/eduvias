/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.Teacher.model.AssignmentSubmission
 *  com.neel.Teacher.repo.AssignmentSubmissionRepository
 *  org.springframework.data.mongodb.repository.MongoRepository
 *  org.springframework.stereotype.Repository
 */
package com.neel.Teacher.repo;

import com.neel.Teacher.model.AssignmentSubmission;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignmentSubmissionRepository
extends MongoRepository<AssignmentSubmission, String> {
    public List<AssignmentSubmission> findByAssignmentId(String var1);

    public List<AssignmentSubmission> findByStudentId(String var1);

    public List<AssignmentSubmission> findBySchoolIdAndStudentId(String var1, String var2);

    public List<AssignmentSubmission> findByAssignmentIdAndStudentId(String var1, String var2);

    public List<AssignmentSubmission> findBySchoolIdAndAssignmentId(String var1, String var2);
}

