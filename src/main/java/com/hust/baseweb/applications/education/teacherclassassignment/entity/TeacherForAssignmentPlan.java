package com.hust.baseweb.applications.education.teacherclassassignment.entity;


import com.hust.baseweb.applications.education.teacherclassassignment.entity.compositeid.TeacherPlanId;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "teacher_for_assignment_plan")
@IdClass(TeacherPlanId.class)
public class TeacherForAssignmentPlan {

    @Id
    @Column(name = "teacher_id")
    private String teacherId;

    @Id
    @Column(name = "plan_id")
    private UUID planId;

    @Column(name = "max_hour_load")
    private double maxHourLoad;

    @Column(name = "minimize_number_working_days")
    private String minimizeNumberWorkingDays;
}
