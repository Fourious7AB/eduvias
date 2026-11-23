/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.RoutineMain.model.ExamTypePage
 *  com.neel.RoutineMain.repo.ExamTypePageRepo
 *  com.neel.RoutineMain.service.ExamTypePageService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 */
package com.neel.RoutineMain.service;

import com.neel.RoutineMain.model.ExamTypePage;
import com.neel.RoutineMain.repo.ExamTypePageRepo;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExamTypePageService {
    @Autowired
    private ExamTypePageRepo examTypePageRepo;

    public List<ExamTypePage> getAllExamTypes() {
        return this.examTypePageRepo.findAll();
    }

    public ExamTypePage getExamTypeById(String id) {
        return this.examTypePageRepo.findById((Object)id).orElse(null);
    }

    public ExamTypePage createExamType(ExamTypePage examTypePage) {
        examTypePage.setSchoolId("67ca9167d56ec91fc9539234");
        return (ExamTypePage)this.examTypePageRepo.save((Object)examTypePage);
    }

    public ExamTypePage updateExamType(String id, ExamTypePage updatedExamType) {
        return this.examTypePageRepo.findById((Object)id).map(existing -> {
            existing.setExamTypeName(updatedExamType.getExamTypeName());
            existing.setExamMarks(updatedExamType.getExamMarks());
            updatedExamType.setSchoolId(existing.getSchoolId());
            return (ExamTypePage)this.examTypePageRepo.save(existing);
        }).orElse(null);
    }

    public void deleteExamType(String id) {
        this.examTypePageRepo.deleteById((Object)id);
    }

    public List<ExamTypePage> getAllExamTypesBySchool(String schoolId) {
        return this.examTypePageRepo.findBySchoolId(schoolId);
    }

    public ExamTypePage getExamTypeByIdAndSchool(String id, String schoolId) {
        return this.examTypePageRepo.findByIdAndSchoolId(id, schoolId).orElse(null);
    }

    public ExamTypePage createExamTypeWithSchool(ExamTypePage examTypePage, String schoolId) {
        examTypePage.setSchoolId(schoolId);
        return (ExamTypePage)this.examTypePageRepo.save((Object)examTypePage);
    }

    public ExamTypePage updateExamTypeWithSchool(String id, ExamTypePage updatedExamType, String schoolId) {
        return this.examTypePageRepo.findByIdAndSchoolId(id, schoolId).map(existing -> {
            existing.setExamTypeName(updatedExamType.getExamTypeName());
            existing.setExamMarks(updatedExamType.getExamMarks());
            existing.setSchoolId(schoolId);
            return (ExamTypePage)this.examTypePageRepo.save(existing);
        }).orElse(null);
    }

    public boolean deleteExamTypeWithSchool(String id, String schoolId) {
        Optional examType = this.examTypePageRepo.findByIdAndSchoolId(id, schoolId);
        if (examType.isPresent()) {
            this.examTypePageRepo.delete((Object)((ExamTypePage)examType.get()));
            return true;
        }
        return false;
    }
}

