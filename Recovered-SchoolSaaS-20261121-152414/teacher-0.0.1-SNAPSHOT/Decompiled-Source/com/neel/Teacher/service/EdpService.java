/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.Teacher.model.AuthUser
 *  com.neel.Teacher.model.Edp
 *  com.neel.Teacher.repo.EdpRepo
 *  com.neel.Teacher.service.EdpService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.beans.factory.annotation.Value
 *  org.springframework.http.HttpEntity
 *  org.springframework.http.HttpHeaders
 *  org.springframework.http.HttpMethod
 *  org.springframework.http.MediaType
 *  org.springframework.http.ResponseEntity
 *  org.springframework.stereotype.Service
 *  org.springframework.util.LinkedMultiValueMap
 *  org.springframework.util.MultiValueMap
 *  org.springframework.web.client.HttpClientErrorException
 *  org.springframework.web.client.RestTemplate
 *  org.springframework.web.multipart.MultipartFile
 */
package com.neel.Teacher.service;

import com.neel.Teacher.model.AuthUser;
import com.neel.Teacher.model.Edp;
import com.neel.Teacher.repo.EdpRepo;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
public class EdpService {
    private final EdpRepo edpRepo;
    private static final String CLOUDINARY_URL = "https://api.cloudinary.com/v1_1/djz6jcjhk/image/upload";
    private static final String API_KEY = "495141318574428";
    private static final String API_SECRET = "WxXxl8AaFXdeuHSfYmg4QrKH4O8";
    @Value(value="${auth.register.url}")
    private String authUrl;

    @Autowired
    public EdpService(EdpRepo edpRepo) {
        this.edpRepo = edpRepo;
    }

    public Long getAllEdpCount() {
        return this.edpRepo.count();
    }

    public String saveFile(MultipartFile file) throws HttpClientErrorException {
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

    public boolean isEmailExists(String email) {
        return this.edpRepo.existsByEmail(email);
    }

    public boolean isEmailExistsForSchool(String email, String schoolId) {
        return this.edpRepo.existsByEmailAndSchoolId(email, schoolId);
    }

    public Edp saveTeacher(Edp teacher) {
        String teacherCode = "EDP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        teacher.setEdpCode(teacherCode);
        this.saveAuthUser(teacher.getEmail(), teacher.getJoiningDate());
        return (Edp)this.edpRepo.save((Object)teacher);
    }

    public Edp getTeacherById(String id, String schoolId) {
        Optional teacher = this.edpRepo.findById((Object)id);
        if (teacher.isPresent() && ((Edp)teacher.get()).getSchoolId().equals(schoolId)) {
            return (Edp)teacher.get();
        }
        return null;
    }

    public Edp getTeacherById(String id) {
        Optional teacher = this.edpRepo.findById((Object)id);
        return teacher.orElse(null);
    }

    public List<Edp> getAllTeachers() {
        return this.edpRepo.findAll();
    }

    public long countEdpBySchool(String schoolId) {
        List count = this.edpRepo.findBySchoolId(schoolId);
        return count.size();
    }

    public List<Edp> getTeachersBySchoolId(String schoolId) {
        return this.edpRepo.findBySchoolId(schoolId);
    }

    public boolean deleteTeacher(String id, String schoolId) {
        Optional teacher = this.edpRepo.findById((Object)id);
        if (teacher.isPresent() && ((Edp)teacher.get()).getSchoolId().equals(schoolId)) {
            this.edpRepo.delete((Object)((Edp)teacher.get()));
            this.deleteAuthByUsername(((Edp)teacher.get()).getEmail());
            return true;
        }
        return false;
    }

    private String deleteAuthByUsername(String username) {
        String url = this.authUrl + "/api/auth/delete/" + username;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity response = restTemplate.exchange(url, HttpMethod.DELETE, null, String.class, new Object[0]);
        return (String)response.getBody();
    }

    public boolean deleteTeacher(String id) {
        Optional teacher = this.edpRepo.findById((Object)id);
        if (teacher.isPresent()) {
            this.edpRepo.delete((Object)((Edp)teacher.get()));
            return true;
        }
        return false;
    }

    private String saveAuthUser(String email, String joiningDate) {
        try {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String formattedDob = LocalDate.parse(joiningDate, inputFormatter).format(DateTimeFormatter.ofPattern("ddMMyyyy"));
            AuthUser authUser = new AuthUser();
            authUser.setUsername(email);
            authUser.setPassword(formattedDob);
            authUser.setRole("edp");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity request = new HttpEntity((Object)authUser, (MultiValueMap)headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity response = restTemplate.postForEntity(this.authUrl + "/api/auth/register", (Object)request, String.class, new Object[0]);
            System.out.println("Auth user created successfully via production: " + (String)response.getBody());
            return (String)response.getBody();
        }
        catch (Exception e) {
            System.err.println("Error creating auth user: " + e.getMessage());
            e.printStackTrace();
            return "Auth user creation failed: " + e.getMessage();
        }
    }

    public Optional<Edp> getEdpByEmail(String email) {
        return this.edpRepo.findByEmail(email);
    }

    public Optional<Edp> getEdpByEmailAndSchool(String email, String schoolId) {
        return this.edpRepo.findByEmailAndSchoolId(email, schoolId);
    }

    public Edp updateTeacher(String id, String schoolId, Edp updatedTeacher) {
        Optional existingTeacher = this.edpRepo.findById((Object)id);
        if (existingTeacher.isPresent() && ((Edp)existingTeacher.get()).getSchoolId().equals(schoolId)) {
            Edp teacher = (Edp)existingTeacher.get();
            updatedTeacher.setId(id);
            updatedTeacher.setSchoolId(schoolId);
            updatedTeacher.setEdpCode(teacher.getEdpCode());
            return (Edp)this.edpRepo.save((Object)updatedTeacher);
        }
        return null;
    }
}

