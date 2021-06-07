package com.hust.baseweb.applications.postsys.model.postshiporder;

import com.hust.baseweb.applications.postsys.entity.PostOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreatePostShipOrderOutputModel {

    private String statusCode;
    private String detail;
    private PostOrder postOrder;
}
