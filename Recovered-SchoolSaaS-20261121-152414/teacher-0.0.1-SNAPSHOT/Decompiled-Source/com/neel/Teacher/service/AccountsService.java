/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.Teacher.model.Accounts
 *  com.neel.Teacher.model.AuthUser
 *  com.neel.Teacher.repo.AccountRepository
 *  com.neel.Teacher.service.AccountsService
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
 *  org.springframework.web.client.RestTemplate
 *  org.springframework.web.multipart.MultipartFile
 */
package com.neel.Teacher.service;

import com.neel.Teacher.model.Accounts;
import com.neel.Teacher.model.AuthUser;
import com.neel.Teacher.repo.AccountRepository;
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
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AccountsService {
    private final AccountRepository accountsRepo;
    private static final String CLOUDINARY_URL = "https://api.cloudinary.com/v1_1/djz6jcjhk/image/upload";
    private static final String API_KEY = "495141318574428";
    private static final String API_SECRET = "WxXxl8AaFXdeuHSfYmg4QrKH4O8";
    @Value(value="${auth.register.url}")
    private String authUrl;

    @Autowired
    public AccountsService(AccountRepository accountsRepo) {
        this.accountsRepo = accountsRepo;
    }

    public Long getAllAccountsCount() {
        return this.accountsRepo.count();
    }

    public String saveFile(MultipartFile file) {
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
        return this.accountsRepo.existsByEmail(email);
    }

    public boolean isEmailExistsForSchool(String email, String schoolId) {
        return this.accountsRepo.existsByEmailAndSchoolId(email, schoolId);
    }

    public Accounts saveAccount(Accounts account) {
        String accountCode = "ACC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        account.setAccCode(accountCode);
        this.saveAuthUser(account.getEmail(), account.getJoiningDate());
        return (Accounts)this.accountsRepo.save((Object)account);
    }

    public Accounts getAccountById(String id, String schoolId) {
        Optional account = this.accountsRepo.findById((Object)id);
        if (account.isPresent() && ((Accounts)account.get()).getSchoolId().equals(schoolId)) {
            return (Accounts)account.get();
        }
        return null;
    }

    public Accounts getAccountById(String id) {
        return this.accountsRepo.findById((Object)id).orElse(null);
    }

    public List<Accounts> getAllAccounts() {
        return this.accountsRepo.findAll();
    }

    public long countAccountsBySchool(String schoolId) {
        return this.accountsRepo.findBySchoolId(schoolId).size();
    }

    public List<Accounts> getAccountsBySchoolId(String schoolId) {
        return this.accountsRepo.findBySchoolId(schoolId);
    }

    public boolean deleteAccount(String id, String schoolId) {
        Optional account = this.accountsRepo.findById((Object)id);
        if (account.isPresent() && ((Accounts)account.get()).getSchoolId().equals(schoolId)) {
            this.accountsRepo.delete((Object)((Accounts)account.get()));
            this.deleteAuthByUsername(((Accounts)account.get()).getEmail());
            return true;
        }
        return false;
    }

    public boolean deleteAccount(String id) {
        Optional account = this.accountsRepo.findById((Object)id);
        if (account.isPresent()) {
            this.accountsRepo.delete((Object)((Accounts)account.get()));
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

    private String saveAuthUser(String email, String joiningDate) {
        try {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            System.out.println("Joining Date: " + joiningDate);
            LocalDate jd = LocalDate.parse(joiningDate, inputFormatter);
            System.out.println("i m from jd: " + jd);
            String formattedDob = jd.format(DateTimeFormatter.ofPattern("ddMMyyyy"));
            AuthUser authUser = new AuthUser();
            authUser.setUsername(email);
            authUser.setPassword(formattedDob);
            authUser.setRole("accountant");
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

    public Optional<Accounts> getAccountByEmail(String email) {
        return this.accountsRepo.findByEmail(email);
    }

    public Optional<Accounts> getAccountByEmailAndSchool(String email, String schoolId) {
        return this.accountsRepo.findByEmailAndSchoolId(email, schoolId);
    }

    public Accounts updateAccount(String id, String schoolId, Accounts updatedAccount) {
        Optional existing = this.accountsRepo.findById((Object)id);
        if (existing.isPresent() && ((Accounts)existing.get()).getSchoolId().equals(schoolId)) {
            Accounts acc = (Accounts)existing.get();
            updatedAccount.setId(id);
            updatedAccount.setSchoolId(schoolId);
            updatedAccount.setAccCode(acc.getAccCode());
            return (Accounts)this.accountsRepo.save((Object)updatedAccount);
        }
        return null;
    }
}

