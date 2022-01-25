package com.hust.baseweb.applications.chat.chattext.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginChatGroup {
    @Id
    @GeneratedValue
    @Column(name = "mapping_id")
    private UUID mappingId;

    @Column(name = "user_login_id")
    private String userLoginId;

    @Column(name = "group_id")
    private UUID groupId;
}
