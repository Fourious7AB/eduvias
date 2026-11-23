/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.StudentAdmission.exception.PlanNotExist
 *  com.neel.StudentAdmission.model.Plan
 *  com.neel.StudentAdmission.model.Plan$Features
 *  com.neel.StudentAdmission.repo.PlansRepository
 *  com.neel.StudentAdmission.service.PlanService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 */
package com.neel.StudentAdmission.service;

import com.neel.StudentAdmission.exception.PlanNotExist;
import com.neel.StudentAdmission.model.Plan;
import com.neel.StudentAdmission.repo.PlansRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlanService {
    @Autowired
    private PlansRepository plansRepository;

    public Plan createPlan(Plan plan) {
        plan.setCreateAt(LocalDateTime.now());
        if (this.plansRepository.existsByPlanName(plan.getPlanName())) {
            throw new PlanNotExist("Plan with the same name already exists");
        }
        if (plan.getDuration() <= 0) {
            throw new PlanNotExist("Duration must be greater than 0");
        }
        plan.setStatus(true);
        return (Plan)this.plansRepository.save(plan);
    }

    public List<Plan> getAllPlans() {
        return this.plansRepository.findAll();
    }

    public Optional<Plan> getPlanById(String id) {
        return this.plansRepository.findById(id);
    }

    public Optional<Plan> updatePlan(String id, Plan newPlan) {
        Optional existPlan = this.plansRepository.findById(id);
        if (existPlan.isPresent() && newPlan != null) {
            Plan updatePlan = (Plan)existPlan.get();
            if (newPlan.getPlanName() != null && !newPlan.getPlanName().isEmpty()) {
                updatePlan.setPlanName(newPlan.getPlanName());
            }
            if (newPlan.getPrice() != null) {
                updatePlan.setPrice(newPlan.getPrice());
            }
            Set existingDescriptions = updatePlan.getFeatures().stream().map(Plan.Features::getDescription).collect(Collectors.toSet());
            ArrayList<Plan.Features> mergedFeatures = new ArrayList<Plan.Features>(updatePlan.getFeatures());
            for (Plan.Features newFeature : newPlan.getFeatures()) {
                if (existingDescriptions.contains(newFeature.getDescription())) continue;
                mergedFeatures.add(newFeature);
            }
            updatePlan.setFeatures(mergedFeatures);
            if (newPlan.getDuration() > 0) {
                updatePlan.setDuration(newPlan.getDuration());
            }
            this.plansRepository.save(updatePlan);
        }
        return existPlan;
    }

    public Optional<Plan> deletePlan(String id) {
        Optional plan = this.plansRepository.findById(id);
        if (plan.isPresent()) {
            this.plansRepository.deleteById(id);
        }
        return plan;
    }
}

