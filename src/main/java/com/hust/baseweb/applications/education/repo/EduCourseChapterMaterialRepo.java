package com.hust.baseweb.applications.education.repo;

import com.hust.baseweb.applications.education.entity.EduCourseChapterMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EduCourseChapterMaterialRepo extends JpaRepository<EduCourseChapterMaterial, UUID> {
    public EduCourseChapterMaterial save(EduCourseChapterMaterial eduCourseChapterMaterial);
}
