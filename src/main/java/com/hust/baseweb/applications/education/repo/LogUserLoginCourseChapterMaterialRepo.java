package com.hust.baseweb.applications.education.repo;

import com.hust.baseweb.applications.education.entity.LogUserLoginCourseChapterMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LogUserLoginCourseChapterMaterialRepo extends JpaRepository<LogUserLoginCourseChapterMaterial, UUID> {

    LogUserLoginCourseChapterMaterial save(LogUserLoginCourseChapterMaterial logUserLoginCourseChapterMaterial);

}
