/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.AcademicMain.exception.ClassNameAlreadyExist
 *  com.neel.AcademicMain.exception.ClassNotFound
 *  com.neel.AcademicMain.models.Subject
 *  com.neel.AcademicMain.repo.SubjectRepository
 *  com.neel.AcademicMain.services.SubjectService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 */
package com.neel.AcademicMain.services;

import com.neel.AcademicMain.exception.ClassNameAlreadyExist;
import com.neel.AcademicMain.exception.ClassNotFound;
import com.neel.AcademicMain.models.Subject;
import com.neel.AcademicMain.repo.SubjectRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubjectService {
    @Autowired
    private SubjectRepository subjectRepository;

    public List<Subject> getAllSubjects() {
        return this.subjectRepository.findAll();
    }

    public List<Subject> getSubjectsBySchoolId(String schoolId) {
        return this.subjectRepository.findBySchoolId(schoolId);
    }

    public List<Subject> getUniversalSubjects() {
        return this.subjectRepository.findByUniversalIsTrue();
    }

    public List<Subject> getSubjectsForSchool(String schoolId) {
        return this.subjectRepository.findBySchoolIdOrUniversalIsTrue(schoolId, true);
    }

    public Subject addSubject(Subject subject) {
        if (!this.checkIfSubjectExists(subject.getSubjectName(), subject.getSchoolId())) {
            Subject lastSubject = this.subjectRepository.findTopByOrderBySubjectIdDesc();
            int nextSubjectId = lastSubject != null ? lastSubject.getSubjectId() + 1 : 1;
            subject.setSubjectId(nextSubjectId);
            return (Subject)this.subjectRepository.save((Object)subject);
        }
        if (subject.isUniversal()) {
            throw new ClassNameAlreadyExist("Universal subject with this name already exists!");
        }
        throw new ClassNameAlreadyExist("Subject name already exists for this school!");
    }

    public Subject updateSubject(String schoolId, String id, Subject updatedSubject) {
        Subject existingSubject = (Subject)this.subjectRepository.findById((Object)id).orElseThrow(() -> new ClassNotFound("Subject not found"));
        if (existingSubject.isUniversal()) {
            updatedSubject.setUniversal(true);
        } else {
            if (!existingSubject.getSchoolId().equals(schoolId)) {
                throw new IllegalArgumentException("Subject does not belong to the specified school");
            }
            updatedSubject.setSchoolId(schoolId);
        }
        if (!this.isSubjectNameUniqueForUpdate(updatedSubject.getSubjectName(), schoolId, id)) {
            throw new IllegalArgumentException("Subject name already exists!");
        }
        existingSubject.setSubjectName(updatedSubject.getSubjectName());
        existingSubject.setSubject_index(updatedSubject.getSubject_index());
        existingSubject.setUniversal(updatedSubject.isUniversal());
        return (Subject)this.subjectRepository.save((Object)existingSubject);
    }

    public void deleteSubject(String schoolId, String id) {
        Subject subject = (Subject)this.subjectRepository.findById((Object)id).orElseThrow(() -> new RuntimeException("Subject not found"));
        if (!subject.isUniversal() && !subject.getSchoolId().equals(schoolId)) {
            throw new IllegalArgumentException("Subject does not belong to the specified school");
        }
        this.subjectRepository.deleteById((Object)id);
    }

    public Subject addUniversalSubject(Subject subject) {
        subject.setUniversal(true);
        subject.setSchoolId(null);
        if (!this.checkIfSubjectExists(subject.getSubjectName(), null)) {
            Subject lastSubject = this.subjectRepository.findTopByOrderBySubjectIdDesc();
            int nextSubjectId = lastSubject != null ? lastSubject.getSubjectId() + 1 : 1;
            subject.setSubjectId(nextSubjectId);
            return (Subject)this.subjectRepository.save((Object)subject);
        }
        throw new IllegalArgumentException("Subject with this name already exists!");
    }

    private boolean checkIfSubjectExists(String subjectName, String schoolId) {
        List universalSubjects = this.subjectRepository.findByUniversalIsTrue();
        boolean universalExists = universalSubjects.stream().anyMatch(s -> s.getSubjectName().equalsIgnoreCase(subjectName));
        if (universalExists) {
            return true;
        }
        if (schoolId != null) {
            List schoolSubjects = this.subjectRepository.findBySchoolId(schoolId);
            return schoolSubjects.stream().anyMatch(s -> s.getSubjectName().equalsIgnoreCase(subjectName));
        }
        return false;
    }

    private boolean isSubjectNameUniqueForUpdate(String subjectName, String schoolId, String currentSubjectId) {
        List universalSubjects = this.subjectRepository.findByUniversalIsTrue();
        boolean duplicateUniversal = universalSubjects.stream().anyMatch(s -> s.getSubjectName().equalsIgnoreCase(subjectName) && !s.getId().equals(currentSubjectId));
        if (duplicateUniversal) {
            return false;
        }
        if (schoolId != null) {
            List schoolSubjects = this.subjectRepository.findBySchoolId(schoolId);
            return schoolSubjects.stream().noneMatch(s -> s.getSubjectName().equalsIgnoreCase(subjectName) && !s.getId().equals(currentSubjectId));
        }
        return true;
    }
}

