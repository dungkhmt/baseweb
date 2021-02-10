package com.hust.baseweb.applications.education.suggesttimetable.entity;

import com.hust.baseweb.applications.education.suggesttimetable.enums.EDepartment;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Getter
@ToString
@Document(collection = "course")
public class EduCourse {

    @MongoId
    private final String id;

    private final String name, eName;

    private final EDepartment department;

    /**
     * Creates a new {@link EduCourse} from the given id, name, eName and department.
     *
     * @param id         must not be {@code null} or empty
     * @param name       must not be {@code null} or empty
     * @param eName      must not be {@code null} or empty
     * @param department must not be {@code null} or empty
     */
    public EduCourse(
        String id, String name, String eName, EDepartment department
    ) {
        this.id = id;
        this.name = name;
        this.eName = eName;
        this.department = department;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (this.id == null || obj == null || !(this.getClass().equals(obj.getClass()))) {
            return false;
        }

        EduCourse that = (EduCourse) obj;

        return this.id.toUpperCase().equals(that.getId().toUpperCase());
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }
}
