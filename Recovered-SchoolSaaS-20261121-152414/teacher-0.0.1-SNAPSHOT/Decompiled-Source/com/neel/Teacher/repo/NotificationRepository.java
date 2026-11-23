/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.Teacher.model.Notification
 *  com.neel.Teacher.repo.NotificationRepository
 *  org.springframework.data.mongodb.repository.MongoRepository
 *  org.springframework.stereotype.Repository
 */
package com.neel.Teacher.repo;

import com.neel.Teacher.model.Notification;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository
extends MongoRepository<Notification, String> {
    public List<Notification> findByRecipientIdOrderByCreatedAtDesc(String var1);

    public List<Notification> findByRecipientIdAndReadOrderByCreatedAtDesc(String var1, boolean var2);

    public long countByRecipientIdAndRead(String var1, boolean var2);
}

