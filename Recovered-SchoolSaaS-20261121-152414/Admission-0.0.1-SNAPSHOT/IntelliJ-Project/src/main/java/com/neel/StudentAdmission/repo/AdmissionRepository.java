/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.StudentAdmission.model.Admission
 *  com.neel.StudentAdmission.repo.AdmissionRepository
 *  org.springframework.data.mongodb.repository.MongoRepository
 *  org.springframework.stereotype.Repository
 */
package com.neel.StudentAdmission.repo;

import com.neel.StudentAdmission.model.Admission;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdmissionRepository
extends MongoRepository<Admission, String> {
    public Optional<Admission> findByAdmissionNumber(String var1);

    public List<Admission> findAllByAdmissionNumberAndSchoolId(String var1, String var2);

    public boolean existsByAdmissionNumber(String var1);

    public boolean existsByAdmissionNumberAndSchoolId(String var1, String var2);

    public List<Admission> findByClassNameIgnoreCaseAndSectionIgnoreCase(String var1, String var2);

    public List<Admission> findByClassNameIgnoreCaseAndSectionIgnoreCaseAndSchoolIdAndAcademicYear(String var1, String var2, String var3, String var4);

    public List<Admission> findBySchoolId(String var1);

    public Optional<Admission> findByAdmissionNumberAndAcademicYear(String var1, String var2);

    public Optional<Admission> findByAcademicYearAndEmailAndSchoolId(String var1, String var2, String var3);

    public Optional<Admission> findByAdmissionNumberAndSchoolId(String var1, String var2);

    public List<Admission> findByStatusAndSchoolId(String var1, String var2);

    public Optional<Admission> findByRollNoAndClassNameIgnoreCaseAndSectionIgnoreCaseAndSchoolId(String var1, String var2, String var3, String var4);

    public List<Admission> findByClassNameIgnoreCaseAndSectionIgnoreCaseAndSchoolId(String var1, String var2, String var3);

    public Optional<Admission> findByRollNoAndClassNameIgnoreCaseAndSectionIgnoreCaseAndSchoolIdAndAcademicYear(String var1, String var2, String var3, String var4, String var5);

    public long countBySchoolId(String var1);

    public Optional<Admission> findByIdAndSchoolId(String var1, String var2);
}

