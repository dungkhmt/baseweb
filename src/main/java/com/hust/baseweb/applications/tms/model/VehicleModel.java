package com.hust.baseweb.applications.tms.model;

import com.hust.baseweb.applications.geo.entity.PostalAddress;
import com.hust.baseweb.applications.tms.entity.Vehicle;
import com.hust.baseweb.applications.tms.entity.VehicleLocationPriority;
import com.poiji.annotation.ExcelCellName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VehicleModel {

    private String vehicleId;
    private Double capacity;
    private Integer length;
    private Integer width;
    private Integer height;
    private Double pallet;
    private String statusId;
    private String description;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateLocationPriority {
        @ExcelCellName("Số xe")
        private String vehicleId;

        @ExcelCellName("Shipto")
        private String locationCode;

        @ExcelCellName("Độ ưu tiên xe phục vụ shipto (giá trị từ 1-4, 1 thể hiện ưu tiên cao nhất)")
        private Integer priority;

        public VehicleLocationPriority toVehicleLocationPriority(Vehicle vehicle, PostalAddress postalAddress) {
            if (vehicle == null || postalAddress == null) {
                return null;
            }
            VehicleLocationPriority vehicleLocationPriority = new VehicleLocationPriority();
            vehicleLocationPriority.setVehicle(vehicle);
            vehicleLocationPriority.setPostalAddress(postalAddress);
            vehicleLocationPriority.setFromDate(new Date());
            vehicleLocationPriority.setPriority(priority);
            return vehicleLocationPriority;
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Create {
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

        @ExcelCellName("Loại hàng")
        private String productTransportCategoryId;

        @ExcelCellName("Xe của  Vinamilk")
        private String priority;

        public Vehicle toVehicle() {
            return new Vehicle(
                    vehicleId,
                    capacity,
                    length,
                    width,
                    height,
                    pallet,
                    statusId,
                    description,
                    productTransportCategoryId,
                    priority.equals("Y") ? 1 : 2
            );
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateDeliveryPlan {
        private String deliveryPlanId;
        private List<String> vehicleIds;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DeleteDeliveryPlan {
        private String deliveryPlanId;
        private String vehicleId;
    }

    @Getter
    @Setter
    public static class InputDistanceStatistic {
        private String fromDate;
        private String thruDate;
    }

    @Getter
    @Setter
    public static class OutputDistanceStatistic {
        private String[] vehicleId;
        private double[] distance;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Distance {
        private String vehicleId;
        private double distance;
    }
}
