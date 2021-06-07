package com.hust.baseweb.applications.postsys.model.postshiporder;

import com.hust.baseweb.applications.postsys.entity.PostOffice;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostOrderRouteOutputModel {

    private PostOffice fromPostOffice;
    private PostOffice toPostOffice;
    private String status;
    private Date arrivedDateTime;
    private Date departureDateTime;
}
