package com.hust.baseweb.applications.tms.entity;

import com.hust.baseweb.applications.tms.model.VehicleModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Vehicle {

    @Id
    @Column(name = "vehicle_id")
    private String vehicleId;

    @Column(name = "capacity")
    private Double capacity;
    @Column(name = "long")
    private Integer length;
    @Column(name = "width")
    private Integer width;
    @Column(name = "height")
    private Integer height;
    @Column(name = "pallet")
    private Double pallet;
    @Column(name = "status_id")
    private String statusId;
    @Column(name = "description")
    private String description;

    @Column(name = "product_transport_category_id")
    private String productTransportCategoryId;  // KHO, LANH, DONG

    private Integer priority;

    public VehicleModel toVehicleModel() {

        return new VehicleModel(
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

    public VehicleMaintenanceHistory createVehicleMaintenanceHistory() {

        VehicleMaintenanceHistory vehicleMaintenanceHistory = new VehicleMaintenanceHistory();
        vehicleMaintenanceHistory.setVehicle(this);
        vehicleMaintenanceHistory.setMaintenanceDate(new Date());
        return vehicleMaintenanceHistory;
    }
}
