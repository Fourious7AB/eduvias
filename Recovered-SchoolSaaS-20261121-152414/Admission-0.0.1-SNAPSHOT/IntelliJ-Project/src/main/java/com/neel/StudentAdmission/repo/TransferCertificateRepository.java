/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.StudentAdmission.model.TransferCertificate
 *  com.neel.StudentAdmission.repo.TransferCertificateRepository
 *  org.springframework.data.mongodb.repository.MongoRepository
 *  org.springframework.stereotype.Repository
 */
package com.neel.StudentAdmission.repo;

import com.neel.StudentAdmission.model.TransferCertificate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferCertificateRepository
extends MongoRepository<TransferCertificate, String> {
    public Optional<TransferCertificate> findByTcNumber(String var1);

    public Optional<TransferCertificate> findByAdmissionNumberAndSchoolId(String var1, String var2);

    public List<TransferCertificate> findBySchoolId(String var1);

    public boolean existsByAdmissionNumberAndSchoolId(String var1, String var2);

    public Optional<TransferCertificate> findByIdAndSchoolId(String var1, String var2);
}

