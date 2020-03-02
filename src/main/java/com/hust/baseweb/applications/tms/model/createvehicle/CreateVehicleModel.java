package com.hust.baseweb.applications.tms.model.createvehicle;

import com.hust.baseweb.applications.tms.entity.Vehicle;
import com.poiji.annotation.ExcelCellName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateVehicleModel {
    @ExcelCellName("Số xe")
    private String vehicleId;

    @ExcelCellName("Tải trọng  (Tấn)")
    private Double capacity;
    private Integer length;
    private Integer width;
    private Integer height;
    @ExcelCellName("SL Pallet  Tối đa")
    private Double pallet;
    private String statusId;
    private String description;

    public Vehicle toVehicle() {
        return new Vehicle(
                vehicleId,
                capacity,
                length,
                width,
                height,
                pallet,
                statusId,
                description
        );
    }
}
