/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.NewLibrary.model.BookIssue
 *  com.neel.NewLibrary.repo.BookIssueRepository
 *  org.springframework.data.mongodb.repository.MongoRepository
 */
package com.neel.NewLibrary.repo;

import com.neel.NewLibrary.model.BookIssue;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookIssueRepository
extends MongoRepository<BookIssue, String> {
    public List<BookIssue> findByStudentId(String var1);

    public List<BookIssue> findByBookNumberAndStudentId(String var1, String var2);

    public List<BookIssue> findBySchoolId(String var1);

    public List<BookIssue> findByStudentIdAndSchoolId(String var1, String var2);

    public List<BookIssue> findByBookNumberAndStudentIdAndSchoolId(String var1, String var2, String var3);

    public Optional<BookIssue> findByIdAndSchoolId(String var1, String var2);
}

