package p1.sort;

import static org.tudalgo.algoutils.student.Student.crash;

/**
 * Optimizes the {@link HybridSort} by trying to find the k-value with the lowest number of read and write operations..
 */
public class HybridOptimizer {

    /**
     * Optimizes the {@link HybridSort} by trying to find the k-value with the lowest number of read and write operations.
     * The method will try out all k-values starting from 0 and return the k-value with the lowest number of read and write operations.
     * It will stop once if found the first local minimum or reaches the maximum possible k-value for the size of the given array.
     *
     * @param hybridSort the {@link HybridSort} to optimize.
     * @param array the array to sort.
     * @return the k-value with the lowest number of read and write operations.
     * @param <T> the type of the elements to be sorted.
     */
    public static <T> int optimize(HybridSort<T> hybridSort, T[] array) {
        //TODO: H2 c) - remove if implemented
        int minK = 0; // Initialize the variable to store the optimal k-value
        int minOPs = Integer.MAX_VALUE; // Initialize the variable to store the minimum number of operations

        // Iterate over all possible k-values from 0 to array.length + 1
        for (int k=0; k<=array.length+1; k++) {
            // Set the current k-value for the sorting algorithm in the HybridSort object
            hybridSort.setK(k);
            // Convert the array to an ArraySortList
            ArraySortList<T> sortList = new ArraySortList<>(array);
            // Perform the sorting with the current k-value
            hybridSort.sort(sortList);

            // Calculate the total number of read and write operations at value k
            int currentOps = sortList.getReadCount() + sortList.getWriteCount();

            // Check if still falling monotonously
            if (currentOps <= minOPs) {
                minOPs = currentOps; // Update the minimum number of operations
                minK = k; // Update the optimal k-value
            } else break; // Stop if the number of operations starts increasing
        }
        return minK; // Return the optimal k-value
    }

}
