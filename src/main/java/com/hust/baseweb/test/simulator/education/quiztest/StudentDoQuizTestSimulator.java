package com.hust.baseweb.test.simulator.education.quiztest;

import com.hust.baseweb.test.simulator.Login;

public class StudentDoQuizTestSimulator {

    public static void main(String[] args){
        String token = Login.login("","");
        System.out.println("token = " + token);
    }
}
