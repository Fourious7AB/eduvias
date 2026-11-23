/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.StudentAdmission.exception.ClassNotFoundInAcademicServiceException
 *  com.neel.StudentAdmission.model.Admission
 *  com.neel.StudentAdmission.model.AdmissionHistory
 *  com.neel.StudentAdmission.model.AuthUser
 *  com.neel.StudentAdmission.model.ResponseMessage
 *  com.neel.StudentAdmission.model.School
 *  com.neel.StudentAdmission.model.Services
 *  com.neel.StudentAdmission.repo.AdmissionHistoryRepository
 *  com.neel.StudentAdmission.repo.AdmissionRepository
 *  com.neel.StudentAdmission.repo.SchoolRepository
 *  com.neel.StudentAdmission.service.AdmissionService
 *  com.neel.StudentAdmission.service.AdmissionService$1
 *  com.neel.StudentAdmission.service.IDCardGenerator
 *  org.apache.poi.ss.usermodel.Cell
 *  org.apache.poi.ss.usermodel.DateUtil
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.beans.factory.annotation.Value
 *  org.springframework.http.HttpEntity
 *  org.springframework.http.HttpHeaders
 *  org.springframework.http.HttpMethod
 *  org.springframework.http.HttpStatus
 *  org.springframework.http.MediaType
 *  org.springframework.http.ResponseEntity
 *  org.springframework.stereotype.Service
 *  org.springframework.util.LinkedMultiValueMap
 *  org.springframework.util.MultiValueMap
 *  org.springframework.web.client.HttpClientErrorException
 *  org.springframework.web.client.HttpServerErrorException
 *  org.springframework.web.client.RestTemplate
 *  org.springframework.web.multipart.MultipartFile
 */
package com.neel.StudentAdmission.service;

import com.neel.StudentAdmission.exception.ClassNotFoundInAcademicServiceException;
import com.neel.StudentAdmission.model.Admission;
import com.neel.StudentAdmission.model.AdmissionHistory;
import com.neel.StudentAdmission.model.AuthUser;
import com.neel.StudentAdmission.model.ResponseMessage;
import com.neel.StudentAdmission.model.School;
import com.neel.StudentAdmission.model.Services;
import com.neel.StudentAdmission.repo.AdmissionHistoryRepository;
import com.neel.StudentAdmission.repo.AdmissionRepository;
import com.neel.StudentAdmission.repo.SchoolRepository;
import com.neel.StudentAdmission.service.AdmissionService;
import com.neel.StudentAdmission.service.IDCardGenerator;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.invoke.CallSite;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AdmissionService {
    @Autowired
    private AdmissionRepository admissionRepository;
    @Autowired
    private SchoolRepository schoolRepository;
    @Autowired
    private AdmissionHistoryRepository admissionHistoryRepository;
    @Value(value="${admission.promote.fee.url}")
    private String promoteFeeUrl;
    @Value(value="${admission.single.promote.fee.url}")
    private String singlePromoteFeeUrl;
    @Value(value="${admission.fee.check.url}")
    private String feeCheckUrl;
    @Value(value="${add.service.fee.url}")
    private String addServiceFeeUrl;
    @Value(value="${update.fee.based.on.update.admission}")
    private String updateAdmissionFeeDetailsBasedOnAdmissionUpdate;
    @Value(value="${auth.register.url:http://localhost:8092/api/auth/register}")
    private String authRegisterUrl;
    private static final String TEMPLATE_PATH = "https://res.cloudinary.com/djz6jcjhk/image/upload/v1740642340/schoolidmain_mewapb.png";
    private static final String OUTPUT_DIR = "src/main/resources/output/";
    private static final String CLOUDINARY_URL = "https://api.cloudinary.com/v1_1/djz6jcjhk/image/upload";
    private static final String API_KEY = "495141318574428";
    private static final String API_SECRET = "WxXxl8AaFXdeuHSfYmg4QrKH4O8";
    private static final String BULK_ID_CARDS_ZIP_DIR = "bulk_id_cards/";

    public String generateAndSaveBulkIDCardsByClass(String className, String section, String schoolId, String academicYear) {
        try {
            File zipDir;
            File outputDir = new File(OUTPUT_DIR);
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }
            if (!(zipDir = new File(BULK_ID_CARDS_ZIP_DIR)).exists()) {
                zipDir.mkdirs();
            }
            School school = (School)this.schoolRepository.findBySchoolId(schoolId).orElseThrow(() -> new RuntimeException("School not found for ID: " + schoolId));
            List<Admission> students = academicYear != null && !academicYear.isEmpty() ? this.admissionRepository.findByClassNameIgnoreCaseAndSectionIgnoreCaseAndSchoolIdAndAcademicYear(className, section, schoolId, academicYear) : this.admissionRepository.findByClassNameIgnoreCaseAndSectionIgnoreCaseAndSchoolId(className, section, schoolId);
            if (students.isEmpty()) {
                throw new RuntimeException("No students found in class " + className + " section " + section);
            }
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String zipFileName = "id_cards_" + schoolId + "_" + className + "_" + section + "_" + timestamp + ".zip";
            String zipFilePath = BULK_ID_CARDS_ZIP_DIR + zipFileName;
            ArrayList<String> generatedCardPaths = new ArrayList<String>();
            for (Admission student : students) {
                try {
                    if (student.getStudentImage() == null || student.getStudentImage().isEmpty()) {
                        System.out.println("Warning: Student image missing for " + student.getAdmissionNumber() + ", skipping...");
                        continue;
                    }
                    String outputPath = "src/main/resources/output/id_card_" + schoolId + "_" + student.getAdmissionNumber() + ".jpg";
                    IDCardGenerator.generateIDCard((String)TEMPLATE_PATH, (String)outputPath, (Admission)student, (School)school);
                    student.setIdCardPath(outputPath);
                    this.admissionRepository.save(student);
                    generatedCardPaths.add(outputPath);
                }
                catch (Exception e) {
                    System.err.println("Error generating ID card for " + student.getAdmissionNumber() + ": " + e.getMessage());
                }
            }
            if (generatedCardPaths.isEmpty()) {
                throw new RuntimeException("No ID cards could be generated. Check if students have images.");
            }
            try (FileOutputStream fos = new FileOutputStream(zipFilePath);
                 ZipOutputStream zos = new ZipOutputStream(fos);){
                for (String string : generatedCardPaths) {
                    File cardFile = new File(string);
                    if (!cardFile.exists()) continue;
                    ZipEntry zipEntry = new ZipEntry(cardFile.getName());
                    zos.putNextEntry(zipEntry);
                    try (FileInputStream fis = new FileInputStream(cardFile);){
                        int length;
                        byte[] buffer = new byte[1024];
                        while ((length = fis.read(buffer)) > 0) {
                            zos.write(buffer, 0, length);
                        }
                    }
                    zos.closeEntry();
                }
            }
            return zipFilePath;
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error generating bulk ID cards: " + e.getMessage(), e);
        }
    }

    public long getTotalStudentCount() {
        return this.admissionRepository.count();
    }

    public long getStudentCountBySchool(String schoolId) {
        return this.admissionRepository.findBySchoolId(schoolId).size();
    }

    protected String generateAdmissionNumber(String schoolCode) {
        String year = String.valueOf(new Date().getYear() + 1900);
        long count = this.admissionRepository.count() + 1L;
        return schoolCode + year + String.format("%03d", count);
    }

    public String saveFile(MultipartFile file) throws HttpClientErrorException {
        try {
            if (file == null || file.isEmpty()) {
                System.out.println("File is empty or null, returning default image");
                return "https://res.cloudinary.com/djz6jcjhk/image/upload/v1740642340/default_profile_image.png";
            }
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

    public Admission createAdmission(Admission admission, String schoolId) {
        Optional school = this.schoolRepository.findBySchoolId(schoolId);
        if (school.isPresent()) {
            String admissionNumber = this.generateAdmissionNumber(((School)school.get()).getSchoolCode());
            admission.setAdmissionNumber(admissionNumber);
            admission.setSchoolId(schoolId);
            this.saveAuthUser(admission.getAdmissionNumber(), admission.getDob());
            return (Admission)this.admissionRepository.save(admission);
        }
        throw new ClassNotFoundInAcademicServiceException("School not found");
    }

    private String saveAuthUser(String admissionNumber, String dob) {
        String formattedDob = LocalDate.parse(dob).format(DateTimeFormatter.ofPattern("ddMMyyyy"));
        AuthUser authUser = new AuthUser();
        authUser.setUsername(admissionNumber);
        authUser.setPassword(formattedDob);
        authUser.setRole("student");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity request = new HttpEntity((Object)authUser, (MultiValueMap)headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity response = restTemplate.postForEntity(this.authRegisterUrl, (Object)request, String.class, new Object[0]);
        System.out.println("Response: " + (String)response.getBody());
        return (String)response.getBody();
    }

    public Admission updateAdmission(Admission admission, String schoolId) {
        Optional school = this.admissionRepository.findByAdmissionNumberAndSchoolId(admission.getAdmissionNumber(), schoolId);
        if (school.isPresent()) {
            return (Admission)this.admissionRepository.save(admission);
        }
        return null;
    }

    public Iterable<Admission> getAllAdmissions() {
        return this.admissionRepository.findAll();
    }

    public List<Admission> getAdmissionsBySchoolId(String schoolId) {
        return this.admissionRepository.findBySchoolId(schoolId);
    }

    public Optional<Admission> getAdmissionByNumber(String admissionNumber) {
        return this.admissionRepository.findByAdmissionNumber(admissionNumber);
    }

    public Optional<Admission> viewAlreadyExistAdmission(String rollNo, String className, String section, String schoolId) {
        return this.admissionRepository.findByRollNoAndClassNameIgnoreCaseAndSectionIgnoreCaseAndSchoolId(rollNo, className, section, schoolId);
    }

    public Optional<Admission> getAdmissionDetailsBySession(String academicYear, String schoolId, String email) {
        return this.admissionRepository.findByAcademicYearAndEmailAndSchoolId(academicYear, schoolId, email);
    }

    public Optional<Admission> getAllAdmissionBySchoolIdAndStudentId(String id, String schoolId) {
        return this.admissionRepository.findByIdAndSchoolId(id, schoolId);
    }

    public Admission getStudentByIdAndSchoolId(String id, String schoolId) {
        Optional admission = this.admissionRepository.findById(id);
        if (admission.isPresent() && ((Admission)admission.get()).getSchoolId().equals(schoolId)) {
            return (Admission)admission.get();
        }
        return null;
    }

    public Optional<Admission> getAdmissionByNumberAndSchool(String admissionNumber, String schoolId) {
        return this.admissionRepository.findByAdmissionNumberAndSchoolId(admissionNumber, schoolId);
    }

    public Admission updateAdmissionByNumber(String admissionNumber, Admission updatedAdmission) {
        Optional admission = this.admissionRepository.findByAdmissionNumber(admissionNumber);
        if (admission.isPresent()) {
            updatedAdmission.setId(((Admission)admission.get()).getId());
            updatedAdmission.setAdmissionNumber(admissionNumber);
            return (Admission)this.admissionRepository.save(updatedAdmission);
        }
        throw new RuntimeException("Admission not found with admission number: " + admissionNumber);
    }

    public Admission updateAdmissionByNumberAndSchool(String admissionNumber, String schoolId, Admission updatedAdmission) {
        Optional admission = this.admissionRepository.findByAdmissionNumberAndSchoolId(admissionNumber, schoolId);
        if (admission.isPresent()) {
            updatedAdmission.setId(((Admission)admission.get()).getId());
            updatedAdmission.setAdmissionNumber(admissionNumber);
            updatedAdmission.setSchoolId(schoolId);
            return (Admission)this.admissionRepository.save(updatedAdmission);
        }
        throw new RuntimeException("Admission not found with admission number: " + admissionNumber + " for this school");
    }

    public void deleteAdmissionByNumber(String admissionNumber) {
        Optional admission = this.admissionRepository.findByAdmissionNumber(admissionNumber);
        if (!admission.isPresent()) {
            throw new RuntimeException("Admission not found with admission number: " + admissionNumber);
        }
        this.admissionRepository.delete(((Admission)admission.get()));
    }

    public boolean deleteAdmissionByNumberAndSchool(String admissionNumber, String schoolId) {
        Optional admission = this.admissionRepository.findByAdmissionNumberAndSchoolId(admissionNumber, schoolId);
        if (admission.isPresent()) {
            this.admissionRepository.delete(((Admission)admission.get()));
            return true;
        }
        return false;
    }

    public String generateAndSaveIDCard(String admissionNumber) {
        Admission admission = (Admission)this.admissionRepository.findByAdmissionNumber(admissionNumber).orElseThrow(() -> new RuntimeException("Admission not found for number: " + admissionNumber));
        School school = (School)this.schoolRepository.findById("67ca9167d56ec91fc9539234").orElseThrow(() -> new RuntimeException("School not found for ID: 67ca9167d56ec91fc9539234"));
        String outputPath = "src/main/resources/output/id_card_" + admissionNumber + ".png";
        this.generateIDCardForAdmission(admission, school, outputPath);
        admission.setIdCardPath(outputPath);
        this.admissionRepository.save(admission);
        return outputPath;
    }

    public List<Admission> getAdmissionsByClassNameAndSection(String className, String section) {
        return this.admissionRepository.findByClassNameIgnoreCaseAndSectionIgnoreCase(className, section);
    }

    public String generateAndSaveIDCardWithSchool(String admissionNumber, String schoolId) {
        try {
            School school;
            Admission admission = (Admission)this.admissionRepository.findByAdmissionNumberAndSchoolId(admissionNumber, schoolId).orElseThrow(() -> new RuntimeException("Admission not found for number: " + admissionNumber + " in this school"));
            if (admission.getStudentImage() == null || admission.getStudentImage().isEmpty()) {
                throw new RuntimeException("Student image is missing for admission: " + admissionNumber);
            }
            String studentImagePath = admission.getStudentImage();
            if (studentImagePath.toLowerCase().endsWith(".avif")) {
                String convertedUrl = studentImagePath.replace("/upload/", "/upload/f_jpg,q_auto/");
                System.out.println("Converting AVIF image to JPG format: " + convertedUrl);
                admission.setStudentImage(convertedUrl);
            }
            if ((school = (School)this.schoolRepository.findBySchoolId(schoolId).orElseThrow(() -> new RuntimeException("School not found for ID: " + schoolId))).getSchoolLogo() == null || school.getSchoolLogo().isEmpty()) {
                throw new RuntimeException("Warning: School logo is missing for school: " + schoolId);
            }
            File outputDir = new File(OUTPUT_DIR);
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }
            String outputPath = "src/main/resources/output/id_card_" + admissionNumber + "_" + schoolId + ".png";
            System.out.println("Generating ID card at path: " + outputPath);
            this.generateIDCardForAdmission(admission, school, outputPath);
            admission.setIdCardPath(outputPath);
            this.admissionRepository.save(admission);
            return outputPath;
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error generating ID card: " + e.getMessage(), e);
        }
    }

    private void generateIDCardForAdmission(Admission admission, School school, String outputPath) {
        IDCardGenerator.generateIDCard((String)TEMPLATE_PATH, (String)outputPath, (Admission)admission, (School)school);
    }

    public Admission updateAdmissionStatus(String admissionNumber, String schoolId, String status) {
        Optional admissionOpt = this.admissionRepository.findByAdmissionNumberAndSchoolId(admissionNumber, schoolId);
        if (admissionOpt.isPresent()) {
            Admission admission = (Admission)admissionOpt.get();
            admission.setStatus(status);
            return (Admission)this.admissionRepository.save(admission);
        }
        throw new RuntimeException("Admission not found with admission number: " + admissionNumber + " for this school");
    }

    public Admission viewById(String _id) {
        return (Admission)this.admissionRepository.findById(_id).orElseThrow(() -> new RuntimeException("Admission not found for ID: " + _id));
    }

    public Admission addServicesToStudent(String admissionNumber, String schoolId, List<Services> services) {
        Optional admissionOpt = this.admissionRepository.findByAdmissionNumberAndSchoolId(admissionNumber, schoolId);
        if (admissionOpt.isPresent()) {
            Admission admission = (Admission)admissionOpt.get();
            if (admission.getServices() == null) {
                admission.setServices(new ArrayList());
            }
            List<String> existingServiceTypes = admission.getServices().stream().map(Services::getServiceType).toList();
            List<Services> uniqueServices = services.stream().filter(service -> !existingServiceTypes.contains(service.getServiceType())).toList();
            if (uniqueServices.isEmpty()) {
                throw new RuntimeException("All services already exist for this student");
            }
            admission.getServices().addAll(uniqueServices);
            this.addServiceFeeInAccount(admissionNumber, schoolId, services);
            return (Admission)this.admissionRepository.save(admission);
        }
        throw new RuntimeException("Admission not found with admission number: " + admissionNumber + " for this school");
    }

    private void addServiceFeeInAccount(String admissionNumber, String schoolId, List<Services> services) {
        try {
            String url = this.addServiceFeeUrl + admissionNumber + "/" + schoolId;
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity requestEntity = new HttpEntity(services, (MultiValueMap)headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String.class, new Object[0]);
            System.out.println("Response: " + (String)response.getBody());
        }
        catch (HttpClientErrorException | HttpServerErrorException ex) {
            System.out.println("Error from service: " + ex.getResponseBodyAsString());
        }
    }

    public List<Services> getStudentServices(String admissionNumber, String schoolId) {
        Optional admissionOpt = this.admissionRepository.findByAdmissionNumberAndSchoolId(admissionNumber, schoolId);
        if (admissionOpt.isPresent()) {
            Admission admission = (Admission)admissionOpt.get();
            return admission.getServices() != null ? admission.getServices() : new ArrayList();
        }
        throw new RuntimeException("Admission not found with admission number: " + admissionNumber + " for this school");
    }

    public Admission removeServiceFromStudent(String admissionNumber, String schoolId, String serviceType) {
        Optional admissionOpt = this.admissionRepository.findByAdmissionNumberAndSchoolId(admissionNumber, schoolId);
        if (admissionOpt.isPresent()) {
            Admission admission = (Admission)admissionOpt.get();
            if (admission.getServices() != null) {
                admission.getServices().removeIf(service -> service.getServiceType().equals(serviceType));
                return (Admission)this.admissionRepository.save(admission);
            }
            throw new RuntimeException("No services found for this student");
        }
        throw new RuntimeException("Admission not found with admission number: " + admissionNumber + " for this school");
    }

    public Optional<Admission> getAdmissionByNumberAndSession(String admissionNumber, String academicYear) {
        return this.admissionRepository.findByAdmissionNumberAndAcademicYear(admissionNumber, academicYear);
    }

    public Admission promoteStudent(String admissionNumber, String schoolId, String newClassName, String newSection, String newSession, boolean includePreviousDue) {
        Optional admissionOpt = this.admissionRepository.findByAdmissionNumberAndSchoolId(admissionNumber, schoolId);
        if (admissionOpt.isPresent()) {
            Admission currentAdmission = (Admission)admissionOpt.get();
            boolean hasPendingDues = this.checkStudentPendingDues(admissionNumber, schoolId);
            if (hasPendingDues && !includePreviousDue) {
                throw new RuntimeException("Student has pending dues and cannot be promoted. Please clear dues or select 'Include Previous Due' option.");
            }
            boolean isSameClass = currentAdmission.getClassName().equals(newClassName);
            if (!isSameClass) {
                AdmissionHistory history = new AdmissionHistory();
                history.setAdmissionNumber(currentAdmission.getAdmissionNumber());
                history.setSchoolId(currentAdmission.getSchoolId());
                history.setStudentName(currentAdmission.getStudentName());
                history.setRollNo(currentAdmission.getRollNo());
                history.setClassName(currentAdmission.getClassName());
                history.setSection(currentAdmission.getSection());
                history.setAcademicYear(currentAdmission.getAcademicYear());
                history.setStatus(currentAdmission.getStatus());
                history.setPromotedDate(LocalDate.now().toString());
                history.setPreviousClassName(currentAdmission.getClassName());
                history.setPreviousSection(currentAdmission.getSection());
                history.setPreviousAcademicYear(currentAdmission.getAcademicYear());
                history.setPromotionType("PROMOTE");
                this.admissionHistoryRepository.save(history);
            }
            try {
                ResponseMessage feeResponse = this.promotedStudentFee(admissionNumber, newClassName, newSection, newSession, includePreviousDue, schoolId);
                System.out.println("Fee update response: " + feeResponse);
            }
            catch (Exception e) {
                System.err.println("Error updating fees: " + e.getMessage());
            }
            currentAdmission.setClassName(newClassName);
            if (newSection != null && !newSection.isEmpty()) {
                currentAdmission.setSection(newSection);
            }
            currentAdmission.setAcademicYear(newSession);
            return (Admission)this.admissionRepository.save(currentAdmission);
        }
        throw new RuntimeException("Admission not found with admission number: " + admissionNumber + " for this school");
    }

    public List<Map<String, Object>> getStudentAcademicHistory(String admissionNumber, String schoolId) {
        try {
            List<Admission> studentRecords = this.admissionRepository.findAllByAdmissionNumberAndSchoolId(admissionNumber, schoolId);
            if (studentRecords.isEmpty()) {
                throw new RuntimeException("Student not found with admission number: " + admissionNumber);
            }
            Admission currentAdmission = studentRecords.stream().filter(record -> record.getAcademicYear() != null).max(Comparator.comparing(Admission::getAcademicYear)).orElse((Admission)studentRecords.get(0));
            ArrayList<Map<String, Object>> academicHistory = new ArrayList<Map<String, Object>>();
            HashMap<String, Object> currentSession = new HashMap<String, Object>();
            currentSession.put("session", currentAdmission.getAcademicYear());
            currentSession.put("className", currentAdmission.getClassName());
            currentSession.put("section", currentAdmission.getSection());
            currentSession.put("status", currentAdmission.getStatus());
            currentSession.put("isCurrent", true);
            academicHistory.add(currentSession);
            for (Admission record2 : studentRecords) {
                if (record2.getId().equals(currentAdmission.getId()) || record2.getAcademicYear() == null) continue;
                HashMap<String, Object> historicalSession = new HashMap<String, Object>();
                historicalSession.put("session", record2.getAcademicYear());
                historicalSession.put("className", record2.getClassName());
                historicalSession.put("section", record2.getSection());
                historicalSession.put("status", record2.getStatus());
                historicalSession.put("isCurrent", false);
                academicHistory.add(historicalSession);
            }
            academicHistory.sort((a, b) -> ((String)b.get("session")).compareTo((String)a.get("session")));
            return academicHistory;
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving academic history: " + e.getMessage());
        }
    }

    public ResponseMessage promotedStudentFee(String admissionNumber, String newClassName, String newSection, String newAcademicYear, boolean includePreviousDue, String schoolId) {
        String url = this.promoteFeeUrl + schoolId;
        HashMap<String, Object> requestBody = new HashMap<String, Object>();
        requestBody.put("admissionNumber", admissionNumber);
        requestBody.put("newClassName", newClassName);
        requestBody.put("newSection", newSection);
        requestBody.put("academicYear", newAcademicYear);
        requestBody.put("includePreviousDue", includePreviousDue);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity requestEntity = new HttpEntity(requestBody, (MultiValueMap)headers);
        ResponseEntity response = restTemplate.postForEntity(url, (Object)requestEntity, ResponseMessage.class, new Object[0]);
        return (ResponseMessage)response.getBody();
    }

    public ResponseMessage singlePromotedStudentFee(String admissionNumber, String newClassName, String newSection, String academicYear, boolean includePreviousDue, String schoolId) {
        String url = this.singlePromoteFeeUrl + schoolId;
        HashMap<String, Object> requestBody = new HashMap<String, Object>();
        requestBody.put("admissionNumber", admissionNumber);
        requestBody.put("newClassName", newClassName);
        requestBody.put("newSection", newSection);
        requestBody.put("academicYear", academicYear);
        requestBody.put("includePreviousDue", includePreviousDue);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity requestEntity = new HttpEntity(requestBody, (MultiValueMap)headers);
        ResponseEntity response = restTemplate.postForEntity(url, (Object)requestEntity, ResponseMessage.class, new Object[0]);
        return (ResponseMessage)response.getBody();
    }

    public List<AdmissionHistory> getStudentPromotionHistory(String admissionNumber, String schoolId) {
        return this.admissionHistoryRepository.findByAdmissionNumberAndSchoolId(admissionNumber, schoolId);
    }

    public List<AdmissionHistory> getStudentPromotionHistoryByYear(String admissionNumber, String schoolId, String academicYear) {
        List<AdmissionHistory> allHistory = this.admissionHistoryRepository.findByAdmissionNumberAndSchoolId(admissionNumber, schoolId);
        if (academicYear != null && !academicYear.isEmpty()) {
            return allHistory.stream().filter(history -> academicYear.equals(history.getAcademicYear()) || academicYear.equals(history.getPreviousAcademicYear())).toList();
        }
        return allHistory;
    }

    private String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING: {
                return cell.getStringCellValue();
            }
            case NUMERIC: {
                if (DateUtil.isCellDateFormatted((Cell)cell)) {
                    return cell.getLocalDateTimeCellValue().toLocalDate().toString();
                }
                return String.valueOf((long)cell.getNumericCellValue());
            }
            case BOOLEAN: {
                return String.valueOf(cell.getBooleanCellValue());
            }
            default: {
                return "";
            }
        }
    }

    private boolean checkStudentPendingDues(String admissionNumber, String schoolId) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = this.feeCheckUrl + "/" + admissionNumber + "/" + schoolId;
            ResponseEntity response = restTemplate.getForEntity(url, Map.class, new Object[0]);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Object hasPendingDuesObj = ((Map)response.getBody()).get("hasPendingDues");
                if (hasPendingDuesObj != null) {
                    return Boolean.TRUE.equals(hasPendingDuesObj);
                }
                Object outstandingAmountObj = ((Map)response.getBody()).get("outstandingAmount");
                if (outstandingAmountObj != null) {
                    if (outstandingAmountObj instanceof Number) {
                        return ((Number)outstandingAmountObj).doubleValue() > 0.0;
                    }
                    if (outstandingAmountObj instanceof String) {
                        try {
                            return Double.parseDouble((String)outstandingAmountObj) > 0.0;
                        }
                        catch (NumberFormatException numberFormatException) {
                            // empty catch block
                        }
                    }
                }
            }
            try {
                ResponseMessage feeResponse = this.promotedStudentFee(admissionNumber, "check", "", "", false, schoolId);
                if (feeResponse != null && feeResponse.getMessage() != null && feeResponse.getMessage().contains("outstanding dues")) {
                    return true;
                }
            }
            catch (Exception e) {
                System.err.println("Error checking fees via promotion API: " + e.getMessage());
            }
            return false;
        }
        catch (Exception e) {
            System.err.println("Error checking pending dues for student " + admissionNumber + ": " + e.getMessage());
            return false;
        }
    }

    public Map<String, Object> promoteStudentsInBulk(String schoolId, String currentClassName, String currentSection, String currentSession, String newClassName, String newSection, String newSession, Boolean includePreviousDue, List<String> admissionNumbers) {
        List<Admission> studentsToPromote;
        if (admissionNumbers != null && !admissionNumbers.isEmpty()) {
            studentsToPromote = new ArrayList<>();
            for (String admissionNumber : admissionNumbers) {
                Optional<Admission> student2 = this.admissionRepository.findByAdmissionNumberAndSchoolId(admissionNumber, schoolId);
                student2.ifPresent(studentsToPromote::add);
            }
        } else {
            studentsToPromote = this.admissionRepository.findByClassNameIgnoreCaseAndSectionIgnoreCaseAndSchoolId(currentClassName, currentSection, schoolId);
            if (currentSession != null && !currentSession.isEmpty()) {
                studentsToPromote = studentsToPromote.stream().filter(student -> currentSession.equals(student.getAcademicYear())).toList();
            }
        }
        ArrayList<Admission> promotedStudents = new ArrayList<Admission>();
        ArrayList sameClassStudents = new ArrayList();
        ArrayList errorStudents = new ArrayList();
        ArrayList feeUpdateResults = new ArrayList();
        ArrayList pendingDueStudents = new ArrayList();
        for (Admission admission : studentsToPromote) {
            try {
                boolean hasPendingDues = this.checkStudentPendingDues(admission.getAdmissionNumber(), schoolId);
                ResponseMessage feeResponse = null;
                String pendingDuesMessage = null;
                boolean feeUpdateSuccess = false;
                try {
                    if (includePreviousDue.booleanValue() || !hasPendingDues) {
                        feeResponse = this.promotedStudentFee(admission.getAdmissionNumber(), newClassName, newSection, newSession, includePreviousDue.booleanValue(), schoolId);
                        if (feeResponse != null && feeResponse.getMessage() != null) {
                            if (feeResponse.getMessage().contains("outstanding dues") || feeResponse.getMessage().contains("pending dues") || feeResponse.getMessage().contains("Cannot promote")) {
                                hasPendingDues = true;
                                pendingDuesMessage = feeResponse.getMessage();
                                feeUpdateSuccess = false;
                            } else {
                                feeUpdateSuccess = true;
                            }
                        }
                        System.out.println("Fee updated for student " + admission.getAdmissionNumber() + " during bulk promotion");
                        System.out.println("Fee update response: " + feeResponse);
                    }
                }
                catch (Exception feeError) {
                    System.err.println("Error updating fees for student " + admission.getAdmissionNumber() + ": " + feeError.getMessage());
                    if (feeError.getMessage() != null && (feeError.getMessage().contains("outstanding dues") || feeError.getMessage().contains("pending dues") || feeError.getMessage().contains("Cannot promote"))) {
                        hasPendingDues = true;
                        pendingDuesMessage = feeError.getMessage();
                    }
                    feeUpdateSuccess = false;
                }
                HashMap<String, String> feeResult = new HashMap<String, String>();
                feeResult.put("admissionNumber", admission.getAdmissionNumber());
                feeResult.put("studentName", admission.getStudentName());
                feeResult.put("status", feeUpdateSuccess ? "success" : "error");
                if (pendingDuesMessage != null) {
                    feeResult.put("message", pendingDuesMessage);
                } else if (feeResponse != null && feeResponse.getMessage() != null) {
                    feeResult.put("message", feeResponse.getMessage());
                } else if (hasPendingDues) {
                    feeResult.put("message", "Student has pending dues");
                } else {
                    feeResult.put("message", feeUpdateSuccess ? "Fee updated successfully" : "Fee update failed");
                }
                feeUpdateResults.add(feeResult);
                if (hasPendingDues && !includePreviousDue.booleanValue()) {
                    HashMap<String, String> pendingDueInfo = new HashMap<String, String>();
                    pendingDueInfo.put("admissionNumber", admission.getAdmissionNumber());
                    pendingDueInfo.put("studentName", admission.getStudentName());
                    pendingDueInfo.put("message", pendingDuesMessage != null ? pendingDuesMessage : "Student has pending dues and cannot be promoted");
                    pendingDueStudents.add(pendingDueInfo);
                    continue;
                }
                boolean isSameClass = admission.getClassName().equals(newClassName);
                Admission currentAdmission = admission;
                if (!isSameClass) {
                    AdmissionHistory history = new AdmissionHistory();
                    history.setAdmissionNumber(currentAdmission.getAdmissionNumber());
                    history.setSchoolId(currentAdmission.getSchoolId());
                    history.setStudentName(currentAdmission.getStudentName());
                    history.setRollNo(currentAdmission.getRollNo());
                    history.setClassName(currentAdmission.getClassName());
                    history.setSection(currentAdmission.getSection());
                    history.setAcademicYear(currentAdmission.getAcademicYear());
                    history.setStatus(currentAdmission.getStatus());
                    history.setPromotedDate(LocalDate.now().toString());
                    history.setPreviousClassName(currentAdmission.getClassName());
                    history.setPreviousSection(currentAdmission.getSection());
                    history.setPreviousAcademicYear(currentAdmission.getAcademicYear());
                    history.setPromotionType("PROMOTE");
                    this.admissionHistoryRepository.save(history);
                }
                currentAdmission.setClassName(newClassName);
                if (newSection != null && !newSection.isEmpty()) {
                    currentAdmission.setSection(newSection);
                }
                currentAdmission.setAcademicYear(newSession);
                Admission promotedStudent = (Admission)this.admissionRepository.save(currentAdmission);
                if (isSameClass) {
                    HashMap<String, String> sameClassInfo = new HashMap<String, String>();
                    sameClassInfo.put("admissionNumber", admission.getAdmissionNumber());
                    sameClassInfo.put("studentName", admission.getStudentName());
                    sameClassInfo.put("message", "Student remains in the same class for new academic year");
                    sameClassStudents.add(sameClassInfo);
                    continue;
                }
                promotedStudents.add(promotedStudent);
            }
            catch (Exception e) {
                System.err.println("Error promoting student " + admission.getAdmissionNumber() + ": " + e.getMessage());
                HashMap<String, String> errorInfo = new HashMap<String, String>();
                errorInfo.put("admissionNumber", admission.getAdmissionNumber());
                errorInfo.put("studentName", admission.getStudentName());
                errorInfo.put("error", e.getMessage());
                errorStudents.add(errorInfo);
            }
        }
        HashMap<String, Object> response = new HashMap<String, Object>();
        response.put("promotedStudents", promotedStudents);
        response.put("sameClassStudents", sameClassStudents);
        response.put("errorStudents", errorStudents);
        response.put("feeUpdateResults", feeUpdateResults);
        response.put("pendingDueStudents", pendingDueStudents);
        response.put("totalProcessed", studentsToPromote.size());
        response.put("totalPromoted", promotedStudents.size());
        response.put("totalSameClass", sameClassStudents.size());
        response.put("totalErrors", errorStudents.size());
        response.put("totalPendingDues", pendingDueStudents.size());
        return response;
    }

    public void updateStudentFee(String admissionNumber, String schoolId, String academicYear, String newClassName, String newSection, String newStudentName) {
        String url = this.updateAdmissionFeeDetailsBasedOnAdmissionUpdate + admissionNumber + "/" + schoolId;
        HashMap<String, String> requestBody = new HashMap<String, String>();
        requestBody.put("newClassName", newClassName);
        requestBody.put("newSection", newSection);
        requestBody.put("academicYear", academicYear);
        requestBody.put("studentName", newStudentName);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity requestEntity = new HttpEntity(requestBody, (MultiValueMap)headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, ResponseMessage.class, new Object[0]);
    }
}

