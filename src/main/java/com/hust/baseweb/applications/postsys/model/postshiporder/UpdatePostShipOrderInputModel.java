package com.hust.baseweb.applications.postsys.model.postshiporder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePostShipOrderInputModel {
    private String postOrderId;
    private String status;
    private String currentPostOfficeId;
}
