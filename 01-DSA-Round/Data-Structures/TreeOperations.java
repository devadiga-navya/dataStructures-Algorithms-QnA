import java.util.*;

/**
 * Tree Operations Problem - Principal Engineer Level Solution
 * 
 * Problem: Implement various tree operations and algorithms including
 * BST operations, tree traversals, tree construction, and advanced tree patterns.
 * 
 * Example:
 * Input: [1, 2, 3, 4, 5, 6, 7]
 * Level Order: [[1], [2, 3], [4, 5, 6, 7]]
 * 
 * Requirements:
 * - Handle edge cases (null root, empty tree, single node)
 * - Provide optimal solutions with different time complexities
 * - Include alternative approaches for different scenarios
 * - Clean, production-ready code
 */
public class TreeOperations {
    
    // TreeNode class
    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        
        TreeNode(int val) {
            this.val = val;
        }
    }
    
    /**
     * Binary Search Tree Implementation
     * Time Complexity: O(h) for most operations where h is height
     * Space Complexity: O(n)
     */
    public static class BST {
        private TreeNode root;
        
        public BST() {
            this.root = null;
        }
        
        public void insert(int value) {
            root = insertRec(root, value);
        }
        
        private TreeNode insertRec(TreeNode root, int value) {
            if (root == null) {
                return new TreeNode(value);
            }
            
            if (value < root.val) {
                root.left = insertRec(root.left, value);
            } else if (value > root.val) {
                root.right = insertRec(root.right, value);
            }
            
            return root;
        }
        
        public boolean search(int value) {
            return searchRec(root, value);
        }
        
        private boolean searchRec(TreeNode root, int value) {
            if (root == null || root.val == value) {
                return root != null;
            }
            
            if (value < root.val) {
                return searchRec(root.left, value);
            }
            
            return searchRec(root.right, value);
        }
        
        public void delete(int value) {
            root = deleteRec(root, value);
        }
        
        private TreeNode deleteRec(TreeNode root, int value) {
            if (root == null) {
                return null;
            }
            
            if (value < root.val) {
                root.left = deleteRec(root.left, value);
            } else if (value > root.val) {
                root.right = deleteRec(root.right, value);
            } else {
                // Node with only one child or no child
                if (root.left == null) {
                    return root.right;
                } else if (root.right == null) {
                    return root.left;
                }
                
                // Node with two children: Get the inorder successor (smallest in right subtree)
                root.val = minValue(root.right);
                root.right = deleteRec(root.right, root.val);
            }
            
            return root;
        }
        
        private int minValue(TreeNode root) {
            int minv = root.val;
            while (root.left != null) {
                minv = root.left.val;
                root = root.left;
            }
            return minv;
        }
        
        public TreeNode getRoot() {
            return root;
        }
    }
    
    /**
     * Check if Binary Tree is BST
     * Time Complexity: O(n)
     * Space Complexity: O(h) - recursion stack
     */
    public boolean isValidBST(TreeNode root) {
        return isValidBSTHelper(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }
    
    private boolean isValidBSTHelper(TreeNode root, long min, long max) {
        if (root == null) {
            return true;
        }
        
        if (root.val <= min || root.val >= max) {
            return false;
        }
        
        return isValidBSTHelper(root.left, min, root.val) &&
               isValidBSTHelper(root.right, root.val, max);
    }
    
    /**
     * Find Lowest Common Ancestor in BST
     * Time Complexity: O(h)
     * Space Complexity: O(1)
     */
    public TreeNode lowestCommonAncestorBST(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null) {
            return null;
        }
        
        if (root.val > p.val && root.val > q.val) {
            return lowestCommonAncestorBST(root.left, p, q);
        }
        
        if (root.val < p.val && root.val < q.val) {
            return lowestCommonAncestorBST(root.right, p, q);
        }
        
        return root;
    }
    
    /**
     * Find Lowest Common Ancestor in Binary Tree
     * Time Complexity: O(n)
     * Space Complexity: O(h)
     */
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null || root == p || root == q) {
            return root;
        }
        
        TreeNode left = lowestCommonAncestor(root.left, p, q);
        TreeNode right = lowestCommonAncestor(root.right, p, q);
        
        if (left != null && right != null) {
            return root;
        }
        
        return left != null ? left : right;
    }
    
    /**
     * Find Kth Smallest Element in BST
     * Time Complexity: O(k)
     * Space Complexity: O(h)
     */
    public int kthSmallest(TreeNode root, int k) {
        Stack<TreeNode> stack = new Stack<>();
        TreeNode current = root;
        int count = 0;
        
        while (current != null || !stack.isEmpty()) {
            while (current != null) {
                stack.push(current);
                current = current.left;
            }
            
            current = stack.pop();
            count++;
            
            if (count == k) {
                return current.val;
            }
            
            current = current.right;
        }
        
        return -1; // k is larger than number of nodes
    }
    
    /**
     * Find Kth Largest Element in BST
     * Time Complexity: O(k)
     * Space Complexity: O(h)
     */
    public int kthLargest(TreeNode root, int k) {
        Stack<TreeNode> stack = new Stack<>();
        TreeNode current = root;
        int count = 0;
        
        while (current != null || !stack.isEmpty()) {
            while (current != null) {
                stack.push(current);
                current = current.right;
            }
            
            current = stack.pop();
            count++;
            
            if (count == k) {
                return current.val;
            }
            
            current = current.left;
        }
        
        return -1;
    }
    
    /**
     * Convert Sorted Array to BST
     * Time Complexity: O(n)
     * Space Complexity: O(log n) - recursion stack
     */
    public TreeNode sortedArrayToBST(int[] nums) {
        if (nums == null || nums.length == 0) {
            return null;
        }
        
        return sortedArrayToBSTHelper(nums, 0, nums.length - 1);
    }
    
    private TreeNode sortedArrayToBSTHelper(int[] nums, int start, int end) {
        if (start > end) {
            return null;
        }
        
        int mid = start + (end - start) / 2;
        TreeNode root = new TreeNode(nums[mid]);
        
        root.left = sortedArrayToBSTHelper(nums, start, mid - 1);
        root.right = sortedArrayToBSTHelper(nums, mid + 1, end);
        
        return root;
    }
    
    /**
     * Convert BST to Sorted Array
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     */
    public int[] bstToSortedArray(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        inorderTraversal(root, result);
        
        return result.stream().mapToInt(Integer::intValue).toArray();
    }
    
    private void inorderTraversal(TreeNode root, List<Integer> result) {
        if (root == null) {
            return;
        }
        
        inorderTraversal(root.left, result);
        result.add(root.val);
        inorderTraversal(root.right, result);
    }
    
    /**
     * Check if Two Trees are Identical
     * Time Complexity: O(n)
     * Space Complexity: O(h)
     */
    public boolean isIdentical(TreeNode root1, TreeNode root2) {
        if (root1 == null && root2 == null) {
            return true;
        }
        
        if (root1 == null || root2 == null) {
            return false;
        }
        
        return root1.val == root2.val &&
               isIdentical(root1.left, root2.left) &&
               isIdentical(root1.right, root2.right);
    }
    
    /**
     * Check if Tree is Symmetric
     * Time Complexity: O(n)
     * Space Complexity: O(h)
     */
    public boolean isSymmetric(TreeNode root) {
        if (root == null) {
            return true;
        }
        
        return isSymmetricHelper(root.left, root.right);
    }
    
    private boolean isSymmetricHelper(TreeNode left, TreeNode right) {
        if (left == null && right == null) {
            return true;
        }
        
        if (left == null || right == null) {
            return false;
        }
        
        return left.val == right.val &&
               isSymmetricHelper(left.left, right.right) &&
               isSymmetricHelper(left.right, right.left);
    }
    
    /**
     * Find Maximum Path Sum
     * Time Complexity: O(n)
     * Space Complexity: O(h)
     */
    public int maxPathSum(TreeNode root) {
        int[] maxSum = {Integer.MIN_VALUE};
        maxPathSumHelper(root, maxSum);
        return maxSum[0];
    }
    
    private int maxPathSumHelper(TreeNode root, int[] maxSum) {
        if (root == null) {
            return 0;
        }
        
        int leftSum = Math.max(0, maxPathSumHelper(root.left, maxSum));
        int rightSum = Math.max(0, maxPathSumHelper(root.right, maxSum));
        
        maxSum[0] = Math.max(maxSum[0], root.val + leftSum + rightSum);
        
        return root.val + Math.max(leftSum, rightSum);
    }
    
    /**
     * Serialize and Deserialize Binary Tree
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     */
    public String serialize(TreeNode root) {
        if (root == null) {
            return "null";
        }
        
        return root.val + "," + serialize(root.left) + "," + serialize(root.right);
    }
    
    public TreeNode deserialize(String data) {
        Queue<String> queue = new LinkedList<>(Arrays.asList(data.split(",")));
        return deserializeHelper(queue);
    }
    
    private TreeNode deserializeHelper(Queue<String> queue) {
        String val = queue.poll();
        
        if (val.equals("null")) {
            return null;
        }
        
        TreeNode root = new TreeNode(Integer.parseInt(val));
        root.left = deserializeHelper(queue);
        root.right = deserializeHelper(queue);
        
        return root;
    }
    
    /**
     * Find All Paths from Root to Leaf
     * Time Complexity: O(n)
     * Space Complexity: O(h)
     */
    public List<List<Integer>> findAllPaths(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        findAllPathsHelper(root, new ArrayList<>(), result);
        return result;
    }
    
    private void findAllPathsHelper(TreeNode root, List<Integer> current, List<List<Integer>> result) {
        if (root == null) {
            return;
        }
        
        current.add(root.val);
        
        if (root.left == null && root.right == null) {
            result.add(new ArrayList<>(current));
        } else {
            findAllPathsHelper(root.left, current, result);
            findAllPathsHelper(root.right, current, result);
        }
        
        current.remove(current.size() - 1);
    }
    
    /**
     * Check if Tree is Balanced
     * Time Complexity: O(n)
     * Space Complexity: O(h)
     */
    public boolean isBalanced(TreeNode root) {
        return checkHeight(root) != -1;
    }
    
    private int checkHeight(TreeNode root) {
        if (root == null) {
            return 0;
        }
        
        int leftHeight = checkHeight(root.left);
        if (leftHeight == -1) {
            return -1;
        }
        
        int rightHeight = checkHeight(root.right);
        if (rightHeight == -1) {
            return -1;
        }
        
        if (Math.abs(leftHeight - rightHeight) > 1) {
            return -1;
        }
        
        return Math.max(leftHeight, rightHeight) + 1;
    }
    
    /**
     * Flatten Binary Tree to Linked List
     * Time Complexity: O(n)
     * Space Complexity: O(h)
     */
    public void flatten(TreeNode root) {
        if (root == null) {
            return;
        }
        
        flatten(root.left);
        flatten(root.right);
        
        TreeNode left = root.left;
        TreeNode right = root.right;
        
        root.left = null;
        root.right = left;
        
        TreeNode current = root;
        while (current.right != null) {
            current = current.right;
        }
        
        current.right = right;
    }
    
    // Utility method to create tree from array
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
        TreeOperations solution = new TreeOperations();
        
        // Test BST operations
        System.out.println("=== BST Operations Test ===");
        BST bst = new BST();
        bst.insert(5);
        bst.insert(3);
        bst.insert(7);
        bst.insert(1);
        bst.insert(9);
        
        System.out.println("Search 3: " + bst.search(3));
        System.out.println("Search 10: " + bst.search(10));
        
        // Test tree validation
        System.out.println("\n=== Tree Validation Test ===");
        Integer[] validBST = {5, 3, 7, 1, 4, 6, 8};
        TreeNode validTree = solution.createTree(validBST);
        System.out.println("Is valid BST: " + solution.isValidBST(validTree));
        
        Integer[] invalidBST = {5, 3, 7, 1, 6, 6, 8};
        TreeNode invalidTree = solution.createTree(invalidBST);
        System.out.println("Is valid BST: " + solution.isValidBST(invalidTree));
        
        // Test LCA
        System.out.println("\n=== LCA Test ===");
        TreeNode node1 = validTree.left.left; // 1
        TreeNode node2 = validTree.left.right; // 4
        TreeNode lca = solution.lowestCommonAncestorBST(validTree, node1, node2);
        System.out.println("LCA of 1 and 4: " + lca.val);
        
        // Test Kth smallest
        System.out.println("\n=== Kth Smallest Test ===");
        System.out.println("3rd smallest: " + solution.kthSmallest(validTree, 3));
        System.out.println("5th smallest: " + solution.kthSmallest(validTree, 5));
        
        // Test sorted array to BST
        System.out.println("\n=== Sorted Array to BST Test ===");
        int[] sortedArray = {1, 2, 3, 4, 5, 6, 7};
        TreeNode bstFromArray = solution.sortedArrayToBST(sortedArray);
        int[] resultArray = solution.bstToSortedArray(bstFromArray);
        System.out.println("Original: " + Arrays.toString(sortedArray));
        System.out.println("BST to array: " + Arrays.toString(resultArray));
        
        // Test tree symmetry
        System.out.println("\n=== Tree Symmetry Test ===");
        Integer[] symmetricTree = {1, 2, 2, 3, 4, 4, 3};
        TreeNode symTree = solution.createTree(symmetricTree);
        System.out.println("Is symmetric: " + solution.isSymmetric(symTree));
        
        // Test max path sum
        System.out.println("\n=== Max Path Sum Test ===");
        Integer[] pathSumTree = {-10, 9, 20, null, null, 15, 7};
        TreeNode pathTree = solution.createTree(pathSumTree);
        System.out.println("Max path sum: " + solution.maxPathSum(pathTree));
        
        // Test serialize/deserialize
        System.out.println("\n=== Serialize/Deserialize Test ===");
        String serialized = solution.serialize(validTree);
        System.out.println("Serialized: " + serialized);
        TreeNode deserialized = solution.deserialize(serialized);
        System.out.println("Trees are identical: " + solution.isIdentical(validTree, deserialized));
        
        // Test find all paths
        System.out.println("\n=== Find All Paths Test ===");
        List<List<Integer>> allPaths = solution.findAllPaths(validTree);
        System.out.println("All paths from root to leaf:");
        for (List<Integer> path : allPaths) {
            System.out.println(path);
        }
        
        // Test tree balance
        System.out.println("\n=== Tree Balance Test ===");
        System.out.println("Is balanced: " + solution.isBalanced(validTree));
        
        // Performance test
        System.out.println("\n=== Performance Test ===");
        Integer[] largeTreeArray = new Integer[10000];
        for (int i = 0; i < largeTreeArray.length; i++) {
            largeTreeArray[i] = i;
        }
        
        TreeNode largeTree = solution.createTree(largeTreeArray);
        
        long startTime = System.currentTimeMillis();
        solution.isValidBST(largeTree);
        long time1 = System.currentTimeMillis() - startTime;
        
        startTime = System.currentTimeMillis();
        solution.maxPathSum(largeTree);
        long time2 = System.currentTimeMillis() - startTime;
        
        System.out.println("BST validation: " + time1 + "ms");
        System.out.println("Max path sum: " + time2 + "ms");
    }
}
