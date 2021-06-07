package com.hust.baseweb.applications.postsys.model.postdriver;

import com.hust.baseweb.applications.postsys.entity.PostDriverPostOfficeAssignment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePostDriverPostOfficeAssignmentOutputModel {

    private String status;
    private String detail;
    private PostDriverPostOfficeAssignment postDriverPostOfficeAssignment;
}
