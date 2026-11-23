/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.StudentAdmission.model.Admission
 *  com.neel.StudentAdmission.repo.AdmissionRepository
 *  org.springframework.data.mongodb.repository.MongoRepository
 */
package com.neel.StudentAdmission.repo;

import com.neel.StudentAdmission.model.Admission;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AdmissionRepository
extends MongoRepository<Admission, String> {
    public Optional<Admission> findByAdmissionNumber(String var1);

    public Optional<Admission> findByAdmissionNumberAndSchoolId(String var1, String var2);

    public boolean existsByAdmissionNumber(String var1);

    public boolean existsByAdmissionNumberAndSchoolId(String var1, String var2);

    public List<Admission> findByClassNameAndSection(String var1, String var2);

    public List<Admission> findByClassNameAndSectionAndSchoolId(String var1, String var2, String var3);

    public List<Admission> findBySchoolId(String var1);
}

