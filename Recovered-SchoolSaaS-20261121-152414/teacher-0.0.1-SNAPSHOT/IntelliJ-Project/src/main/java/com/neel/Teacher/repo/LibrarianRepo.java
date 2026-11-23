/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.Teacher.model.Librarian
 *  com.neel.Teacher.repo.LibrarianRepo
 *  org.springframework.data.mongodb.repository.MongoRepository
 *  org.springframework.stereotype.Repository
 */
package com.neel.Teacher.repo;

import com.neel.Teacher.model.Librarian;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibrarianRepo
extends MongoRepository<Librarian, String> {
    public Optional<Librarian> findByEmail(String var1);

    public Optional<Librarian> findByEmailAndSchoolId(String var1, String var2);

    public boolean existsByEmail(String var1);

    public boolean existsByEmailAndSchoolId(String var1, String var2);

    public List<Librarian> findBySchoolId(String var1);

    public Optional<Librarian> findByIdAndSchoolId(String var1, String var2);
}

