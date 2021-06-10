package com.hust.baseweb.applications.waterresourcesmanagement.model;

import com.hust.baseweb.applications.waterresourcesmanagement.entity.Lake;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LakeLiveInfoModel {

    private Lake lake;
    private int[] mucNuocLuKiemTraHistory;
    private int[] luuLuongXaLuKiemTraHistory;
}
