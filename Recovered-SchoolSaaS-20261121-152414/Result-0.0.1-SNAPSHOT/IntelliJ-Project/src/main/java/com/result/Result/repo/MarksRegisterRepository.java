/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.result.Result.model.MarksRegister
 *  com.result.Result.repo.MarksRegisterRepository
 *  org.springframework.data.mongodb.repository.MongoRepository
 */
package com.result.Result.repo;

import com.result.Result.model.MarksRegister;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MarksRegisterRepository
extends MongoRepository<MarksRegister, String> {
    public Optional<MarksRegister> findByClassNameAndSectionAndSubjectAndExamType(String var1, String var2, String var3, String var4);

    public List<MarksRegister> findByClassNameAndSectionAndExamType(String var1, String var2, String var3);

    public Optional<MarksRegister> findByIdAndSchoolId(String var1, String var2);

    public List<MarksRegister> findBySchoolId(String var1);

    public Optional<MarksRegister> findByClassNameIgnoreCaseAndSectionIgnoreCaseAndSubjectIgnoreCaseAndExamTypeIgnoreCaseAndSchoolId(String var1, String var2, String var3, String var4, String var5);

    public List<MarksRegister> findByClassNameIgnoreCaseAndSectionIgnoreCaseAndExamTypeIgnoreCaseAndSchoolId(String var1, String var2, String var3, String var4);

    public List<MarksRegister> findByClassNameIgnoreCaseAndSectionIgnoreCaseAndSchoolId(String var1, String var2, String var3);
}

