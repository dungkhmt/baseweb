package com.hust.baseweb.applications.education.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="edu_course_teacher_preference")
public class EduCourseTeacherPreference {
	@EmbeddedId
	CourseTeacherCompositeId compositeId;
}
