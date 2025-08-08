import java.util.*;

/**
 * Stack Operations Problem - Principal Engineer Level Solution
 * 
 * Problem: Implement various stack operations and algorithms including
 * expression evaluation, parentheses matching, and advanced stack patterns.
 * 
 * Example:
 * Input: "3 + 4 * 2"
 * Output: 11 (evaluates as 3 + (4 * 2))
 * 
 * Requirements:
 * - Handle edge cases (empty stack, invalid expressions, null inputs)
 * - Provide optimal solutions with different time complexities
 * - Include alternative approaches for different scenarios
 * - Clean, production-ready code
 */
public class StackOperations {
    
    /**
     * Evaluate Postfix Expression
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     */
    public int evaluatePostfix(String expression) {
        if (expression == null || expression.trim().isEmpty()) {
            throw new IllegalArgumentException("Expression cannot be null or empty");
        }
        
        Stack<Integer> stack = new Stack<>();
        String[] tokens = expression.split("\\s+");
        
        for (String token : tokens) {
            if (isOperator(token)) {
                if (stack.size() < 2) {
                    throw new IllegalArgumentException("Invalid postfix expression");
                }
                
                int b = stack.pop();
                int a = stack.pop();
                int result = performOperation(a, b, token);
                stack.push(result);
            } else {
                try {
                    stack.push(Integer.parseInt(token));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid token: " + token);
                }
            }
        }
        
        if (stack.size() != 1) {
            throw new IllegalArgumentException("Invalid postfix expression");
        }
        
        return stack.pop();
    }
    
    /**
     * Convert Infix to Postfix
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     */
    public String infixToPostfix(String expression) {
        if (expression == null || expression.trim().isEmpty()) {
            return "";
        }
        
        StringBuilder result = new StringBuilder();
        Stack<Character> stack = new Stack<>();
        
        for (char c : expression.toCharArray()) {
            if (Character.isDigit(c)) {
                result.append(c);
            } else if (c == '(') {
                stack.push(c);
            } else if (c == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    result.append(" ").append(stack.pop());
                }
                if (!stack.isEmpty() && stack.peek() == '(') {
                    stack.pop(); // Remove '('
                }
            } else if (isOperator(String.valueOf(c))) {
                while (!stack.isEmpty() && stack.peek() != '(' && 
                       getPrecedence(stack.peek()) >= getPrecedence(c)) {
                    result.append(" ").append(stack.pop());
                }
                stack.push(c);
            }
        }
        
        while (!stack.isEmpty()) {
            if (stack.peek() == '(') {
                throw new IllegalArgumentException("Mismatched parentheses");
            }
            result.append(" ").append(stack.pop());
        }
        
        return result.toString().trim();
    }
    
    /**
     * Check Valid Parentheses
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     */
    public boolean isValidParentheses(String s) {
        if (s == null || s.isEmpty()) {
            return true;
        }
        
        Stack<Character> stack = new Stack<>();
        Map<Character, Character> pairs = new HashMap<>();
        pairs.put(')', '(');
        pairs.put('}', '{');
        pairs.put(']', '[');
        
        for (char c : s.toCharArray()) {
            if (c == '(' || c == '{' || c == '[') {
                stack.push(c);
            } else if (c == ')' || c == '}' || c == ']') {
                if (stack.isEmpty() || stack.pop() != pairs.get(c)) {
                    return false;
                }
            }
        }
        
        return stack.isEmpty();
    }
    
    /**
     * Find Next Greater Element
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     */
    public int[] nextGreaterElement(int[] arr) {
        if (arr == null || arr.length == 0) {
            return new int[0];
        }
        
        int n = arr.length;
        int[] result = new int[n];
        Arrays.fill(result, -1);
        Stack<Integer> stack = new Stack<>();
        
        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && arr[stack.peek()] < arr[i]) {
                result[stack.pop()] = arr[i];
            }
            stack.push(i);
        }
        
        return result;
    }
    
    /**
     * Find Next Smaller Element
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     */
    public int[] nextSmallerElement(int[] arr) {
        if (arr == null || arr.length == 0) {
            return new int[0];
        }
        
        int n = arr.length;
        int[] result = new int[n];
        Arrays.fill(result, -1);
        Stack<Integer> stack = new Stack<>();
        
        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && arr[stack.peek()] > arr[i]) {
                result[stack.pop()] = arr[i];
            }
            stack.push(i);
        }
        
        return result;
    }
    
    /**
     * Find Largest Rectangle in Histogram
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     */
    public int largestRectangleArea(int[] heights) {
        if (heights == null || heights.length == 0) {
            return 0;
        }
        
        int n = heights.length;
        int maxArea = 0;
        Stack<Integer> stack = new Stack<>();
        
        for (int i = 0; i <= n; i++) {
            int height = (i == n) ? 0 : heights[i];
            
            while (!stack.isEmpty() && heights[stack.peek()] > height) {
                int h = heights[stack.pop()];
                int w = stack.isEmpty() ? i : i - stack.peek() - 1;
                maxArea = Math.max(maxArea, h * w);
            }
            
            stack.push(i);
        }
        
        return maxArea;
    }
    
    /**
     * Reverse String using Stack
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     */
    public String reverseString(String s) {
        if (s == null) {
            return null;
        }
        
        Stack<Character> stack = new Stack<>();
        for (char c : s.toCharArray()) {
            stack.push(c);
        }
        
        StringBuilder result = new StringBuilder();
        while (!stack.isEmpty()) {
            result.append(stack.pop());
        }
        
        return result.toString();
    }
    
    /**
     * Check if String is Palindrome using Stack
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     */
    public boolean isPalindrome(String s) {
        if (s == null || s.length() <= 1) {
            return true;
        }
        
        Stack<Character> stack = new Stack<>();
        int mid = s.length() / 2;
        
        // Push first half to stack
        for (int i = 0; i < mid; i++) {
            stack.push(s.charAt(i));
        }
        
        // Skip middle character if odd length
        int start = (s.length() % 2 == 0) ? mid : mid + 1;
        
        // Compare with second half
        for (int i = start; i < s.length(); i++) {
            if (stack.isEmpty() || stack.pop() != s.charAt(i)) {
                return false;
            }
        }
        
        return stack.isEmpty();
    }
    
    /**
     * Implement Min Stack
     * Time Complexity: O(1) for all operations
     * Space Complexity: O(n)
     */
    public static class MinStack {
        private Stack<Integer> stack;
        private Stack<Integer> minStack;
        
        public MinStack() {
            this.stack = new Stack<>();
            this.minStack = new Stack<>();
        }
        
        public void push(int val) {
            stack.push(val);
            
            if (minStack.isEmpty() || val <= minStack.peek()) {
                minStack.push(val);
            }
        }
        
        public void pop() {
            if (stack.isEmpty()) {
                throw new IllegalStateException("Stack is empty");
            }
            
            int val = stack.pop();
            if (val == minStack.peek()) {
                minStack.pop();
            }
        }
        
        public int top() {
            if (stack.isEmpty()) {
                throw new IllegalStateException("Stack is empty");
            }
            return stack.peek();
        }
        
        public int getMin() {
            if (minStack.isEmpty()) {
                throw new IllegalStateException("Stack is empty");
            }
            return minStack.peek();
        }
        
        public boolean isEmpty() {
            return stack.isEmpty();
        }
        
        public int size() {
            return stack.size();
        }
    }
    
    /**
     * Implement Queue using Stacks
     * Time Complexity: O(1) amortized for enqueue, O(n) for dequeue
     * Space Complexity: O(n)
     */
    public static class QueueUsingStacks<T> {
        private Stack<T> stack1;
        private Stack<T> stack2;
        
        public QueueUsingStacks() {
            this.stack1 = new Stack<>();
            this.stack2 = new Stack<>();
        }
        
        public void enqueue(T value) {
            stack1.push(value);
        }
        
        public T dequeue() {
            if (isEmpty()) {
                throw new IllegalStateException("Queue is empty");
            }
            
            if (stack2.isEmpty()) {
                while (!stack1.isEmpty()) {
                    stack2.push(stack1.pop());
                }
            }
            
            return stack2.pop();
        }
        
        public T peek() {
            if (isEmpty()) {
                throw new IllegalStateException("Queue is empty");
            }
            
            if (stack2.isEmpty()) {
                while (!stack1.isEmpty()) {
                    stack2.push(stack1.pop());
                }
            }
            
            return stack2.peek();
        }
        
        public boolean isEmpty() {
            return stack1.isEmpty() && stack2.isEmpty();
        }
        
        public int size() {
            return stack1.size() + stack2.size();
        }
    }
    
    /**
     * Sort Stack using Recursion
     * Time Complexity: O(n²)
     * Space Complexity: O(n) - recursion stack
     */
    public void sortStack(Stack<Integer> stack) {
        if (stack.isEmpty()) {
            return;
        }
        
        int temp = stack.pop();
        sortStack(stack);
        insertSorted(stack, temp);
    }
    
    private void insertSorted(Stack<Integer> stack, int value) {
        if (stack.isEmpty() || stack.peek() <= value) {
            stack.push(value);
            return;
        }
        
        int temp = stack.pop();
        insertSorted(stack, value);
        stack.push(temp);
    }
    
    /**
     * Remove Duplicates from Stack
     * Time Complexity: O(n²)
     * Space Complexity: O(n)
     */
    public void removeDuplicates(Stack<Integer> stack) {
        if (stack.isEmpty()) {
            return;
        }
        
        Set<Integer> seen = new HashSet<>();
        Stack<Integer> temp = new Stack<>();
        
        while (!stack.isEmpty()) {
            int val = stack.pop();
            if (!seen.contains(val)) {
                seen.add(val);
                temp.push(val);
            }
        }
        
        while (!temp.isEmpty()) {
            stack.push(temp.pop());
        }
    }
    
    // Helper methods
    private boolean isOperator(String token) {
        return token.equals("+") || token.equals("-") || 
               token.equals("*") || token.equals("/") || 
               token.equals("^");
    }
    
    private int performOperation(int a, int b, String operator) {
        switch (operator) {
            case "+": return a + b;
            case "-": return a - b;
            case "*": return a * b;
            case "/": 
                if (b == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                return a / b;
            case "^": return (int) Math.pow(a, b);
            default: throw new IllegalArgumentException("Unknown operator: " + operator);
        }
    }
    
    private int getPrecedence(char operator) {
        switch (operator) {
            case '^': return 3;
            case '*': case '/': return 2;
            case '+': case '-': return 1;
            default: return 0;
        }
    }
    
    // Test cases and main method
    public static void main(String[] args) {
        StackOperations solution = new StackOperations();
        
        // Test Postfix Evaluation
        System.out.println("=== Postfix Evaluation Test ===");
        String postfix = "3 4 2 * +";
        System.out.println("Postfix: " + postfix + " = " + solution.evaluatePostfix(postfix));
        
        // Test Infix to Postfix
        System.out.println("\n=== Infix to Postfix Test ===");
        String infix = "3 + 4 * 2";
        String postfixResult = solution.infixToPostfix(infix);
        System.out.println("Infix: " + infix + " -> Postfix: " + postfixResult);
        System.out.println("Evaluated: " + solution.evaluatePostfix(postfixResult));
        
        // Test Parentheses Validation
        System.out.println("\n=== Parentheses Validation Test ===");
        String[] parentheses = {"()", "()[]{}", "(]", "([)]", "{[]}", ""};
        for (String p : parentheses) {
            System.out.println("'" + p + "' is valid: " + solution.isValidParentheses(p));
        }
        
        // Test Next Greater Element
        System.out.println("\n=== Next Greater Element Test ===");
        int[] arr = {4, 5, 2, 10, 8};
        int[] nextGreater = solution.nextGreaterElement(arr);
        System.out.println("Array: " + Arrays.toString(arr));
        System.out.println("Next Greater: " + Arrays.toString(nextGreater));
        
        // Test Largest Rectangle in Histogram
        System.out.println("\n=== Largest Rectangle Test ===");
        int[] heights = {2, 1, 5, 6, 2, 3};
        int maxArea = solution.largestRectangleArea(heights);
        System.out.println("Heights: " + Arrays.toString(heights));
        System.out.println("Largest Rectangle Area: " + maxArea);
        
        // Test String Operations
        System.out.println("\n=== String Operations Test ===");
        String testStr = "hello";
        System.out.println("Original: " + testStr);
        System.out.println("Reversed: " + solution.reverseString(testStr));
        System.out.println("Is palindrome: " + solution.isPalindrome("racecar"));
        
        // Test Min Stack
        System.out.println("\n=== Min Stack Test ===");
        MinStack minStack = new MinStack();
        minStack.push(3);
        minStack.push(5);
        minStack.push(2);
        minStack.push(1);
        System.out.println("Top: " + minStack.top());
        System.out.println("Min: " + minStack.getMin());
        minStack.pop();
        System.out.println("After pop - Top: " + minStack.top() + ", Min: " + minStack.getMin());
        
        // Test Queue using Stacks
        System.out.println("\n=== Queue using Stacks Test ===");
        QueueUsingStacks<String> queue = new QueueUsingStacks<>();
        queue.enqueue("first");
        queue.enqueue("second");
        queue.enqueue("third");
        System.out.println("Dequeue: " + queue.dequeue());
        System.out.println("Peek: " + queue.peek());
        
        // Test Stack Sorting
        System.out.println("\n=== Stack Sorting Test ===");
        Stack<Integer> unsortedStack = new Stack<>();
        unsortedStack.push(3);
        unsortedStack.push(1);
        unsortedStack.push(4);
        unsortedStack.push(2);
        System.out.println("Unsorted stack: " + unsortedStack);
        solution.sortStack(unsortedStack);
        System.out.println("Sorted stack: " + unsortedStack);
        
        // Performance test
        System.out.println("\n=== Performance Test ===");
        int[] largeArr = new int[10000];
        for (int i = 0; i < largeArr.length; i++) {
            largeArr[i] = (int) (Math.random() * 1000);
        }
        
        long startTime = System.currentTimeMillis();
        solution.nextGreaterElement(largeArr);
        long time1 = System.currentTimeMillis() - startTime;
        
        startTime = System.currentTimeMillis();
        solution.largestRectangleArea(largeArr);
        long time2 = System.currentTimeMillis() - startTime;
        
        System.out.println("Next greater element: " + time1 + "ms");
        System.out.println("Largest rectangle: " + time2 + "ms");
    }
}
