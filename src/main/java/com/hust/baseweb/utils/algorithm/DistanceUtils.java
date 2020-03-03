package com.hust.baseweb.utils.algorithm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;

public class DistanceUtils {
    public static <T> double calculateGreedyTotalDistance(List<T> ts, BiFunction<T, T, Double> distanceFunction) {
        double totalDistance = 0;
        Set<T> candidates = new HashSet<>(ts);
        List<T> tour = new ArrayList<>();
        tour.add(ts.get(0));
        candidates.remove(ts.get(0));

        for (int i = 1; i < ts.size(); i++) {
            // greedy get minimum neighborhood distance
            double minDistance = Double.MAX_VALUE;
            T selectedT = null;
            for (T candidate : candidates) {
                Double distance = distanceFunction.apply(tour.get(i - 1), candidate);
                if (distance < minDistance) {   // TODO improve real number compare
                    minDistance = distance;
                    selectedT = candidate;
                }
            }
            totalDistance += minDistance;
            tour.add(selectedT);
        }

        totalDistance += distanceFunction.apply(tour.get(tour.size() - 1), tour.get(0));
        return totalDistance;
    }
}
