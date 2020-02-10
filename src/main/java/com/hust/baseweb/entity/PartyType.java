package com.hust.baseweb.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
public class PartyType {
    public static enum PartyTypeEnum {
        AUTOMATED_AGENT, PERSON, PARTY_GROUP, BANK, LEGAL_ORGANIZATION, CORPORATION, CUSTOMER_GROUP
    }

    @Id
    @Column(name = "party_type_id")
    private String id;

    @JoinColumn(name = "parent_type_id", referencedColumnName = "party_type_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private PartyType parentType;

    private boolean hasTable;

    private String description;

    private Date createdStamp;

    private Date lastUpdatedStamp;

    public PartyType(String id, PartyType parentType, boolean hasTable, String description) {
        this.id = id;
        this.parentType = parentType;
        this.hasTable = hasTable;
        this.description = description;
    }

    public PartyType() {
    }
}
