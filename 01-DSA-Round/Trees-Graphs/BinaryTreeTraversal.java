import java.util.*;

/**
 * Binary Tree Traversal Problem - Principal Engineer Level Solution
 * 
 * Problem: Implement various traversal methods for binary trees including
 * inorder, preorder, postorder, and level order traversals.
 * 
 * Example:
 * Input: 
 *       1
 *      / \
 *     2   3
 *    / \
 *   4   5
 * 
 * Inorder: [4, 2, 5, 1, 3]
 * Preorder: [1, 2, 4, 5, 3]
 * Postorder: [4, 5, 2, 3, 1]
 * Level Order: [[1], [2, 3], [4, 5]]
 * 
 * Requirements:
 * - Handle edge cases (null root, single node, empty tree)
 * - Provide both recursive and iterative solutions
 * - Include alternative approaches for different scenarios
 * - Clean, production-ready code
 */

// Definition for binary tree node
class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
    
    TreeNode() {}
    TreeNode(int val) { this.val = val; }
    TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }
}

public class BinaryTreeTraversal {
    
    /**
     * Inorder Traversal - Recursive
     * Time Complexity: O(n)
     * Space Complexity: O(h) where h is height of tree
     * 
     * Approach: Left -> Root -> Right
     */
    public List<Integer> inorderTraversalRecursive(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        inorderHelper(root, result);
        return result;
    }
    
    private void inorderHelper(TreeNode root, List<Integer> result) {
        if (root == null) {
            return;
        }
        
        inorderHelper(root.left, result);
        result.add(root.val);
        inorderHelper(root.right, result);
    }
    
    /**
     * Inorder Traversal - Iterative using Stack
     * Time Complexity: O(n)
     * Space Complexity: O(h)
     * 
     * Approach: Use stack to simulate recursion
     */
    public List<Integer> inorderTraversalIterative(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        Stack<TreeNode> stack = new Stack<>();
        TreeNode current = root;
        
        while (current != null || !stack.isEmpty()) {
            // Go to leftmost node
            while (current != null) {
                stack.push(current);
                current = current.left;
            }
            
            // Process current node
            current = stack.pop();
            result.add(current.val);
            
            // Move to right subtree
            current = current.right;
        }
        
        return result;
    }
    
    /**
     * Preorder Traversal - Recursive
     * Time Complexity: O(n)
     * Space Complexity: O(h)
     * 
     * Approach: Root -> Left -> Right
     */
    public List<Integer> preorderTraversalRecursive(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        preorderHelper(root, result);
        return result;
    }
    
    private void preorderHelper(TreeNode root, List<Integer> result) {
        if (root == null) {
            return;
        }
        
        result.add(root.val);
        preorderHelper(root.left, result);
        preorderHelper(root.right, result);
    }
    
    /**
     * Preorder Traversal - Iterative using Stack
     * Time Complexity: O(n)
     * Space Complexity: O(h)
     */
    public List<Integer> preorderTraversalIterative(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) {
            return result;
        }
        
        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);
        
        while (!stack.isEmpty()) {
            TreeNode current = stack.pop();
            result.add(current.val);
            
            // Push right child first (so left is processed first)
            if (current.right != null) {
                stack.push(current.right);
            }
            if (current.left != null) {
                stack.push(current.left);
            }
        }
        
        return result;
    }
    
    /**
     * Postorder Traversal - Recursive
     * Time Complexity: O(n)
     * Space Complexity: O(h)
     * 
     * Approach: Left -> Right -> Root
     */
    public List<Integer> postorderTraversalRecursive(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        postorderHelper(root, result);
        return result;
    }
    
    private void postorderHelper(TreeNode root, List<Integer> result) {
        if (root == null) {
            return;
        }
        
        postorderHelper(root.left, result);
        postorderHelper(root.right, result);
        result.add(root.val);
    }
    
    /**
     * Postorder Traversal - Iterative using Two Stacks
     * Time Complexity: O(n)
     * Space Complexity: O(h)
     */
    public List<Integer> postorderTraversalIterative(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) {
            return result;
        }
        
        Stack<TreeNode> stack1 = new Stack<>();
        Stack<TreeNode> stack2 = new Stack<>();
        stack1.push(root);
        
        while (!stack1.isEmpty()) {
            TreeNode current = stack1.pop();
            stack2.push(current);
            
            if (current.left != null) {
                stack1.push(current.left);
            }
            if (current.right != null) {
                stack1.push(current.right);
            }
        }
        
        while (!stack2.isEmpty()) {
            result.add(stack2.pop().val);
        }
        
        return result;
    }
    
    /**
     * Level Order Traversal (BFS)
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
     * Zigzag Level Order Traversal
     * Time Complexity: O(n)
     * Space Complexity: O(w)
     */
    public List<List<Integer>> zigzagLevelOrder(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) {
            return result;
        }
        
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        boolean leftToRight = true;
        
        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            List<Integer> currentLevel = new ArrayList<>();
            
            for (int i = 0; i < levelSize; i++) {
                TreeNode current = queue.poll();
                
                if (leftToRight) {
                    currentLevel.add(current.val);
                } else {
                    currentLevel.add(0, current.val);
                }
                
                if (current.left != null) {
                    queue.offer(current.left);
                }
                if (current.right != null) {
                    queue.offer(current.right);
                }
            }
            
            result.add(currentLevel);
            leftToRight = !leftToRight;
        }
        
        return result;
    }
    
    /**
     * Vertical Order Traversal
     * Time Complexity: O(n log n)
     * Space Complexity: O(n)
     */
    public List<List<Integer>> verticalOrderTraversal(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) {
            return result;
        }
        
        TreeMap<Integer, List<Integer>> columnMap = new TreeMap<>();
        Queue<TreeNode> nodeQueue = new LinkedList<>();
        Queue<Integer> colQueue = new LinkedList<>();
        
        nodeQueue.offer(root);
        colQueue.offer(0);
        
        while (!nodeQueue.isEmpty()) {
            TreeNode current = nodeQueue.poll();
            int col = colQueue.poll();
            
            columnMap.computeIfAbsent(col, k -> new ArrayList<>()).add(current.val);
            
            if (current.left != null) {
                nodeQueue.offer(current.left);
                colQueue.offer(col - 1);
            }
            if (current.right != null) {
                nodeQueue.offer(current.right);
                colQueue.offer(col + 1);
            }
        }
        
        result.addAll(columnMap.values());
        return result;
    }
    
    /**
     * Morris Inorder Traversal (Threaded Binary Tree)
     * Time Complexity: O(n)
     * Space Complexity: O(1)
     * 
     * Approach: Use threaded binary tree concept
     */
    public List<Integer> morrisInorderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        TreeNode current = root;
        
        while (current != null) {
            if (current.left == null) {
                result.add(current.val);
                current = current.right;
            } else {
                // Find inorder predecessor
                TreeNode predecessor = current.left;
                while (predecessor.right != null && predecessor.right != current) {
                    predecessor = predecessor.right;
                }
                
                if (predecessor.right == null) {
                    predecessor.right = current;
                    current = current.left;
                } else {
                    predecessor.right = null;
                    result.add(current.val);
                    current = current.right;
                }
            }
        }
        
        return result;
    }
    
    /**
     * Boundary Traversal
     * Time Complexity: O(n)
     * Space Complexity: O(h)
     */
    public List<Integer> boundaryTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) {
            return result;
        }
        
        result.add(root.val);
        
        // Left boundary (excluding leaf)
        addLeftBoundary(root.left, result);
        
        // Leaf nodes
        addLeaves(root, result);
        
        // Right boundary (excluding leaf)
        addRightBoundary(root.right, result);
        
        return result;
    }
    
    private void addLeftBoundary(TreeNode root, List<Integer> result) {
        if (root == null || (root.left == null && root.right == null)) {
            return;
        }
        
        result.add(root.val);
        if (root.left != null) {
            addLeftBoundary(root.left, result);
        } else {
            addLeftBoundary(root.right, result);
        }
    }
    
    private void addRightBoundary(TreeNode root, List<Integer> result) {
        if (root == null || (root.left == null && root.right == null)) {
            return;
        }
        
        if (root.right != null) {
            addRightBoundary(root.right, result);
        } else {
            addRightBoundary(root.left, result);
        }
        result.add(root.val);
    }
    
    private void addLeaves(TreeNode root, List<Integer> result) {
        if (root == null) {
            return;
        }
        
        if (root.left == null && root.right == null) {
            result.add(root.val);
            return;
        }
        
        addLeaves(root.left, result);
        addLeaves(root.right, result);
    }
    
    // Utility method to create binary tree from array
    public TreeNode createTree(Integer[] arr) {
        if (arr == null || arr.length == 0 || arr[0] == null) {
            return null;
        }
        
        TreeNode root = new TreeNode(arr[0]);
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        
        for (int i = 1; i < arr.length; i += 2) {
            TreeNode current = queue.poll();
            
            if (i < arr.length && arr[i] != null) {
                current.left = new TreeNode(arr[i]);
                queue.offer(current.left);
            }
            
            if (i + 1 < arr.length && arr[i + 1] != null) {
                current.right = new TreeNode(arr[i + 1]);
                queue.offer(current.right);
            }
        }
        
        return root;
    }
    
    // Test cases and main method
    public static void main(String[] args) {
        BinaryTreeTraversal solution = new BinaryTreeTraversal();
        
        // Create test tree
        Integer[] treeArray = {1, 2, 3, 4, 5, null, null};
        TreeNode root = solution.createTree(treeArray);
        
        System.out.println("Binary Tree:");
        System.out.println("       1");
        System.out.println("      / \\");
        System.out.println("     2   3");
        System.out.println("    / \\");
        System.out.println("   4   5");
        System.out.println();
        
        // Test different traversals
        System.out.println("Inorder Traversal (Recursive): " + 
                         solution.inorderTraversalRecursive(root));
        System.out.println("Inorder Traversal (Iterative): " + 
                         solution.inorderTraversalIterative(root));
        System.out.println("Preorder Traversal (Recursive): " + 
                         solution.preorderTraversalRecursive(root));
        System.out.println("Preorder Traversal (Iterative): " + 
                         solution.preorderTraversalIterative(root));
        System.out.println("Postorder Traversal (Recursive): " + 
                         solution.postorderTraversalRecursive(root));
        System.out.println("Postorder Traversal (Iterative): " + 
                         solution.postorderTraversalIterative(root));
        System.out.println("Level Order Traversal: " + 
                         solution.levelOrderTraversal(root));
        System.out.println("Zigzag Level Order: " + 
                         solution.zigzagLevelOrder(root));
        System.out.println("Morris Inorder: " + 
                         solution.morrisInorderTraversal(root));
        System.out.println("Boundary Traversal: " + 
                         solution.boundaryTraversal(root));
        
        // Performance comparison
        Integer[] largeTreeArray = new Integer[10000];
        for (int i = 0; i < largeTreeArray.length; i++) {
            largeTreeArray[i] = i;
        }
        
        TreeNode largeTree = solution.createTree(largeTreeArray);
        
        long startTime = System.currentTimeMillis();
        solution.inorderTraversalRecursive(largeTree);
        long time1 = System.currentTimeMillis() - startTime;
        
        startTime = System.currentTimeMillis();
        solution.inorderTraversalIterative(largeTree);
        long time2 = System.currentTimeMillis() - startTime;
        
        startTime = System.currentTimeMillis();
        solution.morrisInorderTraversal(largeTree);
        long time3 = System.currentTimeMillis() - startTime;
        
        System.out.println("\nPerformance Comparison:");
        System.out.println("Recursive Inorder: " + time1 + "ms");
        System.out.println("Iterative Inorder: " + time2 + "ms");
        System.out.println("Morris Inorder: " + time3 + "ms");
    }
}
