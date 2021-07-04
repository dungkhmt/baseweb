package com.hust.baseweb.applications.postsys.model.postman;

import com.hust.baseweb.applications.postsys.entity.Postman;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostmanUpdateOutputModel {

    private String status;
    private String detail;
    Postman postman;
}
