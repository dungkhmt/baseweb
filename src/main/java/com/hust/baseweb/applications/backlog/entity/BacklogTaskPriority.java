package com.hust.baseweb.applications.backlog.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="backlog_task_priority")
public class BacklogTaskPriority {
    @Id
    @Column(name="backlog_task_priority_id")
    private String backlogTaskPriorityId;

    @Column(name="backlog_task_priority_name")
    private String backlogTaskPriorityName;
}
