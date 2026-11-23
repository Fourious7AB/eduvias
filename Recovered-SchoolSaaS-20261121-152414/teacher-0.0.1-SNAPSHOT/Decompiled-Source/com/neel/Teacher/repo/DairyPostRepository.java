/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.Teacher.model.DairyPost
 *  com.neel.Teacher.repo.DairyPostRepository
 *  org.springframework.data.mongodb.repository.MongoRepository
 *  org.springframework.data.mongodb.repository.Query
 *  org.springframework.stereotype.Repository
 */
package com.neel.Teacher.repo;

import com.neel.Teacher.model.DairyPost;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DairyPostRepository
extends MongoRepository<DairyPost, String> {
    public List<DairyPost> findByAuthorId(String var1);

    @Query(value="{'tags.type': ?0, 'tags.id': ?1}")
    public List<DairyPost> findByTagTypeAndTagId(String var1, String var2);

    public List<DairyPost> findAllByOrderByCreatedAtDesc();

    public List<DairyPost> findBySchoolIdOrderByCreatedAtDesc(String var1);

    public List<DairyPost> findBySchoolIdAndAuthorId(String var1, String var2);

    @Query(value="{'schoolId': ?0, 'tags.type': ?1, 'tags.id': ?2}")
    public List<DairyPost> findBySchoolIdAndTagTypeAndTagId(String var1, String var2, String var3);
}

