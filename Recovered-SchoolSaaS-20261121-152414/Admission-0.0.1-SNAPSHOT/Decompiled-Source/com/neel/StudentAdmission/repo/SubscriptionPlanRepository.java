/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.StudentAdmission.model.SubscriptionPlan
 *  com.neel.StudentAdmission.repo.SubscriptionPlanRepository
 *  org.springframework.data.mongodb.repository.MongoRepository
 *  org.springframework.stereotype.Repository
 */
package com.neel.StudentAdmission.repo;

import com.neel.StudentAdmission.model.SubscriptionPlan;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionPlanRepository
extends MongoRepository<SubscriptionPlan, String> {
    public Optional<SubscriptionPlan> findByPaymentDetails_OrderId(String var1);

    public boolean existsBySchoolIdAndStatusTrue(String var1);

    public List<SubscriptionPlan> findByEndDateBeforeAndStatusTrue(LocalDate var1);

    public List<SubscriptionPlan> findBySchoolId(String var1);

    public SubscriptionPlan findByPlanIdAndSchoolId(String var1, String var2);

    public Optional<SubscriptionPlan> findByPlanId(String var1);

    public List<SubscriptionPlan> findByEndDateBetween(LocalDateTime var1, LocalDateTime var2);
}

