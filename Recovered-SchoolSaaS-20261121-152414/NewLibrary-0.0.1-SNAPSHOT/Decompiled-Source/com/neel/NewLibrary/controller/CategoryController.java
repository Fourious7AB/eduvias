/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.NewLibrary.controller.CategoryController
 *  com.neel.NewLibrary.model.Category
 *  com.neel.NewLibrary.model.ResponseMessage
 *  com.neel.NewLibrary.service.CategoryService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.http.HttpStatus
 *  org.springframework.http.HttpStatusCode
 *  org.springframework.http.ResponseEntity
 *  org.springframework.web.bind.annotation.CrossOrigin
 *  org.springframework.web.bind.annotation.DeleteMapping
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.PutMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.neel.NewLibrary.controller;

import com.neel.NewLibrary.model.Category;
import com.neel.NewLibrary.model.ResponseMessage;
import com.neel.NewLibrary.service.CategoryService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins={"*"})
@RequestMapping(value={"/api/v1/library"})
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping(value={"/categories"})
    public ResponseEntity<Object> createOrUpdateCategory(@RequestBody Category category) {
        Object result = this.categoryService.saveCategory(category);
        if (result instanceof ResponseMessage) {
            return new ResponseEntity(result, (HttpStatusCode)HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity((Object)new ResponseMessage("Category created/updated successfully", result), (HttpStatusCode)HttpStatus.CREATED);
    }

    @GetMapping(value={"/categories"})
    public ResponseEntity<Object> getAllCategories() {
        List categories = this.categoryService.getAllCategories();
        if (categories.isEmpty()) {
            return new ResponseEntity((Object)new ResponseMessage("No categories found", null), (HttpStatusCode)HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity((Object)new ResponseMessage("Categories retrieved successfully", (Object)categories), (HttpStatusCode)HttpStatus.OK);
    }

    @GetMapping(value={"/categories/id/{id}"})
    public ResponseEntity<Object> getCategoryById(@PathVariable String id) {
        Optional category = this.categoryService.getCategoryById(id);
        if (category.isPresent()) {
            return new ResponseEntity((Object)new ResponseMessage("Category retrieved successfully", category.get()), (HttpStatusCode)HttpStatus.OK);
        }
        return new ResponseEntity((Object)new ResponseMessage("Category not found", null), (HttpStatusCode)HttpStatus.NOT_FOUND);
    }

    @PutMapping(value={"/categories/{id}"})
    public ResponseEntity<Object> updateCategory(@PathVariable String id, @RequestBody Category category) {
        Optional existingCategory = this.categoryService.getCategoryById(id);
        if (existingCategory.isPresent()) {
            category.setId(id);
            Object result = this.categoryService.updateCategory(category);
            if (result instanceof ResponseMessage) {
                return new ResponseEntity(result, (HttpStatusCode)HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity((Object)new ResponseMessage("Category updated successfully", result), (HttpStatusCode)HttpStatus.OK);
        }
        return new ResponseEntity((Object)new ResponseMessage("Category not found", null), (HttpStatusCode)HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value={"/categories/{id}"})
    public ResponseEntity<Object> deleteCategory(@PathVariable String id) {
        Optional category = this.categoryService.getCategoryById(id);
        if (category.isPresent()) {
            this.categoryService.deleteCategory(id);
            return new ResponseEntity((Object)new ResponseMessage("Category deleted successfully", null), (HttpStatusCode)HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity((Object)new ResponseMessage("Category not found for deletion", null), (HttpStatusCode)HttpStatus.NOT_FOUND);
    }

    @PostMapping(value={"/categories/{schoolId}"})
    public ResponseEntity<Object> createOrUpdateCategoryWithSchool(@PathVariable String schoolId, @RequestBody Category category) {
        Object result = this.categoryService.saveCategoryWithSchool(category, schoolId);
        if (result instanceof ResponseMessage) {
            return new ResponseEntity(result, (HttpStatusCode)HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity((Object)new ResponseMessage("Category created/updated successfully", result), (HttpStatusCode)HttpStatus.CREATED);
    }

    @GetMapping(value={"/categories/{schoolId}"})
    public ResponseEntity<Object> getAllCategoriesBySchool(@PathVariable String schoolId) {
        List categories = this.categoryService.getAllCategoriesBySchool(schoolId);
        if (categories.isEmpty()) {
            return new ResponseEntity((Object)new ResponseMessage("No categories found for this school", null), (HttpStatusCode)HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity((Object)new ResponseMessage("Categories retrieved successfully", (Object)categories), (HttpStatusCode)HttpStatus.OK);
    }

    @GetMapping(value={"/categories/{id}/{schoolId}"})
    public ResponseEntity<Object> getCategoryByIdAndSchool(@PathVariable String id, @PathVariable String schoolId) {
        Optional category = this.categoryService.getCategoryByIdAndSchool(id, schoolId);
        if (category.isPresent()) {
            return new ResponseEntity((Object)new ResponseMessage("Category retrieved successfully", category.get()), (HttpStatusCode)HttpStatus.OK);
        }
        return new ResponseEntity((Object)new ResponseMessage("Category not found for this school", null), (HttpStatusCode)HttpStatus.NOT_FOUND);
    }

    @PutMapping(value={"/categories/{id}/{schoolId}"})
    public ResponseEntity<Object> updateCategoryWithSchool(@PathVariable String id, @PathVariable String schoolId, @RequestBody Category category) {
        Optional existingCategory = this.categoryService.getCategoryByIdAndSchool(id, schoolId);
        if (existingCategory.isPresent()) {
            category.setId(id);
            category.setSchoolId(schoolId);
            Object result = this.categoryService.updateCategoryWithSchool(category, schoolId);
            if (result instanceof ResponseMessage) {
                return new ResponseEntity(result, (HttpStatusCode)HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity((Object)new ResponseMessage("Category updated successfully", result), (HttpStatusCode)HttpStatus.OK);
        }
        return new ResponseEntity((Object)new ResponseMessage("Category not found for this school", null), (HttpStatusCode)HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value={"/categories/{id}/{schoolId}"})
    public ResponseEntity<Object> deleteCategoryWithSchool(@PathVariable String id, @PathVariable String schoolId) {
        boolean deleted = this.categoryService.deleteCategoryWithSchool(id, schoolId);
        if (deleted) {
            return new ResponseEntity((Object)new ResponseMessage("Category deleted successfully", null), (HttpStatusCode)HttpStatus.OK);
        }
        return new ResponseEntity((Object)new ResponseMessage("Category not found for deletion in this school", null), (HttpStatusCode)HttpStatus.NOT_FOUND);
    }
}

