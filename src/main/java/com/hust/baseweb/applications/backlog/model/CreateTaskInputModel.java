package com.hust.baseweb.applications.backlog.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class CreateTaskInputModel {

    private CreateBacklogTaskInputModel taskInput;
    private CreateBacklogTaskAssignmentInputModel assignmentInput;
    private CreateBacklogTaskAssignableInputModel assignableInput;
    private MultipartFile[] files;
}
