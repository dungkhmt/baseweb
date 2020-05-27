package com.hust.baseweb.applications.logistics.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Supplier {

    @Id
    @Column(name = "party_id")
    private UUID partyId; // uuid not null default uuid_generate_v1(),

    private String supplierName; // varchar(200),
    private String supplierCode; // varchar(60),

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class CreateModel {

        private String supplierName; // varchar(200),
        private String supplierCode; // varchar(60),
    }
}
