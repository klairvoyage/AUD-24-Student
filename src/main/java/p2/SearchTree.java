package p2;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Interface for an arbitrary search tree.
 * <p>
 * It stores the elements in an ordered tree structure and provides methods for inserting, searching and traversing.
 * It can contain duplicate elements.
 * <p>
 * The Tree can store any type of elements that implement the {@link Comparable} interface.
 *
 * @param <T> the type of the elements in the tree.
 * @see Node
 */
public interface SearchTree<T extends Comparable<T>> {

    /**
     * Checks if the tree contains the given value.
     * <p>
     * A given value is contained in the tree if there exists at least one node with a key {@code k} for which
     * {@code k.compareTo(value) == 0} returns {@code true}.
     * <p>
     * This method is equivalent to {@code search(value) != null}.
     *
     * @param value the value to search for.
     * @return {@code true} if the value is in the tree, {@code false} otherwise.
     */
    default boolean contains(T value) {
        return search(value) != null;
    }

    /**
     * Searches for the given value in the tree and returns the node containing the value. If the value is not in the
     * tree, the method returns {@code null}.
     * <p>
     * If there are multiple nodes with the same value, the method may return any of them.
     * <p>
     * A node contains the given value if at least one key {@code k} in the node satisfies the condition
     * {@code k.compareTo(value) == 0}.
     *
     * @param value the value to search for.
     * @return the node containing the value, or {@code null} if the value is not in the tree.
     */
    Node<T> search(T value);

    /**
     * Inserts the given value into the tree.
     *
     * @param value the value to insert.
     */
    void insert(T value);

    /**
     * Returns a list of all elements in the tree in ascending order.
     *
     * @return a list of all elements in the tree in ascending order.
     */
    default List<T> inOrder() {
        List<T> result = new ArrayList<>();
        inOrder(findSmallest(), result, Integer.MAX_VALUE, t -> true);
        return result;
    }

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
     * Finds and returns the node that contains the smallest element in the tree.
     *
     * @return the node containing the smallest element in the tree.
     */
    Node<T> findSmallest();

    /**
     * Returns the root node of the tree.
     *
     * @return the root node of the tree.
     */
    Node<T> getRoot();
}
