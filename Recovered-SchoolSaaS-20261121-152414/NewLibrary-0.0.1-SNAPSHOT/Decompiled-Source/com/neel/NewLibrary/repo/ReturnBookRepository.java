/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.NewLibrary.model.ReturnBook
 *  com.neel.NewLibrary.repo.ReturnBookRepository
 *  org.springframework.data.mongodb.repository.MongoRepository
 *  org.springframework.stereotype.Repository
 */
package com.neel.NewLibrary.repo;

import com.neel.NewLibrary.model.ReturnBook;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReturnBookRepository
extends MongoRepository<ReturnBook, String> {
    public List<ReturnBook> findBySchoolId(String var1);

    public Optional<ReturnBook> findByIdAndSchoolId(String var1, String var2);

    public List<ReturnBook> findByStudentIdAndSchoolId(String var1, String var2);
}

