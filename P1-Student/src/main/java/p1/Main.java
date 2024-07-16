package p1;

import p1.sort.ArraySortList;
import p1.sort.HybridOptimizer;
import p1.sort.HybridSort;
import p1.sort.SortList;
import p1.sort.radix.LatinStringIndexExtractor;
import p1.sort.radix.PaddingLatinStringIndexExtractor;
import p1.sort.radix.RadixSort;

import java.util.List;
import java.util.Random;

/**
 * Main entry point in executing the program.
 */
public class Main {

    private static final Random random = new Random();

    /**
     * Main entry point in executing the program.
     *
     * @param args program arguments, currently ignored
     */
    public static void main(String[] args) {

        hybridSort();

        simpleRadixSort(List.of("BAA", "AAZ", "ABA", "CCC", "ABC"));

        paddingRadixSort(List.of("B", "AZ", "AB", "C", "ABB"));
        paddingRadixSort(List.of("FOP", "Mathe1", "DT", "AFE", "AUD", "Mathe2", "RO", "APL"));
    }

    private static void hybridSort() {

        SortList<Integer> list = createRandomList();

        HybridSort<Integer> hybridSort = new HybridSort<>(1, Integer::compareTo);


        int k = HybridOptimizer.optimize(hybridSort, list.toArray());
        System.out.println("first local minimum: " + k);

        hybridSort.setK(k);
        hybridSort.sort(list);

        System.out.println("hybridSort comparisons: " + hybridSort.getComparisonsCount());
    }

    private static void simpleRadixSort(List<String> strings) {
        SortList<String> list = new ArraySortList<>(strings);

        int maxInputLength = strings.stream().mapToInt(String::length).max().orElse(0);
        RadixSort<String> radixSort = new RadixSort<>(26, new LatinStringIndexExtractor());
        radixSort.setMaxInputLength(maxInputLength);

        radixSort.sort(list);

        System.out.println("simpleRadixSort: " + list);
    }

    private static void paddingRadixSort(List<String> strings) {

        SortList<String> list = new ArraySortList<>(strings);

        int maxInputLength = strings.stream().mapToInt(String::length).max().orElse(0);
        RadixSort<String> radixSort = new RadixSort<>(27, new PaddingLatinStringIndexExtractor(maxInputLength));
        radixSort.setMaxInputLength(maxInputLength);

        radixSort.sort(list);

        System.out.println("paddingRadixSort: " + list);
    }

    private static SortList<Integer> createRandomList() {
        int size = 100;
        Integer[] array = new Integer[size];

        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(size);
        }

        return new ArraySortList<>(array);
    }
}
