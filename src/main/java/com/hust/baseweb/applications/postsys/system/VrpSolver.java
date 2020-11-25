package com.hust.baseweb.applications.postsys.system;

import com.google.ortools.Loader;
import com.google.ortools.constraintsolver.*;
import com.hust.baseweb.applications.geo.entity.GeoPoint;
import com.hust.baseweb.utils.LatLngUtils;

import java.util.ArrayList;
import java.util.List;

public class VrpSolver {
    private long[][] distanceMatrix;
    private int vehicleNumber;
    private int depot;
    private List<GeoPoint> geoPoints;
    public VrpSolver(List<GeoPoint> geoPoints, int vehicleNumber, int depot) {
        this.vehicleNumber = vehicleNumber;
        this.depot = depot;
        this.geoPoints = geoPoints;
        createDistanceMatrix(geoPoints);
    }

    public void createDistanceMatrix(List<GeoPoint> geoPoints) {
        int nPoints = geoPoints.size();
        distanceMatrix = new long[nPoints][nPoints];
        for (int i = 0; i < nPoints; i++) {
//            System.out.println(geoPoints.get(i).getLatitude() + "|" + geoPoints.get(i).getLongitude());
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
    public void printSolution(RoutingModel routing, RoutingIndexManager manager, Assignment solution) {
        // Inspect solution.
        long maxRouteDistance = 0;
        for (int i = 0; i < vehicleNumber; ++i) {
            long index = routing.start(i);
            System.out.println("Route for Vehicle " + i + ":");
            long routeDistance = 0;
            StringBuilder route = new StringBuilder();
            while (!routing.isEnd(index)) {
                route.append(manager.indexToNode(index)).append(" -> ");
                long previousIndex = index;
                index = solution.value(routing.nextVar(index));
                routeDistance += routing.getArcCostForVehicle(previousIndex, index, i);
            }
            System.out.println(route.toString() + manager.indexToNode(index));
            System.out.println("Distance of the route: " + routeDistance + "m");
            maxRouteDistance = Math.max(routeDistance, maxRouteDistance);
        }
        System.out.println("Maximum of the route distances: " + maxRouteDistance + "m");
    }

    public void validDistanceMatrix() {
        int n = distanceMatrix.length;
        for(int k=0;k<n;k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if(j<=i) //check for only upper half of diagonal
                        continue;
                    if(distanceMatrix[i][k]+distanceMatrix[k][j]<distanceMatrix[i][j]) {
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
    public Route solve() {
        Loader.loadNativeLibraries();
//        this.printDistanceMatrix();
        // Instantiate the data problem.

        // Create Routing Index Manager
        RoutingIndexManager manager =
            new RoutingIndexManager(distanceMatrix.length, vehicleNumber, depot);

        // Create Routing Model.
        RoutingModel routing = new RoutingModel(manager);

        // Create and register a transit callback.
        final int transitCallbackIndex =
            routing.registerTransitCallback((long fromIndex, long toIndex) -> {
                // Convert from routing variable Index to user NodeIndex.
                int fromNode = manager.indexToNode(fromIndex);
                int toNode = manager.indexToNode(toIndex);
                return distanceMatrix[fromNode][toNode];
            });

        // Define cost of each arc.
        routing.setArcCostEvaluatorOfAllVehicles(transitCallbackIndex);

        // Add Distance constraint.
        routing.addDimension(transitCallbackIndex, 0, Long.MAX_VALUE,
                             true, // start cumul to zero
                             "Distance");
        RoutingDimension distanceDimension = routing.getMutableDimension("Distance");
        distanceDimension.setGlobalSpanCostCoefficient(100);

//         Setting first solution heuristic.
        RoutingSearchParameters searchParameters =
            main.defaultRoutingSearchParameters()
                .toBuilder()
                .setFirstSolutionStrategy(FirstSolutionStrategy.Value.PATH_CHEAPEST_ARC)
                .build();

//        RoutingSearchParameters searchParameters =
//            main.defaultRoutingSearchParameters()
//                .toBuilder()
//                .setFirstSolutionStrategy(FirstSolutionStrategy.Value.PATH_CHEAPEST_ARC)
//                .setLocalSearchMetaheuristic(LocalSearchMetaheuristic.Value.GUIDED_LOCAL_SEARCH)
//                .setTimeLimit(Duration.newBuilder().setSeconds(30).build())
//                .setLogSearch(true)
//                .build();

        // Solve the problem.
        Assignment solution = routing.solveWithParameters(searchParameters);
        List<List<Integer>> routes = new ArrayList<>();
        List<Long> distances = new ArrayList<>();
        switch (routing.status()) {
            case 0:
                System.out.println("ROUTING_NOT_SOLVED: Problem not solved yet.");
                break;
            case 1:
                System.out.println("ROUTING_SUCCESS: Problem solved successfully.");
                for (int i = 0; i < vehicleNumber; ++i) {
                    routes.add(new ArrayList<>());
                    long index = routing.start(i);
                    long routeDistance = 0;
                    while (!routing.isEnd(index)) {
                        long previousIndex = index;
                        index = solution.value(routing.nextVar(index));
                        routes.get(i).add(manager.indexToNode(index));
                        routeDistance += routing.getArcCostForVehicle(previousIndex, index, i);
                    }
                    distances.add(routeDistance);
                }
                return new Route(true, routes, distances);
            case 2:
                System.out.println("ROUTING_FAIL: No solution found to the problem.");
                break;
            case 3:
                System.out.println("ROUTING_FAIL_TIMEOUT: Time limit reached before finding a solution.");
                break;
            case 4:
                System.out.println("ROUTING_INVALID: Model, model parameters, or flags are not valid.");
                break;
        }
        return new Route(false, null, null);
    }
}
