package com.hust.baseweb.applications.education.suggesttimetable.model;

import com.hust.baseweb.applications.education.suggesttimetable.enums.Shift;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;

/**
 * @author Le Anh Tuan
 */
@Getter
@Setter
@AllArgsConstructor
public class EduClassOM {

    private final Integer classId;

    private final Integer attachedClassId;

    private final String courseId;

    private final Integer credit;

    private final DayOfWeek dayOfWeek;

    private final Integer startTime;

    private final Integer endTime;

    private final Shift shift;

    private final String weeks;

    private final String room;

    private final String classType;

    private final String name;
}
