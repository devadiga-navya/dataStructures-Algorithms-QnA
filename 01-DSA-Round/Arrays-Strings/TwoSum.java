import java.util.*;

/**
 * Two Sum Problem - Principal Engineer Level Solution
 * 
 * Problem: Given an array of integers nums and an integer target, 
 * return indices of the two numbers such that they add up to target.
 * 
 * Requirements:
 * - Handle edge cases (null array, empty array, no solution)
 * - Provide optimal solution with O(n) time complexity
 * - Include alternative approaches for different scenarios
 * - Clean, production-ready code
 */
public class TwoSum {
    
    /**
     * Optimal Solution using HashMap
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     * 
     * Approach: Use HashMap to store complements
     * - For each number, check if its complement exists
     * - If found, return the indices
     * - If not found, store current number with its index
     */
    public int[] twoSum(int[] nums, int target) {
        // Edge case handling
        if (nums == null || nums.length < 2) {
            return new int[]{};
        }
        
        Map<Integer, Integer> complementMap = new HashMap<>();
        
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            
            // Check if complement exists
            if (complementMap.containsKey(complement)) {
                return new int[]{complementMap.get(complement), i};
            }
            
            // Store current number and its index
            complementMap.put(nums[i], i);
        }
        
        // No solution found
        return new int[]{};
    }
    
    /**
     * Alternative: Two Pointers for Sorted Array
     * Time Complexity: O(n log n) due to sorting
     * Space Complexity: O(n) to store original indices
     * 
     * Use case: When array is sorted or can be sorted
     */
    public int[] twoSumSorted(int[] nums, int target) {
        if (nums == null || nums.length < 2) {
            return new int[]{};
        }
        
        // Create array of indices for sorting
        Integer[] indices = new Integer[nums.length];
        for (int i = 0; i < nums.length; i++) {
            indices[i] = i;
        }
        
        // Sort indices based on values
        Arrays.sort(indices, (a, b) -> Integer.compare(nums[a], nums[b]));
        
        int left = 0, right = nums.length - 1;
        
        while (left < right) {
            int sum = nums[indices[left]] + nums[indices[right]];
            
            if (sum == target) {
                return new int[]{indices[left], indices[right]};
            } else if (sum < target) {
                left++;
            } else {
                right--;
            }
        }
        
        return new int[]{};
    }
    
    /**
     * Brute Force Solution (for comparison)
     * Time Complexity: O(n²)
     * Space Complexity: O(1)
     * 
     * Use case: Small arrays or when memory is extremely limited
     */
    public int[] twoSumBruteForce(int[] nums, int target) {
        if (nums == null || nums.length < 2) {
            return new int[]{};
        }
        
        for (int i = 0; i < nums.length - 1; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] + nums[j] == target) {
                    return new int[]{i, j};
                }
            }
        }
        
        return new int[]{};
    }
    
    /**
     * Extension: Find all pairs that sum to target
     * Returns list of all valid pairs
     */
    public List<int[]> findAllPairs(int[] nums, int target) {
        List<int[]> result = new ArrayList<>();
        if (nums == null || nums.length < 2) {
            return result;
        }
        
        Map<Integer, List<Integer>> valueToIndices = new HashMap<>();
        
        // Build map of value to all its indices
        for (int i = 0; i < nums.length; i++) {
            valueToIndices.computeIfAbsent(nums[i], k -> new ArrayList<>()).add(i);
        }
        
        // Find all pairs
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            
            if (valueToIndices.containsKey(complement)) {
                for (int j : valueToIndices.get(complement)) {
                    if (i < j) { // Avoid duplicates and same index
                        result.add(new int[]{i, j});
                    }
                }
            }
        }
        
        return result;
    }
    
    /**
     * Extension: Three Sum using Two Sum
     * Time Complexity: O(n²)
     * Space Complexity: O(n)
     */
    public List<List<Integer>> threeSum(int[] nums, int target) {
        List<List<Integer>> result = new ArrayList<>();
        if (nums == null || nums.length < 3) {
            return result;
        }
        
        Arrays.sort(nums);
        
        for (int i = 0; i < nums.length - 2; i++) {
            // Skip duplicates
            if (i > 0 && nums[i] == nums[i - 1]) continue;
            
            int left = i + 1, right = nums.length - 1;
            int targetSum = target - nums[i];
            
            while (left < right) {
                int sum = nums[left] + nums[right];
                
                if (sum == targetSum) {
                    result.add(Arrays.asList(nums[i], nums[left], nums[right]));
                    
                    // Skip duplicates
                    while (left < right && nums[left] == nums[left + 1]) left++;
                    while (left < right && nums[right] == nums[right - 1]) right--;
                    
                    left++;
                    right--;
                } else if (sum < targetSum) {
                    left++;
                } else {
                    right--;
                }
            }
        }
        
        return result;
    }
    
    // Test cases and main method
    public static void main(String[] args) {
        TwoSum solution = new TwoSum();
        
        // Test cases
        int[] nums1 = {2, 7, 11, 15};
        int target1 = 9;
        System.out.println("Test 1: " + Arrays.toString(solution.twoSum(nums1, target1)));
        
        int[] nums2 = {3, 2, 4};
        int target2 = 6;
        System.out.println("Test 2: " + Arrays.toString(solution.twoSum(nums2, target2)));
        
        int[] nums3 = {3, 3};
        int target3 = 6;
        System.out.println("Test 3: " + Arrays.toString(solution.twoSum(nums3, target3)));
        
        // Edge cases
        int[] nums4 = {1};
        int target4 = 2;
        System.out.println("Edge case (single element): " + Arrays.toString(solution.twoSum(nums4, target4)));
        
        int[] nums5 = {};
        int target5 = 0;
        System.out.println("Edge case (empty array): " + Arrays.toString(solution.twoSum(nums5, target5)));
        
        // Performance test
        int[] largeArray = new int[10000];
        for (int i = 0; i < largeArray.length; i++) {
            largeArray[i] = i;
        }
        
        long startTime = System.currentTimeMillis();
        solution.twoSum(largeArray, 19998);
        long endTime = System.currentTimeMillis();
        System.out.println("Performance test (10k elements): " + (endTime - startTime) + "ms");
    }
}
