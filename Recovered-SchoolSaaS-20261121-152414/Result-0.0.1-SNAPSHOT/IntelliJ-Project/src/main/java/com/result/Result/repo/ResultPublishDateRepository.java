package com.result.Result.repo;

import com.result.Result.model.ResultPublishDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultPublishDateRepository extends MongoRepository<ResultPublishDate, String> {
    List<ResultPublishDate> findBySchoolId(String schoolId);

    Optional<ResultPublishDate> findByIdAndSchoolId(String id, String schoolId);
}
