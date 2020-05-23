package com.hust.baseweb.applications.humanresource.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter

public class Department {
    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "department_id")
    private String departmentId;

    @Column(name = "department_name")
    private String departmentName;

}
