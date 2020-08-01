package com.hust.baseweb.applications.education.repo.mongodb;

import com.hust.baseweb.applications.education.entity.mongodb.Course;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CourseRepo extends MongoRepository<Course, String> {
}
