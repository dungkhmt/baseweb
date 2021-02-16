package com.hust.baseweb.applications.education.suggesttimetable.repo;

import com.hust.baseweb.applications.education.suggesttimetable.entity.EduClass;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.math.BigInteger;

public interface IClassMongoRepo extends MongoRepository<EduClass, BigInteger> {
    EduClass save(EduClass eduClass);
}
