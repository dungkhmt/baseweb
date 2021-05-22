package com.hust.baseweb.applications.education.quiztest.service;

import java.util.List;

import com.hust.baseweb.applications.education.quiztest.entity.EduQuizTest;
import com.hust.baseweb.applications.education.quiztest.entity.EduTestQuizGroup;
import com.hust.baseweb.applications.education.quiztest.model.EduQuizTestModel;
import com.hust.baseweb.applications.education.quiztest.model.QuizTestCreateInputModel;
import com.hust.baseweb.applications.education.quiztest.model.StudentInTestQueryReturnModel;
import com.hust.baseweb.applications.education.quiztest.model.quitestgroupquestion.AutoAssignQuestion2QuizTestGroupInputModel;
import com.hust.baseweb.applications.education.quiztest.model.quiztestgroup.AutoAssignParticipants2QuizTestGroupInputModel;
import com.hust.baseweb.applications.education.quiztest.model.quiztestgroup.QuizTestGroupInfoModel;
import com.hust.baseweb.entity.Person;
import com.hust.baseweb.entity.UserLogin;

import java.util.List;

public interface QuizTestService {
    public EduQuizTest save(QuizTestCreateInputModel input, UserLogin user);
    public List<EduQuizTest> getAllTestByCreateUser(String userLoginId);
    public List<StudentInTestQueryReturnModel> getAllStudentInTest(String testId);
    public List<EduQuizTestModel> getListQuizByUserId(String userLoginId);

    public boolean autoAssignParticipants2QuizTestGroup(AutoAssignParticipants2QuizTestGroupInputModel input);

    public boolean autoAssignQuestion2QuizTestGroup(AutoAssignQuestion2QuizTestGroupInputModel input);
    public Integer rejectStudentsInTest(String testId, String[] userLoginId);
    public EduQuizTest getQuizTestById(String testId);

    public Integer acceptStudentsInTest(String testId, String[] userLoginId);
    public List<QuizTestGroupInfoModel> getQuizTestGroupsInfoByTestId(String testId);
    public Integer deleteQuizTestGroups(String testId, String[] listQuizTestGroupId);

}
