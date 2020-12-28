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
import com.hust.baseweb.applications.postsys.model.postshiporder.UpdatePostShipOrderInputModel;
import com.hust.baseweb.applications.postsys.repo.PostCustomerRepo;
import com.hust.baseweb.applications.postsys.repo.PostOrderRepo;
import com.hust.baseweb.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Log4j2
public class PostOrderService {

    @Autowired
    private PostOrderRepo postOrderRepo;
    @Autowired
    UserService userService;
    @Autowired
    PostCustomerRepo postCustomerRepo;
    @Autowired
    GeoPointRepo geoPointRepo;
    @Autowired
    PostalAddressRepo postalAddressRepo;

    public List<PostOrder> findAllPostOrder() {
        List<PostOrder> orders = (List<PostOrder>) postOrderRepo.findAll();
        return orders;
    }

    public CreatePostShipOrderOutputModel createPostShipOrder(CreatePostShipOrderInputModel input) {
        input.print();
        CreatePostShipOrderOutputModel result = new CreatePostShipOrderOutputModel();
        PostOrder postOrder = new PostOrder();
        if (input.isFromCustomerExist()) {
            PostCustomer fromCustomer = postCustomerRepo.findByPostCustomerId(UUID.fromString(input.getFromCustomerId()));
            postOrder.setFromCustomer(fromCustomer);
        } else {
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
        } else {
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
            postOrderRepo.updatePostOrderStatus(UUID.fromString(postOrderId), "ORDER_CANCELLED");
            return new ResponseSample("SUCCESS", "Delete order success");
        } catch (Exception e) {
            return new ResponseSample("ERROR", "");
        }
    }

    @Transactional("jpa_transaction_manager")
    public ResponseSample updatePostOrderStatus(UpdatePostShipOrderInputModel updatePostShipOrderInputModel) {
        try {
            if (updatePostShipOrderInputModel.getCurrentPostOfficeId() == null ||
                updatePostShipOrderInputModel.getCurrentPostOfficeId().length() == 0) {
                postOrderRepo.updatePostOrderStatus(
                    UUID.fromString(updatePostShipOrderInputModel.getPostOrderId()),
                    updatePostShipOrderInputModel.getStatus());
            } else {
                postOrderRepo.updatePostOrderStatusAndCurrentPostOffice(
                    UUID.fromString(updatePostShipOrderInputModel.getPostOrderId()),
                    updatePostShipOrderInputModel.getStatus(),
                    updatePostShipOrderInputModel.getCurrentPostOfficeId());
            }
            return new ResponseSample("SUCCESS", "Update order success");
        } catch (Exception e) {
            log.error(e);
            return new ResponseSample("ERROR", "");
        }
    }

    public List<PostOrder> findAllOrderByDay(Date input) {
        Date tomorrow = new Date(input.getTime() + (1000 * 60 * 60 * 24));
        log.info("Select post order between " + input + ", " + tomorrow);
        return postOrderRepo.findAllByCreatedStampGreaterThanEqualAndCreatedStampLessThan(input, tomorrow);
    }

    public List<PostOrder> findPostOrderbyDate(String statusId, Date fromDate, Date toDate) {
        Date tomorrow = new Date(toDate.getTime() + (1000 * 60 * 60 * 24));
        return postOrderRepo.findByStatusIdAndCreatedStampGreaterThanEqualAndCreatedStampLessThan(statusId, fromDate, toDate);
    }

}
