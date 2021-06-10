package com.hust.baseweb.applications.backlog.entity;

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
@Table(name = "backlog_task_category")
public class BacklogTaskCategory {

    @Id
    @Column(name = "backlog_task_category_id")
    private String backlogTaskCategoryId;

    @Column(name = "backlog_task_category_name")
    private String backlogTaskCategoryName;
//
//    @Column(name="last_update_stamp")
//    private Date lastUpdateStamp;
//
//    @Column(name="create_stamp")
//    private Date createStamp;
}
