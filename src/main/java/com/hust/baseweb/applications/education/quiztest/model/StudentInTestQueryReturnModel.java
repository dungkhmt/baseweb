package com.hust.baseweb.applications.education.quiztest.model;

import java.io.Serializable;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentInTestQueryReturnModel implements Serializable{
    String userLoginId;
    String testId;
    String fullName;
    String email;
    String statusId;

    @Override
    public String toString() {
        return "[TestId: " + testId + 
        ", UserLoginId: " + userLoginId + 
        ", FullName: " + fullName + 
        ", Email: " + email + "]";
    }

}
