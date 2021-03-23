package com.hust.baseweb.applications.education.suggesttimetable.repo;

import com.hust.baseweb.applications.education.suggesttimetable.entity.EduCourse;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Set;

public interface ICourseMongoRepo extends MongoRepository<EduCourse, String> {

    List<EduCourse> findByIdIn(Set<String> courseIds);
}
