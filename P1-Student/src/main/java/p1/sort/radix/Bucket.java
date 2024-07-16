package p1.sort.radix;

/**
 * A bucket is a list of elements that can store elements and remove them in a FIFO (First In First Out) order.
 *
 * <p>Added elements are stored at the end of the bucket and removed elements are removed from the start of the bucket.
 *
 * <p>Once an element is added to the bucket, it can only be removed once all the elements that were added before it have been removed.
 *
 * @param <T> the type of the elements that the bucket can store.
 */
public interface Bucket<T> {

    /**
     * Adds the given value to the end of the bucket.
     * @param value the value to add.
     */
    void add(T value);

    /**
     * Removes the first element from the bucket.
     *
     * <p>The first element is the element that has been in the bucket the longest.
     *
     * @return the removed element.
     * @throws java.util.NoSuchElementException if the bucket is empty.
     */
    T remove();

    /**
     * Returns the number of elements in the bucket.
     * @return the number of elements in the bucket.
     */
    int size();

    /**
     * Returns {@code true} if the bucket contains no elements.
     * @return {@code true} if the bucket is empty.
     */
    default boolean isEmpty() {
        return size() == 0;
    }

}
