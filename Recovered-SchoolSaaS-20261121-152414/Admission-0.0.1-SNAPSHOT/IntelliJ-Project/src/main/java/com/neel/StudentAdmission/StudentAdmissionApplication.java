/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.StudentAdmission.StudentAdmissionApplication
 *  org.springframework.boot.SpringApplication
 *  org.springframework.boot.autoconfigure.SpringBootApplication
 *  org.springframework.scheduling.annotation.EnableScheduling
 */
package com.neel.StudentAdmission;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StudentAdmissionApplication {
    public static void main(String[] args) {
        SpringApplication.run(StudentAdmissionApplication.class, (String[])args);
    }
}

