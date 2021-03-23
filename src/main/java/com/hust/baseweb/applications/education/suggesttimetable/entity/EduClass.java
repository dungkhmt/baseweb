package com.hust.baseweb.applications.education.suggesttimetable.entity;

import com.hust.baseweb.applications.education.exception.CustomException;
import com.hust.baseweb.applications.education.suggesttimetable.enums.Error;
import com.hust.baseweb.applications.education.suggesttimetable.enums.Shift;
import lombok.AccessLevel;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

@Getter
@Document(collection = "class")
public class EduClass {

    public List<Error> errorList = new ArrayList<>();

    private final Integer classId;

    private final Integer attachedClassId;

    private final String courseId;

    private final Integer credit;

    private final String note;

    private final DayOfWeek dayOfWeek;

    private final Integer startTime;

    private final Integer endTime;

    private final Shift shift;

    private final String weeks;

    private final String room;
    private final boolean needExperiment;

    private final Integer numRegistration;

    private final Integer maxQuantity;

    private final String status, classType, managementId;
    @Id
    private BigInteger id;
    @Transient
    @Getter(value = AccessLevel.NONE)
    private List<Integer> weeksList = null;

    public EduClass(
        Integer classId,
        Integer attachedClassId,
        String courseId,
        Integer credit,
        String note,
        DayOfWeek dayOfWeek,
        Integer startTime,
        Integer endTime,
        Shift shift,
        String weeks,
        String room,
        boolean needExperiment,
        Integer numRegistration,
        Integer maxQuantity,
        String status,
        String classType,
        String managementId
    ) throws CustomException {
        if (classId == null) {
            errorList.add(Error.classId_error);
        }
        if (courseId == null) {
            errorList.add(Error.courseId_error);
        }
        if (errorList.size() > 0) {
            throw new CustomException(errorList);
        }
        this.classId = classId;
        this.attachedClassId = attachedClassId;
        this.courseId = courseId;
        this.credit = credit;
        this.note = note;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.shift = shift;
        this.weeks = weeks;
        this.room = room;
        this.needExperiment = needExperiment;
        this.numRegistration = numRegistration;
        this.maxQuantity = maxQuantity;
        this.status = status;
        this.classType = classType;
        this.managementId = managementId;
    }

    public static Integer normalizeInt(Cell cell) {
        return Normalizer.normalizeInt(cell).orElse(null);
    }

    public static String normalizeStr(Cell cell) {
        return Normalizer.normalizeStr(cell);
    }

    public static Shift normalizeShift(Cell cell) {
        String value = Normalizer.normalizeStr(cell);
        return null == value ? null : Shift.of(value);
    }

    public static String normalizeDefault(Cell cell) {
        return Normalizer.defaultString(cell).orElse(null);
    }

    public static DayOfWeek normalizeDayOfWeek(Cell cell) {
        String value = Normalizer.normalizeStr(cell);
        return value == null ? null : DayOfWeek.of(Integer.parseInt(value) - 1);
    }

    public static Integer normalizeFisrt(Cell cell) {
        String value = Normalizer.normalizeStr(cell);
        return NumberUtils.toInt(StringUtils.substring(value, 0, 1));
    }

    public static Integer normalizeAfterTime(Cell cell) {
        String value = Normalizer.normalizeStr(cell);
        return value == null ? null : Integer.parseInt(StringUtils.substringAfter(value, "-"));
    }

    public static Integer normalizeBeforeTime(Cell cell) {
        String value = Normalizer.normalizeStr(cell);
        return value == null ? null : Integer.parseInt(StringUtils.substringBefore(value, "-"));
    }

    public static Boolean normalizeBool(Cell cell) {
        return StringUtils.endsWithIgnoreCase("TN", Normalizer.normalizeStr(cell));
    }

    private List<Integer> convertWeeksToList() {
        // TODO by: datpd
        List<Integer> weeksList = new ArrayList<>();
        String s = StringUtils.deleteWhitespace(this.weeks);
        String[] weeks = s.split("[,.]");
        for (String w : weeks) {
            if (!w.contains("-")) {
                weeksList.add(Integer.parseInt(w));
            } else {
                String[] we = w.split("-");
                int start = Integer.parseInt(we[0]);
                int end = Integer.parseInt(we[1]);
                for (int i = start; i <= end; i++) {
                    weeksList.add(i);
                }
            }
        }
        return weeksList;
    }

    public List<Integer> getWeeksList() {
        if (null == weeksList) {
            weeksList = this.convertWeeksToList();
        }

        return weeksList;
    }

    /**
     * Check the time overlap of 2 classes
     *
     * @param eduClass
     * @return {@code true} if 2 classes overlap, {@code false} if otherwise
     */
    public boolean overlap(@Nullable EduClass eduClass) {
        // TODO by: datpd
        if (null == eduClass) {
            return false;
        }

        if (this == eduClass) {
            return true;
        }
        if (this.endTime == null || this.startTime == null ||
            eduClass.getStartTime() == null || eduClass.getEndTime() == null) {
            return false;
        }

        if (this.endTime < eduClass.getStartTime() ||
            this.startTime > eduClass.getEndTime()) { // Time does not overlap.
            return false;
        } else {
            // Check common elements of 2 list of weeks
            List<Integer> a = this.getWeeksList();
            List<Integer> b = eduClass.getWeeksList();
            int m = 0;
            for (int i : a) {
                for (int k = m; k < b.size(); k++) {
                    if (b.get(k) == i) {
                        return true;
                    }
                    if (b.get(k) > i) {
                        m = k;
                        break;
                    }
                }
            }
        }

        return false;
    }
}
