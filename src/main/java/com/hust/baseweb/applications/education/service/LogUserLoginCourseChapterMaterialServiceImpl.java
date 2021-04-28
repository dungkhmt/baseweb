package com.hust.baseweb.applications.education.service;

import com.hust.baseweb.applications.education.entity.LogUserLoginCourseChapterMaterial;
import com.hust.baseweb.applications.education.repo.LogUserLoginCourseChapterMaterialRepo;
import com.hust.baseweb.entity.UserLogin;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Log4j2
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class LogUserLoginCourseChapterMaterialServiceImpl implements  LogUserLoginCourseChapterMaterialService{
    private LogUserLoginCourseChapterMaterialRepo logUserLoginCourseChapterMaterialRepo;
    @Override
    public void logUserLoginMaterial(UserLogin userLogin, UUID eduCourseChapterMaterialId) {
        LogUserLoginCourseChapterMaterial logUserLoginCourseChapterMaterial = new LogUserLoginCourseChapterMaterial();
        logUserLoginCourseChapterMaterial.setUserLoginId(userLogin.getUserLoginId());
        logUserLoginCourseChapterMaterial.setEduCourseMaterialId(eduCourseChapterMaterialId);
        logUserLoginCourseChapterMaterial.setCreateStamp(new Date());
        logUserLoginCourseChapterMaterial = logUserLoginCourseChapterMaterialRepo.save(logUserLoginCourseChapterMaterial);

    }
}
