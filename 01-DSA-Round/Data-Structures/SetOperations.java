import java.util.*;

/**
 * Set Operations Problem - Principal Engineer Level Solution
 * 
 * Problem: Implement various set operations and algorithms using HashSet, TreeSet,
 * and LinkedHashSet with different use cases and optimizations.
 * 
 * Example:
 * Input: Set A = {1, 2, 3, 4, 5}, Set B = {4, 5, 6, 7, 8}
 * Union: {1, 2, 3, 4, 5, 6, 7, 8}
 * Intersection: {4, 5}
 * Difference: A - B = {1, 2, 3}, B - A = {6, 7, 8}
 * 
 * Requirements:
 * - Handle edge cases (empty sets, null sets, large datasets)
 * - Provide optimal solutions with different time complexities
 * - Include alternative approaches for different scenarios
 * - Clean, production-ready code
 */
public class SetOperations {
    
    /**
     * Union of Two Sets
     * Time Complexity: O(n + m) where n and m are sizes of sets
     * Space Complexity: O(n + m)
     */
    public Set<Integer> union(Set<Integer> setA, Set<Integer> setB) {
        if (setA == null && setB == null) {
            return new HashSet<>();
        }
        if (setA == null) {
            return new HashSet<>(setB);
        }
        if (setB == null) {
            return new HashSet<>(setA);
        }
        
        Set<Integer> result = new HashSet<>(setA);
        result.addAll(setB);
        return result;
    }
    
    /**
     * Intersection of Two Sets
     * Time Complexity: O(min(n, m))
     * Space Complexity: O(min(n, m))
     */
    public Set<Integer> intersection(Set<Integer> setA, Set<Integer> setB) {
        if (setA == null || setB == null) {
            return new HashSet<>();
        }
        
        Set<Integer> result = new HashSet<>();
        Set<Integer> smallerSet = setA.size() <= setB.size() ? setA : setB;
        Set<Integer> largerSet = setA.size() <= setB.size() ? setB : setA;
        
        for (Integer element : smallerSet) {
            if (largerSet.contains(element)) {
                result.add(element);
            }
        }
        
        return result;
    }
    
    /**
     * Difference of Two Sets (A - B)
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     */
    public Set<Integer> difference(Set<Integer> setA, Set<Integer> setB) {
        if (setA == null) {
            return new HashSet<>();
        }
        if (setB == null) {
            return new HashSet<>(setA);
        }
        
        Set<Integer> result = new HashSet<>(setA);
        result.removeAll(setB);
        return result;
    }
    
    /**
     * Symmetric Difference (A ⊕ B = (A - B) ∪ (B - A))
     * Time Complexity: O(n + m)
     * Space Complexity: O(n + m)
     */
    public Set<Integer> symmetricDifference(Set<Integer> setA, Set<Integer> setB) {
        Set<Integer> diffA = difference(setA, setB);
        Set<Integer> diffB = difference(setB, setA);
        return union(diffA, diffB);
    }
    
    /**
     * Check if Two Sets are Equal
     * Time Complexity: O(n)
     * Space Complexity: O(1)
     */
    public boolean areEqual(Set<Integer> setA, Set<Integer> setB) {
        if (setA == null && setB == null) {
            return true;
        }
        if (setA == null || setB == null) {
            return false;
        }
        
        if (setA.size() != setB.size()) {
            return false;
        }
        
        return setA.containsAll(setB);
    }
    
    /**
     * Check if Set A is Subset of Set B
     * Time Complexity: O(n)
     * Space Complexity: O(1)
     */
    public boolean isSubset(Set<Integer> setA, Set<Integer> setB) {
        if (setA == null) {
            return true;
        }
        if (setB == null) {
            return false;
        }
        
        return setB.containsAll(setA);
    }
    
    /**
     * Check if Set A is Superset of Set B
     * Time Complexity: O(m)
     * Space Complexity: O(1)
     */
    public boolean isSuperset(Set<Integer> setA, Set<Integer> setB) {
        if (setB == null) {
            return true;
        }
        if (setA == null) {
            return false;
        }
        
        return setA.containsAll(setB);
    }
    
    /**
     * Find Duplicate Elements in Array using Set
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     */
    public Set<Integer> findDuplicates(int[] arr) {
        Set<Integer> duplicates = new HashSet<>();
        Set<Integer> seen = new HashSet<>();
        
        for (int num : arr) {
            if (!seen.add(num)) {
                duplicates.add(num);
            }
        }
        
        return duplicates;
    }
    
    /**
     * Find Missing Elements in Range using Set
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     */
    public Set<Integer> findMissingElements(int[] arr, int start, int end) {
        Set<Integer> present = new HashSet<>();
        for (int num : arr) {
            present.add(num);
        }
        
        Set<Integer> missing = new HashSet<>();
        for (int i = start; i <= end; i++) {
            if (!present.contains(i)) {
                missing.add(i);
            }
        }
        
        return missing;
    }
    
    /**
     * Find First Non-Repeating Element
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     */
    public Integer findFirstNonRepeating(int[] arr) {
        Map<Integer, Integer> frequency = new HashMap<>();
        Set<Integer> nonRepeating = new LinkedHashSet<>();
        
        for (int num : arr) {
            frequency.put(num, frequency.getOrDefault(num, 0) + 1);
            
            if (frequency.get(num) == 1) {
                nonRepeating.add(num);
            } else {
                nonRepeating.remove(num);
            }
        }
        
        return nonRepeating.isEmpty() ? null : nonRepeating.iterator().next();
    }
    
    /**
     * Find All Subsets of a Set
     * Time Complexity: O(2^n)
     * Space Complexity: O(2^n)
     */
    public Set<Set<Integer>> findAllSubsets(Set<Integer> set) {
        Set<Set<Integer>> result = new HashSet<>();
        List<Integer> elements = new ArrayList<>(set);
        int n = elements.size();
        
        for (int i = 0; i < (1 << n); i++) {
            Set<Integer> subset = new HashSet<>();
            for (int j = 0; j < n; j++) {
                if ((i & (1 << j)) != 0) {
                    subset.add(elements.get(j));
                }
            }
            result.add(subset);
        }
        
        return result;
    }
    
    /**
     * Find Common Elements in Multiple Sets
     * Time Complexity: O(n * k) where k is number of sets
     * Space Complexity: O(min(n1, n2, ..., nk))
     */
    public Set<Integer> findCommonElements(List<Set<Integer>> sets) {
        if (sets == null || sets.isEmpty()) {
            return new HashSet<>();
        }
        
        Set<Integer> result = new HashSet<>(sets.get(0));
        
        for (int i = 1; i < sets.size(); i++) {
            result.retainAll(sets.get(i));
        }
        
        return result;
    }
    
    /**
     * Remove Duplicates from Array using Set
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     */
    public int[] removeDuplicates(int[] arr) {
        if (arr == null || arr.length == 0) {
            return arr;
        }
        
        Set<Integer> set = new LinkedHashSet<>();
        for (int num : arr) {
            set.add(num);
        }
        
        return set.stream().mapToInt(Integer::intValue).toArray();
    }
    
    /**
     * Check if Array Contains Duplicates
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     */
    public boolean containsDuplicates(int[] arr) {
        if (arr == null || arr.length <= 1) {
            return false;
        }
        
        Set<Integer> set = new HashSet<>();
        for (int num : arr) {
            if (!set.add(num)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Find Longest Consecutive Sequence
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     */
    public int longestConsecutiveSequence(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        
        Set<Integer> set = new HashSet<>();
        for (int num : arr) {
            set.add(num);
        }
        
        int maxLength = 0;
        
        for (int num : set) {
            if (!set.contains(num - 1)) {
                int currentNum = num;
                int currentLength = 1;
                
                while (set.contains(currentNum + 1)) {
                    currentNum++;
                    currentLength++;
                }
                
                maxLength = Math.max(maxLength, currentLength);
            }
        }
        
        return maxLength;
    }
    
    /**
     * Find All Pairs with Given Sum
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     */
    public Set<int[]> findPairsWithSum(int[] arr, int target) {
        Set<int[]> result = new HashSet<>();
        Set<Integer> seen = new HashSet<>();
        
        for (int num : arr) {
            int complement = target - num;
            if (seen.contains(complement)) {
                result.add(new int[]{Math.min(num, complement), Math.max(num, complement)});
            }
            seen.add(num);
        }
        
        return result;
    }
    
    // Test cases and main method
    public static void main(String[] args) {
        SetOperations solution = new SetOperations();
        
        // Test sets
        Set<Integer> setA = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5));
        Set<Integer> setB = new HashSet<>(Arrays.asList(4, 5, 6, 7, 8));
        
        System.out.println("Set A: " + setA);
        System.out.println("Set B: " + setB);
        System.out.println();
        
        // Test basic operations
        System.out.println("Union: " + solution.union(setA, setB));
        System.out.println("Intersection: " + solution.intersection(setA, setB));
        System.out.println("A - B: " + solution.difference(setA, setB));
        System.out.println("B - A: " + solution.difference(setB, setA));
        System.out.println("Symmetric Difference: " + solution.symmetricDifference(setA, setB));
        System.out.println("Are Equal: " + solution.areEqual(setA, setB));
        System.out.println("Is A subset of B: " + solution.isSubset(setA, setB));
        System.out.println("Is A superset of B: " + solution.isSuperset(setA, setB));
        
        // Test array operations
        int[] arr = {1, 2, 3, 4, 2, 5, 6, 3, 7, 8, 1};
        System.out.println("\nArray: " + Arrays.toString(arr));
        System.out.println("Duplicates: " + solution.findDuplicates(arr));
        System.out.println("First Non-Repeating: " + solution.findFirstNonRepeating(arr));
        System.out.println("Contains Duplicates: " + solution.containsDuplicates(arr));
        System.out.println("Without Duplicates: " + Arrays.toString(solution.removeDuplicates(arr)));
        
        // Test consecutive sequence
        int[] seqArr = {100, 4, 200, 1, 3, 2};
        System.out.println("\nLongest Consecutive Sequence in " + Arrays.toString(seqArr) + 
                         ": " + solution.longestConsecutiveSequence(seqArr));
        
        // Test pairs with sum
        int[] pairArr = {1, 5, 7, -1, 5};
        int target = 6;
        Set<int[]> pairs = solution.findPairsWithSum(pairArr, target);
        System.out.println("\nPairs with sum " + target + " in " + Arrays.toString(pairArr) + ":");
        for (int[] pair : pairs) {
            System.out.println("(" + pair[0] + ", " + pair[1] + ")");
        }
        
        // Test subsets
        Set<Integer> testSet = new HashSet<>(Arrays.asList(1, 2, 3));
        Set<Set<Integer>> subsets = solution.findAllSubsets(testSet);
        System.out.println("\nAll subsets of " + testSet + ":");
        for (Set<Integer> subset : subsets) {
            System.out.println(subset);
        }
        
        // Performance test
        int[] largeArr = new int[10000];
        for (int i = 0; i < largeArr.length; i++) {
            largeArr[i] = (int) (Math.random() * 1000);
        }
        
        long startTime = System.currentTimeMillis();
        solution.findDuplicates(largeArr);
        long time1 = System.currentTimeMillis() - startTime;
        
        startTime = System.currentTimeMillis();
        solution.removeDuplicates(largeArr);
        long time2 = System.currentTimeMillis() - startTime;
        
        System.out.println("\nPerformance Test:");
        System.out.println("Find duplicates: " + time1 + "ms");
        System.out.println("Remove duplicates: " + time2 + "ms");
    }
}
