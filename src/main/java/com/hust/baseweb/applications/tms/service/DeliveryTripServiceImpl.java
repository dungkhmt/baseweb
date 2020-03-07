package com.hust.baseweb.applications.tms.service;

import com.hust.baseweb.applications.geo.entity.GeoPoint;
import com.hust.baseweb.applications.logistics.entity.Product;
import com.hust.baseweb.applications.logistics.repo.ProductRepo;
import com.hust.baseweb.applications.tms.entity.*;
import com.hust.baseweb.applications.tms.model.createdeliverytrip.CreateDeliveryTripDetailInputModel;
import com.hust.baseweb.applications.tms.model.createdeliverytrip.CreateDeliveryTripInputModel;
import com.hust.baseweb.applications.tms.model.deliverytrip.DeliveryTripInfoModel;
import com.hust.baseweb.applications.tms.model.deliverytrip.DeliveryTripModel;
import com.hust.baseweb.applications.tms.repo.*;
import com.hust.baseweb.utils.LatLngUtils;
import com.hust.baseweb.utils.algorithm.DistanceUtils;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
public class DeliveryTripServiceImpl implements DeliveryTripService {

    private DeliveryTripRepo deliveryTripRepo;
    private DeliveryPlanRepo deliveryPlanRepo;
    private VehicleRepo vehicleRepo;
    private VehicleMaintenanceHistoryRepo vehicleMaintenanceHistoryRepo;
    private ShipmentItemDeliveryPlanRepo shipmentItemDeliveryPlanRepo;
    private ShipmentItemRepo shipmentItemRepo;
    private DeliveryTripDetailRepo deliveryTripDetailRepo;
    private DistanceTravelTimeGeoPointRepo distanceTravelTimeGeoPointRepo;
    private ProductRepo productRepo;

    @Override
    @Transactional
    public DeliveryTrip save(CreateDeliveryTripInputModel input) {
        DeliveryTrip deliveryTrip = new DeliveryTrip();
        deliveryTrip.setDeliveryPlan(deliveryPlanRepo.findByDeliveryPlanId(input.getDeliveryPlanId()));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date executeDate = null;
        try {
            executeDate = formatter.parse(input.getExecuteDate());
        } catch (Exception e) {
            e.printStackTrace();
        }
        deliveryTrip.setExecuteDate(executeDate);

        Vehicle vehicle = vehicleRepo.findById(input.getVehicleId())
                .orElseGet(() -> {
                    Vehicle v = new Vehicle(input.getVehicleId(), null, null, null, null, null, null, null);
                    vehicleRepo.save(v);
                    vehicleMaintenanceHistoryRepo.save(v.createVehicleMaintenanceHistory());
                    return v;
                });

        deliveryTrip.setVehicle(vehicle);

        deliveryTrip.setDistance(0.0);
        deliveryTrip.setTotalWeight(0.0);
        deliveryTrip.setTotalPallet(0.0);

        deliveryTrip = deliveryTripRepo.save(deliveryTrip);
        return deliveryTrip;
    }

    @Override
    public Page<DeliveryTripModel> findAllByDeliveryPlanId(String deliveryPlanId, Pageable pageable) {
        DeliveryPlan deliveryPlan = new DeliveryPlan();
        deliveryPlan.setDeliveryPlanId(UUID.fromString(deliveryPlanId));
        return deliveryTripRepo.findAllByDeliveryPlan(deliveryPlan, pageable).map(DeliveryTrip::toDeliveryTripModel);
    }

    @Override
    public List<DeliveryTripModel> findAllByDeliveryPlanId(String deliveryPlanId) {
        DeliveryPlan deliveryPlan = new DeliveryPlan();
        deliveryPlan.setDeliveryPlanId(UUID.fromString(deliveryPlanId));
        List<DeliveryTrip> deliveryTrips = deliveryTripRepo.findAllByDeliveryPlan(deliveryPlan);
        return deliveryTrips.stream().map(DeliveryTrip::toDeliveryTripModel).collect(Collectors.toList());
    }

    @Override
    public DeliveryTrip findById(UUID deliveryTripId) {
        return deliveryTripRepo.findById(deliveryTripId).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public DeliveryTripInfoModel getDeliveryTripInfo(String deliveryTripId,
                                                     List<CreateDeliveryTripDetailInputModel> shipmentItemModels) {
        DeliveryTrip deliveryTrip = deliveryTripRepo.findById(UUID.fromString(deliveryTripId)).orElseThrow(NoSuchElementException::new);
        String deliveryPlanId = deliveryTrip.getDeliveryPlan().getDeliveryPlanId().toString();

        List<ShipmentItemDeliveryPlan> shipmentItemDeliveryPlans
                = shipmentItemDeliveryPlanRepo.findAllByDeliveryPlanId(UUID.fromString(deliveryPlanId));

        List<ShipmentItem> shipmentItemsInDeliveryPlan = shipmentItemRepo.findAllByShipmentItemIdIn(shipmentItemDeliveryPlans.stream()
                .map(ShipmentItemDeliveryPlan::getShipmentItemId).collect(Collectors.toList()));
        Map<String, ShipmentItem> shipmentItemMap = new HashMap<>();
        shipmentItemsInDeliveryPlan.forEach(shipmentItem -> shipmentItemMap.put(shipmentItem.getShipmentItemId().toString(), shipmentItem));

        List<DeliveryTripDetail> deliveryTripDetails = deliveryTripDetailRepo.findAllByDeliveryTripId(UUID.fromString(deliveryTripId));
        List<ShipmentItem> shipmentItemsInDeliveryTrip = deliveryTripDetails
                .stream().map(DeliveryTripDetail::getShipmentItem).collect(Collectors.toList());

        List<GeoPoint> geoPointsInDeliveryTrip = shipmentItemsInDeliveryTrip.stream()
                .map(shipmentItem -> shipmentItem.getShipToLocation().getGeoPoint()).distinct().collect(Collectors.toList());

        List<ShipmentItem> shipmentItemsSelected =
                shipmentItemRepo.findAllByShipmentItemIdIn(shipmentItemModels.stream().map(
                        CreateDeliveryTripDetailInputModel::getShipmentItemId
                ).collect(Collectors.toList()));

        List<GeoPoint> geoPointsSelected = shipmentItemsSelected.stream()
                .map(shipmentItem -> shipmentItem.getShipToLocation().getGeoPoint()).distinct().collect(Collectors.toList());

        List<GeoPoint> allGeoPoints = new ArrayList<>(geoPointsInDeliveryTrip);
        allGeoPoints.addAll(geoPointsSelected);

        double totalDistance = DistanceUtils.calculateGreedyTotalDistance(allGeoPoints, (fromGeoPoint, toGeoPoint) -> {
//            DistanceTravelTimeGeoPoint distanceTravelTimeGeoPoint
//                    = distanceTravelTimeGeoPointRepo.findByFromGeoPointAndToGeoPoint(fromGeoPoint, toGeoPoint);
//            if (distanceTravelTimeGeoPoint == null) {   // Haversine formula
            return LatLngUtils.distance(
                    Double.parseDouble(toGeoPoint.getLatitude()),
                    Double.parseDouble(toGeoPoint.getLongitude()),
                    Double.parseDouble(fromGeoPoint.getLatitude()),
                    Double.parseDouble(fromGeoPoint.getLongitude())
            );
//            } else {
//                return distanceTravelTimeGeoPoint.getDistance();
//            }
        });

        Map<String, Product> productMap = new HashMap<>();
        productRepo.findAllByProductIdIn(shipmentItemsInDeliveryPlan
                .stream().map(ShipmentItem::getProductId).collect(Collectors.toList()))
                .forEach(product -> productMap.put(product.getProductId(), product));

        double totalWeight = 0;
        double totalPallet = 0;

        for (DeliveryTripDetail deliveryTripDetail : deliveryTripDetails) {
            ShipmentItem shipmentItem = deliveryTripDetail.getShipmentItem();
            Product product = productMap.get(shipmentItem.getProductId());
            if(product == null){
            	log.info("getDeliveryTripInfo, cannot find product of productId " + shipmentItem.getProductId());
            }else{
            	log.info("getDeliveryTripInfo, product " + product.getProductName() + " weight = " + product.getWeight());
            }
            if(deliveryTripDetail == null){
            	log.info("getDeliveryTripInfo, deliveryTripDetail is null");
            }else{
            	log.info("getDeliveryTripInfo, deliveryTripDetail quantity = " + deliveryTripDetail.getDeliveryQuantity());
            }
            totalWeight += product.getWeight() * deliveryTripDetail.getDeliveryQuantity();
            totalPallet += shipmentItem.getPallet() / shipmentItem.getQuantity() * deliveryTripDetail.getDeliveryQuantity();
        }

        for (CreateDeliveryTripDetailInputModel shipmentItemModel : shipmentItemModels) {
            ShipmentItem shipmentItem = shipmentItemMap.get(shipmentItemModel.getShipmentItemId().toString());
            Product product = productMap.get(shipmentItem.getProductId());
            totalWeight += product.getWeight() / shipmentItem.getQuantity() * shipmentItemModel.getDeliveryQuantity();
            totalPallet += shipmentItem.getPallet() / shipmentItem.getQuantity() * shipmentItemModel.getDeliveryQuantity();
        }

        return new DeliveryTripInfoModel(deliveryTripId, totalDistance, totalWeight, totalPallet);
    }


}
