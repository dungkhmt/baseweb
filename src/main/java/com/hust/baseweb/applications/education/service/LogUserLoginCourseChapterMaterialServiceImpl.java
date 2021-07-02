package com.hust.baseweb.applications.education.service;

import com.hust.baseweb.BasewebApplication;
import com.hust.baseweb.applications.education.classmanagement.controller.ClassController;
import com.hust.baseweb.applications.education.entity.EduClass;
import com.hust.baseweb.applications.education.entity.EduCourseChapterMaterial;
import com.hust.baseweb.applications.education.entity.LogUserLoginCourseChapterMaterial;
import com.hust.baseweb.applications.education.repo.ClassRepo;
import com.hust.baseweb.applications.education.repo.EduCourseChapterMaterialRepo;
import com.hust.baseweb.applications.education.repo.LogUserLoginCourseChapterMaterialRepo;
import com.hust.baseweb.applications.education.report.model.courseparticipation.StudentCourseParticipationModel;
import com.hust.baseweb.entity.UserLogin;
import com.hust.baseweb.model.PersonModel;
import com.hust.baseweb.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Log4j2
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class LogUserLoginCourseChapterMaterialServiceImpl implements LogUserLoginCourseChapterMaterialService {

    private LogUserLoginCourseChapterMaterialRepo logUserLoginCourseChapterMaterialRepo;
    private EduCourseChapterMaterialRepo eduCourseChapterMaterialRepo;
    private UserService userService;
    private ClassRepo classRepo;

    //private UserCache userCache;
    public static HashMap<String, PersonModel> mUserLoginId2PersonModel = new HashMap();

    public PersonModel getPersonModel(String userLoginId){
        if(mUserLoginId2PersonModel.get(userLoginId) == null){
            // load data from DB
            List<UserLogin> userLoginList = userService.getAllUserLogins();
            log.info("UserCache, got list " + userLoginList.size() + " users");
            for(UserLogin u: userLoginList){
                PersonModel pm = userService.findPersonByUserLoginId(u.getUserLoginId());
                mUserLoginId2PersonModel.put(u.getUserLoginId(),pm);
            }
        }
        return mUserLoginId2PersonModel.get(userLoginId);
    }

    @Override
    public void logUserLoginMaterial(UserLogin userLogin, UUID eduCourseChapterMaterialId) {
        LogUserLoginCourseChapterMaterial logUserLoginCourseChapterMaterial = new LogUserLoginCourseChapterMaterial();
        logUserLoginCourseChapterMaterial.setUserLoginId(userLogin.getUserLoginId());
        logUserLoginCourseChapterMaterial.setEduCourseMaterialId(eduCourseChapterMaterialId);
        logUserLoginCourseChapterMaterial.setCreateStamp(new Date());
        logUserLoginCourseChapterMaterial = logUserLoginCourseChapterMaterialRepo.save(logUserLoginCourseChapterMaterial);

    }

    @Override
    public List<StudentCourseParticipationModel> findAllByClassId(UUID classId) {
        List<EduCourseChapterMaterial> eduCourseChapterMaterials = eduCourseChapterMaterialRepo.findAll();
        Map<UUID, String> mId2Name = new HashMap<UUID, String>();
        for (EduCourseChapterMaterial m : eduCourseChapterMaterials) {
            mId2Name.put(m.getEduCourseMaterialId(), m.getEduCourseMaterialName());
        }

        EduClass eduClass = classRepo.findById(classId).orElse(null);
        int classCode = 0;
        String courseId = "";
        String courseName = "";
        if (eduClass != null) {
            classCode = eduClass.getCode();
            courseId = eduClass.getEduCourse().getId();
            courseName = eduClass.getEduCourse().getName();
        }

        List<LogUserLoginCourseChapterMaterial> lst = logUserLoginCourseChapterMaterialRepo.findAll();
        List<StudentCourseParticipationModel> studentClassParticipationOutputModels = new ArrayList();
        for (LogUserLoginCourseChapterMaterial e : lst) {
            //PersonModel personModel = userService.findPersonByUserLoginId(e.getUserLoginId());
            // use cache
            PersonModel personModel = getPersonModel(e.getUserLoginId());

            studentClassParticipationOutputModels.add(new StudentCourseParticipationModel(
                e.getUserLoginId(),
                personModel.getLastName() + " " + personModel.getMiddleName() + " " + personModel.getFirstName(),
                classCode + "",
                courseId,
                courseName,
                mId2Name.get(e.getEduCourseMaterialId()), e.getCreateStamp()));
        }
        return studentClassParticipationOutputModels;
    }
}
