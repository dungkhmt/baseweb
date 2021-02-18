package com.hust.baseweb.applications.education.suggesttimetable.normalizer;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;

public class EndTimeNormalizer extends Normalizer {

    @Override
    public Comparable normalize(Cell cell) {
        String input = this.formatString(cell);
        if (input == null)
            return null;
        return Integer.parseInt(StringUtils.substringAfter(input, "-"));
    }
}
