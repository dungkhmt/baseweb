package com.hust.baseweb.applications.education.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class ClassTeacherCompositeId implements Serializable {
	@Column(name="class_id")
	private String classId;
	
	@Column(name="teacher_id")
	private String teacherId;
	
	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClassTeacherCompositeId Id1 = (ClassTeacherCompositeId) o;
        if (classId != Id1.classId) {
            return false;
        }
        return teacherId.equals(Id1.teacherId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(classId, teacherId);
    }
}
