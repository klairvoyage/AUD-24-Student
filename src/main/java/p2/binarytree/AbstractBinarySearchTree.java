package p2.binarytree;

import p2.SearchTree;

import java.util.List;
import java.util.Stack;
import java.util.function.Predicate;

import static org.tudalgo.algoutils.student.Student.crash;

/**
 * A base implementation of a binary search tree containing common methods.
 * <p>
 * It contains the root node of the tree and provides methods for searching and inserting elements.
 * <p>
 * It assumes that only binary nodes are used, i.e. every node contains exactly one key and has at most two children,
 * where the left child is smaller than the parent and the right child is greater than the parent.
 *
 * @param <T> the type of the keys in the tree.
 * @param <N> the type of the nodes in the tree, e.g., {@link BSTNode} or {@link RBNode}.
 * @see SearchTree
 * @see AbstractBinaryNode
 */
public abstract class AbstractBinarySearchTree<T extends Comparable<T>, N extends AbstractBinaryNode<T, N>> implements BinarySearchTree<T> {

    /**
     * The root node of the tree.
     */
    protected N root;

    @Override
    public N search(T value) {

        N x = root;

        while (x != null && x.getKey().compareTo(value) != 0) {
            if (x.getKey().compareTo(value) > 0) {
                x = x.getLeft();
            } else {
                x = x.getRight();
            }
        }

        return x;
    }

    /**
     * Inserts the given node into the tree.
     *
     * @param node      the node to insert.
     * @param initialPX The initial value used for the pointer to the parent node.
     *                  This is required for implementations that use a sentinel node. For normal trees, this value
     *                  should be {@code null}.
     */
    protected void insert(N node, N initialPX) {
        //TODO: H2 a) - remove if implemented
        N x = root; // Start from the root of the tree
        N px = initialPX; // Initialize px with the initial parent value (null for normal trees)

        // Traverse the tree to find the appropriate position for the new node
        while (x != null) {
            // Update px to the current node
            px = x;
            // If the new node's key is smaller, move to the left child
            if (x.getKey().compareTo(node.getKey()) > 0) x = x.getLeft();
            else x = x.getRight(); // Otherwise, move to the right child
        }
        node.setParent(px); // Set the parent of the new node to px

        // If the parent is still the initialPX, it means the tree was empty and the new node becomes the root
        if (px == initialPX) root = node;
        else { // Attach the new node to the correct position in the tree
            // If the new node's key is smaller, set it as the left child
            if (px.getKey().compareTo(node.getKey()) > 0) px.setLeft(node);
            // If the new node's key is greater or equal, set it as the right child
            else px.setRight(node);
        }
    }

    /**
     * Adds all elements in the subtree represented by the given node to the given list.
     * <p>
     * The elements are added in ascending order.
     * The method adds at most {@code max} elements.
     * The method stops traversing the tree if the predicate returns {@code false} for one of the elements and does
     * not add any further elements. The first element which did not satisfy the predicate is also excluded.
     * It assumes that the predicate returns {@code false} for all greater values once it returned {@code false} for
     * one value, i.e. it represents a limit check.
     *
     * @param node   The root of the subtree to traverse.
     * @param result The list to store the elements in.
     * @param max    The maximum number of elements to include in the result.
     * @param limit  The predicate to test the elements against. If the predicate returns {@code false} for an element,
     *               the traversal stops.
     */
    protected void inOrder(N node, List<? super T> result, int max, Predicate<? super T> limit) {
        crash(); //TODO: H3 a) - remove if implemented
    }

    /**
     * Adds all elements in the tree that are greater than or equal to the given node to the given list.
     * <p>
     * The elements are added in ascending order.
     * The method adds at most {@code max} elements.
     * The method stops traversing the tree if the predicate returns {@code false} for one of the elements and does
     * not add any further elements. The first element which did not satisfy the predicate is also excluded.
     * It assumes that the predicate returns {@code false} for all greater values once it returned {@code false} for
     * one value, i.e. it represents a limit check.
     *
     * @param node   The node to start the search from. The node itself is included in the search.
     * @param result The list to store the elements in.
     * @param max    The maximum number of elements to include in the result.
     * @param limit  The predicate to test the elements against. If the predicate returns {@code false} for an element,
     *               the traversal stops.
     */
    protected void findNext(N node, List<? super T> result, int max, Predicate<? super T> limit) {
        crash(); //TODO: H13 b) - remove if implemented
    }

    @Override
    public N findSmallest() {
        N x = root;
        while (x.hasLeft()) {
            x = x.getLeft();
        }
        return x;
    }

    @Override
    public N getRoot() {
        return root;
    }

    /**
     * Creates a new node with the given key.
     * <p>
     * The type of the node is determined by the concrete implementation. If the implementation uses additional
     * information within the node, a standard value is used for them, e.g., red for the color of a node in a
     * red-black tree.
     *
     * @param key the key of the new node.
     * @return a new node with the given key.
     */
    protected abstract N createNode(T key);

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        if (root == null) {
            sb.append("[]");
        } else {
            root.buildString(sb);
        }

        return sb.toString();
    }

}
