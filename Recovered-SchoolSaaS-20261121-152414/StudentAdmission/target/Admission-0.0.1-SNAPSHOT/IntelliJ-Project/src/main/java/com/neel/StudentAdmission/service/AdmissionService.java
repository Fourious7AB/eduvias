/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.StudentAdmission.model.Admission
 *  com.neel.StudentAdmission.model.AuthUser
 *  com.neel.StudentAdmission.model.School
 *  com.neel.StudentAdmission.repo.AdmissionRepository
 *  com.neel.StudentAdmission.repo.SchoolRepository
 *  com.neel.StudentAdmission.service.AdmissionService
 *  com.neel.StudentAdmission.service.IDCardGenerator
 *  org.springframework.beans.factory.annotation.Autowired
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

import com.neel.StudentAdmission.model.Admission;
import com.neel.StudentAdmission.model.AuthUser;
import com.neel.StudentAdmission.model.School;
import com.neel.StudentAdmission.repo.AdmissionRepository;
import com.neel.StudentAdmission.repo.SchoolRepository;
import com.neel.StudentAdmission.service.IDCardGenerator;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
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
public class AdmissionService {
    @Autowired
    private AdmissionRepository admissionRepository;
    @Autowired
    private SchoolRepository schoolRepository;
    private static final String TEMPLATE_PATH = "https://res.cloudinary.com/djz6jcjhk/image/upload/v1740642340/schoolidmain_mewapb.png";
    private static final String OUTPUT_DIR = "src/main/resources/output/";
    private static final String CLOUDINARY_URL = "https://api.cloudinary.com/v1_1/djz6jcjhk/image/upload";
    private static final String API_KEY = "495141318574428";
    private static final String API_SECRET = "WxXxl8AaFXdeuHSfYmg4QrKH4O8";

    public long getTotalStudentCount() {
        return this.admissionRepository.count();
    }

    public long getStudentCountBySchool(String schoolId) {
        return this.admissionRepository.findBySchoolId(schoolId).size();
    }

    private String generateAdmissionNumber(String schoolCode) {
        String year = String.valueOf(new Date().getYear() + 1900);
        long count = this.admissionRepository.count() + 1L;
        return schoolCode + year + String.format("%03d", count);
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

    public Admission createAdmission(Admission admission, String schoolId) {
        Optional school = this.schoolRepository.findBySchoolId(schoolId);
        if (school.isPresent()) {
            String admissionNumber = this.generateAdmissionNumber(((School)school.get()).getSchoolCode());
            admission.setAdmissionNumber(admissionNumber);
            admission.setSchoolId(schoolId);
            this.saveAuthUser(admission.getAdmissionNumber(), admission.getDob());
            return (Admission)this.admissionRepository.save((Object)admission);
        }
        throw new RuntimeException("School not found");
    }

    public Iterable<Admission> getAllAdmissions() {
        return this.admissionRepository.findAll();
    }

    public List<Admission> getAdmissionsBySchoolId(String schoolId) {
        return this.admissionRepository.findBySchoolId(schoolId);
    }

    public Optional<Admission> getAdmissionByNumber(String admissionNumber) {
        return this.admissionRepository.findByAdmissionNumber(admissionNumber);
    }

    public Optional<Admission> getAdmissionByNumberAndSchool(String admissionNumber, String schoolId) {
        return this.admissionRepository.findByAdmissionNumberAndSchoolId(admissionNumber, schoolId);
    }

    public Admission updateAdmissionByNumber(String admissionNumber, Admission updatedAdmission) {
        Optional admission = this.admissionRepository.findByAdmissionNumber(admissionNumber);
        if (admission.isPresent()) {
            updatedAdmission.setId(((Admission)admission.get()).getId());
            updatedAdmission.setAdmissionNumber(admissionNumber);
            return (Admission)this.admissionRepository.save((Object)updatedAdmission);
        }
        throw new RuntimeException("Admission not found with admission number: " + admissionNumber);
    }

    public Admission updateAdmissionByNumberAndSchool(String admissionNumber, String schoolId, Admission updatedAdmission) {
        Optional admission = this.admissionRepository.findByAdmissionNumberAndSchoolId(admissionNumber, schoolId);
        if (admission.isPresent()) {
            updatedAdmission.setId(((Admission)admission.get()).getId());
            updatedAdmission.setAdmissionNumber(admissionNumber);
            updatedAdmission.setSchoolId(schoolId);
            return (Admission)this.admissionRepository.save((Object)updatedAdmission);
        }
        throw new RuntimeException("Admission not found with admission number: " + admissionNumber + " for this school");
    }

    public void deleteAdmissionByNumber(String admissionNumber) {
        Optional admission = this.admissionRepository.findByAdmissionNumber(admissionNumber);
        if (!admission.isPresent()) {
            throw new RuntimeException("Admission not found with admission number: " + admissionNumber);
        }
        this.admissionRepository.delete((Object)((Admission)admission.get()));
    }

    public boolean deleteAdmissionByNumberAndSchool(String admissionNumber, String schoolId) {
        Optional admission = this.admissionRepository.findByAdmissionNumberAndSchoolId(admissionNumber, schoolId);
        if (admission.isPresent()) {
            this.admissionRepository.delete((Object)((Admission)admission.get()));
            return true;
        }
        return false;
    }

    public String generateAndSaveIDCard(String admissionNumber) {
        Admission admission = (Admission)this.admissionRepository.findByAdmissionNumber(admissionNumber).orElseThrow(() -> new RuntimeException("Admission not found for number: " + admissionNumber));
        School school = (School)this.schoolRepository.findById((Object)"67ca9167d56ec91fc9539234").orElseThrow(() -> new RuntimeException("School not found for ID: 67ca9167d56ec91fc9539234"));
        String outputPath = "src/main/resources/output/id_card_" + admissionNumber + ".png";
        this.generateIDCardForAdmission(admission, school, outputPath);
        admission.setIdCardPath(outputPath);
        this.admissionRepository.save((Object)admission);
        return outputPath;
    }

    public String generateAndSaveIDCardWithSchool(String admissionNumber, String schoolId) {
        try {
            File outputDir;
            School school;
            Admission admission = (Admission)this.admissionRepository.findByAdmissionNumberAndSchoolId(admissionNumber, schoolId).orElseThrow(() -> new RuntimeException("Admission not found for number: " + admissionNumber + " in this school"));
            if (admission.getStudentImage() == null || admission.getStudentImage().isEmpty()) {
                throw new RuntimeException("Student image is missing for admission: " + admissionNumber);
            }
            String studentImagePath = admission.getStudentImage();
            if (studentImagePath.toLowerCase().endsWith(".avif")) {
                String convertedUrl = studentImagePath.replace("/upload/", "/upload/f_jpg,q_auto/");
                System.out.println("Converting AVIF image to JPG format: " + convertedUrl);
                admission.setStudentImage(convertedUrl);
            }
            if ((school = (School)this.schoolRepository.findBySchoolId(schoolId).orElseThrow(() -> new RuntimeException("School not found for ID: " + schoolId))).getSchoolLogo() == null || school.getSchoolLogo().isEmpty()) {
                System.out.println("Warning: School logo is missing for school: " + schoolId);
            }
            if (!(outputDir = new File(OUTPUT_DIR)).exists()) {
                outputDir.mkdirs();
            }
            String outputPath = "src/main/resources/output/id_card_" + admissionNumber + "_" + schoolId + ".png";
            System.out.println("Generating ID card at path: " + outputPath);
            this.generateIDCardForAdmission(admission, school, outputPath);
            admission.setIdCardPath(outputPath);
            this.admissionRepository.save((Object)admission);
            return outputPath;
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error generating ID card: " + e.getMessage(), e);
        }
    }

    private void generateIDCardForAdmission(Admission admission, School school, String outputPath) {
        IDCardGenerator.generateIDCard((String)TEMPLATE_PATH, (String)outputPath, (Admission)admission, (School)school);
    }

    private void saveAuthUser(String admissionNumber, String dob) {
        String formattedDob = LocalDate.parse(dob).format(DateTimeFormatter.ofPattern("ddMMyyyy"));
        AuthUser authUser = new AuthUser();
        authUser.setUsername(admissionNumber);
        authUser.setPassword(formattedDob);
        authUser.setRole("student");
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForEntity("https://collage.vaisacademy.com/service4/api/auth/register", (Object)authUser, String.class, new Object[0]);
    }

    public Admission updateAdmissionStatus(String admissionNumber, String schoolId, String status) {
        Optional admissionOpt = this.admissionRepository.findByAdmissionNumberAndSchoolId(admissionNumber, schoolId);
        if (admissionOpt.isPresent()) {
            Admission admission = (Admission)admissionOpt.get();
            admission.setStatus(status);
            return (Admission)this.admissionRepository.save((Object)admission);
        }
        throw new RuntimeException("Admission not found with admission number: " + admissionNumber + " for this school");
    }
}

