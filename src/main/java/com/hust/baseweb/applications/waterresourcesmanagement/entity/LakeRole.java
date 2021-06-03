package com.hust.baseweb.applications.waterresourcesmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LakeRole {
    @Id
    @Column(name="lake_role_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID lakeRoleId;

    @JoinColumn(name="lake_id", referencedColumnName="lake_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Lake lake;

    @Column(name="user_login_id")
    private String userLoginId;

    @Column(name="from_date")
    private Date fromDate;

    @Column(name="thru_date")
    private Date thruDate;

    @Column(name="role_type_id")
    private String roleTypeId;

}
