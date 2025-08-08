import java.util.*;

/**
 * Container With Most Water
 * 
 * Problem: Given n non-negative integers height where each represents a point 
 * at coordinate (i, height[i]), find two lines that together with the x-axis 
 * form a container that would hold the maximum amount of water.
 * 
 * Example:
 * Input: height = [1,8,6,2,5,4,8,3,7]
 * Output: 49
 * Explanation: The maximum area is obtained by choosing height[1] = 8 and 
 * height[8] = 7, giving us 8 * (8-1) = 49.
 * 
 * Requirements:
 * - Handle edge cases (null array, empty array, single element)
 * - Provide optimal solution with O(n) time complexity
 * - Include alternative approaches for different scenarios
 * - Clean, production-ready code
 */
public class ContainerWithMostWater {
    
    /**
     * Optimal Solution using Two Pointers
     * Time Complexity: O(n)
     * Space Complexity: O(1)
     * 
     * Approach: Use two pointers from both ends
     * - Start with left = 0, right = n-1
     * - Calculate area with current heights
     * - Move pointer with smaller height inward
     * - Track maximum area found
     */
    public int maxArea(int[] height) {
        if (height == null || height.length < 2) {
            return 0;
        }
        
        int maxArea = 0;
        int left = 0;
        int right = height.length - 1;
        
        while (left < right) {
            // Calculate current area
            int width = right - left;
            int minHeight = Math.min(height[left], height[right]);
            int currentArea = width * minHeight;
            
            // Update maximum area
            maxArea = Math.max(maxArea, currentArea);
            
            // Move pointer with smaller height
            if (height[left] < height[right]) {
                left++;
            } else {
                right--;
            }
        }
        
        return maxArea;
    }
    
    /**
     * Alternative: Brute Force Approach
     * Time Complexity: O(n²)
     * Space Complexity: O(1)
     * 
     * Use case: For understanding the problem or small arrays
     */
    public int maxAreaBruteForce(int[] height) {
        if (height == null || height.length < 2) {
            return 0;
        }
        
        int maxArea = 0;
        
        for (int i = 0; i < height.length - 1; i++) {
            for (int j = i + 1; j < height.length; j++) {
                int width = j - i;
                int minHeight = Math.min(height[i], height[j]);
                int currentArea = width * minHeight;
                maxArea = Math.max(maxArea, currentArea);
            }
        }
        
        return maxArea;
    }
    
    /**
     * Extension: Find all pairs that give maximum area
     * Returns list of all pairs (i, j) that give maximum area
     */
    public List<int[]> getAllMaxAreaPairs(int[] height) {
        List<int[]> result = new ArrayList<>();
        if (height == null || height.length < 2) {
            return result;
        }
        
        int maxArea = maxArea(height);
        
        for (int i = 0; i < height.length - 1; i++) {
            for (int j = i + 1; j < height.length; j++) {
                int width = j - i;
                int minHeight = Math.min(height[i], height[j]);
                int currentArea = width * minHeight;
                
                if (currentArea == maxArea) {
                    result.add(new int[]{i, j});
                }
            }
        }
        
        return result;
    }
    
    /**
     * Extension: Container with most water with obstacles
     * Consider that some heights might be obstacles (0 height)
     */
    public int maxAreaWithObstacles(int[] height) {
        if (height == null || height.length < 2) {
            return 0;
        }
        
        int maxArea = 0;
        int left = 0;
        int right = height.length - 1;
        
        while (left < right) {
            // Skip obstacles (height = 0)
            while (left < right && height[left] == 0) {
                left++;
            }
            while (left < right && height[right] == 0) {
                right--;
            }
            
            if (left >= right) {
                break;
            }
            
            int width = right - left;
            int minHeight = Math.min(height[left], height[right]);
            int currentArea = width * minHeight;
            
            maxArea = Math.max(maxArea, currentArea);
            
            if (height[left] < height[right]) {
                left++;
            } else {
                right--;
            }
        }
        
        return maxArea;
    }
    
    /**
     * Extension: Container with most water with cost
     * Each container has a cost, find maximum area with minimum cost
     */
    public int maxAreaWithCost(int[] height, int[] cost) {
        if (height == null || cost == null || height.length < 2 || 
            height.length != cost.length) {
            return 0;
        }
        
        int maxArea = 0;
        int minCost = Integer.MAX_VALUE;
        
        for (int i = 0; i < height.length - 1; i++) {
            for (int j = i + 1; j < height.length; j++) {
                int width = j - i;
                int minHeight = Math.min(height[i], height[j]);
                int currentArea = width * minHeight;
                int currentCost = cost[i] + cost[j];
                
                if (currentArea > maxArea || 
                    (currentArea == maxArea && currentCost < minCost)) {
                    maxArea = currentArea;
                    minCost = currentCost;
                }
            }
        }
        
        return maxArea;
    }
    
    /**
     * Extension: Container with most water in 3D
     * Given a 2D grid of heights, find maximum volume
     */
    public int maxVolume3D(int[][] height) {
        if (height == null || height.length == 0 || height[0].length == 0) {
            return 0;
        }
        
        int rows = height.length;
        int cols = height[0].length;
        int maxVolume = 0;
        
        // For each pair of columns, find maximum volume
        for (int i = 0; i < cols - 1; i++) {
            for (int j = i + 1; j < cols; j++) {
                int width = j - i;
                
                // Find minimum height for each row
                for (int row = 0; row < rows; row++) {
                    int minHeight = Math.min(height[row][i], height[row][j]);
                    int volume = width * minHeight;
                    maxVolume = Math.max(maxVolume, volume);
                }
            }
        }
        
        return maxVolume;
    }
    
    /**
     * Extension: Container with most water with time constraints
     * Heights change over time, find maximum area at any time
     */
    public int maxAreaOverTime(int[][] heights) {
        if (heights == null || heights.length == 0 || heights[0].length < 2) {
            return 0;
        }
        
        int maxArea = 0;
        
        for (int time = 0; time < heights.length; time++) {
            int currentMaxArea = maxArea(heights[time]);
            maxArea = Math.max(maxArea, currentMaxArea);
        }
        
        return maxArea;
    }
    
    // Test cases and main method
    public static void main(String[] args) {
        ContainerWithMostWater solution = new ContainerWithMostWater();
        
        // Test cases
        int[][] testCases = {
            {1, 8, 6, 2, 5, 4, 8, 3, 7},  // Expected: 49
            {1, 1},                         // Expected: 1
            {4, 3, 2, 1, 4},               // Expected: 16
            {1, 2, 1},                     // Expected: 2
            {1, 8, 6, 2, 5, 4, 8, 3, 7},  // Expected: 49
            {1, 1, 1, 1, 1},              // Expected: 4
            {1, 2, 3, 4, 5},              // Expected: 6
            {5, 4, 3, 2, 1}               // Expected: 6
        };
        
        for (int i = 0; i < testCases.length; i++) {
            int result = solution.maxArea(testCases[i]);
            System.out.println("Test " + (i + 1) + ": " + result);
        }
        
        // Performance comparison
        int[] largeArray = new int[10000];
        for (int i = 0; i < largeArray.length; i++) {
            largeArray[i] = (int) (Math.random() * 1000);
        }
        
        long startTime = System.currentTimeMillis();
        int result1 = solution.maxArea(largeArray);
        long time1 = System.currentTimeMillis() - startTime;
        
        startTime = System.currentTimeMillis();
        int result2 = solution.maxAreaBruteForce(largeArray);
        long time2 = System.currentTimeMillis() - startTime;
        
        System.out.println("\nPerformance Comparison:");
        System.out.println("Two Pointers: " + result1 + " in " + time1 + "ms");
        System.out.println("Brute Force: " + result2 + " in " + time2 + "ms");
        
        // Extension tests
        System.out.println("\nExtension Tests:");
        
        // All max area pairs
        List<int[]> maxPairs = solution.getAllMaxAreaPairs(new int[]{1, 8, 6, 2, 5, 4, 8, 3, 7});
        System.out.println("All max area pairs: " + maxPairs.size() + " pairs found");
        
        // With obstacles
        int obstacleResult = solution.maxAreaWithObstacles(new int[]{1, 0, 8, 0, 6, 2, 5, 4, 8, 3, 7});
        System.out.println("Max area with obstacles: " + obstacleResult);
        
        // With cost
        int[] heights = {1, 8, 6, 2, 5, 4, 8, 3, 7};
        int[] costs = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        int costResult = solution.maxAreaWithCost(heights, costs);
        System.out.println("Max area with cost: " + costResult);
        
        // 3D volume
        int[][] height3D = {
            {1, 4, 2, 3},
            {2, 3, 1, 4},
            {3, 2, 4, 1}
        };
        int volume3D = solution.maxVolume3D(height3D);
        System.out.println("Max 3D volume: " + volume3D);
    }
}
