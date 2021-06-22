package com.hust.baseweb.applications.education.teacherclassassignment.utils;

import java.sql.Time;

public class TimetableConflictChecker {
    public static String extractPeriod(String timeTableCode){
        return timeTableCode.substring(2,9);
    }
    public static boolean conflict(String timetableCode1, String timetableCode2){
        try {
            String[] p1 = extractPeriod(timetableCode1).split(",");
            String[] p2 = extractPeriod(timetableCode2).split(",");
            int start1 = Integer.valueOf(p1[0]);
            int end1 = Integer.valueOf(p1[1]);
            int start2 = Integer.valueOf(p2[0]);
            int end2 = Integer.valueOf(p2[1]);
            //System.out.println("start1 = " + start1 + " end1 = " + end1 + " start2 = " + start2 + " end2 = " + end2);
            return !(start1 > end2 || start2 > end1);
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("TimetableConflictChecker.conflict, EXCEPTION t1 = " + timetableCode1 + ", t2 = " + timetableCode2);
            return false;
        }
    }
    public static void main(String[] args){
        String t1 = "1,312,314,2-9,11-18,D9-107;";
        String t2 = "1,411,414,2-9,11-18,D9-101;";
        System.out.println(TimetableConflictChecker.conflict(t1,t2));
    }
}
