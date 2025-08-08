import java.util.*;

/**
 * Longest Substring Without Repeating Characters
 * 
 * Problem: Given a string s, find the length of the longest substring 
 * without repeating characters.
 * 
 * Example:
 * Input: s = "abcabcbb"
 * Output: 3
 * Explanation: The answer is "abc", with the length of 3.
 * 
 * Requirements:
 * - Handle edge cases (empty string, single character, all same characters)
 * - Provide optimal solution with O(n) time complexity
 * - Include alternative approaches for different scenarios
 * - Clean, production-ready code
 */
public class LongestSubstringWithoutRepeating {
    
    /**
     * Optimal Solution using Sliding Window with HashMap
     * Time Complexity: O(n)
     * Space Complexity: O(min(m, n)) where m is charset size
     * 
     * Approach: Sliding window with HashMap to track character positions
     * - Use two pointers (left and right) to maintain window
     * - HashMap stores character to its last position
     * - When duplicate found, move left pointer to max(left, lastPosition + 1)
     */
    public int lengthOfLongestSubstring(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        
        Map<Character, Integer> charMap = new HashMap<>();
        int maxLength = 0;
        int left = 0;
        
        for (int right = 0; right < s.length(); right++) {
            char currentChar = s.charAt(right);
            
            // If character already exists in current window
            if (charMap.containsKey(currentChar)) {
                // Move left pointer to max of current left or last position + 1
                left = Math.max(left, charMap.get(currentChar) + 1);
            }
            
            // Update character position
            charMap.put(currentChar, right);
            
            // Update max length
            maxLength = Math.max(maxLength, right - left + 1);
        }
        
        return maxLength;
    }
    
    /**
     * Alternative: Sliding Window with Array (for ASCII characters)
     * Time Complexity: O(n)
     * Space Complexity: O(1) - fixed size array
     * 
     * Use case: When string contains only ASCII characters
     * More efficient for ASCII strings due to constant space
     */
    public int lengthOfLongestSubstringArray(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        
        int[] charIndex = new int[128]; // ASCII characters
        Arrays.fill(charIndex, -1);
        
        int maxLength = 0;
        int left = 0;
        
        for (int right = 0; right < s.length(); right++) {
            char currentChar = s.charAt(right);
            
            // If character already exists in current window
            if (charIndex[currentChar] >= left) {
                left = charIndex[currentChar] + 1;
            }
            
            // Update character position
            charIndex[currentChar] = right;
            
            // Update max length
            maxLength = Math.max(maxLength, right - left + 1);
        }
        
        return maxLength;
    }
    
    /**
     * Alternative: Brute Force with HashSet
     * Time Complexity: O(n²)
     * Space Complexity: O(min(m, n))
     * 
     * Use case: For understanding the problem or small strings
     */
    public int lengthOfLongestSubstringBruteForce(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        
        int maxLength = 0;
        
        for (int i = 0; i < s.length(); i++) {
            Set<Character> charSet = new HashSet<>();
            int currentLength = 0;
            
            for (int j = i; j < s.length(); j++) {
                char currentChar = s.charAt(j);
                
                if (charSet.contains(currentChar)) {
                    break;
                }
                
                charSet.add(currentChar);
                currentLength++;
            }
            
            maxLength = Math.max(maxLength, currentLength);
        }
        
        return maxLength;
    }
    
    /**
     * Extension: Find all longest substrings without repeating characters
     * Returns list of all longest substrings
     */
    public List<String> getAllLongestSubstrings(String s) {
        List<String> result = new ArrayList<>();
        if (s == null || s.length() == 0) {
            return result;
        }
        
        int maxLength = lengthOfLongestSubstring(s);
        Set<String> uniqueSubstrings = new HashSet<>();
        
        for (int i = 0; i <= s.length() - maxLength; i++) {
            String substring = s.substring(i, i + maxLength);
            if (isUnique(substring)) {
                uniqueSubstrings.add(substring);
            }
        }
        
        result.addAll(uniqueSubstrings);
        return result;
    }
    
    private boolean isUnique(String s) {
        Set<Character> charSet = new HashSet<>();
        for (char c : s.toCharArray()) {
            if (charSet.contains(c)) {
                return false;
            }
            charSet.add(c);
        }
        return true;
    }
    
    /**
     * Extension: Longest substring with at most k distinct characters
     * Time Complexity: O(n)
     * Space Complexity: O(k)
     */
    public int lengthOfLongestSubstringKDistinct(String s, int k) {
        if (s == null || s.length() == 0 || k <= 0) {
            return 0;
        }
        
        Map<Character, Integer> charCount = new HashMap<>();
        int maxLength = 0;
        int left = 0;
        
        for (int right = 0; right < s.length(); right++) {
            char currentChar = s.charAt(right);
            charCount.put(currentChar, charCount.getOrDefault(currentChar, 0) + 1);
            
            // Shrink window if more than k distinct characters
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
     * Extension: Minimum window substring
     * Find the minimum window in s which will contain all characters in t
     */
    public String minWindow(String s, String t) {
        if (s == null || t == null || s.length() == 0 || t.length() == 0) {
            return "";
        }
        
        // Count characters in t
        Map<Character, Integer> targetCount = new HashMap<>();
        for (char c : t.toCharArray()) {
            targetCount.put(c, targetCount.getOrDefault(c, 0) + 1);
        }
        
        int required = targetCount.size();
        int formed = 0;
        int left = 0, right = 0;
        int minLength = Integer.MAX_VALUE;
        int minStart = 0;
        
        Map<Character, Integer> windowCount = new HashMap<>();
        
        while (right < s.length()) {
            char currentChar = s.charAt(right);
            windowCount.put(currentChar, windowCount.getOrDefault(currentChar, 0) + 1);
            
            if (targetCount.containsKey(currentChar) && 
                windowCount.get(currentChar).equals(targetCount.get(currentChar))) {
                formed++;
            }
            
            // Try to shrink window
            while (left <= right && formed == required) {
                char leftChar = s.charAt(left);
                
                if (right - left + 1 < minLength) {
                    minLength = right - left + 1;
                    minStart = left;
                }
                
                windowCount.put(leftChar, windowCount.get(leftChar) - 1);
                
                if (targetCount.containsKey(leftChar) && 
                    windowCount.get(leftChar) < targetCount.get(leftChar)) {
                    formed--;
                }
                
                left++;
            }
            
            right++;
        }
        
        return minLength == Integer.MAX_VALUE ? "" : s.substring(minStart, minStart + minLength);
    }
    
    // Test cases and main method
    public static void main(String[] args) {
        LongestSubstringWithoutRepeating solution = new LongestSubstringWithoutRepeating();
        
        // Test cases
        String[] testCases = {
            "abcabcbb",  // Expected: 3
            "bbbbb",     // Expected: 1
            "pwwkew",    // Expected: 3
            "",          // Expected: 0
            "a",         // Expected: 1
            "ab",        // Expected: 2
            "abc",       // Expected: 3
            "dvdf",      // Expected: 3
            "anviaj"     // Expected: 5
        };
        
        for (String testCase : testCases) {
            int result = solution.lengthOfLongestSubstring(testCase);
            System.out.println("Input: \"" + testCase + "\" -> Length: " + result);
        }
        
        // Performance comparison
        String largeString = "abcdefghijklmnopqrstuvwxyz".repeat(1000);
        
        long startTime = System.currentTimeMillis();
        int result1 = solution.lengthOfLongestSubstring(largeString);
        long time1 = System.currentTimeMillis() - startTime;
        
        startTime = System.currentTimeMillis();
        int result2 = solution.lengthOfLongestSubstringArray(largeString);
        long time2 = System.currentTimeMillis() - startTime;
        
        System.out.println("\nPerformance Comparison:");
        System.out.println("HashMap approach: " + result1 + " in " + time1 + "ms");
        System.out.println("Array approach: " + result2 + " in " + time2 + "ms");
        
        // Extension tests
        System.out.println("\nExtension Tests:");
        
        // All longest substrings
        List<String> longestSubstrings = solution.getAllLongestSubstrings("abcabcbb");
        System.out.println("All longest substrings: " + longestSubstrings);
        
        // K distinct characters
        int kDistinctResult = solution.lengthOfLongestSubstringKDistinct("eceba", 2);
        System.out.println("Longest substring with 2 distinct chars: " + kDistinctResult);
        
        // Minimum window substring
        String minWindowResult = solution.minWindow("ADOBECODEBANC", "ABC");
        System.out.println("Minimum window substring: \"" + minWindowResult + "\"");
    }
}
