package com.hust.baseweb.applications.postsys.model.postoffice;

import com.hust.baseweb.applications.geo.entity.PostalAddress;
import com.hust.baseweb.applications.postsys.entity.PostOffice;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostOfficeOrderStatusOutputModel {

    private String postOfficeId;
    private String postOfficeName;
    private PostalAddress postalAddress;
    private int postOfficeLevel;
    private boolean status;

    public PostOfficeOrderStatusOutputModel(PostOffice postOffice) {
        this.setPostOfficeId(postOffice.getPostOfficeId());
        this.setPostOfficeName(postOffice.getPostOfficeName());
        this.setPostalAddress(postOffice.getPostalAddress());
        this.setPostOfficeLevel(postOffice.getPostOfficeLevel());
        this.status = false;
    }
}
