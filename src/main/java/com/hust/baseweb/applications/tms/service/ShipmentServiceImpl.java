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
import com.hust.baseweb.applications.logistics.constant.LogisticsConstant;
import com.hust.baseweb.applications.logistics.entity.Facility;
import com.hust.baseweb.applications.logistics.entity.Product;
import com.hust.baseweb.applications.logistics.entity.ShipmentItemRole;
import com.hust.baseweb.applications.logistics.repo.FacilityRepo;
import com.hust.baseweb.applications.logistics.repo.ProductRepo;
import com.hust.baseweb.applications.logistics.repo.ShipmentItemRoleRepo;
import com.hust.baseweb.applications.logistics.service.ProductService;
import com.hust.baseweb.applications.order.entity.CompositeOrderItemId;
import com.hust.baseweb.applications.order.entity.OrderHeader;
import com.hust.baseweb.applications.order.entity.OrderItem;
import com.hust.baseweb.applications.order.entity.OrderRole;
import com.hust.baseweb.applications.order.repo.OrderHeaderRepo;
import com.hust.baseweb.applications.order.repo.OrderItemRepo;
import com.hust.baseweb.applications.order.repo.OrderRoleRepo;
import com.hust.baseweb.applications.order.repo.PartyCustomerRepo;
import com.hust.baseweb.applications.order.service.RevenueService;
import com.hust.baseweb.applications.tms.entity.Shipment;
import com.hust.baseweb.applications.tms.entity.ShipmentItem;
import com.hust.baseweb.applications.tms.entity.status.ShipmentItemStatus;
import com.hust.baseweb.applications.tms.model.ShipmentItemModel;
import com.hust.baseweb.applications.tms.model.ShipmentModel;
import com.hust.baseweb.applications.tms.repo.ShipmentItemRepo;
import com.hust.baseweb.applications.tms.repo.ShipmentRepo;
import com.hust.baseweb.applications.tms.repo.status.ShipmentItemStatusRepo;
import com.hust.baseweb.entity.*;
import com.hust.baseweb.repo.PartyRepo;
import com.hust.baseweb.repo.PartyTypeRepo;
import com.hust.baseweb.repo.StatusItemRepo;
import com.hust.baseweb.repo.StatusRepo;
import com.hust.baseweb.utils.Constant;
import com.hust.baseweb.utils.GoogleMapUtils;
import com.hust.baseweb.utils.LatLngUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
public class ShipmentServiceImpl implements ShipmentService {
    private ShipmentRepo shipmentRepo;
    private ShipmentItemRepo shipmentItemRepo;

    private FacilityRepo facilityRepo;

    private PartyCustomerRepo partyCustomerRepo;
    private PostalAddressRepo postalAddressRepo;
    private GeoPointRepo geoPointRepo;
    private ProductRepo productRepo;
    private OrderHeaderRepo orderHeaderRepo;
    private OrderItemRepo orderItemRepo;
    private OrderRoleRepo orderRoleRepo;

    private CustomerService customerService;
    private ProductService productService;
    private PostalAddressService postalAddressService;

    private PartyRepo partyRepo;
    private PartyTypeRepo partyTypeRepo;
    private StatusRepo statusRepo;
    private CustomerRepo customerRepo;
    private PartyContactMechPurposeRepo partyContactMechPurposeRepo;

    private RevenueService revenueService;

    private StatusItemRepo statusItemRepo;
    private ShipmentItemStatusRepo shipmentItemStatusRepo;
    private ShipmentItemRoleRepo shipmentItemRoleRepo;

    // @Override
    @Transactional
    public Shipment privateSave(ShipmentModel.CreateShipmentInputModel input) {
        if (input.getShipmentItems() == null || input.getShipmentItems().length == 0) {
            return null;
        }

        log.info("save1, shipmentItem.length = "
                + input.getShipmentItems().length);
        Shipment shipment = createAndSaveShipment();

        for (int i = 0; i < input.getShipmentItems().length; i++) {
            ShipmentItemModel.Create shipmentItemInputModel = input.getShipmentItems()[i];

            List<PartyCustomer> customers = partyCustomerRepo
                    .findAllByCustomerCode(shipmentItemInputModel.getCustomerCode());
            PartyCustomer customer;
            if (customers == null || customers.size() == 0) {
                // insert a customer
                String[] s = shipmentItemInputModel.getLatLng().split(",");
                double lat = Double.parseDouble(s[0].trim());
                double lng = Double.parseDouble(s[1].trim());
                customer = customerService.save(new CreateCustomerInputModel(shipmentItemInputModel.getCustomerCode(),
                        shipmentItemInputModel.getCustomerName(), shipmentItemInputModel.getAddress(), lat, lng));
            } else {
                customer = customers.get(0);
            }

            Product product = productRepo.findByProductId(shipmentItemInputModel.getProductId());
            if (product == null) {
                product = productService.save(shipmentItemInputModel.getProductId(),
                        shipmentItemInputModel.getProductTransportCategory(),
                        shipmentItemInputModel.getProductName(),
                        shipmentItemInputModel.getWeight() / shipmentItemInputModel.getQuantity(),
                        shipmentItemInputModel.getUom(),
                        shipmentItemInputModel.getHsThu(),
                        shipmentItemInputModel.getHsPal());
            }
            List<PostalAddress> addresses = postalAddressRepo
                    .findAllByLocationCode(shipmentItemInputModel.getLocationCode());
            PostalAddress address;
            if (addresses == null || addresses.size() == 0) {
                String[] latlng = shipmentItemInputModel.getLatLng().split(",");
                address = postalAddressService.save(shipmentItemInputModel.getLocationCode(),
                        shipmentItemInputModel.getAddress(),
                        Double.parseDouble(latlng[0]),
                        Double.parseDouble(latlng[1]));
            } else {
                address = addresses.get(0);
            }
            Party partyCustomer = partyRepo.findByPartyId(customer.getPartyId());
            ShipmentItem shipmentItem = new ShipmentItem();
            //shipmentItem.setCustomer(customer);
            shipmentItem.setPartyCustomer(partyCustomer);
            shipmentItem.setShipToLocation(address);
            shipmentItem.setPallet(shipmentItemInputModel.getPallet());
//            shipmentItem.setProductId(product.getProductId());
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
    public Shipment save(UserLogin userLogin, ShipmentModel.CreateShipmentInputModel input) {
        log.info("save, shipmentItem.length = " + input.getShipmentItems().length);
//        if (true) {
//            return privateSave(input);
//        }

        // Tạo shipment
        mergeDistinctShipmentItems(input);

        Shipment shipment = createAndSaveShipment();

        // Tạo shipment_item

        CodeListInInput codeListInInput = new CodeListInInput(input).invoke();
        List<String> customerCodes = codeListInInput.getCustomerCodes();
        List<String> locationCodes = codeListInInput.getLocationCodes();
        List<String> productIds = codeListInInput.getProductIds();
        List<String> orderIds = codeListInInput.getOrderIds();

        CodeListInDb codeListInDb = new CodeListInDb(customerCodes, locationCodes, productIds, orderIds).invoke();
        Map<String, PartyCustomer> partyCustomerMap = codeListInDb.getPartyCustomerMap();
        Map<String, PostalAddress> postalAddressMap = codeListInDb.getPostalAddressMap();
        Map<String, Product> productMap = codeListInDb.getProductMap();
        Map<String, OrderHeader> orderHeaderMap = codeListInDb.getOrderHeaderMap();

        Map<String, Facility> facilityMap = facilityRepo.findAllByFacilityIdIn(
                Stream.of(input.getShipmentItems())
                        .map(ShipmentItemModel.Create::getFacilityId)
                        .distinct()
                        .collect(Collectors.toList()))
                .stream().collect(Collectors.toMap(Facility::getFacilityId, facility -> facility));

        Map<String, Integer> orderSeqIdCounterMap = new HashMap<>();
        Map<CompositeOrderItemId, ShipmentItemModel.Create> orderItemIdToShipmentItemModelMap = new HashMap<>();

        List<OrderItem> orderItems = new ArrayList<>();
        Map<CompositeOrderItemId, LocalDate> orderItemIdToDateMap = new HashMap<>();

        for (int i = 0; i < input.getShipmentItems().length; i++) {
            ShipmentItemModel.Create shipmentItemModel = input.getShipmentItems()[i];

            Product product = createProductIfAbsent(productMap, shipmentItemModel);

            OrderHeader orderHeader = getOrCreateOrderHeader(orderHeaderMap, shipmentItemModel);

            OrderItem orderItem = createOrderItem(orderSeqIdCounterMap, shipmentItemModel, product, orderHeader);

            orderItems.add(orderItem);

            orderItemIdToShipmentItemModelMap.put(new CompositeOrderItemId(orderItem.getOrderId(),
                    orderItem.getOrderItemSeqId()), shipmentItemModel);

            orderItemIdToDateMap.put(new CompositeOrderItemId(orderItem.getOrderId(), orderItem.getOrderItemSeqId()),
                    orderHeader.getOrderDate().toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate());
        }

        orderItems = orderItemRepo.saveAll(orderItems);

        List<ShipmentItem> shipmentItems = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            ShipmentItem shipmentItem = createShipmentItem(shipment,
                    partyCustomerMap,
                    postalAddressMap,
                    facilityMap,
                    orderItemIdToShipmentItemModelMap.get(new CompositeOrderItemId(orderItem.getOrderId(),
                            orderItem.getOrderItemSeqId())),
                    orderItem, orderHeaderMap.get(orderItem.getOrderId()));

            shipmentItems.add(shipmentItem);
        }

        List<ShipmentItemRole> shipmentItemRoles = new ArrayList<>();
        for(ShipmentItem shipmentItem: shipmentItems){
            ShipmentItemRole shipmentItemRole = new ShipmentItemRole();
            shipmentItemRole.setShipmentItem(shipmentItem);
            shipmentItemRole.setParty(userLogin.getParty());
            shipmentItemRole.setRoleTypeId(LogisticsConstant.CREATE_DELIVERY_TRIP);
            shipmentItemRole.setFromDate(new Date());
            //shipmentItemRole = shipmentItemRoleRepo.save(shipmentItemRole);
            shipmentItemRoles.add(shipmentItemRole);
        }


        //Map<OrderItem, PartyCustomer> orderItemToCustomerMap = shipmentItems.stream()
        //        .collect(Collectors.toMap(ShipmentItem::getOrderItem, ShipmentItem::getCustomer));
        Map<OrderItem, Party> orderItemToCustomerMap = shipmentItems.stream()
                .collect(Collectors.toMap(ShipmentItem::getOrderItem, ShipmentItem::getPartyCustomer));


        StatusItem statusItem = statusItemRepo.findById("SHIPMENT_ITEM_CREATED")
                .orElseThrow(NoSuchElementException::new);
        shipmentItems.forEach(shipmentItem -> shipmentItem.setStatusItem(statusItem));

        facilityRepo.saveAll(facilityMap.values());

        // lưu tất cả shipment item vào DB
        shipmentItemRepo.saveAll(shipmentItems);

        List<ShipmentItemStatus> shipmentItemStatuses = createShipmentItemStatuses(shipmentItems, statusItem);
        shipmentItemStatusRepo.saveAll(shipmentItemStatuses);

        shipmentItemRoleRepo.saveAll(shipmentItemRoles);

        revenueService.updateRevenue(orderItems,
                orderItemToCustomerMap::get,
                orderItem -> orderItemIdToDateMap.get(new CompositeOrderItemId(orderItem.getOrderId(),
                        orderItem.getOrderItemSeqId())));

        return shipment;
    }

    private void mergeDistinctShipmentItems(ShipmentModel.CreateShipmentInputModel input) {
        ShipmentItemModel.Create[] shipmentItems = input.getShipmentItems();
        Map<List<String>, ShipmentItemModel.Create> orderIdAndProductIdToShipmentItem = new HashMap<>();
        for (ShipmentItemModel.Create shipmentItem : shipmentItems) {
            orderIdAndProductIdToShipmentItem.merge(Arrays.asList(shipmentItem.getOrderId(),
                    shipmentItem.getProductId()),
                    shipmentItem, (old, other) -> {
                        old.setQuantity(old.getQuantity() + other.getQuantity());
                        old.setWeight(old.getWeight() + other.getWeight());
                        old.setPallet(old.getPallet() + other.getPallet());
                        return old;
                    });
        }
        input.setShipmentItems(orderIdAndProductIdToShipmentItem.values().toArray(new ShipmentItemModel.Create[0]));
    }

    @NotNull
    private List<ShipmentItemStatus> createShipmentItemStatuses(List<ShipmentItem> shipmentItems,
                                                                StatusItem statusItem) {
        Date now = new Date();
        return shipmentItems.stream()
                .map(shipmentItem -> new ShipmentItemStatus(null, shipmentItem, statusItem, now, null))
                .collect(Collectors.toList());
    }

    @NotNull
    private ShipmentItem createShipmentItem(Shipment shipment,
                                            Map<String, PartyCustomer> partyCustomerMap,
                                            Map<String, PostalAddress> postalAddressMap,
                                            Map<String, Facility> facilityMap,
                                            ShipmentItemModel.Create shipmentItemModel,
                                            OrderItem orderItem,
                                            OrderHeader orderHeader) {
        ShipmentItem shipmentItem = new ShipmentItem();
        shipmentItem.setShipment(shipment);
        shipmentItem.setQuantity(shipmentItemModel.getQuantity());
        shipmentItem.setPallet(shipmentItemModel.getPallet());
        shipmentItem.setOrderItem(orderItem);

        PostalAddress postalAddress = getOrCreatePostalAddress(postalAddressMap, shipmentItemModel);
        shipmentItem.setShipToLocation(postalAddress);

        Facility facility = facilityMap.computeIfAbsent(shipmentItemModel.getFacilityId(), Facility::new);
        shipmentItem.setFacility(facility);

        PartyCustomer partyCustomer = getOrCreatePartyCustomer(partyCustomerMap, shipmentItemModel, postalAddress);
        //shipmentItem.setCustomer(partyCustomer);

        Party party = partyRepo.findByPartyId(partyCustomer.getPartyId());
        shipmentItem.setPartyCustomer(party);
        //orderHeader.setPartyCustomer(partyCustomer);
        orderHeader.setPartyCustomer(party);

        return shipmentItem;
    }

    @NotNull
    private OrderItem createOrderItem(Map<String, Integer> orderSeqIdCounterMap,
                                      ShipmentItemModel.Create shipmentItemModel,
                                      Product product,
                                      OrderHeader orderHeader) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId(orderHeader.getOrderId());
        orderItem.setProduct(product);
        orderItem.setQuantity(shipmentItemModel.getQuantity());
        orderItem.setOrderItemSeqId(orderSeqIdCounterMap.merge(orderHeader.getOrderId(), 1, Integer::sum) + "");
        orderItem.setExportedQuantity(shipmentItemModel.getQuantity());
        return orderItem;
    }

    @NotNull
    private OrderHeader getOrCreateOrderHeader(Map<String, OrderHeader> orderHeaderMap,
                                               ShipmentItemModel.Create shipmentItemModel) {
        Date orderDate = null;
//        log.info("getOrCreateOrderHeader, orderDate = " + shipmentItemModel.getOrderDate());
        if(shipmentItemModel.getOrderDate() == null)
            orderDate = new Date();
        else{
        try {
            orderDate = Constant.ORDER_EXCEL_DATE_FORMAT.parse(shipmentItemModel.getOrderDate());
        } catch (ParseException e) {
//            e.printStackTrace();
        }
        }
        final Date finalOrderDate = orderDate;
        return orderHeaderMap.computeIfAbsent(shipmentItemModel.getOrderId(), orderId -> {
            OrderHeader orderHeader = new OrderHeader();
            orderHeader.setOrderId(orderId);
            orderHeader.setOrderDate(finalOrderDate);
            orderHeader.setExported(true);
            return orderHeaderRepo.save(orderHeader);
        });
    }

    private Product createProductIfAbsent(Map<String, Product> productMap,
                                          ShipmentItemModel.Create shipmentItemModel) {
        // Nếu product hiện tại chưa có trong DB, và chưa từng được duyệt qua lần nào, thêm mới nó
        return productMap.computeIfAbsent(shipmentItemModel.getProductId(), productId ->
                productService.save(productId, shipmentItemModel.getProductName(),
                        shipmentItemModel.getProductTransportCategory(),
                        shipmentItemModel.getWeight() / shipmentItemModel.getQuantity() * 1000, // kg
                        shipmentItemModel.getUom(), shipmentItemModel.getHsThu(), shipmentItemModel.getHsPal()));
    }

    @NotNull
    private PartyCustomer getOrCreatePartyCustomer(Map<String, PartyCustomer> partyCustomerMap,
                                                   ShipmentItemModel.Create shipmentItemModel,
                                                   PostalAddress postalAddress) {
        // Nếu party customer hiện tại chưa có trong DB, và chưa từng được duyệt qua lần nào, thêm mới nó
        return partyCustomerMap.computeIfAbsent(shipmentItemModel.getCustomerCode(),
                customerCode ->
                {
                    PartyType partyType = partyTypeRepo.findByPartyTypeId("PARTY_RETAIL_OUTLET");

                    Party party = new Party(null, partyType, "",
                            statusRepo.findById(Status.StatusEnum.PARTY_ENABLED.name())
                                    .orElseThrow(NoSuchElementException::new),
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

                    OrderRole orderRole = new OrderRole();
                    orderRole.setOrderId(shipmentItemModel.getOrderId());
                    orderRole.setPartyId(customer.getPartyId());
                    orderRole.setRoleTypeId("BILL_TO_CUSTOMER");
                    orderRole = orderRoleRepo.save(orderRole);

                    return customer;
                });
    }

    @NotNull
    private PostalAddress getOrCreatePostalAddress(Map<String, PostalAddress> postalAddressMap,
                                                   ShipmentItemModel.Create shipmentItemModel) {
        // Nếu portal address hiện tại chưa có trong DB, và chưa từng được duyệt qua lần nào, thêm mới nó
        // đồng thời với Geopoint, query GGMap
        return postalAddressMap.computeIfAbsent(shipmentItemModel.getLocationCode(),
                locationCode -> {
                    GeoPoint geoPoint;
                    if (shipmentItemModel.getLatLng() != null) {
                        LatLng location = LatLngUtils.parse(shipmentItemModel.getLatLng());
                        geoPoint = new GeoPoint(null, location.lat, location.lng);
                    } else {
                        geoPoint = new GeoPoint();
                    }
                    geoPoint = geoPointRepo.save(geoPoint);

                    PostalAddress pa = new PostalAddress();
                    pa.setAddress(shipmentItemModel.getAddress());
                    pa.setLocationCode(locationCode);
                    pa.setGeoPoint(geoPoint);
                    pa.setMaxLoadWeight(Double.MAX_VALUE);
                    pa = postalAddressRepo.save(pa);
                    return pa;
                });
    }

    @NotNull
    private Shipment createAndSaveShipment() {
        Shipment shipment = new Shipment();
        shipment.setShipmentTypeId("SALES_SHIPMENT");
        shipment = shipmentRepo.save(shipment);
        return shipment;
    }

    @Override
    @Transactional
    public Shipment save(UserLogin userLogin, ShipmentItemModel.Create shipmentItemModel) {

        Shipment shipment = new Shipment();

        ShipmentItem shipmentItem = new ShipmentItem();
        shipmentItem.setShipment(shipment);
        //shipmentItem.setShipmentItemSeqId(shipmentItemSeqId);
        shipmentItem.setQuantity(shipmentItemModel.getQuantity());
        shipmentItem.setPallet(shipmentItemModel.getPallet());
//        shipmentItem.setProductId(shipmentItemModel.getProductId());

        String locationCode = shipmentItemModel.getLocationCode();
        List<PostalAddress> postalAddresses = postalAddressRepo.findAllByLocationCode(locationCode);
        if (postalAddresses.isEmpty()) {
            log.info("Query latLng: " + shipmentItemModel.getAddress());
            GeocodingResult[] geocodingResults = GoogleMapUtils.queryLatLng(shipmentItemModel.getAddress());
            GeoPoint geoPoint;
            if (geocodingResults != null && geocodingResults.length > 0) {
                LatLng location = geocodingResults[0].geometry.location;
                geoPoint = new GeoPoint(null, location.lat, location.lng);
            } else {
                geoPoint = new GeoPoint();
            }
            geoPointRepo.save(geoPoint);
            PostalAddress postalAddress = new PostalAddress();
            postalAddress.setAddress(shipmentItemModel.getAddress());
            postalAddress.setLocationCode(locationCode);
            postalAddress.setGeoPoint(geoPoint);
            postalAddress.setMaxLoadWeight(Double.MAX_VALUE);
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
                    shipmentItemModel.getProductTransportCategory(),
                    shipmentItemModel.getWeight() / shipmentItemModel.getQuantity(),
                    shipmentItemModel.getUom(), shipmentItemModel.getHsThu(), shipmentItemModel.getHsPal());
            productRepo.save(product);
        }

        // insert shipment_item_role
        ShipmentItemRole shipmentItemRole = new ShipmentItemRole();
        shipmentItemRole.setShipmentItem(shipmentItem);
        shipmentItemRole.setParty(userLogin.getParty());
        shipmentItemRole.setRoleTypeId(LogisticsConstant.CREATE_DELIVERY_TRIP);
        shipmentItemRole.setFromDate(new Date());
        shipmentItemRole = shipmentItemRoleRepo.save(shipmentItemRole);

        return shipment;
    }

    @Override
    public Page<ShipmentModel> findAll(Pageable pageable) {
        return shipmentRepo.findAll(pageable).map(Shipment::toShipmentModel);
    }

    private class CodeListInInput {
        private ShipmentModel.CreateShipmentInputModel input;
        private List<String> customerCodes;
        private List<String> locationCodes;
        private List<String> productIds;
        private List<String> orderIds;

        CodeListInInput(ShipmentModel.CreateShipmentInputModel input) {
            this.input = input;
        }

        List<String> getCustomerCodes() {
            return customerCodes;
        }

        List<String> getLocationCodes() {
            return locationCodes;
        }

        List<String> getProductIds() {
            return productIds;
        }

        List<String> getOrderIds() {
            return orderIds;
        }

        CodeListInInput invoke() {
            // Convert shipmentItemModel to list
            List<ShipmentItemModel.Create> shipmentItemInputModels = Arrays.asList(input.getShipmentItems());

            log.info("save, finished convert shipmentItems to list");

            // Danh sách customerCode khác nhau đã có trong input
            customerCodes = shipmentItemInputModels.stream()
                    .map(ShipmentItemModel.Create::getCustomerCode).distinct().collect(Collectors.toList());

            log.info("save, customerCodes = " + customerCodes.size());

            // Danh sách locationCode khác nhau đã có trong input
            locationCodes = shipmentItemInputModels.stream()
                    .map(ShipmentItemModel.Create::getLocationCode).distinct().collect(Collectors.toList());

            log.info("save, locationCodes = " + locationCodes.size());

            // Danh sách product id khác nhau đã có trong input
            productIds = shipmentItemInputModels.stream()
                    .map(ShipmentItemModel.Create::getProductId).distinct().collect(Collectors.toList());

            log.info("save, productIds = " + productIds.size());

            // Danh sách order id khác nhau đã có trong input
            orderIds = shipmentItemInputModels.stream()
                    .map(ShipmentItemModel.Create::getOrderId).distinct().collect(Collectors.toList());
            return this;
        }
    }

    private class CodeListInDb {
        private List<String> customerCodes;
        private List<String> locationCodes;
        private List<String> productIds;
        private List<String> orderIds;
        private Map<String, PartyCustomer> partyCustomerMap;
        private Map<String, PostalAddress> postalAddressMap;
        private Map<String, Product> productMap;
        private Map<String, OrderHeader> orderHeaderMap;

        CodeListInDb(List<String> customerCodes,
                     List<String> locationCodes,
                     List<String> productIds,
                     List<String> orderIds) {
            this.customerCodes = customerCodes;
            this.locationCodes = locationCodes;
            this.productIds = productIds;
            this.orderIds = orderIds;
        }

        Map<String, PartyCustomer> getPartyCustomerMap() {
            return partyCustomerMap;
        }

        Map<String, PostalAddress> getPostalAddressMap() {
            return postalAddressMap;
        }

        Map<String, Product> getProductMap() {
            return productMap;
        }

        Map<String, OrderHeader> getOrderHeaderMap() {
            return orderHeaderMap;
        }

        CodeListInDb invoke() {
            // partyCustomerMap: danh sách party customer đã có trong DB
            partyCustomerMap = new HashMap<>();
            partyCustomerRepo.findAllByCustomerCodeIn(customerCodes)
                    .forEach(partyCustomer -> partyCustomerMap.put(partyCustomer.getCustomerCode(), partyCustomer));

            // partyCustomerMap: danh sách postal address và geo point đã có trong DB
            postalAddressMap = new HashMap<>();
            postalAddressRepo.findAllByLocationCodeIn(locationCodes)
                    .forEach(postalAddress -> postalAddressMap.put(postalAddress.getLocationCode(), postalAddress));

            // productMap: danh sách product đã có trong DB
            productMap = new HashMap<>();
            productRepo.findAllByProductIdIn(productIds)
                    .forEach(product -> productMap.put(product.getProductId(), product));

            // orderHeaderMap: danh sách order đã có trong DB
            orderHeaderMap = new HashMap<>();
            orderHeaderRepo.findAllByOrderIdIn(orderIds)
                    .forEach(orderHeader -> orderHeaderMap.put(orderHeader.getOrderId(), orderHeader));
            return this;
        }
    }
}
