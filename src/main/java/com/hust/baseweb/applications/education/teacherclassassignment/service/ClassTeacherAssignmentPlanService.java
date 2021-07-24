package com.hust.baseweb.applications.education.teacherclassassignment.service;

import com.hust.baseweb.applications.education.teacherclassassignment.entity.*;
import com.hust.baseweb.applications.education.teacherclassassignment.model.*;
import com.hust.baseweb.entity.UserLogin;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface ClassTeacherAssignmentPlanService {

    public ClassTeacherAssignmentPlan create(UserLogin u, ClassTeacherAssignmentPlanCreateModel input);

    public List<ClassTeacherAssignmentPlan> findAll();

    public ClassTeacherAssignmentPlanDetailModel getClassTeacherAssignmentPlanDetail(UUID planId);

    //public List<ClassTeacherAssignmentClassInfo> findAllClassTeacherAssignmentClassByPlanId(UUID planId);
    public List<ClassInfoForAssignment2TeacherModel> findAllClassTeacherAssignmentClassByPlanId(UUID planId);

    public boolean extractExcelAndStoreDB(UUID planId, MultipartFile file);

    public List<EduTeacher> findAllTeachers();

    public List<TeacherForAssignmentPlan> findAllTeacherByPlanId(UUID planId);

    public boolean addTeacherToAssignmentPlan(UUID planId, String teacherList);

    public boolean removeTeacherFromAssignmentPlan(UUID planId, String teacherList);

    public boolean removeClassFromAssignmentPlan(UUID planId, String classList);

    public List<TeacherCourse> findAllTeacherCourse();

    public List<TeacherCourseForAssignmentPlan> findTeacherCourseOfPlan(UUID planId);

    public boolean extractExcelAndStoreDBTeacherCourse(UUID planId, String choice, MultipartFile file);

    public boolean autoAssignTeacher2Class(UUID planId);

    public List<ClassTeacherAssignmentSolutionModel> getNotAssignedClassSolution(UUID planId);

    public List<SuggestedTeacherForClass> getSuggestedTeacherForClass(String classId,UUID planId);

    public TeacherClassAssignmentSolution assignTeacherToClass(UserLogin u, AssignTeacherToClassInputModel input);

    public boolean removeClassTeacherAssignmentSolution(
        UserLogin u,
        RemoveClassTeacherAssignmentSolutionInputModel input
    );

    public boolean removeClassTeacherAssignmentSolutionList(UUID planId, String solutionItemList);

    public List<ClassesAssignedToATeacherModel> getClassesAssignedToATeacherSolution(UUID planId);
    public List<ClassesAssignedToATeacherModel> getClassesAssignedToATeacherSolutionDuplicateWhenMultipleFragmentTimeTable(UUID planId);

    public List<ClassTeacherAssignmentSolutionModel> getClassTeacherAssignmentSolution(UUID planId);

    public boolean addTeacherCourseToAssignmentPlan(UUID planId, String teacherCourseList);

    public boolean removeTeacherCourseFromAssignmentPlan(UUID planId, String teacherCourseList);

    public List<PairOfConflictTimetableClassModel> getPairOfConflictTimetableClass(UUID planId);

    public ClassTeacherAssignmentClassInfo updateClassForAssignment(
        UserLogin u,
        UpdateClassForAssignmentInputModel input
    );


    public TeacherForAssignmentPlan updateTeacherForAssignment(UserLogin u, UpdateTeacherForAssignmentInputModel input);

    public TeacherCourseForAssignmentPlan updateTeacherCourseForAssignmentPlan(UserLogin u, UpdateTeacherCoursePriorityForAssignmentPlanInputModel input);
}
