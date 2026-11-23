/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.Auth.model.AuthUser
 *  com.neel.Auth.repo.AuthUserRepository
 *  org.springframework.data.mongodb.repository.MongoRepository
 */
package com.neel.Auth.repo;

import com.neel.Auth.model.AuthUser;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AuthUserRepository
extends MongoRepository<AuthUser, String> {
    public Optional<AuthUser> findByUsername(String var1);

    public boolean existsByUsername(String var1);

    public void deleteByUsername(String var1);
}

