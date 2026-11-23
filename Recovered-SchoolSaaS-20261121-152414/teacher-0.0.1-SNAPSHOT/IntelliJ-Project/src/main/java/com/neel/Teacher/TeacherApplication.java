/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.Teacher.TeacherApplication
 *  org.springframework.boot.SpringApplication
 *  org.springframework.boot.autoconfigure.SpringBootApplication
 *  org.springframework.scheduling.annotation.EnableScheduling
 */
package com.neel.Teacher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TeacherApplication {
    public static void main(String[] args) {
        SpringApplication.run(TeacherApplication.class, (String[])args);
    }
}

