package com.hust.baseweb.applications.education.suggesttimetable.service;

import com.hust.baseweb.applications.education.exception.SimpleResponse;
import com.hust.baseweb.applications.education.suggesttimetable.entity.EduClass;
import com.hust.baseweb.applications.education.suggesttimetable.entity.EduCourse;
import com.hust.baseweb.applications.education.suggesttimetable.enums.EDepartment;
import com.hust.baseweb.applications.education.suggesttimetable.enums.EShift;
import com.hust.baseweb.applications.education.suggesttimetable.repo.EduClassRepo;
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

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            EduClass eduClass = new EduClass(
                EduClass.normalizeInteger(row.getCell(listInputClass.get("MÃ_LỚP"))),
                EduClass.normalizeInteger(row.getCell(listInputClass.get("MÃ_LỚP_KÈM"))),
                EduClass.normalizeString(row.getCell(listInputClass.get("MÃ_HP"))),
                EduClass.normalizeFisrt(row.getCell(listInputClass.get("KHỐI_LƯỢNG"))),
                EduClass.normalizeString(row.getCell(listInputClass.get("GHI_CHÚ"))),
                EduClass.normalizeDayOfWeek(row.getCell(listInputClass.get("THỨ"))),
                EduClass.normalizeBeforeTime(row.getCell(listInputClass.get("THỜI_GIAN"))),
                EduClass.normalizeAfterTime(row.getCell(listInputClass.get("THỜI_GIAN"))),
                EduClass.normalizeShift(row.getCell(listInputClass.get("KÍP"))),
                EduClass.normalizeString(row.getCell(listInputClass.get("TUẦN"))),
                EduClass.normalizeString(row.getCell(listInputClass.get("PHÒNG"))),
                EduClass.normalizeBoolean(row.getCell(listInputClass.get("CẦN_TN"))),
                EduClass.normalizeInteger(row.getCell(listInputClass.get("SLĐK"))),
                EduClass.normalizeInteger(row.getCell(listInputClass.get("SL_MAX"))),
                EduClass.normalizeString(row.getCell(listInputClass.get("TRẠNG_THÁI"))),
                EduClass.normalizeString(row.getCell(listInputClass.get("LOẠI_LỚP"))),
                EduClass.normalizeString(row.getCell(listInputClass.get("MÃ_QL")))
            );
            listClassMongo.add(eduClass);
            if(listClassMongo.size() == 100){
                classRepo.saveAll(listClassMongo);
                listClassMongo = new ArrayList<>();
            }
            EduCourse eduCourse = new EduCourse(
                EduCourse.normalizeString(row.getCell(listInputCourse.get("MÃ_HP"))),
                EduCourse.normalizeString(row.getCell(listInputCourse.get("TÊN_HP"))),
                EduCourse.normalizeString(row.getCell(listInputCourse.get("TÊN_HP_TIẾNG_ANH"))),
                EduCourse.normalizeDepartment(row.getCell(listInputCourse.get("KHOA_VIỆN")))
            );
            listCourseMongo.add(eduCourse);

        }

        classRepo.saveAll(listClassMongo);
        courseRepo.saveAll(listCourseMongo);

        return null;
    }


}
