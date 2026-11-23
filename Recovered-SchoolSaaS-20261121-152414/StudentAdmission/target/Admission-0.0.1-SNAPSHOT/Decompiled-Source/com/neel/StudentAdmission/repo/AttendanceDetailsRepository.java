/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.StudentAdmission.model.AttendanceDetails
 *  com.neel.StudentAdmission.repo.AttendanceDetailsRepository
 *  org.springframework.data.mongodb.repository.MongoRepository
 *  org.springframework.stereotype.Repository
 */
package com.neel.StudentAdmission.repo;

import com.neel.StudentAdmission.model.AttendanceDetails;
import java.util.Date;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceDetailsRepository
extends MongoRepository<AttendanceDetails, String> {
    public List<AttendanceDetails> findByClassNameAndSection(String var1, String var2);

    public List<AttendanceDetails> findByClassNameAndSectionAndAttendanceDate(String var1, String var2, Date var3);

    public List<AttendanceDetails> findByClassNameAndSectionAndAttendanceDateBetween(String var1, String var2, Date var3, Date var4);

    public List<AttendanceDetails> findBySchoolId(String var1);

    public List<AttendanceDetails> findBySchoolIdAndClassNameAndSection(String var1, String var2, String var3);

    public List<AttendanceDetails> findBySchoolIdAndClassNameAndSectionAndAttendanceDate(String var1, String var2, String var3, Date var4);

    public List<AttendanceDetails> findBySchoolIdAndClassNameAndSectionAndAttendanceDateBetween(String var1, String var2, String var3, Date var4, Date var5);
}

