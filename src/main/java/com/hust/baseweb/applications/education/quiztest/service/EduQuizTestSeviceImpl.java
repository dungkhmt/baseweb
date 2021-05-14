package com.hust.baseweb.applications.education.quiztest.service;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

import java.util.Date;

import com.hust.baseweb.applications.education.quiztest.entity.EduQuizTest;
import com.hust.baseweb.applications.education.quiztest.model.QuizTestCreateInputModel;
import com.hust.baseweb.applications.education.quiztest.repo.EduQuizTestRepo;
import com.hust.baseweb.entity.UserLogin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class EduQuizTestSeviceImpl implements QuizTestService{

    EduQuizTestRepo repo;

    @Override
    public EduQuizTest save(QuizTestCreateInputModel input, UserLogin user) {
        EduQuizTest newRecord = new EduQuizTest();

        newRecord.setTestName(input.getTestName());
        newRecord.setCourseId(input.getCourseId());
        newRecord.setCreatedByUserLoginId(user.getUserLoginId());
        newRecord.setDuration(input.getDuration());
        newRecord.setScheduleDatetime(input.getScheduleDatetime());
        newRecord.setStatusId("");
        newRecord.setTestId(input.getTestId());
        newRecord.setClassId(input.getClassId());
        newRecord.setCreatedStamp(new Date());
        newRecord.setLastUpdatedStamp(new Date());

        return repo.save(newRecord);
    }
}
