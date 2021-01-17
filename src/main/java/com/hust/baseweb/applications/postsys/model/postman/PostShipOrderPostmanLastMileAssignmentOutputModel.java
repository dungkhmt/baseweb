package com.hust.baseweb.applications.postsys.model.postman;

import com.hust.baseweb.applications.postsys.entity.PostShipOrderPostmanLastMileAssignment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
public class PostShipOrderPostmanLastMileAssignmentOutputModel {

    List<PostShipOrderPostmanLastMileAssignment> pickAssignment;
    List<PostShipOrderPostmanLastMileAssignment> shipAssignment;
    List<PostShipOrderPostmanLastMileAssignment> finishedAssignment;

}
