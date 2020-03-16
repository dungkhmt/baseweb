package com.hust.baseweb.applications.tms.model;

import com.poiji.annotation.ExcelCellName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public class LocationModel {


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Create {
        @ExcelCellName("Mã Ship To")
        private String locationCode;

        @ExcelCellName("T.tai Xe Tda (kg)")
        private Double maxLoadWeight;
    }
}
