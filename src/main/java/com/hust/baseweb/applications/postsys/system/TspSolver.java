package com.hust.baseweb.applications.postsys.system;

import com.google.ortools.Loader;
import com.google.ortools.constraintsolver.*;
import com.hust.baseweb.applications.geo.entity.GeoPoint;
import com.hust.baseweb.utils.LatLngUtils;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;

@Log4j2
public class TspSolver {

    private long[][] distanceMatrix;
    private int depot;
    private List<GeoPoint> geoPoints;

    public TspSolver(List<GeoPoint> geoPoints, int depot) {
        this.depot = depot;
        this.geoPoints = geoPoints;
        createDistanceMatrix(geoPoints);
    }

    public void createDistanceMatrix(List<GeoPoint> geoPoints) {
        int nPoints = geoPoints.size();
        distanceMatrix = new long[nPoints][nPoints];
        for (int i = 0; i < nPoints; i++) {
            for (int j = 0; j < nPoints; j++) {
                distanceMatrix[i][j] = LatLngUtils.distance(
                    geoPoints.get(i).getLatitude(),
                    geoPoints.get(i).getLongitude(),
                    geoPoints.get(j).getLatitude(),
                    geoPoints.get(j).getLongitude()
                );
            }
        }
    }

    public void printDistanceMatrix() {
        for (int i = 0; i < this.distanceMatrix.length; i++) {
            for (int j = 0; j < this.distanceMatrix.length; j++) {
                System.out.print(this.distanceMatrix[i][j] + "|");
            }
            System.out.println();
        }
    }

    @SuppressWarnings("unused")
    public void printSolution(
        RoutingModel routing, RoutingIndexManager manager, Assignment solution
    ) {
        log.info("Tsp solver, obj = " + solution.objectiveValue());
        log.info("Route:");
        long routeDistance = 0;
        String route = "";
        long index = routing.start(0);
        while (!routing.isEnd(index)) {
            route += manager.indexToNode(index) + " -> ";
            long previousIndex = index;
            index = solution.value(routing.nextVar(index));
            routing.getArcCostForVehicle(previousIndex, index, 0);
        }
        route += manager.indexToNode(routing.end(0));
        log.info(route);
        log.info("Tsp solver, route distance = " + routeDistance + " m");
    }

    public void validDistanceMatrix() {
        int n = distanceMatrix.length;
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (j <= i) //check for only upper half of diagonal
                    {
                        continue;
                    }
                    if (distanceMatrix[i][k] + distanceMatrix[k][j] < distanceMatrix[i][j]) {
//                        System.out.print(distanceMatrix[i][k] + " ");
//                        System.out.print(distanceMatrix[k][j] + " ");
//                        System.out.print(distanceMatrix[i][k] + distanceMatrix[k][j] + " ");
//                        System.out.println(distanceMatrix[i][j]);
                        this.distanceMatrix[i][j] = distanceMatrix[i][k] + distanceMatrix[k][j];
                        this.distanceMatrix[j][i] = distanceMatrix[i][k] + distanceMatrix[k][j];
                    }
                }
            }
        }
    }

    public List<Integer> solve() {
        Loader.loadNativeLibraries();
        createDistanceMatrix(geoPoints);
        RoutingIndexManager manager = new RoutingIndexManager(geoPoints.size(), 1, depot);
        RoutingModel routing = new RoutingModel(manager);
        final int transitCallbackIndex =
            routing.registerTransitCallback((long fromIndex, long toIndex) -> {
                // Convert from routing variable Index to user NodeIndex.
                int fromNode = manager.indexToNode(fromIndex);
                int toNode = manager.indexToNode(toIndex);
                return distanceMatrix[fromNode][toNode];
            });
        routing.setArcCostEvaluatorOfAllVehicles(transitCallbackIndex);
        RoutingSearchParameters searchParameters =
            main.defaultRoutingSearchParameters()
                .toBuilder()
                .setFirstSolutionStrategy(FirstSolutionStrategy.Value.PATH_CHEAPEST_ARC)
                .build();
        Assignment solution = routing.solveWithParameters(searchParameters);
        List<Integer> finalSolution = new ArrayList<>();
        printSolution(routing, manager, solution);
        long index = routing.start(0);
        while (!routing.isEnd(index)) {
            index = solution.value(routing.nextVar(index));
            finalSolution.add(manager.indexToNode(index));
        }
        return finalSolution;
    }
}
