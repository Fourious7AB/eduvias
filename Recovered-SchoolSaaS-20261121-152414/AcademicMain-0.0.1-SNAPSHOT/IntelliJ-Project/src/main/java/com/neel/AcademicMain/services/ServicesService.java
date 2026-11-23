/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.AcademicMain.exception.ClassNameAlreadyExist
 *  com.neel.AcademicMain.models.Services
 *  com.neel.AcademicMain.repo.ServicesRepository
 *  com.neel.AcademicMain.services.ServicesService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 */
package com.neel.AcademicMain.services;

import com.neel.AcademicMain.exception.ClassNameAlreadyExist;
import com.neel.AcademicMain.models.Services;
import com.neel.AcademicMain.repo.ServicesRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServicesService {
    private final ServicesRepository servicesRepository;

    @Autowired
    public ServicesService(ServicesRepository servicesRepository) {
        this.servicesRepository = servicesRepository;
    }

    public Services createService(Services service) {
        service.setUniversal(false);
        return this.servicesRepository.save(service);
    }

    public Services getServiceById(String id) {
        Optional<Services> serviceOptional = this.servicesRepository.findById(id);
        return serviceOptional.orElse(null);
    }

    public List<Services> getAllServices() {
        return this.servicesRepository.findAll();
    }

    public Services updateServiceIfUnassignedSchool(String id, Services service) {
        if (service == null) {
            throw new IllegalArgumentException("Update payload must not be null");
        }
        Services existing = this.servicesRepository.findById(id).orElseThrow(() -> new ClassNameAlreadyExist("Service not found with id: " + id));
        if (existing.getSchoolId() != null) {
            throw new ClassNameAlreadyExist("You don't have access to update this service");
        }
        this.applyUpdates(existing, service);
        return this.servicesRepository.save(existing);
    }

    public Services updateServiceIfAssigned(String id, Services service) {
        if (service == null) {
            throw new IllegalArgumentException("Update payload must not be null");
        }
        Services existing = this.servicesRepository.findById(id).orElseThrow(() -> new ClassNameAlreadyExist("Service not found with id: " + id));
        if (existing.getSchoolId() == null) {
            throw new ClassNameAlreadyExist("You don't have access to update this service");
        }
        this.applyUpdates(existing, service);
        return this.servicesRepository.save(existing);
    }

    public void deleteServiceIfUnassigned(String id) {
        Services existing = this.servicesRepository.findById(id).orElseThrow(() -> new ClassNameAlreadyExist("Service not found with id: " + id));
        if (existing.getSchoolId() != null) {
            throw new ClassNameAlreadyExist("You don't have access to delete this service");
        }
        this.servicesRepository.delete(existing);
    }

    public void deleteServiceIfAssigned(String id) {
        Services existing = this.servicesRepository.findById(id).orElseThrow(() -> new ClassNameAlreadyExist("Service not found with id: " + id));
        if (existing.getSchoolId() == null) {
            throw new ClassNameAlreadyExist("You don't have access to delete this service");
        }
        this.servicesRepository.delete(existing);
    }

    public List<Services> getAllUniversalServices() {
        return this.servicesRepository.findByUniversalTrue();
    }

    public List<Services> getServicesBySchoolId(String schoolId) {
        return this.servicesRepository.findBySchoolId(schoolId);
    }

    public List<Services> getUniversalAndSchoolServices(String schoolId) {
        return this.servicesRepository.findByUniversalTrueOrSchoolId(schoolId);
    }

    public List<Services> getServicesByTypeAndUniversal(String serviceType) {
        return this.servicesRepository.findByServiceTypeAndUniversalTrue(serviceType);
    }

    public List<Services> getServicesByTypeAndSchool(String serviceType, String schoolId) {
        return this.servicesRepository.findByServiceTypeAndSchoolId(serviceType, schoolId);
    }

    private void applyUpdates(Services existing, Services service) {
        if (service.getServiceType() != null) {
            existing.setServiceType(service.getServiceType());
        }
        if (service.getCharge() > 0.0) {
            existing.setCharge(service.getCharge());
        }
        if (service.getDescription() != null) {
            existing.setDescription(service.getDescription());
        }
        if (service.getSchoolId() != null) {
            existing.setSchoolId(service.getSchoolId());
        }
    }
}

