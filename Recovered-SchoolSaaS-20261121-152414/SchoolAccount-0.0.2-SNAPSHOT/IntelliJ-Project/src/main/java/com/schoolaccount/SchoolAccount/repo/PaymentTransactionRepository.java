/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.schoolaccount.SchoolAccount.model.PaymentTransaction
 *  com.schoolaccount.SchoolAccount.repo.PaymentTransactionRepository
 *  org.springframework.data.mongodb.repository.MongoRepository
 *  org.springframework.stereotype.Repository
 */
package com.schoolaccount.SchoolAccount.repo;

import com.schoolaccount.SchoolAccount.model.PaymentTransaction;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentTransactionRepository
extends MongoRepository<PaymentTransaction, String> {
    public List<PaymentTransaction> findBySchoolId(String var1);

    public List<PaymentTransaction> findByStudentId(String var1);

    public List<PaymentTransaction> findByAdmissionNumber(String var1);

    public List<PaymentTransaction> findByDistributedToSchool(boolean var1);

    public List<PaymentTransaction> findBySchoolIdAndCreatedAtBetween(String var1, LocalDateTime var2, LocalDateTime var3);
}

