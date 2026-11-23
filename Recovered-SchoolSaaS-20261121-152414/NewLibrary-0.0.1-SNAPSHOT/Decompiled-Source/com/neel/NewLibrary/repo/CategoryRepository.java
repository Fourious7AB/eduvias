/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.NewLibrary.model.Category
 *  com.neel.NewLibrary.repo.CategoryRepository
 *  org.springframework.data.mongodb.repository.MongoRepository
 *  org.springframework.stereotype.Repository
 */
package com.neel.NewLibrary.repo;

import com.neel.NewLibrary.model.Category;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository
extends MongoRepository<Category, String> {
    public Optional<Category> findByBookCategoryCode(String var1);

    public List<Category> findBySchoolId(String var1);

    public Optional<Category> findByBookCategoryCodeAndSchoolId(String var1, String var2);

    public Optional<Category> findByIdAndSchoolId(String var1, String var2);
}

