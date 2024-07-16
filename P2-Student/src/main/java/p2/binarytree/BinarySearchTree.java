package p2.binarytree;

import p2.SearchTree;

/**
 * An interface for a {@link SearchTree} that uses {@linkplain BinaryNode binary nodes}.
 *
 * @param <T> The type of the elements in the tree.
 * @see SearchTree
 * @see BinaryNode
 * @see AbstractBinarySearchTree
 */
public interface BinarySearchTree<T extends Comparable<T>> extends SearchTree<T> {

    @Override
    BinaryNode<T> search(T value);

    @Override
    BinaryNode<T> findSmallest();

    @Override
    BinaryNode<T> getRoot();
}
