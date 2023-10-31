package org.example.util;

import java.util.ArrayList;
import java.util.List;

public class Permutations {
    public static List<List<Integer>> permute(List<Integer> nums) {
        List<List<Integer>> result = new ArrayList<>();
        generatePermutations(nums, 0, result);
        return result;
    }

    private static void generatePermutations(List<Integer> nums, int index, List<List<Integer>> result) {
        if (index == nums.size() - 1) {
            result.add(new ArrayList<>(nums));
            return;
        }

        for (int i = index; i < nums.size(); i++) {
            swap(nums, index, i);
            generatePermutations(nums, index + 1, result);
            swap(nums, index, i); // Backtrack to the original state
        }
    }

    private static void swap(List<Integer> nums, int i, int j) {
        int temp = nums.get(i);
        nums.set(i, nums.get(j));
        nums.set(j, temp);
    }
}
