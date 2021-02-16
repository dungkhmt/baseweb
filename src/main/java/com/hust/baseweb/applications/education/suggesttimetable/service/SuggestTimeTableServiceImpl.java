package com.hust.baseweb.applications.education.suggesttimetable.service;

import com.hust.baseweb.applications.education.exception.SimpleResponse;
import com.hust.baseweb.applications.education.suggesttimetable.entity.EduClass;
import com.hust.baseweb.applications.education.suggesttimetable.enums.EShift;
import com.hust.baseweb.applications.education.suggesttimetable.repo.IClassRepo;
import com.hust.baseweb.applications.education.suggesttimetable.repo.ICourseRepo;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.Iterator;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class SuggestTimeTableServiceImpl implements ISuggestTimeTableService {

    private ICourseRepo courseRepo;

    private IClassRepo classRepo;


    @Override
    public SimpleResponse uploadTimetable(MultipartFile file) throws IOException {
        HashMap<String,Integer> input = new HashMap<>();
        FileInputStream inputStream = (FileInputStream) file.getInputStream();
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt(0);
        sheet.removeRow(sheet.getRow(0));
        Iterator<Row> rowIterator = sheet.iterator();
        Row row1 = rowIterator.next();
        Iterator<Cell> cellIterator = row1.cellIterator();

        int i = 0;
        while (cellIterator.hasNext()){
            Cell cell = cellIterator.next();
            String s = cell.getStringCellValue();
            input.put(s,i);
            i = i + 1;
        }

        while (rowIterator.hasNext()){
            Row row = rowIterator.next();
            Integer classId = (Integer) Convert.covertInt(row.getCell(input.get("Mã_lớp")).getStringCellValue());
            Integer attachedClassId = (Integer) Convert.covertInt(row.getCell(input.get("Mã_lớp_kèm")).getStringCellValue());
            String courseId = Convert.convertString( row.getCell(input.get("Mã_HP")).getStringCellValue());
            Integer credit = (Integer) Convert.convertCredit(row.getCell(input.get("Khối_lượng")).getStringCellValue());
            String note = Convert.convertString(row.getCell(input.get("Ghi_chú")).getStringCellValue());
            DayOfWeek dayOfWeek = Convert.covertDayOfWeek(row.getCell(input.get("Thứ")).getStringCellValue());
            Integer startTime = (Integer) Convert.covertStartTime(row.getCell(input.get("Thời_gian")).getStringCellValue());
            Integer endTime = (Integer) Convert.covertEndTime(row.getCell(input.get("Thời_gian")).getStringCellValue());
            EShift Shift = Convert.convertShift(row.getCell(input.get("Kíp")).getStringCellValue());
            String weeks = Convert.convertWeeks(row.getCell(input.get("Tuần")).getStringCellValue());
            String room = Convert.convertString(row.getCell(input.get("Phòng")).getStringCellValue());
            boolean needExperiment = Convert.covertExperiment(row.getCell(input.get("Cần_TN")).getStringCellValue());
            Integer numRegistration = (Integer) Convert.covertInt(row.getCell(input.get("SLĐK")).getStringCellValue());
            Integer maxQuantity = (Integer) Convert.covertInt(row.getCell(input.get("SL_Max")).getStringCellValue());
            String status = Convert.convertString(row.getCell(input.get("Trạng_thái")).getStringCellValue());
            String classType = Convert.convertString(row.getCell(input.get("Loại_lớp")).getStringCellValue());
            String managementId = Convert.convertString(row.getCell(input.get("Mã_QL")).getStringCellValue());

            EduClass eduClass = new EduClass(classId, attachedClassId, courseId, credit,
                                             note, dayOfWeek, startTime, endTime, Shift, weeks, room, needExperiment, numRegistration,
                                             maxQuantity, status, classType, managementId);

            classRepo.save(eduClass);



        }
        System.out.println("--------------- FindAll -----------------");
        return null;

    }



}
