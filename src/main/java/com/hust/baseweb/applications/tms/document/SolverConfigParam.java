package com.hust.baseweb.applications.tms.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;

@Getter
@Setter
@Document
@AllArgsConstructor
@NoArgsConstructor
public class SolverConfigParam {

    @MongoId
    private ObjectId id;

    private Integer maxTripDistance; // meter

    // do dai quang duong toi da cho phep giua 2 location lien tiep trong moi trip
    private Integer maxDistanceConsecutiveLocationTripAllowed; // meter

    private Integer maxLocationsPerTripAllowed;// so locations toi da tren moi trip

    private Integer fixLoadTime;// thoi gian co dinh load hang // second
    private Integer fixUnloadTime;// thoi gian co dinh unload hang // second
    private Double loadRate;// he so thoi gian dong load hang (per unit kg)
    private Double manualUnloadTime;// he so thoi gian unload hang bang tay (per unit kg) // second
    private Double palletUnloadTime;// he so thoi gian unload hang bang pallet (per unit kg) // second
    /*
    tong thoi gian phuc vu load hang tai moi diem = fixLoadTime + load_rate * kg
    tong thoi gian phuc vu unload hang tai moi diem = fixUnloadTime + unload_rate * kg
     */

    public SolverConfigParam(InputModel inputModel, Date fromDate) {

        this.maxTripDistance = inputModel.maxTripDistance;
        this.maxDistanceConsecutiveLocationTripAllowed = inputModel.maxDistanceConsecutiveLocationTripAllowed;
        this.maxLocationsPerTripAllowed = inputModel.maxLocationsPerTripAllowed;
        this.fixLoadTime = inputModel.fixLoadTime;
        this.fixUnloadTime = inputModel.fixUnloadTime;
        this.loadRate = inputModel.loadRate;
        this.manualUnloadTime = inputModel.manualUnloadTime;
        this.palletUnloadTime = inputModel.palletUnloadTime;
        this.fromDate = fromDate;
    }

    private Date fromDate;
    private Date thruDate;

    public InputModel toInputModel() {

        return new InputModel(
            maxTripDistance,
            maxDistanceConsecutiveLocationTripAllowed,
            maxLocationsPerTripAllowed,
            fixLoadTime,
            fixUnloadTime,
            loadRate,
            manualUnloadTime,
            palletUnloadTime
        );
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class InputModel {

        private Integer maxTripDistance; // meter
        // do dai quang duong toi da cho phep giua 2 location lien tiep trong moi trip
        private Integer maxDistanceConsecutiveLocationTripAllowed;

        private Integer maxLocationsPerTripAllowed;// so locations toi da tren moi trip

        private Integer fixLoadTime;// thoi gian co dinh load hang
        private Integer fixUnloadTime;// thoi gian co dinh unload hang
        private Double loadRate;// he so thoi gian dong load hang (per unit kg)
        private Double manualUnloadTime;// he so thoi gian unload hang bang tay (per unit kg) // second
        private Double palletUnloadTime;// he so thoi gian unload hang bang pallet (per unit kg) // second
    }
}
