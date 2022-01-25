package com.hust.baseweb.applications.chat.chattext.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="message")
public class Message {
    @Id
    @GeneratedValue
    @Column(name = "msg_id")
    private UUID msgId;

    @Column(name = "message")
    private String message;

    @Column(name = "created_by_user_login_id")
    private String fromUserLoginId;

    @Column(name = "to_user_login_id")
    private String toUserLoginId;

    @Column(name = "to_group_id")
    private UUID toGroup;

    @JoinColumn(name = "reply_to_message_id", referencedColumnName = "msg_id")
    @OneToOne(fetch = FetchType.LAZY)
    private Message replyTo;

    @Column(name = "status_id")
    private String statusId;

    @Column(name = "created_stamp")
    private LocalDateTime createdStamp;

    @Column(name = "last_updated_stamp")
    private LocalDateTime lastUpdatedStamp;
}
