package com.hust.baseweb.applications.postsys.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="POST_PACKAGE_TYPE")
@Getter
@Setter
public class PostPackageType {
    @Id
    @Column(name="post_package_type_id")
    private String postPackageTypeId;
    @Column(name="post_package_type_name")
    private String postPackageTypeName;
}
