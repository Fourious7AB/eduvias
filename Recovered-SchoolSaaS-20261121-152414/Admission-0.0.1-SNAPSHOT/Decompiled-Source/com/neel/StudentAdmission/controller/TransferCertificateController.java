/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.StudentAdmission.controller.TransferCertificateController
 *  com.neel.StudentAdmission.model.ResponseMessage
 *  com.neel.StudentAdmission.model.TransferCertificate
 *  com.neel.StudentAdmission.model.TransferCertificateRequest
 *  com.neel.StudentAdmission.service.TransferCertificateService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.core.io.ByteArrayResource
 *  org.springframework.core.io.Resource
 *  org.springframework.http.HttpStatus
 *  org.springframework.http.HttpStatusCode
 *  org.springframework.http.MediaType
 *  org.springframework.http.ResponseEntity
 *  org.springframework.http.ResponseEntity$BodyBuilder
 *  org.springframework.web.bind.annotation.CrossOrigin
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.RestController
 *  org.springframework.web.multipart.MultipartFile
 */
package com.neel.StudentAdmission.controller;

import com.neel.StudentAdmission.model.ResponseMessage;
import com.neel.StudentAdmission.model.TransferCertificate;
import com.neel.StudentAdmission.model.TransferCertificateRequest;
import com.neel.StudentAdmission.service.TransferCertificateService;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins={"*"})
@RequestMapping
public class TransferCertificateController {
    @Autowired
    private TransferCertificateService transferCertificateService;

    @PostMapping(value={"/transfer-certificates/request/{admissionNumber}/{schoolId}"})
    public ResponseEntity<ResponseMessage> requestTransferCertificate(@PathVariable String admissionNumber, @PathVariable String schoolId, @RequestParam(value="reasonForLeaving") String reasonForLeaving, @RequestParam(value="requiredDocs", required=false) List<MultipartFile> requiredDocs) {
        try {
            ArrayList docs = requiredDocs != null ? requiredDocs : new ArrayList();
            TransferCertificateRequest request = this.transferCertificateService.requestTransferCertificate(admissionNumber, schoolId, reasonForLeaving, docs);
            return ResponseEntity.ok((Object)new ResponseMessage("Transfer certificate request submitted successfully", (Object)request));
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status((HttpStatusCode)HttpStatus.INTERNAL_SERVER_ERROR).body((Object)new ResponseMessage(e.getMessage(), null));
        }
    }

    @GetMapping(value={"/transfer-certificates/requests/{schoolId}"})
    public ResponseEntity<ResponseMessage> getAllTransferCertificateRequests(@PathVariable String schoolId) {
        try {
            List requests = this.transferCertificateService.getAllTransferCertificateRequests(schoolId);
            return ResponseEntity.ok((Object)new ResponseMessage("Transfer certificate requests retrieved successfully", (Object)requests));
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.INTERNAL_SERVER_ERROR).body((Object)new ResponseMessage("Error retrieving transfer certificate requests: " + e.getMessage(), null));
        }
    }

    @GetMapping(value={"/transfer-certificates/requests/pending/{schoolId}"})
    public ResponseEntity<ResponseMessage> getPendingTransferCertificateRequests(@PathVariable String schoolId) {
        try {
            List requests = this.transferCertificateService.getPendingTransferCertificateRequests(schoolId);
            return ResponseEntity.ok((Object)new ResponseMessage("Pending transfer certificate requests retrieved successfully", (Object)requests));
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.INTERNAL_SERVER_ERROR).body((Object)new ResponseMessage("Error retrieving pending transfer certificate requests: " + e.getMessage(), null));
        }
    }

    @GetMapping(value={"/transfer-certificates/request-status/{admissionNumber}/{schoolId}"})
    public ResponseEntity<ResponseMessage> getTransferCertificateRequestStatus(@PathVariable String admissionNumber, @PathVariable String schoolId) {
        try {
            List request = this.transferCertificateService.getTransferCertificateRequestStatus(admissionNumber, schoolId);
            return ResponseEntity.ok((Object)new ResponseMessage("Transfer certificate request status retrieved successfully", (Object)request));
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body((Object)new ResponseMessage(e.getMessage(), null));
        }
    }

    @PostMapping(value={"/transfer-certificates/approve/{requestId}"})
    public ResponseEntity<ResponseMessage> approveTransferCertificateRequest(@PathVariable String requestId, @RequestParam(value="adminRemarks") String adminRemarks, @RequestBody TransferCertificate tcDetails) {
        try {
            TransferCertificate tc = this.transferCertificateService.approveTransferCertificateRequest(requestId, adminRemarks, tcDetails);
            return ResponseEntity.ok((Object)new ResponseMessage("Transfer certificate request approved successfully", (Object)tc));
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.INTERNAL_SERVER_ERROR).body((Object)new ResponseMessage("Error approving transfer certificate request: " + e.getMessage(), null));
        }
    }

    @PostMapping(value={"/transfer-certificates/reject/{requestId}"})
    public ResponseEntity<ResponseMessage> rejectTransferCertificateRequest(@PathVariable String requestId, @RequestBody Map<String, String> payload) {
        try {
            String adminRemarks = payload.get("adminRemarks");
            TransferCertificateRequest request = this.transferCertificateService.rejectTransferCertificateRequest(requestId, adminRemarks);
            return ResponseEntity.ok((Object)new ResponseMessage("Transfer certificate request rejected successfully", (Object)request));
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.INTERNAL_SERVER_ERROR).body((Object)new ResponseMessage("Error rejecting transfer certificate request: " + e.getMessage(), null));
        }
    }

    @PostMapping(value={"/transfer-certificates/{admissionNumber}/{schoolId}"})
    public ResponseEntity<ResponseMessage> createTransferCertificate(@PathVariable String admissionNumber, @PathVariable String schoolId, @RequestBody TransferCertificate tcDetails) {
        try {
            TransferCertificate tc = this.transferCertificateService.createTransferCertificate(admissionNumber, schoolId, tcDetails);
            return ResponseEntity.ok((Object)new ResponseMessage("Transfer certificate created successfully", (Object)tc));
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.INTERNAL_SERVER_ERROR).body((Object)new ResponseMessage("Error creating transfer certificate: " + e.getMessage(), null));
        }
    }

    @GetMapping(value={"/transfer-certificates/number"})
    public ResponseEntity<ResponseMessage> getTransferCertificateByNumber(@RequestBody String tcNumber) {
        try {
            TransferCertificate tc = this.transferCertificateService.getTransferCertificateByNumber(tcNumber);
            return ResponseEntity.ok((Object)new ResponseMessage("Transfer certificate retrieved successfully", (Object)tc));
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body((Object)new ResponseMessage("Error retrieving transfer certificate: " + e.getMessage(), null));
        }
    }

    @GetMapping(value={"/transfer-certificates/{admissionNumber}/{schoolId}"})
    public ResponseEntity<ResponseMessage> getTransferCertificateByAdmission(@PathVariable String admissionNumber, @PathVariable String schoolId) {
        try {
            TransferCertificate tc = this.transferCertificateService.getTransferCertificateByAdmission(admissionNumber, schoolId);
            return ResponseEntity.ok((Object)new ResponseMessage("Transfer certificate retrieved successfully", (Object)tc));
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body((Object)new ResponseMessage(e.getMessage(), null));
        }
    }

    @GetMapping(value={"/transfer-certificates/school/{schoolId}"})
    public ResponseEntity<ResponseMessage> getAllTransferCertificates(@PathVariable String schoolId) {
        try {
            List tcs = this.transferCertificateService.getAllTransferCertificates(schoolId);
            return ResponseEntity.ok((Object)new ResponseMessage("Transfer certificates retrieved successfully", (Object)tcs));
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.INTERNAL_SERVER_ERROR).body((Object)new ResponseMessage("Error retrieving transfer certificates: " + e.getMessage(), null));
        }
    }

    @GetMapping(value={"/transfer-certificates/download"})
    public ResponseEntity<Resource> downloadTransferCertificate(@RequestParam String tcNumber) {
        try {
            TransferCertificate tc = this.transferCertificateService.getTransferCertificateByNumber(tcNumber);
            System.out.println("Generating fresh TC PDF...");
            File file = this.transferCertificateService.generateTransferCertificatePDF(tc);
            if (file == null || !file.exists()) {
                throw new RuntimeException("Failed to generate transfer certificate file");
            }
            Path path = file.toPath();
            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
            return ((ResponseEntity.BodyBuilder)ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).contentLength(file.length()).header("Content-Disposition", new String[]{"attachment; filename=\"" + file.getName() + "\""})).body((Object)resource);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error downloading transfer certificate: " + e.getMessage());
        }
    }

    @GetMapping(value={"/transfer-certificates/download/{id}/{schoolId}"})
    public ResponseEntity<Resource> downloadTransferCertificateByAdmin(@PathVariable String id, @PathVariable String schoolId) {
        try {
            TransferCertificate tc = this.transferCertificateService.getTransferCertificateById(id, schoolId);
            System.out.println("Generating fresh TC PDF...");
            File file = this.transferCertificateService.generateTransferCertificatePDF(tc);
            if (file == null || !file.exists()) {
                throw new RuntimeException("Failed to generate transfer certificate file");
            }
            Path path = file.toPath();
            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
            return ((ResponseEntity.BodyBuilder)ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).contentLength(file.length()).header("Content-Disposition", new String[]{"attachment; filename=\"" + file.getName() + "\""})).body((Object)resource);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error downloading transfer certificate: " + e.getMessage());
        }
    }

    private File findFileInAlternativeLocations(TransferCertificate tc) {
        String[] possiblePaths = new String[]{"id_cards/transfer_certificates/tc_" + tc.getAdmissionNumber() + "_" + tc.getSchoolId() + ".pdf", "id_cards\\transfer_certificates\\tc_" + tc.getAdmissionNumber() + "_" + tc.getSchoolId() + ".pdf", tc.getTcPath().replace('/', '\\'), tc.getTcPath().replace('\\', '/')};
        String baseDir = System.getProperty("user.dir");
        for (String path : possiblePaths) {
            File file1 = new File(path);
            File file2 = new File(baseDir, path);
            if (file1.exists()) {
                return file1;
            }
            if (file2.exists()) {
                return file2;
            }
            System.out.println("Tried path: " + file1.getAbsolutePath() + " - exists: " + file1.exists());
            System.out.println("Tried path: " + file2.getAbsolutePath() + " - exists: " + file2.exists());
        }
        return null;
    }
}

