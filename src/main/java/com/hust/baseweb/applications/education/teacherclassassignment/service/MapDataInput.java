package com.hust.baseweb.applications.education.teacherclassassignment.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.PrintWriter;
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
    public boolean[][] classDays;// classDays[i][d] = true indicates that class i happens on day d
    public HashSet<Integer> teacherWantToMinimizeWorkingDays;

    public void savePlainTextFile(String filename){
        try{
            PrintWriter out = new PrintWriter(filename);
            out.println(n + " " + m);

            for(int i = 0; i < n; i++) out.print(hourClass[i] + " ");
            out.println();

            for(int j = 0; j < m; j++) out.print(maxHourTeacher[j] + " ");
            out.println();

            for(int i = 0; i < n; i++){
                out.print(D[i].size() + " ");
                for(int j: D[i]){
                    out.print(j + " ");
                }
                out.println();
            }
            for(int i = 0; i < n; i++){
                for(int j = 0; j < n; j++){
                    if(conflict[i][j]) out.print(1 + " ");
                    else out.print(0 + " ");
                }
                out.println();
            }

            out.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
