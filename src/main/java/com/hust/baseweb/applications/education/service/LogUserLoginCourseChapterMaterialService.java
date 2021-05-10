package com.hust.baseweb.applications.education.service;

import com.hust.baseweb.applications.education.entity.EduCourseChapterMaterial;
import com.hust.baseweb.applications.education.report.model.courseparticipation.StudentCourseParticipationModel;
import com.hust.baseweb.entity.UserLogin;

import java.util.List;
import java.util.UUID;

public interface LogUserLoginCourseChapterMaterialService {
    public void logUserLoginMaterial(UserLogin userLogin, UUID eduCourseChapterMaterialId);
    public List<StudentCourseParticipationModel> findAllByClassId(UUID classId);
}
