package p2.binarytree.implementation;

import p2.binarytree.Color;
import p2.binarytree.RBNode;

public class TestRBNode<T extends Comparable<T>> extends RBNode<T> {

    private int getLeftCount = 0;
    private int getRightCount = 0;

    private static boolean counting = false;

    public TestRBNode(T key, Color color) {
        super(key, color);
    }

    public static void startCounting() {
        counting = true;
    }

    public static void stopCounting() {
        counting = false;
    }

    @Override
    public RBNode<T> getLeft() {
        if (counting) {
            getLeftCount++;
        }
        return super.getLeft();
    }

    @Override
    public RBNode<T> getRight() {
        if (counting) getRightCount++;
        return super.getRight();
    }

    public int getLeftCount() {
        return getLeftCount;
    }

    public int getRightCount() {
        return getRightCount;
    }
}
