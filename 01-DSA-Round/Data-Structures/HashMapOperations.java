import java.util.*;

/**
 * HashMap Operations Problem - Principal Engineer Level Solution
 * 
 * Problem: Implement various HashMap operations and algorithms including
 * frequency counting, grouping, caching, and advanced data structures.
 * 
 * Example:
 * Input: ["apple", "banana", "apple", "cherry", "banana", "apple"]
 * Frequency Map: {apple=3, banana=2, cherry=1}
 * Most Frequent: apple
 * 
 * Requirements:
 * - Handle edge cases (null inputs, empty maps, large datasets)
 * - Provide optimal solutions with different time complexities
 * - Include alternative approaches for different scenarios
 * - Clean, production-ready code
 */
public class HashMapOperations {
    
    /**
     * Count Frequency of Elements
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     */
    public Map<String, Integer> countFrequency(String[] arr) {
        Map<String, Integer> frequency = new HashMap<>();
        
        if (arr == null || arr.length == 0) {
            return frequency;
        }
        
        for (String item : arr) {
            frequency.put(item, frequency.getOrDefault(item, 0) + 1);
        }
        
        return frequency;
    }
    
    /**
     * Find Most Frequent Element
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     */
    public String findMostFrequent(String[] arr) {
        if (arr == null || arr.length == 0) {
            return null;
        }
        
        Map<String, Integer> frequency = countFrequency(arr);
        String mostFrequent = null;
        int maxCount = 0;
        
        for (Map.Entry<String, Integer> entry : frequency.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mostFrequent = entry.getKey();
            }
        }
        
        return mostFrequent;
    }
    
    /**
     * Find Top K Frequent Elements
     * Time Complexity: O(n + k log n)
     * Space Complexity: O(n)
     */
    public List<String> findTopKFrequent(String[] arr, int k) {
        if (arr == null || arr.length == 0 || k <= 0) {
            return new ArrayList<>();
        }
        
        Map<String, Integer> frequency = countFrequency(arr);
        PriorityQueue<Map.Entry<String, Integer>> pq = 
            new PriorityQueue<>((a, b) -> Integer.compare(a.getValue(), b.getValue()));
        
        for (Map.Entry<String, Integer> entry : frequency.entrySet()) {
            pq.offer(entry);
            if (pq.size() > k) {
                pq.poll();
            }
        }
        
        List<String> result = new ArrayList<>();
        while (!pq.isEmpty()) {
            result.add(0, pq.poll().getKey());
        }
        
        return result;
    }
    
    /**
     * Group Elements by Category
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     */
    public Map<String, List<String>> groupByCategory(String[] items, String[] categories) {
        Map<String, List<String>> groups = new HashMap<>();
        
        if (items == null || categories == null || items.length != categories.length) {
            return groups;
        }
        
        for (int i = 0; i < items.length; i++) {
            String category = categories[i];
            groups.computeIfAbsent(category, k -> new ArrayList<>()).add(items[i]);
        }
        
        return groups;
    }
    
    /**
     * Find First Non-Repeating Character
     * Time Complexity: O(n)
     * Space Complexity: O(1) - fixed size for ASCII
     */
    public Character findFirstNonRepeatingChar(String s) {
        if (s == null || s.length() == 0) {
            return null;
        }
        
        Map<Character, Integer> frequency = new HashMap<>();
        
        // Count frequency
        for (char c : s.toCharArray()) {
            frequency.put(c, frequency.getOrDefault(c, 0) + 1);
        }
        
        // Find first non-repeating
        for (char c : s.toCharArray()) {
            if (frequency.get(c) == 1) {
                return c;
            }
        }
        
        return null;
    }
    
    /**
     * Check if Two Strings are Anagrams
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     */
    public boolean areAnagrams(String s1, String s2) {
        if (s1 == null && s2 == null) {
            return true;
        }
        if (s1 == null || s2 == null) {
            return false;
        }
        if (s1.length() != s2.length()) {
            return false;
        }
        
        Map<Character, Integer> charCount = new HashMap<>();
        
        // Count characters in first string
        for (char c : s1.toCharArray()) {
            charCount.put(c, charCount.getOrDefault(c, 0) + 1);
        }
        
        // Decrement for second string
        for (char c : s2.toCharArray()) {
            int count = charCount.getOrDefault(c, 0);
            if (count == 0) {
                return false;
            }
            charCount.put(c, count - 1);
        }
        
        return true;
    }
    
    /**
     * Find All Anagrams in String
     * Time Complexity: O(n * m) where n is length of s, m is length of p
     * Space Complexity: O(m)
     */
    public List<Integer> findAllAnagrams(String s, String p) {
        List<Integer> result = new ArrayList<>();
        
        if (s == null || p == null || s.length() < p.length()) {
            return result;
        }
        
        Map<Character, Integer> pCount = new HashMap<>();
        Map<Character, Integer> sCount = new HashMap<>();
        
        // Count characters in pattern
        for (char c : p.toCharArray()) {
            pCount.put(c, pCount.getOrDefault(c, 0) + 1);
        }
        
        int pLen = p.length();
        
        // Sliding window
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            sCount.put(c, sCount.getOrDefault(c, 0) + 1);
            
            if (i >= pLen) {
                char oldChar = s.charAt(i - pLen);
                sCount.put(oldChar, sCount.get(oldChar) - 1);
                if (sCount.get(oldChar) == 0) {
                    sCount.remove(oldChar);
                }
            }
            
            if (sCount.equals(pCount)) {
                result.add(i - pLen + 1);
            }
        }
        
        return result;
    }
    
    /**
     * LRU Cache Implementation
     * Time Complexity: O(1) for get and put
     * Space Complexity: O(capacity)
     */
    public static class LRUCache<K, V> {
        private final int capacity;
        private final Map<K, Node<K, V>> cache;
        private final Node<K, V> head;
        private final Node<K, V> tail;
        
        public LRUCache(int capacity) {
            this.capacity = capacity;
            this.cache = new HashMap<>();
            this.head = new Node<>();
            this.tail = new Node<>();
            head.next = tail;
            tail.prev = head;
        }
        
        public V get(K key) {
            Node<K, V> node = cache.get(key);
            if (node == null) {
                return null;
            }
            
            moveToHead(node);
            return node.value;
        }
        
        public void put(K key, V value) {
            Node<K, V> node = cache.get(key);
            
            if (node == null) {
                node = new Node<>(key, value);
                cache.put(key, node);
                addNode(node);
                
                if (cache.size() > capacity) {
                    Node<K, V> lru = removeTail();
                    cache.remove(lru.key);
                }
            } else {
                node.value = value;
                moveToHead(node);
            }
        }
        
        private void addNode(Node<K, V> node) {
            node.prev = head;
            node.next = head.next;
            head.next.prev = node;
            head.next = node;
        }
        
        private void removeNode(Node<K, V> node) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
        
        private void moveToHead(Node<K, V> node) {
            removeNode(node);
            addNode(node);
        }
        
        private Node<K, V> removeTail() {
            Node<K, V> lru = tail.prev;
            removeNode(lru);
            return lru;
        }
        
        private static class Node<K, V> {
            K key;
            V value;
            Node<K, V> prev;
            Node<K, V> next;
            
            Node() {}
            Node(K key, V value) {
                this.key = key;
                this.value = value;
            }
        }
    }
    
    /**
     * Word Pattern Matching
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     */
    public boolean wordPattern(String pattern, String s) {
        if (pattern == null || s == null) {
            return false;
        }
        
        String[] words = s.split(" ");
        if (pattern.length() != words.length) {
            return false;
        }
        
        Map<Character, String> charToWord = new HashMap<>();
        Map<String, Character> wordToChar = new HashMap<>();
        
        for (int i = 0; i < pattern.length(); i++) {
            char c = pattern.charAt(i);
            String word = words[i];
            
            if (charToWord.containsKey(c)) {
                if (!charToWord.get(c).equals(word)) {
                    return false;
                }
            } else {
                if (wordToChar.containsKey(word)) {
                    return false;
                }
                charToWord.put(c, word);
                wordToChar.put(word, c);
            }
        }
        
        return true;
    }
    
    /**
     * Find Subarray with Given Sum
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     */
    public int[] findSubarrayWithSum(int[] arr, int target) {
        if (arr == null || arr.length == 0) {
            return new int[]{-1, -1};
        }
        
        Map<Integer, Integer> sumIndex = new HashMap<>();
        sumIndex.put(0, -1);
        int currentSum = 0;
        
        for (int i = 0; i < arr.length; i++) {
            currentSum += arr[i];
            
            if (sumIndex.containsKey(currentSum - target)) {
                return new int[]{sumIndex.get(currentSum - target) + 1, i};
            }
            
            sumIndex.put(currentSum, i);
        }
        
        return new int[]{-1, -1};
    }
    
    /**
     * Find Longest Substring with K Distinct Characters
     * Time Complexity: O(n)
     * Space Complexity: O(k)
     */
    public int longestSubstringWithKDistinct(String s, int k) {
        if (s == null || s.length() == 0 || k <= 0) {
            return 0;
        }
        
        Map<Character, Integer> charCount = new HashMap<>();
        int maxLength = 0;
        int left = 0;
        
        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);
            charCount.put(c, charCount.getOrDefault(c, 0) + 1);
            
            while (charCount.size() > k) {
                char leftChar = s.charAt(left);
                charCount.put(leftChar, charCount.get(leftChar) - 1);
                if (charCount.get(leftChar) == 0) {
                    charCount.remove(leftChar);
                }
                left++;
            }
            
            maxLength = Math.max(maxLength, right - left + 1);
        }
        
        return maxLength;
    }
    
    /**
     * Find All Duplicates in Array
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     */
    public List<Integer> findAllDuplicates(int[] arr) {
        List<Integer> result = new ArrayList<>();
        
        if (arr == null || arr.length == 0) {
            return result;
        }
        
        Map<Integer, Integer> frequency = new HashMap<>();
        
        for (int num : arr) {
            frequency.put(num, frequency.getOrDefault(num, 0) + 1);
        }
        
        for (Map.Entry<Integer, Integer> entry : frequency.entrySet()) {
            if (entry.getValue() > 1) {
                result.add(entry.getKey());
            }
        }
        
        return result;
    }
    
    // Test cases and main method
    public static void main(String[] args) {
        HashMapOperations solution = new HashMapOperations();
        
        // Test frequency counting
        String[] fruits = {"apple", "banana", "apple", "cherry", "banana", "apple"};
        System.out.println("Array: " + Arrays.toString(fruits));
        System.out.println("Frequency: " + solution.countFrequency(fruits));
        System.out.println("Most Frequent: " + solution.findMostFrequent(fruits));
        System.out.println("Top 2 Frequent: " + solution.findTopKFrequent(fruits, 2));
        
        // Test grouping
        String[] items = {"apple", "banana", "carrot", "lettuce", "orange"};
        String[] categories = {"fruit", "fruit", "vegetable", "vegetable", "fruit"};
        System.out.println("\nGrouped by category: " + solution.groupByCategory(items, categories));
        
        // Test anagrams
        String s1 = "listen";
        String s2 = "silent";
        System.out.println("\nAre anagrams (" + s1 + ", " + s2 + "): " + solution.areAnagrams(s1, s2));
        
        // Test first non-repeating character
        String testStr = "leetcode";
        System.out.println("First non-repeating in '" + testStr + "': " + solution.findFirstNonRepeatingChar(testStr));
        
        // Test LRU Cache
        LRUCache<Integer, String> cache = new LRUCache<>(3);
        cache.put(1, "one");
        cache.put(2, "two");
        cache.put(3, "three");
        System.out.println("\nLRU Cache get(1): " + cache.get(1));
        cache.put(4, "four");
        System.out.println("LRU Cache get(2): " + cache.get(2)); // Should be null (evicted)
        
        // Test word pattern
        String pattern = "abba";
        String words = "dog cat cat dog";
        System.out.println("\nWord pattern (" + pattern + ", " + words + "): " + 
                         solution.wordPattern(pattern, words));
        
        // Test subarray with sum
        int[] sumArr = {1, 4, 20, 3, 10, 5};
        int target = 33;
        int[] subarray = solution.findSubarrayWithSum(sumArr, target);
        System.out.println("\nSubarray with sum " + target + " in " + Arrays.toString(sumArr) + 
                         ": [" + subarray[0] + ", " + subarray[1] + "]");
        
        // Test longest substring with k distinct
        String kStr = "eceba";
        int k = 2;
        System.out.println("Longest substring with " + k + " distinct chars in '" + kStr + "': " + 
                         solution.longestSubstringWithKDistinct(kStr, k));
        
        // Test find all duplicates
        int[] dupArr = {4, 3, 2, 7, 8, 2, 3, 1};
        System.out.println("\nAll duplicates in " + Arrays.toString(dupArr) + ": " + 
                         solution.findAllDuplicates(dupArr));
        
        // Performance test
        String[] largeArr = new String[10000];
        for (int i = 0; i < largeArr.length; i++) {
            largeArr[i] = "item" + (int) (Math.random() * 100);
        }
        
        long startTime = System.currentTimeMillis();
        solution.countFrequency(largeArr);
        long time1 = System.currentTimeMillis() - startTime;
        
        startTime = System.currentTimeMillis();
        solution.findTopKFrequent(largeArr, 10);
        long time2 = System.currentTimeMillis() - startTime;
        
        System.out.println("\nPerformance Test:");
        System.out.println("Count frequency: " + time1 + "ms");
        System.out.println("Find top K frequent: " + time2 + "ms");
    }
}
