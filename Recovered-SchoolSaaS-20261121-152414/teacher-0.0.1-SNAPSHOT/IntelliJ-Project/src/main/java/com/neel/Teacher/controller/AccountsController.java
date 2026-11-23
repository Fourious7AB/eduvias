/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.Teacher.controller.AccountsController
 *  com.neel.Teacher.dto.AccountsDto
 *  com.neel.Teacher.model.Accounts
 *  com.neel.Teacher.model.ResponseMessage
 *  com.neel.Teacher.model.ResponseModel
 *  com.neel.Teacher.repo.LibrarianRepo
 *  com.neel.Teacher.repo.TeachersRepository
 *  com.neel.Teacher.service.AccountsService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.beans.factory.annotation.Value
 *  org.springframework.http.HttpStatus
 *  org.springframework.http.HttpStatusCode
 *  org.springframework.http.ResponseEntity
 *  org.springframework.web.bind.annotation.CrossOrigin
 *  org.springframework.web.bind.annotation.DeleteMapping
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.ModelAttribute
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.PutMapping
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 *  org.springframework.web.client.HttpClientErrorException
 */
package com.neel.Teacher.controller;

import com.neel.Teacher.dto.AccountsDto;
import com.neel.Teacher.model.Accounts;
import com.neel.Teacher.model.ResponseMessage;
import com.neel.Teacher.model.ResponseModel;
import com.neel.Teacher.repo.LibrarianRepo;
import com.neel.Teacher.repo.TeachersRepository;
import com.neel.Teacher.service.AccountsService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

@RestController
@CrossOrigin(origins={"*"})
@RequestMapping(value={"/api"})
public class AccountsController {
    private final AccountsService accountsService;
    private final TeachersRepository teachersRepository;
    private final LibrarianRepo librarianRepo;
    @Value(value="${view.subscription.plan.url}")
    private String subscriptionPlanUrl;

    @Autowired
    public AccountsController(AccountsService accountsService, TeachersRepository teachersRepository, LibrarianRepo librarianRepo) {
        this.accountsService = accountsService;
        this.teachersRepository = teachersRepository;
        this.librarianRepo = librarianRepo;
    }

    @GetMapping(value={"/accounts"})
    public ResponseEntity<ResponseMessage<List<Accounts>>> getAllAccounts() {
        List accounts = this.accountsService.getAllAccounts();
        return ResponseEntity.ok(new ResponseMessage("Accounts retrieved successfully!", accounts));
    }

    @GetMapping(value={"/counts/all"})
    public ResponseEntity<ResponseMessage<Map<String, Long>>> getAllCounts() {
        HashMap<String, Long> counts = new HashMap<String, Long>();
        counts.put("totalAccountants", this.accountsService.getAllAccountsCount());
        counts.put("totalTeachers", this.teachersRepository.count());
        counts.put("totalLibrarians", this.librarianRepo.count());
        return ResponseEntity.ok(new ResponseMessage("All counts retrieved successfully!", counts));
    }

    @GetMapping(value={"/accounts/{schoolId}"})
    public ResponseEntity<ResponseMessage<List<Accounts>>> getAccountsBySchoolId(@PathVariable String schoolId) {
        List accounts = this.accountsService.getAccountsBySchoolId(schoolId);
        if (accounts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage("No accounts found for this school!", null));
        }
        return ResponseEntity.ok(new ResponseMessage("Accounts retrieved successfully!", accounts));
    }

    @PostMapping(value={"/accounts/{schoolId}"})
    public ResponseEntity<ResponseMessage<Accounts>> addAccount(@PathVariable String schoolId, @ModelAttribute AccountsDto accountsDto) {
        try {
            accountsDto.setSchoolId(schoolId);
            if (this.accountsService.isEmailExistsForSchool(accountsDto.getEmail(), schoolId)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseMessage("Email already exists for this school!", null));
            }
            String filePath = this.accountsService.saveFile(accountsDto.getAccImage());
            Accounts account = new Accounts();
            account.setSchoolId(schoolId);
            account.setAccName(accountsDto.getAccName());
            account.setAccImage(filePath);
            account.setEmail(accountsDto.getEmail());
            account.setGender(accountsDto.getGender());
            account.setBloodGroup(accountsDto.getBloodGroup());
            account.setReligion(accountsDto.getReligion());
            account.setPhone(accountsDto.getPhone());
            account.setMaritialStatus(accountsDto.getMaritialStatus());
            account.setJoiningDate(accountsDto.getJoiningDate());
            account.setPinCode(accountsDto.getPinCode());
            account.setState(accountsDto.getState());
            account.setCity(accountsDto.getCity());
            account.setDistrict(accountsDto.getDistrict());
            account.setCountry(accountsDto.getCountry());
            account.setPoliceStation(accountsDto.getPoliceStation());
            account.setVillPost(accountsDto.getVillPost());
            Accounts saved = this.accountsService.saveAccount(account);
            return ResponseEntity.ok(new ResponseMessage("Account added successfully!", saved));
        }
        catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new ResponseMessage(e.getResponseBodyAsString(), null));
        }
    }

    @GetMapping(value={"/accounts/email/{email}/{schoolId}"})
    public ResponseEntity<ResponseModel> getAccountByEmailAndSchool(@PathVariable String email, @PathVariable String schoolId) {
        Optional<Accounts> account = this.accountsService.getAccountByEmailAndSchool(email, schoolId);
        return account.map(accounts -> ResponseEntity.ok(new ResponseModel("Account retrieved successfully", accounts))).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseModel("Account not found with email: " + email + " for this school", null)));
    }

    @GetMapping(value={"/accounts/email/{email}"})
    public ResponseModel getAccountByEmail(@PathVariable String email) {
        Optional<Accounts> account = this.accountsService.getAccountByEmail(email);
        return account.map(value -> new ResponseModel("Account retrieved successfully", value)).orElseGet(() -> new ResponseModel("Account not found with email: " + email, null));
    }

    @GetMapping(value={"/accounts/id/{id}/{schoolId}"})
    public ResponseEntity<ResponseMessage<Accounts>> getAccountByIdAndSchool(@PathVariable String id, @PathVariable String schoolId) {
        Accounts account = this.accountsService.getAccountById(id, schoolId);
        if (account != null) {
            return ResponseEntity.ok(new ResponseMessage("Account details found!", account));
        }
        return ResponseEntity.status((int)404).body(new ResponseMessage("Account not found for this school!", null));
    }

    @GetMapping(value={"/accounts/id/{id}"})
    public ResponseEntity<ResponseMessage<Accounts>> getAccountById(@PathVariable String id) {
        Accounts account = this.accountsService.getAccountById(id);
        if (account != null) {
            return ResponseEntity.ok(new ResponseMessage("Account details found!", account));
        }
        return ResponseEntity.status((int)404).body(new ResponseMessage("Account not found!", null));
    }

    @PutMapping(value={"/accounts/{id}/{schoolId}"})
    public ResponseEntity<ResponseMessage<Accounts>> updateAccountWithSchool(@PathVariable String schoolId, @PathVariable String id, @ModelAttribute AccountsDto accountsDto) {
        Accounts existing = this.accountsService.getAccountById(id, schoolId);
        if (existing == null) {
            return ResponseEntity.status((int)404).body(new ResponseMessage("Account not found!", null));
        }
        if (!existing.getEmail().equals(accountsDto.getEmail()) && this.accountsService.isEmailExistsForSchool(accountsDto.getEmail(), schoolId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseMessage("Email already exists for another account!", null));
        }
        Accounts account = new Accounts();
        account.setId(id);
        account.setSchoolId(schoolId);
        account.setAccCode(existing.getAccCode());
        account.setAccName(accountsDto.getAccName());
        account.setEmail(accountsDto.getEmail());
        account.setGender(accountsDto.getGender());
        account.setBloodGroup(accountsDto.getBloodGroup());
        account.setReligion(accountsDto.getReligion());
        account.setPhone(accountsDto.getPhone());
        account.setMaritialStatus(accountsDto.getMaritialStatus());
        account.setJoiningDate(accountsDto.getJoiningDate());
        account.setPinCode(accountsDto.getPinCode());
        account.setState(accountsDto.getState());
        account.setCity(accountsDto.getCity());
        account.setDistrict(accountsDto.getDistrict());
        account.setCountry(accountsDto.getCountry());
        account.setPoliceStation(accountsDto.getPoliceStation());
        account.setVillPost(accountsDto.getVillPost());
        if (accountsDto.getAccImage() != null && !accountsDto.getAccImage().isEmpty()) {
            String filePath = this.accountsService.saveFile(accountsDto.getAccImage());
            account.setAccImage(filePath);
        } else {
            account.setAccImage(existing.getAccImage());
        }
        Accounts updated = this.accountsService.updateAccount(id, schoolId, account);
        return ResponseEntity.ok(new ResponseMessage("Account updated successfully!", updated));
    }

    @PutMapping(value={"/accounts/{id}"})
    public ResponseEntity<ResponseMessage<Accounts>> updateAccount(@PathVariable String id, @ModelAttribute AccountsDto accountsDto) {
        Accounts existing = this.accountsService.getAccountById(id);
        if (existing != null) {
            existing.setAccName(accountsDto.getAccName());
            existing.setEmail(accountsDto.getEmail());
            existing.setGender(accountsDto.getGender());
            existing.setBloodGroup(accountsDto.getBloodGroup());
            existing.setReligion(accountsDto.getReligion());
            existing.setPhone(accountsDto.getPhone());
            existing.setMaritialStatus(accountsDto.getMaritialStatus());
            existing.setJoiningDate(accountsDto.getJoiningDate());
            existing.setPinCode(accountsDto.getPinCode());
            existing.setState(accountsDto.getState());
            existing.setCity(accountsDto.getCity());
            existing.setDistrict(accountsDto.getDistrict());
            existing.setCountry(accountsDto.getCountry());
            existing.setPoliceStation(accountsDto.getPoliceStation());
            existing.setVillPost(accountsDto.getVillPost());
            if (accountsDto.getAccImage() != null && !accountsDto.getAccImage().isEmpty()) {
                String filePath = this.accountsService.saveFile(accountsDto.getAccImage());
                existing.setAccImage(filePath);
            }
            Accounts updated = this.accountsService.saveAccount(existing);
            return ResponseEntity.ok(new ResponseMessage("Account updated successfully!", updated));
        }
        return ResponseEntity.status((int)404).body(new ResponseMessage("Account not found!", null));
    }

    @DeleteMapping(value={"/accounts/{id}/{schoolId}"})
    public ResponseEntity<ResponseMessage<Void>> deleteAccountWithSchool(@PathVariable String id, @PathVariable String schoolId) {
        boolean deleted = this.accountsService.deleteAccount(id, schoolId);
        if (deleted) {
            return ResponseEntity.ok(new ResponseMessage("Account deleted successfully!", null));
        }
        return ResponseEntity.status((int)404).body(new ResponseMessage("Account not found!", null));
    }

    @DeleteMapping(value={"/accounts/{id}"})
    public ResponseEntity<ResponseMessage<Void>> deleteAccount(@PathVariable String id) {
        boolean deleted = this.accountsService.deleteAccount(id);
        if (deleted) {
            return ResponseEntity.ok(new ResponseMessage("Account deleted successfully!", null));
        }
        return ResponseEntity.status((int)404).body(new ResponseMessage("Account not found!", null));
    }
}

