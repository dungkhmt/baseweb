package com.hust.baseweb.applications.chat.chattext.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.hust.baseweb.applications.geo.entity.PostalAddress;
import com.hust.baseweb.entity.UserLogin;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="chat_group")
public class ChatGroup {
    @Id
    @Column(name = "group_id")
    @GeneratedValue
    private UUID groupId;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "created_by_user_login_id")
    private String createdById;

    @Column(name = "last_message_time")
    private LocalDateTime lastMessageTime;

    @Column(name = "created_stamp")
    private LocalDateTime createdStamp;

    @Column(name = "last_updated_stamp")
    private LocalDateTime lastUpdatedStamp;
//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "group_id")
//    private List<UserLoginChatGroup> members;

//    @JoinColumn(name = "group_id", referencedColumnName = "to_group_id")
//    @OneToMany(fetch = FetchType.LAZY)
//    private List<Message> messages;
}
