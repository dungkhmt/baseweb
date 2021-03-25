package com.hust.baseweb.applications.education.suggesttimetable.repo;

import com.hust.baseweb.applications.education.suggesttimetable.entity.EduCourse;
import lombok.AllArgsConstructor;
import lombok.experimental.Delegate;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

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

    @Override
    public List<EduCourse> findAllById(Set<String> courseIds) {
        return (List<EduCourse>) courseRepo.findAllById(courseIds);
    }

    /**
     * Drop collection and insert courses in batch.
     *
     * @param courses
     */
    public void insertCoursesInBatch(List<EduCourse> courses) {
        String collectionName = "course";
        mongoTemplate.dropCollection(collectionName);
        BulkOperations bulkOperations = mongoTemplate.bulkOps(BulkOperations.BulkMode.ORDERED, collectionName);
        bulkOperations.insert(courses);
        bulkOperations.execute();
    }
}
