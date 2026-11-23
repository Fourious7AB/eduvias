package com.result.Result.repo;

import com.result.Result.model.AdditionalMarksRegister;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdditionalMarksRegisterRepository extends MongoRepository<AdditionalMarksRegister, String> {
    List<AdditionalMarksRegister> findByClassNameIgnoreCaseAndSectionIgnoreCaseAndExamTypeIgnoreCaseAndSchoolId(
            String className, String section, String examType, String schoolId);
}
