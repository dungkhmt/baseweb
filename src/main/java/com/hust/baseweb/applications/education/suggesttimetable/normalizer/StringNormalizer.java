package com.hust.baseweb.applications.education.suggesttimetable.normalizer;

import com.hust.baseweb.applications.education.suggesttimetable.normalizer.Normalizer;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.DataFormatter;

public class StringNormalizer extends Normalizer {

    @Override
    public Comparable normalize(Cell cell) {
        String output = this.formatString(cell);
        return output;
    }
}
