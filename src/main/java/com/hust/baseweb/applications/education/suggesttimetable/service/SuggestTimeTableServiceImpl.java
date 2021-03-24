package com.hust.baseweb.applications.education.suggesttimetable.service;

import com.hust.baseweb.applications.education.exception.CustomException;
import com.hust.baseweb.applications.education.exception.SimpleResponse;
import com.hust.baseweb.applications.education.suggesttimetable.entity.EduClass;
import com.hust.baseweb.applications.education.suggesttimetable.entity.EduCourse;
import com.hust.baseweb.applications.education.suggesttimetable.enums.Error;
import com.hust.baseweb.applications.education.suggesttimetable.model.EduClassOM;
import com.hust.baseweb.applications.education.suggesttimetable.model.FindAndGroupClassesOM;
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

    public static LinkedHashMap<CustomException, Integer> errorLists = new LinkedHashMap<>();

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

                EduClass eduClass = new EduClass(
                    EduClass.normalizeInt(row.getCell(mapClassColumn.get("MÃ_LỚP"))),
                    EduClass.normalizeInt(row.getCell(mapClassColumn.get("MÃ_LỚP_KÈM"))),
                    EduClass.normalizeStr(row.getCell(mapClassColumn.get("MÃ_HP"))),
                    EduClass.normalizeFisrt(row.getCell(mapClassColumn.get("KHỐI_LƯỢNG"))),
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
                classes.add(eduClass);

                // Extract course.
                EduCourse eduCourse = new EduCourse(
                    EduCourse.normalizeString(row.getCell(mapCourseColumn.get("MÃ_HP"))),
                    EduCourse.normalizeDefault(row.getCell(mapCourseColumn.get("TÊN_HP"))),
                    EduCourse.normalizeDefault(row.getCell(mapCourseColumn.get("TÊN_HP_TIẾNG_ANH"))),
                    EduCourse.normalizeDept(row.getCell(mapCourseColumn.get("KHOA_VIỆN"))));
                courses.add(eduCourse);
            } catch (CustomException e) {
                errorLists.put(e, j);
            }
        }

        if (errorLists.size() > 0) {
            return new SimpleResponse(200, null, Error.handleError(errorLists));
        } else {
            // Save in batches.
            classRepo.insertClassesInBatch(classes);
            List<EduCourse> eduCourses = new ArrayList<>(courses);
            courseRepo.insertCoursesInBatch(eduCourses);
            return new SimpleResponse(200, null, null);
        }
    }

    @Override
    public List<List<EduClassOM>> getAllTimetablesOfCourses(final Set<String> courseIds) {
        List<FindAndGroupClassesOM> groups = classRepo.getAllClassesOfCourses(courseIds);
        // TODO by DATPD: index classes, use bidiMap of Apache-Commons-Collections
        ArrayList<short[]> conflictSet = genSetOfConflictClassPairs(groups);
        List<EduCourse> courses = courseRepo.findByIdIn(courseIds);
        TimetableGenerator generator = new TimetableGenerator(groups, null, conflictSet, courses);

        return generator.generate();
    }

    private ArrayList<short[]> genSetOfConflictClassPairs(final List<FindAndGroupClassesOM> classGroups) {
        // TODO: by DATPD
        return null;
    }
}
