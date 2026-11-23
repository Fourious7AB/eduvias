/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.AcademicMain.controller.ServicesController
 *  com.neel.AcademicMain.dto.ApiResponseDto
 *  com.neel.AcademicMain.models.Services
 *  com.neel.AcademicMain.services.ServicesService
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
package com.neel.AcademicMain.controller;

import com.neel.AcademicMain.dto.ApiResponseDto;
import com.neel.AcademicMain.models.Services;
import com.neel.AcademicMain.services.ServicesService;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/api/services"})
@CrossOrigin(origins={"*"})
public class ServicesController {
    private final ServicesService servicesService;

    @Autowired
    public ServicesController(ServicesService servicesService) {
        this.servicesService = servicesService;
    }

    @PostMapping
    public ResponseEntity<Services> createService(@RequestBody Services service) {
        Services createdService = this.servicesService.createService(service);
        return new ResponseEntity<>(createdService, HttpStatus.CREATED);
    }

    @PostMapping(value={"/universal"})
    public ResponseEntity<Services> createUniversalService(@RequestBody Services service) {
        service.setUniversal(true);
        service.setSchoolId(null);
        Services createdService = this.servicesService.createService(service);
        return new ResponseEntity<>(createdService, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Services>> getAllServices() {
        List<Services> services = this.servicesService.getAllServices();
        return new ResponseEntity<>(services, HttpStatus.OK);
    }

    @GetMapping(value={"/{id}"})
    public ResponseEntity<Services> getServiceById(@PathVariable String id) {
        Services service = this.servicesService.getServiceById(id);
        if (service != null) {
            return new ResponseEntity<>(service, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(value={"update/{id}"})
    public ResponseEntity<ApiResponseDto> updateService(@PathVariable String id, @RequestBody Services service) {
        Services updatedService = this.servicesService.updateServiceIfUnassignedSchool(id, service);
        return ResponseEntity.status((HttpStatusCode)HttpStatus.OK).body(new ApiResponseDto(Boolean.valueOf(true), "Service updated successfully", updatedService));
    }

    @PutMapping(value={"/assigned/{id}"})
    public ResponseEntity<ApiResponseDto> updateIfAssigned(@PathVariable String id, @RequestBody Services service) {
        Services updated = this.servicesService.updateServiceIfAssigned(id, service);
        return ResponseEntity.status((HttpStatusCode)HttpStatus.OK).body(new ApiResponseDto(Boolean.valueOf(true), "your services updated successfully", updated));
    }

    @DeleteMapping(value={"/unassigned/{id}"})
    public ResponseEntity<ApiResponseDto> deleteIfUnassigned(@PathVariable String id) {
        this.servicesService.deleteServiceIfUnassigned(id);
        return ResponseEntity.status((HttpStatusCode)HttpStatus.OK).body(new ApiResponseDto(Boolean.valueOf(true), "services deleted successfully", null));
    }

    @DeleteMapping(value={"/assigned/{id}"})
    public ResponseEntity<ApiResponseDto> deleteIfAssigned(@PathVariable String id) {
        this.servicesService.deleteServiceIfAssigned(id);
        return ResponseEntity.status((HttpStatusCode)HttpStatus.OK).body(new ApiResponseDto(Boolean.valueOf(true), " your services deleted successfully", null));
    }

    @GetMapping(value={"/universal"})
    public ResponseEntity<List<Services>> getAllUniversalServices() {
        List<Services> universalServices = this.servicesService.getAllUniversalServices();
        return new ResponseEntity<>(universalServices, HttpStatus.OK);
    }

    @GetMapping(value={"/school/{schoolId}"})
    public ResponseEntity<List<Services>> getServicesBySchoolId(@PathVariable String schoolId) {
        List<Services> schoolServices = this.servicesService.getServicesBySchoolId(schoolId);
        return new ResponseEntity<>(schoolServices, HttpStatus.OK);
    }

    @GetMapping(value={"/all/{schoolId}"})
    public ResponseEntity<List<Services>> getUniversalAndSchoolServices(@PathVariable String schoolId) {
        List<Services> allServices = this.servicesService.getUniversalAndSchoolServices(schoolId);
        return new ResponseEntity<>(allServices, HttpStatus.OK);
    }
}

