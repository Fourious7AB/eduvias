/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.StudentAdmission.controller.PlanController
 *  com.neel.StudentAdmission.model.Plan
 *  com.neel.StudentAdmission.model.ResponseMessage
 *  com.neel.StudentAdmission.model.ResponseModel
 *  com.neel.StudentAdmission.service.PlanService
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
 *  org.springframework.web.bind.annotation.RestController
 */
package com.neel.StudentAdmission.controller;

import com.neel.StudentAdmission.model.Plan;
import com.neel.StudentAdmission.model.ResponseMessage;
import com.neel.StudentAdmission.model.ResponseModel;
import com.neel.StudentAdmission.service.PlanService;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(value={"*"})
@RequestMapping(value={"/api/v1/plan"})
public class PlanController {
    @Autowired
    private PlanService subsPlanService;

    @PostMapping
    public ResponseEntity<ResponseMessage> createPlan(@RequestBody Plan plan) {
        Plan savePlan = this.subsPlanService.createPlan(plan);
        return new ResponseEntity((Object)new ResponseMessage("Plan created successfully", (Object)savePlan), (HttpStatusCode)HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ResponseModel> allPlan() {
        List planList = this.subsPlanService.getAllPlans();
        if (planList.isEmpty()) {
            return new ResponseEntity((Object)new ResponseModel(Boolean.valueOf(false), "Plan not available in database.", null), (HttpStatusCode)HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity((Object)new ResponseModel(Boolean.valueOf(true), "All plan retrieve successfully.", (Object)planList), (HttpStatusCode)HttpStatus.OK);
    }

    @GetMapping(value={"/{id}"})
    public ResponseEntity<ResponseModel> viewPlanById(@PathVariable String id) {
        Optional existPLan = this.subsPlanService.getPlanById(id);
        if (existPLan.isPresent()) {
            return new ResponseEntity((Object)new ResponseModel(Boolean.valueOf(true), "plan retrieve successfully", (Object)existPLan), (HttpStatusCode)HttpStatus.OK);
        }
        return new ResponseEntity((Object)new ResponseModel(Boolean.valueOf(true), "plan not found", null), (HttpStatusCode)HttpStatus.NOT_FOUND);
    }

    @PutMapping(value={"/{id}"})
    public ResponseEntity<ResponseModel> updatePlan(@PathVariable String id, @RequestBody Plan newPlan) {
        Optional updatedPLan = this.subsPlanService.updatePlan(id, newPlan);
        if (updatedPLan.isPresent()) {
            return new ResponseEntity((Object)new ResponseModel(Boolean.valueOf(true), "plan updated successfully", (Object)updatedPLan), (HttpStatusCode)HttpStatus.OK);
        }
        return new ResponseEntity((Object)new ResponseModel(Boolean.valueOf(true), "plan not updated", null), (HttpStatusCode)HttpStatus.OK);
    }

    @DeleteMapping(value={"/{id}"})
    public ResponseEntity<ResponseModel> deletePlan(@PathVariable String id) {
        Optional deletedPlan = this.subsPlanService.deletePlan(id);
        if (deletedPlan.isPresent()) {
            return new ResponseEntity((Object)new ResponseModel(Boolean.valueOf(true), "plan deleted successfully", null), (HttpStatusCode)HttpStatus.OK);
        }
        return new ResponseEntity((Object)new ResponseModel(Boolean.valueOf(true), "plan not found", null), (HttpStatusCode)HttpStatus.NOT_FOUND);
    }
}

