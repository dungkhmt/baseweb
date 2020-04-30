package com.hust.baseweb.applications.tms.service;

import com.hust.baseweb.applications.customer.entity.PartyCustomer;
import com.hust.baseweb.applications.geo.entity.GeoPoint;
import com.hust.baseweb.applications.logistics.entity.Product;
import com.hust.baseweb.applications.logistics.repo.ProductRepo;
import com.hust.baseweb.applications.order.repo.PartyCustomerRepo;
import com.hust.baseweb.applications.tms.entity.*;
import com.hust.baseweb.applications.tms.entity.status.DeliveryTripDetailStatus;
import com.hust.baseweb.applications.tms.entity.status.DeliveryTripStatus;
import com.hust.baseweb.applications.tms.entity.status.ShipmentItemStatus;
import com.hust.baseweb.applications.tms.model.DeliveryTripDetailModel;
import com.hust.baseweb.applications.tms.model.DeliveryTripModel;
import com.hust.baseweb.applications.tms.model.deliverytrip.GetDeliveryTripAssignedToDriverOutputModel;
import com.hust.baseweb.applications.tms.repo.*;
import com.hust.baseweb.applications.tms.repo.status.DeliveryTripDetailStatusRepo;
import com.hust.baseweb.applications.tms.repo.status.DeliveryTripStatusRepo;
import com.hust.baseweb.applications.tms.repo.status.ShipmentItemStatusRepo;
import com.hust.baseweb.entity.StatusItem;
import com.hust.baseweb.entity.UserLogin;
import com.hust.baseweb.repo.PartyRepo;
import com.hust.baseweb.repo.StatusItemRepo;
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
    private ProductRepo productRepo;
    private UserLoginRepo userLoginRepo;
    private PartyRepo partyRepo;
    private PartyDriverService partyDriverService;
    private PartyDriverRepo partyDriverRepo;
    private PartyCustomerRepo partyCustomerRepo;
    private StatusItemRepo statusItemRepo;
    private DeliveryTripStatusRepo deliveryTripStatusRepo;
    private DeliveryTripDetailStatusRepo deliveryTripDetailStatusRepo;
    private ShipmentItemStatusRepo shipmentItemStatusRepo;

    @Override
    @Transactional
    public DeliveryTrip save(DeliveryTripModel.Create input,
                             double totalDistance, // meter
                             double totalWeight, // kg
                             double totalPallet,
                             double totalExecutionTime,
                             int totalLocation,
                             int completedDeliveryTripDetailCount,
                             int deliveryTripDetailCount) {
        DeliveryPlan deliveryPlan = deliveryPlanRepo.findById(input.getDeliveryPlanId())
                .orElseThrow(NoSuchElementException::new);

        DeliveryTrip deliveryTrip = new DeliveryTrip();
        deliveryTrip.setDeliveryPlan(deliveryPlan);
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

        deliveryTrip.setDistance(totalDistance); // meter
        deliveryTrip.setTotalWeight(totalWeight);
        deliveryTrip.setTotalPallet(totalPallet);
        deliveryTrip.setTotalExecutionTime(totalExecutionTime); // second
        deliveryTrip.setTotalLocation(totalLocation);
        deliveryTrip.setCompletedDeliveryTripDetailCount(completedDeliveryTripDetailCount);
        deliveryTrip.setDeliveryTripDetailCount(deliveryTripDetailCount);

        StatusItem statusItem = statusItemRepo.findById("DELIVERY_TRIP_CREATED")
                .orElseThrow(NoSuchElementException::new);

        deliveryTrip.setStatusItem(statusItem);
        deliveryTrip = deliveryTripRepo.save(deliveryTrip);

        DeliveryTripStatus deliveryTripStatus = new DeliveryTripStatus(null,
                deliveryTrip,
                statusItem,
                new Date(),
                null);
        deliveryTripStatusRepo.save(deliveryTripStatus);

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

        List<DeliveryTripDetail> deliveryTripDetails = deliveryTripDetailRepo.findAllByDeliveryTrip(deliveryTrip);
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
                                toGeoPoint.getLatitude(),
                                toGeoPoint.getLongitude(),
                                fromGeoPoint.getLatitude(),
                                fromGeoPoint.getLongitude()
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

        //StatusItem statusItem = statusItemRepo.findByStatusId("APPROVED_TRIP");
        StatusItem statusItem = statusItemRepo.findByStatusId("DELIVERY_TRIP_APPROVED_TRIP");

        //List<DeliveryTrip> deliveryTrips = deliveryTripRepo.findByPartyDriver(partyDriver);
        List<DeliveryTrip> deliveryTrips = deliveryTripRepo.findByPartyDriverAndStatusItem(partyDriver, statusItem);

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

            List<DeliveryTripDetail> deliveryTripDetails = deliveryTripDetailRepo.findAllByDeliveryTrip(deliveryTrip);
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

                DeliveryTripDetail deliveryTripDetail = deliveryTripDetails.get(idx);
                ShipmentItem shipmentItem = deliveryTripDetail.getShipmentItem();
                //PartyCustomer partyCustomer = shipmentItem.getCustomer();
                PartyCustomer partyCustomer = partyCustomerRepo.findByPartyId(shipmentItem.getPartyCustomer()
                        .getPartyId());


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
                double latitude = shipmentItem.getShipToLocation().getGeoPoint().getLatitude();
                double longitude = shipmentItem.getShipToLocation().getGeoPoint().getLongitude();
                //UUID partyCustomerId;
                List<DeliveryTripModel.LocationItemView> items = new ArrayList<>();

                UUID deliveryTripDetailId = deliveryTripDetail.getDeliveryTripDetailId();
                UUID shipmentItemId = shipmentItem.getShipmentItemId();
                Product product = shipmentItem.getOrderItem().getProduct();
//                if (product == null) {
//                    product = productRepo.findByProductId(productId);
//                    mID2Product.put(productId, product);
//                }
                String productName = product.getProductName();
                int deliveryQuantity = deliveryTripDetail.getDeliveryQuantity();
                items.add(new DeliveryTripModel.LocationItemView(deliveryTripDetailId,
                        shipmentItemId,
                        product.getProductId(),
                        productName,
                        deliveryQuantity,
                        deliveryTripDetail.getStatusItem().getStatusId()));

                int j = idx + 1;
                while (j < deliveryTripDetails.size() &&
                        deliveryTripDetails.get(j)
                                .getShipmentItem()
                                //.getCustomer()
                                .getPartyCustomer()
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
                            deliveryQuantity,
                            deliveryTripDetails.get(j).getStatusItem().getStatusId()));
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
                    deliveryTripLocations,
                    deliveryTrip.getStatusItem().getStatusId());
        }
        return new GetDeliveryTripAssignedToDriverOutputModel(headerViews);
    }

    @Override
    @Transactional
    public boolean approveDeliveryTrip(UUID deliveryTripId) {
        Date now = new Date();

        StatusItem deliveryTripCreated = statusItemRepo.findById("DELIVERY_TRIP_CREATED")
                .orElseThrow(NoSuchElementException::new);
        StatusItem deliveryTripApprovedTrip = statusItemRepo.findById("DELIVERY_TRIP_APPROVED_TRIP")
                .orElseThrow(NoSuchElementException::new);
        StatusItem deliveryTripDetailApprovedTrip = statusItemRepo.findById("DELIVERY_TRIP_DETAIL_APPROVED_TRIP")
                .orElseThrow(NoSuchElementException::new);

        return updateDeliveryTripStatus(deliveryTripId,
                now,
                deliveryTripCreated,
                deliveryTripApprovedTrip,
                deliveryTripDetailApprovedTrip);
    }

    private void updateDeliveryTripDetailStatus(Date updateDate, StatusItem statusItem, DeliveryTrip deliveryTrip) {
        List<DeliveryTripDetail> deliveryTripDetails = deliveryTripDetailRepo.findAllByDeliveryTrip(deliveryTrip);
        for (DeliveryTripDetail deliveryTripDetail : deliveryTripDetails) {
            deliveryTripDetail.setStatusItem(statusItem);
        }
        deliveryTripDetailRepo.saveAll(deliveryTripDetails);

        List<DeliveryTripDetailStatus> deliveryTripDetailStatuses = deliveryTripDetailStatusRepo.findAllByDeliveryTripDetailInAndThruDateNull(
                deliveryTripDetails);
        deliveryTripDetailStatuses.forEach(deliveryTripDetailStatus -> deliveryTripDetailStatus.setThruDate(updateDate));

        deliveryTripDetailStatusRepo.saveAll(deliveryTripDetails.stream()
                .map(deliveryTripDetail -> new DeliveryTripDetailStatus(null,
                        deliveryTripDetail,
                        statusItem,
                        updateDate,
                        null,
                        null)).collect(Collectors.toList()));
    }

    private DeliveryTrip updateDeliveryTripStatus(DeliveryTrip deliveryTrip, Date updateDate, StatusItem statusItem) {
        if (deliveryTrip.getStatusItem().getStatusId().equals(statusItem.getStatusId())) {
            return null;
        }
        deliveryTrip.setStatusItem(statusItem);
        deliveryTrip = deliveryTripRepo.save(deliveryTrip);

        List<DeliveryTripStatus> deliveryTripStatuses = deliveryTripStatusRepo.findAllByDeliveryTripAndThruDateNull(
                deliveryTrip);
        deliveryTripStatuses.forEach(deliveryTripStatus -> deliveryTripStatus.setThruDate(updateDate));
        deliveryTripStatusRepo.saveAll(deliveryTripStatuses);

        deliveryTripStatusRepo.save(new DeliveryTripStatus(null, deliveryTrip, statusItem, updateDate, null));
        return deliveryTrip;
    }

    @Override
    public boolean startExecuteDeliveryTrip(UUID deliveryTripId) {
        Date now = new Date();

        StatusItem deliveryTripApprovedTrip = statusItemRepo.findById("DELIVERY_TRIP_APPROVED_TRIP")
                .orElseThrow(NoSuchElementException::new);
        StatusItem deliveryTripExecuted = statusItemRepo.findById("DELIVERY_TRIP_EXECUTED")
                .orElseThrow(NoSuchElementException::new);
        StatusItem deliveryTripDetailOnTrip = statusItemRepo.findById("DELIVERY_TRIP_DETAIL_ON_TRIP")
                .orElseThrow(NoSuchElementException::new);

        return updateDeliveryTripStatus(deliveryTripId,
                now,
                deliveryTripApprovedTrip,
                deliveryTripExecuted,
                deliveryTripDetailOnTrip);
    }

    @Override
    @Transactional
    public boolean deleteAll() {
        Date now = new Date();
        List<ShipmentItem> shipmentItems = deliveryTripDetailRepo.findAll()
                .stream()
                .map(DeliveryTripDetail::getShipmentItem)
                .distinct()
                .collect(Collectors.toList());
        StatusItem shipmentItemCreated = statusItemRepo.findById("SHIPMENT_ITEM_CREATED")
                .orElseThrow(NoSuchElementException::new);
        List<ShipmentItemStatus> shipmentItemStatuses = shipmentItemStatusRepo.findAllByShipmentItemInAndThruDateNull(
                shipmentItems);
        for (ShipmentItemStatus shipmentItemStatus : shipmentItemStatuses) {
            shipmentItemStatus.setThruDate(now);
        }

        for (ShipmentItem shipmentItem : shipmentItems) {
            shipmentItem.setScheduledQuantity(0);
            shipmentItem.setStatusItem(shipmentItemCreated);
            shipmentItemStatuses.add(new ShipmentItemStatus(null, shipmentItem, shipmentItemCreated, now, null));
        }

        shipmentItemRepo.saveAll(shipmentItems);
        shipmentItemStatusRepo.saveAll(shipmentItemStatuses);

        deliveryTripDetailStatusRepo.deleteAllInBatch();
        deliveryTripStatusRepo.deleteAllInBatch();

        deliveryTripDetailRepo.deleteAllInBatch();
        deliveryTripRepo.deleteAllInBatch();

        return true;
    }

    private boolean updateDeliveryTripStatus(UUID deliveryTripId,
                                             Date updateDate,
                                             StatusItem deliveryTripPreConditionStatus,
                                             StatusItem deliveryTripSetStatus,
                                             StatusItem deliveryTripDetailSetStatus) {
        DeliveryTrip deliveryTrip = deliveryTripRepo.findById(deliveryTripId).orElseThrow(NoSuchElementException::new);
        if (!deliveryTrip.getStatusItem().equals(deliveryTripPreConditionStatus)) {
            return false;
        }
        deliveryTrip = updateDeliveryTripStatus(deliveryTrip, updateDate, deliveryTripSetStatus);

        if (deliveryTrip != null) {
            updateDeliveryTripDetailStatus(updateDate, deliveryTripDetailSetStatus, deliveryTrip);
            return true;
        }
        return false;
    }


}
