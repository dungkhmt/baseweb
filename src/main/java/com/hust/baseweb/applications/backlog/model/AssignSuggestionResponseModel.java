package com.hust.baseweb.applications.backlog.model;

import com.hust.baseweb.applications.backlog.entity.BacklogTask;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AssignSuggestionResponseModel {
    BacklogTask backlogTask;
    UserLoginReduced userSuggestion;
}
