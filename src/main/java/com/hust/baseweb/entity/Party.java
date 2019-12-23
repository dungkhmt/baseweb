package com.hust.baseweb.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Party {
    @Id
    @Column(name="party_id")
    private UUID partyId;

    private String partyCode;

    @JoinColumn(name = "party_type_id", referencedColumnName = "party_type_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private PartyType type;

    @Column(columnDefinition = "TEXT")
    private String description;

    @JoinColumn(name = "status_id", referencedColumnName = "status_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Status partyStatus;

    //@JoinColumn(name = "party_id", referencedColumnName = "party_id")
    @OneToOne(fetch = FetchType.EAGER, mappedBy = "party")
    private UserLogin userLogin;

    private boolean isUnread;

    @JoinColumn(name = "created_by_user_login", referencedColumnName = "user_login_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private UserLogin createdBy;


    @JoinColumn(name = "last_modified_by_user_login", referencedColumnName = "user_login_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private UserLogin modifiedBy;

    private Date createdStamp;
    private Date lastUpdatedStamp;


    public Party(String partyCode, PartyType type,
                 String description, Status partyStatus, boolean isUnread,
                 UserLogin createdBy) {
        super();
        this.partyCode = partyCode;
        this.type = type;
        this.description = description;
        this.partyStatus = partyStatus;
        this.isUnread = isUnread;
        this.createdBy = createdBy;


    }

    public Party(String partyCode, PartyType type,
                 String description, Status partyStatus, boolean isUnread,
                 UserLogin createdBy, UserLogin modifiedBy) {
        super();
        this.partyCode = partyCode;
        this.type = type;
        this.description = description;
        this.partyStatus = partyStatus;
        this.isUnread = isUnread;
        this.createdBy = createdBy;
        this.modifiedBy = modifiedBy;

    }

    public Party() {
        super();
        // TODO Auto-generated constructor stub
    }

}