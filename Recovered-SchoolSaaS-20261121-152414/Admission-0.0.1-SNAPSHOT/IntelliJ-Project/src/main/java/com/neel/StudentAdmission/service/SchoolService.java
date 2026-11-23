/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.StudentAdmission.model.School
 *  com.neel.StudentAdmission.repo.SchoolRepository
 *  com.neel.StudentAdmission.service.SchoolService
 *  org.springframework.http.HttpEntity
 *  org.springframework.http.HttpHeaders
 *  org.springframework.http.HttpMethod
 *  org.springframework.http.MediaType
 *  org.springframework.http.ResponseEntity
 *  org.springframework.stereotype.Service
 *  org.springframework.util.LinkedMultiValueMap
 *  org.springframework.util.MultiValueMap
 *  org.springframework.web.client.RestTemplate
 *  org.springframework.web.multipart.MultipartFile
 */
package com.neel.StudentAdmission.service;

import com.neel.StudentAdmission.model.School;
import com.neel.StudentAdmission.repo.SchoolRepository;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
public class SchoolService {
    private final SchoolRepository schoolRepository;
    private static final String CLOUDINARY_URL = "https://api.cloudinary.com/v1_1/djz6jcjhk/image/upload";
    private static final String API_KEY = "495141318574428";
    private static final String API_SECRET = "WxXxl8AaFXdeuHSfYmg4QrKH4O8";

    public SchoolService(SchoolRepository schoolRepository) {
        this.schoolRepository = schoolRepository;
    }

    public String saveFile(MultipartFile file) throws IOException {
        try {
            RestTemplate restTemplate = new RestTemplate();
            LinkedMultiValueMap body = new LinkedMultiValueMap();
            body.add((Object)"file", (Object)file.getResource());
            body.add((Object)"upload_preset", (Object)"Teacher_image");
            body.add((Object)API_KEY, (Object)API_SECRET);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            HttpEntity requestEntity = new HttpEntity((Object)body, (MultiValueMap)headers);
            ResponseEntity responseEntity = restTemplate.exchange(CLOUDINARY_URL, HttpMethod.POST, requestEntity, Map.class, new Object[0]);
            Map responseBody = (Map)responseEntity.getBody();
            if (responseBody != null) {
                return (String)responseBody.get("secure_url");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public School saveSchool(School school) {
        return (School)this.schoolRepository.save(school);
    }

    public Optional<School> toggleBlockedSchool(String schoolId) {
        Optional existSchool = this.schoolRepository.findBySchoolId(schoolId);
        if (existSchool.isPresent()) {
            School updateSchool = (School)existSchool.get();
            String currentStatus = updateSchool.getStatus();
            if ("BLOCKED".equalsIgnoreCase(currentStatus)) {
                updateSchool.setStatus("UNBLOCKED");
            } else {
                updateSchool.setStatus("BLOCKED");
            }
            this.schoolRepository.save(updateSchool);
        }
        return existSchool;
    }

    public School getSchoolById(String id) {
        Optional<School> school = this.schoolRepository.findById(id);
        return school.orElse(null);
    }

    public Optional<School> getBySchoolId(String schoolId) {
        return this.schoolRepository.findBySchoolId(schoolId);
    }

    public School getSchoolBySchoolId(String id, String schoolId) {
        Optional school = this.schoolRepository.findBySchoolId(id);
        if (school.isPresent() && ((School)school.get()).getSchoolId().equals(schoolId)) {
            return (School)school.get();
        }
        return null;
    }

    public Long getSchoolCount() {
        return this.schoolRepository.count();
    }

    public List<School> getAllSchools() {
        return this.schoolRepository.findAll();
    }

    public Optional<School> getBySchoolEmail(String schoolEmail) {
        return this.schoolRepository.findBySchoolEmail(schoolEmail);
    }

    public Optional<School> getBySchoolPrincipalEmail(String schoolPrincipalEmail) {
        return this.schoolRepository.findBySchoolPrincipalEmail(schoolPrincipalEmail);
    }

    public boolean deleteSchool(String id) {
        Optional school = this.schoolRepository.findById(id);
        if (school.isPresent()) {
            this.schoolRepository.delete(((School)school.get()));
            return true;
        }
        return false;
    }

    public Optional<School> getSchool(String schoolId) {
        return this.schoolRepository.findBySchoolId(schoolId);
    }
}

