package com.hust.baseweb.applications.education.suggesttimetable.repo;

import com.hust.baseweb.applications.education.suggesttimetable.entity.EduCourse;
import lombok.AllArgsConstructor;
import lombok.experimental.Delegate;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class CourseRepo implements ICourseRepo {

    @Delegate
    private final ICourseMongoRepo courseRepo;

    private final MongoTemplate mongoTemplate;

    @Override
    public void saveAll(List<EduCourse> eduCourses) {
        courseRepo.saveAll(eduCourses);
    }

    /**
     * Drop collection and insert courses in batch.
     *
     * @param courses
     */
    public void insertCoursesInBatch(List<EduCourse> courses) {
        mongoTemplate.dropCollection("courses");
        BulkOperations bulkOperations = mongoTemplate.bulkOps(BulkOperations.BulkMode.ORDERED, "courses");
        bulkOperations.insert(courses);
        bulkOperations.execute();
    }
}
