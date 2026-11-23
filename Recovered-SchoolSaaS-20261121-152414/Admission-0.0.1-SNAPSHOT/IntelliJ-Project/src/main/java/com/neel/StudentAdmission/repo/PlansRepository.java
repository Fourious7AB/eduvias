/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.StudentAdmission.model.Plan
 *  com.neel.StudentAdmission.repo.PlansRepository
 *  org.hibernate.validator.constraints.UniqueElements
 *  org.springframework.data.mongodb.repository.MongoRepository
 *  org.springframework.stereotype.Repository
 */
package com.neel.StudentAdmission.repo;

import com.neel.StudentAdmission.model.Plan;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlansRepository
extends MongoRepository<Plan, String> {
    public boolean existsByPlanName(@UniqueElements String var1);
}

