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
        projectMarks.setProjectName(projectMarks.getProjectName().trim());
        projectMarks.setSubjectType(projectMarks.getSubjectType().trim());

        List<ProjectMarks> existingProjects = this.projectMarkRepository.findAllBySchoolId(schoolId);
        boolean isDuplicate = existingProjects.stream().anyMatch(existing ->
                existing.getProjectName() != null
                        && existing.getSubjectType() != null
                        && existing.getProjectName().trim().equalsIgnoreCase(projectMarks.getProjectName())
                        && existing.getSubjectType().trim().equalsIgnoreCase(projectMarks.getSubjectType()));
        if (isDuplicate) {
            throw new IllegalArgumentException("A project with the same name and subject type already exists for this school");
        }
        projectMarks.setSchoolId(schoolId);
        return this.projectMarkRepository.save(projectMarks);
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
        Optional<ProjectMarks> existProject = this.projectMarkRepository.findByIdAndSchoolId(id, schoolId);
        if (existProject.isPresent()) {
            ProjectMarks updatedProjectMarks = existProject.get();
            String nextName = (updateProject.getProjectName() != null && !updateProject.getProjectName().trim().isEmpty())
                    ? updateProject.getProjectName().trim()
                    : updatedProjectMarks.getProjectName();
            String nextSubjectType = (updateProject.getSubjectType() != null && !updateProject.getSubjectType().trim().isEmpty())
                    ? updateProject.getSubjectType().trim()
                    : updatedProjectMarks.getSubjectType();

            boolean isDuplicate = this.projectMarkRepository.findAllBySchoolId(schoolId).stream()
                    .filter(p -> p.getId() != null && !p.getId().equals(id))
                    .anyMatch(existing -> existing.getProjectName() != null
                            && existing.getSubjectType() != null
                            && nextName != null
                            && nextSubjectType != null
                            && existing.getProjectName().trim().equalsIgnoreCase(nextName)
                            && existing.getSubjectType().trim().equalsIgnoreCase(nextSubjectType));
            if (isDuplicate) {
                throw new IllegalArgumentException("A project with the same name and subject type already exists for this school");
            }

            if (updateProject.getProjectMarks() != null) {
                if (updateProject.getProjectMarks() < 0.0) {
                    throw new IllegalArgumentException("Project marks cannot be negative");
                }
                updatedProjectMarks.setProjectMarks(updateProject.getProjectMarks());
            }
            if (updateProject.getFullMarks() != null) {
                if (updateProject.getFullMarks() < 0.0) {
                    throw new IllegalArgumentException("Full marks cannot be negative");
                }
                updatedProjectMarks.setFullMarks(updateProject.getFullMarks());
            }
            updatedProjectMarks.setProjectName(nextName);
            updatedProjectMarks.setSubjectType(nextSubjectType);
            updatedProjectMarks.setSchoolId(schoolId);
            this.projectMarkRepository.save(updatedProjectMarks);
            return Optional.of(updatedProjectMarks);
        }
        return Optional.empty();
    }

    public Optional<ProjectMarks> deleteProjectBySchool(String schoolId, String id) {
        if (schoolId == null || schoolId.trim().isEmpty()) {
            throw new IllegalArgumentException("School ID cannot be null or empty");
        }
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Project ID cannot be null or empty");
        }
        Optional<ProjectMarks> exist = this.projectMarkRepository.findByIdAndSchoolId(id, schoolId);
        if (exist.isPresent()) {
            this.projectMarkRepository.deleteById(id);
        }
        return exist;
    }

    private void validateProjectMarks(ProjectMarks projectMarks) {
        if (projectMarks.getProjectName() == null || projectMarks.getProjectName().trim().isEmpty()) {
            throw new IllegalArgumentException("Project name cannot be null or empty");
        }
        if (projectMarks.getSubjectType() == null || projectMarks.getSubjectType().trim().isEmpty()) {
            throw new IllegalArgumentException("Subject type cannot be null or empty");
        }
        if (projectMarks.getProjectMarks() == null) {
            throw new IllegalArgumentException("Project marks cannot be null");
        }
        if (projectMarks.getProjectMarks() < 0.0) {
            throw new IllegalArgumentException("Project marks cannot be negative");
        }
        if (projectMarks.getFullMarks() != null && projectMarks.getFullMarks() < 0.0) {
            throw new IllegalArgumentException("Full marks cannot be negative");
        }
    }
}
