package com.hust.baseweb.rest.entity;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import javax.persistence.SecondaryTables;
import javax.persistence.Table;

import com.hust.baseweb.entity.Party;
import com.hust.baseweb.entity.SecurityGroup;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.Getter;
import lombok.Setter;

/**
 * UserDetailEntity
 */
@Entity
@Table(name = "party")
@Getter
@Setter
@SecondaryTables({ @SecondaryTable(name = "person"),
        @SecondaryTable(name = "user_login", pkJoinColumns = @PrimaryKeyJoinColumn(name = "party_id")) })
public class UserCombineEntity {

    public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    @Column(name = "user_login_id", table = "user_login")
    private String userLoginId;
    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "party_id")
    private UUID partyId;
    @Column(table = "person")
    private String firstName;
    @Column(table = "person")
    private String middleName;
    @Column(table = "person")
    private String lastName;
    @Column(table = "person")
    private String gender;
    @Column(table = "person")
    private Date birthDate;
    //@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    //@JoinTable(name = "user_login_security_group", joinColumns = @JoinColumn(name = "user_login_id", referencedColumnName = "user_login_id"), inverseJoinColumns = @JoinColumn(name = "group_id", referencedColumnName = "group_id"))
    //private List<SecurityGroup> roles;

    public UserCombineEntity() {
    }
}