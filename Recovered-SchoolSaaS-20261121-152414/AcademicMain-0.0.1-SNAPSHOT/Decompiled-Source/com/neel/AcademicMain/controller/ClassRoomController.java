/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.AcademicMain.controller.ClassRoomController
 *  com.neel.AcademicMain.models.ClassRoom
 *  com.neel.AcademicMain.services.ClassRoomService
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
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.RestController
 */
package com.neel.AcademicMain.controller;

import com.neel.AcademicMain.models.ClassRoom;
import com.neel.AcademicMain.services.ClassRoomService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins={"*"})
@RestController
@RequestMapping(value={"/api/v1/Academic"})
public class ClassRoomController {
    @Autowired
    private ClassRoomService classRoomService;

    @GetMapping(value={"/class-room"})
    public List<ClassRoom> getAllRoom() {
        return this.classRoomService.getAllRoom();
    }

    @GetMapping(value={"/class-room/{schoolId}"})
    public ResponseEntity<List<ClassRoom>> getRoomsBySchoolId(@PathVariable String schoolId) {
        List classRooms = this.classRoomService.getRoomsBySchoolId(schoolId);
        if (classRooms.isEmpty()) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok((Object)classRooms);
    }

    @PostMapping(value={"/class-room/{schoolId}"})
    public ResponseEntity<String> createClassRoom(@PathVariable String schoolId, @RequestBody ClassRoom classRoom) {
        try {
            classRoom.setSchoolId(schoolId);
            this.classRoomService.addRoom(classRoom);
            return ResponseEntity.ok((Object)"Classroom created successfully!");
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body((Object)e.getMessage());
        }
    }

    @PutMapping(value={"/class-room/{id}/{schoolId}"})
    public ResponseEntity<String> updateClassRoom(@PathVariable String schoolId, @PathVariable String id, @RequestBody ClassRoom classRoom) {
        try {
            this.classRoomService.updateClassRoom(schoolId, id, classRoom);
            return ResponseEntity.ok((Object)"Classroom updated successfully!");
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body((Object)e.getMessage());
        }
    }

    @DeleteMapping(value={"/class-room/{id}/{schoolId}"})
    public ResponseEntity<String> deleteRoom(@PathVariable String schoolId, @PathVariable String id) {
        try {
            this.classRoomService.deleteRoom(schoolId, id);
            return ResponseEntity.ok((Object)"Classroom deleted successfully!");
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body((Object)e.getMessage());
        }
    }

    @GetMapping(value={"/class-room/{schoolId}/search"})
    public ResponseEntity<List<ClassRoom>> getClassRoomsByClassIdAndSchool(@PathVariable String schoolId, @RequestParam String classId) {
        try {
            List classRooms = this.classRoomService.getClassRoomsByClassIdAndSchool(classId, schoolId);
            if (classRooms.isEmpty()) {
                return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok((Object)classRooms);
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

