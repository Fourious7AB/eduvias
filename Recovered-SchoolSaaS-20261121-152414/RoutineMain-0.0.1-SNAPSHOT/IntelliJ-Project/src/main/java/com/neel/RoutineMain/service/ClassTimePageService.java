/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.RoutineMain.model.ClassTimePage
 *  com.neel.RoutineMain.repo.ClassTimePageRepository
 *  com.neel.RoutineMain.service.ClassTimePageService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 */
package com.neel.RoutineMain.service;

import com.neel.RoutineMain.model.ClassTimePage;
import com.neel.RoutineMain.repo.ClassTimePageRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClassTimePageService {
    @Autowired
    private ClassTimePageRepository repository;

    public List<ClassTimePage> getAllClassTimes() {
        return this.repository.findAll();
    }

    public ClassTimePage getClassTimeById(String id) {
        return this.repository.findById(id).orElse(null);
    }

    public ClassTimePage createClassTime(ClassTimePage classTime) {
        return (ClassTimePage)this.repository.save(classTime);
    }

    public ClassTimePage updateClassTime(String id, ClassTimePage updatedClassTime) {
        updatedClassTime.setSchoolId("67ca9167d56ec91fc9539234");
        return this.repository.findById(id).map(existing -> {
            existing.setPeriods(updatedClassTime.getPeriods());
            existing.setStartTime(updatedClassTime.getStartTime());
            existing.setEndTime(updatedClassTime.getEndTime());
            existing.setSchoolId(updatedClassTime.getSchoolId());
            return (ClassTimePage)this.repository.save(existing);
        }).orElse(null);
    }

    public void deleteClassTime(String id) {
        this.repository.deleteById(id);
    }

    public List<ClassTimePage> getAllClassTimesBySchool(String schoolId) {
        return this.repository.findBySchoolId(schoolId);
    }

    public ClassTimePage getClassTimeByIdAndSchool(String id, String schoolId) {
        return this.repository.findByIdAndSchoolId(id, schoolId).orElse(null);
    }

    public ClassTimePage createClassTimeWithSchool(ClassTimePage classTime, String schoolId) {
        classTime.setSchoolId(schoolId);
        return (ClassTimePage)this.repository.save(classTime);
    }

    public Optional<ClassTimePage> getAllClassTimesBySchoolAndPeriod(String schoolId, String periods) {
        return this.repository.findBySchoolIdAndPeriods(schoolId, periods);
    }

    public ClassTimePage updateClassTimeWithSchool(String id, ClassTimePage updatedClassTime, String schoolId) {
        updatedClassTime.setSchoolId(schoolId);
        return this.repository.findByIdAndSchoolId(id, schoolId).map(existing -> {
            existing.setPeriods(updatedClassTime.getPeriods());
            existing.setStartTime(updatedClassTime.getStartTime());
            existing.setEndTime(updatedClassTime.getEndTime());
            existing.setSchoolId(schoolId);
            return (ClassTimePage)this.repository.save(existing);
        }).orElse(null);
    }

    public boolean deleteClassTimeWithSchool(String id, String schoolId) {
        if (this.repository.findByIdAndSchoolId(id, schoolId).isPresent()) {
            this.repository.deleteById(id);
            return true;
        }
        return false;
    }
}

