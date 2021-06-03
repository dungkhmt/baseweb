package com.hust.baseweb.applications.postsys.model.postman;

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
public class PostmanAssignmentByDate {
    private UUID postmanId;
    private String postmanName;
    private String postOfficeId;
    private List<PostOrder> postOrders;
}
