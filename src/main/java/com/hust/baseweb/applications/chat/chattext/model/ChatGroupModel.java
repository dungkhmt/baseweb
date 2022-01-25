package com.hust.baseweb.applications.chat.chattext.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatGroupModel {
    private String name;
    private List<String> memberIds;
    private String ownerId;
}
