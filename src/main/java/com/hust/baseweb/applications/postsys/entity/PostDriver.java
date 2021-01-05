package com.hust.baseweb.applications.postsys.entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="post_driver")
public class PostDriver {
    @Id
    @Column(name="post_driver_id")
    private UUID postDriverId;

    @Column(name="post_driver_name")
    private String postDriverName;
}

