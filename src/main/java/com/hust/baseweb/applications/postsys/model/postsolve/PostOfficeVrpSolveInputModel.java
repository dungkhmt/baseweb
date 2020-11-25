package com.hust.baseweb.applications.postsys.model.postsolve;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostOfficeVrpSolveInputModel {
    private String postOfficeId;
    /**
     * type=pick/delivery
     */
    private String type;
}
