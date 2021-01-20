package com.hust.baseweb.algorithmsapi.stafftaskassignment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StaffTaskAssignmentInput {
    private List<Staff> staffs;
    private List<Task> tasks;
    private List<Distance> distances;
    public void loadData(String filename){
        try {
            tasks = new ArrayList();
            staffs = new ArrayList();
            distances = new ArrayList();

            //XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(new File("D:/projects/baseweb/data/stafftaskassignment/input.xlsx")));
            XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(new File(filename)));
            XSSFSheet sheet = wb.getSheet("Tasks");
            Iterator<Row> rowIterator = sheet.rowIterator();
            rowIterator.next();

            while(rowIterator.hasNext()){
                Row r = rowIterator.next();
                int taskID;
                int duration;
                String locationID;
                Cell c = r.getCell(0);
                taskID = (int)c.getNumericCellValue();
                c = r.getCell(1);
                duration = (int)c.getNumericCellValue();
                c = r.getCell(2);
                locationID = c.getStringCellValue();
                c = r.getCell(3);
                String typeID = "";
                typeID = c.getStringCellValue();
                tasks.add(new Task(taskID + "",typeID,duration,locationID));
                System.out.println(taskID + "\t" + duration + "\t" + locationID);
            }

            sheet = wb.getSheet("Staffs");
            rowIterator = sheet.iterator();
            rowIterator.next();
            while(rowIterator.hasNext()){
                Row r = rowIterator.next();
                Cell c = r.getCell(0);
                String staffId = (int)c.getNumericCellValue() + "";
                c = r.getCell(1);
                String[] a_skills = c.getStringCellValue().split(",");
                List<String> skills = new ArrayList();
                for(String s: a_skills)
                    skills.add(s.trim());
                staffs.add(new Staff(staffId,skills));
            }
            for(Staff s: staffs){
                System.out.println(s.toString());
            }
            sheet = wb.getSheet("Distances");
            rowIterator = sheet.iterator();
            rowIterator.next();
            while(rowIterator.hasNext()){
                Row r = rowIterator.next();
                Cell c = r.getCell(0);
                String from = c.getStringCellValue();
                c = r.getCell(1);
                String to = c.getStringCellValue();
                c = r.getCell(2);
                int travelTime = (int)c.getNumericCellValue();
                distances.add(new Distance(from,to,travelTime));
            }
            for(Distance d: distances){
                System.out.println(d.toString());
            }
            System.out.println("OK");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
