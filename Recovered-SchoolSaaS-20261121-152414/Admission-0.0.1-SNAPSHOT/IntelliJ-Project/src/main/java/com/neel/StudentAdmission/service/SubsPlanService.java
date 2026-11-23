/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.StudentAdmission.exception.ClassNotFoundInAcademicServiceException
 *  com.neel.StudentAdmission.exception.PlanNotExist
 *  com.neel.StudentAdmission.model.PaymentDetails
 *  com.neel.StudentAdmission.model.Plan
 *  com.neel.StudentAdmission.model.SignUp
 *  com.neel.StudentAdmission.model.SubscriptionPlan
 *  com.neel.StudentAdmission.repo.PlansRepository
 *  com.neel.StudentAdmission.repo.SubscriptionPlanRepository
 *  com.neel.StudentAdmission.service.SignUpService
 *  com.neel.StudentAdmission.service.SubsPlanService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.scheduling.annotation.Scheduled
 *  org.springframework.stereotype.Service
 */
package com.neel.StudentAdmission.service;

import com.neel.StudentAdmission.exception.ClassNotFoundInAcademicServiceException;
import com.neel.StudentAdmission.exception.PlanNotExist;
import com.neel.StudentAdmission.model.PaymentDetails;
import com.neel.StudentAdmission.model.Plan;
import com.neel.StudentAdmission.model.SignUp;
import com.neel.StudentAdmission.model.SubscriptionPlan;
import com.neel.StudentAdmission.repo.PlansRepository;
import com.neel.StudentAdmission.repo.SubscriptionPlanRepository;
import com.neel.StudentAdmission.service.SignUpService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SubsPlanService {
    @Autowired
    private SubscriptionPlanRepository subscriptionRepository;
    @Autowired
    private SignUpService signUpService;
    @Autowired
    private PlansRepository plansRepository;

    public SubscriptionPlan createSubscription(String planId, String schoolId) {
        Plan plan = (Plan)this.plansRepository.findById(planId).orElseThrow(() -> new ClassNotFoundInAcademicServiceException("Plan not found with id: " + planId));
        System.out.println(" i m from plan : " + plan);
        System.out.println(" i m from schoolId : " + schoolId);
        SubscriptionPlan existingSubscription = this.subscriptionRepository.findByPlanIdAndSchoolId(planId, schoolId);
        System.out.println(" i m from exis : " + existingSubscription);
        LocalDateTime startDate = LocalDateTime.now();
        if (plan.getDuration() <= 0) {
            throw new PlanNotExist("Invalid plan duration: Must be greater than 0");
        }
        LocalDateTime endDate = startDate.plusDays(plan.getDuration());
        if (existingSubscription != null) {
            existingSubscription.setPlanId(planId);
            existingSubscription.setPlanName(plan.getPlanName());
            existingSubscription.setDuration(Integer.valueOf(plan.getDuration()));
            existingSubscription.setFeatures(plan.getFeatures());
            existingSubscription.setAmount(plan.getPrice());
            existingSubscription.setStartDate(startDate);
            existingSubscription.setEndDate(endDate);
            existingSubscription.setStatus(Boolean.valueOf(false));
            return (SubscriptionPlan)this.subscriptionRepository.save(existingSubscription);
        }
        SubscriptionPlan newSubscriptionPlan = new SubscriptionPlan();
        newSubscriptionPlan.setPlanName(plan.getPlanName());
        newSubscriptionPlan.setPlanId(planId);
        newSubscriptionPlan.setDuration(Integer.valueOf(plan.getDuration()));
        newSubscriptionPlan.setSchoolId(schoolId);
        System.out.println(" i m from else part");
        newSubscriptionPlan.setFeatures(plan.getFeatures());
        newSubscriptionPlan.setAmount(plan.getPrice());
        newSubscriptionPlan.setStartDate(startDate);
        newSubscriptionPlan.setEndDate(endDate);
        newSubscriptionPlan.setStatus(Boolean.valueOf(false));
        newSubscriptionPlan.setCreateAt(LocalDateTime.now());
        return (SubscriptionPlan)this.subscriptionRepository.save(newSubscriptionPlan);
    }

    public SubscriptionPlan toggleSubscriptionStatus(String id) {
        SubscriptionPlan subscription = (SubscriptionPlan)this.subscriptionRepository.findById(id).orElseThrow(() -> new PlanNotExist("Subscription not found with id: " + id));
        if (!Boolean.TRUE.equals(subscription.getStatus())) {
            subscription.setStatus(Boolean.valueOf(true));
            this.subscriptionRepository.save(subscription);
        }
        return subscription;
    }

    public List<SubscriptionPlan> allSubscriptionPayment() {
        return this.subscriptionRepository.findAll();
    }

    public List<SubscriptionPlan> getAllPlanHistoryBySchool(String schoolId) {
        return this.subscriptionRepository.findBySchoolId(schoolId);
    }

    @Scheduled(cron="0 0 0 * * ?")
    public void updateExpiredSubscriptions() {
        LocalDate currentDate = LocalDate.now();
        SignUp signUp = new SignUp();
        List<SubscriptionPlan> expiredSubscriptions = this.subscriptionRepository.findByEndDateBeforeAndStatusTrue(currentDate);
        expiredSubscriptions.forEach(subscription -> {
            subscription.setStatus(Boolean.valueOf(false));
            signUp.setActive(Boolean.valueOf(false));
            this.subscriptionRepository.save(subscription);
        });
        System.out.println("Expired subscriptions have been updated.");
    }

    public SubscriptionPlan updatePayments(SubscriptionPlan payment) {
        this.signUpService.updateSubsPlan(payment.getSchoolId(), payment);
        return (SubscriptionPlan)this.subscriptionRepository.save(payment);
    }

    public SubscriptionPlan updatePaymentStatus(String orderId, String paymentId) {
        Optional subscriptionOptional = this.subscriptionRepository.findByPaymentDetails_OrderId(orderId);
        if (subscriptionOptional.isPresent()) {
            SubscriptionPlan subscription = (SubscriptionPlan)subscriptionOptional.get();
            subscription.setStatus(Boolean.valueOf(true));
            return (SubscriptionPlan)this.subscriptionRepository.save(subscription);
        }
        throw new RuntimeException("Subscription not found for Order ID: " + orderId);
    }

    public SubscriptionPlan renewSubscription(String planId, String schoolId) {
        Plan plan = (Plan)this.plansRepository.findById(planId).orElseThrow(() -> new RuntimeException("Plan not found with id: " + planId));
        SubscriptionPlan existing = this.subscriptionRepository.findByPlanIdAndSchoolId(planId, schoolId);
        if (existing == null) {
            throw new PlanNotExist("No existing subscription found for this plan and school.");
        }
        LocalDateTime newEnd = existing.getEndDate().plusDays(plan.getDuration());
        PaymentDetails renewalPayment = new PaymentDetails();
        renewalPayment.setAmount(plan.getPrice());
        renewalPayment.setTimestamp(LocalDateTime.now());
        renewalPayment.setStatus("PAID");
        existing.setEndDate(newEnd);
        existing.setStatus(Boolean.valueOf(false));
        return (SubscriptionPlan)this.subscriptionRepository.save(existing);
    }
}

