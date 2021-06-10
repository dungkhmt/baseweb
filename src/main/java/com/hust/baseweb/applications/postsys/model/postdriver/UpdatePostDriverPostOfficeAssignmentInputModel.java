package com.hust.baseweb.applications.postsys.model.postdriver;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePostDriverPostOfficeAssignmentInputModel {

    private String postDriverId;
    private String postDriverPostOfficeAssignmentId;
    private String postOfficeFixedTripId;
}
