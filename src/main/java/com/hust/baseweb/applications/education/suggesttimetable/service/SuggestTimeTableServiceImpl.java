package com.hust.baseweb.applications.education.suggesttimetable.service;

import com.hust.baseweb.applications.education.exception.SimpleResponse;
import com.hust.baseweb.applications.education.suggesttimetable.entity.EduClass;
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
        HashMap<String,Integer> input = new HashMap<>();
        HashMap<String, Comparable> listInput = new HashMap<>();
        List<EduClass> listEduClass = new ArrayList<>();

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
            input.put(s, i);
            i = i + 1;
        }
        input.put("BẮT_ĐẦU", input.get("THỜI_GIAN"));
        input.put("KẾT_THÚC", input.get("THỜI_GIAN"));
        input.remove("KỲ");
        input.remove("THỜI_GIAN");
        input.remove("KHOA_VIỆN");
        input.remove("TÊN_HP");
        input.remove("TÊN_HP_TIẾNG_ANH");
        input.remove("BUỔI_SỐ");
        input.remove("KT");
        input.remove("BĐ");
        input.remove("BĐ");
        input.remove("ĐỢT_MỞ");


        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            for (String s : input.keySet()) {
                listInput.put(s, EduClass.normalize(row.getCell(input.get(s)), s));
            }
            EduClass eduClass = new EduClass(
                (Integer) listInput.get("MÃ_LỚP"),
                (Integer) listInput.get("MÃ_LỚP_KÈM"),
                (String) listInput.get("MÃ_HP"),
                (Integer) listInput.get("KHỐI_LƯỢNG"),
                (String) listInput.get("GHI_CHÚ"),
                (DayOfWeek) listInput.get("THỨ"),
                (Integer) listInput.get("BẮT_ĐẦU"),
                (Integer) listInput.get("KẾT_THÚC"),
                (EShift) listInput.get("KÍP"),
                (String) listInput.get("TUẦN"),
                (String) listInput.get("PHÒNG"),
                (boolean) listInput.get("CẦN_TN"),
                (Integer) listInput.get("SLĐK"),
                (Integer) listInput.get("SL_MAX"),
                (String) listInput.get("TRẠNG_THÁI"),
                (String) listInput.get("LOẠI_LỚP"),
                (String) listInput.get("MÃ_QL")
            );
            listEduClass.add(eduClass);



        }
        classRepo.saveAll(listEduClass);
        return null;

    }


}
