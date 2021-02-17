package com.hust.baseweb.applications.education.suggesttimetable.repo;

import com.hust.baseweb.applications.education.suggesttimetable.entity.EduCourse;
import lombok.AllArgsConstructor;
import lombok.experimental.Delegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CourseRepo implements ICourseRepo {

    @Delegate
    private ICourseMongoRepo courseRepo;

    @Override
    public void saveAll(List<EduCourse> eduCourses) {
        courseRepo.saveAll(eduCourses);
    }
}
