package com.hust.baseweb.applications.logistics.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Uom {

    @Id
    @Column(name = "uom_id")
    private String uomId;

    @JoinColumn(name = "uom_type_id", referencedColumnName = "uom_type_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private UomType uomType;

    @Column(name = "abbreviation")
    private String abbreviation;

    @Column(name = "description")
    private String description;

}
