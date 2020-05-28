package com.hust.baseweb.applicationsandbox.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddInputModel {

    private int a;
    private int b;

    public AddInputModel(int a, int b) {
        super();
        this.a = a;
        this.b = b;
    }

    public AddInputModel() {
        super();
        // TODO Auto-generated constructor stub
    }

}
