package com.hust.baseweb.applications.education.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name="edu_class_teacher_asignment")
public class EduClassTeacherAssignment {
	@EmbeddedId
	ClassTeacherCompositeId classTeacherCompositeId;
}
