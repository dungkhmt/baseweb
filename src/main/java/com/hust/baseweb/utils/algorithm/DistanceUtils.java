package com.hust.baseweb.utils.algorithm;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;

public class DistanceUtils {
    public static <T> DirectionSolution<T> calculateGreedyTotalDistance(List<T> ts, BiFunction<T, T, Double> distanceFunction) {
        if (ts.isEmpty()) {
            return new DirectionSolution<>(0.0, new ArrayList<>());
        }
        double totalDistance = 0;
        Set<Integer> candidates = new HashSet<>();  //  T in ts can be duplicated
        for (int i = 0; i < ts.size(); i++) {
            candidates.add(i);
        }
        List<T> tour = new ArrayList<>();
        tour.add(ts.get(0));
        candidates.remove(0);

        for (int i = 1; i < ts.size(); i++) {
            // greedy get minimum neighborhood distance
            double minDistance = Double.MAX_VALUE;
            int selectedId = -1;
            for (int candidateId : candidates) {
                Double distance = distanceFunction.apply(tour.get(tour.size() - 1), ts.get(candidateId));
                if (distance < minDistance) {   // TODO improve real number compare
                    minDistance = distance;
                    selectedId = candidateId;
                }
            }
            totalDistance += minDistance;
//            if (minDistance > 0) {
            tour.add(ts.get(selectedId));
//            }
            candidates.remove(selectedId);
        }

        totalDistance += distanceFunction.apply(tour.get(tour.size() - 1), tour.get(0));
        return new DirectionSolution<>(totalDistance, tour);
    }

    @AllArgsConstructor
    @Getter
    public static class DirectionSolution<T> {
        private Double distance;
        private List<T> tour;
    }
}
