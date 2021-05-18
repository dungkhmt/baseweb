package com.hust.baseweb.applications.education.quiztest.repo;

import com.hust.baseweb.applications.education.quiztest.entity.EduQuizTest;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EduQuizTestRepo extends JpaRepository<EduQuizTest, String>{
    
}
