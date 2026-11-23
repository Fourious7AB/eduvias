/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.NewLibrary.model.Book
 *  com.neel.NewLibrary.repo.BookRepository
 *  org.springframework.data.mongodb.repository.MongoRepository
 *  org.springframework.stereotype.Repository
 */
package com.neel.NewLibrary.repo;

import com.neel.NewLibrary.model.Book;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository
extends MongoRepository<Book, String> {
    public List<Book> findBySchoolId(String var1);

    public Optional<Book> findByIdAndSchoolId(String var1, String var2);

    public List<Book> findByBookCategoryAndSchoolId(String var1, String var2);

    public List<Book> findByClassAssignedAndSchoolId(String var1, String var2);

    public Optional<Book> findByBookNumberAndSchoolId(String var1, String var2);
}

