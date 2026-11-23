/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.RoutineMain.controller.RoutineController
 *  com.neel.RoutineMain.model.ResponseMessage
 *  com.neel.RoutineMain.model.Routine
 *  com.neel.RoutineMain.service.RoutineService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.http.HttpHeaders
 *  org.springframework.http.HttpStatus
 *  org.springframework.http.HttpStatusCode
 *  org.springframework.http.MediaType
 *  org.springframework.http.ResponseEntity
 *  org.springframework.util.MultiValueMap
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
package com.neel.RoutineMain.controller;

import com.neel.RoutineMain.model.ResponseMessage;
import com.neel.RoutineMain.model.Routine;
import com.neel.RoutineMain.service.RoutineService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
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

@RestController
@CrossOrigin(origins={"*"})
@RequestMapping(value={"/api/v1/routine"})
public class RoutineController {
    @Autowired
    private RoutineService routineService;

    @PostMapping
    public ResponseEntity<ResponseMessage> createRoutine(@RequestBody Routine routine) {
        ResponseMessage response = this.routineService.createRoutine(routine);
        return ResponseEntity.status((HttpStatusCode)HttpStatus.CREATED).body((Object)response);
    }

    @PutMapping(value={"/{id}"})
    public ResponseEntity<ResponseMessage> updateRoutine(@PathVariable String id, @RequestBody Routine routine) {
        ResponseMessage response = this.routineService.updateRoutine(id, routine);
        return ResponseEntity.ok((Object)response);
    }

    @GetMapping
    public ResponseEntity<List<Routine>> getAllRoutines() {
        List routines = this.routineService.getAllRoutines();
        return ResponseEntity.ok((Object)routines);
    }

    @GetMapping(value={"/{id}"})
    public ResponseEntity<Routine> getRoutineById(@PathVariable String id) {
        Routine routine = this.routineService.getRoutineById(id);
        if (routine != null) {
            return ResponseEntity.ok((Object)routine);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(value={"/search"})
    public ResponseEntity<List<Routine>> searchRoutineByClassAndSection(@RequestParam String className, @RequestParam String section) {
        List routines = this.routineService.searchRoutineByClassAndSection(className, section);
        return ResponseEntity.ok((Object)routines);
    }

    @DeleteMapping(value={"/{id}"})
    public ResponseEntity<Void> deleteRoutine(@PathVariable String id) {
        this.routineService.deleteRoutine(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value={"/{schoolId}"})
    public ResponseEntity<ResponseMessage> createRoutineWithSchool(@RequestBody Routine routine, @PathVariable String schoolId) {
        ResponseMessage response = this.routineService.createRoutineWithSchool(routine, schoolId);
        System.out.println(" i m  from creat routinge with school  " + response.getMessage());
        return ResponseEntity.status((HttpStatusCode)HttpStatus.CREATED).body((Object)response);
    }

    @PutMapping(value={"/{id}/{schoolId}"})
    public ResponseEntity<ResponseMessage> updateRoutineWithSchool(@PathVariable String id, @RequestBody Routine routine, @PathVariable String schoolId) {
        ResponseMessage response = this.routineService.updateRoutineWithSchool(id, routine, schoolId);
        if (response.getMessage().contains("not found") || response.getMessage().contains("don't have access")) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.FORBIDDEN).body((Object)response);
        }
        return ResponseEntity.ok((Object)response);
    }

    @GetMapping(value={"/all/{schoolId}"})
    public ResponseEntity<List<Routine>> getAllRoutinesBySchool(@PathVariable String schoolId) {
        List routines = this.routineService.getAllRoutinesBySchool(schoolId);
        return ResponseEntity.ok((Object)routines);
    }

    @GetMapping(value={"/{id}/{schoolId}"})
    public ResponseEntity<Routine> getRoutineByIdAndSchool(@PathVariable String id, @PathVariable String schoolId) {
        Routine routine = this.routineService.getRoutineByIdAndSchool(id, schoolId);
        if (routine != null) {
            return ResponseEntity.ok((Object)routine);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(value={"/{schoolId}/search"})
    public ResponseEntity<List<Routine>> searchRoutineByClassAndSectionWithSchool(@RequestParam String className, @RequestParam String section, @PathVariable String schoolId) {
        List routines = this.routineService.searchRoutineByClassAndSectionWithSchool(className, section, schoolId);
        System.out.println("Routines: " + routines);
        return ResponseEntity.ok((Object)routines);
    }

    @DeleteMapping(value={"/{id}/{schoolId}"})
    public ResponseEntity<ResponseMessage> deleteRoutineWithSchool(@PathVariable String id, @PathVariable String schoolId) {
        boolean deleted = this.routineService.deleteRoutineWithSchool(id, schoolId);
        if (deleted) {
            return ResponseEntity.ok((Object)new ResponseMessage("Routine deleted successfully", null));
        }
        return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body((Object)new ResponseMessage("Routine not found or does not belong to this school", null));
    }

    @GetMapping(value={"/{schoolId}/search/download"})
    public ResponseEntity<byte[]> generateRoutinePdfWithSchool(@RequestParam String className, @RequestParam String section, @PathVariable String schoolId) {
        try {
            byte[] pdfBytes = this.routineService.generateRoutinePdfWithSchool(className, section, schoolId);
            if (pdfBytes == null || pdfBytes.length == 0) {
                System.err.println("PDF generation failed: Empty PDF returned");
                return ResponseEntity.status((HttpStatusCode)HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "routine.pdf");
            return new ResponseEntity((Object)pdfBytes, (MultiValueMap)headers, (HttpStatusCode)HttpStatus.OK);
        }
        catch (Exception e) {
            System.err.println("Error generating PDF: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status((HttpStatusCode)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value={"/teacher/{schoolId}"})
    public ResponseEntity<ResponseMessage> getTeacherSpecificRoutinesWithSchool(@RequestParam String teacherName, @PathVariable String schoolId) {
        ResponseMessage response = this.routineService.getTeacherSpecificRoutinesWithSchool(teacherName, schoolId);
        return ResponseEntity.ok((Object)response);
    }

    @GetMapping(value={"/teacher/{schoolId}/pdf"})
    public ResponseEntity<byte[]> generateTeacherRoutinePdfWithSchool(@RequestParam String teacherName, @PathVariable String schoolId) {
        try {
            byte[] pdfBytes = this.routineService.generateTeacherRoutinePdfWithSchool(teacherName, schoolId);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "teacher_routine.pdf");
            return new ResponseEntity((Object)pdfBytes, (MultiValueMap)headers, (HttpStatusCode)HttpStatus.OK);
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

