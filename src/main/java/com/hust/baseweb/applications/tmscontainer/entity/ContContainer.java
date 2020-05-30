package com.hust.baseweb.applications.tmscontainer.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContContainer {

    @Id
    @Column(name = "container_id")
    private String containerId;

    @ManyToOne
    @JoinColumn(name = "container_type_id", referencedColumnName = "container_type_id")
    private ContContainerType contContainerType;

    @Column(name = "last_updated_stamp")
    private Date lastUpdatedStamp;

    @Column(name = "created_stamp")
    private Date createdStamp;

    @Column(name = "container_name")
    private String containerName;

    @Transient
    private String containerType;


}
