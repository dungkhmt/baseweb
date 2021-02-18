package com.hust.baseweb.applications.education.suggesttimetable.normalizer;

import com.hust.baseweb.applications.education.suggesttimetable.normalizer.Normalizer;
import org.apache.poi.ss.usermodel.Cell;

public class IntegerNormalizer extends Normalizer {

    @Override
    public Comparable normalize(Cell cell) {
        String input = this.formatString(cell);
        if (input == null)
            return null;
        return Integer.parseInt(input);
    }
}
