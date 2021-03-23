package com.hust.baseweb.applications.education.exception;

import com.hust.baseweb.applications.education.suggesttimetable.enums.Error;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class CustomException extends Exception{
    List<Error> listError = new ArrayList<>();

    public CustomException(List<Error> listError) {
        this.listError = listError;
    }

    @Override
    public String getMessage(){
        String str="";
        for (Error exception : listError) {
            str += exception.getDescription() + "\n";
        }
        str = StringUtils.chop(str);
        return str;
    }


    public List<Error> getListError() {
        return listError;
    }
}
