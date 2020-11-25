package com.hust.baseweb.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StatusItem {

    @Id
    @Column(name = "status_id")
    private String statusId;

    @JoinColumn(name = "status_type_id", referencedColumnName = "status_type_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private StatusType type;

    @Column(name = "status_code")
    private String statusCode;

    @Column(name = "description")
    private String description;

}

