package com.hust.baseweb.applications.backlog.eumeration;

import lombok.Getter;

public enum BacklogEnum {
    BACKLOG_GROUP_PERMISSION("ROLE_BACKLOG"),
    BACKLOG_TASK_STATUS_INPROGRESS("TASK_INPROGRESS"),
    BACKLOG_TASK_STATUS_RESOLVED("TASK_RESOLVED"),
    BACKLOG_TASK_STATUS_CLOSED("TASK_CLOSED"),
    BACKLOG_TASK_STATUS_OPEN("TASK_OPEN");

    @Getter
    String value;
    BacklogEnum(String value) {
        this.value = value;
    }
}
