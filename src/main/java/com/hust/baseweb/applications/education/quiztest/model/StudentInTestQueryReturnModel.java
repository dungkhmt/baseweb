package com.hust.baseweb.applications.education.quiztest.model;

import java.io.Serializable;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentInTestQueryReturnModel implements Serializable{
    String userLoginId;
    String testId;
    String fullName;
    String email;
    String statusId;
    String testGroupCode;

    @Override
    public String toString() {
        return "[TestId: " + testId + 
        ", UserLoginId: " + userLoginId + 
        ", FullName: " + fullName + 
        ", Email: " + email +
        ", Group: " + testGroupCode +
               "]";
    }

}
