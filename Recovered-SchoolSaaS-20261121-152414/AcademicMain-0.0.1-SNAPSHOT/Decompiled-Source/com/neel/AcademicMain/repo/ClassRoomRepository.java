/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.AcademicMain.models.ClassRoom
 *  com.neel.AcademicMain.repo.ClassRoomRepository
 *  org.springframework.data.mongodb.repository.MongoRepository
 */
package com.neel.AcademicMain.repo;

import com.neel.AcademicMain.models.ClassRoom;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClassRoomRepository
extends MongoRepository<ClassRoom, String> {
    public Optional<ClassRoom> findByRoomNo(int var1);

    public Optional<ClassRoom> findByRoomNoAndSchoolId(int var1, String var2);

    public ClassRoom findTopByOrderByClassRoomIdDesc();

    public List<ClassRoom> findByClassId(String var1);

    public List<ClassRoom> findBySchoolId(String var1);

    public List<ClassRoom> findByClassIdAndSchoolId(String var1, String var2);
}

