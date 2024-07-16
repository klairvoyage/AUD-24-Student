package p1.sort.radix;

import java.util.NoSuchElementException;

/**
 * An implementation of {@link Bucket} that uses a linked list to store the elements.
 *
 * @param <T> the type of the elements that the bucket can store.
 */
public class BucketLinkedList<T> implements Bucket<T> {

    /**
     * The number of elements in the bucket.
     */
    private int size;

    /**
     * The first element in the bucket (the start of the bucket).
     * It always contains the element that is next to be removed.
     * If the bucket is empty, it is {@code null}.
     */
    private BucketItem first;

    /**
     * The last element in the bucket (the end of the bucket).
     * It always contains the element that was added last.
     * If the bucket is empty, it is {@code null}.
     */
    private BucketItem last;

    @Override
    public void add(T value) {
        BucketItem item = new BucketItem(value);

        if (first == null) {
            first = item;
        } else {
            last.next = item;
        }

        size++;

        last = item;
    }

    @Override
    public T remove() {
        if (first == null) {
            throw new NoSuchElementException("Bucket is empty");
        }

        T value = first.element;

        first = first.next;
        if (first == null) {
            last = null;
        }

        size--;

        return value;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");

        BucketItem current = first;
        while (current != null) {
            sb.append(current.element);
            if (current.next != null) {
                sb.append(", ");
            }
            current = current.next;
        }

        sb.append("]");

        return sb.toString();
    }

    /**
     * Stores an element in the bucket and a reference to the next element in the bucket.
     */
    private class BucketItem {

        /**
         * The next element in the bucket.
         * If this element is the last element in the bucket, it is {@code null}.
         */
        BucketItem next;

        /**
         * The stored element.
         */
        T element;

        /**
         * Creates a new {@link BucketItem} that stores the given element.
         * Initially the {@link #next} element is {@code null}.
         *
         * @param element the element to store.
         */
        public BucketItem(T element) {
            this.element = element;
        }

    }
}
