package com.hust.baseweb.applications.postsys.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="postman")
public class Postman {
    @Id
    @Column(name="postman_id")
    private UUID postmanId;

    @Column(name="postman_name")
    private String postmanName;

    @Column(name="post_office_id")
    private String postOfficeId;

//    @OneToMany
//    @JoinColumn(name = "postman_id", referencedColumnName = "postman_id",insertable = false, updatable = false)
//    private List<PostShipOrderPostmanLastMileAssignment> postShipOrderPostmanLastMileAssignments;
}
