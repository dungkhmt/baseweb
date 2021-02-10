package com.hust.baseweb.applications.education.suggesttimetable.repo;

import lombok.AllArgsConstructor;
import lombok.experimental.Delegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CourseRepo implements ICourseRepo {

    @Delegate
    private ICourseMongoRepo courseRepo;
}
