package com.hust.baseweb.applications.order.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderHeaderSequenceId {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private Long id;
}
