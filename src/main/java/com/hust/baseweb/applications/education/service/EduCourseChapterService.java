package com.hust.baseweb.applications.education.service;

import com.hust.baseweb.applications.education.entity.EduCourseChapter;
import com.hust.baseweb.applications.education.model.EduCourseChapterModelCreate;

import java.util.List;

public interface EduCourseChapterService {
    public EduCourseChapter save(EduCourseChapterModelCreate eduCourseChapterModelCreate);
    public List<EduCourseChapter> findAll();
}
