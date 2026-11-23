/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.schoolaccount.SchoolAccount.model.SuperAdminAccount
 *  com.schoolaccount.SchoolAccount.model.SuperAdminAccount$Transaction
 *  com.schoolaccount.SchoolAccount.repo.SuperAdminAccountRepository
 *  org.springframework.data.mongodb.repository.MongoRepository
 *  org.springframework.data.mongodb.repository.Query
 *  org.springframework.stereotype.Repository
 */
package com.schoolaccount.SchoolAccount.repo;

import com.schoolaccount.SchoolAccount.model.SuperAdminAccount;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SuperAdminAccountRepository
extends MongoRepository<SuperAdminAccount, String> {
    public Optional<SuperAdminAccount> findFirstByOrderByIdAsc();

    @Query(value="{ 'recentTransactions.schoolId': ?0 }")
    public List<SuperAdminAccount.Transaction> findTransactionsBySchoolId(String var1);

    @Query(value="{ 'recentTransactions.status': ?0 }")
    public List<SuperAdminAccount.Transaction> findTransactionsByStatus(String var1);

    @Query(value="{ 'recentTransactions.timestamp': { $gte: ?0, $lte: ?1 } }")
    public List<SuperAdminAccount.Transaction> findTransactionsByDateRange(LocalDateTime var1, LocalDateTime var2);

    @Query(value="{ 'recentTransactions.status': 'RECEIVED' }")
    public List<SuperAdminAccount.Transaction> findPendingDistributions();

    @Query(value="{ 'recentTransactions.admissionNumber': ?0 }")
    public List<SuperAdminAccount.Transaction> findTransactionsByAdmissionNumber(String var1);
}

