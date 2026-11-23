package com.result.Result.controller;

import com.result.Result.model.ResponseModel;
import com.result.Result.model.ResultPublishDate;
import com.result.Result.service.ResultPublishDateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = {"*"})
@RequestMapping("/api/result-publish-date")
public class ResultPublishDateController {
    @Autowired
    private ResultPublishDateService service;

    @PostMapping("/create/{schoolId}")
    public ResponseEntity<ResponseModel> create(@PathVariable String schoolId, @RequestBody ResultPublishDate payload) {
        return this.service.create(schoolId, payload);
    }

    @GetMapping("/getAll/{schoolId}")
    public ResponseEntity<ResponseModel> getAll(@PathVariable String schoolId) {
        return this.service.getAll(schoolId);
    }

    @GetMapping("/search/{schoolId}")
    public ResponseEntity<ResponseModel> search(
            @PathVariable String schoolId,
            @RequestParam(required = false) String className,
            @RequestParam(required = false) String examType) {
        return this.service.search(schoolId, className, examType);
    }

    @PutMapping("/update/{schoolId}/{id}")
    public ResponseEntity<ResponseModel> update(
            @PathVariable String schoolId,
            @PathVariable String id,
            @RequestBody ResultPublishDate payload) {
        return this.service.update(schoolId, id, payload);
    }

    @DeleteMapping("/delete/{schoolId}/{id}")
    public ResponseEntity<ResponseModel> delete(@PathVariable String schoolId, @PathVariable String id) {
        return this.service.delete(schoolId, id);
    }
}
