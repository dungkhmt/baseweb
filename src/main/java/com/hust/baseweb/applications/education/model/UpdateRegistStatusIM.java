package com.hust.baseweb.applications.education.model;

import com.hust.baseweb.applications.education.classmanagement.enumeration.RegistStatus;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UpdateRegistStatusIM extends RegistIM {

    @NotNull
    private RegistStatus status;
}
