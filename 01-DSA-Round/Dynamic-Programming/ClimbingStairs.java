import java.util.*;

/**
 * Climbing Stairs
 * 
 * Problem: You are climbing a staircase. It takes n steps to reach the top.
 * Each time you can either climb 1 or 2 steps. In how many distinct ways 
 * can you climb to the top?
 * 
 * Example:
 * Input: n = 3
 * Output: 3
 * Explanation: There are three ways to climb to the top.
 * 1. 1 step + 1 step + 1 step
 * 2. 1 step + 2 steps
 * 3. 2 steps + 1 step
 * 
 * Requirements:
 * - Handle edge cases (n <= 0, n = 1, n = 2)
 * - Provide optimal solution with O(n) time complexity
 * - Include space-optimized version
 * - Clean, production-ready code
 */
public class ClimbingStairs {
    
    /**
     * Dynamic Programming Solution with Array
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     * 
     * Approach: Bottom-up DP
     * - dp[i] = number of ways to reach step i
     * - dp[i] = dp[i-1] + dp[i-2] (can reach from i-1 or i-2)
     * - Base cases: dp[0] = 1, dp[1] = 1
     */
    public int climbStairs(int n) {
        if (n <= 0) return 0;
        if (n == 1) return 1;
        if (n == 2) return 2;
        
        int[] dp = new int[n + 1];
        dp[0] = 1; // Base case: one way to stay at ground
        dp[1] = 1; // Base case: one way to reach step 1
        
        for (int i = 2; i <= n; i++) {
            dp[i] = dp[i - 1] + dp[i - 2];
        }
        
        return dp[n];
    }
    
    /**
     * Space-Optimized Solution
     * Time Complexity: O(n)
     * Space Complexity: O(1)
     * 
     * Approach: Use only two variables to track previous states
     * - Only need dp[i-1] and dp[i-2] to calculate dp[i]
     * - Update variables in each iteration
     */
    public int climbStairsOptimized(int n) {
        if (n <= 0) return 0;
        if (n == 1) return 1;
        if (n == 2) return 2;
        
        int prev2 = 1; // dp[i-2]
        int prev1 = 1; // dp[i-1]
        int current = 0;
        
        for (int i = 2; i <= n; i++) {
            current = prev1 + prev2;
            prev2 = prev1;
            prev1 = current;
        }
        
        return current;
    }
    
    /**
     * Recursive Solution with Memoization
     * Time Complexity: O(n)
     * Space Complexity: O(n) for recursion stack + memo
     * 
     * Approach: Top-down DP with memoization
     * - Use HashMap to cache results
     * - Avoid recalculating same subproblems
     */
    public int climbStairsMemo(int n) {
        if (n <= 0) return 0;
        if (n == 1) return 1;
        if (n == 2) return 2;
        
        Map<Integer, Integer> memo = new HashMap<>();
        return climbStairsHelper(n, memo);
    }
    
    private int climbStairsHelper(int n, Map<Integer, Integer> memo) {
        // Base cases
        if (n <= 0) return 0;
        if (n == 1) return 1;
        if (n == 2) return 2;
        
        // Check memo
        if (memo.containsKey(n)) {
            return memo.get(n);
        }
        
        // Calculate and memoize
        int result = climbStairsHelper(n - 1, memo) + climbStairsHelper(n - 2, memo);
        memo.put(n, result);
        
        return result;
    }
    
    /**
     * Matrix Exponentiation Solution
     * Time Complexity: O(log n)
     * Space Complexity: O(1)
     * 
     * Approach: Use matrix exponentiation for faster computation
     * - Based on Fibonacci sequence properties
     * - Useful for very large n values
     */
    public int climbStairsMatrix(int n) {
        if (n <= 0) return 0;
        if (n == 1) return 1;
        if (n == 2) return 2;
        
        // Matrix for Fibonacci: [[1, 1], [1, 0]]
        long[][] matrix = {{1, 1}, {1, 0}};
        long[][] result = matrixPower(matrix, n);
        
        return (int) result[0][0];
    }
    
    private long[][] matrixPower(long[][] matrix, int n) {
        if (n == 0) {
            return new long[][]{{1, 0}, {0, 1}}; // Identity matrix
        }
        if (n == 1) {
            return matrix;
        }
        
        long[][] half = matrixPower(matrix, n / 2);
        long[][] squared = matrixMultiply(half, half);
        
        if (n % 2 == 0) {
            return squared;
        } else {
            return matrixMultiply(squared, matrix);
        }
    }
    
    private long[][] matrixMultiply(long[][] a, long[][] b) {
        return new long[][]{
            {a[0][0] * b[0][0] + a[0][1] * b[1][0], a[0][0] * b[0][1] + a[0][1] * b[1][1]},
            {a[1][0] * b[0][0] + a[1][1] * b[1][0], a[1][0] * b[0][1] + a[1][1] * b[1][1]}
        };
    }
    
    /**
     * Extension: Climbing stairs with variable step sizes
     * Given an array of allowed step sizes, find number of ways
     */
    public int climbStairsVariable(int n, int[] steps) {
        if (n <= 0 || steps == null || steps.length == 0) {
            return 0;
        }
        
        int[] dp = new int[n + 1];
        dp[0] = 1; // Base case
        
        for (int i = 1; i <= n; i++) {
            for (int step : steps) {
                if (i >= step) {
                    dp[i] += dp[i - step];
                }
            }
        }
        
        return dp[n];
    }
    
    /**
     * Extension: Find all possible climbing sequences
     * Returns list of all possible step sequences
     */
    public List<List<Integer>> getAllClimbingSequences(int n) {
        List<List<Integer>> result = new ArrayList<>();
        if (n <= 0) {
            return result;
        }
        
        getAllSequencesHelper(n, new ArrayList<>(), result);
        return result;
    }
    
    private void getAllSequencesHelper(int remaining, List<Integer> current, List<List<Integer>> result) {
        if (remaining == 0) {
            result.add(new ArrayList<>(current));
            return;
        }
        if (remaining < 0) {
            return;
        }
        
        // Try 1 step
        current.add(1);
        getAllSequencesHelper(remaining - 1, current, result);
        current.remove(current.size() - 1);
        
        // Try 2 steps
        current.add(2);
        getAllSequencesHelper(remaining - 2, current, result);
        current.remove(current.size() - 1);
    }
    
    /**
     * Extension: Climbing stairs with cost
     * Each step has a cost, find minimum cost to reach top
     */
    public int minCostClimbingStairs(int[] cost) {
        if (cost == null || cost.length == 0) {
            return 0;
        }
        if (cost.length == 1) {
            return cost[0];
        }
        
        int n = cost.length;
        int[] dp = new int[n + 1];
        
        // Can start from step 0 or step 1
        dp[0] = 0;
        dp[1] = 0;
        
        for (int i = 2; i <= n; i++) {
            dp[i] = Math.min(dp[i - 1] + cost[i - 1], dp[i - 2] + cost[i - 2]);
        }
        
        return dp[n];
    }
    
    // Test cases and main method
    public static void main(String[] args) {
        ClimbingStairs solution = new ClimbingStairs();
        
        // Test cases
        int[] testCases = {0, 1, 2, 3, 4, 5, 10, 20};
        
        for (int n : testCases) {
            int result = solution.climbStairs(n);
            System.out.println("n = " + n + " -> Ways: " + result);
        }
        
        // Performance comparison
        int largeN = 45;
        
        long startTime = System.currentTimeMillis();
        int result1 = solution.climbStairs(largeN);
        long time1 = System.currentTimeMillis() - startTime;
        
        startTime = System.currentTimeMillis();
        int result2 = solution.climbStairsOptimized(largeN);
        long time2 = System.currentTimeMillis() - startTime;
        
        startTime = System.currentTimeMillis();
        int result3 = solution.climbStairsMemo(largeN);
        long time3 = System.currentTimeMillis() - startTime;
        
        System.out.println("\nPerformance Comparison (n = " + largeN + "):");
        System.out.println("DP Array: " + result1 + " ways in " + time1 + "ms");
        System.out.println("Optimized: " + result2 + " ways in " + time2 + "ms");
        System.out.println("Memoization: " + result3 + " ways in " + time3 + "ms");
        
        // Extensions
        System.out.println("\nExtensions:");
        
        // Variable steps
        int[] steps = {1, 2, 3};
        int variableResult = solution.climbStairsVariable(4, steps);
        System.out.println("Variable steps [1,2,3] for n=4: " + variableResult + " ways");
        
        // All sequences
        List<List<Integer>> sequences = solution.getAllClimbingSequences(3);
        System.out.println("All sequences for n=3: " + sequences);
        
        // Min cost
        int[] cost = {10, 15, 20};
        int minCost = solution.minCostClimbingStairs(cost);
        System.out.println("Min cost climbing: " + minCost);
    }
}
