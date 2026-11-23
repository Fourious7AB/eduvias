/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.result.Result.model.ProjectMarks
 *  com.result.Result.repo.ProjectMarkRepository
 *  com.result.Result.service.ProjectMarksService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 */
package com.result.Result.service;

import com.result.Result.model.ProjectMarks;
import com.result.Result.repo.ProjectMarkRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectMarksService {
    @Autowired
    private ProjectMarkRepository projectMarkRepository;

    public ProjectMarks createProjectWithSchool(String schoolId, ProjectMarks projectMarks) {
        if (schoolId == null || schoolId.trim().isEmpty()) {
            throw new IllegalArgumentException("School ID cannot be null or empty");
        }
        if (projectMarks == null) {
            throw new IllegalArgumentException("Project marks cannot be null");
        }
        this.validateProjectMarks(projectMarks);
        System.out.println("yeah code maine kiya hu : ");
        List existingProjects = this.projectMarkRepository.findAllBySchoolId(schoolId);
        boolean isDuplicate = existingProjects.stream().anyMatch(existing -> existing.getProjectName().equalsIgnoreCase(projectMarks.getProjectName()) && existing.getSubjectType().equals(projectMarks.getSubjectType()));
        if (isDuplicate) {
            throw new IllegalArgumentException("A project with the same name and subject type already exists for this school");
        }
        projectMarks.setSchoolId(schoolId);
        return (ProjectMarks)this.projectMarkRepository.save((Object)projectMarks);
    }

    public List<ProjectMarks> allProject(String schoolId) {
        if (schoolId == null || schoolId.trim().isEmpty()) {
            throw new IllegalArgumentException("School ID cannot be null or empty");
        }
        return this.projectMarkRepository.findAllBySchoolId(schoolId);
    }

    public Optional<ProjectMarks> viewProjectById(String schoolId, String id) {
        if (schoolId == null || schoolId.trim().isEmpty()) {
            throw new IllegalArgumentException("School ID cannot be null or empty");
        }
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Project ID cannot be null or empty");
        }
        return this.projectMarkRepository.findByIdAndSchoolId(id, schoolId);
    }

    public Optional<ProjectMarks> updateProjectWithSchool(String schoolId, String id, ProjectMarks updateProject) {
        if (schoolId == null || schoolId.trim().isEmpty()) {
            throw new IllegalArgumentException("School ID cannot be null or empty");
        }
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Project ID cannot be null or empty");
        }
        if (updateProject == null) {
            throw new IllegalArgumentException("Update project cannot be null");
        }
        Optional existProject = this.projectMarkRepository.findByIdAndSchoolId(id, schoolId);
        if (existProject.isPresent()) {
            ProjectMarks updatedProjectMarks = (ProjectMarks)existProject.get();
            if (updateProject.getProjectName() != null && !updateProject.getProjectName().isEmpty() && !updateProject.getProjectName().equals(updatedProjectMarks.getProjectName())) {
                String subjectTypeToCheck = updateProject.getSubjectType() != null ? updateProject.getSubjectType() : updatedProjectMarks.getSubjectType();
                boolean isDuplicate = this.projectMarkRepository.findAllBySchoolId(schoolId).stream().filter(p -> !p.getId().equals(id)).anyMatch(existing -> existing.getProjectName().equalsIgnoreCase(updateProject.getProjectName()) && existing.getSubjectType().equals(subjectTypeToCheck));
                if (isDuplicate) {
                    throw new IllegalArgumentException("A project with the same name and subject type already exists for this school");
                }
            }
            updateProject.setSchoolId(schoolId);
            if (updateProject.getProjectMarks() != null) {
                updatedProjectMarks.setProjectMarks(updateProject.getProjectMarks());
            }
            if (updateProject.getProjectName() != null && !updateProject.getProjectName().isEmpty()) {
                updatedProjectMarks.setProjectName(updateProject.getProjectName());
            }
            if (updateProject.getSubjectType() != null) {
                updatedProjectMarks.setSubjectType(updateProject.getSubjectType());
            }
            this.projectMarkRepository.save((Object)updatedProjectMarks);
        }
        return existProject;
    }

    public Optional<ProjectMarks> deleteProjectBySchool(String schoolId, String id) {
        if (schoolId == null || schoolId.trim().isEmpty()) {
            throw new IllegalArgumentException("School ID cannot be null or empty");
        }
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Project ID cannot be null or empty");
        }
        Optional exist = this.projectMarkRepository.findByIdAndSchoolId(id, schoolId);
        if (exist.isPresent()) {
            this.projectMarkRepository.deleteById((Object)id);
        }
        return exist;
    }

    private void validateProjectMarks(ProjectMarks projectMarks) {
        if (projectMarks.getProjectName() == null || projectMarks.getProjectName().trim().isEmpty()) {
            throw new IllegalArgumentException("Project name cannot be null or empty");
        }
        if (projectMarks.getSubjectType() == null) {
            throw new IllegalArgumentException("Subject type cannot be null");
        }
        if (projectMarks.getProjectMarks() == null) {
            throw new IllegalArgumentException("Project marks cannot be null");
        }
        if (projectMarks.getProjectMarks() < 0.0) {
            throw new IllegalArgumentException("Project marks cannot be negative");
        }
    }
}

