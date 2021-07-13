package com.hust.baseweb.applications.admin.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SendMailToAllUsersInputModel {
    private String mailTitle;
    private String mailContent;

}
