/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.StudentAdmission.model.Admission
 *  com.neel.StudentAdmission.model.EntityType
 *  com.neel.StudentAdmission.model.LookUp
 *  com.neel.StudentAdmission.model.School
 *  com.neel.StudentAdmission.service.AdmissionService
 *  com.neel.StudentAdmission.service.LookupService
 *  com.neel.StudentAdmission.service.SchoolService
 *  lombok.Generated
 *  org.springframework.stereotype.Service
 */
package com.neel.StudentAdmission.service;

import com.neel.StudentAdmission.model.Admission;
import com.neel.StudentAdmission.model.EntityType;
import com.neel.StudentAdmission.model.LookUp;
import com.neel.StudentAdmission.model.School;
import com.neel.StudentAdmission.service.AdmissionService;
import com.neel.StudentAdmission.service.SchoolService;
import lombok.Generated;
import org.springframework.stereotype.Service;

@Service
public class LookupService {
    private final AdmissionService admissionService;
    private final SchoolService schoolService;

    public LookUp findEntityById(String id, String schoolId) {
        Admission admission = this.admissionService.getStudentByIdAndSchoolId(id, schoolId);
        if (admission != null) {
            return LookUp.found((EntityType)EntityType.STUDENT, (Object)admission);
        }
        School school = this.schoolService.getSchoolBySchoolId(id, schoolId);
        if (school != null) {
            return LookUp.found((EntityType)EntityType.ADMIN, (Object)school);
        }
        return LookUp.notFound();
    }

    @Generated
    public LookupService(AdmissionService admissionService, SchoolService schoolService) {
        this.admissionService = admissionService;
        this.schoolService = schoolService;
    }
}

