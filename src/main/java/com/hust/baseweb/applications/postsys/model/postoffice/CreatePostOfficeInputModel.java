package com.hust.baseweb.applications.postsys.model.postoffice;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreatePostOfficeInputModel {

    private String postOfficeId;
    private String postOfficeName;

    private int postOfficeLevel;

    private UUID contactMechId;
    private String address;
    private Double latitude;
    private Double longitude;
}
