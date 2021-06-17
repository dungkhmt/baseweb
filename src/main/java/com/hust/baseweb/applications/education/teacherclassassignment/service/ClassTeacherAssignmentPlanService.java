package com.hust.baseweb.applications.education.teacherclassassignment.service;

import com.hust.baseweb.applications.education.teacherclassassignment.entity.ClassTeacherAssignmentClassInfo;
import com.hust.baseweb.applications.education.teacherclassassignment.entity.ClassTeacherAssignmentPlan;
import com.hust.baseweb.applications.education.teacherclassassignment.model.ClassTeacherAssignmentPlanCreateModel;
import com.hust.baseweb.applications.education.teacherclassassignment.model.ClassTeacherAssignmentPlanDetailModel;
import com.hust.baseweb.entity.UserLogin;

import java.util.List;
import java.util.UUID;

public interface ClassTeacherAssignmentPlanService {
    public ClassTeacherAssignmentPlan create(UserLogin u, ClassTeacherAssignmentPlanCreateModel input);
    public List<ClassTeacherAssignmentPlan> findAll();
    public ClassTeacherAssignmentPlanDetailModel getClassTeacherAssignmentPlanDetail(UUID planId);

    public List<ClassTeacherAssignmentClassInfo> findAllClassTeacherAssignmentClassByPlanId(UUID planId);

}
