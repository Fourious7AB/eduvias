package com.result.Result.service;

import com.result.Result.model.Criteria;
import com.result.Result.repo.CriteriaRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CriteriaService {
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
        this.normalizeCriteria(criteria);
        this.validateCriteria(criteria);

        List<Criteria> existingCriteria = this.criteriaRepository.findAllBySchoolId(schoolId);
        boolean isDuplicate = existingCriteria.stream().anyMatch(existing ->
                existing.getClassName() != null
                        && existing.getExamType() != null
                        && existing.getClassName().trim().equalsIgnoreCase(criteria.getClassName())
                        && existing.getExamType().trim().equalsIgnoreCase(criteria.getExamType())
                        && this.sectionsConflict(existing.getSection(), criteria.getSection()));
        if (isDuplicate) {
            throw new IllegalArgumentException("Criteria for this class and exam type already exists for this school");
        }

        criteria.setSchoolId(schoolId);
        LocalDate today = LocalDate.now();
        for (Criteria.Subject sub : criteria.getSubjects()) {
            if (sub.getCreateAt() == null) {
                sub.setCreateAt(today);
            }
        }
        return this.criteriaRepository.save(criteria);
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

    public Optional<Criteria> getCriteriaForClassAndExam(String schoolId, String className, String examType) {
        if (schoolId == null || schoolId.trim().isEmpty()
                || className == null || className.trim().isEmpty()
                || examType == null || examType.trim().isEmpty()) {
            return Optional.empty();
        }
        String trimmedClass = className.trim();
        String trimmedExam = examType.trim();

        List<Criteria> matched = this.criteriaRepository
                .findBySchoolIdAndClassNameIgnoreCaseAndExamTypeIgnoreCase(schoolId.trim(), trimmedClass, trimmedExam);
        if (matched != null && !matched.isEmpty()) {
            return Optional.of(matched.get(0));
        }

        // Fallback scan to match legacy documents that may have leading/trailing whitespace
        List<Criteria> allForSchool = this.criteriaRepository.findAllBySchoolId(schoolId.trim());
        if (allForSchool != null) {
            return allForSchool.stream()
                    .filter(c -> c.getClassName() != null && c.getExamType() != null
                            && c.getClassName().trim().equalsIgnoreCase(trimmedClass)
                            && c.getExamType().trim().equalsIgnoreCase(trimmedExam))
                    .findFirst();
        }
        return Optional.empty();
    }

    public List<Criteria.Subject> getAllSubjects(String schoolId, String className, String examType) {
        if (schoolId == null || schoolId.trim().isEmpty()) {
            throw new IllegalArgumentException("School ID cannot be null or empty");
        }
        if (className == null || className.trim().isEmpty()) {
            throw new IllegalArgumentException("Class name cannot be null or empty");
        }
        if (examType == null || examType.trim().isEmpty()) {
            throw new IllegalArgumentException("Exam type cannot be null or empty");
        }
        return this.getCriteriaForClassAndExam(schoolId, className, examType)
                .map(c -> c.getSubjects() != null ? c.getSubjects() : new ArrayList<Criteria.Subject>())
                .orElseThrow(() -> new IllegalArgumentException("Subject not found for this school."));
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

        Optional<Criteria> existingCriteriaOpt = this.criteriaRepository.findByIdAndSchoolId(id, schoolId);
        if (existingCriteriaOpt.isEmpty()) {
            return Optional.empty();
        }

        Criteria updatedCriteria = existingCriteriaOpt.get();
        String classNameToCheck = (updateCriteria.getClassName() != null && !updateCriteria.getClassName().trim().isEmpty())
                ? updateCriteria.getClassName().trim()
                : (updatedCriteria.getClassName() != null ? updatedCriteria.getClassName().trim() : "");
        String sectionToCheck = (updateCriteria.getSection() != null && !updateCriteria.getSection().trim().isEmpty())
                ? updateCriteria.getSection().trim()
                : updatedCriteria.getSection();
        String examTypeToCheck = (updateCriteria.getExamType() != null && !updateCriteria.getExamType().trim().isEmpty())
                ? updateCriteria.getExamType().trim()
                : (updatedCriteria.getExamType() != null ? updatedCriteria.getExamType().trim() : "");

        boolean keyChanged = !equalsIgnoreCaseSafe(classNameToCheck, updatedCriteria.getClassName())
                || !equalsIgnoreCaseSafe(sectionToCheck, updatedCriteria.getSection())
                || !equalsIgnoreCaseSafe(examTypeToCheck, updatedCriteria.getExamType());

        if (keyChanged) {
            boolean isDuplicate = this.criteriaRepository.findAllBySchoolId(schoolId).stream()
                    .filter(c -> c.getId() != null && !c.getId().equals(id))
                    .anyMatch(existing -> existing.getClassName() != null
                            && existing.getExamType() != null
                            && existing.getClassName().trim().equalsIgnoreCase(classNameToCheck)
                            && existing.getExamType().trim().equalsIgnoreCase(examTypeToCheck)
                            && this.sectionsConflict(existing.getSection(), sectionToCheck));
            if (isDuplicate) {
                throw new IllegalArgumentException("Criteria for this class and exam type already exists for this school");
            }
        }

        updatedCriteria.setSchoolId(schoolId);
        updatedCriteria.setClassName(classNameToCheck);
        updatedCriteria.setExamType(examTypeToCheck);
        if (updateCriteria.getSection() != null) {
            updatedCriteria.setSection(updateCriteria.getSection().trim().isEmpty() ? null : updateCriteria.getSection().trim());
        }
        if (updateCriteria.getPassingMark() != null) {
            updatedCriteria.setPassingMark(updateCriteria.getPassingMark());
        }

        if (updateCriteria.getSubjects() != null) {
            // Preserve createAt dates from existing subjects matched case-insensitively,
            // while replacing the full subject list so removed subjects stay removed.
            Map<String, LocalDate> existingDates = new HashMap<>();
            if (updatedCriteria.getSubjects() != null) {
                for (Criteria.Subject existingSub : updatedCriteria.getSubjects()) {
                    if (existingSub.getSubjectName() != null && existingSub.getCreateAt() != null) {
                        existingDates.putIfAbsent(existingSub.getSubjectName().trim().toLowerCase(), existingSub.getCreateAt());
                    }
                }
            }
            LocalDate today = LocalDate.now();
            List<Criteria.Subject> nextSubjects = new ArrayList<>();
            for (Criteria.Subject incoming : updateCriteria.getSubjects()) {
                Criteria.Subject normalized = new Criteria.Subject();
                String cleanName = incoming.getSubjectName() != null ? incoming.getSubjectName().trim() : null;
                normalized.setSubjectName(cleanName);
                normalized.setSubjectMark(incoming.getSubjectMark());
                normalized.setProjectMark(incoming.getProjectMark());
                if (incoming.getSubjectMark() != null && incoming.getProjectMark() != null) {
                    normalized.setFullMark(incoming.getSubjectMark() + incoming.getProjectMark());
                } else {
                    normalized.setFullMark(incoming.getFullMark());
                }
                LocalDate preservedDate = cleanName != null ? existingDates.get(cleanName.toLowerCase()) : null;
                normalized.setCreateAt(incoming.getCreateAt() != null ? incoming.getCreateAt() : (preservedDate != null ? preservedDate : today));
                nextSubjects.add(normalized);
            }
            updatedCriteria.setSubjects(nextSubjects);
        }

        this.validateCriteria(updatedCriteria);
        this.criteriaRepository.save(updatedCriteria);
        return Optional.of(updatedCriteria);
    }

    public Optional<Criteria> deleteCriteria(String schoolId, String id) {
        if (schoolId == null || schoolId.trim().isEmpty()) {
            throw new IllegalArgumentException("School ID cannot be null or empty");
        }
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Criteria ID cannot be null or empty");
        }
        Optional<Criteria> existingCriteria = this.criteriaRepository.findByIdAndSchoolId(id, schoolId);
        if (existingCriteria.isPresent()) {
            this.criteriaRepository.deleteById(id);
        }
        return existingCriteria;
    }

    public Optional<Criteria> deleteSubjectFromCriteria(String schoolId, String id, String subjectName) {
        if (schoolId == null || schoolId.trim().isEmpty()) {
            throw new IllegalArgumentException("School ID cannot be null or empty");
        }
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Criteria ID cannot be null or empty");
        }
        if (subjectName == null || subjectName.trim().isEmpty()) {
            throw new IllegalArgumentException("Subject name cannot be null or empty");
        }

        Optional<Criteria> existingOpt = this.criteriaRepository.findByIdAndSchoolId(id, schoolId);
        if (existingOpt.isEmpty()) {
            return Optional.empty();
        }

        Criteria criteria = existingOpt.get();
        List<Criteria.Subject> currentSubjects = criteria.getSubjects() != null
                ? new ArrayList<>(criteria.getSubjects())
                : new ArrayList<>();
        String target = subjectName.trim();
        boolean removed = currentSubjects.removeIf(s ->
                s.getSubjectName() != null && s.getSubjectName().trim().equalsIgnoreCase(target));
        if (!removed) {
            throw new IllegalArgumentException("Subject '" + target + "' not found in criteria");
        }

        if (currentSubjects.isEmpty()) {
            this.criteriaRepository.deleteById(id);
            criteria.setSubjects(currentSubjects);
            return Optional.of(criteria);
        }

        criteria.setSubjects(currentSubjects);
        this.criteriaRepository.save(criteria);
        return Optional.of(criteria);
    }

    private boolean sectionsConflict(String sectionA, String sectionB) {
        boolean aEmpty = sectionA == null || sectionA.trim().isEmpty();
        boolean bEmpty = sectionB == null || sectionB.trim().isEmpty();
        if (aEmpty || bEmpty) {
            return true;
        }
        return sectionA.trim().equalsIgnoreCase(sectionB.trim());
    }

    private boolean equalsIgnoreCaseSafe(String a, String b) {
        String normA = a == null ? "" : a.trim();
        String normB = b == null ? "" : b.trim();
        return normA.equalsIgnoreCase(normB);
    }

    private void normalizeCriteria(Criteria criteria) {
        if (criteria.getClassName() != null) {
            criteria.setClassName(criteria.getClassName().trim());
        }
        if (criteria.getExamType() != null) {
            criteria.setExamType(criteria.getExamType().trim());
        }
        if (criteria.getSection() != null) {
            String trimmedSection = criteria.getSection().trim();
            criteria.setSection(trimmedSection.isEmpty() ? null : trimmedSection);
        }
        if (criteria.getSubjects() != null) {
            for (Criteria.Subject subject : criteria.getSubjects()) {
                if (subject.getSubjectName() != null) {
                    subject.setSubjectName(subject.getSubjectName().trim());
                }
                if (subject.getSubjectMark() != null && subject.getProjectMark() != null) {
                    subject.setFullMark(subject.getSubjectMark() + subject.getProjectMark());
                }
            }
        }
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
        if (criteria.getPassingMark() == null) {
            throw new IllegalArgumentException("Passing mark cannot be null");
        }
        if (criteria.getPassingMark() < 0.0) {
            throw new IllegalArgumentException("Passing mark cannot be negative");
        }

        Set<String> seenSubjects = new HashSet<>();
        for (Criteria.Subject subject : criteria.getSubjects()) {
            if (subject.getSubjectName() == null || subject.getSubjectName().trim().isEmpty()) {
                throw new IllegalArgumentException("Subject name cannot be null or empty");
            }
            String key = subject.getSubjectName().trim().toLowerCase();
            if (!seenSubjects.add(key)) {
                throw new IllegalArgumentException("Duplicate subject in criteria: " + subject.getSubjectName().trim());
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
            if (criteria.getPassingMark() > subject.getSubjectMark() + subject.getProjectMark()) {
                throw new IllegalArgumentException("Passing mark cannot be greater than the sum of subject and project marks for " + subject.getSubjectName().trim());
            }
        }
    }
}
