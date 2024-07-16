package p2.binarytree;

/**
 * A class for checking the rules of a red-black tree.
 */
public class RBTreeChecker {

    /**
     * Checks if the given tree satisfies all the rules of a red-black tree.
     *
     * @param rbTree the tree to check.
     * @throws RBTreeException if the tree does not satisfy any of the rules.
     */
    public static void checkAllRules(RBTree<?> rbTree) {
        checkRule1(rbTree);
        checkRule2(rbTree);
        checkRule3(rbTree);
        checkRule4(rbTree);
    }

    /**
     * Checks if the given tree satisfies the first rule of black tree.
     * <p>
     * The first rule of a red-black tree states that every node is either red or black, i.e. its color is not {@code null}.
     *
     * @param rbTree the tree to check.
     * @throws RBTreeException if the tree does not satisfy the rule.
     */
    public static void checkRule1(RBTree<?> rbTree) {
        //TODO: H1 a) - remove if implemented
        checkColor(rbTree.root);
    }

    /**
     * Checks if the given node has a color and its attribute is either red or black, i.e. its color is not {@code null}.
     *
     * @param rbNode the node to check.
     * @throws RBTreeException if color of the given node is neither red nor black.
     */
    private static void checkColor(RBNode<?> rbNode) {
        // Stop Condition: null nodes are valid
        if (rbNode != null) {
            // Check if the node's color is either red or black
            if ((rbNode.getColor() == null) || (!rbNode.isRed() && !rbNode.isBlack()))
                throw new RBTreeException("at least one node has no set color");
            // Recursively check the left and right children
            checkColor(rbNode.getLeft());
            checkColor(rbNode.getRight());
        }

    }

    /**
     * Checks if the given tree satisfies the second rule of black tree.
     * <p>
     * The second rule of a red-black tree states that the root of the tree is black.
     *
     * @param rbTree the tree to check.
     * @throws RBTreeException if the tree does not satisfy the rule.
     */
    public static void checkRule2(RBTree<?> rbTree) {
        //TODO: H1 b) - remove if implemented
        if (rbTree.root != null) // Check if not empty tree / has a root
            if (!rbTree.root.isBlack()) throw new RBTreeException("root is not black"); // Check if root is black
    }

    /**
     * Checks if the given tree satisfies the third rule of black tree.
     * <p>
     * The third rule of a red-black tree states that no red node has a red child.
     *
     * @param rbTree the tree to check.
     * @throws RBTreeException if the tree does not satisfy the rule.
     */
    public static void checkRule3(RBTree<?> rbTree) {
        //TODO: H1 c) - remove if implemented
        checkChildrenOfRed(rbTree.root);
    }

    /**
     * Checks whether the given node, if it is red, has a red child/children.
     *
     * @param rbNode the node to check.
     * @throws RBTreeException if color of the given node is red, and it has a red child/children.
     */
    private static void checkChildrenOfRed(RBNode<?> rbNode) {
        // Stop Condition: null nodes are valid
        if (rbNode != null) {
            RBNode<?> rbLeftChildNode = rbNode.getLeft();
            RBNode<?> rbRightChildNode = rbNode.getRight();
            // If the current node is red, check its children
            if (rbNode.isRed())
                if ((rbLeftChildNode!=null && rbLeftChildNode.isRed()) ||
                    (rbRightChildNode!=null && rbRightChildNode.isRed()))
                    throw new RBTreeException("at least one red node has a red child/children");
            // Recursively check the left and right children
            checkChildrenOfRed(rbLeftChildNode);
            checkChildrenOfRed(rbRightChildNode);
        }
    }

    /**
     * Checks if the given tree satisfies the fourth rule of black tree.
     * <p>
     * The fourth rule of a red-black tree states that all paths from a node to a leave or half-leave contain the same number of black nodes.
     *
     * @param rbTree the tree to check.
     * @throws RBTreeException if the tree does not satisfy the rule.
     */
    public static void checkRule4(RBTree<?> rbTree) {
        //TODO: H1 d) - remove if implemented
        getBlackHeight(rbTree.root);
    }

    /**
     * Returns the black height of the given node
     *
     * @param rbNode the node for which it is calculated.
     * @throws RBTreeException if the children of the given node have different black heights.
     * @return the black height of the given node.
     */
    private static int getBlackHeight(RBNode<?> rbNode) {
        // Stop Condition: null nodes contribute 0 to the black height
        if (rbNode == null) return 0;
        // Recursively check the black height of the left and right children
        int rbLeftChildNodeHeight = getBlackHeight(rbNode.getLeft());
        int rbRightChildNodeHeight = getBlackHeight(rbNode.getRight());
        if (rbLeftChildNodeHeight != rbRightChildNodeHeight) throw new RBTreeException("inconsistent black height");
        return rbLeftChildNodeHeight + (rbNode.isBlack() ? 1 : 0); // Add 1 to black height if the current node is black
    }
}
