/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.AcademicMain.exception.AccessDenied
 *  com.neel.AcademicMain.exception.ClassNameAlreadyExist
 *  com.neel.AcademicMain.exception.ClassNotFound
 *  com.neel.AcademicMain.models.ClassEntity
 *  com.neel.AcademicMain.repo.ClassRepository
 *  com.neel.AcademicMain.repo.SectionRepository
 *  com.neel.AcademicMain.services.ClassService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 */
package com.neel.AcademicMain.services;

import com.neel.AcademicMain.exception.AccessDenied;
import com.neel.AcademicMain.exception.ClassNameAlreadyExist;
import com.neel.AcademicMain.exception.ClassNotFound;
import com.neel.AcademicMain.models.ClassEntity;
import com.neel.AcademicMain.repo.ClassRepository;
import com.neel.AcademicMain.repo.SectionRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClassService {
    @Autowired
    private ClassRepository classRepository;
    @Autowired
    private SectionRepository sectionRepository;

    public List<ClassEntity> getAllClasses() {
        return this.classRepository.findAll();
    }

    public ClassEntity addClass(ClassEntity classEntity) {
        ClassEntity lastClasses;
        if (!classEntity.isUniversal()) {
            Optional existingClass = this.classRepository.findByClassNameAndSchoolId(classEntity.getClassName(), classEntity.getSchoolId());
            if (existingClass.isPresent()) {
                throw new ClassNameAlreadyExist("Class name already exists for this school!");
            }
        } else {
            Optional<ClassEntity> existingUniversalClass = this.classRepository.findAll().stream().filter(ClassEntity::isUniversal).filter(c -> c.getClassName().equals(classEntity.getClassName())).findFirst();
            if (existingUniversalClass.isPresent()) {
                throw new ClassNameAlreadyExist("Universal class with this name already exists!");
            }
        }
        int nextClassId = (lastClasses = this.classRepository.findTopByOrderByClassIdDesc()) != null ? lastClasses.getClassId() + 1 : 1;
        classEntity.setClassId(nextClassId);
        return (ClassEntity)this.classRepository.save((Object)classEntity);
    }

    public List<ClassEntity> getAllUniversalClasses() {
        return this.classRepository.findByUniversalTrue();
    }

    public List<ClassEntity> getAllClassesForSchool(String schoolId) {
        return this.classRepository.findByUniversalTrueOrSchoolId(schoolId);
    }

    public ClassEntity updateClasses(String schoolId, String id, ClassEntity updatedClasses) {
        if (schoolId == null || schoolId.trim().isEmpty()) {
            throw new AccessDenied("You don't have access to update this class");
        }
        ClassEntity existingClasses = (ClassEntity)this.classRepository.findById((Object)id).orElseThrow(() -> new ClassNotFound("Class not found"));
        Optional duplicateClass = this.classRepository.findByClassNameAndSchoolId(updatedClasses.getClassName(), schoolId);
        if (duplicateClass.isPresent() && !((ClassEntity)duplicateClass.get()).getId().equals(id)) {
            throw new ClassNameAlreadyExist("Class name already exists for this school!");
        }
        if (existingClasses.getSchoolId() != null && !existingClasses.getSchoolId().isEmpty()) {
            existingClasses.setSchoolId(schoolId);
        }
        if (existingClasses.getClassName() != null && !existingClasses.getClassName().isEmpty()) {
            existingClasses.setClassName(updatedClasses.getClassName());
        }
        if (existingClasses.getClass_Code() != null && !existingClasses.getClass_Code().isEmpty()) {
            existingClasses.setClass_Code(updatedClasses.getClass_Code());
        }
        return (ClassEntity)this.classRepository.save((Object)existingClasses);
    }

    public ClassEntity updateClassesBySupAdmin(String id, ClassEntity updatedClasses) {
        if (updatedClasses.getSchoolId() != null) {
            throw new AccessDenied("You don't have access to update this class");
        }
        ClassEntity existingClasses = (ClassEntity)this.classRepository.findById((Object)id).orElseThrow(() -> new ClassNotFound("Class not found"));
        Optional duplicateClass = this.classRepository.findByClassNameAndSchoolId(updatedClasses.getClassName(), updatedClasses.getSchoolId());
        if (duplicateClass.isPresent() && !((ClassEntity)duplicateClass.get()).getId().equals(id)) {
            throw new ClassNameAlreadyExist("Class name already exists for this school!");
        }
        if (existingClasses.getClassName() != null && !existingClasses.getClassName().isEmpty()) {
            existingClasses.setClassName(updatedClasses.getClassName());
        }
        if (existingClasses.getClass_Code() != null && !existingClasses.getClass_Code().isEmpty()) {
            existingClasses.setClass_Code(updatedClasses.getClass_Code());
        }
        return (ClassEntity)this.classRepository.save((Object)existingClasses);
    }

    public List<ClassEntity> getClassesBySchoolId(String schoolId) {
        return this.classRepository.findBySchoolId(schoolId);
    }

    public void deleteClasses(String schoolId, String id) {
        if (schoolId == null || schoolId.trim().isEmpty()) {
            throw new AccessDenied("You don't have access to delete this class");
        }
        ClassEntity classEntity = (ClassEntity)this.classRepository.findById((Object)id).orElseThrow(() -> new RuntimeException("Class not found"));
        if (!classEntity.getSchoolId().equals(schoolId)) {
            throw new ClassNameAlreadyExist("Class does not belong to the specified school");
        }
        this.classRepository.deleteById((Object)id);
    }

    public void deleteBySupAdminClasses(String id) {
        ClassEntity classEntity = (ClassEntity)this.classRepository.findById((Object)id).orElseThrow(() -> new RuntimeException("Class not found"));
        if (classEntity.getSchoolId() != null) {
            throw new AccessDenied("You don't have access to delete this class");
        }
        this.classRepository.deleteById((Object)id);
    }

    public boolean checkIfClassExists(String className, String schoolId) {
        List universalClasses = this.classRepository.findByUniversal(true);
        boolean universalExists = universalClasses.stream().anyMatch(c -> c.getClassName().equalsIgnoreCase(className));
        if (universalExists) {
            return true;
        }
        List schoolClasses = this.classRepository.findBySchoolId(schoolId);
        return schoolClasses.stream().anyMatch(c -> c.getClassName().equalsIgnoreCase(className));
    }

    public List<ClassEntity> getClassByClassNameAndSchoolId(String className, String schoolId) {
        List classEntity = this.classRepository.findByUniversalTrueOrSchoolIdOrClassNameIgnoreCase(className, schoolId);
        return classEntity;
    }
}

