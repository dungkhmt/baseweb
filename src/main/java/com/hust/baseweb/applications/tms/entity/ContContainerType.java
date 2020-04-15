package com.hust.baseweb.applications.tms.entity.container;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContContainerType {
    @Id
    @Column(name = "container_type_id")
    private String containerTypeId;

    @Column(name = "description")
    private String description;

    @Column(name = "unit")
    private int unit;

    @Column(name = "last_updated_stamp")
    private Date lastUpdateStamp;

    @Column(name = "created_stamp")
    private Date createdStamp;

}
