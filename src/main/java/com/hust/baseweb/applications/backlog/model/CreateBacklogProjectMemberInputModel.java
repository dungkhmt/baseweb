package com.hust.baseweb.applications.backlog.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateBacklogProjectMemberInputModel {
    String backlogProjectId;
    String userLoginId;
}
