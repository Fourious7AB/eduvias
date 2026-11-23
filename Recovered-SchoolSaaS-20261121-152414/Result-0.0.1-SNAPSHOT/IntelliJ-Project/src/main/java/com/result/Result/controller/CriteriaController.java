package com.result.Result.controller;

import com.result.Result.model.Criteria;
import com.result.Result.model.ResponseMessage;
import com.result.Result.model.ResponseModel;
import com.result.Result.service.CriteriaService;
import java.util.List;
import java.util.Optional;
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

@RestController
@CrossOrigin(value={"*"})
@RequestMapping(value={"/api/schools/criteria"})
public class CriteriaController {
    @Autowired
    private CriteriaService criteriaService;

    @PostMapping(value={"/{schoolId}"})
    public ResponseEntity<?> createCriteria(@PathVariable String schoolId, @RequestBody Criteria criteria) {
        try {
            Criteria createdCriteria = this.criteriaService.createCriteria(schoolId, criteria);
            return new ResponseEntity<>(createdCriteria, (HttpStatusCode) HttpStatus.CREATED);
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ResponseMessage(e.getMessage(), null), (HttpStatusCode) HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            return new ResponseEntity<>(new ResponseMessage("An error occurred: " + e.getMessage(), null), (HttpStatusCode) HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value={"/{schoolId}"})
    public ResponseEntity<?> getAllCriteria(@PathVariable String schoolId) {
        try {
            List<Criteria> criteriaList = this.criteriaService.getAllCriteria(schoolId);
            return new ResponseEntity<>(criteriaList, (HttpStatusCode) HttpStatus.OK);
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ResponseMessage(e.getMessage(), null), (HttpStatusCode) HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            return new ResponseEntity<>(new ResponseMessage("An error occurred: " + e.getMessage(), null), (HttpStatusCode) HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value={"/{id}/{schoolId}"})
    public ResponseEntity<?> getCriteriaById(@PathVariable String schoolId, @PathVariable String id) {
        try {
            Optional<Criteria> criteria = this.criteriaService.getCriteriaById(schoolId, id);
            if (criteria.isPresent()) {
                return new ResponseEntity<>(criteria.get(), (HttpStatusCode) HttpStatus.OK);
            }
            return new ResponseEntity<>(new ResponseMessage("Criteria not found", null), (HttpStatusCode) HttpStatus.NOT_FOUND);
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ResponseMessage(e.getMessage(), null), (HttpStatusCode) HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            return new ResponseEntity<>(new ResponseMessage("An error occurred: " + e.getMessage(), null), (HttpStatusCode) HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value={"/{id}/{schoolId}"})
    public ResponseEntity<?> updateCriteria(@PathVariable String schoolId, @PathVariable String id, @RequestBody Criteria criteria) {
        try {
            Optional<Criteria> updatedCriteria = this.criteriaService.updateCriteria(schoolId, id, criteria);
            if (updatedCriteria.isPresent()) {
                return new ResponseEntity<>(updatedCriteria.get(), (HttpStatusCode) HttpStatus.OK);
            }
            return new ResponseEntity<>(new ResponseMessage("Criteria not found", null), (HttpStatusCode) HttpStatus.NOT_FOUND);
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ResponseMessage(e.getMessage(), null), (HttpStatusCode) HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            return new ResponseEntity<>(new ResponseMessage("An error occurred: " + e.getMessage(), null), (HttpStatusCode) HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value={"/search/{schoolId}"})
    public ResponseEntity<ResponseModel> viewAllSubject(@PathVariable String schoolId, @RequestParam String className, @RequestParam String examType) {
        try {
            List<Criteria.Subject> allSubjects = this.criteriaService.getAllSubjects(schoolId, className, examType);
            if (allSubjects.isEmpty()) {
                return new ResponseEntity<>(new ResponseModel(false, "Subject not available in database.", null), (HttpStatusCode) HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(new ResponseModel(true, "All subject fetched successfully.", allSubjects), (HttpStatusCode) HttpStatus.OK);
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ResponseModel(false, e.getMessage(), null), (HttpStatusCode) HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            return new ResponseEntity<>(new ResponseModel(false, "An error occurred: " + e.getMessage(), null), (HttpStatusCode) HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value={"/{id}/{schoolId}"})
    public ResponseEntity<?> deleteCriteria(@PathVariable String schoolId, @PathVariable String id) {
        try {
            Optional<Criteria> deletedCriteria = this.criteriaService.deleteCriteria(schoolId, id);
            if (deletedCriteria.isPresent()) {
                return new ResponseEntity<>(new ResponseMessage("Criteria deleted successfully", null), (HttpStatusCode) HttpStatus.OK);
            }
            return new ResponseEntity<>(new ResponseMessage("Criteria not found", null), (HttpStatusCode) HttpStatus.NOT_FOUND);
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ResponseMessage(e.getMessage(), null), (HttpStatusCode) HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            return new ResponseEntity<>(new ResponseMessage("An error occurred: " + e.getMessage(), null), (HttpStatusCode) HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value={"/{id}/{schoolId}/subject/{subjectName}"})
    public ResponseEntity<?> deleteSubjectFromCriteria(
            @PathVariable String schoolId,
            @PathVariable String id,
            @PathVariable String subjectName) {
        try {
            Optional<Criteria> updated = this.criteriaService.deleteSubjectFromCriteria(schoolId, id, subjectName);
            if (updated.isPresent()) {
                return new ResponseEntity<>(new ResponseMessage("Subject removed from criteria successfully", updated.get()), (HttpStatusCode) HttpStatus.OK);
            }
            return new ResponseEntity<>(new ResponseMessage("Criteria not found", null), (HttpStatusCode) HttpStatus.NOT_FOUND);
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ResponseMessage(e.getMessage(), null), (HttpStatusCode) HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            return new ResponseEntity<>(new ResponseMessage("An error occurred: " + e.getMessage(), null), (HttpStatusCode) HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
