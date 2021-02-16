package com.hust.baseweb.applications.education.suggesttimetable.service;

import com.hust.baseweb.applications.education.suggesttimetable.enums.EShift;
import org.apache.commons.lang3.StringUtils;

import java.time.DayOfWeek;

public class Convert {


    //attachedId, classId, dayOfweeks, time, shift, weeks, numRegistration, maxQuantity
    public static Number covertInt(String s){
        String input = StringUtils.deleteWhitespace(s);
        if(input.equalsIgnoreCase("NULL") || input.equals(""))
            return null;
        else {
            return Integer.parseInt(s);
        }
    }

    public static String convertString(String s){
        String input = StringUtils.defaultString(s);
        if(input.equalsIgnoreCase("NULL") || input.equals(""))
            return null;
        else
            return s;
    }



    public static String convertWeeks(String s){
        String input = StringUtils.defaultString(s);
        if(input.equalsIgnoreCase("NULL") || input.equals("")){
            return null;
        }else {
            return StringUtils.deleteWhitespace(s);
        }
    }

    public static Number convertCredit(String s){
        String input = StringUtils.defaultString(s);
        return Integer.parseInt(StringUtils.substring(input,0,1));
    }

    //startTime
    public static Number covertStartTime(String s){
        String input = StringUtils.defaultString(s);
        if(input.equalsIgnoreCase("NULL") || input.equals("")){
            return null;
        }else {
            return Integer.parseInt(StringUtils.substringBefore(input,"-"));
        }
    }
    //endTime
    public static Number covertEndTime(String s){
        String input = StringUtils.defaultString(s);
        if(input.equalsIgnoreCase("NULL") || input.equals("")){
            return null;
        }else {
            return Integer.parseInt(StringUtils.substringAfter(input,"-"));
        }
    }

    public static boolean covertExperiment(String s){
        String input = StringUtils.defaultString(s);
        return !input.equalsIgnoreCase("NULL") && !input.equals("");
    }

    public static DayOfWeek covertDayOfWeek(String s){
        String input = StringUtils.defaultString(s);
        if(input.equalsIgnoreCase("NULL") || input.equals("")){
            return null;
        } else {
            return DayOfWeek.of(Integer.parseInt(s));
        }
    }

    public static EShift convertShift(String s){
        String input = StringUtils.defaultString(s);
        if(input.equalsIgnoreCase("NULL") || input.equals("")){
            return null;
        }else
            return EShift.of(input);
    }
}
