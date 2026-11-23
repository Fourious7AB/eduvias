/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.schoolaccount.SchoolAccount.model.IncomePage
 *  com.schoolaccount.SchoolAccount.repo.IncomePageRepository
 *  org.springframework.data.mongodb.repository.Aggregation
 *  org.springframework.data.mongodb.repository.MongoRepository
 *  org.springframework.stereotype.Repository
 */
package com.schoolaccount.SchoolAccount.repo;

import com.schoolaccount.SchoolAccount.model.IncomePage;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncomePageRepository
extends MongoRepository<IncomePage, String> {
    public List<IncomePage> findByDateBetween(LocalDate var1, LocalDate var2);

    @Aggregation(pipeline={"{ $match: { paymentMethod: ?0 } }", "{ $group: { _id: null, total: { $sum: '$amount' } } }"})
    public Double sumByPaymentMethod(String var1);

    @Aggregation(pipeline={"{ $match: { paymentMethod: { $ne: 'cash' } } }", "{ $group: { _id: null, total: { $sum: '$amount' } } }"})
    public Double sumByPaymentMethodNot(String var1);

    public List<IncomePage> findBySchoolId(String var1);

    public List<IncomePage> findByDateBetweenAndSchoolId(LocalDate var1, LocalDate var2, String var3);

    @Aggregation(pipeline={"{ $match: { paymentMethod: ?0, schoolId: ?1 } }", "{ $group: { _id: null, total: { $sum: '$amount' } } }"})
    public Double sumByPaymentMethodAndSchoolId(String var1, String var2);

    @Aggregation(pipeline={"{ $match: { paymentMethod: { $ne: ?0 }, schoolId: ?1 } }", "{ $group: { _id: null, total: { $sum: '$amount' } } }"})
    public Double sumByPaymentMethodNotAndSchoolId(String var1, String var2);

    public List<IncomePage> findBySchoolIdAndDateBetween(String var1, LocalDate var2, LocalDate var3);
}

