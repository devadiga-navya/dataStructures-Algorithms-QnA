import java.util.*;

/**
 * Queue Operations Problem - Principal Engineer Level Solution
 * 
 * Problem: Implement various queue operations and algorithms including
 * circular queue, priority queue, deque, and advanced queue patterns.
 * 
 * Example:
 * Input: [1, 2, 3, 4, 5]
 * Enqueue: 6 -> [1, 2, 3, 4, 5, 6]
 * Dequeue: 1 -> [2, 3, 4, 5, 6]
 * 
 * Requirements:
 * - Handle edge cases (empty queue, full queue, null elements)
 * - Provide optimal solutions with different time complexities
 * - Include alternative approaches for different scenarios
 * - Clean, production-ready code
 */
public class QueueOperations {
    
    /**
     * Circular Queue Implementation
     * Time Complexity: O(1) for enqueue and dequeue
     * Space Complexity: O(n)
     */
    public static class CircularQueue {
        private int[] queue;
        private int front;
        private int rear;
        private int size;
        private int capacity;
        
        public CircularQueue(int capacity) {
            this.capacity = capacity;
            this.queue = new int[capacity];
            this.front = 0;
            this.rear = -1;
            this.size = 0;
        }
        
        public boolean enqueue(int value) {
            if (isFull()) {
                return false;
            }
            
            rear = (rear + 1) % capacity;
            queue[rear] = value;
            size++;
            return true;
        }
        
        public int dequeue() {
            if (isEmpty()) {
                throw new IllegalStateException("Queue is empty");
            }
            
            int value = queue[front];
            front = (front + 1) % capacity;
            size--;
            return value;
        }
        
        public int peek() {
            if (isEmpty()) {
                throw new IllegalStateException("Queue is empty");
            }
            return queue[front];
        }
        
        public boolean isEmpty() {
            return size == 0;
        }
        
        public boolean isFull() {
            return size == capacity;
        }
        
        public int size() {
            return size;
        }
        
        public void clear() {
            front = 0;
            rear = -1;
            size = 0;
        }
    }
    
    /**
     * Priority Queue Implementation
     * Time Complexity: O(log n) for enqueue and dequeue
     * Space Complexity: O(n)
     */
    public static class PriorityQueue<T extends Comparable<T>> {
        private List<T> heap;
        
        public PriorityQueue() {
            this.heap = new ArrayList<>();
        }
        
        public void enqueue(T value) {
            heap.add(value);
            heapifyUp(heap.size() - 1);
        }
        
        public T dequeue() {
            if (isEmpty()) {
                throw new IllegalStateException("Priority queue is empty");
            }
            
            T max = heap.get(0);
            heap.set(0, heap.get(heap.size() - 1));
            heap.remove(heap.size() - 1);
            
            if (!isEmpty()) {
                heapifyDown(0);
            }
            
            return max;
        }
        
        public T peek() {
            if (isEmpty()) {
                throw new IllegalStateException("Priority queue is empty");
            }
            return heap.get(0);
        }
        
        public boolean isEmpty() {
            return heap.isEmpty();
        }
        
        public int size() {
            return heap.size();
        }
        
        private void heapifyUp(int index) {
            int parent = (index - 1) / 2;
            
            if (index > 0 && heap.get(index).compareTo(heap.get(parent)) > 0) {
                swap(index, parent);
                heapifyUp(parent);
            }
        }
        
        private void heapifyDown(int index) {
            int leftChild = 2 * index + 1;
            int rightChild = 2 * index + 2;
            int largest = index;
            
            if (leftChild < heap.size() && heap.get(leftChild).compareTo(heap.get(largest)) > 0) {
                largest = leftChild;
            }
            
            if (rightChild < heap.size() && heap.get(rightChild).compareTo(heap.get(largest)) > 0) {
                largest = rightChild;
            }
            
            if (largest != index) {
                swap(index, largest);
                heapifyDown(largest);
            }
        }
        
        private void swap(int i, int j) {
            T temp = heap.get(i);
            heap.set(i, heap.get(j));
            heap.set(j, temp);
        }
    }
    
    /**
     * Deque (Double-Ended Queue) Implementation
     * Time Complexity: O(1) for all operations
     * Space Complexity: O(n)
     */
    public static class Deque<T> {
        private Node<T> head;
        private Node<T> tail;
        private int size;
        
        public Deque() {
            this.head = null;
            this.tail = null;
            this.size = 0;
        }
        
        public void addFirst(T value) {
            Node<T> newNode = new Node<>(value);
            if (isEmpty()) {
                head = tail = newNode;
            } else {
                newNode.next = head;
                head.prev = newNode;
                head = newNode;
            }
            size++;
        }
        
        public void addLast(T value) {
            Node<T> newNode = new Node<>(value);
            if (isEmpty()) {
                head = tail = newNode;
            } else {
                newNode.prev = tail;
                tail.next = newNode;
                tail = newNode;
            }
            size++;
        }
        
        public T removeFirst() {
            if (isEmpty()) {
                throw new IllegalStateException("Deque is empty");
            }
            
            T value = head.value;
            head = head.next;
            
            if (head == null) {
                tail = null;
            } else {
                head.prev = null;
            }
            
            size--;
            return value;
        }
        
        public T removeLast() {
            if (isEmpty()) {
                throw new IllegalStateException("Deque is empty");
            }
            
            T value = tail.value;
            tail = tail.prev;
            
            if (tail == null) {
                head = null;
            } else {
                tail.next = null;
            }
            
            size--;
            return value;
        }
        
        public T getFirst() {
            if (isEmpty()) {
                throw new IllegalStateException("Deque is empty");
            }
            return head.value;
        }
        
        public T getLast() {
            if (isEmpty()) {
                throw new IllegalStateException("Deque is empty");
            }
            return tail.value;
        }
        
        public boolean isEmpty() {
            return size == 0;
        }
        
        public int size() {
            return size;
        }
        
        private static class Node<T> {
            T value;
            Node<T> prev;
            Node<T> next;
            
            Node(T value) {
                this.value = value;
            }
        }
    }
    
    /**
     * Sliding Window Maximum using Deque
     * Time Complexity: O(n)
     * Space Complexity: O(k)
     */
    public int[] maxSlidingWindow(int[] nums, int k) {
        if (nums == null || nums.length == 0 || k <= 0) {
            return new int[0];
        }
        
        int n = nums.length;
        int[] result = new int[n - k + 1];
        Deque<Integer> deque = new Deque<>();
        
        for (int i = 0; i < n; i++) {
            // Remove elements outside the window
            while (!deque.isEmpty() && deque.getFirst() <= i - k) {
                deque.removeFirst();
            }
            
            // Remove smaller elements from the back
            while (!deque.isEmpty() && nums[deque.getLast()] < nums[i]) {
                deque.removeLast();
            }
            
            deque.addLast(i);
            
            // Add maximum to result
            if (i >= k - 1) {
                result[i - k + 1] = nums[deque.getFirst()];
            }
        }
        
        return result;
    }
    
    /**
     * Level Order Traversal using Queue
     * Time Complexity: O(n)
     * Space Complexity: O(w) where w is maximum width
     */
    public List<List<Integer>> levelOrderTraversal(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        
        if (root == null) {
            return result;
        }
        
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        
        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            List<Integer> currentLevel = new ArrayList<>();
            
            for (int i = 0; i < levelSize; i++) {
                TreeNode current = queue.poll();
                currentLevel.add(current.val);
                
                if (current.left != null) {
                    queue.offer(current.left);
                }
                if (current.right != null) {
                    queue.offer(current.right);
                }
            }
            
            result.add(currentLevel);
        }
        
        return result;
    }
    
    /**
     * Implement Stack using Queues
     * Time Complexity: O(n) for push, O(1) for pop
     * Space Complexity: O(n)
     */
    public static class StackUsingQueues<T> {
        private Queue<T> q1;
        private Queue<T> q2;
        
        public StackUsingQueues() {
            this.q1 = new LinkedList<>();
            this.q2 = new LinkedList<>();
        }
        
        public void push(T value) {
            q2.offer(value);
            
            while (!q1.isEmpty()) {
                q2.offer(q1.poll());
            }
            
            Queue<T> temp = q1;
            q1 = q2;
            q2 = temp;
        }
        
        public T pop() {
            if (isEmpty()) {
                throw new IllegalStateException("Stack is empty");
            }
            return q1.poll();
        }
        
        public T peek() {
            if (isEmpty()) {
                throw new IllegalStateException("Stack is empty");
            }
            return q1.peek();
        }
        
        public boolean isEmpty() {
            return q1.isEmpty();
        }
        
        public int size() {
            return q1.size();
        }
    }
    
    /**
     * Implement Queue using Stacks
     * Time Complexity: O(1) for push, O(n) for pop (amortized O(1))
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
     * Find First Negative Number in Every Window of Size K
     * Time Complexity: O(n)
     * Space Complexity: O(k)
     */
    public List<Integer> firstNegativeInWindow(int[] arr, int k) {
        List<Integer> result = new ArrayList<>();
        
        if (arr == null || arr.length == 0 || k <= 0) {
            return result;
        }
        
        Queue<Integer> queue = new LinkedList<>();
        
        for (int i = 0; i < arr.length; i++) {
            // Remove elements outside the window
            while (!queue.isEmpty() && queue.peek() <= i - k) {
                queue.poll();
            }
            
            // Add negative numbers to queue
            if (arr[i] < 0) {
                queue.offer(i);
            }
            
            // Add result for current window
            if (i >= k - 1) {
                if (queue.isEmpty()) {
                    result.add(0);
                } else {
                    result.add(arr[queue.peek()]);
                }
            }
        }
        
        return result;
    }
    
    /**
     * Generate Binary Numbers from 1 to N
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     */
    public List<String> generateBinaryNumbers(int n) {
        List<String> result = new ArrayList<>();
        
        if (n <= 0) {
            return result;
        }
        
        Queue<String> queue = new LinkedList<>();
        queue.offer("1");
        
        for (int i = 0; i < n; i++) {
            String current = queue.poll();
            result.add(current);
            
            queue.offer(current + "0");
            queue.offer(current + "1");
        }
        
        return result;
    }
    
    // TreeNode class for tree operations
    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        
        TreeNode(int val) {
            this.val = val;
        }
    }
    
    // Test cases and main method
    public static void main(String[] args) {
        QueueOperations solution = new QueueOperations();
        
        // Test Circular Queue
        System.out.println("=== Circular Queue Test ===");
        CircularQueue cq = new CircularQueue(5);
        cq.enqueue(1);
        cq.enqueue(2);
        cq.enqueue(3);
        System.out.println("Dequeued: " + cq.dequeue());
        System.out.println("Peek: " + cq.peek());
        cq.enqueue(4);
        cq.enqueue(5);
        cq.enqueue(6);
        System.out.println("Is full: " + cq.isFull());
        
        // Test Priority Queue
        System.out.println("\n=== Priority Queue Test ===");
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        pq.enqueue(3);
        pq.enqueue(1);
        pq.enqueue(4);
        pq.enqueue(2);
        System.out.println("Peek: " + pq.peek());
        System.out.println("Dequeued: " + pq.dequeue());
        System.out.println("Dequeued: " + pq.dequeue());
        
        // Test Deque
        System.out.println("\n=== Deque Test ===");
        Deque<String> deque = new Deque<>();
        deque.addFirst("first");
        deque.addLast("last");
        deque.addFirst("newFirst");
        System.out.println("First: " + deque.getFirst());
        System.out.println("Last: " + deque.getLast());
        System.out.println("Removed first: " + deque.removeFirst());
        System.out.println("Removed last: " + deque.removeLast());
        
        // Test Sliding Window Maximum
        System.out.println("\n=== Sliding Window Maximum Test ===");
        int[] nums = {1, 3, -1, -3, 5, 3, 6, 7};
        int k = 3;
        int[] maxWindow = solution.maxSlidingWindow(nums, k);
        System.out.println("Max sliding window: " + Arrays.toString(maxWindow));
        
        // Test Stack using Queues
        System.out.println("\n=== Stack using Queues Test ===");
        StackUsingQueues<Integer> stack = new StackUsingQueues<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        System.out.println("Pop: " + stack.pop());
        System.out.println("Peek: " + stack.peek());
        
        // Test Queue using Stacks
        System.out.println("\n=== Queue using Stacks Test ===");
        QueueUsingStacks<String> queue = new QueueUsingStacks<>();
        queue.enqueue("first");
        queue.enqueue("second");
        queue.enqueue("third");
        System.out.println("Dequeue: " + queue.dequeue());
        System.out.println("Peek: " + queue.peek());
        
        // Test First Negative in Window
        System.out.println("\n=== First Negative in Window Test ===");
        int[] negArr = {12, -1, -7, 8, -15, 30, 16, 28};
        int windowSize = 3;
        List<Integer> negatives = solution.firstNegativeInWindow(negArr, windowSize);
        System.out.println("First negatives in windows: " + negatives);
        
        // Test Generate Binary Numbers
        System.out.println("\n=== Generate Binary Numbers Test ===");
        List<String> binaryNumbers = solution.generateBinaryNumbers(5);
        System.out.println("Binary numbers 1 to 5: " + binaryNumbers);
        
        // Performance test
        System.out.println("\n=== Performance Test ===");
        int[] largeArr = new int[10000];
        for (int i = 0; i < largeArr.length; i++) {
            largeArr[i] = (int) (Math.random() * 1000);
        }
        
        long startTime = System.currentTimeMillis();
        solution.maxSlidingWindow(largeArr, 100);
        long time1 = System.currentTimeMillis() - startTime;
        
        startTime = System.currentTimeMillis();
        solution.firstNegativeInWindow(largeArr, 100);
        long time2 = System.currentTimeMillis() - startTime;
        
        System.out.println("Sliding window max: " + time1 + "ms");
        System.out.println("First negative in window: " + time2 + "ms");
    }
}
