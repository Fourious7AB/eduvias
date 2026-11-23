/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.StudentAdmission.model.TransferCertificateRequest
 *  com.neel.StudentAdmission.repo.TransferCertificateRequestRepository
 *  org.springframework.data.mongodb.repository.MongoRepository
 *  org.springframework.stereotype.Repository
 */
package com.neel.StudentAdmission.repo;

import com.neel.StudentAdmission.model.TransferCertificateRequest;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferCertificateRequestRepository
extends MongoRepository<TransferCertificateRequest, String> {
    public List<TransferCertificateRequest> findBySchoolId(String var1);

    public List<TransferCertificateRequest> findBySchoolIdAndStatus(String var1, String var2);

    public List<TransferCertificateRequest> findByAdmissionNumberAndSchoolId(String var1, String var2);

    public List<TransferCertificateRequest> findByAdmissionNumberAndSchoolIdAndStatus(String var1, String var2, String var3);

    public boolean existsByAdmissionNumberAndSchoolIdAndStatusNot(String var1, String var2, String var3);
}

