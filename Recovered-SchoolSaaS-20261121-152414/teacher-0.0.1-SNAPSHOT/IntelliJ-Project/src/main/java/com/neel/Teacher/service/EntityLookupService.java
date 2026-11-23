/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.Teacher.model.Edp
 *  com.neel.Teacher.model.EntityLookupResult
 *  com.neel.Teacher.model.EntityType
 *  com.neel.Teacher.model.Librarian
 *  com.neel.Teacher.model.Teachers
 *  com.neel.Teacher.service.EdpService
 *  com.neel.Teacher.service.EntityLookupService
 *  com.neel.Teacher.service.EntityLookupService$ExternalLookupResponse
 *  com.neel.Teacher.service.LibrarianService
 *  com.neel.Teacher.service.TeachersService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.beans.factory.annotation.Value
 *  org.springframework.http.ResponseEntity
 *  org.springframework.stereotype.Service
 *  org.springframework.web.client.RestClientException
 *  org.springframework.web.client.RestTemplate
 */
package com.neel.Teacher.service;

import com.neel.Teacher.model.Edp;
import com.neel.Teacher.model.EntityLookupResult;
import com.neel.Teacher.model.EntityType;
import com.neel.Teacher.model.Librarian;
import com.neel.Teacher.model.Teachers;
import com.neel.Teacher.service.EdpService;
import com.neel.Teacher.service.EntityLookupService;
import com.neel.Teacher.service.LibrarianService;
import com.neel.Teacher.service.TeachersService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class EntityLookupService {
    private final TeachersService teachersService;
    private final EdpService edpService;
    private final LibrarianService librarianService;
    private final RestTemplate restTemplate;
    @Value(value="${view.subscription.plan.url}")
    private String subscriptionPlanUrl;
    @Value(value="${external.lookup.url}")
    private String EXTERNAL_LOOKUP_URL;

    @Autowired
    public EntityLookupService(TeachersService teachersService, EdpService edpService, LibrarianService librarianService, RestTemplate restTemplate) {
        this.teachersService = teachersService;
        this.edpService = edpService;
        this.librarianService = librarianService;
        this.restTemplate = restTemplate;
    }

    public EntityLookupResult findEntityById(String id, String schoolId) {
        block10: {
            Teachers teacher = this.teachersService.getTeacherById(id, schoolId);
            if (teacher != null) {
                return EntityLookupResult.found((EntityType)EntityType.TEACHER, (Object)teacher);
            }
            Edp edp = this.edpService.getTeacherById(id, schoolId);
            if (edp != null) {
                return EntityLookupResult.found((EntityType)EntityType.EDP, (Object)edp);
            }
            Librarian librarian = this.librarianService.getTeacherById(id, schoolId);
            if (librarian != null) {
                return EntityLookupResult.found((EntityType)EntityType.LIBRARIAN, (Object)librarian);
            }
            try {
                String url = this.EXTERNAL_LOOKUP_URL + "/" + id + "/" + schoolId;
                ResponseEntity response = this.restTemplate.getForEntity(url, ExternalLookupResponse.class, new Object[0]);
                System.out.println(" url : " + response);
                if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) break block10;
                ExternalLookupResponse externalResult = (ExternalLookupResponse)response.getBody();
                System.out.println(" after response : " + externalResult);
                if (externalResult.getData() == null) break block10;
                Map dataMap = (Map)externalResult.getData();
                System.out.println(" after response data : " + dataMap);
                if (!dataMap.containsKey("found") || !Boolean.TRUE.equals(dataMap.get("found"))) break block10;
                EntityType entityType = EntityType.NONE;
                if (dataMap.containsKey("role")) {
                    String roleStr = dataMap.get("role").toString();
                    try {
                        entityType = EntityType.valueOf((String)roleStr);
                    }
                    catch (IllegalArgumentException e) {
                        System.err.println("Unknown entity type: " + roleStr);
                    }
                }
                Map entityData = dataMap;
                if (dataMap.containsKey("user")) {
                    entityData = (Map)dataMap.get("user");
                }
                return EntityLookupResult.found((EntityType)entityType, (Object)entityData);
            }
            catch (RestClientException e) {
                System.err.println("Error calling external lookup service: " + e.getMessage());
            }
            catch (ClassCastException e) {
                System.err.println("Error processing response data: " + e.getMessage());
            }
        }
        return EntityLookupResult.notFound();
    }

    public static class ExternalLookupResponse {
        private boolean success;
        private String message;
        private Object data;

        public ExternalLookupResponse() {}
        public boolean isSuccess() { return this.success; }
        public String getMessage() { return this.message; }
        public Object getData() { return this.data; }
        public void setSuccess(boolean success) { this.success = success; }
        public void setMessage(String message) { this.message = message; }
        public void setData(Object data) { this.data = data; }
    }
}
