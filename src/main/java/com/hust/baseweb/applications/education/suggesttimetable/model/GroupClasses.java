package com.hust.baseweb.applications.education.suggesttimetable.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * @author Le Anh Tuan
 */
@Getter
@AllArgsConstructor
public class GroupClasses {

    private final String courseId;

    private final String classType;

    private final List<EduOM> classes;
}
