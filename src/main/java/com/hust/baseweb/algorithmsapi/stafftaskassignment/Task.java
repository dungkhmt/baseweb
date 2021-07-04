package com.hust.baseweb.algorithmsapi.stafftaskassignment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Task {

    private String taskID;
    private String taskTypeID;
    private int duration;
    private String locationID;

}
