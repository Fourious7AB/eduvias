/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.AcademicMain.exception.ClassNameAlreadyExist
 *  com.neel.AcademicMain.exception.ClassNotFound
 *  com.neel.AcademicMain.models.ClassRoom
 *  com.neel.AcademicMain.repo.ClassRoomRepository
 *  com.neel.AcademicMain.services.ClassRoomService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 */
package com.neel.AcademicMain.services;

import com.neel.AcademicMain.exception.ClassNameAlreadyExist;
import com.neel.AcademicMain.exception.ClassNotFound;
import com.neel.AcademicMain.models.ClassRoom;
import com.neel.AcademicMain.repo.ClassRoomRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClassRoomService {
    @Autowired
    private ClassRoomRepository classRoomRepository;

    public List<ClassRoom> getAllRoom() {
        return this.classRoomRepository.findAll();
    }

    public List<ClassRoom> getRoomsBySchoolId(String schoolId) {
        return this.classRoomRepository.findBySchoolId(schoolId);
    }

    public ClassRoom addRoom(ClassRoom classRoom) {
        Optional<ClassRoom> existingClassRoom = this.classRoomRepository.findByRoomNoAndSchoolId(classRoom.getRoomNo(), classRoom.getSchoolId());
        if (existingClassRoom.isPresent()) {
            throw new ClassNameAlreadyExist("Classroom with this room number already exists for this school!");
        }
        ClassRoom lastClassRoom = this.classRoomRepository.findTopByOrderByClassRoomIdDesc();
        int nextClassRoomId = lastClassRoom != null ? lastClassRoom.getClassRoomId() + 1 : 1;
        classRoom.setClassRoomId(nextClassRoomId);
        return this.classRoomRepository.save(classRoom);
    }

    public ClassRoom updateClassRoom(String schoolId, String id, ClassRoom updatedClassRoom) {
        ClassRoom existingClassRoom = this.classRoomRepository.findById(id).orElseThrow(() -> new ClassNotFound("Classroom not found"));
        if (!existingClassRoom.getSchoolId().equals(schoolId)) {
            throw new ClassNotFound("Classroom does not belong to the specified school");
        }
        Optional<ClassRoom> duplicateClassRoom = this.classRoomRepository.findByRoomNoAndSchoolId(updatedClassRoom.getRoomNo(), schoolId);
        if (duplicateClassRoom.isPresent() && !duplicateClassRoom.get().getId().equals(id)) {
            throw new ClassNameAlreadyExist("Classroom with this room number already exists for this school!");
        }
        existingClassRoom.setClassId(updatedClassRoom.getClassId());
        existingClassRoom.setSectionId(updatedClassRoom.getSectionId());
        existingClassRoom.setRoomNo(updatedClassRoom.getRoomNo());
        existingClassRoom.setSchoolId(schoolId);
        return this.classRoomRepository.save(existingClassRoom);
    }

    public void deleteRoom(String schoolId, String id) {
        ClassRoom classRoom = this.classRoomRepository.findById(id).orElseThrow(() -> new ClassNotFound("Classroom not found"));
        if (!classRoom.getSchoolId().equals(schoolId)) {
            throw new ClassNotFound("Classroom does not belong to the specified school");
        }
        this.classRoomRepository.deleteById(id);
    }

    public List<ClassRoom> getClassRoomsByClassIdAndSchool(String classId, String schoolId) {
        return this.classRoomRepository.findByClassIdAndSchoolId(classId, schoolId);
    }

    public List<ClassRoom> getClassRoomsByClassId(String classId) {
        return this.classRoomRepository.findByClassId(classId);
    }
}

