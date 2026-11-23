/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.StudentAdmission.model.SignUp
 *  com.neel.StudentAdmission.repo.SignUpRepository
 *  org.springframework.data.mongodb.repository.MongoRepository
 */
package com.neel.StudentAdmission.repo;

import com.neel.StudentAdmission.model.SignUp;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SignUpRepository
extends MongoRepository<SignUp, String> {
    public Optional<SignUp> findByEmail(String var1);

    public Optional<SignUp> findByPhoneNumber(String var1);
}

