package googleInterview;

/*
question:
https://github.com/jiadaizhao/LintCode/tree/master/0717-Tree%20Longest%20Path%20With%20Same%20Value
https://www.codela.io/challenges/5a5cb027b1c6d9b569a97a93/maximum-path-with-the-same-labels
https://www.geeksforgeeks.org/google-interview-experience-set-7-software-engineering-intern/

We consider an undirected tree with N nodes, numbered from 1 to N,
Additionally, each node also has a label attached to it and the label is an integer value.
Note that different nodes can have identical labels. You need to write a function,
that , given a zero-indexed array A of length N, where A[J] is the label value of
the (J + 1)-th node in the tree, and a zero-indexed array E of length K = (N - 1) * 2
in which the edges of the tree are described (for every 0 <= j <= N -2 values E[2 * J]
and E[2 * J + 1] represents and edge connecting node E[2 * J] with node E[2 * J + 1])，
returns the length of the longest path such that all the nodes on that path have the
same label. Then length of a path if defined as the number of edges in that path.



  Example:
This tree is shown below. A node follows the form label, value.

Give A = [1, 1, 1, 2, 2] and E = [1, 2, 1, 3, 2, 4, 2, 5] described tree appears as follows:

                   1 （value = 1）
                 /   \
    (value = 1) 2     3 (value = 1)
               /  \
 (value = 2)  4     5 (value = 2)

The function should return 2, because the longest path is 2->1->3, and there are 2 edges in this path.

Assume that 1 <= N <= 1000 and each element of the array A is an integer in the range [1, 1000000000].



solution/ article

https://github.com/jiadaizhao/LintCode/tree/master/0717-Tree%20Longest%20Path%20With%20Same%20Value

 */
public class practise02_Longest_Univalue_Path {

    public static void main(String[] args) {
        practise02_Longest_Univalue_Path p = new practise02_Longest_Univalue_Path();
        System.out.println(p.solution(new int[]{1, 1, 1, 2, 2}, new int[]{1, 2, 1, 3, 2, 4, 2, 5}));
        System.out.println(p.solution(new int[]{5,4,5,1,1,5}, new int[]{1, 2, 1, 3, 2, 4, 2, 5, 3, 6}));
        System.out.println(p.solution(new int[]{1,4,5,4,4,5}, new int[]{1, 2, 1, 3, 2, 4, 2, 5, 3, 6}));
//        p.solution(new int[]{11, 12, 13, 14, 15}, new int[]{1, 2, 1, 3, 2, 4, 2, 5});
    }

    /*
     * todo: explain how it works*/
    private int solution(int[] labels, int[] paths) {
        TreeNode root = createInputTree(labels, paths);
        return longestUnivaluePath(root);
    }

    private TreeNode createInputTree(int[] labels, int[] paths) {
        /* assumptions made:
        - the given tree will be a binary tree. as we have (n-1)*2 edges, its a fair assumption
        - the first node is the root of the tree.
          It is true for the given example but this might not be true for every test case. But to find
          the root of an "undirected" binary tree with its edges(undirected) given, seems like a question to me.
          I will try to find the root if I can save time for it.
        - assuming right node of an edge is the child, left is a parent
         */

        // create all nodes
        TreeNode[] nodes = new TreeNode[labels.length];
        for (int i = 0; i < labels.length; i++) {
            nodes[i] = new TreeNode(labels[i]);
        }

        // connect edges
        for (int i = 0; i <= paths.length - 2; i+=2) {
            // assuming right node of an edge is the child, left is a parent
            TreeNode parent = nodes[paths[i]-1];
            TreeNode child = nodes[paths[i + 1]-1];
            if (parent.left == null) {
                parent.left = child;
            } else if (parent.right == null) {
                parent.right = child;
            } else {
                System.out.println("ERROR: this is not a binary tree.");
            }
        }

        /*print tree*/
//        for (TreeNode node : nodes) {
//            System.out.print("val:" + node.val);
//            if (node.left == null) System.out.print(" left:null");
//            else System.out.print(" left:" + node.left.val);
//
//            if (node.right == null) System.out.print(" right:null");
//            else System.out.print(" right:" + node.right.val);
//
//            System.out.println();
//        }

        return nodes[0]; //assuming first node is root
    }

    int ans;
    public int longestUnivaluePath(TreeNode root) {
        ans = 0;
        arrowLength(root);
        return ans;
    }
    public int arrowLength(TreeNode node) {
        if (node == null) return 0;
        int left = arrowLength(node.left);
        int right = arrowLength(node.right);
        int arrowLeft = 0, arrowRight = 0;
        if (node.left != null && node.left.val == node.val) {
            arrowLeft += left + 1;
        }
        if (node.right != null && node.right.val == node.val) {
            arrowRight += right + 1;
        }
        ans = Math.max(ans, arrowLeft + arrowRight);
        return Math.max(arrowLeft, arrowRight);
    }

    class TreeNode {
        TreeNode left;
        TreeNode right;
        int val;

        public TreeNode(int val) {
            this.val = val;
        }

        public StringBuilder toString(StringBuilder prefix, boolean isTail, StringBuilder sb) {
            if(right!=null) {
                right.toString(new StringBuilder().append(prefix).append(isTail ? "│   " : "    "), false, sb);
            }
            sb.append(prefix).append(isTail ? "└── " : "┌── ").append(val).append("\n");
            if(left!=null) {
                left.toString(new StringBuilder().append(prefix).append(isTail ? "    " : "│   "), true, sb);
            }
            return sb;
        }

        @Override
        public String toString() {
            return this.toString(new StringBuilder(), true, new StringBuilder()).toString();
        }    }

    /*
    Time Complexity: O(N), where NN is the number of nodes in the tree. We process every node once.

    Space Complexity: O(H), where HH is the height of the tree. Our recursive call stack could be up to HH layers deep.
    */
}
