package p2.binarytree.implementation;

import p2.binarytree.BSTNode;
import p2.binarytree.SimpleBinarySearchTree;

public class TestBST<T extends Comparable<T>> extends SimpleBinarySearchTree<T> {

    @Override
    protected BSTNode<T> createNode(T key) {
        return new TestBSTNode<>(key);
    }
}
