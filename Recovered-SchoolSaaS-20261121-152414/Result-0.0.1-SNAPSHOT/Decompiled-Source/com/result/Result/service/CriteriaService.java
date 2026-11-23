/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.result.Result.model.Criteria
 *  com.result.Result.model.Criteria$Subject
 *  com.result.Result.repo.CriteriaRepository
 *  com.result.Result.service.CriteriaService
 *  lombok.Generated
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 */
package com.result.Result.service;

import com.result.Result.model.Criteria;
import com.result.Result.repo.CriteriaRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CriteriaService {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(CriteriaService.class);
    @Autowired
    private CriteriaRepository criteriaRepository;

    public Criteria createCriteria(String schoolId, Criteria criteria) {
        if (schoolId == null || schoolId.trim().isEmpty()) {
            throw new IllegalArgumentException("School ID cannot be null or empty");
        }
        if (criteria == null) {
            throw new IllegalArgumentException("Criteria cannot be null");
        }
        this.validateCriteria(criteria);
        List existingCriteria = this.criteriaRepository.findAllBySchoolId(schoolId);
        boolean isDuplicate = existingCriteria.stream().anyMatch(existing -> existing.getClassName().equalsIgnoreCase(criteria.getClassName()) && existing.getExamType().equalsIgnoreCase(criteria.getExamType()));
        if (isDuplicate) {
            throw new IllegalArgumentException("Criteria for this class and exam type already exists for this school");
        }
        criteria.setSchoolId(schoolId);
        Criteria.Subject subject = new Criteria.Subject();
        subject.setCreateAt(LocalDate.now());
        return (Criteria)this.criteriaRepository.save((Object)criteria);
    }

    public List<Criteria> getAllCriteria(String schoolId) {
        if (schoolId == null || schoolId.trim().isEmpty()) {
            throw new IllegalArgumentException("School ID cannot be null or empty");
        }
        return this.criteriaRepository.findAllBySchoolId(schoolId);
    }

    public Optional<Criteria> getCriteriaById(String schoolId, String id) {
        if (schoolId == null || schoolId.trim().isEmpty()) {
            throw new IllegalArgumentException("School ID cannot be null or empty");
        }
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Criteria ID cannot be null or empty");
        }
        return this.criteriaRepository.findByIdAndSchoolId(id, schoolId);
    }

    public List<Criteria.Subject> getAllSubjects(String schoolId, String className, String examType) {
        return this.criteriaRepository.findBySchoolIdAndClassNameAndExamType(schoolId, className, examType).map(Criteria::getSubjects).orElseThrow(() -> new IllegalArgumentException("Subject not found for this school."));
    }

    public Optional<Criteria> updateCriteria(String schoolId, String id, Criteria updateCriteria) {
        if (schoolId == null || schoolId.trim().isEmpty()) {
            throw new IllegalArgumentException("School ID cannot be null or empty");
        }
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Criteria ID cannot be null or empty");
        }
        if (updateCriteria == null) {
            throw new IllegalArgumentException("Update criteria cannot be null");
        }
        Optional existingCriteria = this.criteriaRepository.findByIdAndSchoolId(id, schoolId);
        if (existingCriteria.isPresent()) {
            Criteria updatedCriteria = (Criteria)existingCriteria.get();
            if (updateCriteria.getClassName() != null && !updateCriteria.getClassName().equals(updatedCriteria.getClassName()) || updateCriteria.getSection() != null && !updateCriteria.getSection().equals(updatedCriteria.getSection()) || updateCriteria.getExamType() != null && !updateCriteria.getExamType().equals(updatedCriteria.getExamType())) {
                String classNameToCheck = updateCriteria.getClassName() != null ? updateCriteria.getClassName() : updatedCriteria.getClassName();
                String sectionToCheck = updateCriteria.getSection() != null ? updateCriteria.getSection() : updatedCriteria.getSection();
                String examTypeToCheck = updateCriteria.getExamType() != null ? updateCriteria.getExamType() : updatedCriteria.getExamType();
                boolean isDuplicate = this.criteriaRepository.findAllBySchoolId(schoolId).stream().filter(c -> !c.getId().equals(id)).anyMatch(existing -> existing.getClassName().equalsIgnoreCase(classNameToCheck) && existing.getSection().equalsIgnoreCase(sectionToCheck) && existing.getExamType().equalsIgnoreCase(examTypeToCheck));
                if (isDuplicate) {
                    throw new IllegalArgumentException("Criteria for this class, section and exam type already exists for this school");
                }
            }
            updateCriteria.setSchoolId(schoolId);
            if (updateCriteria.getClassName() != null && !updateCriteria.getClassName().isEmpty()) {
                updatedCriteria.setClassName(updateCriteria.getClassName());
            }
            if (updateCriteria.getPassingMark() != null) {
                updatedCriteria.setPassingMark(updateCriteria.getPassingMark());
            }
            if (updateCriteria.getSection() != null && !updateCriteria.getSection().isEmpty()) {
                updatedCriteria.setSection(updateCriteria.getSection());
            }
            if (updateCriteria.getExamType() != null && !updateCriteria.getExamType().isEmpty()) {
                updatedCriteria.setExamType(updateCriteria.getExamType());
            }
            if (updateCriteria.getSubjects() != null && !updateCriteria.getSubjects().isEmpty()) {
                ArrayList<Criteria.Subject> existingSubjects = updatedCriteria.getSubjects() != null ? new ArrayList<Criteria.Subject>(updatedCriteria.getSubjects()) : new ArrayList();
                Map<String, Criteria.Subject> subjectMap = existingSubjects.stream().collect(Collectors.toMap(Criteria.Subject::getSubjectName, s -> s));
                for (Criteria.Subject newSub : updateCriteria.getSubjects()) {
                    if (subjectMap.containsKey(newSub.getSubjectName())) {
                        Criteria.Subject existing2 = subjectMap.get(newSub.getSubjectName());
                        existing2.setSubjectName(newSub.getSubjectName());
                        existing2.setSubjectMark(newSub.getSubjectMark());
                        existing2.setProjectMark(newSub.getProjectMark());
                        existing2.setFullMark(newSub.getFullMark());
                        continue;
                    }
                    existingSubjects.add(newSub);
                }
                updatedCriteria.setSubjects(existingSubjects);
            }
            this.criteriaRepository.save((Object)updatedCriteria);
            return Optional.of(updatedCriteria);
        }
        return existingCriteria;
    }

    public Optional<Criteria> deleteCriteria(String schoolId, String id) {
        if (schoolId == null || schoolId.trim().isEmpty()) {
            throw new IllegalArgumentException("School ID cannot be null or empty");
        }
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Criteria ID cannot be null or empty");
        }
        Optional existingCriteria = this.criteriaRepository.findByIdAndSchoolId(id, schoolId);
        if (existingCriteria.isPresent()) {
            this.criteriaRepository.deleteById((Object)id);
        }
        return existingCriteria;
    }

    private void validateCriteria(Criteria criteria) {
        if (criteria.getClassName() == null || criteria.getClassName().trim().isEmpty()) {
            throw new IllegalArgumentException("Class name cannot be null or empty");
        }
        if (criteria.getExamType() == null || criteria.getExamType().trim().isEmpty()) {
            throw new IllegalArgumentException("Exam type cannot be null or empty");
        }
        if (criteria.getSubjects() == null || criteria.getSubjects().isEmpty()) {
            throw new IllegalArgumentException("Subjects list cannot be null or empty");
        }
        if (criteria.getPassingMark() < 0.0) {
            throw new IllegalArgumentException("Passing mark cannot be negative");
        }
        for (Criteria.Subject subject : criteria.getSubjects()) {
            if (subject.getSubjectName() == null || subject.getSubjectName().trim().isEmpty()) {
                throw new IllegalArgumentException("Subject name cannot be null or empty");
            }
            if (subject.getSubjectMark() == null) {
                throw new IllegalArgumentException("Subject mark cannot be null");
            }
            if (subject.getProjectMark() == null) {
                throw new IllegalArgumentException("Project mark cannot be null");
            }
            if (subject.getSubjectMark() < 0.0) {
                throw new IllegalArgumentException("Subject mark cannot be negative");
            }
            if (subject.getProjectMark() < 0.0) {
                throw new IllegalArgumentException("Project mark cannot be negative");
            }
            if (!(criteria.getPassingMark() > subject.getSubjectMark() + subject.getProjectMark())) continue;
            throw new IllegalArgumentException("Passing mark cannot be greater than the sum of subject and project marks");
        }
    }
}

