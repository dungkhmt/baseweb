package com.hust.baseweb.applications.education.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "edu_course_chapter")
public class EduCourseChapter {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="chapter_id")
    private UUID chapterId;

    @Column(name="chapter_name")
    private String chapterName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    private EduCourse eduCourse;

}
