package com.hust.baseweb.applications.education.teacherclassassignment.service;

import com.hust.baseweb.applications.education.teacherclassassignment.entity.ClassTeacherAssignmentClassInfo;
import com.hust.baseweb.applications.education.teacherclassassignment.entity.ClassTeacherAssignmentPlan;
import com.hust.baseweb.applications.education.teacherclassassignment.model.ClassTeacherAssignmentPlanCreateModel;
import com.hust.baseweb.applications.education.teacherclassassignment.model.ClassTeacherAssignmentPlanDetailModel;
import com.hust.baseweb.applications.education.teacherclassassignment.repo.ClassTeacherAssignmentClassInfoRepo;
import com.hust.baseweb.applications.education.teacherclassassignment.repo.ClassTeacherAssignmentPlanRepo;
import com.hust.baseweb.entity.UserLogin;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Log4j2
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class ClassTeacherAssignmentPlanServiceImpl implements ClassTeacherAssignmentPlanService{
    private ClassTeacherAssignmentPlanRepo classTeacherAssignmentPlanRepo;
    private ClassTeacherAssignmentClassInfoRepo classTeacherAssignmentClassInfoRepo;
    @Override
    public ClassTeacherAssignmentPlan create(UserLogin u, ClassTeacherAssignmentPlanCreateModel input) {
        ClassTeacherAssignmentPlan classTeacherAssignmentPlan = new ClassTeacherAssignmentPlan();
        classTeacherAssignmentPlan.setPlanName(input.getPlanName());
        classTeacherAssignmentPlan.setCreatedStamp(new Date());
        classTeacherAssignmentPlan.setCreatedByUserLoginId(u.getUserLoginId());
        classTeacherAssignmentPlan = classTeacherAssignmentPlanRepo.save(classTeacherAssignmentPlan);
        return classTeacherAssignmentPlan;
    }

    @Override
    public List<ClassTeacherAssignmentPlan> findAll() {
        List<ClassTeacherAssignmentPlan> classTeacherAssignmentPlans = classTeacherAssignmentPlanRepo.findAll();
        return classTeacherAssignmentPlans;
    }

    @Override
    public ClassTeacherAssignmentPlanDetailModel getClassTeacherAssignmentPlanDetail(UUID planId) {
        ClassTeacherAssignmentPlan classTeacherAssignmentPlan = classTeacherAssignmentPlanRepo.findById(planId).orElse(null);
        ClassTeacherAssignmentPlanDetailModel classTeacherAssignmentPlanDetailModel = new ClassTeacherAssignmentPlanDetailModel();
        classTeacherAssignmentPlanDetailModel.setPlanId(classTeacherAssignmentPlan.getPlanId());
        classTeacherAssignmentPlanDetailModel.setPlanName(classTeacherAssignmentPlan.getPlanName());
        classTeacherAssignmentPlanDetailModel.setCreatedByUserLoginId(classTeacherAssignmentPlan.getCreatedByUserLoginId());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String dateTimeStr = dateFormat.format(classTeacherAssignmentPlan.getCreatedStamp());
        classTeacherAssignmentPlanDetailModel.setCreatedDate(dateTimeStr);

        return classTeacherAssignmentPlanDetailModel;
    }

    @Override
    public List<ClassTeacherAssignmentClassInfo> findAllClassTeacherAssignmentClassByPlanId(UUID planId) {
        List<ClassTeacherAssignmentClassInfo> classTeacherAssignmentClassInfos =
            classTeacherAssignmentClassInfoRepo.findAllByPlanId(planId);

        return classTeacherAssignmentClassInfos;
    }
}
