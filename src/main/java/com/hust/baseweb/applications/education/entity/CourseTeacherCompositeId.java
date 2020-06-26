package com.hust.baseweb.applications.education.entity;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.hust.baseweb.entity.Party;
import com.hust.baseweb.entity.Person;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class CourseTeacherCompositeId implements Serializable {
	@Column(name="course_id")
	private String courseId;
	
	@Column(name="teacher_id")
	private String teacherId;
	
	@Column(name="class_type")
	private String classType;
	
	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        
        CourseTeacherCompositeId Id1 = (CourseTeacherCompositeId) o;
        if (courseId == Id1.getCourseId() && teacherId == Id1.getTeacherId() && classType == Id1.getClassType()) {
        	return true;
        }
        
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseId, teacherId, classType);
    }
}
