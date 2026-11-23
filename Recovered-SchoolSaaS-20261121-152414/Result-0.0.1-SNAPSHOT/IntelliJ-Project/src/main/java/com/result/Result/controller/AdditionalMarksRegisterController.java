package com.result.Result.controller;

import com.result.Result.model.AdditionalMarksRegister;
import com.result.Result.model.ResponseModel;
import com.result.Result.service.AdditionalMarksRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = {"*"})
@RequestMapping("/api/additional-marks-register")
public class AdditionalMarksRegisterController {
    @Autowired
    private AdditionalMarksRegisterService service;

    @PostMapping("/create")
    public ResponseEntity<ResponseModel> create(@RequestBody AdditionalMarksRegister payload) {
        return this.service.create(payload);
    }

    @GetMapping("/additionalMarksheetData")
    public ResponseEntity<ResponseModel> getAdditionalMarksheetData(
            @RequestParam String schoolId,
            @RequestParam String className,
            @RequestParam String section,
            @RequestParam String examType) {
        return this.service.getAdditionalMarksheetData(schoolId, className, section, examType);
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseModel> update(@RequestBody AdditionalMarksRegister payload) {
        return this.service.update(payload);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseModel> delete(
            @RequestParam String schoolId,
            @RequestParam String className,
            @RequestParam String section,
            @RequestParam String examType,
            @RequestParam String subject) {
        return this.service.delete(schoolId, className, section, examType, subject);
    }
}
