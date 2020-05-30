package com.hust.baseweb.applications.humanresource.entity;

import com.hust.baseweb.entity.Party;
import com.hust.baseweb.entity.RoleType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
public class PartyDepartment {

    @Id
    @Column(name = "party_department_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID partyDepartmentId;

    @JoinColumn(name = "department_id", referencedColumnName = "department_id")
    @ManyToOne
    private Department department;

    @JoinColumn(name = "party_id", referencedColumnName = "party_id")
    @ManyToOne
    private Party party;

    @JoinColumn(name = "role_type_id", referencedColumnName = "role_type_id")
    @ManyToOne
    private RoleType roleType;

    //@Column(name="department_id")
    //private String departmentId;

    @Column(name = "from_date")
    private Date fromDate;

    @Column(name = "thru_date")
    private Date thruDate;

}
