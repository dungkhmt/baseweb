package com.hust.baseweb.applications.postsys.entity;

import com.hust.baseweb.applications.geo.entity.PostalAddress;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "post_customer")
public class PostCustomer {

    @Id
    @Column(name = "post_customer_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID postCustomerId;
    @Column(name = "post_customer_name")
    private String postCustomerName;
    @Column(name = "phone_num")
    private String phoneNum;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "contact_mech_id", referencedColumnName = "contact_mech_id")
    private PostalAddress postalAddress;
    @Column(name = "party_id")
    private UUID partyId;
}
