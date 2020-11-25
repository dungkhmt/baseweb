package com.hust.baseweb.applications.postsys.system;

import com.hust.baseweb.applications.postsys.entity.PostOffice;
import com.hust.baseweb.applications.postsys.entity.PostOrder;
import com.hust.baseweb.applications.postsys.entity.PostTrip;
import com.hust.baseweb.applications.postsys.repo.PostOfficeRepo;
import com.hust.baseweb.applications.postsys.repo.PostOrderRepo;
import com.hust.baseweb.applications.postsys.repo.PostTripRepo;
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

    @Scheduled(fixedDelayString="${postsys.post_assign_delay}")
    @Transactional("jpa_transaction_manager")
    public void orderOfficeAssign() {
        log.info("Staring order office assignment");
        // select all post office
        List<PostOffice> postOffices = postOfficeRepo.findAll();
        // select all post post order
        List<PostOrder> postOrders = postOrderRepo.findAll();
        for(PostOrder postOrder: postOrders) {
            if (postOrder.getStatusItem().getStatusId().equals("ORDER_CREATED")) {
                //assign to post office
                double min_dist_start = Integer.MAX_VALUE, min_dist_end = Integer.MAX_VALUE;
                PostOffice start_office = null, end_office = null;
                double lat1 = postOrder.getFromCustomer().getPostalAddress().getGeoPoint().getLatitude();
                double lng1 = postOrder.getFromCustomer().getPostalAddress().getGeoPoint().getLongitude();
                double lat2 = postOrder.getToCustomer().getPostalAddress().getGeoPoint().getLatitude();
                double lng2 = postOrder.getToCustomer().getPostalAddress().getGeoPoint().getLongitude();
                for (PostOffice postOffice: postOffices) {
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
        List<PostOrder> postOrders = postOrderRepo.findByStatusId("POST_ORDER_ASSIGNED");
        List<PostTrip> postTrips = postTripRepo.findAll();
        List<PostOffice> postOffices = postOfficeRepo.findAll();
        for (PostOrder postOrder: postOrders) {
            findShortestTrip(postTrips, postOffices, postOrder);
        }
    }

    public void findShortestTrip(List<PostTrip> postTrips, List<PostOffice> postOffices,PostOrder postOrder) {
        Map<String, Integer> postOfficeIndex = new HashMap<>();
        for (int i = 0; i < postOffices.size(); i++) {
            postOfficeIndex.put(postOffices.get(i).getPostOfficeId(), i);
        }
        // init matrix distance
        int[][] distanceMatrix = new int[postOffices.size()][postOffices.size()];

        for (int i = 0; i < postOffices.size(); i++) {
            for (int j = 0; j < postOffices.size(); j++) {
                boolean isAdjacent = false;
                for (PostTrip postTrip : postTrips) {
                    if (postTrip.getFromPostOffice().getPostOfficeId().equals(postOffices.get(i).getPostOfficeId()) &&
                        postTrip.getToPostOffice().getPostOfficeId().equals(postOffices.get(j).getPostOfficeId())) {
                        distanceMatrix[postOfficeIndex.get(postOffices.get(i))][postOfficeIndex.get(postOffices.get(i))] =
                            LatLngUtils.distance(postOffices.get(i).getPostalAddress(), postOffices.get(j).getPostalAddress());
                        isAdjacent = true;
                    }
                    if (!isAdjacent) distanceMatrix[postOfficeIndex.get(postOffices.get(i))][postOfficeIndex.get(postOffices.get(i))] = Integer.MAX_VALUE;
                }
            }
        }
        int source = postOfficeIndex.get(postOrder.getFromPostOfficeId());
        int dest = postOfficeIndex.get(postOrder.getToPostOfficeId());
        int[] dist = new int[postOffices.size()];
        int[] prev = new int[postOffices.size()];
        Queue<Integer> Q = new PriorityQueue<>((integer, t1) -> Integer.compare(distanceMatrix[integer][source], distanceMatrix[t1][source]));
        for (PostOffice postOffice : postOffices) {
            if (!postOffice.getPostOfficeId().equals(source)) {
                dist[postOfficeIndex.get(postOffice.getPostOfficeId())] =Integer.MAX_VALUE;
                prev[postOfficeIndex.get(postOffice.getPostOfficeId())] = -1;
                Q.offer(postOfficeIndex.get(postOffice.getPostOfficeName()));
            }
        }
        while (!Q.isEmpty()) {
            int u = Q.poll();
            if (u == dest) break;
            for (int v = 0; v < postOffices.size(); v++ ) {
                if (distanceMatrix[u][v] != Integer.MAX_VALUE) {
                    int temp = dist[u] + distanceMatrix[u][v];
                    if (temp < dist[v]) {
                        dist[v] = temp;
                        prev[v] = u;
                    }
                }
            }
        }
        System.out.println();
    }
}
