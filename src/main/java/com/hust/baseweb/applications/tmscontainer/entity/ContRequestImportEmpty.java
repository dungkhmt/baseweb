package com.hust.baseweb.applications.tmscontainer.entity;


import com.hust.baseweb.applications.customer.entity.PartyCustomer;
import com.hust.baseweb.applications.logistics.entity.Facility;
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
public class ContRequestImportEmpty {

    @Id
    @Column(name = "request_import_empty_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID requestImportEmptyId;

    @JoinColumn(name = "facility_id", referencedColumnName = "facility_id")
    @ManyToOne
    private Facility facility;

    @JoinColumn(name = "party_customer_id", referencedColumnName = "party_id")
    @ManyToOne
    private PartyCustomer partyCustomer;


    @JoinColumn(name = "container_type_id", referencedColumnName = "container_type_id")
    @ManyToOne
    private ContContainerType contContainerType;

    @Column(name = "number_containers")
    private Integer numberContainers;

    @Column(name = "has_trailer")
    private String hasTrailer;

    @Column(name = "request_date_time")
    private Date requestDateTime;

    @Column(name = "early_date_time_expected")
    private Date earlyDateTimeExpected;

    @Column(name = "late_date_time_expected")
    private Date lateDateTimeExpected;

    @Column(name = "last_updated_stamp")
    private Date lastUpdatedStamp;

    @Column(name = "created_stamp")
    private Date createdStamp;

    @Transient
    private String customerName;

    @Transient
    private String facilityName;

    @Transient
    private String address;

    @Transient
    private String containerType;

    @Transient
    String time;
}
