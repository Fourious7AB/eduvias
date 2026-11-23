/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.schoolaccount.SchoolAccount.model.StudentAdmissionFee
 *  com.schoolaccount.SchoolAccount.repo.StudentAdmissionFeeRepository
 *  org.springframework.data.mongodb.repository.MongoRepository
 */
package com.schoolaccount.SchoolAccount.repo;

import com.schoolaccount.SchoolAccount.model.StudentAdmissionFee;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StudentAdmissionFeeRepository
extends MongoRepository<StudentAdmissionFee, String> {
    public Optional<StudentAdmissionFee> findByAdmissionNumber(String var1);

    public Optional<StudentAdmissionFee> findByAdmissionNumberAndAcademicYearAndSchoolId(String var1, String var2, String var3);

    public List<StudentAdmissionFee> findBySchoolId(String var1);

    public boolean existsByAdmissionNumberAndSchoolId(String var1, String var2);

    public List<StudentAdmissionFee> findAllByAdmissionNumberAndSchoolId(String var1, String var2);

    public Optional<StudentAdmissionFee> findByAdmissionNumberAndSchoolId(String var1, String var2);

    public List<StudentAdmissionFee> findByClassNameAndSchoolId(String var1, String var2);

    public List<StudentAdmissionFee> findBySchoolIdAndAcademicYear(String var1, String var2);

    public List<StudentAdmissionFee> findBySchoolIdAndAcademicYearAndAdmissionNumber(String var1, String var2, String var3);
}

