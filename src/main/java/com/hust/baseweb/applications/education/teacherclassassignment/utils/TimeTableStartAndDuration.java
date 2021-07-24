package com.hust.baseweb.applications.education.teacherclassassignment.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TimeTableStartAndDuration {
    private int startSlot;
    private int endSlot;
    private int duration;
    public String toString(){
        return startSlot + "-" + endSlot + "-" + duration;
    }
}
