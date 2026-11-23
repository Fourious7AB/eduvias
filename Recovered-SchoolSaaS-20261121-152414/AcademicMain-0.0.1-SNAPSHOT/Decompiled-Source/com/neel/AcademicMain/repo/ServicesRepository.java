/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.AcademicMain.models.Services
 *  com.neel.AcademicMain.repo.ServicesRepository
 *  org.springframework.data.mongodb.repository.MongoRepository
 */
package com.neel.AcademicMain.repo;

import com.neel.AcademicMain.models.Services;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ServicesRepository
extends MongoRepository<Services, String> {
    public List<Services> findByUniversalTrue();

    public List<Services> findBySchoolId(String var1);

    public List<Services> findByUniversalTrueOrSchoolId(String var1);

    public List<Services> findByServiceTypeAndUniversalTrue(String var1);

    public List<Services> findByServiceTypeAndSchoolId(String var1, String var2);
}

