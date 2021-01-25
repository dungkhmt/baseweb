package com.hust.baseweb.applications.postsys.model.postman;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePostmanPostOrderAssignmentInputModel {
    private String postShipOrderPostmanLastMileAssignmentId;
    private String postShipOrderId;
    private String status;
}
