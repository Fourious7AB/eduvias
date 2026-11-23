package com.result.Result.repo;

import com.result.Result.model.Criteria;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CriteriaRepository extends MongoRepository<Criteria, String> {
    public List<Criteria> findAllBySchoolId(String schoolId);

    public Optional<Criteria> findByIdAndSchoolId(String id, String schoolId);

    public Optional<Criteria> findBySchoolIdAndClassNameAndExamType(String schoolId, String className, String examType);

    public List<Criteria> findBySchoolIdAndClassNameIgnoreCaseAndExamTypeIgnoreCase(String schoolId, String className, String examType);
}
