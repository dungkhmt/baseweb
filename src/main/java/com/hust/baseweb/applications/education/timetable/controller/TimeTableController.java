package com.hust.baseweb.applications.education.timetable.controller;

import com.hust.baseweb.applications.education.timetable.entity.TimeTable;
import com.hust.baseweb.applications.education.timetable.model.TimeTableModelInput;
import com.hust.baseweb.applications.education.timetable.service.TimeTableService;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.security.Principal;
import java.util.List;

@RestController
@Log4j2
public class TimeTableController {

    @Autowired
    private TimeTableService timeTableService;

    @GetMapping("/view")
    public ResponseEntity<?> view(Principal principal)
    {
        List<TimeTable> list = timeTableService.findAll();
        log.info("Showing!");
        return ResponseEntity.ok().body(list);
    }
    @PostMapping("/upload")
    public ResponseEntity<?> upload(Principal principal, @RequestParam("file") MultipartFile file) {
        log.info("Successfully upload " + file.getOriginalFilename() + " !");
        try {
            InputStream fileInputStream = file.getInputStream();
            String fileName = file.getOriginalFilename();
            String filenameExtensions = fileName.substring(fileName.length() - 4);

            if (!"xlsx".equals(filenameExtensions)) {
                log.info(fileName + " is not a xlsx file !");
                return ResponseEntity.ok().body("OK");
            } else {
                log.info("Allocating " + fileName + " !");
                final int cid = 0;
                final int acid = 1;
                final int course_cod = 2;
                final int course_name = 3;
                final int capacity = 4;
                final int note = 5;
                final int course_week = 6;
                final int day_of_week = 7;
                final int course_time = 8;
                final int start_section = 9;
                final int finish_section = 10;
                final int shift = 11;
                final int room = 12;
                final int type_of_class = 13;
                final int total = 14;
                final int course_session = 15;
                final int state = 16;
                final int experiment = 17;
                final int mid = 18;
                final int college = 19;
                final int course_period = 20;

                Workbook workbook = new XSSFWorkbook(fileInputStream);
                Sheet sheet = workbook.getSheetAt(0);
                TimeTableModelInput input = new TimeTableModelInput();
                for (Row row : sheet) {
                    if (row.getRowNum() == 0) {
                        continue;
                    }
                    for (Cell cell : row) {
                        int index = cell.getColumnIndex();
                        switch (index) {
                            case cid:
                                input.setCid((int) cell.getNumericCellValue());
                                break;
                            case acid:
                                input.setAcid((int) cell.getNumericCellValue());
                                break;
                            case course_cod:
                                input.setCourse_code(cell.getStringCellValue());
                                break;
                            case course_name:
                                input.setCourse_name(cell.getStringCellValue());
                                break;
                            case capacity:
                                input.setCapacity(cell.getStringCellValue());
                                break;
                            case note:
                                input.setNote(cell.getStringCellValue());
                                break;
                            case course_week:
                                input.setCourse_week(cell.getStringCellValue());
                                break;
                            case day_of_week:
                                input.setDay_of_week((int) cell.getNumericCellValue());
                                break;
                            case course_time:
                                input.setCourse_time(cell.getStringCellValue());
                                break;
                            case start_section:
                                input.setStart_section((int) cell.getNumericCellValue());
                                break;
                            case finish_section:
                                input.setFinish_section((int) cell.getNumericCellValue());
                                break;
                            case shift:
                                input.setShift(cell.getStringCellValue());
                                break;
                            case room:
                                input.setRoom(cell.getStringCellValue());
                                break;
                            case type_of_class:
                                input.setType_of_class(cell.getStringCellValue());
                                break;
                            case total:
                                input.setTotal((int) cell.getNumericCellValue());
                                break;
                            case course_session:
                                input.setCourse_session((int) cell.getNumericCellValue());
                                break;
                            case state:
                                input.setState(cell.getStringCellValue());
                                break;
                            case experiment:
                                input.setExperiment(cell.getStringCellValue());
                                break;
                            case mid:
                                input.setMid((int) cell.getNumericCellValue());
                                break;
                            case college:
                                input.setCollege(cell.getStringCellValue());
                                break;
                            case course_period:
                                input.setCourse_period(cell.getStringCellValue());
                                break;
                        }
                    }
                    timeTableService.save(input);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(null);
    }
}
