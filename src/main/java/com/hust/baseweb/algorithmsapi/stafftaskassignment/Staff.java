package com.hust.baseweb.algorithmsapi.stafftaskassignment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@AllArgsConstructor
@Getter
@Setter
public class Staff {
    private String staffID;
    private List<String> skills;
    public String toString(){
        String des = staffID + ": ";
        for(String s: skills) des = des + s + ",";
        return des;
    }
}
