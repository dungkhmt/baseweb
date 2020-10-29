package com.hust.baseweb.applications.backlog.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddBacklogProjectMemberInputModel {
    String backlogProjectId;
    List<String> usersLoginId;
}
