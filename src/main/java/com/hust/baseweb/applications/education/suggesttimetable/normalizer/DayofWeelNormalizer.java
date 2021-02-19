package com.hust.baseweb.applications.education.suggesttimetable.normalizer;

import org.apache.poi.ss.usermodel.Cell;

import java.time.DayOfWeek;

public class DayofWeelNormalizer extends Normalizer {

    @Override
    public Comparable normalize(Cell cell) {
        String input = this.formatString(cell);
        if (input == null)
            return null;
        return DayOfWeek.of(Integer.parseInt(input) - 1);
    }
}
