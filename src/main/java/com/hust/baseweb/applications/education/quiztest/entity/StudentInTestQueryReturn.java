package com.hust.baseweb.applications.education.quiztest.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class StudentInTestQueryReturn implements Serializable{
    @Id
    @Column(name="user_login_id")
    String userLoginId;

    @Column(name="test_id")
    String testId;

    @Column(name="full_name")
    String fullName;

    @Column(name="email")
    String email;
}
