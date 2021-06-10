package com.hust.baseweb.applications.postsys.model.postsolve;

import com.hust.baseweb.applications.postsys.entity.PostOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

/**
 * postmen: danh sach nguoi dua thu
 * geoPoints: danh sach route
 * distance: do dai moi route
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostOfficeVrpSolveOutputModel {

    private boolean isSolutionFound;
    private List<List<PostOrder>> routes;
    private List<Long> distance;
    private List<UUID> postmanIds;
}
