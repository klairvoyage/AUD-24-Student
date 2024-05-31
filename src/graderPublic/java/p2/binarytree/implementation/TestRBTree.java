package p2.binarytree.implementation;

import p2.binarytree.Color;
import p2.binarytree.RBNode;
import p2.binarytree.RBTree;

public class TestRBTree<T extends Comparable<T>> extends RBTree<T> {

    @Override
    protected RBNode<T> createNode(T key) {
        return new TestRBNode<>(key, Color.RED);
    }
}
