package com.hust.baseweb.applications.education.teacherclassassignment.service;

import com.hust.baseweb.applications.education.teacherclassassignment.entity.ClassTeacherAssignmentPlan;
import com.hust.baseweb.applications.education.teacherclassassignment.model.ClassTeacherAssignmentPlanCreateModel;
import com.hust.baseweb.applications.education.teacherclassassignment.repo.ClassTeacherAssignmentPlanRepo;
import com.hust.baseweb.entity.UserLogin;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Log4j2
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class ClassTeacherAssignmentPlanServiceImpl implements ClassTeacherAssignmentPlanService{
    private ClassTeacherAssignmentPlanRepo classTeacherAssignmentPlanRepo;

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
}
