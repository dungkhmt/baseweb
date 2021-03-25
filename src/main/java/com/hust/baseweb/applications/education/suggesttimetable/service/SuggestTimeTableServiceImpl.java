package com.hust.baseweb.applications.education.suggesttimetable.service;

import com.hust.baseweb.applications.education.exception.CustomExceptionExcel;
import com.hust.baseweb.applications.education.exception.SimpleResponse;
import com.hust.baseweb.applications.education.suggesttimetable.entity.EduClass;
import com.hust.baseweb.applications.education.suggesttimetable.entity.EduCourse;
import com.hust.baseweb.applications.education.suggesttimetable.enums.ErrorExcel;
import com.hust.baseweb.applications.education.suggesttimetable.model.EduClassOM;
import com.hust.baseweb.applications.education.suggesttimetable.model.GroupClassesOM;
import com.hust.baseweb.applications.education.suggesttimetable.repo.IClassRepo;
import com.hust.baseweb.applications.education.suggesttimetable.repo.ICourseRepo;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
@Lazy
@AllArgsConstructor
public class SuggestTimeTableServiceImpl implements ISuggestTimeTableService {

    private LinkedHashMap<CustomExceptionExcel, Integer> errorLists;

    private final ICourseRepo courseRepo;

    private final IClassRepo classRepo;

    @Override
    public SimpleResponse uploadTimetable(MultipartFile file) throws IOException {
        HashMap<String, Short> mapClassColumn = new HashMap<>();
        HashMap<String, Short> mapCourseColumn = new HashMap<>();

        List<EduClass> classes = new ArrayList<>();
        HashSet<EduCourse> courses = new HashSet<>();

        InputStream inputStream = file.getInputStream();
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt(0);

        sheet.removeRow(sheet.getRow(0));
        Iterator<Row> rowIterator = sheet.iterator();
        Row headerRow = rowIterator.next();
        Iterator<Cell> headerCell = headerRow.cellIterator();

        // Extract header.
        short i = 0;
        CellType cellType = headerCell.next().getCellType();
        if (cellType == CellType.BLANK || cellType == CellType._NONE) {
            headerRow = rowIterator.next();
            headerCell = headerRow.cellIterator();
        }
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
        int j = 3;
        while (rowIterator.hasNext()) {
            j = j + 1;
            Row row = rowIterator.next();
            try {

                EduClass clazz = new EduClass(
                    EduClass.normalizeInt(row.getCell(mapClassColumn.get("MÃ_LỚP"))),
                    EduClass.normalizeInt(row.getCell(mapClassColumn.get("MÃ_LỚP_KÈM"))),
                    EduClass.normalizeStr(row.getCell(mapClassColumn.get("MÃ_HP"))),
                    EduClass.normalizeFist(row.getCell(mapClassColumn.get("KHỐI_LƯỢNG"))),
                    EduClass.normalizeDefault(row.getCell(mapClassColumn.get("GHI_CHÚ"))),
                    EduClass.normalizeDayOfWeek(row.getCell(mapClassColumn.get("THỨ"))),
                    EduClass.normalizeBeforeTime(row.getCell(mapClassColumn.get("THỜI_GIAN"))),
                    EduClass.normalizeAfterTime(row.getCell(mapClassColumn.get("THỜI_GIAN"))),
                    EduClass.normalizeShift(row.getCell(mapClassColumn.get("KÍP"))),
                    EduClass.normalizeStr(row.getCell(mapClassColumn.get("TUẦN"))),
                    EduClass.normalizeStr(row.getCell(mapClassColumn.get("PHÒNG"))),
                    EduClass.normalizeBool(row.getCell(mapClassColumn.get("CẦN_TN"))),
                    EduClass.normalizeInt(row.getCell(mapClassColumn.get("SLĐK"))),
                    EduClass.normalizeInt(row.getCell(mapClassColumn.get("SL_MAX"))),
                    EduClass.normalizeDefault(row.getCell(mapClassColumn.get("TRẠNG_THÁI"))),
                    EduClass.normalizeDefault(row.getCell(mapClassColumn.get("LOẠI_LỚP"))),
                    EduClass.normalizeDefault(row.getCell(mapClassColumn.get("MÃ_QL")))
                );

                classes.add(clazz);

                // Extract course.
                EduCourse eduCourse = new EduCourse(
                    EduCourse.normalizeString(row.getCell(mapCourseColumn.get("MÃ_HP"))),
                    EduCourse.normalizeDefault(row.getCell(mapCourseColumn.get("TÊN_HP"))),
                    EduCourse.normalizeDefault(row.getCell(mapCourseColumn.get("TÊN_HP_TIẾNG_ANH"))),
                    EduCourse.normalizeDept(row.getCell(mapCourseColumn.get("KHOA_VIỆN"))));
                courses.add(eduCourse);
            } catch (CustomExceptionExcel e) {
                errorLists.put(e, j);
            }
        }

        if (errorLists.size() > 0) {
            LinkedHashMap<CustomExceptionExcel, Integer> copy = errorLists;
            errorLists = new LinkedHashMap<>();
            return new SimpleResponse(200, null, ErrorExcel.handleError(copy));
        } else {
            // Save in batches.
            classRepo.insertClassesInBatch(classes);
            List<EduCourse> eduCourses = new ArrayList<>(courses);
            courseRepo.insertCoursesInBatch(eduCourses);
            return new SimpleResponse(200, null, null);
        }
    }

    @Override
    public List<List<EduClassOM>> getAllTimetablesOfCourses(final Set<String> courseIds) throws Exception {
        List<GroupClassesOM> classGroups = classRepo.getAllClassesOfCourses(courseIds);

        if (classGroups.size() < courseIds.size()) {
            for (GroupClassesOM group : classGroups) {
                String courseId = group.getCourseId();

                if (courseIds.contains(courseId)) {
                    courseIds.remove(courseId);
                }
            }

            throw new Exception("Học phần " +
                                courseIds.toString() +
                                " không được mở trong kỳ này hoặc thời khoá biểu chưa được cập nhật");
        }

        List<EduCourse> courses = courseRepo.findAllById(courseIds);
        TimetableGenerator generator = new TimetableGenerator(classGroups, courses);

        return generator.generate();
    }
}
