package com.hust.baseweb.applications.education.teacherclassassignment.dataprocessing;

import com.google.gson.Gson;
import com.hust.baseweb.applications.education.teacherclassassignment.model.AlgoClassIM;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ClassExtracter implements IExtracter {

    private Map<String, Integer> indexOfColumn;

    private FileInputStream file;

    private XSSFWorkbook workbook;

    private XSSFSheet sheet;

    private Iterator<Row> rowIterator;

    private ArrayList<AlgoClassIM> classes = new ArrayList<>();

    public ClassExtracter(FileInputStream file) throws IOException {
        indexOfColumn = new HashMap<>();
        this.file = file;
        workbook = new XSSFWorkbook(file);

    }

    public static void main(String[] args) {
        try {
            // Modify file path.
            ClassExtracter extracter = new ClassExtracter(new FileInputStream(new File(
                "D:\\sscm\\basewe\\src\\main\\java\\com\\hust\\baseweb\\applications\\education\\teacherclassassignment\\dataprocessing\\data\\CNTT_20201.xlsx")));

            extracter.getIndexOfColumnIn("Sheet1");
            extracter.extract();

            // Write to file.
            // Modify file path.
            BufferedWriter writer = new BufferedWriter(new FileWriter(
                "D:\\sscm\\basewe\\src\\main\\java\\com\\hust\\baseweb\\applications\\education\\teacherclassassignment\\dataprocessing\\data\\classes_TN.txt"));

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
                case "mã lớp":
                    if (!indexOfColumn.containsKey("id")) {
                        indexOfColumn.put("id", i);
                    }
                    break;
                case "classtype":
                    if (!indexOfColumn.containsKey("classType")) {
                        indexOfColumn.put("classType", i);
                    }
                    break;
                case "mã hp":
                    if (!indexOfColumn.containsKey("course_id")) {
                        indexOfColumn.put("course_id", i);
                    }
                    break;
                case "session":
                    if (!indexOfColumn.containsKey("timetable")) {
                        indexOfColumn.put("timetable", i);
                    }
                    break;
            }
        }
    }

    @Override
    public void extract() {
        Row row;

        while (rowIterator.hasNext()) {
            row = rowIterator.next();
            AlgoClassIM classIM = new AlgoClassIM();

            classIM.setId((int) row.getCell(indexOfColumn.get("id")).getNumericCellValue());
            classIM.setClassType(row.getCell(indexOfColumn.get("classType")).getStringCellValue());
            classIM.setCourseId(row.getCell(indexOfColumn.get("course_id")).getStringCellValue());
            classIM.setTimetable(row.getCell(indexOfColumn.get("timetable")).getStringCellValue());

            classes.add(classIM);
        }
    }

    @Override
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(classes);
    }

    private void close() throws IOException {
        file.close();
    }
}
