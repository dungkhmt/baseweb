package com.hust.baseweb.applications.notifications.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "notifications")
public class Notifications {

    public static final String STATUS_CREATED = "STATUS_CREATED";
    public static final String STATUS_VIEWED = "STATUS_VIEWED";


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "notification_id")
    private UUID notificationId;

    @Column(name = "notification_name")
    private String notificationName;

    @Column(name = "from_user_login_id")
    private String fromUserLoginId;

    @Column(name = "to_user_login_id")
    private String toUserLoginId;

    @Column(name = "url")
    private String url;

    @Column(name = "status_id")
    private String statusId;

    @Column(name = "created_stamp")
    private Date createdStamp;


}
