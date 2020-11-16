package com.hust.baseweb.applications.education.teacherclassassignment.dataprocessing;

import com.google.gson.Gson;

import java.io.*;

public class Main {

    public static void main(String[] args) {
        try {
            // Modify file path.
            TeacherExtracter teacherExtracter = new TeacherExtracter(new FileInputStream(new File(
                "D:\\PersonalProjects\\IdeaProjects\\bca\\course4teacher_20191.xlsx")));
            ClassExtracter classExtracter = new ClassExtracter(new FileInputStream(new File(
                "D:\\PersonalProjects\\IdeaProjects\\bca\\CNTT_20201.xlsx")));

            teacherExtracter.getIndexOfColumnIn("Sheet1");
            teacherExtracter.extract();

            classExtracter.getIndexOfColumnIn("Sheet1");
            classExtracter.extract();

            // Write to file.
            ExtractDataOM extractDataOM = new ExtractDataOM();

            extractDataOM.setTeachers(teacherExtracter.getTeachers());
            extractDataOM.setClasses(classExtracter.getClasses());

            // Modify file path.
            BufferedWriter writer = new BufferedWriter(new FileWriter(
                "D:\\PersonalProjects\\IdeaProjects\\bca\\input_hour.json"));

            Gson gson = new Gson();

            writer.write(gson.toJson(extractDataOM));

            writer.close();
            teacherExtracter.close();
            classExtracter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
