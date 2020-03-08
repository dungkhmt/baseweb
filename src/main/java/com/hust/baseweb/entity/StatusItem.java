package com.hust.baseweb.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class StatusItem {
	@Id
    @Column(name = "status_id")
    private String statusId;
   
	@JoinColumn(name = "status_type_id", referencedColumnName = "status_type_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private StatusType type;
	
	@Column(name="status_code")
    private String statusCode;
    
    @Column(name="description")
    private String description;
    
}
