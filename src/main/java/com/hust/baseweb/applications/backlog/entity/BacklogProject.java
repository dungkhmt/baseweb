package com.hust.baseweb.applications.backlog.entity;

import com.hust.baseweb.applications.backlog.model.CreateProjectInputModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "backlog_project")
public class BacklogProject {

    public BacklogProject(CreateProjectInputModel input) {
        backlogProjectCode = input.getBacklogProjectCode();
        backlogProjectName = input.getBacklogProjectName();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "backlog_project_id")
    private UUID backlogProjectId;

    @Column(name = "backlog_project_code")
    private String backlogProjectCode;

    @Column(name = "backlog_project_name")
    private String backlogProjectName;
}
