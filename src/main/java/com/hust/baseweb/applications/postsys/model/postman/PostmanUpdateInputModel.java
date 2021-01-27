package com.hust.baseweb.applications.postsys.model.postman;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostmanUpdateInputModel {
    private String postmanId;
    private String postmanName;
    private String postOfficeId;
}
