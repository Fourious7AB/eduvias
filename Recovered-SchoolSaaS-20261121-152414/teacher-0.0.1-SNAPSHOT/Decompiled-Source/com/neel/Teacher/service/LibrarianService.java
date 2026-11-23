/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.Teacher.model.AuthUser
 *  com.neel.Teacher.model.Librarian
 *  com.neel.Teacher.repo.LibrarianRepo
 *  com.neel.Teacher.service.LibrarianService
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
import com.neel.Teacher.model.Librarian;
import com.neel.Teacher.repo.LibrarianRepo;
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
public class LibrarianService {
    private final LibrarianRepo librarianRepo;
    private static final String CLOUDINARY_URL = "https://api.cloudinary.com/v1_1/djz6jcjhk/image/upload";
    private static final String API_KEY = "495141318574428";
    private static final String API_SECRET = "WxXxl8AaFXdeuHSfYmg4QrKH4O8";
    @Value(value="${auth.register.url}")
    private String authUrl;

    @Autowired
    public LibrarianService(LibrarianRepo librarianRepo) {
        this.librarianRepo = librarianRepo;
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
        return this.librarianRepo.existsByEmail(email);
    }

    public boolean isEmailExistsForSchool(String email, String schoolId) {
        return this.librarianRepo.existsByEmailAndSchoolId(email, schoolId);
    }

    public Librarian saveTeacher(Librarian librarian) {
        String librarianCode = "LIB-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        librarian.setLibrarianCode(librarianCode);
        this.saveAuthUser(librarian.getEmail(), librarian.getJoiningDate());
        return (Librarian)this.librarianRepo.save((Object)librarian);
    }

    public Librarian getTeacherById(String id, String schoolId) {
        Optional librarian = this.librarianRepo.findById((Object)id);
        if (librarian.isPresent() && ((Librarian)librarian.get()).getSchoolId().equals(schoolId)) {
            return (Librarian)librarian.get();
        }
        return null;
    }

    public Librarian getTeacherById(String id) {
        Optional librarian = this.librarianRepo.findById((Object)id);
        return librarian.orElse(null);
    }

    public List<Librarian> getAllTeachers() {
        return this.librarianRepo.findAll();
    }

    public List<Librarian> getTeachersBySchoolId(String schoolId) {
        return this.librarianRepo.findBySchoolId(schoolId);
    }

    public Optional<Librarian> getLibrarianByEmail(String email) {
        return this.librarianRepo.findByEmail(email);
    }

    public Optional<Librarian> getLibrarianByEmailAndSchool(String email, String schoolId) {
        return this.librarianRepo.findByEmailAndSchoolId(email, schoolId);
    }

    public boolean deleteTeacher(String id, String schoolId) {
        Optional librarian = this.librarianRepo.findById((Object)id);
        if (librarian.isPresent() && ((Librarian)librarian.get()).getSchoolId().equals(schoolId)) {
            this.librarianRepo.delete((Object)((Librarian)librarian.get()));
            this.deleteAuthByUsername(((Librarian)librarian.get()).getEmail());
            return true;
        }
        return false;
    }

    public boolean deleteTeacher(String id) {
        Optional librarian = this.librarianRepo.findById((Object)id);
        if (librarian.isPresent()) {
            this.librarianRepo.delete((Object)((Librarian)librarian.get()));
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

    public Librarian updateLibrarian(Librarian updatedLibrarian) {
        return (Librarian)this.librarianRepo.save((Object)updatedLibrarian);
    }

    private String saveAuthUser(String email, String joiningDate) {
        try {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String formattedDob = LocalDate.parse(joiningDate, inputFormatter).format(DateTimeFormatter.ofPattern("ddMMyyyy"));
            AuthUser authUser = new AuthUser();
            authUser.setUsername(email);
            authUser.setPassword(formattedDob);
            authUser.setRole("librarian");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity request = new HttpEntity((Object)authUser, (MultiValueMap)headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity response = restTemplate.postForEntity(this.authUrl + "/api/auth/register", (Object)request, String.class, new Object[0]);
            System.out.println("Auth user created successfully via production: " + (String)response.getBody());
            return (String)response.getBody();
        }
        catch (HttpClientErrorException e) {
            System.err.println("Error creating auth user: " + e.getMessage());
            e.printStackTrace();
            return "Auth user creation failed: " + e.getMessage();
        }
    }

    public Optional<Librarian> getByLibrarianIdAndSchoolId(String id, String schoolId) {
        return this.librarianRepo.findByIdAndSchoolId(id, schoolId);
    }
}

