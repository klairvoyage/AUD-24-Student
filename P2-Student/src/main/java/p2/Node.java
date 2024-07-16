package p2;

/**
 * Interface for an arbitrary node in a {@link SearchTree}.
 * <p>
 * It contains at least one key but can also contain multiple keys.
 * <p>
 * The keys can be of any type that implements the {@link Comparable} interface.
 *
 * @param <T> the type of the keys in the node.
 * @see SearchTree
 */
public interface Node<T extends Comparable<T>> {

    /**
     * Returns an array containing all keys in the node.
     * <p>
     * The keys are ordered in ascending order.
     * The size of the array is equal to the number of keys in the node.
     * The array is a copy of the internal array and can be modified without affecting the node.
     *
     * @return an array containing all keys in the node.
     */
    T[] getKeys();

}
