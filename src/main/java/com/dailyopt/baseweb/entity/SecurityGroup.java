package com.dailyopt.baseweb.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Setter
@Getter
public class SecurityGroup {
    @Id
    @Column(name = "group_id")
    private String id;

    private String description;

    @JoinTable(name = "SecurityGroupPermission",
            joinColumns = @JoinColumn(name = "group_id",
                    referencedColumnName = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id",
                    referencedColumnName = "permission_id"))
    @OneToMany(fetch = FetchType.LAZY)
    private List<SecurityPermission> permissions;


    private Date createdStamp;


    private Date lastUpdatedStamp;

    public SecurityGroup() {
    }
}
