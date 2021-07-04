package com.hust.baseweb.applications.postsys.model.postman;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SolvePostmanPostOrderAssignmentTspInputModel {

    List<String> postShipOrderPostmanLastMileAssignmentIds;
    Boolean pick;
    String postOfficeId;
}
