package com.hust.baseweb.applications.education.teacherclassassignment.utils;

import java.sql.Time;
import java.util.HashMap;

public class TimetableConflictChecker {
    static class SlotMapping {

        public HashMap<String, String> mStartSlotStr2SlotCode = new HashMap();
        public HashMap<String, String> mEndSlotStr2SlotCode = new HashMap();

        public SlotMapping(){
            // map starting slot
            mStartSlotStr2SlotCode.put("10645","11");
            mStartSlotStr2SlotCode.put("10730","12");
            mStartSlotStr2SlotCode.put("10825","13");
            mStartSlotStr2SlotCode.put("10920","14");
            mStartSlotStr2SlotCode.put("11015","15");
            mStartSlotStr2SlotCode.put("11100","16");
            mStartSlotStr2SlotCode.put("21230","21");
            mStartSlotStr2SlotCode.put("21315","22");
            mStartSlotStr2SlotCode.put("21410","23");
            mStartSlotStr2SlotCode.put("21505","24");
            mStartSlotStr2SlotCode.put("21600","25");
            mStartSlotStr2SlotCode.put("21645","26");

            // map end slot
            mEndSlotStr2SlotCode.put("10730","11");
            mEndSlotStr2SlotCode.put("10815","12");
            mEndSlotStr2SlotCode.put("10910","13");
            mEndSlotStr2SlotCode.put("11005","14");
            mEndSlotStr2SlotCode.put("11100","15");
            mEndSlotStr2SlotCode.put("11145","16");
            mEndSlotStr2SlotCode.put("21315","21");
            mEndSlotStr2SlotCode.put("21400","22");
            mEndSlotStr2SlotCode.put("21455","23");
            mEndSlotStr2SlotCode.put("21550","24");
            mEndSlotStr2SlotCode.put("21645","25");
            mEndSlotStr2SlotCode.put("21730","26");
        }
        public String getStartSlot(String s){
            return mStartSlotStr2SlotCode.get(s);
        }
        public String getEndSlot(String s){
            return mEndSlotStr2SlotCode.get(s);
        }
    }
    public static SlotMapping slotMapping = new SlotMapping();

    public static String getDayOfWeek(String s){
        return s.substring(0,1);
    }
    public static String getSlotStr(String s){// s under format e.g.,  21505 (15h05 chieu)
        return s.substring(1,s.length());
    }
    public static String convertStartSlotStr2Code(String s){
        String d = getDayOfWeek(s);
        String slots = getSlotStr(s);
        return d + slotMapping.getStartSlot(slots);
    }
    public static String convertEndSlotStr2Code(String s){
        String d = getDayOfWeek(s);
        String slots = getSlotStr(s);
        return d + slotMapping.getEndSlot(slots);
    }
    public static String extractPeriod(String timeTableCode){
        System.out.println("extractPeriod, timetableCode = " + timeTableCode);
        if(timeTableCode.length() < 9) return null;
        String str = timeTableCode.substring(2,9);
        String[] s = str.split(",");
        if(s != null && s.length == 2 && s[0].length() == 3 && s[1].length() == 3){
            // format e.g.,  1,221,223
            return str;
        }
        // format e.g., 1,221505,221730
        //System.out.println("timetableCode = " + timeTableCode);
        str = timeTableCode.substring(2,15);
        s = str.split(",");

        return convertStartSlotStr2Code(s[0] ) + "," + convertEndSlotStr2Code(s[1]);
    }
    public static boolean conflict(String timetableCode1, String timetableCode2){
        try {
            String code1 = extractPeriod(timetableCode1);
            String code2 = extractPeriod(timetableCode2);
            if(code1 == null || code2 == null){
                System.out.println("conflict, EXCEPTION timeTableCode timeTableCode1 = " + timetableCode1
                                   + ", timeTableCode2 = " + timetableCode2);
                return true;
            }
            String[] p1 = code1.split(",");
            String[] p2 = code2.split(",");
            if(p1 == null || p2 == null){
                System.out.println("conflict, EXCEPTION timeTableCode p1 = " + p1 + ", p2 = " + p2);
                return true;
            }
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

        String t = "1,221505,221730";
        String c = TimetableConflictChecker.extractPeriod(t);
        System.out.println(c);
    }
}
