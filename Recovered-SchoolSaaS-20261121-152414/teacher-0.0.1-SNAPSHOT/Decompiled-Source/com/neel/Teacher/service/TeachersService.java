/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.Teacher.model.AuthUser
 *  com.neel.Teacher.model.SubjectAssignment
 *  com.neel.Teacher.model.SubjectAssignmentDTO
 *  com.neel.Teacher.model.Teachers
 *  com.neel.Teacher.repo.TeachersRepository
 *  com.neel.Teacher.service.EmailService
 *  com.neel.Teacher.service.TeachersService
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
import com.neel.Teacher.model.SubjectAssignment;
import com.neel.Teacher.model.SubjectAssignmentDTO;
import com.neel.Teacher.model.Teachers;
import com.neel.Teacher.repo.TeachersRepository;
import com.neel.Teacher.service.EmailService;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
public class TeachersService {
    private final TeachersRepository teachersRepository;
    private final EmailService emailService;
    private static final String CLOUDINARY_URL = "https://api.cloudinary.com/v1_1/djz6jcjhk/image/upload";
    private static final String API_KEY = "495141318574428";
    private static final String API_SECRET = "WxXxl8AaFXdeuHSfYmg4QrKH4O8";
    @Value(value="${auth.register.url}")
    private String authUrl;
    @Value(value="${view.subscription.plan.url}")
    private String subscriptionPlanUrl;

    @Autowired
    public TeachersService(TeachersRepository teachersRepository, EmailService emailService) {
        this.teachersRepository = teachersRepository;
        this.emailService = emailService;
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
        return this.teachersRepository.existsByEmail(email);
    }

    public boolean isEmailExistsForSchool(String email, String schoolId) {
        return this.teachersRepository.existsByEmailAndSchoolId(email, schoolId);
    }

    public Teachers saveTeacher(Teachers teacher) {
        String teacherCode = "TEA-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        teacher.setTeachersCode(teacherCode);
        this.saveAuthUser(teacher.getEmail(), teacher.getJoiningDate());
        return (Teachers)this.teachersRepository.save((Object)teacher);
    }

    public Teachers updateTeacher(Teachers teacher) {
        return (Teachers)this.teachersRepository.save((Object)teacher);
    }

    public Teachers getTeacherById(String id, String schoolId) {
        Optional teacher = this.teachersRepository.findById((Object)id);
        if (teacher.isPresent() && ((Teachers)teacher.get()).getSchoolId().equals(schoolId)) {
            return (Teachers)teacher.get();
        }
        return null;
    }

    public Teachers getTeacherById(String id) {
        Optional teacher = this.teachersRepository.findById((Object)id);
        return teacher.orElse(null);
    }

    public List<Teachers> getAllTeachers() {
        return this.teachersRepository.findAll();
    }

    public List<Teachers> getTeachersBySchoolId(String schoolId) {
        return this.teachersRepository.findBySchoolId(schoolId);
    }

    public List<Teachers> getTeachersBySubject(String subject) {
        if (subject != null && !subject.isEmpty()) {
            return this.teachersRepository.findBySubject(subject);
        }
        return new ArrayList<Teachers>();
    }

    public List<Teachers> getTeachersBySubjectAndSchool(String subject, String schoolId) {
        if (subject != null && !subject.isEmpty()) {
            return this.teachersRepository.findBySubjectAndSchoolId(subject, schoolId);
        }
        return new ArrayList<Teachers>();
    }

    public Optional<Teachers> getTeacherByEmail(String email) {
        return this.teachersRepository.findByEmail(email);
    }

    public Optional<Teachers> getTeacherByEmailAndSchool(String email, String schoolId) {
        return this.teachersRepository.findByEmailAndSchoolId(email, schoolId);
    }

    public boolean deleteTeacher(String id, String schoolId) {
        Optional teacher = this.teachersRepository.findById((Object)id);
        if (teacher.isPresent() && ((Teachers)teacher.get()).getSchoolId().equals(schoolId)) {
            this.teachersRepository.delete((Object)((Teachers)teacher.get()));
            this.deleteAuthByUsername(((Teachers)teacher.get()).getEmail());
            return true;
        }
        return false;
    }

    public boolean deleteTeacher(String id) {
        Optional teacher = this.teachersRepository.findById((Object)id);
        if (teacher.isPresent()) {
            this.teachersRepository.delete((Object)((Teachers)teacher.get()));
            this.deleteAuthByUsername(((Teachers)teacher.get()).getEmail());
            return true;
        }
        return false;
    }

    public Teachers updateTeacher(String id, String schoolId, Teachers updatedTeacher) {
        Optional existingTeacher = this.teachersRepository.findById((Object)id);
        if (existingTeacher.isPresent() && ((Teachers)existingTeacher.get()).getSchoolId().equals(schoolId)) {
            Teachers teacher = (Teachers)existingTeacher.get();
            updatedTeacher.setId(id);
            updatedTeacher.setSchoolId(schoolId);
            updatedTeacher.setTeachersCode(teacher.getTeachersCode());
            return (Teachers)this.teachersRepository.save((Object)updatedTeacher);
        }
        return null;
    }

    private String saveAuthUser(String email, String joiningDate) {
        try {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String formattedDob = LocalDate.parse(joiningDate, inputFormatter).format(DateTimeFormatter.ofPattern("ddMMyyyy"));
            AuthUser authUser = new AuthUser();
            authUser.setUsername(email);
            authUser.setPassword(formattedDob);
            authUser.setRole("teacher");
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

    public Teachers addSubjectAssignment(String teacherId, String schoolId, SubjectAssignmentDTO assignmentDTO) {
        Optional teacherOpt = this.teachersRepository.findById((Object)teacherId);
        if (teacherOpt.isPresent() && ((Teachers)teacherOpt.get()).getSchoolId().equals(schoolId)) {
            Teachers teacher = (Teachers)teacherOpt.get();
            SubjectAssignment assignment = new SubjectAssignment();
            assignment.setSubjectName(assignmentDTO.getSubjectName());
            assignment.setDayOfWeek(assignmentDTO.getDayOfWeek());
            assignment.setTimeSlot(assignmentDTO.getTimeSlot());
            assignment.setClassGroup(assignmentDTO.getClassGroup());
            assignment.setAssignedDate(LocalDate.parse(assignmentDTO.getAssignedDate()));
            assignment.setStatus(assignmentDTO.getStatus() != null ? assignmentDTO.getStatus() : "Active");
            if (teacher.getSubjectAssignments() == null) {
                teacher.setSubjectAssignments(new ArrayList());
            }
            teacher.getSubjectAssignments().add(assignment);
            Teachers updatedTeacher = (Teachers)this.teachersRepository.save((Object)teacher);
            return updatedTeacher;
        }
        return null;
    }

    public List<SubjectAssignment> getTeacherSubjectAssignments(String teacherId, String schoolId) {
        Optional teacherOpt = this.teachersRepository.findById((Object)teacherId);
        if (teacherOpt.isPresent() && ((Teachers)teacherOpt.get()).getSchoolId().equals(schoolId)) {
            Teachers teacher = (Teachers)teacherOpt.get();
            return teacher.getSubjectAssignments();
        }
        return new ArrayList<SubjectAssignment>();
    }

    public Teachers updateSubjectAssignmentByName(String teacherId, String schoolId, String subjectName, SubjectAssignmentDTO assignmentDTO) {
        Optional teacherOpt = this.teachersRepository.findById((Object)teacherId);
        System.out.println("Teacher found: " + (teacherOpt.isPresent() ? "Yes" : "No"));
        if (teacherOpt.isPresent()) {
            Teachers teacher = (Teachers)teacherOpt.get();
            System.out.println("School ID match: " + (teacher.getSchoolId().equals(schoolId) ? "Yes" : "No"));
            System.out.println("Subject assignments null: " + (teacher.getSubjectAssignments() == null ? "Yes" : "No"));
            if (teacher.getSchoolId().equals(schoolId) && teacher.getSubjectAssignments() != null) {
                boolean found = false;
                System.out.println("Number of assignments: " + teacher.getSubjectAssignments().size());
                for (SubjectAssignment assignment : teacher.getSubjectAssignments()) {
                    System.out.println("Checking assignment: " + assignment.getSubjectName());
                    if (!assignment.getSubjectName().equals(subjectName)) continue;
                    System.out.println("Found matching assignment!");
                    assignment.setSubjectName(assignmentDTO.getSubjectName());
                    assignment.setDayOfWeek(assignmentDTO.getDayOfWeek());
                    assignment.setTimeSlot(assignmentDTO.getTimeSlot());
                    assignment.setClassGroup(assignmentDTO.getClassGroup());
                    if (assignmentDTO.getAssignedDate() != null) {
                        assignment.setAssignedDate(LocalDate.parse(assignmentDTO.getAssignedDate()));
                    }
                    if (assignmentDTO.getStatus() != null) {
                        assignment.setStatus(assignmentDTO.getStatus());
                    }
                    found = true;
                    break;
                }
                if (found) {
                    System.out.println("Saving updated teacher");
                    return (Teachers)this.teachersRepository.save((Object)teacher);
                }
                System.out.println("No matching assignment found for subject: " + subjectName);
            } else {
                System.out.println("School ID mismatch or subject assignments is null");
            }
        } else {
            System.out.println("Teacher not found with ID: " + teacherId);
        }
        return null;
    }

    public Teachers deleteSubjectAssignmentByName(String teacherId, String schoolId, String subjectName) {
        Teachers teacher;
        Optional teacherOpt = this.teachersRepository.findById((Object)teacherId);
        if (teacherOpt.isPresent() && ((Teachers)teacherOpt.get()).getSchoolId().equals(schoolId) && (teacher = (Teachers)teacherOpt.get()).getSubjectAssignments() != null) {
            for (int i = 0; i < teacher.getSubjectAssignments().size(); ++i) {
                if (!((SubjectAssignment)teacher.getSubjectAssignments().get(i)).getSubjectName().equals(subjectName)) continue;
                teacher.getSubjectAssignments().remove(i);
                return (Teachers)this.teachersRepository.save((Object)teacher);
            }
        }
        return null;
    }

    public Teachers addMultipleSubjectAssignments(String teacherId, String schoolId, List<SubjectAssignmentDTO> assignmentDTOs) {
        Optional teacherOpt = this.teachersRepository.findById((Object)teacherId);
        if (teacherOpt.isPresent() && ((Teachers)teacherOpt.get()).getSchoolId().equals(schoolId)) {
            Teachers teacher = (Teachers)teacherOpt.get();
            if (teacher.getSubjectAssignments() == null) {
                teacher.setSubjectAssignments(new ArrayList());
            }
            String[][] assignmentDetails = new String[assignmentDTOs.size()][4];
            int index = 0;
            for (SubjectAssignmentDTO dto : assignmentDTOs) {
                SubjectAssignment assignment = new SubjectAssignment();
                assignment.setSubjectName(dto.getSubjectName());
                assignment.setDayOfWeek(dto.getDayOfWeek());
                assignment.setTimeSlot(dto.getTimeSlot());
                assignment.setClassGroup(dto.getClassGroup());
                if (dto.getAssignedDate() != null) {
                    try {
                        assignment.setAssignedDate(LocalDate.parse(dto.getAssignedDate()));
                    }
                    catch (Exception e) {
                        System.out.println("Error parsing date: " + e.getMessage());
                        assignment.setAssignedDate(LocalDate.now());
                    }
                } else {
                    assignment.setAssignedDate(LocalDate.now());
                }
                assignment.setStatus(dto.getStatus() != null ? dto.getStatus() : "Active");
                teacher.getSubjectAssignments().add(assignment);
                assignmentDetails[index][0] = assignment.getSubjectName();
                assignmentDetails[index][1] = assignment.getDayOfWeek();
                assignmentDetails[index][2] = assignment.getTimeSlot();
                assignmentDetails[index][3] = assignment.getClassGroup();
                ++index;
            }
            Teachers updatedTeacher = (Teachers)this.teachersRepository.save((Object)teacher);
            return updatedTeacher;
        }
        return null;
    }

    public Optional<Teachers> getTeacherByIdAndSchoolId(String id, String schoolId) {
        return this.teachersRepository.findByIdAndSchoolId(id, schoolId);
    }

    public long countTeacherBySchool(String schoolId) {
        List allTeacher = this.teachersRepository.findBySchoolId(schoolId);
        return allTeacher.size();
    }

    private String deleteAuthByUsername(String username) {
        String url = this.authUrl + "/api/auth/delete/" + username;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity response = restTemplate.exchange(url, HttpMethod.DELETE, null, String.class, new Object[0]);
        return (String)response.getBody();
    }

    private void viewPlanBySpecificSchool(String schoolId) {
        String viewSubsPlanUrl = this.subscriptionPlanUrl + schoolId;
        System.out.println(" i m from plan url : " + viewSubsPlanUrl);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.exchange(viewSubsPlanUrl, HttpMethod.GET, null, String.class, new Object[0]);
    }
}

