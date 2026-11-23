/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.NewLibrary.model.Category
 *  com.neel.NewLibrary.model.ResponseMessage
 *  com.neel.NewLibrary.repo.CategoryRepository
 *  com.neel.NewLibrary.service.CategoryService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 */
package com.neel.NewLibrary.service;

import com.neel.NewLibrary.model.Category;
import com.neel.NewLibrary.model.ResponseMessage;
import com.neel.NewLibrary.repo.CategoryRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public Object saveCategory(Category category) {
        category.setSchoolId("67ca9167d56ec91fc9539234");
        return this.processCategorySave(category);
    }

    public Object updateCategory(Category category) {
        category.setSchoolId("67ca9167d56ec91fc9539234");
        return this.processCategorySave(category);
    }

    public List<Category> getAllCategories() {
        return this.categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(String id) {
        return this.categoryRepository.findById((Object)id);
    }

    public void deleteCategory(String id) {
        this.categoryRepository.deleteById((Object)id);
    }

    public Object saveCategoryWithSchool(Category category, String schoolId) {
        category.setSchoolId(schoolId);
        return this.processCategorySave(category);
    }

    public Object updateCategoryWithSchool(Category category, String schoolId) {
        category.setSchoolId(schoolId);
        return this.processCategorySave(category);
    }

    private Object processCategorySave(Category category) {
        Optional existingCategory = this.categoryRepository.findByBookCategoryCodeAndSchoolId(category.getBookCategoryCode(), category.getSchoolId());
        if (existingCategory.isPresent() && !((Category)existingCategory.get()).getId().equals(category.getId())) {
            return new ResponseMessage("Error: Category with this code already exists in this school", null);
        }
        return this.categoryRepository.save((Object)category);
    }

    public List<Category> getAllCategoriesBySchool(String schoolId) {
        return this.categoryRepository.findBySchoolId(schoolId);
    }

    public Optional<Category> getCategoryByIdAndSchool(String id, String schoolId) {
        return this.categoryRepository.findByIdAndSchoolId(id, schoolId);
    }

    public boolean deleteCategoryWithSchool(String id, String schoolId) {
        Optional category = this.categoryRepository.findByIdAndSchoolId(id, schoolId);
        if (category.isPresent()) {
            this.categoryRepository.delete((Object)((Category)category.get()));
            return true;
        }
        return false;
    }

    public long totalCategoryBySchool(String schoolId) {
        List allCategory = this.categoryRepository.findBySchoolId(schoolId);
        return allCategory.size();
    }
}

