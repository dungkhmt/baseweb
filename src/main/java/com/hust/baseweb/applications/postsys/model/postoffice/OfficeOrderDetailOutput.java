package com.hust.baseweb.applications.postsys.model.postoffice;

import com.hust.baseweb.applications.postsys.entity.PostOffice;
import com.hust.baseweb.applications.postsys.entity.PostOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OfficeOrderDetailOutput {

    PostOffice postOffice;
    List<PostOrder> fromPostOrders;
    List<PostOrder> toPostOrders;
}
