package com.hust.baseweb.applications.education.suggesttimetable.entity;

import com.hust.baseweb.applications.education.suggesttimetable.enums.EDepartment;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Getter
@ToString
@Document(collection = "course")
public class EduCourse {

    //@MongoId, khi test tren project thi khong luu duoc
    @Id
    private final String id; // MÃ_HP

    private final String name, eName; //TÊN_HP, TÊN_HP_TIẾNG_ANH

    private final EDepartment department; // KHOA_VIỆN

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

    public static Comparable normalize(Cell cell, String status) {
        String s = cell.getStringCellValue();
        String input = StringUtils.deleteWhitespace(s);
        if (StringUtils.equalsIgnoreCase("NULL",input) || StringUtils.equalsIgnoreCase("",input)){
            return null;
        } else {
            switch (status){
                case "MÃ_HP":
                    return EduClass.normalize(cell,status);
                case "TÊN_HP":
                case "TÊN_HP_TIẾNG_ANH":
                    return input;
                case "KHOA_VIỆN":
                    return EDepartment.of(input);
            }
        }
        return null;

    }
}
