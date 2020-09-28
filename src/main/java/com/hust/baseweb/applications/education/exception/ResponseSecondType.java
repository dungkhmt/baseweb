package com.hust.baseweb.applications.education.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResponseSecondType {
    private final int status;

    private final String error;

    private String message;

    public void addMessage(String message) {
        this.message += "; " + message;
    }
}
