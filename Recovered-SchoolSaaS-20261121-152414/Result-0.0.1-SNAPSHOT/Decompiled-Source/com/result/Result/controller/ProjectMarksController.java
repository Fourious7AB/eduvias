/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.result.Result.controller.ProjectMarksController
 *  com.result.Result.model.ProjectMarks
 *  com.result.Result.service.ProjectMarksService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.http.HttpStatus
 *  org.springframework.http.HttpStatusCode
 *  org.springframework.http.ResponseEntity
 *  org.springframework.web.bind.annotation.DeleteMapping
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.PutMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.result.Result.controller;

import com.result.Result.model.ProjectMarks;
import com.result.Result.service.ProjectMarksService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/api/schools/{schoolId}/projects"})
public class ProjectMarksController {
    @Autowired
    private ProjectMarksService projectMarksService;

    @PostMapping
    public ResponseEntity<ProjectMarks> createProject(@PathVariable String schoolId, @RequestBody ProjectMarks projectMarks) {
        try {
            ProjectMarks createdProject = this.projectMarksService.createProjectWithSchool(schoolId, projectMarks);
            return new ResponseEntity((Object)createdProject, (HttpStatusCode)HttpStatus.CREATED);
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity((HttpStatusCode)HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<ProjectMarks>> getAllProjects(@PathVariable String schoolId) {
        try {
            List projects = this.projectMarksService.allProject(schoolId);
            return new ResponseEntity((Object)projects, (HttpStatusCode)HttpStatus.OK);
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity((HttpStatusCode)HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value={"/{projectId}"})
    public ResponseEntity<ProjectMarks> getProjectById(@PathVariable String schoolId, @PathVariable String projectId) {
        try {
            Optional project = this.projectMarksService.viewProjectById(schoolId, projectId);
            return project.map(value -> new ResponseEntity(value, (HttpStatusCode)HttpStatus.OK)).orElseGet(() -> new ResponseEntity((HttpStatusCode)HttpStatus.NOT_FOUND));
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity((HttpStatusCode)HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value={"/{projectId}"})
    public ResponseEntity<ProjectMarks> updateProject(@PathVariable String schoolId, @PathVariable String projectId, @RequestBody ProjectMarks projectMarks) {
        try {
            Optional updatedProject = this.projectMarksService.updateProjectWithSchool(schoolId, projectId, projectMarks);
            return updatedProject.map(value -> new ResponseEntity(value, (HttpStatusCode)HttpStatus.OK)).orElseGet(() -> new ResponseEntity((HttpStatusCode)HttpStatus.NOT_FOUND));
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity((HttpStatusCode)HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value={"/{projectId}"})
    public ResponseEntity<Void> deleteProject(@PathVariable String schoolId, @PathVariable String projectId) {
        try {
            Optional deletedProject = this.projectMarksService.deleteProjectBySchool(schoolId, projectId);
            if (deletedProject.isPresent()) {
                return new ResponseEntity((HttpStatusCode)HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity((HttpStatusCode)HttpStatus.NOT_FOUND);
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity((HttpStatusCode)HttpStatus.BAD_REQUEST);
        }
    }
}

