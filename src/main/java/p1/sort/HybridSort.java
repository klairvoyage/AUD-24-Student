package p1.sort;

import p1.comparator.CountingComparator;

import java.util.Comparator;

import static org.tudalgo.algoutils.student.Student.crash;

/**
 * A hybrid sorting algorithm. It uses a combination of mergeSort and bubbleSort.
 * <p>
 * mergeSort is used for sorting the lists of size greater than or equal to k.
 * <p>
 * bubbleSort is used for sorting the lists of size less than k.
 *
 * @param <T> the type of the elements to be sorted.
 *
 * @see Sort
 */
public class HybridSort<T> implements Sort<T> {

    /**
     * The threshold for switching from mergeSort to bubbleSort.
     */
    private int k;

    /**
     * The comparator used for comparing the sorted elements.
     */
    private final CountingComparator<T> comparator;

    /**
     * Creates a new {@link HybridSort} instance.
     *
     * @param k          the threshold for switching from mergeSort to bubbleSort.
     * @param comparator the comparator used for comparing the sorted elements.
     */
    public HybridSort(int k, Comparator<T> comparator) {
        this.k = k;
        this.comparator = new CountingComparator<>(comparator);
    }

    @Override
    public void sort(SortList<T> sortList) {
        comparator.reset();
        mergeSort(sortList, 0, sortList.getSize() - 1);
    }

    @Override
    public int getComparisonsCount() {
        return comparator.getComparisonsCount();
    }

    /**
     * Returns the current threshold for switching from mergeSort to bubbleSort.
     * @return the current threshold for switching from mergeSort to bubbleSort.
     */
    public int getK() {
        return k;
    }

    /**
     * Sets the threshold for switching from mergeSort to bubbleSort.
     * @param k the new threshold.
     */
    public void setK(int k) {
        this.k = k;
    }

    /**
     * Sorts the given {@link SortList} using the mergeSort algorithm.
     * It will only consider the elements between the given left and right indices (both inclusive).
     * Elements with indices less than left or greater than right will not be altered.
     * <p>
     * Once the amount of elements to sort is less than the threshold {@link #k}, the algorithm switches to bubbleSort.
     * @param sortList the {@link SortList} to be sorted.
     * @param left The leftmost index of the list to be sorted. (inclusive)
     * @param right The rightmost index of the list to be sorted. (inclusive)
     */
    public void mergeSort(SortList<T> sortList, int left, int right) {
        // Check if there is more than one element to sort
        if (left<right) {
            // If the number of elements is less than the threshold, use bubbleSort
            if ((right-left+1)<getK()) bubbleSort(sortList, left, right);
            else {
                // Calculate the middle index to split the list into two halves (rounded down)
                int mid = (left+right)/2;
                // Recursively sort the left part of the list
                mergeSort(sortList, left, mid);
                // Recursively sort the right part of the list
                mergeSort(sortList, mid+1, right);
                // Merge the two sorted halves into one sorted list
                merge(sortList, left, mid, right);
            }
        }
    }

    /**
     * Merges the two sorted sublists between the indices left and right (both inclusive) of the given {@link SortList}.
     * The middle index separates the two sublists and is the last index of the left sublist.
     *
     * <p>The left sublist ranges from left to middle (both inclusive) and the right sublist ranges from
     * middle + 1 to right (both inclusive). Bot sublists are sorted.
     *
     * <p>The algorithm uses a temporary {@link SortList} to store the merged elements. The results are copied back to
     * the original {@link SortList} at the same location. Elements with indices less than left or greater than right
     * will not be altered.
     *
     * <p>After merging the elements between left and right (both inclusive) will be sorted.
     *
     * @param sortList the {@link SortList} to be sorted.
     * @param left The leftmost index of the two sublists to be merged. (inclusive)
     * @param middle The index that separates the two sublists. It is the last index that belongs to the left sublist.
     * @param right The rightmost index of the two sublists to be merged. (inclusive)
     */
    public void merge(SortList<T> sortList, int left, int middle, int right) { // requires left<=mid<=right
        // Create a temporary SortList to store the merged elements
        SortList<T> temp = new ArraySortList<>(right-left+1);

        // Initialize pointers for both sublists
        int p = left; // Position in the left sublist
        int q = middle + 1; // Position in the right sublist

        // Merge elements from both sublists into the temporary list
        for (int i=0; i<(right-left+1); i++) {
            // If all elements from the right sublist are merged or
            // the current element in the left sublist is less than or equal to the current element in the right sublist
            if (q>right || ((p<=middle) && (comparator.compare(sortList.get(p), sortList.get(q)) <= 0))) {
                temp.set(i, sortList.get(p));
                p++; // Move to the next element in the left sublist
            } else {
                // If the current element in the right sublist is less than the current element in the left sublist
                temp.set(i, sortList.get(q));
                q++; // Move to the next element in the right sublist
            }
        }
        // Copy the merged elements back to the original list
        for (int i=0; i<(right-left+1); i++) {
            sortList.set(i+left, temp.get(i));
        }
    }

    /**
     * Sorts the given {@link SortList} using the bubbleSort algorithm.
     * It will only consider the elements between the given left and right indices (both inclusive).
     * Elements with indices less than left or greater than right will not be altered.
     *
     * @param sortList the {@link SortList} to be sorted.
     * @param left The leftmost index of the list to be sorted.
     * @param right The rightmost index of the list to be sorted.
     */
    public void bubbleSort(SortList<T> sortList, int left, int right) {
        // Outer loop: Start from the rightmost element and move towards the left
        for (int i=right; i>=left; i--) {
            // Inner loop: Traverse the list from the leftmost element to the i-th element
            for (int j=left; j<i; j++) {
                // Compare the current element with the next element
                if (comparator.compare(sortList.get(j), sortList.get(j+1)) > 0) {
                    // Swap the elements if they are in the wrong order
                    T temp = sortList.get(j+1);
                    sortList.set(j+1, sortList.get(j));
                    sortList.set(j, temp);
                }
            }
        }
    }

}
