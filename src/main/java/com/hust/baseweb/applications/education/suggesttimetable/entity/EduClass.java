package com.hust.baseweb.applications.education.suggesttimetable.entity;

import com.hust.baseweb.applications.education.exception.CustomExceptionExcel;
import com.hust.baseweb.applications.education.suggesttimetable.enums.ErrorExcel;
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
import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

@Getter
@Document(collection = "class")
public class EduClass {

    @Transient
    public List<ErrorExcel> errorList = new ArrayList<>();

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

    @Transient
    @Getter(value = AccessLevel.NONE)
    private boolean anyMissTime = false;

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
    ) throws CustomExceptionExcel {
        if (classId == null) {
            errorList.add(ErrorExcel.classId_error);
        }
        if (courseId == null) {
            errorList.add(ErrorExcel.courseId_error);
        }
        if (errorList.size() > 0) {
            throw new CustomExceptionExcel(errorList);
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

        if (this.startTime == null || this.endTime == null || this.dayOfWeek == null) {
            this.anyMissTime = true;
        }
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

        try {
            return DayOfWeek.of(Integer.parseInt(value) - 1);
        } catch (NumberFormatException | DateTimeException e) {
            return null;
        }
    }

    public static Integer normalizeFist(Cell cell) {
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
        String weeksStr = StringUtils.deleteWhitespace(this.weeks);

        if (null == weeksStr) {
            return null;
        }

        List<Integer> weeksList = new ArrayList<>();
        String[] weeks = weeksStr.split("[,.]");

        for (String week : weeks) {
            if (week.contains("-")) {
                String[] range = week.split("-");
                int fromWeek = Integer.parseInt(range[0]);
                int toWeek = Integer.parseInt(range[1]);

                for (int i = fromWeek; i <= toWeek; i++) {
                    weeksList.add(i);
                }
            } else {
                weeksList.add(Integer.parseInt(week));
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
     * Check the overlapping of 2 classes.
     *
     * @param clazz
     * @return {@code true} if 2 classes overlap, {@code false} if otherwise
     */
    public boolean overlap(@Nullable EduClass clazz) {
        if (this == clazz) {
            return true;
        }

        if (clazz == null || this.anyMissTime == true || clazz.anyMissTime == true) {
            return false;
        }

        if (clazz.getStartTime() <= this.endTime && this.startTime <= clazz.getEndTime()) {
            // Time overlaps, so consider day of week.
            if (this.dayOfWeek.equals(clazz.dayOfWeek)) {
                // Time and day of week overlap, so consider week by checking common elements of 2 list of weeks.
                List<Integer> smallerWeeksList = this.getWeeksList();
                List<Integer> biggerWeeksList = clazz.getWeeksList();

                if (smallerWeeksList == null || biggerWeeksList == null) {
                    return false;
                } else {
                    if (smallerWeeksList.size() > biggerWeeksList.size()) {
                        smallerWeeksList = clazz.getWeeksList();
                        biggerWeeksList = this.getWeeksList();
                    }

                    for (Integer week : biggerWeeksList) {
                        if (smallerWeeksList.contains(week)) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }
}
