package com.hust.baseweb.applications.education.timetable.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class TimeTable {

    @Id
    @Column(name = "cid")
    private int cid;

    @Column(name = "acid")
    private int acid;

    @Column(name = "course_code")
    private String course_code;

    @Column(name = "course_name")
    private String course_name;

    @Column(name = "capacity")
    private String capacity;

    @Column(name = "note")
    private String note;

    @Column(name = "course_week")
    private String course_week;

    @Column(name = "day_of_week")
    private int day_of_week;

    @Column(name = "course_time")
    private String course_time;

    @Column(name = "start_section")
    private int start_section;

    @Column(name = "finish_section")
    private int finish_section;

    @Column(name = "shift")
    private String shift;

    @Column(name = "room")
    private String room;

    @Column(name = "type_of_class")
    private String type_of_class;

    @Column(name = "total")
    private int total;

    @Column(name = "course_session")
    private int course_session;

    @Column(name = "state")
    private String state;

    @Column(name = "experiment")
    private String experiment;

    @Column(name = "mid") /// Management ID
    private int mid;

    @Column(name = "college")
    private String college;

    @Column(name = "course_period")
    private String course_period;
}
