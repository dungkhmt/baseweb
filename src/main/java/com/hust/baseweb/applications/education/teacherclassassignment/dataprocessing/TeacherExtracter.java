package com.hust.baseweb.applications.education.teacherclassassignment.dataprocessing;

import com.google.gson.Gson;
import com.hust.baseweb.applications.education.teacherclassassignment.model.AlgoTeacherIM;
import com.hust.baseweb.applications.education.teacherclassassignment.model.Course4Teacher;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TeacherExtracter implements IExtracter {

    private Map<String, Integer> indexOfColumn;

    private FileInputStream file;

    private XSSFWorkbook workbook;

    private XSSFSheet sheet;

    private Iterator<Row> rowIterator;

    private ArrayList<AlgoTeacherIM> teachers = new ArrayList<>();

    public TeacherExtracter(FileInputStream file) throws IOException {
        indexOfColumn = new HashMap<>();
        this.file = file;
        workbook = new XSSFWorkbook(file);

    }

    public static void main(String[] args) {
        try {
            // Modify file path.
            TeacherExtracter extracter = new TeacherExtracter(new FileInputStream(new File(
                "D:\\sscm\\basewe\\src\\main\\java\\com\\hust\\baseweb\\applications\\education\\teacherclassassignment\\dataprocessing\\data\\course4teacher_20191.xlsx")));

            extracter.getIndexOfColumnIn("Sheet1");
            extracter.extract();

            // Write to file.
            // Modify file path.
            BufferedWriter writer = new BufferedWriter(new FileWriter(
                "D:\\sscm\\basewe\\src\\main\\java\\com\\hust\\baseweb\\applications\\education\\teacherclassassignment\\dataprocessing\\data\\teachers.txt"));

            writer.write(extracter.toJson());

            writer.close();
            extracter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getIndexOfColumnIn(String sheetName) {
        sheet = workbook.getSheet(sheetName);
        rowIterator = sheet.iterator();
        Row row = rowIterator.next();

        for (int i = 0; i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);

            switch (cell.getStringCellValue().toLowerCase()) {
                case "email":
                    if (!indexOfColumn.containsKey("id")) {
                        indexOfColumn.put("id", i);
                    }
                    break;
                case "teacher":
                    if (!indexOfColumn.containsKey("name")) {
                        indexOfColumn.put("name", i);
                    }
                    break;
                case "course_id":
                    if (!indexOfColumn.containsKey("course_id")) {
                        indexOfColumn.put("course_id", i);
                    }
                    break;
                case "class_type":
                    if (!indexOfColumn.containsKey("type")) {
                        indexOfColumn.put("type", i);
                    }
                    break;
            }
        }
    }

    @Override
    public void extract() {
        Row row;
        String preTeacher = "";

        while (rowIterator.hasNext()) {
            row = rowIterator.next();
            AlgoTeacherIM teacher;
            String id = row.getCell(indexOfColumn.get("id")).getStringCellValue();

            if (preTeacher.equalsIgnoreCase(id)) {
                teacher = teachers.get(teachers.size() - 1);
            } else {
                teacher = new AlgoTeacherIM();

                teacher.setId(id);
                teacher.setName(row.getCell(indexOfColumn.get("name")).getStringCellValue());
                teacher.setClasses(new ArrayList<>());

                preTeacher = id;
                teachers.add(teacher);
            }

            teacher.getClasses().add(
                new Course4Teacher(
                    row.getCell(indexOfColumn.get("course_id")).getStringCellValue(),
                    row.getCell(indexOfColumn.get("type")).getStringCellValue()
                )
            );
        }
    }

    @Override
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(teachers);
    }

    private void close() throws IOException {
        file.close();
    }
}
