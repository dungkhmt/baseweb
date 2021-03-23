package com.hust.baseweb.applications.education.suggesttimetable.enums;


import com.hust.baseweb.applications.education.exception.CustomException;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@Getter
public enum Error {

    classId_error(0,"Mã lớp lỗi tại :"),
    attachedClassId_error(1,"Mã lớp kèm lỗi tại các hàng:"),
    courseId_error(2,"Mã học phần lỗi tại các hàng: "),
    credit_error(3,"Khối lượng lỗi tại các hàng: "),
    note_error(4,"Ghi chú lỗi tại các hàng: "),
    dayOfWeek_error(5,"Thứ lỗi tại các hàng: "),
    startTime_error(6,"Thời gian bắt đầu lỗi tại các hàng: "),
    endTime_error(7,"Thời gian bắt đầu lỗi tại các hàng: "),
    shift_error(8,"Kíp lỗi tại các hàng: "),
    weeks_error(9,"Tuần lỗi tại các hàng: "),
    room_error(10,"Phòng lỗi tại các hàng: "),
    needExperiment_error(11,"Cần_TN lỗi tại các hàng: "),
    numRegistration_error(12,"SLĐK lỗi tại các hàng: "),
    maxQuantity_error(13,"SL_MAX lỗi tại các hàng: "),
    status_error(14,"Trạng thái lỗi tại các hàng: "),
    classType_error(15,"Loại lớp lỗi tại các hàng: "),
    managementId_error(16,"Mã_QL lỗi tại các hàng: "),
    name_error(17,"Tên học phần lỗi tại các hàng: "),
    eName_error(18,"Tên học phần tiếng anh lỗi tại các hàng: "),
    department_error(19,"Khoa viện lỗi tại các hàng: ");

    private final int code;
    private final String description;

    private static HashMap<Error,List<Integer>> errorListHashMap = new HashMap<>();


    Error(int code, String description) {
        this.code = code;
        this.description = description;
    }

    @Override
    public String toString() {
        return code + ": " + description;
    }


    public static String handleError(LinkedHashMap<CustomException, Integer> errorLists){
        StringBuilder str = new StringBuilder();
        errorListHashMap.put(Error.classId_error,new ArrayList<>());
        errorListHashMap.put(Error.attachedClassId_error,new ArrayList<>());
        errorListHashMap.put(Error.courseId_error,new ArrayList<>());
        errorListHashMap.put(Error.credit_error,new ArrayList<>());
        errorListHashMap.put(Error.note_error,new ArrayList<>());
        errorListHashMap.put(Error.status_error,new ArrayList<>());
        errorListHashMap.put(Error.dayOfWeek_error,new ArrayList<>());
        errorListHashMap.put(Error.startTime_error,new ArrayList<>());
        errorListHashMap.put(Error.endTime_error,new ArrayList<>());
        errorListHashMap.put(Error.shift_error,new ArrayList<>());
        errorListHashMap.put(Error.weeks_error,new ArrayList<>());
        errorListHashMap.put(Error.room_error,new ArrayList<>());
        errorListHashMap.put(Error.needExperiment_error,new ArrayList<>());
        errorListHashMap.put(Error.numRegistration_error,new ArrayList<>());
        errorListHashMap.put(Error.maxQuantity_error,new ArrayList<>());
        errorListHashMap.put(Error.classType_error,new ArrayList<>());
        errorListHashMap.put(Error.managementId_error,new ArrayList<>());
        errorListHashMap.put(Error.name_error,new ArrayList<>());
        errorListHashMap.put(Error.eName_error,new ArrayList<>());
        errorListHashMap.put(Error.department_error,new ArrayList<>());
        for (CustomException c : errorLists.keySet()) {
            int k = errorLists.get(c);
            for (Error error : c.getListError()) {
                int code = error.getCode();
                switch (code) {
                    case 0:
                        errorListHashMap.get(Error.classId_error).add(k);
                        break;
                    case 1:
                        errorListHashMap.get(Error.attachedClassId_error).add(k);
                        break;
                    case 2:
                        errorListHashMap.get(Error.courseId_error).add(k);
                        break;
                    case 3:
                        errorListHashMap.get(Error.credit_error).add(k);
                        break;
                    case 4:
                        errorListHashMap.get(Error.note_error).add(k);
                        break;
                    case 5:
                        errorListHashMap.get(Error.dayOfWeek_error).add(k);
                        break;
                    case 6:
                        errorListHashMap.get(Error.startTime_error).add(k);
                        break;
                    case 7:
                        errorListHashMap.get(Error.endTime_error).add(k);
                        break;
                    case 8:
                        errorListHashMap.get(Error.shift_error).add(k);
                        break;
                    case 9:
                        errorListHashMap.get(Error.weeks_error).add(k);
                        break;
                    case 10:
                        errorListHashMap.get(Error.room_error).add(k);
                        break;
                    case 11:
                        errorListHashMap.get(Error.needExperiment_error).add(k);
                        break;
                    case 12:
                        errorListHashMap.get(Error.numRegistration_error).add(k);
                        break;
                    case 13:
                        errorListHashMap.get(Error.maxQuantity_error).add(k);
                        break;
                    case 14:
                        errorListHashMap.get(Error.status_error).add(k);
                        break;
                    case 15:
                        errorListHashMap.get(Error.classType_error).add(k);
                        break;
                    case 16:
                        errorListHashMap.get(Error.managementId_error).add(k);
                        break;
                    case 17:
                        errorListHashMap.get(Error.name_error).add(k);
                        break;
                    case 18:
                        errorListHashMap.get(Error.eName_error).add(k);
                        break;
                    case 19:
                        errorListHashMap.get(Error.department_error).add(k);
                        break;
                }

            }
        }


        for (Error error : errorListHashMap.keySet()) {
            if (errorListHashMap.get(error).size() > 0) {
                str.append(error.getDescription()).append("\n");
                for (int i : errorListHashMap.get(error)) {
                    str.append(i).append("\n");
                }
            }
        }

        return str.toString();
    }

}

