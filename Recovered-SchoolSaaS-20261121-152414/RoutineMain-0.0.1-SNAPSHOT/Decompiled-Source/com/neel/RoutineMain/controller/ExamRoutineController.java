/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.RoutineMain.controller.ExamRoutineController
 *  com.neel.RoutineMain.model.ExamRoutine
 *  com.neel.RoutineMain.model.ResponseMessage
 *  com.neel.RoutineMain.service.ExamRoutineService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.http.HttpStatus
 *  org.springframework.http.HttpStatusCode
 *  org.springframework.http.MediaType
 *  org.springframework.http.ResponseEntity
 *  org.springframework.http.ResponseEntity$BodyBuilder
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

import com.neel.RoutineMain.model.ExamRoutine;
import com.neel.RoutineMain.model.ResponseMessage;
import com.neel.RoutineMain.service.ExamRoutineService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
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

@RestController
@CrossOrigin(origins={"*"})
@RequestMapping(value={"/api/exam-routines"})
public class ExamRoutineController {
    @Autowired
    private ExamRoutineService service;

    @PostMapping
    public ResponseEntity<ResponseMessage> createRoutine(@RequestBody ExamRoutine routine) {
        try {
            ExamRoutine createdRoutine = this.service.createRoutine(routine);
            return ResponseEntity.ok((Object)new ResponseMessage("Routine created successfully.", (Object)createdRoutine));
        }
        catch (RuntimeException e) {
            return ResponseEntity.badRequest().body((Object)new ResponseMessage(e.getMessage(), null));
        }
    }

    @GetMapping
    public ResponseEntity<ResponseMessage> getAllRoutines() {
        List routines = this.service.getAllRoutines();
        return ResponseEntity.ok((Object)new ResponseMessage("All routines retrieved successfully", (Object)routines));
    }

    @PutMapping(value={"/{id}"})
    public ResponseEntity<ResponseMessage> updateRoutine(@PathVariable String id, @RequestBody ExamRoutine updatedRoutine) {
        try {
            ExamRoutine updated = this.service.updateRoutine(id, updatedRoutine);
            return ResponseEntity.ok((Object)new ResponseMessage("Routine updated successfully.", (Object)updated));
        }
        catch (RuntimeException e) {
            return ResponseEntity.badRequest().body((Object)new ResponseMessage(e.getMessage(), null));
        }
    }

    @GetMapping(value={"/search"})
    public ResponseEntity<ResponseMessage> getRoutineByClass(@RequestParam String className, @RequestParam String examType) {
        List routine = this.service.getRoutineByClass(className, examType);
        if (!routine.isEmpty()) {
            return ResponseEntity.ok((Object)new ResponseMessage("Routines found successfully", (Object)routine));
        }
        return ResponseEntity.status((int)404).body((Object)new ResponseMessage("No routines found", null));
    }

    @GetMapping(value={"/search/download"})
    public ResponseEntity<byte[]> downloadRoutinePdf(@RequestParam String className, @RequestParam String examType) {
        try {
            byte[] pdfBytes = this.service.generateRoutinePdf(className, examType);
            String fileName = String.format("Routine_%s.pdf", className);
            return ((ResponseEntity.BodyBuilder)ResponseEntity.ok().header("Content-Disposition", new String[]{"attachment; filename=" + fileName})).contentType(MediaType.APPLICATION_PDF).body((Object)pdfBytes);
        }
        catch (RuntimeException e) {
            return ((ResponseEntity.BodyBuilder)ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).header("Content-Type", new String[]{"text/plain"})).body((Object)("Error: " + e.getMessage()).getBytes());
        }
    }

    @DeleteMapping(value={"/{id}"})
    public ResponseEntity<ResponseMessage> deleteRoutine(@PathVariable String id) {
        try {
            this.service.deleteRoutine(id);
            return ResponseEntity.ok((Object)new ResponseMessage("Routine deleted successfully.", null));
        }
        catch (RuntimeException e) {
            return ResponseEntity.badRequest().body((Object)new ResponseMessage(e.getMessage(), null));
        }
    }

    @PostMapping(value={"/{schoolId}"})
    public ResponseEntity<ResponseMessage> createRoutineWithSchool(@RequestBody ExamRoutine routine, @PathVariable String schoolId) {
        try {
            ExamRoutine createdRoutine = this.service.createRoutineWithSchool(routine, schoolId);
            return ResponseEntity.ok((Object)new ResponseMessage("Routine created successfully for school.", (Object)createdRoutine));
        }
        catch (RuntimeException e) {
            return ResponseEntity.badRequest().body((Object)new ResponseMessage(e.getMessage(), null));
        }
    }

    @GetMapping(value={"/all/{schoolId}"})
    public ResponseEntity<ResponseMessage> getAllRoutinesBySchool(@PathVariable String schoolId) {
        List routines = this.service.getAllRoutinesBySchool(schoolId);
        return ResponseEntity.ok((Object)new ResponseMessage("All routines retrieved successfully for school", (Object)routines));
    }

    @PutMapping(value={"/{id}/{schoolId}"})
    public ResponseEntity<ResponseMessage> updateRoutineWithSchool(@PathVariable String id, @RequestBody ExamRoutine updatedRoutine, @PathVariable String schoolId) {
        try {
            return ResponseEntity.ok((Object)new ResponseMessage("Routine updated successfully for school.", (Object)this.service.updateRoutineWithSchool(id, updatedRoutine, schoolId)));
        }
        catch (RuntimeException e) {
            return ResponseEntity.badRequest().body((Object)new ResponseMessage(e.getMessage(), null));
        }
    }

    @GetMapping(value={"/{schoolId}/search"})
    public ResponseEntity<ResponseMessage> getRoutineByClassAndSchool(@RequestParam String className, @RequestParam String examType, @PathVariable String schoolId) {
        List routine = this.service.getRoutineByClassAndSchool(className, examType, schoolId);
        if (!routine.isEmpty()) {
            return ResponseEntity.ok((Object)new ResponseMessage("Routines found successfully for school", (Object)routine));
        }
        return ResponseEntity.status((int)404).body((Object)new ResponseMessage("No routines found for this school", null));
    }

    @GetMapping(value={"/{schoolId}/search/download"})
    public ResponseEntity<byte[]> downloadRoutinePdfWithSchool(@RequestParam String className, @RequestParam String examType, @PathVariable String schoolId) {
        try {
            byte[] pdfBytes = this.service.generateRoutinePdfWithSchool(className, examType, schoolId);
            String fileName = String.format("Routine_%s.pdf", className);
            return ((ResponseEntity.BodyBuilder)ResponseEntity.ok().header("Content-Disposition", new String[]{"attachment; filename=" + fileName})).contentType(MediaType.APPLICATION_PDF).body((Object)pdfBytes);
        }
        catch (RuntimeException e) {
            return ((ResponseEntity.BodyBuilder)ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).header("Content-Type", new String[]{"text/plain"})).body((Object)("Error: " + e.getMessage()).getBytes());
        }
    }

    @DeleteMapping(value={"/{id}/{schoolId}"})
    public ResponseEntity<ResponseMessage> deleteRoutineWithSchool(@PathVariable String id, @PathVariable String schoolId) {
        try {
            boolean deleted = this.service.deleteRoutineWithSchool(id, schoolId);
            if (deleted) {
                return ResponseEntity.ok((Object)new ResponseMessage("Routine deleted successfully for school.", null));
            }
            return ResponseEntity.status((int)404).body((Object)new ResponseMessage("Routine not found or does not belong to this school", null));
        }
        catch (RuntimeException e) {
            return ResponseEntity.badRequest().body((Object)new ResponseMessage(e.getMessage(), null));
        }
    }
}

