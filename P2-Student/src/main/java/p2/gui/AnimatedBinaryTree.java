package p2.gui;

import p2.Node;
import p2.binarytree.BinaryNode;

import java.util.List;
import java.util.function.Predicate;

/**
 * An interface for animations of binary trees that can be visualized and stopped.
 * The implementations of this interface are responsible for calling {@link Object#wait()} to wait for the next step
 * of the animation being triggered and update the visualization before waiting.
 * To avoid blocking the JavaFX thread, the animation should be performed in a new thread.
 * To continue the animation, {@link Object#notify()} should be called on the animation from the JavaFX thread.
 * For example:
 * <blockquote><pre>
 * synchronized (animation) {
 *     animation.notify();
 * }
 * </pre></blockquote>
 */
public interface AnimatedBinaryTree<T extends Comparable<T>> {

    /**
     * @return the root node of the tree
     */
    BinaryNode<T> getRoot();

    /**
     * Initializes the animation with the given scene.
     *
     * @param animationScene The scene used for visualization.
     */
    void init(BinaryTreeAnimationScene<T> animationScene);

    /**
     * Turns on the animation, i.e., the thread should wait for the next step of the animation to be triggered once
     * when reaching an appropriate point.
     */
    void turnOnAnimation();

    /**
     * Turns off the animation, i.e., the thread should not wait for the next step of the animation to be triggered
     * and simply finish.
     */
    void turnOffAnimation();

    /**
     * @return {@code true} if the animation is currently running, i.e., the thread will wait for the next step of the
     * animation to be triggered when reaching an appropriate point, {@code false} otherwise.
     */
    boolean isAnimating();

    /**
     * Tells the animation to finish gracefully independent of the current state.
     * This is used when the animation needs to be stopped independent of the current state of {@link #isAnimating()},
     * e.g., when the user closes the application or the next animation is started before the current one is finished.
     */
    void finishWithNextStep();

    /**
     * Tells the animation that it does not need to finish gracefully with the next step anymore.
     *
     * @see #finishWithNextStep()
     */
    void disableFinishWithNextStep();

    /**
     * @return {@code true} if the animation should finish with the next step, {@code false} otherwise
     * @see #finishWithNextStep()
     */
    boolean isFinishingWithNextStep();

    /**
     * Searches for the given value in the tree.
     *
     * @param value the value to search for.
     * @return the node containing the value, or {@code null} if the value is not in the tree.
     */
    BinaryNode<T> search(T value);

    /**
     * Inserts the given value into the tree.
     *
     * @param value the value to insert.
     */
    void insert(T value);

    /**
     * Adds all elements in the subtree represented by the given node to the given list.
     * <p>
     * The elements are added in ascending order.
     * The method adds at most {@code max} elements.
     * The method stops traversing the tree if the predicate returns {@code false} for one of the elements and does
     * not add any further elements. The first element which did not satisfy the predicate is also excluded.
     * If later elements exist that satisfy the predicate, they are excluded as well.
     *
     * @param node      The root of the subtree to traverse.
     * @param result    The list to store the elements in.
     * @param max       The maximum number of elements to include in the result.
     * @param predicate The predicate to test the elements against. If the predicate returns {@code false} for an element,
     *                  the traversal stops.
     */
    void inOrder(Node<T> node, List<? super T> result, int max, Predicate<? super T> predicate);

    /**
     * Adds all elements in the tree that are greater than or equal to the given node to the given list.
     * <p>
     * The elements are added in ascending order.
     * The method adds at most {@code max} elements.
     * The method stops traversing the tree if the predicate returns {@code false} for one of the elements and does
     * not add any further elements. The first element which did not satisfy the predicate is also excluded.
     * If later elements exist that satisfy the predicate, they are excluded as well.
     *
     * @param node      The node to start the search from. The node itself is included in the search.
     * @param result    The list to store the elements in.
     * @param max       The maximum number of elements to include in the result.
     * @param predicate The predicate to test the elements against. If the predicate returns {@code false} for an element,
     *                  the traversal stops.
     */
    void findNext(Node<T> node, List<? super T> result, int max, Predicate<? super T> predicate);

    /**
     * Executes the given runnable without triggering any animations, i.e. waiting of the thread.
     * Afterward, the animation state is restored to the previous state.
     *
     * @param runnable the runnable to execute without triggering animations.
     */
    default void runWithoutAnimation(Runnable runnable) {
        boolean previousState = isAnimating();
        turnOffAnimation();
        runnable.run();
        if (previousState) turnOnAnimation();
    }

    /**
     * Causes the current thread to wait until the next step of the animation is triggered using {@link Object#notify()}.
     */
    default void waitUntilNextStep() {
        if (!isFinishingWithNextStep()) {
            synchronized (this) {
                try {
                    this.wait();
                } catch (InterruptedException ignored) {
                }
            }
        }
    }

}
