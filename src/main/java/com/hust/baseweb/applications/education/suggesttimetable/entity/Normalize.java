package com.hust.baseweb.applications.education.suggesttimetable.entity;

import com.hust.baseweb.applications.education.suggesttimetable.enums.EShift;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;

public class Normalize {

    public static String formatCell(Cell cell) {
        DataFormatter formatter = new DataFormatter();
        String value = StringUtils.deleteWhitespace(formatter.formatCellValue(cell));

        if (StringUtils.equalsIgnoreCase("NULL", value) || StringUtils.equalsIgnoreCase("", value)) {
            return null;
        }

        return value;
    }

    public static Integer normalizeInt(Cell cell) {
        String value = Normalize.formatCell(cell);
        return NumberUtils.toInt(value);
    }

    public static String normalizeStr(Cell cell) {
        return Normalize.formatCell(cell);
    }


}
