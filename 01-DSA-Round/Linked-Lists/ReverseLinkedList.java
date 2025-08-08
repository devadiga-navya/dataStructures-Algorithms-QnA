import java.util.*;

/**
 * Reverse Linked List Problem - Principal Engineer Level Solution
 * 
 * Problem: Given the head of a singly linked list, reverse the list, and return the reversed list.
 * 
 * Example:
 * Input: head = [1,2,3,4,5]
 * Output: [5,4,3,2,1]
 * 
 * Requirements:
 * - Handle edge cases (null head, single node, empty list)
 * - Provide optimal solution with O(n) time complexity
 * - Include alternative approaches for different scenarios
 * - Clean, production-ready code
 */

// Definition for singly-linked list node
class ListNode {
    int val;
    ListNode next;
    
    ListNode() {}
    ListNode(int val) { this.val = val; }
    ListNode(int val, ListNode next) { 
        this.val = val; 
        this.next = next; 
    }
}

public class ReverseLinkedList {
    
    /**
     * Optimal Solution using Iterative Approach
     * Time Complexity: O(n)
     * Space Complexity: O(1)
     * 
     * Approach: Use three pointers to reverse links
     * - prev: points to previous node (initially null)
     * - current: points to current node
     * - next: points to next node
     * - Reverse each link and move pointers
     */
    public ListNode reverseList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        
        ListNode prev = null;
        ListNode current = head;
        
        while (current != null) {
            ListNode next = current.next;
            current.next = prev;
            prev = current;
            current = next;
        }
        
        return prev;
    }
    
    /**
     * Alternative: Recursive Approach
     * Time Complexity: O(n)
     * Space Complexity: O(n) - recursion stack
     * 
     * Use case: When you prefer recursive solutions
     */
    public ListNode reverseListRecursive(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        
        ListNode newHead = reverseListRecursive(head.next);
        head.next.next = head;
        head.next = null;
        
        return newHead;
    }
    
    /**
     * Alternative: Using Stack (when extra space is allowed)
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     * 
     * Use case: When you want to preserve original list
     */
    public ListNode reverseListStack(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        
        Stack<ListNode> stack = new Stack<>();
        ListNode current = head;
        
        // Push all nodes to stack
        while (current != null) {
            stack.push(current);
            current = current.next;
        }
        
        // Pop and create new list
        ListNode newHead = stack.pop();
        current = newHead;
        
        while (!stack.isEmpty()) {
            current.next = stack.pop();
            current = current.next;
        }
        
        current.next = null;
        return newHead;
    }
    
    /**
     * Extension: Reverse Linked List in groups of k
     * Reverse every k consecutive nodes
     */
    public ListNode reverseKGroup(ListNode head, int k) {
        if (head == null || head.next == null || k <= 1) {
            return head;
        }
        
        // Count nodes in current group
        int count = 0;
        ListNode current = head;
        while (current != null && count < k) {
            current = current.next;
            count++;
        }
        
        // If we have k nodes, reverse them
        if (count == k) {
            ListNode prev = null;
            current = head;
            
            // Reverse k nodes
            for (int i = 0; i < k; i++) {
                ListNode next = current.next;
                current.next = prev;
                prev = current;
                current = next;
            }
            
            // Recursively reverse remaining nodes
            head.next = reverseKGroup(current, k);
            return prev;
        }
        
        // If less than k nodes, return as is
        return head;
    }
    
    /**
     * Extension: Reverse nodes between positions m and n
     * Reverse nodes from position m to n (1-indexed)
     */
    public ListNode reverseBetween(ListNode head, int m, int n) {
        if (head == null || m >= n) {
            return head;
        }
        
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode prev = dummy;
        
        // Move to position m-1
        for (int i = 1; i < m; i++) {
            prev = prev.next;
        }
        
        ListNode start = prev.next;
        ListNode current = start.next;
        
        // Reverse nodes from m to n
        for (int i = 0; i < n - m; i++) {
            start.next = current.next;
            current.next = prev.next;
            prev.next = current;
            current = start.next;
        }
        
        return dummy.next;
    }
    
    /**
     * Extension: Reverse alternate nodes
     * Reverse every alternate node in the list
     */
    public ListNode reverseAlternate(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        
        ListNode current = head;
        ListNode next = current.next;
        
        // Reverse first two nodes
        current.next = next.next;
        next.next = current;
        head = next;
        
        // Reverse remaining alternate pairs
        current = current.next;
        while (current != null && current.next != null) {
            next = current.next;
            current.next = next.next;
            next.next = current;
            current = current.next;
        }
        
        return head;
    }
    
    /**
     * Extension: Reverse nodes in pairs
     * Reverse every pair of nodes
     */
    public ListNode reversePairs(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode prev = dummy;
        
        while (prev.next != null && prev.next.next != null) {
            ListNode first = prev.next;
            ListNode second = first.next;
            
            // Reverse the pair
            first.next = second.next;
            second.next = first;
            prev.next = second;
            
            // Move to next pair
            prev = first;
        }
        
        return dummy.next;
    }
    
    /**
     * Extension: Check if linked list is palindrome
     * Reverse second half and compare with first half
     */
    public boolean isPalindrome(ListNode head) {
        if (head == null || head.next == null) {
            return true;
        }
        
        // Find middle of list
        ListNode slow = head;
        ListNode fast = head;
        
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        
        // Reverse second half
        ListNode secondHalf = reverseList(slow.next);
        ListNode firstHalf = head;
        
        // Compare first and second half
        while (secondHalf != null) {
            if (firstHalf.val != secondHalf.val) {
                return false;
            }
            firstHalf = firstHalf.next;
            secondHalf = secondHalf.next;
        }
        
        return true;
    }
    
    /**
     * Extension: Add two numbers represented by linked lists
     * Numbers are stored in reverse order
     */
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(0);
        ListNode current = dummy;
        int carry = 0;
        
        while (l1 != null || l2 != null || carry != 0) {
            int sum = carry;
            
            if (l1 != null) {
                sum += l1.val;
                l1 = l1.next;
            }
            
            if (l2 != null) {
                sum += l2.val;
                l2 = l2.next;
            }
            
            carry = sum / 10;
            current.next = new ListNode(sum % 10);
            current = current.next;
        }
        
        return dummy.next;
    }
    
    // Utility method to create linked list from array
    public ListNode createList(int[] arr) {
        if (arr == null || arr.length == 0) {
            return null;
        }
        
        ListNode head = new ListNode(arr[0]);
        ListNode current = head;
        
        for (int i = 1; i < arr.length; i++) {
            current.next = new ListNode(arr[i]);
            current = current.next;
        }
        
        return head;
    }
    
    // Utility method to print linked list
    public void printList(ListNode head) {
        ListNode current = head;
        while (current != null) {
            System.out.print(current.val + " -> ");
            current = current.next;
        }
        System.out.println("null");
    }
    
    // Test cases and main method
    public static void main(String[] args) {
        ReverseLinkedList solution = new ReverseLinkedList();
        
        // Test cases
        int[][] testCases = {
            {1, 2, 3, 4, 5},    // Normal case
            {1, 2},              // Two nodes
            {1},                 // Single node
            {},                  // Empty array
            {1, 2, 3, 4, 5, 6}, // Even number of nodes
            {1, 2, 3, 4, 5}     // Odd number of nodes
        };
        
        for (int i = 0; i < testCases.length; i++) {
            ListNode head = solution.createList(testCases[i]);
            System.out.print("Original " + (i + 1) + ": ");
            solution.printList(head);
            
            ListNode reversed = solution.reverseList(head);
            System.out.print("Reversed " + (i + 1) + ": ");
            solution.printList(reversed);
            System.out.println();
        }
        
        // Performance comparison
        int[] largeArray = new int[10000];
        for (int i = 0; i < largeArray.length; i++) {
            largeArray[i] = i;
        }
        
        ListNode largeList = solution.createList(largeArray);
        
        long startTime = System.currentTimeMillis();
        solution.reverseList(largeList);
        long time1 = System.currentTimeMillis() - startTime;
        
        largeList = solution.createList(largeArray);
        startTime = System.currentTimeMillis();
        solution.reverseListRecursive(largeList);
        long time2 = System.currentTimeMillis() - startTime;
        
        System.out.println("Performance Comparison:");
        System.out.println("Iterative: " + time1 + "ms");
        System.out.println("Recursive: " + time2 + "ms");
        
        // Extension tests
        System.out.println("\nExtension Tests:");
        
        // Reverse in groups
        ListNode groupList = solution.createList(new int[]{1, 2, 3, 4, 5, 6, 7, 8});
        ListNode groupReversed = solution.reverseKGroup(groupList, 3);
        System.out.print("Reverse in groups of 3: ");
        solution.printList(groupReversed);
        
        // Reverse between positions
        ListNode betweenList = solution.createList(new int[]{1, 2, 3, 4, 5});
        ListNode betweenReversed = solution.reverseBetween(betweenList, 2, 4);
        System.out.print("Reverse between 2-4: ");
        solution.printList(betweenReversed);
        
        // Palindrome check
        ListNode palindromeList = solution.createList(new int[]{1, 2, 3, 2, 1});
        boolean isPalindrome = solution.isPalindrome(palindromeList);
        System.out.println("Is palindrome: " + isPalindrome);
        
        // Add two numbers
        ListNode num1 = solution.createList(new int[]{2, 4, 3});
        ListNode num2 = solution.createList(new int[]{5, 6, 4});
        ListNode sum = solution.addTwoNumbers(num1, num2);
        System.out.print("Add two numbers: ");
        solution.printList(sum);
    }
}
