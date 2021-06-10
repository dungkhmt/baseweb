package com.hust.baseweb.applications.backlog.model;

import com.hust.baseweb.applications.backlog.entity.BacklogTask;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BacklogTaskWithAssignment {

    BacklogTask backlogTask;
    List<UserLoginReduced> assignment;
}
