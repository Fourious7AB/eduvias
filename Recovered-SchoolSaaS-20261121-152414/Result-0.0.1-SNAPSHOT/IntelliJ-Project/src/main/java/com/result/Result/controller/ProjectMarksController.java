package com.result.Result.controller;

import com.result.Result.model.ProjectMarks;
import com.result.Result.model.ResponseMessage;
import com.result.Result.service.ProjectMarksService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins={"*"})
@RequestMapping(value={"/api/schools/{schoolId}/projects"})
public class ProjectMarksController {
    @Autowired
    private ProjectMarksService projectMarksService;

    @PostMapping
    public ResponseEntity<?> createProject(@PathVariable String schoolId, @RequestBody ProjectMarks projectMarks) {
        try {
            ProjectMarks createdProject = this.projectMarksService.createProjectWithSchool(schoolId, projectMarks);
            return new ResponseEntity<>(createdProject, (HttpStatusCode) HttpStatus.CREATED);
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ResponseMessage(e.getMessage(), null), (HttpStatusCode) HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllProjects(@PathVariable String schoolId) {
        try {
            List<ProjectMarks> projects = this.projectMarksService.allProject(schoolId);
            return new ResponseEntity<>(projects, (HttpStatusCode) HttpStatus.OK);
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ResponseMessage(e.getMessage(), null), (HttpStatusCode) HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value={"/{projectId}"})
    public ResponseEntity<?> getProjectById(@PathVariable String schoolId, @PathVariable String projectId) {
        try {
            Optional<ProjectMarks> project = this.projectMarksService.viewProjectById(schoolId, projectId);
            return project.<ResponseEntity<?>>map(ResponseEntity::ok)
                    .orElseGet(() -> new ResponseEntity<>(new ResponseMessage("Project not found", null), (HttpStatusCode) HttpStatus.NOT_FOUND));
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ResponseMessage(e.getMessage(), null), (HttpStatusCode) HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value={"/{projectId}"})
    public ResponseEntity<?> updateProject(@PathVariable String schoolId, @PathVariable String projectId, @RequestBody ProjectMarks projectMarks) {
        try {
            Optional<ProjectMarks> updatedProject = this.projectMarksService.updateProjectWithSchool(schoolId, projectId, projectMarks);
            return updatedProject.<ResponseEntity<?>>map(ResponseEntity::ok)
                    .orElseGet(() -> new ResponseEntity<>(new ResponseMessage("Project not found", null), (HttpStatusCode) HttpStatus.NOT_FOUND));
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ResponseMessage(e.getMessage(), null), (HttpStatusCode) HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value={"/{projectId}"})
    public ResponseEntity<?> deleteProject(@PathVariable String schoolId, @PathVariable String projectId) {
        try {
            Optional<ProjectMarks> deletedProject = this.projectMarksService.deleteProjectBySchool(schoolId, projectId);
            if (deletedProject.isPresent()) {
                return new ResponseEntity<>((HttpStatusCode) HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(new ResponseMessage("Project not found", null), (HttpStatusCode) HttpStatus.NOT_FOUND);
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ResponseMessage(e.getMessage(), null), (HttpStatusCode) HttpStatus.BAD_REQUEST);
        }
    }
}
