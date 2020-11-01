package com.hust.baseweb.applications.backlog.entity;

import com.hust.baseweb.applications.backlog.model.CreateProjectInputModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="backlog_project")
public class BacklogProject {

    public BacklogProject(CreateProjectInputModel input) {
        backlogProjectId = input.getBacklogProjectId();
        backlogProjectName = input.getBacklogProjectName();
    }

    @Id
    @Column(name="backlog_project_id")
    private String backlogProjectId;

    @Column(name="backlog_project_name")
    private String backlogProjectName;
}
