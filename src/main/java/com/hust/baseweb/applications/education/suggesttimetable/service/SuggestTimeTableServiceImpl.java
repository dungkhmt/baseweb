package com.hust.baseweb.applications.education.suggesttimetable.service;

import com.hust.baseweb.applications.education.exception.SimpleResponse;
import com.hust.baseweb.applications.education.suggesttimetable.entity.EduClass;
import com.hust.baseweb.applications.education.suggesttimetable.entity.EduCourse;
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

import java.io.IOException;
import java.io.InputStream;
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
        HashMap<String, Short> mapClassColumn = new HashMap<>();
        HashMap<String, Short> mapCourseColumn = new HashMap<>();

        List<EduClass> classes = new ArrayList<>();
        List<EduCourse> courses = new ArrayList<>();

        InputStream inputStream = file.getInputStream();
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt(0);

        sheet.removeRow(sheet.getRow(0));
        Iterator<Row> rowIterator = sheet.iterator();
        Row headerRow = rowIterator.next();
        Iterator<Cell> headerCell = headerRow.cellIterator();

        // Extract header.
        short i = 0;
        while (headerCell.hasNext()) {
            Cell cell = headerCell.next();
            String columnName = StringUtils.upperCase(StringUtils.deleteWhitespace(cell.getStringCellValue()));

            switch (columnName) {
                case "MÃ_HP":
                    mapClassColumn.put(columnName, i);
                    mapCourseColumn.put(columnName, i);
                case "TÊN_HP":
                case "TÊN_HP_TIẾNG_ANH":
                case "KHOA_VIỆN":
                    mapCourseColumn.put(columnName, i);
                default:
                    mapClassColumn.put(columnName, i);
            }

            i++;
        }

        // Extract data.
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            // Extract class.
            EduClass eduClass = new EduClass(
                EduClass.normalizeInt(row.getCell(mapClassColumn.get("MÃ_LỚP"))),
                EduClass.normalizeInt(row.getCell(mapClassColumn.get("MÃ_LỚP_KÈM"))),
                EduClass.normalizeStr(row.getCell(mapClassColumn.get("MÃ_HP"))),
                EduClass.normalizeFisrt(row.getCell(mapClassColumn.get("KHỐI_LƯỢNG"))),
                EduClass.normalizeStr(row.getCell(mapClassColumn.get("GHI_CHÚ"))),
                EduClass.normalizeDayOfWeek(row.getCell(mapClassColumn.get("THỨ"))),
                EduClass.normalizeBeforeTime(row.getCell(mapClassColumn.get("THỜI_GIAN"))),
                EduClass.normalizeAfterTime(row.getCell(mapClassColumn.get("THỜI_GIAN"))),
                EduClass.normalizeShift(row.getCell(mapClassColumn.get("KÍP"))),
                EduClass.normalizeStr(row.getCell(mapClassColumn.get("TUẦN"))),
                EduClass.normalizeStr(row.getCell(mapClassColumn.get("PHÒNG"))),
                EduClass.normalizeBool(row.getCell(mapClassColumn.get("CẦN_TN"))),
                EduClass.normalizeInt(row.getCell(mapClassColumn.get("SLĐK"))),
                EduClass.normalizeInt(row.getCell(mapClassColumn.get("SL_MAX"))),
                EduClass.normalizeStr(row.getCell(mapClassColumn.get("TRẠNG_THÁI"))),
                EduClass.normalizeStr(row.getCell(mapClassColumn.get("LOẠI_LỚP"))),
                EduClass.normalizeStr(row.getCell(mapClassColumn.get("MÃ_QL")))
            );

            classes.add(eduClass);

            /*if (classes.size() == 100) {
                classRepo.saveAll(classes);
                classes = new ArrayList<>();
            }*/

            // Extract course.
            EduCourse eduCourse = new EduCourse(
                EduCourse.normalizeString(row.getCell(mapCourseColumn.get("MÃ_HP"))),
                EduCourse.normalizeString(row.getCell(mapCourseColumn.get("TÊN_HP"))),
                EduCourse.normalizeString(row.getCell(mapCourseColumn.get("TÊN_HP_TIẾNG_ANH"))),
                EduCourse.normalizeDept(row.getCell(mapCourseColumn.get("KHOA_VIỆN")))
            );

            // FIXME: handle duplicate courses
            courses.add(eduCourse);
        }

        // Save in batches.
        saveClassesInBatch(classes);
        saveCoursesInBatch(courses);

        return new SimpleResponse(200, null, null);
    }

    /**
     * Upsert
     *
     * @param classes
     */
    private void saveClassesInBatch(List<EduClass> classes) {
        // TODO: use bulk operation with mongo template to complete this method.
    }

    /**
     * @param courses
     */
    private void saveCoursesInBatch(List<EduCourse> courses) {
        // TODO: use bulk operation with mongo template to complete this method.
    }
}
