package com.hust.baseweb.applications.postsys.system;

import com.hust.baseweb.applications.postsys.entity.*;
import com.hust.baseweb.applications.postsys.model.posttrip.PostShipOrderTripPostOfficeAssignmentOM;
import com.hust.baseweb.applications.postsys.repo.*;
import com.hust.baseweb.utils.LatLngUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.math3.util.Pair;
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
        postOrderRepo.saveAll(findShortestTrip(postOfficeTrips, postOffices, postOrders));
    }

    public List<PostOrder> findShortestTrip(
        List<PostOfficeTrip> postOfficeTrips,
        List<PostOffice> postOffices,
        List<PostOrder> postOrders
    ) {
        List<PostOrder> result = new ArrayList<>();
        for (PostOrder postOrder : postOrders) {
            if (postOrder.getToPostOfficeId().equals(postOrder.getCurrentPostOfficeId())) {
                postOrder.setStatusId("POST_ORDER_FINAL_TRIP");
                log.info("Post order " + postOrder.getPostShipOrderId() + " reach final post office");
                postOrderRepo.save(postOrder);
                return result;
            }
            Map<String, Integer> postOfficeIndex = new HashMap<>();
            for (int i = 0; i < postOffices.size(); i++) {
                postOfficeIndex.put(postOffices.get(i).getPostOfficeId(), i);
            }
            // init matrix distance
            int[][] distanceMatrix = new int[postOffices.size()][postOffices.size()];
            for (int i = 0; i < postOffices.size(); i++) {
                for (int j = 0; j < postOffices.size(); j++) {
                    if (i != j) {
                        distanceMatrix[postOfficeIndex.get(postOffices.get(i).getPostOfficeId())][postOfficeIndex.get(
                            postOffices.get(j).getPostOfficeId())]
                            = distanceMatrix[postOfficeIndex.get(postOffices
                                                                     .get(j)
                                                                     .getPostOfficeId())][postOfficeIndex.get(
                            postOffices.get(i).getPostOfficeId())]
                            = Integer.MAX_VALUE;
                    }
                }
            }
            for (int i = 0; i < postOffices.size(); i++) {
                for (int j = 0; j < postOffices.size(); j++) {
                    for (PostOfficeTrip postOfficeTrip : postOfficeTrips) {
                        if (postOfficeTrip
                                .getFromPostOffice().getPostOfficeId().equals(postOffices.get(i).getPostOfficeId()) &&
                            postOfficeTrip
                                .getToPostOffice()
                                .getPostOfficeId()
                                .equals(postOffices.get(j).getPostOfficeId())) {
                            distanceMatrix[postOfficeIndex.get(postOffices
                                                                   .get(i)
                                                                   .getPostOfficeId())][postOfficeIndex.get(
                                postOffices.get(j).getPostOfficeId())] =
                                LatLngUtils.distance(
                                    postOffices.get(i).getPostalAddress(),
                                    postOffices.get(j).getPostalAddress());
                            String fromPostOffice = postOfficeTrip.getFromPostOfficeId();
                            String toPostOffice = postOfficeTrip.getToPostOfficeId();
                        }
                    }
                }
            }
            int source = postOfficeIndex.get(postOrder.getCurrentPostOfficeId());
            int dest = postOfficeIndex.get(postOrder.getToPostOfficeId());
            int[] dist = new int[postOffices.size()];
            int[] prev = new int[postOffices.size()];
            boolean[] visited = new boolean[postOffices.size()];
            int size = postOffices.size();
            PriorityQueue<Pair<Integer, Integer>> Q1 = new PriorityQueue<Pair<Integer, Integer>>((v1, v2) -> Integer.compare(
                v1.getValue(), v2.getValue()
            ));
            Queue<Integer> Q = new PriorityQueue<>((integer, t1) -> Integer.compare(
                dist[integer],
                dist[t1]));
            for (PostOffice postOffice : postOffices) {
                if (!postOffice.getPostOfficeId().equals(source)) {
                    dist[postOfficeIndex.get(postOffice.getPostOfficeId())] = Integer.MAX_VALUE;
                    prev[postOfficeIndex.get(postOffice.getPostOfficeId())] = -1;
                    visited[postOfficeIndex.get(postOffice.getPostOfficeId())] = false;
                    Q.offer(postOfficeIndex.get(postOffice.getPostOfficeId()));
                    Q1.offer(
                        new Pair(
                            postOfficeIndex.get(postOffice.getPostOfficeId()),
                            dist[postOfficeIndex.get(postOffice.getPostOfficeId())]
                        ));
                }
            }
            dist[source] = 0;
            while (size > 0) {
                int u = getMinDist(dist, postOffices.size(), visited);
                size--;
                visited[u] = true;
                if (u == dest) {
                    System.out.println(dest);
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
            if (prev[dest] == -1) {
                log.info("Khong tim duoc duong di, Order Id: " +
                         postOrder.getPostShipOrderId() +
                         ", from post office: " +
                         postOrder.getFromPostOffice().getPostOfficeName() +
                         " (" +
                         postOrder.getFromPostOfficeId() +
                         ") " +
                         ",post office: " +
                         postOrder.getToPostOffice().getPostOfficeName() +
                         " (" +
                         postOrder.getToPostOfficeId() +
                         ")");
                return result;
            }
            while (prev[cur] != -1) {
                pathLog += " <- " + postOffices.get(prev[cur]).getPostOfficeId();
                if (prev[cur] == source) {
                    nextOfficeIndex = cur;
                }
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
            result.add(postOrder);
            log.info("Order Id: " +
                     postOrder.getPostShipOrderId() +
                     ", assign from post office: " +
                     postOrder.getFromPostOffice().getPostOfficeName() +
                     " (" +
                     postOrder.getFromPostOfficeId() +
                     ") " +
                     " to post office: " +
                     postOrder.getToPostOffice().getPostOfficeName() +
                     " (" +
                     postOrder.getToPostOfficeId() +
                     ") " +
                     ", trip id: " +
                     postShipOrderTripPostOfficeAssignment.getPostOfficeTripId() +
                     ", assignment id: " +
                     postShipOrderTripPostOfficeAssignment.getPostShipOrderPostOfficeTripAssignmentId());
        }
        return result;
    }

    public int getMinDist(int dist[], int size, boolean visited[]) {
        int min = Integer.MAX_VALUE;
        int result = 0;
        for (int i = 0; i < size; i++) {
            if (!visited[i]) {
                if (dist[i] < min) {
                    min = dist[i];
                    result = i;
                }
            }
        }
        return result;
    }

    public void printDistanceMatrix(int[][] distanceMatrix) {
        for (int i = 0; i < distanceMatrix.length; i++) {
            for (int j = 0; j < distanceMatrix[i].length; j++) {
                System.out.print(distanceMatrix[i][j] + "\t");
            }
            System.out.println();
        }
    }
}
