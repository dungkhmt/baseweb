package com.hust.baseweb.applications.postsys.model.postsolve;

import com.hust.baseweb.applications.postsys.entity.PostOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

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
    private List<UUID> postmanIds;
    private List<UUID> postOrderIds;
}
