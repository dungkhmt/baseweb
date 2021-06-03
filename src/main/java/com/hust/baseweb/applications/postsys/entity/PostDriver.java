package com.hust.baseweb.applications.postsys.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="post_driver")
public class PostDriver {
    @Id
    @Column(name="post_driver_id")
    private UUID postDriverId;

    @Column(name="post_driver_name")
    private String postDriverName;

//    @OneToMany
//    @JoinColumn(name="post_driver_id", referencedColumnName = "post_driver_id", insertable = false, updatable = false)
//    private List<PostDriverPostOfficeAssignment> postDriverPostOfficeAssignment;

}

