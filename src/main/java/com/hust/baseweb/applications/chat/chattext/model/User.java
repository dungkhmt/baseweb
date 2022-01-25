package com.hust.baseweb.applications.chat.chattext.model;

import java.security.Principal;

public class User implements Principal {

    private String name;
    private String token;

    public User(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
