package com.hust.baseweb.applications.education.service;

import com.hust.baseweb.applications.education.content.Video;
import com.hust.baseweb.applications.education.entity.EduCourseChapterMaterial;
import com.hust.baseweb.applications.education.model.EduCourseChapterMaterialModelCreate;

import java.util.List;
import java.util.UUID;

public interface EduCourseChapterMaterialService {

    public EduCourseChapterMaterial save(
        EduCourseChapterMaterialModelCreate eduCourseChapterMaterialModelCreate,
        Video video
    );

    public List<EduCourseChapterMaterial> findAll();

    public List<EduCourseChapterMaterial> findAllByChapterId(UUID chapterId);

    public EduCourseChapterMaterial findById(UUID eduCourseChapterMaterialId);
}
