/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.RoutineMain.model.Notification
 *  com.neel.RoutineMain.repo.NotificationRepository
 *  org.springframework.data.mongodb.repository.MongoRepository
 */
package com.neel.RoutineMain.repo;

import com.neel.RoutineMain.model.Notification;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository
extends MongoRepository<Notification, String> {
    public List<Notification> findBySchoolId(String var1);

    public Optional<Notification> findByIdAndSchoolId(String var1, String var2);
}

