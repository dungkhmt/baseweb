package com.hust.baseweb.applications.tms.service;

import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.hust.baseweb.applications.customer.entity.PartyCustomer;
import com.hust.baseweb.applications.customer.model.CreateCustomerInputModel;
import com.hust.baseweb.applications.customer.service.CustomerService;
import com.hust.baseweb.applications.geo.entity.GeoPoint;
import com.hust.baseweb.applications.geo.entity.PostalAddress;
import com.hust.baseweb.applications.geo.repo.GeoPointRepo;
import com.hust.baseweb.applications.geo.repo.PostalAddressRepo;
import com.hust.baseweb.applications.logistics.entity.Product;
import com.hust.baseweb.applications.logistics.repo.ProductRepo;
import com.hust.baseweb.applications.logistics.service.ProductService;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
public class ShipmentServiceImpl implements ShipmentService {
    private ShipmentRepo shipmentRepo;
    private ShipmentItemRepo shipmentItemRepo;

    private PartyCustomerRepo partyCustomerRepo;
    private PostalAddressRepo postalAddressRepo;
    private GeoPointRepo geoPointRepo;
    private ProductRepo productRepo;

    private CustomerService customerService;
    private ProductService productService;

    @Override
    @Transactional
    public Shipment save(CreateShipmentInputModel input) {
        log.info("save, shipmentItem.length = " + input.getShipmentItems().length);

        // Tạo shipment
//        UUID shipmentId = UUID.randomUUID();
        Shipment shipment = new Shipment();
//        shipment.setShipmentId(shipmentId);
        shipment.setShipmentTypeId("SALES_SHIPMENT");
        shipment = shipmentRepo.save(shipment);

        // Tạo shipment_item

        // Convert shipmentItemModel to list
        List<CreateShipmentItemInputModel> shipmentItemInputModels = Arrays.asList(input.getShipmentItems());

        log.info("save, finished convert shipmentItems to list");


        // Danh sách customerCode khác nhau đã có trong input
        List<String> customerCodes = shipmentItemInputModels.stream()
                .map(CreateShipmentItemInputModel::getCustomerCode).distinct().collect(Collectors.toList());

        log.info("save, customerCodes = " + customerCodes.size());

        // Danh sách locationCode khác nhau đã có trong input
        List<String> locationCodes = shipmentItemInputModels.stream()
                .map(CreateShipmentItemInputModel::getLocationCode).distinct().collect(Collectors.toList());

        log.info("save, locationCodes = " + locationCodes.size());


        // Danh sách product id khác nhau đã có trong input
        List<String> productIds = shipmentItemInputModels.stream()
                .map(CreateShipmentItemInputModel::getProductId).distinct().collect(Collectors.toList());

        log.info("save, productIds = " + productIds.size());

        // partyCustomerMap: danh sách party customer đã có trong DB
        Map<String, PartyCustomer> partyCustomerMap = new HashMap<>();
        partyCustomerRepo.findAllByCustomerCodeIn(customerCodes).forEach(partyCustomer -> partyCustomerMap.put(partyCustomer.getCustomerCode(), partyCustomer));

        // partyCustomerMap: danh sách portal address và geo point đã có trong DB
        Map<String, PostalAddress> postalAddressMap = new HashMap<>();
        Map<String, GeoPoint> locationCodeToGeoPointMap = new HashMap<>();
        postalAddressRepo.findAllByLocationCodeIn(locationCodes).forEach(postalAddress -> postalAddressMap.put(postalAddress.getLocationCode(), postalAddress));

        // productMap: danh sách product đã có trong DB
        Map<String, Product> productMap = new HashMap<>();
        productRepo.findAllByProductIdIn(productIds).forEach(product -> productMap.put(product.getProductId(), product));

        log.info("save, start process items...");
        int idx = 0;
        List<ShipmentItem> shipmentItems = new ArrayList<>();
        for (int i = 0; i < input.getShipmentItems().length; i++) {
            CreateShipmentItemInputModel shipmentItemModel = input.getShipmentItems()[i];

            log.info("::save, idx = " + idx + "/" + input.getShipmentItems().length + ", product = " + shipmentItemModel.getProductId() + ", quantity = " + shipmentItemModel.getQuantity() + " pallet = " + shipmentItemModel.getPallet());
            idx++;

            // sao chép model sang entity class
            String shipmentItemSeqId = CommonUtils.buildSeqId(idx);
            ShipmentItem shipmentItem = new ShipmentItem();
            shipmentItem.setShipment(shipment);
            //shipmentItem.setShipmentItemSeqId(shipmentItemSeqId);
            shipmentItem.setQuantity(shipmentItemModel.getQuantity());
            shipmentItem.setPallet(shipmentItemModel.getPallet());
            shipmentItem.setProductId(shipmentItemModel.getProductId());

            shipmentItems.add(shipmentItem);

            // Nếu portal address hiện tại chưa có trong DB, và chưa từng được duyệt qua lần nào, thêm mới nó
            // đồng thời với Geopoint, query GGMap
            PostalAddress postalAddress = postalAddressMap.computeIfAbsent(shipmentItemModel.getLocationCode(),
                    locationCode -> {
                        log.info("Query latLng: " + shipmentItemModel.getAddress());
                        GeocodingResult[] geocodingResults = GoogleMapUtils.queryLatLng(shipmentItemModel.getAddress());
                        GeoPoint geoPoint;
                        if (geocodingResults != null && geocodingResults.length > 0) {
                            LatLng location = geocodingResults[0].geometry.location;
                            geoPoint = new GeoPoint(null, location.lat + "", location.lng + "");
                        } else {
                            geoPoint = new GeoPoint();
                        }
                        locationCodeToGeoPointMap.put(locationCode, geoPoint);
                        PostalAddress pa = new PostalAddress();
                        pa.setAddress(shipmentItemModel.getAddress());
                        pa.setLocationCode(locationCode);
                        pa.setGeoPoint(geoPoint);
                        return pa;
                    });

            // Nếu party customer hiện tại chưa có trong DB, và chưa từng được duyệt qua lần nào, thêm mới nó
            PartyCustomer partyCustomer = partyCustomerMap.computeIfAbsent(shipmentItemModel.getCustomerCode(),
                    customerCode ->
                            customerService.save(new CreateCustomerInputModel(
                                    shipmentItemModel.getCustomerName(),
                                    shipmentItemModel.getAddress(),
                                    postalAddress.getGeoPoint().getLatitude(),
                                    postalAddress.getGeoPoint().getLongitude()
                            )));
            // thêm portal address hiện tại vào party customer
            //partyCustomer.getPostalAddress().add(postalAddress);// NOT attach address into list 

            // Nếu product hiện tại chưa có trong DB, và chưa từng được duyệt qua lần nào, thêm mới nó
            productMap.computeIfAbsent(shipmentItemModel.getProductId(), productId ->
                    productService.save(productId, shipmentItemModel.getProductName(), shipmentItemModel.getUom()));
        }

        // lọc trùng postal address nếu 1 postal address được add nhiều lần vào cùng 1 customer
        partyCustomerMap.values().forEach(partyCustomer -> partyCustomer.setPostalAddress(
                partyCustomer.getPostalAddress().stream().distinct().collect(Collectors.toList()))
        );

        // lưu tất cả vào DB
        geoPointRepo.saveAll(locationCodeToGeoPointMap.values());
        postalAddressRepo.saveAll(postalAddressMap.values());
        partyCustomerRepo.saveAll(partyCustomerMap.values());
//        for (GeoPoint gp : locationCodeToGeoPointMap.values()) {
//            log.info("save geo point " + gp.getGeoPointId());
//            geoPointRepo.save(gp);
//        }
//        for (PostalAddress pa : postalAddressMap.values()) {
//            log.info("save address " + pa.getContactMechId() + ", geo point = " + pa.getGeoPoint().getGeoPointId());
//            postalAddressRepo.save(pa);
//        }
//        for (PartyCustomer pc : partyCustomerMap.values()) {
//            log.info("save, partyCustomer " + pc.getCustomerCode() + ", shipToLocationCode = " + pc.getPostalAddress().size());
//        }
        productRepo.saveAll(productMap.values());
        shipmentItemRepo.saveAll(shipmentItems);
//        shipment.setShipmentItems(shipmentItems);
        return shipment;
    }

    @Override
    public Page<ShipmentModel> findAll(Pageable pageable) {
        return shipmentRepo.findAll(pageable).map(Shipment::toShipmentModel);
    }

}
