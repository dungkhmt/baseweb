package com.hust.baseweb.applications.education.suggesttimetable.model;

import com.hust.baseweb.applications.education.suggesttimetable.entity.EduClass;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * @author Le Anh Tuan
 */
@Getter
@AllArgsConstructor
public class FindAndGroupClassesOM {

    private final String courseId;

    private final String classType;

    private final List<EduClass> classes;
}
