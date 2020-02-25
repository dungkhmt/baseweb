package com.hust.baseweb.applications.tms.service;

import com.google.maps.model.LatLng;
import com.hust.baseweb.applications.customer.entity.PartyCustomer;
import com.hust.baseweb.applications.geo.entity.GeoPoint;
import com.hust.baseweb.applications.geo.entity.PostalAddress;
import com.hust.baseweb.applications.geo.repo.GeoPointRepo;
import com.hust.baseweb.applications.geo.repo.PostalAddressRepo;
import com.hust.baseweb.applications.order.repo.PartyCustomerRepo;
import com.hust.baseweb.applications.tms.entity.Shipment;
import com.hust.baseweb.applications.tms.entity.ShipmentItem;
import com.hust.baseweb.applications.tms.model.shipmentorder.CreateShipmentInputModel;
import com.hust.baseweb.applications.tms.model.shipmentorder.CreateShipmentItemInputModel;
import com.hust.baseweb.applications.tms.model.shipmentorder.ShipmentModel;
import com.hust.baseweb.applications.tms.repo.ShipmentItemRepo;
import com.hust.baseweb.applications.tms.repo.ShipmentRepo;
import com.hust.baseweb.utils.CommonUtils;
import com.hust.baseweb.utils.GoogleMapUtils;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
public class ShipmentServiceImpl implements ShipmentService {
    private ShipmentRepo shipmentRepo;
    private ShipmentItemRepo shipmentItemRepo;

    private PartyCustomerRepo partyCustomerRepo;
    private PostalAddressRepo postalAddressRepo;
    private GeoPointRepo geoPointRepo;

    @Override
    @Transactional
    public Shipment save(CreateShipmentInputModel input) {

        UUID shipmentId = UUID.randomUUID();
        Shipment shipment = new Shipment();
        shipment.setShipmentId(shipmentId);
        shipment.setShipmentTypeId("SALES_SHIPMENT");
        shipmentRepo.save(shipment);

        List<CreateShipmentItemInputModel> shipmentItemInputModels = Arrays.asList(input.getShipmentItems());
        List<String> customerCodes = shipmentItemInputModels.stream()
                .map(CreateShipmentItemInputModel::getCustomerCode).distinct().collect(Collectors.toList());
        List<String> locationCodes = shipmentItemInputModels.stream()
                .map(CreateShipmentItemInputModel::getLocationCode).distinct().collect(Collectors.toList());

        Map<String, PartyCustomer> partyCustomerMap = new HashMap<>();
        partyCustomerRepo.findAllByCustomerCodeIn(customerCodes).forEach(partyCustomer -> partyCustomerMap.put(partyCustomer.getCustomerCode(), partyCustomer));
        customerCodes.forEach(customerCode -> partyCustomerMap.computeIfAbsent(customerCode,
                k -> new PartyCustomer(
                        UUID.randomUUID(),
                        customerCode,
                        customerCode,
                        new ArrayList<>()
                )
        ));

        Map<String, PostalAddress> postalAddressMap = new HashMap<>();
        Map<String, GeoPoint> locationCodeToGeoPoint = new HashMap<>();
        postalAddressRepo.findAllByLocationCodeIn(locationCodes).forEach(postalAddress -> postalAddressMap.put(postalAddress.getLocationCode(), postalAddress));
        locationCodes.forEach(locationCode -> postalAddressMap.computeIfAbsent(locationCode, k -> {
            GeoPoint geoPoint = new GeoPoint(UUID.randomUUID(), null, null);
            locationCodeToGeoPoint.put(locationCode, geoPoint);
            return new PostalAddress(
                    UUID.randomUUID(),
                    locationCode,
                    null,
                    geoPoint
            );
        }));

        int idx = 0;
        List<ShipmentItem> shipmentItems = new ArrayList<>();
        for (int i = 0; i < input.getShipmentItems().length; i++) {
            CreateShipmentItemInputModel shipmentItemModel = input.getShipmentItems()[i];
            log.info("::save, idx = " + idx + ", product = " + shipmentItemModel.getProductId() + ", quantity = " + shipmentItemModel.getQuantity() + " pallet = " + shipmentItemModel.getPallet());

            idx++;
            String shipmentItemSeqId = CommonUtils.buildSeqId(idx);
            ShipmentItem shipmentItem = new ShipmentItem();
            shipmentItem.setShipmentId(shipmentId);
            shipmentItem.setShipmentItemSeqId(shipmentItemSeqId);
            shipmentItem.setQuantity(shipmentItemModel.getQuantity());
            shipmentItem.setPallet(shipmentItemModel.getPallet());
            shipmentItem.setProductId(shipmentItemModel.getProductId());

            shipmentItems.add(shipmentItem);

            PostalAddress postalAddress = postalAddressMap.get(shipmentItemModel.getLocationCode());
            postalAddress.setAddress(shipmentItemModel.getAddress());

            partyCustomerMap.get(shipmentItemModel.getCustomerCode()).getPostalAddress().add(postalAddress);

            GeoPoint geoPoint = locationCodeToGeoPoint.get(shipmentItemModel.getLocationCode());
            if (geoPoint.getLatitude() == null) {
                log.info("Query latLng: " + shipmentItemModel.getAddress());
                LatLng location = GoogleMapUtils.queryLatLng(shipmentItemModel.getAddress())[0].geometry.location;
                geoPoint.setLatitude(location.lat + "");
                geoPoint.setLongitude(location.lng + "");
            }
        }

        partyCustomerMap.values().forEach(partyCustomer -> partyCustomer.setPostalAddress(
                partyCustomer.getPostalAddress().stream().distinct().collect(Collectors.toList()))
        );

        geoPointRepo.saveAll(locationCodeToGeoPoint.values());
        postalAddressRepo.saveAll(postalAddressMap.values());
        shipmentItemRepo.saveAll(shipmentItems);
        shipment.setShipmentItems(shipmentItems);
        return shipment;
    }

    @Override
    public Page<ShipmentModel> findAll(Pageable pageable) {
        return shipmentRepo.findAll(pageable).map(Shipment::toShipmentModel);
    }

}
