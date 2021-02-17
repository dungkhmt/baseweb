package com.hust.baseweb.applications.education.suggesttimetable.service;

import com.hust.baseweb.applications.education.exception.SimpleResponse;
import com.hust.baseweb.applications.education.suggesttimetable.entity.EduClass;
import com.hust.baseweb.applications.education.suggesttimetable.entity.EduCourse;
import com.hust.baseweb.applications.education.suggesttimetable.enums.EDepartment;
import com.hust.baseweb.applications.education.suggesttimetable.enums.EShift;
import com.hust.baseweb.applications.education.suggesttimetable.repo.IClassRepo;
import com.hust.baseweb.applications.education.suggesttimetable.repo.ICourseRepo;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class SuggestTimeTableServiceImpl implements ISuggestTimeTableService {

    private ICourseRepo courseRepo;

    private IClassRepo classRepo;


    @Override
    public SimpleResponse uploadTimetable(MultipartFile file) throws IOException {
        HashMap<String, Comparable> listCourse = new HashMap<>();
        HashMap<String, Comparable> listClass = new HashMap<>();

        HashMap<String, Integer> listInputClass = new HashMap<>();
        HashMap<String, Integer> listInputCourse = new HashMap<>();

        List<EduClass> listClassMongo = new ArrayList<>();
        List<EduCourse> listCourseMongo = new ArrayList<>();


        FileInputStream inputStream = (FileInputStream) file.getInputStream();
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt(0);
        sheet.removeRow(sheet.getRow(0));
        Iterator<Row> rowIterator = sheet.iterator();
        Row row1 = rowIterator.next();
        Iterator<Cell> cellIterator = row1.cellIterator();

        int i = 0;
//        System.out.println("ok");
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            String s = cell.getStringCellValue();
            s = StringUtils.deleteWhitespace(s);
            s = StringUtils.upperCase(s);
            switch (s) {
                case "MÃ_HP":
                    listInputClass.put(s, i);
                    listInputCourse.put(s, i);
                case "TÊN_HP":
                case "TÊN_HP_TIẾNG_ANH":
                case "KHOA_VIỆN":
                    listInputCourse.put(s, i);
                default:
                    listInputClass.put(s, i);
            }
            i = i + 1;
        }
        listInputClass.put("BẮT_ĐẦU", listInputClass.get("THỜI_GIAN"));
        listInputClass.put("KẾT_THÚC", listInputClass.get("THỜI_GIAN"));
        listInputClass.remove("BUỔI_SỐ");
        listInputClass.remove("KT");
        listInputClass.remove("BĐ");
        listInputClass.remove("BĐ");
        listInputClass.remove("ĐỢT_MỞ");
        listInputClass.remove("KỲ");

//        System.out.println("ok");
        for (String s : listInputClass.keySet()) {
            System.out.println(s + listInputClass.get(s));
        }
        System.out.println("------------");

        for (String s : listInputCourse.keySet()) {
            System.out.println(s + listInputCourse.get(s));
        }

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            for (String s : listInputClass.keySet()) {
                listClass.put(s, EduClass.normalize(row.getCell(listInputClass.get(s)), s));
            }

            EduClass eduClass = new EduClass(
                (Integer) listClass.get("MÃ_LỚP"),
                (Integer) listClass.get("MÃ_LỚP_KÈM"),
                (String) listClass.get("MÃ_HP"),
                (Integer) listClass.get("KHỐI_LƯỢNG"),
                (String) listClass.get("GHI_CHÚ"),
                (DayOfWeek) listClass.get("THỨ"),
                (Integer) listClass.get("BẮT_ĐẦU"),
                (Integer) listClass.get("KẾT_THÚC"),
                (EShift) listClass.get("KÍP"),
                (String) listClass.get("TUẦN"),
                (String) listClass.get("PHÒNG"),
                (boolean) listClass.get("CẦN_TN"),
                (Integer) listClass.get("SLĐK"),
                (Integer) listClass.get("SL_MAX"),
                (String) listClass.get("TRẠNG_THÁI"),
                (String) listClass.get("LOẠI_LỚP"),
                (String) listClass.get("MÃ_QL")
            );
            listClassMongo.add(eduClass);

            for (String s : listInputCourse.keySet()) {
                listCourse.put(s, EduCourse.normalize(row.getCell(listInputCourse.get(s)), s));
            }

            EduCourse eduCourse = new EduCourse(
                (String) listCourse.get("MÃ_HP"),
                (String) listCourse.get("TÊN_HP"),
                (String) listCourse.get("TÊN_HP_TIẾNG_ANH"),
                (EDepartment) listCourse.get("KHOA_VIỆN")
            );
            listCourseMongo.add(eduCourse);


        }

        classRepo.saveAll(listClassMongo);
        courseRepo.saveAll(listCourseMongo);

        System.out.println("--------------- FindAll -----------------");
        return null;
    }


}
