package p2.gui;

import javafx.application.Platform;
import p2.binarytree.BSTNode;
import p2.binarytree.SimpleBinarySearchTree;

import java.util.Arrays;

/**
 * A simple binary search tree that can be animated.
 * <p>
 * The animation stops after each invocation of the following methods:
 * <ul>
 *     <li>{@link BSTNode#getLeft()}</li>
 *     <li>{@link BSTNode#setLeft(BSTNode)}</li>
 *     <li>{@link BSTNode#getRight()}</li>
 *     <li>{@link BSTNode#setRight(BSTNode)}</li>
 *     <li>{@link BSTNode#getParent()}</li>
 *     <li>{@link BSTNode#setParent(BSTNode)}</li>
 * </ul>
 *
 * @param <T> The type of the elements in the tree.
 * @see SimpleBinarySearchTree
 * @see AnimatedBinaryTree
 */
@SuppressWarnings({"DuplicatedCode", "JavadocReference"})
public class SimpleBinarySearchTreeAnimation<T extends Comparable<T>> extends SimpleBinarySearchTree<T> implements AnimatedBinaryTree<T> {

    private BinaryTreeAnimationScene<T> animationScene;

    private boolean animate = false;
    private boolean finishWithNextStep = false;

    @Override
    public void init(BinaryTreeAnimationScene<T> animationScene) {
        this.animationScene = animationScene;
    }

    @Override
    protected BSTNode<T> createNode(T key) {
        return new AnimatedBSTNode(key);
    }

    @Override
    public void turnOnAnimation() {
        animate = true;
    }

    @Override
    public void turnOffAnimation() {
        animate = false;
    }

    @Override
    public boolean isAnimating() {
        return animate;
    }

    @Override
    public boolean isFinishingWithNextStep() {
        return finishWithNextStep;
    }

    @Override
    public void disableFinishWithNextStep() {
        finishWithNextStep = false;
    }

    @Override
    public void finishWithNextStep() {
        finishWithNextStep = true;
    }

    private void updateState(StackTraceElement[] stackTrace, String operation) {
        animationScene.getAnimationState().setStackTrace(Arrays.stream(stackTrace)
            .filter(e -> e.getClassName().startsWith("p2"))
            .filter(e -> !e.getClassName().contains(this.getClass().getSimpleName()))
            .toArray(StackTraceElement[]::new));
        animationScene.getAnimationState().setOperation(operation);
    }

    public class AnimatedBSTNode extends BSTNode<T> {

        public AnimatedBSTNode(T key) {
            super(key);
        }

        @Override
        public BSTNode<T> getLeft() {
            BSTNode<T> left = super.getLeft();

            if (animate && left != null) {
                StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

                Platform.runLater(() -> {
                    updateState(stackTrace, "(%s).getLeft()".formatted(getKey()));
                    animationScene.refresh(this, left);
                });
                waitUntilNextStep();
            }

            return left;
        }

        @Override
        protected void setLeft(BSTNode<T> left) {
            super.setLeft(left);

            if (animate) {
                StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

                Platform.runLater(() -> {
                    updateState(stackTrace, "(%s).setLeft(%s)".formatted(getKey(), left.getKey()));
                    runWithoutAnimation(() ->
                        animationScene.getTreePane().setTree(getRoot()));
                    animationScene.refresh(this, left);
                });
                waitUntilNextStep();
            }
        }

        @Override
        public BSTNode<T> getRight() {
            BSTNode<T> right = super.getRight();

            if (animate && right != null) {
                StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

                Platform.runLater(() -> {
                    updateState(stackTrace, "(%s).getRight()".formatted(getKey()));
                    animationScene.refresh(this, right);
                });
                waitUntilNextStep();
            }

            return right;
        }

        @Override
        protected void setRight(BSTNode<T> right) {
            super.setRight(right);

            if (animate) {
                StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

                Platform.runLater(() -> {
                    updateState(stackTrace, "(%s).setRight(%s)".formatted(getKey(), right.getKey()));
                    runWithoutAnimation(() ->
                        animationScene.getTreePane().setTree(getRoot()));
                    animationScene.refresh(this, right);
                });
                waitUntilNextStep();
            }
        }

        @Override
        public BSTNode<T> getParent() {
            BSTNode<T> parent = super.getParent();

            if (animate && parent != null) {
                StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

                Platform.runLater(() -> {
                    updateState(stackTrace, "(%s).getParent()".formatted(getKey()));
                    animationScene.refresh(this, parent);
                });
                waitUntilNextStep();
            }

            return parent;
        }

        @Override
        public void setParent(BSTNode<T> parent) {
            super.setParent(parent);

            if (animate) {
                StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

                Platform.runLater(() -> {
                    updateState(stackTrace, "(%s).setParent(%s)".formatted(getKey(), parent == null ? "null" : parent.getKey()));
                    runWithoutAnimation(() -> animationScene.getTreePane().setTree(getRoot()));
                    animationScene.refresh(this, parent);
                });
                waitUntilNextStep();
            }
        }
    }

}

