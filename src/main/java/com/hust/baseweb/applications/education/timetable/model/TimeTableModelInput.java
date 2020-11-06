package com.hust.baseweb.applications.education.timetable.model;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class TimeTableModelInput /// A Row
{
    private int cid;
    private int acid;
    private String course_code;
    private String course_name;
    private String capacity;
    private String note;
    private String course_week;
    private int day_of_week;
    private String course_time;
    private int start_section;
    private int finish_section;
    private String shift;
    private String room;
    private String type_of_class;
    private int total;
    private int course_session;
    private String state;
    private String experiment;
    private int mid; ///Management ID
    private String college;
    private String course_period;
}
