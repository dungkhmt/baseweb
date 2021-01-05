package com.hust.baseweb.applications.postsys.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="post_driver_post_office_assignment")
@Getter
@Setter
public class PostDriverPostOfficeAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="post_driver_post_office_assignment_id")
    private UUID postDriverPostOfficeAssignmentId;
    @Column(name="post_driver_id")
    private UUID postDriverId;
    @Column(name="from_post_office_id")
    private String fromPostOfficeId;

    @Column(name="to_post_office_id")
    private String toPostOfficeId;
    @ManyToOne
    @JoinColumn(name="post_driver_id", referencedColumnName = "post_driver_id", insertable = false, updatable = false)
    PostDriver postDriver;

    @ManyToOne
    @JoinColumn(name="from_post_office_id", referencedColumnName = "post_office_id", insertable = false, updatable = false)
    PostOffice fromPostOffice;

    @ManyToOne
    @JoinColumn(name="to_post_office_id", referencedColumnName = "post_office_id", insertable = false, updatable = false)
    PostOffice toPostOffice;
}
