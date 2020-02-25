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

    public Vehicle toVehicle() {
        return new Vehicle(vehicleId);
    }
}
