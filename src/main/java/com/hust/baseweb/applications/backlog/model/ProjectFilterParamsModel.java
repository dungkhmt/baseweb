package com.hust.baseweb.applications.backlog.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Component
public class ProjectFilterParamsModel {

    private String backlogTaskName;
    private String categoryName;
    private String statusName;
    private String priorityName;
    private String assignment;
    private String createdByUser;

    public ProjectFilterParamsModel(
        String backlogTaskName,
        String categoryName,
        String statusName,
        String priorityName,
        String assignment,
        String createdByUser
    ) {
        this.backlogTaskName = Objects.toString(backlogTaskName, "");
        this.categoryName = Objects.toString(categoryName, "");
        this.statusName = Objects.toString(statusName, "");
        this.priorityName = Objects.toString(priorityName, "");
        this.assignment = Objects.toString(assignment, "");
        this.createdByUser = Objects.toString(createdByUser, "");
    }
}
