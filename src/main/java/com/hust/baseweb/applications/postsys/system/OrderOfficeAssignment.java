package com.hust.baseweb.applications.postsys.system;

import com.hust.baseweb.applications.postsys.entity.*;
import com.hust.baseweb.applications.postsys.model.posttrip.PostShipOrderTripPostOfficeAssignmentOM;
import com.hust.baseweb.applications.postsys.repo.*;
import com.hust.baseweb.utils.LatLngUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Log4j2
@Component
public class OrderOfficeAssignment {

    PostOfficeRepo postOfficeRepo;

    PostOrderRepo postOrderRepo;

    PostTripRepo postTripRepo;

    PostShipOrderFixedTripPostOfficeAssignmentRepo postShipOrderFixedTripPostOfficeAssignmentRepo;

    PostShipOrderTripPostOfficeAssignmentRepo postShipOrderTripPostOfficeAssignmentRepo;

    @Autowired
    public void setPostShipOrderTripPostOfficeAssignmentRepo(PostShipOrderTripPostOfficeAssignmentRepo postShipOrderTripPostOfficeAssignmentRepo) {
        this.postShipOrderTripPostOfficeAssignmentRepo = postShipOrderTripPostOfficeAssignmentRepo;
    }

    @Autowired
    public void setPostShipOrderFixedTripPostOfficeAssignmentRepo(PostShipOrderFixedTripPostOfficeAssignmentRepo postShipOrderFixedTripPostOfficeAssignmentRepo) {
        this.postShipOrderFixedTripPostOfficeAssignmentRepo = postShipOrderFixedTripPostOfficeAssignmentRepo;
    }

    @Autowired
    public void setPostOfficeRepo(PostOfficeRepo postOfficeRepo) {
        this.postOfficeRepo = postOfficeRepo;
    }

    @Autowired
    public void setPostOrderRepo(PostOrderRepo postOrderRepo) {
        this.postOrderRepo = postOrderRepo;
    }

    @Autowired
    public void setPostTripRepo(PostTripRepo postTripRepo) {
        this.postTripRepo = postTripRepo;
    }

    @Scheduled(fixedDelayString = "${postsys.post_assign_delay}")
    @Transactional("jpa_transaction_manager")
    public void orderOfficeAssign() {
        // select all post office
        List<PostOffice> postOffices = postOfficeRepo.findAll();
        // select all post post order
        List<PostOrder> postOrders = postOrderRepo.findAll();
        for (PostOrder postOrder : postOrders) {
            if (postOrder.getStatusItem().getStatusId().equals("ORDER_CREATED")) {
                //assign to post office
                double min_dist_start = Integer.MAX_VALUE, min_dist_end = Integer.MAX_VALUE;
                PostOffice start_office = null, end_office = null;
                double lat1 = postOrder.getFromCustomer().getPostalAddress().getGeoPoint().getLatitude();
                double lng1 = postOrder.getFromCustomer().getPostalAddress().getGeoPoint().getLongitude();
                double lat2 = postOrder.getToCustomer().getPostalAddress().getGeoPoint().getLatitude();
                double lng2 = postOrder.getToCustomer().getPostalAddress().getGeoPoint().getLongitude();
                for (PostOffice postOffice : postOffices) {
                    double lat_office = postOffice.getPostalAddress().getGeoPoint().getLatitude();
                    double lng_office = postOffice.getPostalAddress().getGeoPoint().getLongitude();
                    double dist_start = LatLngUtils.distance(lat1, lng1, lat_office, lng_office);
                    double dist_end = LatLngUtils.distance(lat2, lng2, lat_office, lng_office);
                    if (dist_start < min_dist_start) {
                        min_dist_start = dist_start;
                        start_office = postOffice;
                    }
                    if (dist_end < min_dist_end) {
                        min_dist_end = dist_end;
                        end_office = postOffice;
                    }
                }
                // update status of order
                postOrder.setStatusId("POST_ORDER_ASSIGNED");
                postOrder.setFromPostOfficeId(start_office.getPostOfficeId());
                postOrder.setToPostOfficeId(end_office.getPostOfficeId());
                log.info("Order " + postOrder.getPostShipOrderId() + " assigned to " +
                         start_office.getPostOfficeId() + " => " + end_office.getPostOfficeId() +
                         ", dist_start: " + min_dist_start + ", dist_end: " + min_dist_end);
                postOrderRepo.save(postOrder);
            }
        }
    }

    @Scheduled(fixedDelayString = "${postsys.post_assign_delay}")
    @Transactional("jpa_transaction_manager")
    public void orderTripAssignment() {
        List<PostOrder> postOrders = postOrderRepo.findByStatusId("POST_ORDER_READY_FIND_PATH");
        List<PostOfficeTrip> postOfficeTrips = postTripRepo.findAll();
        List<PostOffice> postOffices = postOfficeRepo.findAll();
        for (PostOrder postOrder : postOrders) {
            findShortestTrip(postOfficeTrips, postOffices, postOrder);
        }
    }

    public void findShortestTrip(
        List<PostOfficeTrip> postOfficeTrips,
        List<PostOffice> postOffices,
        PostOrder postOrder
    ) {
        if (postOrder.getToPostOfficeId().equals(postOrder.getCurrentPostOfficeId())) {
            postOrder.setStatusId("POST_ORDER_FINAL_TRIP");
            log.info("Post order " + postOrder.getPostShipOrderId() + " reach final post office");
            postOrderRepo.save(postOrder);
            return;
        }
        Map<String, Integer> postOfficeIndex = new HashMap<>();
        for (int i = 0; i < postOffices.size(); i++) {
            postOfficeIndex.put(postOffices.get(i).getPostOfficeId(), i);
            log.info(postOffices.get(i).getPostOfficeId() + " <-> " + i);
        }
        // init matrix distance
        int[][] distanceMatrix = new int[postOffices.size()][postOffices.size()];
        for (int i = 0; i < postOffices.size(); i++) {
            for (int j = 0; j < postOffices.size(); j++) {
                if (i != j) {
                    distanceMatrix[postOfficeIndex.get(postOffices.get(i).getPostOfficeId())][postOfficeIndex.get(
                        postOffices.get(j).getPostOfficeId())]
                        = distanceMatrix[postOfficeIndex.get(postOffices.get(j).getPostOfficeId())][postOfficeIndex.get(
                        postOffices.get(i).getPostOfficeId())]
                        = Integer.MAX_VALUE;
                }
            }
        }
        for (int i = 0; i < postOffices.size(); i++) {
            for (int j = 0; j < postOffices.size(); j++) {
                for (PostOfficeTrip postOfficetrip : postOfficeTrips) {
                    if (postOfficetrip
                            .getFromPostOffice().getPostOfficeId().equals(postOffices.get(i).getPostOfficeId()) &&
                        postOfficetrip
                            .getToPostOffice()
                            .getPostOfficeId()
                            .equals(postOffices.get(j).getPostOfficeId())) {
                        distanceMatrix[postOfficeIndex.get(postOffices.get(i).getPostOfficeId())][postOfficeIndex.get(
                            postOffices.get(j).getPostOfficeId())] =
                            LatLngUtils.distance(
                                postOffices.get(i).getPostalAddress(),
                                postOffices.get(j).getPostalAddress());
                    }
                }
            }
        }
        int source = postOfficeIndex.get(postOrder.getCurrentPostOfficeId());
        int dest = postOfficeIndex.get(postOrder.getToPostOfficeId());
        int[] dist = new int[postOffices.size()];
        int[] prev = new int[postOffices.size()];
//        int[] ntrips = new int[postOffices.size()];
        Queue<Integer> Q = new PriorityQueue<>((integer, t1) -> Integer.compare(
            distanceMatrix[integer][source],
            distanceMatrix[t1][source]));
        for (PostOffice postOffice : postOffices) {
            if (!postOffice.getPostOfficeId().equals(source)) {
                dist[postOfficeIndex.get(postOffice.getPostOfficeId())] = Integer.MAX_VALUE;
                prev[postOfficeIndex.get(postOffice.getPostOfficeId())] = -1;
//                ntrips[postOfficeIndex.get(postOffice.getPostOfficeId())] = 0;
                Q.offer(postOfficeIndex.get(postOffice.getPostOfficeId()));
            }
        }
        dist[source] = 0;
        while (!Q.isEmpty()) {
            int u = Q.poll();
            if (u == dest) {
                break;
            }
            for (int v = 0; v < postOffices.size(); v++) {
                if (distanceMatrix[u][v] != Integer.MAX_VALUE) {
                    int temp = dist[u] + distanceMatrix[u][v];
                    if (temp < 0) {
                        temp = Integer.MAX_VALUE;
                    }
                    if (temp < dist[v]) {
                        dist[v] = temp;
                        prev[v] = u;
//                        ntrips[v] += 1;
                    }
                }
            }
        }
        int current_order = 0;
        try {
            PostShipOrderTripPostOfficeAssignmentOM postShipOrderTripPostOfficeAssignment = postShipOrderTripPostOfficeAssignmentRepo
                .findByMaxDeliveryOrderPostShipOrderId(postOrder.getPostShipOrderId());
            current_order = postShipOrderTripPostOfficeAssignment.getDeliveryOrder();
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            log.error("Order executing trip not found, create new");
        }
        String pathLog = "Expected path order Id " + postOrder.getPostShipOrderId() + ": ";
        int cur = dest, nextOfficeIndex = -1;
        pathLog += postOffices.get(dest).getPostOfficeId();
        while (prev[cur] != -1) {
            pathLog += " <- " + postOffices.get(prev[cur]).getPostOfficeId();
            if (prev[cur] == source) nextOfficeIndex = cur;
            cur = prev[cur];
        }
        log.info(pathLog);
        PostShipOrderTripPostOfficeAssignment postShipOrderTripPostOfficeAssignment = new PostShipOrderTripPostOfficeAssignment();
        postShipOrderTripPostOfficeAssignment.setDeliveryOrder(current_order + 1);
        postShipOrderTripPostOfficeAssignment.setPostShipOrderId(postOrder.getPostShipOrderId());
        for (PostOfficeTrip postOfficetrip : postOfficeTrips) {
            String nextPostOfficeId = postOffices.get(nextOfficeIndex).getPostOfficeId();
            if (postOfficetrip.getFromPostOffice().getPostOfficeId().equals(postOrder.getCurrentPostOfficeId()) &&
                postOfficetrip.getToPostOffice().getPostOfficeId().equals(nextPostOfficeId)) {
                postShipOrderTripPostOfficeAssignment.setPostOfficeTripId(postOfficetrip.getPostOfficeTripId());
            }
        }
        postShipOrderTripPostOfficeAssignment = postShipOrderTripPostOfficeAssignmentRepo.save(
            postShipOrderTripPostOfficeAssignment);
        postOrder.setStatusId("POST_ORDER_READY_DELIVERY");
        postOrderRepo.save(postOrder);
        log.info("Order Id: " +
                 postOrder.getPostShipOrderId() +
                 ", assign from post office: " +
                 postOrder.getFromPostOffice().getPostOfficeName() + " (" +postOrder.getFromPostOfficeId() + ") " +
                 " to post office: " +
                 postOrder.getToPostOffice().getPostOfficeName() + " (" +postOrder.getToPostOfficeId() + ") " +
                 ", trip id: " +
                 postShipOrderTripPostOfficeAssignment.getPostOfficeTripId() +
                 ", assignment id: " + postShipOrderTripPostOfficeAssignment.getPostShipOrderPostOfficeTripAssignmentId());
    }
//
//    public static <T, E> T getKeysByValue(Map<T, E> map, E value) {
//        for (Map.Entry<T, E> entry : map.entrySet()) {
//            if (Objects.equals(value, entry.getValue())) {
//                return entry.getKey();
//            }
//        }
//        return null;
//    }
}
