package p2.binarytree;

import p2.Node;

import java.util.List;
import java.util.function.Predicate;

/**
 * A simple implementation of a binary search tree.
 *
 * @param <T> The type of the keys in the tree.
 * @see AbstractBinarySearchTree
 */
public class SimpleBinarySearchTree<T extends Comparable<T>> extends AbstractBinarySearchTree<T, BSTNode<T>> {

    @Override
    public void insert(T value) {
        insert(createNode(value), null);
    }

    @Override
    public void inOrder(Node<T> node, List<? super T> result, int max, Predicate<? super T> predicate) {
        if (node instanceof BSTNode<T> bstNode) {
            super.inOrder(bstNode, result, max, predicate);
            return;
        }

        if (node != null) throw new IllegalArgumentException("Node must be of type BSTNode");
    }

    @Override
    public void findNext(Node<T> node, List<? super T> result, int max, Predicate<? super T> predicate) {
        if (node instanceof BSTNode<T> rbNode) {
            super.findNext(rbNode, result, max, predicate);
            return;
        }
        if (node != null) throw new IllegalArgumentException("Node must be of type BSTNode");
    }

    @Override
    protected BSTNode<T> createNode(T key) {
        return new BSTNode<>(key);
    }
}
