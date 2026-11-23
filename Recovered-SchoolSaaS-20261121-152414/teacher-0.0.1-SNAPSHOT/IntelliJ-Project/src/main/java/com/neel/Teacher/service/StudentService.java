/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.databind.JavaType
 *  com.fasterxml.jackson.databind.ObjectMapper
 *  com.neel.Teacher.service.StudentService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.beans.factory.annotation.Value
 *  org.springframework.http.ResponseEntity
 *  org.springframework.stereotype.Service
 *  org.springframework.web.client.RestTemplate
 */
package com.neel.Teacher.service;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class StudentService {
    private final RestTemplate restTemplate;
    @Value(value="${student.service.url}")
    String studentServiceUrl;

    @Autowired
    public StudentService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<String, Object> getStudentsByClassAndSection(String schoolId, String className, String section) {
        try {
            String encodedClassName = URLEncoder.encode(className, "UTF-8");
            String encodedSection = URLEncoder.encode(section, "UTF-8");
            String url = this.studentServiceUrl + "/{schoolId}/attendance/students?className={className}&section={section}";
            System.out.println("Calling student service with URL: " + url);
            System.out.println("Parameters - schoolId: " + schoolId + ", className: " + encodedClassName + ", section: " + encodedSection);
            HashMap<String, String> uriVariables = new HashMap<String, String>();
            uriVariables.put("schoolId", schoolId);
            uriVariables.put("className", encodedClassName);
            uriVariables.put("section", encodedSection);
            ResponseEntity response = this.restTemplate.getForEntity(url, String.class, uriVariables);
            System.out.println("Response status code: " + response.getStatusCode());
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                System.out.println("Response body received successfully");
                String responseBody = (String)response.getBody();
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    List studentsList = (List)mapper.readValue(responseBody, (JavaType)mapper.getTypeFactory().constructCollectionType(List.class, Map.class));
                    HashMap<String, Object> resultMap = new HashMap<String, Object>();
                    resultMap.put("data", studentsList);
                    return resultMap;
                }
                catch (Exception e) {
                    System.out.println("Error parsing JSON response: " + e.getMessage());
                    HashMap<String, Object> fallbackMap = new HashMap<String, Object>();
                    fallbackMap.put("rawResponse", responseBody);
                    return fallbackMap;
                }
            }
            System.out.println("Received unsuccessful response or null body");
            return new HashMap<String, Object>();
        }
        catch (Exception e) {
            System.out.println("Error fetching students: " + e.getMessage());
            e.printStackTrace();
            return new HashMap<String, Object>();
        }
    }

    public List<String> getStudentEmails(String schoolId, String className, String section) {
        Map response = this.getStudentsByClassAndSection(schoolId, className, section);
        ArrayList<String> emails = new ArrayList<String>();
        if (response.containsKey("data") && response.get("data") instanceof List) {
            List<Map> students = (List<Map>)response.get("data");
            for (Map student : students) {
                if (!student.containsKey("email") || student.get("email") == null) continue;
                emails.add(student.get("email").toString());
            }
        }
        return emails;
    }

    public List<String> getStudentNames(String schoolId, String className, String section) {
        Map response = this.getStudentsByClassAndSection(schoolId, className, section);
        ArrayList<String> names = new ArrayList<String>();
        if (response.containsKey("data") && response.get("data") instanceof List) {
            List<Map> students = (List<Map>)response.get("data");
            for (Map student : students) {
                if (!student.containsKey("studentName") || student.get("studentName") == null) continue;
                names.add(student.get("studentName").toString());
            }
        }
        return names;
    }
}

