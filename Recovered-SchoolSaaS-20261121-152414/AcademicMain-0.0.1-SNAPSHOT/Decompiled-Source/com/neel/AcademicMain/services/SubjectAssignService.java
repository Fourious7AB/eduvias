/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.AcademicMain.models.ClassEntity
 *  com.neel.AcademicMain.models.Subject
 *  com.neel.AcademicMain.models.Subject_Assign
 *  com.neel.AcademicMain.repo.ClassRepository
 *  com.neel.AcademicMain.repo.SubjectAssignRepository
 *  com.neel.AcademicMain.repo.SubjectRepository
 *  com.neel.AcademicMain.services.SubjectAssignService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 */
package com.neel.AcademicMain.services;

import com.neel.AcademicMain.models.ClassEntity;
import com.neel.AcademicMain.models.Subject;
import com.neel.AcademicMain.models.Subject_Assign;
import com.neel.AcademicMain.repo.ClassRepository;
import com.neel.AcademicMain.repo.SubjectAssignRepository;
import com.neel.AcademicMain.repo.SubjectRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubjectAssignService {
    @Autowired
    private SubjectAssignRepository subjectAssignRepository;
    @Autowired
    private ClassRepository classRepository;
    @Autowired
    private SubjectRepository subjectRepository;

    public List<Subject_Assign> getAllSubjectAssign() {
        return this.subjectAssignRepository.findAll();
    }

    public List<Subject_Assign> getSubjectAssignBySchoolId(String schoolId) {
        return this.subjectAssignRepository.findBySchoolId(schoolId);
    }

    public Subject_Assign addSubjectAssign(Subject_Assign subject_assign) {
        Subject_Assign lastSubjectAssign = this.subjectAssignRepository.findTopByOrderBySubjectAssignIdDesc();
        int nextSubjectAssignId = lastSubjectAssign != null ? lastSubjectAssign.getSubjectAssignId() + 1 : 1;
        subject_assign.setSubjectAssignId(nextSubjectAssignId);
        return (Subject_Assign)this.subjectAssignRepository.save((Object)subject_assign);
    }

    public Subject_Assign updateSubjectAssign(String schoolId, String id, Subject_Assign updatedSubjectAssign) {
        Subject_Assign existingSubjectAssign = (Subject_Assign)this.subjectAssignRepository.findById((Object)id).orElseThrow(() -> new IllegalArgumentException("Subject assignment not found"));
        if (!existingSubjectAssign.getSchoolId().equals(schoolId)) {
            throw new IllegalArgumentException("Subject assignment does not belong to the specified school");
        }
        ClassEntity classObj = (ClassEntity)this.classRepository.findByClassNameAndSchoolId(updatedSubjectAssign.getClassId(), schoolId).orElseThrow(() -> new IllegalArgumentException("Class not found for this school"));
        Subject subjectObj = (Subject)this.subjectRepository.findBySubjectNameAndSchoolId(updatedSubjectAssign.getSubjectId(), schoolId).orElseThrow(() -> new IllegalArgumentException("Subject not found for this school"));
        existingSubjectAssign.setClassId(updatedSubjectAssign.getClassId());
        existingSubjectAssign.setSubjectId(updatedSubjectAssign.getSubjectId());
        String subjectCode = classObj.getClass_Code() + "-" + subjectObj.getSubject_index();
        existingSubjectAssign.setSubjectCode(subjectCode);
        existingSubjectAssign.setSchoolId(schoolId);
        return (Subject_Assign)this.subjectAssignRepository.save((Object)existingSubjectAssign);
    }

    public void deleteSubjectAssign(String schoolId, String id) {
        Subject_Assign subjectAssign = (Subject_Assign)this.subjectAssignRepository.findById((Object)id).orElseThrow(() -> new IllegalArgumentException("Subject assignment not found"));
        if (!subjectAssign.getSchoolId().equals(schoolId)) {
            throw new IllegalArgumentException("Subject assignment does not belong to the specified school");
        }
        this.subjectAssignRepository.deleteById((Object)id);
    }

    public List<Subject_Assign> getSubjectByClassNameAndSchool(String classId, String schoolId) {
        return this.subjectAssignRepository.findByClassIdAndSchoolId(classId, schoolId);
    }

    public List<Subject_Assign> getSubjectByClassName(String classId) {
        return this.subjectAssignRepository.findByClassId(classId);
    }

    public boolean checkIfAssignmentExists(String className, String subjectName, String schoolId) {
        List existingAssignments = this.subjectAssignRepository.findBySchoolId(schoolId);
        return existingAssignments.stream().anyMatch(assignment -> assignment.getClassId().equalsIgnoreCase(className) && assignment.getSubjectId().equalsIgnoreCase(subjectName));
    }
}

