package com.hust.baseweb.applications.postsys.service;

import com.hust.baseweb.applications.geo.entity.GeoPoint;
import com.hust.baseweb.applications.postsys.entity.PostOffice;
import com.hust.baseweb.applications.postsys.entity.PostOrder;
import com.hust.baseweb.applications.postsys.entity.Postman;
import com.hust.baseweb.applications.postsys.model.postsolve.PostOfficeVrpSolveInputModel;
import com.hust.baseweb.applications.postsys.model.postsolve.PostOfficeVrpSolveOutputModel;
import com.hust.baseweb.applications.postsys.repo.PostOfficeRepo;
import com.hust.baseweb.applications.postsys.repo.PostOrderRepo;
import com.hust.baseweb.applications.postsys.repo.PostmanRepo;
import com.hust.baseweb.applications.postsys.system.Route;
import com.hust.baseweb.applications.postsys.system.VrpSolver;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
public class SolverService {
    @Autowired
    PostOfficeRepo postOfficeRepo;
    @Autowired
    PostOrderRepo postOrderRepo;
    @Autowired
    PostmanRepo postmanRepo;
    public PostOfficeVrpSolveOutputModel postOfficeVrpSolve(PostOfficeVrpSolveInputModel postOfficeVrpSolveInputModel) {
        PostOffice postOffice = postOfficeRepo.findById(postOfficeVrpSolveInputModel.getPostOfficeId()).get();
        List<PostOrder> postOrders = postOrderRepo.findByPostShipOrderIdIn(postOfficeVrpSolveInputModel.getPostOrderIds());
        List<GeoPoint> geoPoints = new ArrayList<>();
        geoPoints.add(postOffice.getPostalAddress().getGeoPoint());
        if (postOfficeVrpSolveInputModel.getType().equals("pick")) {
            for (int i = 0; i < postOrders.size(); i++) {
                geoPoints.add(postOrders.get(i).getFromCustomer().getPostalAddress().getGeoPoint());
            }
        }
        else {
            for (int i = 0; i < postOrders.size(); i++) {
                geoPoints.add(postOrders.get(i).getToCustomer().getPostalAddress().getGeoPoint());
            }
        }
        if (postOrders.size() == 0) {
            log.info("No order to solve, postOfficeId = " + postOfficeVrpSolveInputModel.getPostOfficeId());
            return new PostOfficeVrpSolveOutputModel(false, null, null, postOfficeVrpSolveInputModel.getPostmanIds());
        }
        List<Postman> postmen = postmanRepo.findByPostmanIdIn(postOfficeVrpSolveInputModel.getPostmanIds());
        if (postOrders.size() == 0) {
            log.info("No postman to solve, postOfficeId = " + postOfficeVrpSolveInputModel.getPostOfficeId());
            return new PostOfficeVrpSolveOutputModel(false, null, null, postOfficeVrpSolveInputModel.getPostmanIds());
        }
        VrpSolver vrpSolver = new VrpSolver(geoPoints, postmen.size(), 0);
        Route route =  vrpSolver.solve();
        PostOfficeVrpSolveOutputModel solution = new PostOfficeVrpSolveOutputModel(false, null, null, postOfficeVrpSolveInputModel.getPostmanIds());
        if (route.isSolutionFound()) {
            solution.setRoutes(new ArrayList<>());
            for (int i = 0; i < postmen.size(); i++) {
                solution.getRoutes().add(new ArrayList<>());
                for (int j = 0; j < route.getIndexes().get(i).size(); j++) {
                    if (route.getIndexes().get(i).get(j) > 0) {
                        solution.getRoutes().get(i).add(postOrders.get(route.getIndexes().get(i).get(j) -1));
                    }
                }
            }
            solution.setSolutionFound(true);
            solution.setDistance(route.getDistance());
        }
        return solution;
    }
}
