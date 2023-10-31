package org.example.util;

import org.example.enums.Position;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CombinationHelper {

    public static void getCombinations(List<Integer> arr, int n, int r, List<List<Integer>> store) {
        List<Integer> data = new ArrayList<>();
        for (int i = 0; i < r; i++) {
            data.add(i, 0);
        }
        combinationUntil(arr, data, 0, n - 1, 0, r, store);
    }

    private static void combinationUntil(List<Integer> arr, List<Integer> data, int start, int end, int index, int r, List<List<Integer>> store) {
        if (index == r) {
            List<Integer> item = new ArrayList<>();
            for (int j = 0; j < r; j++) {
                item.add(data.get(j));
            }
            store.add(item);
            return;
        }
        for (int i = start; i <= end && end -i + 1 >= r - index; i++) {
            data.set(index, arr.get(i));
            combinationUntil(arr, data, i + 1, end, index + 1, r, store);
        }
    }

    public static Map<Position, Integer> getFrequency(List<Position> positions) {
        Map<Position, Integer> frequencies = new HashMap<>();
        positions.forEach(position -> {
            frequencies.merge(position, 1, Integer::sum);
        });
        return frequencies;
    }

    public static long combination(int m, int n) {
        if (m == n) {
            return 1;
        }
        if (m < n) {
            return 0;
        }
        return factorial(m) / (factorial(m - n) * factorial(n));
    }

    private static long factorial(int n) {
        if (n <= 2) {
            return n;
        }
        return n * factorial(n - 1);
    }

    public static double calculateETA(double percentage, long startTime) {
        double percentageLeft = 100 - percentage;
        long elapsedTime = System.nanoTime() - startTime;
        return ((percentageLeft * elapsedTime) / percentage) / 1000000000;
    }
}
