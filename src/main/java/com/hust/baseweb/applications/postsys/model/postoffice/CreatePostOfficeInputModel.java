package com.hust.baseweb.applications.postsys.model.postoffice;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

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