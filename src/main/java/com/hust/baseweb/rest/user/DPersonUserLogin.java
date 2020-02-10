package com.hust.baseweb.rest.user;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hust.baseweb.entity.Party;
import com.hust.baseweb.entity.SecurityGroup;

import lombok.Data;

/**
 * DPersonUserLogin
 */
@Data
@Entity
@Table(name = "user_login")
public class DPersonUserLogin {

    @Id
    @Column(name = "user_login_id", updatable = false, nullable = false)
    private String userLoginId;

    private boolean isSystem;

    private boolean enabled;

    @JoinColumn(name = "party_id", referencedColumnName = "party_id")
    @OneToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private Party party;


    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_login_security_group",
            joinColumns = @JoinColumn(name = "user_login_id", referencedColumnName = "user_login_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id", referencedColumnName = "group_id"))
    private List<SecurityGroup> roles;
    private Date disabledDateTime;


    private Date createdStamp;


    private Date lastUpdatedStamp;
}