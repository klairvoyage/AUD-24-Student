package p2.binarytree;

import p2.Node;

/**
 * Interface for a node in a binary search tree.
 * <p>
 * It contains exactly one key and has at most two children, where the left child is smaller than the parent and the
 * right child is greater than the parent. It also contains a reference to the parent node.
 * <p>
 * The key can be of any type that implements the {@link Comparable} interface.
 *
 * @param <T> the type of the key in the node.
 * @see AbstractBinaryNode
 * @see Node
 * @see AbstractBinarySearchTree
 */
public interface BinaryNode<T extends Comparable<T>> extends Node<T> {

    /**
     * Returns the key of the node.
     *
     * @return the key of the node.
     */
    T getKey();

    @SuppressWarnings("unchecked")
    @Override
    default T[] getKeys() {
        return (T[]) new Comparable[]{getKey()};
    }

    /**
     * Returns the left child of the node.
     * If the node has no left child, {@code null} is returned.
     *
     * @return the left child of the node.
     */
    BinaryNode<T> getLeft();

    /**
     * Checks whether the node has a left child.
     *
     * @return {@code true} if the node has a left child, {@code false} otherwise.
     */
    boolean hasLeft();

    /**
     * Returns the right child of the node.
     * If the node has no right child, {@code null} is returned.
     *
     * @return the right child of the node.
     */
    BinaryNode<T> getRight();

    /**
     * Checks whether the node has a right child.
     *
     * @return {@code true} if the node has a right child, {@code false} otherwise.
     */
    boolean hasRight();

    /**
     * Returns the parent of the node.
     * If the node has no parent, {@code null} is returned.
     *
     * @return the parent of the node.
     */
    BinaryNode<T> getParent();
}
