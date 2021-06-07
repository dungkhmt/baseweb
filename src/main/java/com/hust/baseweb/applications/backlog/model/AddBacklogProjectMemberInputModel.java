package com.hust.baseweb.applications.backlog.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddBacklogProjectMemberInputModel {

    UUID backlogProjectId;
    List<String> usersLoginId;
}
