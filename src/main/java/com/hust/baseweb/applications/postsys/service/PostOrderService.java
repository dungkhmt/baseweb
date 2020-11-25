package com.hust.baseweb.applications.postsys.service;

import com.hust.baseweb.applications.geo.entity.GeoPoint;
import com.hust.baseweb.applications.geo.entity.PostalAddress;
import com.hust.baseweb.applications.geo.repo.GeoPointRepo;
import com.hust.baseweb.applications.geo.repo.PostalAddressRepo;
import com.hust.baseweb.applications.postsys.entity.PostCustomer;
import com.hust.baseweb.applications.postsys.entity.PostOrder;
import com.hust.baseweb.applications.postsys.model.ResponseSample;
import com.hust.baseweb.applications.postsys.model.postshiporder.CreatePostShipOrderInputModel;
import com.hust.baseweb.applications.postsys.model.postshiporder.CreatePostShipOrderOutputModel;
import com.hust.baseweb.applications.postsys.repo.PostCustomerRepo;
import com.hust.baseweb.applications.postsys.repo.PostOrderRepo;
import com.hust.baseweb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PostOrderService {
    @Autowired private PostOrderRepo postOrderRepo;
    @Autowired UserService userService;
    @Autowired PostCustomerRepo postCustomerRepo;
    @Autowired GeoPointRepo geoPointRepo;
    @Autowired PostalAddressRepo postalAddressRepo;    
    
    public List<PostOrder> findAllPostOrder(){
        List<PostOrder> orders = (List<PostOrder>) postOrderRepo.findAll();
        return orders;
    }
    public CreatePostShipOrderOutputModel createPostShipOrder(CreatePostShipOrderInputModel input){
        input.print();
        CreatePostShipOrderOutputModel result = new CreatePostShipOrderOutputModel();
        PostOrder postOrder = new PostOrder();
        if (input.isFromCustomerExist()) {
            PostCustomer fromCustomer = postCustomerRepo.findByPostCustomerId(UUID.fromString(input.getFromCustomerId()));
            postOrder.setFromCustomer(fromCustomer);
        }
        else {
            //Create new post customer
            GeoPoint geoPoint = new GeoPoint();
            geoPoint.setLatitude(input.getFromCustomerLat());
            geoPoint.setLongitude(input.getFromCustomerLng());
            geoPointRepo.save(geoPoint);
            PostalAddress postalAddress = new PostalAddress();
            postalAddress.setAddress(input.getFromCustomerAddress());
            postalAddress.setGeoPoint(geoPoint);
            postalAddressRepo.save(postalAddress);
            PostCustomer fromCustomer = new PostCustomer();
            fromCustomer.setPostCustomerName(input.getFromCustomerName());
            fromCustomer.setPhoneNum(input.getFromCustomerPhoneNum());
            fromCustomer.setPostalAddress(postalAddress);
            postCustomerRepo.save(fromCustomer);
            postOrder.setFromCustomer(fromCustomer);
        }
        if (input.isToCustomerExist()) {
            PostCustomer toCustomer = postCustomerRepo.findByPostCustomerId(UUID.fromString(input.getToCustomerId()));
            postOrder.setToCustomer(toCustomer);
        }
        else {
            //Create new post customer
            GeoPoint geoPoint = new GeoPoint();
            geoPoint.setLatitude(input.getToCustomerLat());
            geoPoint.setLongitude(input.getToCustomerLng());
            geoPointRepo.save(geoPoint);
            PostalAddress postalAddress = new PostalAddress();
            postalAddress.setAddress(input.getToCustomerAddress());
            postalAddress.setGeoPoint(geoPoint);
            postalAddressRepo.save(postalAddress);
            PostCustomer toCustomer = new PostCustomer();
            toCustomer.setPostCustomerName(input.getToCustomerName());
            toCustomer.setPhoneNum(input.getToCustomerPhoneNum());
            toCustomer.setPostalAddress(postalAddress);
            postCustomerRepo.save(toCustomer);
            postOrder.setToCustomer(toCustomer);
        }
        postOrder.setPackageName(input.getPackageName());
        postOrder.setWeight(input.getWeight());
        postOrder.setDescription(input.getDescription());
        postOrder.getPostPackageType().setPostPackageTypeId(input.getPostPackageTypeId());
        postOrder.getStatusItem().setStatusId("ORDER_CREATED");
        postOrder.setStatusId("ORDER_CREATED");
        PostOrder postOrderResult = postOrderRepo.save(postOrder);
        result.setStatusCode("SUCCESS");
        result.setPostOrder(postOrderResult);
        result.setDetail("Create post order success");
        return result;
    }
    public ResponseSample CancelPostOrder(String postOrderId) {
        try {
            postOrderRepo.updatePostOrderStatus(UUID.fromString(postOrderId),"ORDER_CANCELLED");
            return new ResponseSample("SUCCESS", "Delete order success");
        } catch (Exception e) {
            return new ResponseSample("ERROR", "");
        }
    }
}
