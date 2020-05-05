package com.hust.baseweb.applications.accounting.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class PaymentSequenceId {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private Long id;
}
