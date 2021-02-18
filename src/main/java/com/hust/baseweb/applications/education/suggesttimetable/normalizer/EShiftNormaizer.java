package com.hust.baseweb.applications.education.suggesttimetable.normalizer;

import com.hust.baseweb.applications.education.suggesttimetable.enums.EShift;
import org.apache.poi.ss.usermodel.Cell;

public class EShiftNormaizer extends Normalizer {

    @Override
    public Comparable normalize(Cell cell) {
        String input = this.formatString(cell);
        if (input == null)
            return null;
        return EShift.of(input);
    }
}
