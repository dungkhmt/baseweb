package com.hust.baseweb.applications.tms.service;

import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.hust.baseweb.applications.customer.entity.PartyContactMechPurpose;
import com.hust.baseweb.applications.customer.entity.PartyCustomer;
import com.hust.baseweb.applications.customer.model.CreateCustomerInputModel;
import com.hust.baseweb.applications.customer.repo.CustomerRepo;
import com.hust.baseweb.applications.customer.repo.PartyContactMechPurposeRepo;
import com.hust.baseweb.applications.customer.service.CustomerService;
import com.hust.baseweb.applications.geo.entity.GeoPoint;
import com.hust.baseweb.applications.geo.entity.PostalAddress;
import com.hust.baseweb.applications.geo.repo.GeoPointRepo;
import com.hust.baseweb.applications.geo.repo.PostalAddressRepo;
import com.hust.baseweb.applications.geo.service.PostalAddressService;
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
import com.hust.baseweb.entity.Party;
import com.hust.baseweb.entity.PartyType;
import com.hust.baseweb.entity.Status;
import com.hust.baseweb.repo.PartyRepo;
import com.hust.baseweb.repo.PartyTypeRepo;
import com.hust.baseweb.repo.StatusRepo;
import com.hust.baseweb.utils.Constant;
import com.hust.baseweb.utils.GoogleMapUtils;
import com.hust.baseweb.utils.LatLngUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
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
    private PostalAddressService postalAddressService;

    private PartyRepo partyRepo;
    private PartyTypeRepo partyTypeRepo;
    private StatusRepo statusRepo;
    private CustomerRepo customerRepo;
    private PartyContactMechPurposeRepo partyContactMechPurposeRepo;

    // @Override
    @Transactional
    public Shipment privateSave(CreateShipmentInputModel input) {
        if (input.getShipmentItems() == null || input.getShipmentItems().length == 0) {
            return null;
        }

        log.info("save1, shipmentItem.length = "
                + input.getShipmentItems().length);
        Shipment shipment = new Shipment();
        shipment.setShipmentTypeId("SALES_SHIPMENT");
        shipment = shipmentRepo.save(shipment);

        for (int i = 0; i < input.getShipmentItems().length; i++) {
            CreateShipmentItemInputModel shipmentItemInputModel = input.getShipmentItems()[i];

            List<PartyCustomer> customers = partyCustomerRepo
                    .findAllByCustomerCode(shipmentItemInputModel.getCustomerCode());
            PartyCustomer customer;
            if (customers == null || customers.size() == 0) {
                // insert a customer
                String[] s = shipmentItemInputModel.getLatLng().split(",");
                // double lat = Double.valueOf(s[0].trim());
                // double lng = Double.valueOf(s[1].trim());
                customer = customerService.save(new CreateCustomerInputModel(shipmentItemInputModel.getCustomerCode(),
                        shipmentItemInputModel.getCustomerName(), shipmentItemInputModel.getAddress(), s[0].trim(), s[1]
                        .trim()));
            } else {
                customer = customers.get(0);
            }

            Product product = productRepo.findByProductId(shipmentItemInputModel.getProductId());
            if (product == null) {
                product = productService.save(shipmentItemInputModel.getProductId(), shipmentItemInputModel.getProductTransportCategory(),
                        shipmentItemInputModel.getProductName(), shipmentItemInputModel.getWeight(), shipmentItemInputModel.getUom());
            }
            List<PostalAddress> addresses = postalAddressRepo
                    .findAllByLocationCode(shipmentItemInputModel.getLocationCode());
            PostalAddress address;
            if (addresses == null || addresses.size() == 0) {
                String[] latlng = shipmentItemInputModel.getLatLng().split(",");
                address = postalAddressService.save(shipmentItemInputModel.getLocationCode(),
                        shipmentItemInputModel.getAddress(), latlng[0], latlng[1]);
            } else {
                address = addresses.get(0);
            }
            ShipmentItem shipmentItem = new ShipmentItem();
            shipmentItem.setCustomer(customer);
            shipmentItem.setShipToLocation(address);
            shipmentItem.setPallet(shipmentItemInputModel.getPallet());
            shipmentItem.setProductId(product.getProductId());
            shipmentItem.setQuantity(shipmentItemInputModel.getQuantity());
            shipmentItem.setShipment(shipment);

            shipmentItem = shipmentItemRepo.save(shipmentItem);
            log.info("save1 shipmentItem[" + i + "/"
                    + input.getShipmentItems().length + " --> DONE OK");
        }
        return shipment;
    }

    @Override
    @Transactional
    public Shipment save(CreateShipmentInputModel input) {
        log.info("save, shipmentItem.length = " + input.getShipmentItems().length);
//        if (true) {
//            return privateSave(input);
//        }

        // Tạo shipment
        Shipment shipment = new Shipment();
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
//            String shipmentItemSeqId = CommonUtils.buildSeqId(idx);
            ShipmentItem shipmentItem = new ShipmentItem();
            shipmentItem.setShipment(shipment);
            //shipmentItem.setShipmentItemSeqId(shipmentItemSeqId);
            shipmentItem.setQuantity(shipmentItemModel.getQuantity());
            shipmentItem.setPallet(shipmentItemModel.getPallet());
            shipmentItem.setProductId(shipmentItemModel.getProductId());

            try {
                shipmentItem.setOrderDate(Constant.ORDER_EXCEL_DATE_FORMAT.parse(shipmentItemModel.getOrderDate()));
            } catch (ParseException e) {
                log.error("Date parse error: " + shipmentItemModel.getOrderDate());
            }

            shipmentItems.add(shipmentItem);

            // Nếu portal address hiện tại chưa có trong DB, và chưa từng được duyệt qua lần nào, thêm mới nó
            // đồng thời với Geopoint, query GGMap
            PostalAddress postalAddress = postalAddressMap.computeIfAbsent(shipmentItemModel.getLocationCode(),
                    locationCode -> {
                        GeoPoint geoPoint;
                        if (shipmentItemModel.getLatLng() != null) {
                            LatLng location = LatLngUtils.parse(shipmentItemModel.getLatLng());
                            geoPoint = new GeoPoint(null, location.lat + "", location.lng + "");
                        } else {
                            geoPoint = new GeoPoint();
                        }
                        geoPoint = geoPointRepo.save(geoPoint);

                        PostalAddress pa = new PostalAddress();
                        pa.setAddress(shipmentItemModel.getAddress());
                        pa.setLocationCode(locationCode);
                        pa.setGeoPoint(geoPoint);
                        pa = postalAddressRepo.save(pa);
                        return pa;
                    });

            shipmentItem.setShipToLocation(postalAddress);

            // Nếu party customer hiện tại chưa có trong DB, và chưa từng được duyệt qua lần nào, thêm mới nó
            PartyCustomer partyCustomer = partyCustomerMap.computeIfAbsent(shipmentItemModel.getCustomerCode(),
                    customerCode ->
                    {
                        PartyType partyType = partyTypeRepo.findByPartyTypeId("PARTY_RETAILOUTLET");

                        Party party = new Party(null, partyType, "",
                                statusRepo.findById(Status.StatusEnum.PARTY_ENABLED.name()).orElseThrow(NoSuchElementException::new),
                                false);

                        party = partyRepo.save(party);

                        UUID partyId = party.getPartyId();

                        PartyCustomer customer = new PartyCustomer();
                        customer.setPartyId(partyId);
                        customer.setCustomerCode(shipmentItemModel.getCustomerCode());
                        customer.setPartyType(partyType);
                        customer.setCustomerName(shipmentItemModel.getCustomerName());
                        customer.setPostalAddress(new ArrayList<>());

                        customer = customerRepo.save(customer);

                        PartyContactMechPurpose partyContactMechPurpose = new PartyContactMechPurpose();
                        partyContactMechPurpose.setContactMechId(postalAddress.getContactMechId());
                        partyContactMechPurpose.setPartyId(partyId);
                        partyContactMechPurpose.setContactMechPurposeTypeId("PRIMARY_LOCATION");
                        partyContactMechPurpose.setFromDate(new Date());
                        partyContactMechPurposeRepo.save(partyContactMechPurpose);

                        return customer;
                    });
            shipmentItem.setCustomer(partyCustomer);

            // Nếu product hiện tại chưa có trong DB, và chưa từng được duyệt qua lần nào, thêm mới nó
            productMap.computeIfAbsent(shipmentItemModel.getProductId(), productId ->
                    productService.save(productId, shipmentItemModel.getProductName(),
                            shipmentItemModel.getProductTransportCategory(),
                            shipmentItemModel.getWeight(), shipmentItemModel.getUom()));
        }

        // lưu tất cả shipment item vào DB
        shipmentItemRepo.saveAll(shipmentItems);
        return shipment;
    }

    @Override
    @Transactional
    public Shipment save(CreateShipmentItemInputModel shipmentItemModel) {

        Shipment shipment = new Shipment();

        ShipmentItem shipmentItem = new ShipmentItem();
        shipmentItem.setShipment(shipment);
        //shipmentItem.setShipmentItemSeqId(shipmentItemSeqId);
        shipmentItem.setQuantity(shipmentItemModel.getQuantity());
        shipmentItem.setPallet(shipmentItemModel.getPallet());
        shipmentItem.setProductId(shipmentItemModel.getProductId());

        String locationCode = shipmentItemModel.getLocationCode();
        List<PostalAddress> postalAddresses = postalAddressRepo.findAllByLocationCode(locationCode);
        if (postalAddresses.isEmpty()) {
            log.info("Query latLng: " + shipmentItemModel.getAddress());
            GeocodingResult[] geocodingResults = GoogleMapUtils.queryLatLng(shipmentItemModel.getAddress());
            GeoPoint geoPoint;
            if (geocodingResults != null && geocodingResults.length > 0) {
                LatLng location = geocodingResults[0].geometry.location;
                geoPoint = new GeoPoint(null, location.lat + "", location.lng + "");
            } else {
                geoPoint = new GeoPoint();
            }
            geoPointRepo.save(geoPoint);
            PostalAddress postalAddress = new PostalAddress();
            postalAddress.setAddress(shipmentItemModel.getAddress());
            postalAddress.setLocationCode(locationCode);
            postalAddress.setGeoPoint(geoPoint);
            postalAddressRepo.save(postalAddress);
            postalAddresses.add(postalAddress);
        }

        String customerCode = shipmentItemModel.getCustomerCode();
        List<PartyCustomer> partyCustomers = partyCustomerRepo.findAllByCustomerCode(customerCode);
        if (partyCustomers.isEmpty()) {
            PartyCustomer partyCustomer = customerService.save(new CreateCustomerInputModel(
                    shipmentItemModel.getCustomerCode(),
                    shipmentItemModel.getCustomerName(),
                    shipmentItemModel.getAddress(),
                    postalAddresses.get(0).getGeoPoint().getLatitude(),
                    postalAddresses.get(0).getGeoPoint().getLongitude()
            ));
            partyCustomerRepo.save(partyCustomer);
        }

        String productId = shipmentItemModel.getProductId();
        Product product = productRepo.findByProductId(productId);
        if (product == null) {
            product = productService.save(productId, shipmentItemModel.getProductName(),
                    shipmentItemModel.getProductTransportCategory(), shipmentItemModel.getWeight(), shipmentItemModel.getUom());
            productRepo.save(product);
        }

        return shipment;
    }

    @Override
    public Page<ShipmentModel> findAll(Pageable pageable) {
        return shipmentRepo.findAll(pageable).map(Shipment::toShipmentModel);
    }

}
