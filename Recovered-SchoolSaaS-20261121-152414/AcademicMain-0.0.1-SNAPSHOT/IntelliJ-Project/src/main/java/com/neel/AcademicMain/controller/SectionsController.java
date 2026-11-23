/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.AcademicMain.controller.SectionsController
 *  com.neel.AcademicMain.models.Section
 *  com.neel.AcademicMain.services.SectionService
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
package com.neel.AcademicMain.controller;

import com.neel.AcademicMain.models.Section;
import com.neel.AcademicMain.services.SectionService;
import java.util.List;
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

@CrossOrigin(origins={"*"})
@RestController
@RequestMapping(value={"/api/v1/Academic"})
public class SectionsController {
    @Autowired
    private SectionService sectionService;

    @GetMapping(value={"/sections"})
    public List<Section> getAllSections() {
        return this.sectionService.getAllSections();
    }

    @GetMapping(value={"/sections/{schoolId}"})
    public ResponseEntity<List<Section>> getSectionsBySchoolId(@PathVariable String schoolId) {
        List<Section> sections = this.sectionService.getSectionsBySchoolId(schoolId);
        if (sections.isEmpty()) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(sections);
    }

    @GetMapping(value={"/sections/universal"})
    public ResponseEntity<List<Section>> getUniversalSections() {
        List<Section> sections = this.sectionService.getUniversalSections();
        if (sections.isEmpty()) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(sections);
    }

    @GetMapping(value={"/sections/all/{schoolId}"})
    public ResponseEntity<List<Section>> getAllSectionsForSchool(@PathVariable String schoolId) {
        List<Section> sections = this.sectionService.getSectionsForSchool(schoolId);
        if (sections.isEmpty()) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(sections);
    }

    @PostMapping(value={"/sections/{schoolId}"})
    public ResponseEntity<String> createSection(@PathVariable String schoolId, @RequestBody Section sections) {
        try {
            sections.setSchoolId(schoolId);
            this.sectionService.addSection(sections);
            return ResponseEntity.ok("Section created successfully!");
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value={"/sections/universal"})
    public ResponseEntity<String> createUniversalSection(@RequestBody Section section) {
        try {
            this.sectionService.addUniversalSection(section);
            return ResponseEntity.ok("Universal section created successfully!");
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping(value={"/sections/{id}/{schoolId}"})
    public ResponseEntity<String> updateSection(@PathVariable String schoolId, @PathVariable String id, @RequestBody Section sections) {
        try {
            this.sectionService.updateSection(schoolId, id, sections);
            return ResponseEntity.ok("Section updated successfully!");
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping(value={"/sections/{id}/{schoolId}"})
    public ResponseEntity<String> deleteSection(@PathVariable String schoolId, @PathVariable String id) {
        try {
            this.sectionService.deleteSection(schoolId, id);
            return ResponseEntity.ok("Section deleted successfully!");
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

