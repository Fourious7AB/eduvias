/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.AcademicMain.models.Section
 *  com.neel.AcademicMain.repo.SectionRepository
 *  com.neel.AcademicMain.services.SectionService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 */
package com.neel.AcademicMain.services;

import com.neel.AcademicMain.models.Section;
import com.neel.AcademicMain.repo.SectionRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SectionService {
    @Autowired
    private SectionRepository sectionRepository;

    public List<Section> getAllSections() {
        return this.sectionRepository.findAll();
    }

    public List<Section> getSectionsBySchoolId(String schoolId) {
        return this.sectionRepository.findBySchoolId(schoolId);
    }

    public List<Section> getUniversalSections() {
        return this.sectionRepository.findByUniversalIsTrue();
    }

    public List<Section> getSectionsForSchool(String schoolId) {
        return this.sectionRepository.findBySchoolIdOrUniversalIsTrue(schoolId, true);
    }

    public Section addSection(Section sections) {
        if (!this.checkIfSectionExists(sections.getSectionName(), sections.getSchoolId())) {
            Section lastSection = this.sectionRepository.findTopByOrderBySectionIdDesc();
            int nextSectionId = lastSection != null ? lastSection.getSectionId() + 1 : 1;
            sections.setSectionId(nextSectionId);
            return this.sectionRepository.save(sections);
        }
        if (sections.isUniversal()) {
            throw new IllegalArgumentException("Universal section with this name already exists!");
        }
        throw new IllegalArgumentException("Section name already exists for this school!");
    }

    public Section updateSection(String schoolId, String id, Section updatedSection) {
        Section existingSection = this.sectionRepository.findById(id).orElseThrow(() -> new RuntimeException("Section not found"));
        if (existingSection.isUniversal()) {
            updatedSection.setUniversal(true);
        } else {
            if (!existingSection.getSchoolId().equals(schoolId)) {
                throw new IllegalArgumentException("Section does not belong to the specified school");
            }
            updatedSection.setSchoolId(schoolId);
        }
        if (!this.isSectionNameUniqueForUpdate(updatedSection.getSectionName(), schoolId, id)) {
            throw new IllegalArgumentException("Section name already exists!");
        }
        existingSection.setSectionName(updatedSection.getSectionName());
        existingSection.setSection_Code(updatedSection.getSection_Code());
        existingSection.setUniversal(updatedSection.isUniversal());
        return this.sectionRepository.save(existingSection);
    }

    public void deleteSection(String schoolId, String id) {
        Section section = this.sectionRepository.findById(id).orElseThrow(() -> new RuntimeException("Section not found"));
        if (!section.isUniversal() && !section.getSchoolId().equals(schoolId)) {
            throw new IllegalArgumentException("Section does not belong to the specified school");
        }
        this.sectionRepository.deleteById(id);
    }

    public Section addUniversalSection(Section section) {
        section.setUniversal(true);
        section.setSchoolId(null);
        if (!this.checkIfSectionExists(section.getSectionName(), null)) {
            Section lastSection = this.sectionRepository.findTopByOrderBySectionIdDesc();
            int nextSectionId = lastSection != null ? lastSection.getSectionId() + 1 : 1;
            section.setSectionId(nextSectionId);
            return this.sectionRepository.save(section);
        }
        throw new IllegalArgumentException("Section with this name already exists!");
    }

    private boolean checkIfSectionExists(String sectionName, String schoolId) {
        List<Section> universalSections = this.sectionRepository.findByUniversalIsTrue();
        boolean universalExists = universalSections.stream().anyMatch(s -> s.getSectionName().equalsIgnoreCase(sectionName));
        if (universalExists) {
            return true;
        }
        if (schoolId != null) {
            List<Section> schoolSections = this.sectionRepository.findBySchoolId(schoolId);
            return schoolSections.stream().anyMatch(s -> s.getSectionName().equalsIgnoreCase(sectionName));
        }
        return false;
    }

    private boolean isSectionNameUniqueForUpdate(String sectionName, String schoolId, String currentSectionId) {
        List<Section> universalSections = this.sectionRepository.findByUniversalIsTrue();
        boolean duplicateUniversal = universalSections.stream().anyMatch(s -> s.getSectionName().equalsIgnoreCase(sectionName) && !s.getId().equals(currentSectionId));
        if (duplicateUniversal) {
            return false;
        }
        if (schoolId != null) {
            List<Section> schoolSections = this.sectionRepository.findBySchoolId(schoolId);
            return schoolSections.stream().noneMatch(s -> s.getSectionName().equalsIgnoreCase(sectionName) && !s.getId().equals(currentSectionId));
        }
        return true;
    }
}

