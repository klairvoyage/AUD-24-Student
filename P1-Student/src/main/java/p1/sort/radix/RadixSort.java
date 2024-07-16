package p1.sort.radix;

import p1.sort.Sort;
import p1.sort.SortList;

import static org.tudalgo.algoutils.student.Student.crash;

/**
 * An implementation of the radix sort algorithm.
 *
 * <p> It is a non-comparison-based sorting algorithm that sorts the elements by assigning them into buckets depending
 * on the value of the element at the currently considered position. If all elements have been assigned to the buckets,
 * the elements are collected from the buckets, starting at the first bucket, and the process is repeated for the next position.
 * The process is repeated until the every element of every value has been considered. The amount of iterations needed
 * is equal to {@link #maxInputLength}. The index associated with an element at a position in a value is extracted using
 * the given {@link RadixIndexExtractor}.
 *
 * @param <T> the type of the elements to be sorted.
 *
 * @see Bucket
 * @see RadixIndexExtractor
 */
public class RadixSort<T> implements Sort<T> {

    /**
     * The extractor used for mapping the element of a value at a given position to an index in the {@link #buckets} array.
     */
    private final RadixIndexExtractor<T> indexExtractor;

    /**
     * The buckets used for sorting the elements.
     */
    private final Bucket<T>[] buckets;

    /**
     * The maximum amount of elements that any value in the sorted {@link SortList} contains.
     *
     * <p> It is equal to the lowest number {@code a} where {@code indexExtractor.extractIndex(value, a) == 0}
     * for all values in the sorted {@link SortList}.
     *
     * <p> It is used for determining the amount of iterations needed to sort the list.
     */
    private int maxInputLength;

    /**
     * Creates a new {@link RadixSort} instance.
     *
     * @param radix The amount of buckets to use.
     * @param indexExtractor The extractor used for extracting the key (index) to insert the elements into the buckets.
     */
    @SuppressWarnings("unchecked")
    public RadixSort(int radix, RadixIndexExtractor<T> indexExtractor) {
        this.indexExtractor = indexExtractor;
        this.buckets = new Bucket[radix];

        if (radix < 1) {
            throw new IllegalArgumentException("The radix must be greater than 0.");
        }

        if (radix < indexExtractor.getRadix()) {
            throw new IllegalArgumentException("The given radix may not be less than the radix of the keyExtractor.");
        }

        for (int i = 0; i < radix; i++) {
            buckets[i] = new BucketLinkedList<>();
        }
    }

    @Override
    public void sort(SortList<T> sortList) {
        // Iterate through all positions from 0 to maxInputLength - 1
        for (int i=0; i<maxInputLength; i++) {
            // For each element in the sortList, place it into the appropriate bucket based on the current position
            for (int j=0; j<sortList.getSize(); j++) putBucket(sortList.get(j), i);

            // Reassemble the sortList from the buckets
            int a = 0;
            // Iterate through each bucket
            for (int k=0; k<buckets.length; k++) {
                int size = buckets[k].size();
                // Remove all elements from the current bucket and place them back into the sortList (FIFO-principle!)
                for (int b=0; b<size; b++) {
                    sortList.set(a, buckets[k].remove());
                    a++;
                }
            }
        }
    }

    /**
     * Adds the given value to one of the {@link #buckets}.
     * The index of the bucket is determined using the {@link #indexExtractor} with the given value and position.
     *
     * @param value The value to add to the buckets.
     * @param position The position used to determine the index in the {@link #buckets} array. The lowest position is 0.
     * @throws IndexOutOfBoundsException if the position is less than 0.
     * @see RadixIndexExtractor#extractIndex(T, int)
     */
    public void putBucket(T value, int position) {
        // Extract the index for the given value and position using the indexExtractor
        int index = indexExtractor.extractIndex(value, position);
        // Add the value to the corresponding bucket (FIFO-principle!)
        buckets[index].add(value);
    }

    @Override
    public int getComparisonsCount() {
        return 0; //Radix sort is not based on comparisons.
    }

    /**
     * Sets the maximum amount of elements that any value in the {@link SortList}, that will be sorted, contains.
     * @param maxInputLength the new maximum input length.
     *
     * @see #maxInputLength
     */
    public void setMaxInputLength(int maxInputLength) {
        this.maxInputLength = maxInputLength;
    }
}
