package com.hust.baseweb.applications.postsys.entity;

import com.hust.baseweb.applications.geo.entity.PostalAddress;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name="post_office")
public class PostOffice {

    @Id
    @Column(name = "post_office_id")
    private String postOfficeId;

    @Column(name = "post_office_name")
    private String postOfficeName;

    @JoinColumn(name = "contact_mech_id", referencedColumnName = "contact_mech_id")
    @ManyToOne
    private PostalAddress postalAddress;

    @Column(name = "post_office_level")
    private int postOfficeLevel;

}
