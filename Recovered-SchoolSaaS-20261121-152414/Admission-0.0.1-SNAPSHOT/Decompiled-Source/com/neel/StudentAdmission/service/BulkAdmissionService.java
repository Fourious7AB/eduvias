/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.databind.ObjectMapper
 *  com.neel.StudentAdmission.dto.BulkAdmissionResponse
 *  com.neel.StudentAdmission.dto.FailedAdmissionDto
 *  com.neel.StudentAdmission.dto.FeesStructureDTO
 *  com.neel.StudentAdmission.dto.StudentAdmissionFeeDTO
 *  com.neel.StudentAdmission.exception.ClassNotFoundInAcademicServiceException
 *  com.neel.StudentAdmission.model.Admission
 *  com.neel.StudentAdmission.model.AuthUser
 *  com.neel.StudentAdmission.model.ResponseMessage
 *  com.neel.StudentAdmission.model.School
 *  com.neel.StudentAdmission.repo.AdmissionRepository
 *  com.neel.StudentAdmission.repo.SchoolRepository
 *  com.neel.StudentAdmission.service.AdmissionService
 *  com.neel.StudentAdmission.service.BulkAdmissionService
 *  com.neel.StudentAdmission.service.BulkAdmissionService$2
 *  org.apache.poi.ss.usermodel.Cell
 *  org.apache.poi.ss.usermodel.CellStyle
 *  org.apache.poi.ss.usermodel.DateUtil
 *  org.apache.poi.ss.usermodel.Font
 *  org.apache.poi.ss.usermodel.IndexedColors
 *  org.apache.poi.ss.usermodel.Row
 *  org.apache.poi.ss.usermodel.Sheet
 *  org.apache.poi.ss.util.CellRangeAddress
 *  org.apache.poi.xssf.usermodel.XSSFWorkbook
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.beans.factory.annotation.Value
 *  org.springframework.core.ParameterizedTypeReference
 *  org.springframework.http.HttpEntity
 *  org.springframework.http.HttpHeaders
 *  org.springframework.http.HttpMethod
 *  org.springframework.http.MediaType
 *  org.springframework.http.ResponseEntity
 *  org.springframework.stereotype.Service
 *  org.springframework.util.MultiValueMap
 *  org.springframework.web.client.HttpClientErrorException
 *  org.springframework.web.client.HttpClientErrorException$NotFound
 *  org.springframework.web.client.HttpServerErrorException
 *  org.springframework.web.client.ResourceAccessException
 *  org.springframework.web.client.RestTemplate
 *  org.springframework.web.multipart.MultipartFile
 */
package com.neel.StudentAdmission.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neel.StudentAdmission.dto.BulkAdmissionResponse;
import com.neel.StudentAdmission.dto.FailedAdmissionDto;
import com.neel.StudentAdmission.dto.FeesStructureDTO;
import com.neel.StudentAdmission.dto.StudentAdmissionFeeDTO;
import com.neel.StudentAdmission.exception.ClassNotFoundInAcademicServiceException;
import com.neel.StudentAdmission.model.Admission;
import com.neel.StudentAdmission.model.AuthUser;
import com.neel.StudentAdmission.model.ResponseMessage;
import com.neel.StudentAdmission.model.School;
import com.neel.StudentAdmission.repo.AdmissionRepository;
import com.neel.StudentAdmission.repo.SchoolRepository;
import com.neel.StudentAdmission.service.AdmissionService;
import com.neel.StudentAdmission.service.BulkAdmissionService;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
public class BulkAdmissionService {
    private final AdmissionRepository admissionRepository;
    private final SchoolRepository schoolRepository;
    private final AdmissionService admissionService;
    @Value(value="${fee.structure.url}")
    private String feeStructureUrl;
    @Value(value="${class.exist.url}")
    private String classExistUrl;
    private RestTemplate restTemplate = new RestTemplate();
    private ObjectMapper objectMapper = new ObjectMapper();
    private Logger logger = LoggerFactory.getLogger(BulkAdmissionService.class);

    @Autowired
    public BulkAdmissionService(AdmissionRepository admissionRepository, SchoolRepository schoolRepository, AdmissionService admissionService) {
        this.admissionRepository = admissionRepository;
        this.schoolRepository = schoolRepository;
        this.admissionService = admissionService;
    }

    public BulkAdmissionResponse processExcelAdmissions(MultipartFile file, String schoolId) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        ArrayList<Admission> admissionSuccessList = new ArrayList<Admission>();
        ArrayList<FailedAdmissionDto> admissionFailedList = new ArrayList<FailedAdmissionDto>();
        for (Row row : sheet) {
            if (row.getRowNum() == 0 || this.isRowEmpty(row)) continue;
            Admission admission = new Admission();
            admission.setStudentName(this.getCellValue(row.getCell(0)));
            admission.setRollNo(this.getCellValue(row.getCell(1)));
            admission.setAdmissionDate(this.getCellValue(row.getCell(2)));
            admission.setClassName(this.getCellValue(row.getCell(3)));
            admission.setSection(this.getCellValue(row.getCell(4)));
            admission.setGender(this.getCellValue(row.getCell(5)));
            admission.setBloodGroup(this.getCellValue(row.getCell(6)));
            admission.setDob(this.getCellValue(row.getCell(7)));
            admission.setReligion(this.getCellValue(row.getCell(8)));
            admission.setFatherName(this.getCellValue(row.getCell(9)));
            admission.setMotherName(this.getCellValue(row.getCell(10)));
            admission.setPhone(this.getCellValue(row.getCell(11)));
            admission.setAlternativePhone(this.getCellValue(row.getCell(12)));
            admission.setEmail(this.getCellValue(row.getCell(13)));
            admission.setPincode(this.getCellValue(row.getCell(14)));
            admission.setState(this.getCellValue(row.getCell(15)));
            admission.setCity(this.getCellValue(row.getCell(16)));
            admission.setDistrict(this.getCellValue(row.getCell(17)));
            admission.setCountry(this.getCellValue(row.getCell(18)));
            admission.setPoliceStation(this.getCellValue(row.getCell(19)));
            admission.setVillagePost(this.getCellValue(row.getCell(20)));
            admission.setAcademicYear(this.getCellValue(row.getCell(21)));
            admission.setStudentImage("default_student.jpg");
            admission.setJointImage("default_joint.jpg");
            try {
                Admission savedAdmission = this.createAdmission(admission, schoolId);
                admissionSuccessList.add(savedAdmission);
            }
            catch (ClassNotFoundInAcademicServiceException e) {
                admissionFailedList.add(new FailedAdmissionDto(admission.getStudentName(), admission.getClassName(), e.getMessage()));
            }
            catch (RuntimeException e) {
                admissionFailedList.add(new FailedAdmissionDto(admission.getStudentName(), admission.getClassName(), e.getMessage()));
            }
        }
        workbook.close();
        return new BulkAdmissionResponse(admissionSuccessList, admissionFailedList);
    }

    private boolean isRowEmpty(Row row) {
        if (row == null) {
            return true;
        }
        for (int i = 0; i <= row.getLastCellNum(); ++i) {
            Cell cell = row.getCell(i);
            if (cell == null || this.getCellValue(cell).isBlank()) continue;
            return false;
        }
        return true;
    }

    private String getCellValue(Cell cell) {
        if (cell == null) {
            return "true";
        }
        switch (2.$SwitchMap$org$apache$poi$ss$usermodel$CellType[cell.getCellType().ordinal()]) {
            case 1: {
                return cell.getStringCellValue();
            }
            case 2: {
                if (DateUtil.isCellDateFormatted((Cell)cell)) {
                    return cell.getLocalDateTimeCellValue().toLocalDate().toString();
                }
                return String.valueOf((long)cell.getNumericCellValue());
            }
            case 3: {
                return String.valueOf(cell.getBooleanCellValue());
            }
        }
        return "";
    }

    public Admission createAdmission(Admission admission, String schoolId) {
        Optional school = this.schoolRepository.findBySchoolId(schoolId);
        if (school.isPresent()) {
            boolean classExist = this.isClassExistBySchoolIdAndClassName(schoolId, admission.getClassName());
            if (!classExist) {
                throw new ClassNotFoundInAcademicServiceException("className : " + admission.getClassName() + " does not exist in school with schoolId : " + schoolId);
            }
            Optional existingAdmission = this.admissionRepository.findByRollNoAndClassNameIgnoreCaseAndSectionIgnoreCaseAndSchoolIdAndAcademicYear(admission.getRollNo(), admission.getClassName(), admission.getSection(), schoolId, admission.getAcademicYear());
            if (existingAdmission.isPresent()) {
                throw new RuntimeException("Student with Roll No: " + admission.getRollNo() + " already exists in class: " + admission.getClassName() + ", section: " + admission.getSection() + " for academic year: " + admission.getAcademicYear());
            }
            String admissionNumber = this.admissionService.generateAdmissionNumber(((School)school.get()).getSchoolCode());
            admission.setAdmissionNumber(admissionNumber);
            admission.setSchoolId(schoolId);
            this.saveAuthUser(admission.getAdmissionNumber(), admission.getDob());
            return (Admission)this.admissionRepository.save((Object)admission);
        }
        throw new RuntimeException("School not found");
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
        ResponseEntity response = restTemplate.postForEntity("https://collage.vaisacademy.com/service4/api/auth/register", (Object)request, String.class, new Object[0]);
        System.out.println("Response: " + (String)response.getBody());
        return (String)response.getBody();
    }

    private boolean isClassExistBySchoolIdAndClassName(String schoolId, String className) {
        String url = this.classExistUrl + className + "/schoolId/" + schoolId;
        try {
            ResponseEntity response = this.restTemplate.getForEntity(url, Boolean.class, new Object[0]);
            return Boolean.TRUE.equals(response.getBody());
        }
        catch (HttpClientErrorException.NotFound e) {
            return false;
        }
        catch (HttpClientErrorException e) {
            System.err.println("Client error while checking class existence: " + e.getMessage());
            throw new RuntimeException("Client error in class service: " + e.getStatusCode());
        }
        catch (Exception e) {
            System.err.println("Unexpected error while checking class existence: " + e.getMessage());
            throw new RuntimeException("Error while calling class service", e);
        }
    }

    private void syncAdmissionWithFeeStructure(Admission savedAdmission) {
        try {
            String feeServiceUrl = this.feeStructureUrl + savedAdmission.getClassName() + "/" + savedAdmission.getSchoolId();
            ResponseEntity response = this.restTemplate.exchange(feeServiceUrl, HttpMethod.GET, null, (ParameterizedTypeReference)new /* Unavailable Anonymous Inner Class!! */, new Object[0]);
            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                throw new IllegalStateException("Fee structure not found for School ID: " + savedAdmission.getSchoolId() + ", Class: " + savedAdmission.getClassName());
            }
            ResponseMessage respMsg = (ResponseMessage)response.getBody();
            if (respMsg.getData() == null) {
                throw new IllegalStateException("Fee service responded with error: " + respMsg.getMessage());
            }
            FeesStructureDTO fees = (FeesStructureDTO)this.objectMapper.convertValue(respMsg.getData(), FeesStructureDTO.class);
            StudentAdmissionFeeDTO feeDTO = new StudentAdmissionFeeDTO();
            feeDTO.setSchoolId(savedAdmission.getSchoolId());
            feeDTO.setAdmissionNumber(savedAdmission.getAdmissionNumber());
            feeDTO.setStudentName(savedAdmission.getStudentName());
            feeDTO.setClassName(savedAdmission.getClassName());
            feeDTO.setSection(savedAdmission.getSection());
            feeDTO.setAdmissionFees(fees.getAdmission_Fees());
            System.out.println("i am from studentAdmission service : getAdmissionFees" + fees.getAdmission_Fees());
            feeDTO.setBooks(fees.getBooks());
            System.out.println("i am from studentAdmission service : getBooksFee :" + fees.getBooks());
            feeDTO.setDonation(fees.getDonation());
            feeDTO.setFine(fees.getFine());
            feeDTO.setIdCardCharges(fees.getId_Card_Charges());
            feeDTO.setLateFees(fees.getLate_Fees());
            feeDTO.setMiscellaneous(fees.getMiscellaneous());
            feeDTO.setTransportationalFees(fees.getTransportationalFees());
            feeDTO.setTuitionFees(fees.getTuitionFees());
            feeDTO.setUniformCharges(fees.getUniform_Charges());
            feeDTO.setOther(fees.getOther());
            feeDTO.setTotalFees(fees.getTotalFees());
            feeDTO.setDate(LocalDateTime.now());
        }
        catch (HttpClientErrorException | HttpServerErrorException httpEx) {
            this.logger.error("HTTP Error: {} - {}", (Object)httpEx.getStatusCode(), (Object)httpEx.getResponseBodyAsString());
        }
        catch (ResourceAccessException rae) {
            this.logger.error("Connection error: Could not reach fee service - {}", (Object)rae.getMessage());
        }
        catch (IllegalStateException ise) {
            this.logger.warn("Logic Error: {}", (Object)ise.getMessage());
        }
        catch (Exception e) {
            this.logger.error("Unexpected error: {}", (Object)e.getMessage(), (Object)e);
        }
    }

    public ByteArrayInputStream generateBulkAdmissionTemplate() throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook();){
            Sheet sheet = workbook.createSheet("Student Admission Template");
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);
            Row headerRow = sheet.createRow(0);
            String[] columns = new String[]{"Student Name*", "Roll No*", "Admission Date (YYYY-MM-DD)*", "Class Name*", "Section*", "Gender (M/F)*", "Blood Group", "Date of Birth (YYYY-MM-DD)*", "Religion", "Father Name*", "Mother Name*", "Phone*", "Alternative Phone", "Email", "Pincode", "State", "City", "District", "Country", "Police Station", "Village/Post", "Academic Year*"};
            for (int i = 0; i < columns.length; ++i) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerCellStyle);
                sheet.setColumnWidth(i, 6000);
            }
            Row sampleRow = sheet.createRow(1);
            sampleRow.createCell(0).setCellValue("John Doe");
            sampleRow.createCell(1).setCellValue("101");
            sampleRow.createCell(2).setCellValue("2023-06-01");
            sampleRow.createCell(3).setCellValue("Class 1");
            sampleRow.createCell(4).setCellValue("A");
            sampleRow.createCell(5).setCellValue("M");
            sampleRow.createCell(6).setCellValue("O+");
            sampleRow.createCell(7).setCellValue("2017-05-15");
            sampleRow.createCell(8).setCellValue("Christianity");
            sampleRow.createCell(9).setCellValue("John Doe Sr.");
            sampleRow.createCell(10).setCellValue("Jane Doe");
            sampleRow.createCell(11).setCellValue("9876543210");
            sampleRow.createCell(12).setCellValue("8765432109");
            sampleRow.createCell(13).setCellValue("john.doe@example.com");
            sampleRow.createCell(14).setCellValue("123456");
            sampleRow.createCell(15).setCellValue("Karnataka");
            sampleRow.createCell(16).setCellValue("Bangalore");
            sampleRow.createCell(17).setCellValue("Bangalore Urban");
            sampleRow.createCell(18).setCellValue("India");
            sampleRow.createCell(19).setCellValue("Koramangala");
            sampleRow.createCell(20).setCellValue("HSR Layout");
            sampleRow.createCell(21).setCellValue("2023-2024");
            Row noteRow = sheet.createRow(2);
            Cell noteCell = noteRow.createCell(0);
            noteCell.setCellValue("Note: Fields marked with * are mandatory");
            CellStyle noteStyle = workbook.createCellStyle();
            Font noteFont = workbook.createFont();
            noteFont.setItalic(true);
            noteFont.setColor(IndexedColors.RED.getIndex());
            noteStyle.setFont(noteFont);
            noteCell.setCellStyle(noteStyle);
            sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 5));
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write((OutputStream)outputStream);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(outputStream.toByteArray());
            return byteArrayInputStream;
        }
    }
}

