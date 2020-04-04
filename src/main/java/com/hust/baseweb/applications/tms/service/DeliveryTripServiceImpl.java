package com.hust.baseweb.applications.tms.service;

import com.hust.baseweb.applications.customer.entity.PartyCustomer;
import com.hust.baseweb.applications.geo.entity.GeoPoint;
import com.hust.baseweb.applications.logistics.entity.Product;
import com.hust.baseweb.applications.logistics.repo.ProductRepo;
import com.hust.baseweb.applications.tms.entity.*;
import com.hust.baseweb.applications.tms.model.DeliveryTripDetailModel;
import com.hust.baseweb.applications.tms.model.DeliveryTripModel;
import com.hust.baseweb.applications.tms.model.deliverytrip.GetDeliveryTripAssignedToDriverOutputModel;
import com.hust.baseweb.applications.tms.repo.*;
import com.hust.baseweb.entity.UserLogin;
import com.hust.baseweb.repo.PartyRepo;
import com.hust.baseweb.repo.UserLoginRepo;
import com.hust.baseweb.utils.Constant;
import com.hust.baseweb.utils.LatLngUtils;
import com.hust.baseweb.utils.algorithm.DistanceUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private UserLoginRepo userLoginRepo;
    private PartyRepo partyRepo;
    private PartyDriverService partyDriverService;
    private PartyDriverRepo partyDriverRepo;

    @Override
    @Transactional
    public DeliveryTrip save(DeliveryTripModel.Create input,
                             double totalDistance,
                             double totalWeight,
                             double totalPallet,
                             double totalExecutionTime,
                             int totalLocation) {
        DeliveryTrip deliveryTrip = new DeliveryTrip();
        deliveryTrip.setDeliveryPlan(deliveryPlanRepo.findByDeliveryPlanId(input.getDeliveryPlanId()));
        Date executeDate = null;
        try {
            executeDate = Constant.DATE_FORMAT.parse(input.getExecuteDate());
        } catch (Exception e) {
            e.printStackTrace();
        }
        deliveryTrip.setExecuteDate(executeDate);

        Vehicle vehicle = vehicleRepo.findById(input.getVehicleId()).orElseThrow(NoSuchElementException::new);

        deliveryTrip.setVehicle(vehicle);

        if (input.getDriverId() != null) {
            PartyDriver partyDriver = partyDriverService.findByPartyId(UUID.fromString(input.getDriverId()));
            deliveryTrip.setPartyDriver(partyDriver);
        }

        deliveryTrip.setDistance(totalDistance);
        deliveryTrip.setTotalWeight(totalWeight);
        deliveryTrip.setTotalPallet(totalPallet);
        deliveryTrip.setTotalExecutionTime(totalExecutionTime);
        deliveryTrip.setTotalLocation(totalLocation);

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
    public DeliveryTripModel findById(UUID deliveryTripId) {
        return deliveryTripRepo.findById(deliveryTripId).orElseThrow(NoSuchElementException::new).toDeliveryTripModel();
    }

    @Override
    public DeliveryTripModel.Tour getDeliveryTripInfo(String deliveryTripId,
                                                      List<DeliveryTripDetailModel.Create> shipmentItemModels) {
        DeliveryTrip deliveryTrip = deliveryTripRepo.findById(UUID.fromString(deliveryTripId))
                .orElseThrow(NoSuchElementException::new);
        DeliveryPlan deliveryPlan = deliveryTrip.getDeliveryPlan();
        String deliveryPlanId = deliveryPlan.getDeliveryPlanId().toString();

        List<ShipmentItemDeliveryPlan> shipmentItemDeliveryPlans
                = shipmentItemDeliveryPlanRepo.findAllByDeliveryPlanId(UUID.fromString(deliveryPlanId));

        List<ShipmentItem> shipmentItemsInDeliveryPlan = shipmentItemRepo.findAllByShipmentItemIdIn(
                shipmentItemDeliveryPlans.stream()
                        .map(ShipmentItemDeliveryPlan::getShipmentItemId).collect(Collectors.toList()));
        Map<String, ShipmentItem> shipmentItemMap = new HashMap<>();
        shipmentItemsInDeliveryPlan.forEach(shipmentItem -> shipmentItemMap.put(shipmentItem.getShipmentItemId()
                .toString(), shipmentItem));

        List<DeliveryTripDetail> deliveryTripDetails = deliveryTripDetailRepo.findAllByDeliveryTripId(UUID.fromString(
                deliveryTripId));
        List<ShipmentItem> shipmentItemsInDeliveryTrip = deliveryTripDetails
                .stream().map(DeliveryTripDetail::getShipmentItem).collect(Collectors.toList());

        List<GeoPoint> geoPointsInDeliveryTrip = shipmentItemsInDeliveryTrip.stream()
                .map(shipmentItem -> shipmentItem.getShipToLocation().getGeoPoint())
                .distinct()
                .collect(Collectors.toList());

        List<ShipmentItem> shipmentItemsSelected =
                shipmentItemRepo.findAllByShipmentItemIdIn(shipmentItemModels.stream().map(
                        DeliveryTripDetailModel.Create::getShipmentItemId
                ).collect(Collectors.toList()));

        List<GeoPoint> geoPointsSelected = shipmentItemsSelected.stream()
                .map(shipmentItem -> shipmentItem.getShipToLocation().getGeoPoint())
                .distinct()
                .collect(Collectors.toList());

        List<GeoPoint> allGeoPoints = new ArrayList<>(geoPointsInDeliveryTrip);
        allGeoPoints.addAll(geoPointsSelected);

        DistanceUtils.DirectionSolution<GeoPoint> directionSolution;
        if (allGeoPoints.isEmpty()) {
            directionSolution = new DistanceUtils.DirectionSolution<>(0.0, new ArrayList<>());
        } else {
            directionSolution = DistanceUtils.calculateGreedyTotalDistance(allGeoPoints,
                    allGeoPoints.get(0),    // TODO: temporary set the first point to depot
                    (fromGeoPoint, toGeoPoint) -> {
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
        }

        double totalWeight = 0;
        double totalPallet = 0;

        for (DeliveryTripDetail deliveryTripDetail : deliveryTripDetails) {
            ShipmentItem shipmentItem = deliveryTripDetail.getShipmentItem();
            Product product = shipmentItem.getOrderItem().getProduct();
            totalWeight += product.getWeight() * deliveryTripDetail.getDeliveryQuantity();
            totalPallet += shipmentItem.getPallet() / shipmentItem.getQuantity() *
                    deliveryTripDetail.getDeliveryQuantity();
        }

        for (DeliveryTripDetailModel.Create shipmentItemModel : shipmentItemModels) {
            ShipmentItem shipmentItem = shipmentItemMap.get(shipmentItemModel.getShipmentItemId().toString());
            Product product = shipmentItem.getOrderItem().getProduct();
            totalWeight += product.getWeight() * shipmentItemModel.getDeliveryQuantity();
            totalPallet += shipmentItem.getPallet() / shipmentItem.getQuantity() *
                    shipmentItemModel.getDeliveryQuantity();
        }

        return new DeliveryTripModel.Tour(deliveryTripId,
                directionSolution.getDistance(),
                totalWeight,
                totalPallet,
                directionSolution.getTour());
    }

    @Override
    public GetDeliveryTripAssignedToDriverOutputModel getDeliveryTripAssignedToDriver(
            String driverUserLoginId) {
        UserLogin userLogin = userLoginRepo.findByUserLoginId(driverUserLoginId);
        PartyDriver partyDriver = partyDriverRepo.findByPartyId(userLogin.getParty().getPartyId());

        List<DeliveryTrip> deliveryTrips = deliveryTripRepo.findByPartyDriver(partyDriver);

        log.info("getDeliveryTripAssignedToDriver, got deliveryTrips.sz = " + deliveryTrips.size());

        DeliveryTripModel.HeaderView[] headerViews = new DeliveryTripModel.HeaderView[deliveryTrips.size()];
        HashMap<String, Product> mID2Product = new HashMap<>();
        for (int i = 0; i < deliveryTrips.size(); i++) {
            DeliveryTrip deliveryTrip = deliveryTrips.get(i);
            UUID deliveryTripId = deliveryTrip.getDeliveryTripId();
            String vehicleId = deliveryTrip.getVehicle().getVehicleId();
            UUID driverPartyId = deliveryTrip.getPartyDriver().getPartyId();
            //String driverUserLoginId;
            Date executeDate = deliveryTrip.getExecuteDate();

            List<DeliveryTripModel.LocationView> deliveryTripLocations = new ArrayList<>();

            List<DeliveryTripDetail> deliveryTripDetails = deliveryTripDetailRepo.findAllByDeliveryTripId(deliveryTripId);
            log.info("getDeliveryTripAssignedToDriver, dtd of deliveryTripId " +
                    deliveryTripId +
                    " = " +
                    deliveryTripDetails.size());

            deliveryTripDetails.sort(Comparator.comparingInt(DeliveryTripDetail::getSequenceId));
            for (int j = 0; j < deliveryTripDetails.size(); j++) {
                log.info("getDeliveryTripAssignedToDriver dtd(" +
                        j +
                        ") " +
                        deliveryTripDetails.get(j).getDeliveryQuantity() +
                        ", " +
                        deliveryTripDetails.get(j).getDeliveryTripDetailId());
            }
            int idx = 0;
            while (idx < deliveryTripDetails.size()) {
                //List<DeliveryTripLocationView> deliveryTripLocationViews = new ArrayList<>();

                ShipmentItem shipmentItem = deliveryTripDetails.get(idx).getShipmentItem();
                PartyCustomer partyCustomer = shipmentItem.getCustomer();
//                if (partyCustomer != null) {
//                    log.info("getDeliveryTripAssignedToDriver dtd[" + idx + "], shipmentItemId = " + shipmentItem.getShipmentItemId() +
//                            ", customer = " + partyCustomer.getCustomerName());
//                } else {
//                    log.info("getDeliveryTripAssignedToDriver dtd[" + idx + "], shipmentItemId = " + shipmentItem.getShipmentItemId() +
//                            ", customer = " + " NULL");
//                }
                UUID partyCustomerId = partyCustomer.getPartyId();
                String customerName = partyCustomer.getCustomerName();
                String address = shipmentItem.getShipToLocation().getAddress();
                double latitude = Double.parseDouble(shipmentItem.getShipToLocation().getGeoPoint().getLatitude());
                double longitude = Double.parseDouble(shipmentItem.getShipToLocation().getGeoPoint().getLongitude());
                //UUID partyCustomerId;
                List<DeliveryTripModel.LocationItemView> items = new ArrayList<>();

                UUID deliveryTripDetailId = deliveryTripDetails.get(idx).getDeliveryTripDetailId();
                UUID shipmentItemId = shipmentItem.getShipmentItemId();
                Product product = shipmentItem.getOrderItem().getProduct();
//                if (product == null) {
//                    product = productRepo.findByProductId(productId);
//                    mID2Product.put(productId, product);
//                }
                String productName = product.getProductName();
                int deliveryQuantity = deliveryTripDetails.get(idx).getDeliveryQuantity();
                items.add(new DeliveryTripModel.LocationItemView(deliveryTripDetailId,
                        shipmentItemId,
                        product.getProductId(),
                        productName,
                        deliveryQuantity));

                int j = idx + 1;
                while (j < deliveryTripDetails.size() &&
                        deliveryTripDetails.get(j)
                                .getShipmentItem()
                                .getCustomer()
                                .getPartyId()
                                .equals(partyCustomerId)) {
                    shipmentItem = deliveryTripDetails.get(j).getShipmentItem();
                    deliveryTripDetailId = deliveryTripDetails.get(j).getDeliveryTripDetailId();
                    shipmentItemId = shipmentItem.getShipmentItemId();
                    product = shipmentItem.getOrderItem().getProduct();
//                    if (product == null) {
//                        product = productRepo.findByProductId(productId);
//                        mID2Product.put(productId, product);
//                    }
                    productName = product.getProductName();
                    deliveryQuantity = deliveryTripDetails.get(j).getDeliveryQuantity();
                    items.add(new DeliveryTripModel.LocationItemView(deliveryTripDetailId,
                            shipmentItemId,
                            product.getProductId(),
                            productName,
                            deliveryQuantity));
                    j++;
                }

                deliveryTripLocations.add(new DeliveryTripModel.LocationView(customerName,
                        address,
                        latitude,
                        longitude,
                        partyCustomerId,
                        items));
                idx = j;
            }
            headerViews[i] = new DeliveryTripModel.HeaderView(deliveryTripId,
                    vehicleId,
                    driverPartyId,
                    driverUserLoginId,
                    executeDate,
                    deliveryTripLocations);
        }
        return new GetDeliveryTripAssignedToDriverOutputModel(headerViews);
    }


}
