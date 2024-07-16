package p2.binarytree;

import p2.Node;

import java.util.List;
import java.util.function.Predicate;

import static org.tudalgo.algoutils.student.Student.crash;

/**
 * An implementation of a red-black tree.
 * <p>
 * A red-black tree is a self-balancing binary search tree. It guarantees that the searching and inserting operation
 * have a logarithmic time complexity.
 *
 * @param <T> The type of the keys in the tree.
 * @see AbstractBinarySearchTree
 * @see RBNode
 */
public class RBTree<T extends Comparable<T>> extends AbstractBinarySearchTree<T, RBNode<T>> {

    /**
     * The sentinel node of the tree.
     * <p>
     * The sentinel node is a special node that is used to simplify the implementation of the tree. It is a black
     * node that is used as the parent of the root node and is its own child. It is not considered part of the tree.
     */
    protected final RBNode<T> sentinel = new RBNode<>(null, Color.BLACK);

    /**
     * Creates a new, empty red-black tree.
     */
    public RBTree() {
        sentinel.setParent(sentinel);
        sentinel.setLeft(sentinel);
        sentinel.setRight(sentinel);
    }

    @Override
    public void insert(T value) {
        //TODO: H2 c) - remove if implemented
        RBNode<T> newNode = createNode(value); // Create a new node with the given value using the createNode method
        super.insert(newNode, sentinel); // Insert new node into the tree; sentinel node passed as the initial parent
        fixColorsAfterInsertion(newNode); // Fix colors to ensure red-black tree properties are maintained after insert
    }

    /**
     * Ensures that the red-black tree properties are maintained after inserting a new node, which might have
     * added a red node as a child of another red node.
     *
     * @param z The node that was inserted.
     */
    protected void fixColorsAfterInsertion(RBNode<T> z) {
        //TODO: H2 b) - remove if implemented
        while (z!=null && z.getParent()!=null && z.getParent().isRed()) { // Loop to fix as long as there is a violation
            RBNode<T> parent = z.getParent(); // parent
            RBNode<T> grandParent = parent.getParent(); // grandparent
            // Check if the parent is the left child of the grandparent
            if (grandParent!=null && grandParent.getLeft()!=null && parent.getKey()==grandParent.getLeft().getKey()) {
                RBNode<T> uncle = grandParent.getRight(); // uncle
                // Case 1: Uncle is red
                if (uncle!=null && uncle.isRed()) {
                    parent.setColor(Color.BLACK); // Recolor the parent to black
                    uncle.setColor(Color.BLACK); // Recolor the uncle to black
                    grandParent.setColor(Color.RED); // Recolor the grandparent to red
                    z = grandParent; // Move up to the grandparent
                } else {
                    // Case 2: Node is the right child
                    if (parent.getRight()!=null && z.getKey()==parent.getRight().getKey()) {
                        z = parent; // Move up to the parent
                        rotateLeft(z); // Perform left rotation
                    }
                    // Case 3: Node is the left child
                    z.getParent().setColor(Color.BLACK); // Recolor the parent to black
                    z.getParent().getParent().setColor(Color.RED); // Recolor the grandparent to red
                    rotateRight(z.getParent().getParent()); // Perform right rotation on grandparent
                }
            } else { // Symmetric cases for when the parent is the right child of the grandparent
                RBNode<T> uncle = grandParent.getLeft(); // assert grandParent != null;
                // Case 1: Uncle is red
                if (uncle!=null && uncle.isRed()) {
                    parent.setColor(Color.BLACK); // Recolor the parent to black
                    uncle.setColor(Color.BLACK); // Recolor the uncle to black
                    grandParent.setColor(Color.RED); // Recolor the grandparent to red
                    z = grandParent; // Move up to the grandparent
                } else {
                    // Case 2: Node is the left child
                    if (parent.getLeft()!=null && z==parent.getLeft()) {
                        z = parent; // Move up to the parent
                        rotateRight(z); // Perform right rotation
                    }
                    // Case 3: Node is the right child
                    z.getParent().setColor(Color.BLACK); // Recolor the parent to black
                    z.getParent().getParent().setColor(Color.RED); // Recolor the grandparent to red
                    rotateLeft(z.getParent().getParent()); // Perform left rotation on grandparent
                }
            }
        }
        if (root!=null) root.setColor(Color.BLACK); // Ensure the root is always black
    }

    /**
     * Rotates the given node to the left by making it the left child of its previous right child.
     * <p>
     * The method assumes that the right child of the given node is not {@code null}.
     *
     * @param x The node to rotate.
     */
    protected void rotateLeft(RBNode<T> x) {
        //TODO: H2 b) - remove if implemented
        if (x.getRight() != null) {
            RBNode<T> y = x.getRight(); // y is x's right child
            x.setRight(y.getLeft()); // x's right child becomes y's left child
            if (y.getLeft() != null) y.getLeft().setParent(x); // Set x as the parent of y's left child
            y.setParent(x.getParent()); // Set y's parent to be x's parent

            // If x was the root, now y becomes the root
            if (x.getParent() == sentinel) root = y;
            else {
                // If x was the left child, y becomes the left child
                if (x == x.getParent().getLeft()) x.getParent().setLeft(y);
                // If x was the right child, y becomes the right child
                else x.getParent().setRight(y);
            }

            y.setLeft(x); // x becomes the left child of y
            x.setParent(y); // y becomes the parent of x
        }
    }

    /**
     * Rotates the given node to the right by making it the right child of its previous left child.
     * <p>
     * The method assumes that the left child of the given node is not {@code null}.
     *
     * @param x The node to rotate.
     */
    protected void rotateRight(RBNode<T> x) {
        //TODO: H2 b) - remove if implemented
        if (x.getLeft() != null) {
            RBNode<T> y = x.getLeft(); // y is x's left child
            x.setLeft(y.getRight()); // x's left child becomes y's right child
            if (y.getRight() != null) y.getRight().setParent(x); // Set x as the parent of y's right child
            y.setParent(x.getParent()); // Set y's parent to be x's parent

            // If x was the root, now y becomes the root
            if (x.getParent() == sentinel) root = y;
            else {
                // If x was the right child, y becomes the right child
                if (x == x.getParent().getRight()) x.getParent().setRight(y);
                // If x was the left child, y becomes the left child
                else x.getParent().setLeft(y);
            }

            y.setRight(x); // x becomes the right child of y
            x.setParent(y); // y becomes the parent of x
        }
    }

    @Override
    public void inOrder(Node<T> node, List<? super T> result, int max, Predicate<? super T> predicate) {
        if (node instanceof RBNode<T> rbNode) {
            super.inOrder(rbNode, result, max, predicate);
            return;
        }

        if (node != null) throw new IllegalArgumentException("Node must be of type RBNode");
    }

    @Override
    public void findNext(Node<T> node, List<? super T> result, int max, Predicate<? super T> predicate) {
        if (node instanceof RBNode<T> rbNode) {
            super.findNext(rbNode, result, max, predicate);
            return;
        }
        if (node != null) throw new IllegalArgumentException("Node must be of type RBNode");
    }

    /**
     * Joins this red-black tree with another red-black tree by inserting a join-key.
     * <p>
     * The method assumes that both trees are non-empty and every element in this tree is less than every element in
     * the other tree. Additionally, it assumes that the join-key is greater than every element in this tree and less
     * than every element in the other tree.
     * <p>
     * The method modifies the tree in place, so that the other tree is merged into this tree. The other tree is
     * effectively destroyed in the process, i.e. it is not guaranteed that it is a valid red-black tree anymore.
     * <p>
     * It works by first finding two nodes in both trees with the same black height. For the tree with the smaller black
     * height, this is the node root node. For the tree with the larger black height, this is the largest or smallest
     * node with the same black height as the root node of the other tree. Then, it creates a new, red, node with the
     * join-key as the key and makes it the parent of the two previously found nodes. Finally, it fixes the colors of
     * the tree to ensure that the red-black tree properties are maintained.
     *
     * @param other   The other red-black tree to join with this tree.
     * @param joinKey The key to insert into the tree to join the two trees.
     */
    public void join(RBTree<T> other, T joinKey) {
        crash(); //TODO: H4 c) - remove if implemented
    }

    /**
     * Returns the black height of the tree, i.e. the number of black nodes on a path from the root to a leaf.
     *
     * @return the black height of the tree.
     */
    public int blackHeight() {
        //TODO: H4 a) - remove if implemented
        return blackHeight(root); // Call the helper method to calculate the black height from the root of the tree
    }

    /**
     * Returns the black height of the given node, i.e. the number of black nodes on a path from the given node to a leaf.
     *
     * @param rbNode the node for which it is calculated.
     * @return the black height of the given node.
     */
    private int blackHeight(RBNode<T> rbNode) {
        if (rbNode == null) return 0; // Stop Condition: If node is null; return 0 since it doesn't contribute to height
        // If the black heights of the left and right subtrees are not equal, the tree violates the red-black properties
        if (blackHeight(rbNode.getLeft()) != blackHeight(rbNode.getRight())) throw new RBTreeException("checkAllRules");
        return blackHeight(rbNode.getLeft()) + (rbNode.isBlack() ? 1 : 0); // increments height if current node is black
    }

    /**
     * Finds a black node with the given black height in the tree.
     * <p>
     * Depending on the value of the {@code findSmallest} parameter, the method finds the smallest or largest node with the
     * target black height.
     * <p>
     * It assumes that the tree is non-empty and that there is a node with the target black height.
     *
     * @param targetBlackHeight The target black height to find a node with.
     * @param totalBlackHeight  The total black height of the tree.
     * @param findSmallest      Whether to find the smallest or largest node with the target black height.
     * @return A black node with the target black height.
     */
    public RBNode<T> findBlackNodeWithBlackHeight(int targetBlackHeight, int totalBlackHeight, boolean findSmallest) {
        //TODO: H4 b) - remove if implemented
        if (targetBlackHeight>=0 && targetBlackHeight<=totalBlackHeight) // Check if target height is within valid range
            // Call the helper method to find the node with the target black height
            return loremIpsumHelper(root, targetBlackHeight, findSmallest);
        else
            // Throw an exception if the target black height is not valid
            throw new IllegalArgumentException("Target black height (" + targetBlackHeight + ") exceeds total black height!");
    }

    /**
     * Recursively finds a black node with the given black height in the tree.
     * <p>
     * Depending on the value of the {@code findSmallest} parameter, it will return either the smallest or
     * the largest node that meets this criterion.
     * <p>
     * It assumes that the tree is non-empty and that there is a node with the target black height.
     *
     * @param rbNode            The current node in the tree.
     * @param targetBlackHeight The target black height to find a node with.
     * @param findSmallest      Whether to find the smallest or largest node with the target black height.
     * @return The black node with the specified black height.
     */
    private RBNode<T> loremIpsumHelper(RBNode<T> rbNode, int targetBlackHeight, boolean findSmallest) {
        if (rbNode == null) return null; // Stop Condition: if the node is null, return null

        // Variable to store the result node
        RBNode<T> result = null;

        // If the current node is black and has the target black height, it is a candidate
        if (rbNode.isBlack() && blackHeight(rbNode) == targetBlackHeight) result = rbNode; // candidate

        // Recursively find the target node in the left subtree
        RBNode<T> leftSubtree = loremIpsumHelper(rbNode.getLeft(), targetBlackHeight, findSmallest);
        // Recursively find the target node in the right subtree
        RBNode<T> rightSubtree = loremIpsumHelper(rbNode.getRight(), targetBlackHeight, findSmallest);

        // Determine the smallest or largest node based on the findSmallest flag
        if (findSmallest) {
            // For finding the smallest node, prefer the left subtree
            if (leftSubtree != null) result = leftSubtree;
            // If no left subtree result and no current result, prefer the right subtree
            else if (result == null && rightSubtree != null) result = rightSubtree;
        } else {
            // For finding the largest node, prefer the right subtree
            if (rightSubtree != null) result = rightSubtree;
            // If no right subtree result and no current result, prefer the left subtree
            else if (result == null && leftSubtree != null) result = leftSubtree;
        }
        return result;
    }

    @Override
    protected RBNode<T> createNode(T key) {
        return new RBNode<>(key, Color.RED);
    }
}
