package com.hust.baseweb.applications.education.teacherclassassignment.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MapDataInput {
    public int n;// number of classes
    public int m;// number of teachers
    public HashSet<Integer>[] D;// D[i] is the set of teachers that can be assigned to class i
    public boolean[][] conflict;
    public int[][] priority;
    public double[] hourClass;
    public double[] maxHourTeacher;
    public int[][] preAssignment;
}
