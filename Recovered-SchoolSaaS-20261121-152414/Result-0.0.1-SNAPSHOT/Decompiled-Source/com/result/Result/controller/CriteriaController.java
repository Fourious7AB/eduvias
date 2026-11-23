/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.result.Result.controller.CriteriaController
 *  com.result.Result.model.Criteria
 *  com.result.Result.model.ResponseModel
 *  com.result.Result.service.CriteriaService
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
package com.result.Result.controller;

import com.result.Result.model.Criteria;
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
            return new ResponseEntity((Object)createdCriteria, (HttpStatusCode)HttpStatus.CREATED);
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity((Object)e.getMessage(), (HttpStatusCode)HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            return new ResponseEntity((Object)("An error occurred: " + e.getMessage()), (HttpStatusCode)HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value={"/{schoolId}"})
    public ResponseEntity<?> getAllCriteria(@PathVariable String schoolId) {
        try {
            List criteriaList = this.criteriaService.getAllCriteria(schoolId);
            return new ResponseEntity((Object)criteriaList, (HttpStatusCode)HttpStatus.OK);
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity((Object)e.getMessage(), (HttpStatusCode)HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            return new ResponseEntity((Object)("An error occurred: " + e.getMessage()), (HttpStatusCode)HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value={"/{id}/{schoolId}"})
    public ResponseEntity<?> getCriteriaById(@PathVariable String schoolId, @PathVariable String id) {
        try {
            Optional criteria = this.criteriaService.getCriteriaById(schoolId, id);
            if (criteria.isPresent()) {
                return new ResponseEntity((Object)((Criteria)criteria.get()), (HttpStatusCode)HttpStatus.OK);
            }
            return new ResponseEntity((Object)"Criteria not found", (HttpStatusCode)HttpStatus.NOT_FOUND);
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity((Object)e.getMessage(), (HttpStatusCode)HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            return new ResponseEntity((Object)("An error occurred: " + e.getMessage()), (HttpStatusCode)HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value={"/{id}/{schoolId}"})
    public ResponseEntity<?> updateCriteria(@PathVariable String schoolId, @PathVariable String id, @RequestBody Criteria criteria) {
        try {
            Optional updatedCriteria = this.criteriaService.updateCriteria(schoolId, id, criteria);
            if (updatedCriteria.isPresent()) {
                return new ResponseEntity((Object)((Criteria)updatedCriteria.get()), (HttpStatusCode)HttpStatus.OK);
            }
            return new ResponseEntity((Object)"Criteria not found", (HttpStatusCode)HttpStatus.NOT_FOUND);
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity((Object)e.getMessage(), (HttpStatusCode)HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            return new ResponseEntity((Object)("An error occurred: " + e.getMessage()), (HttpStatusCode)HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value={"/search/{schoolId}"})
    public ResponseEntity<ResponseModel> viewAllSubject(@PathVariable String schoolId, @RequestParam String className, @RequestParam String examType) {
        try {
            List allSubjects = this.criteriaService.getAllSubjects(schoolId, className, examType);
            if (allSubjects.isEmpty()) {
                return new ResponseEntity((Object)new ResponseModel(false, "Subject not available on in database.", null), (HttpStatusCode)HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity((Object)new ResponseModel(true, "All subject fetched successfully.", (Object)allSubjects), (HttpStatusCode)HttpStatus.OK);
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity((Object)new ResponseModel(false, e.getMessage(), null), (HttpStatusCode)HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping(value={"/{id}/{schoolId}"})
    public ResponseEntity<?> deleteCriteria(@PathVariable String schoolId, @PathVariable String id) {
        try {
            Optional deletedCriteria = this.criteriaService.deleteCriteria(schoolId, id);
            if (deletedCriteria.isPresent()) {
                return new ResponseEntity((Object)"Criteria deleted successfully", (HttpStatusCode)HttpStatus.OK);
            }
            return new ResponseEntity((Object)"Criteria not found", (HttpStatusCode)HttpStatus.NOT_FOUND);
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity((Object)e.getMessage(), (HttpStatusCode)HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            return new ResponseEntity((Object)("An error occurred: " + e.getMessage()), (HttpStatusCode)HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

