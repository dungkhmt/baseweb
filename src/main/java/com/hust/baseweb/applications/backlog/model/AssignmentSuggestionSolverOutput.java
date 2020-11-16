package com.hust.baseweb.applications.backlog.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.UUID;

@Getter
@Setter
public class AssignmentSuggestionSolverOutput {
    HashMap<UUID, UUID> assignmentSuggestion;
}
