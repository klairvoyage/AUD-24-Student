package p1;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junitpioneer.jupiter.json.JsonClasspathSource;
import org.junitpioneer.jupiter.json.Property;
import org.mockito.ArgumentCaptor;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import p1.sort.ArraySortList;
import p1.sort.HybridSort;
import p1.sort.SortList;
import p1.transformers.MethodInterceptor;

import java.util.Comparator;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertEquals;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertSame;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.contextBuilder;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.fail;

@SuppressWarnings("DuplicatedCode")
@TestForSubmission
public class MergeSortTests {

    private static final Comparator<Integer> COMPARATOR = Comparator.naturalOrder();
    private static HybridSort<Integer> hybridSort;

    @BeforeEach
    public void setup() {
        MethodInterceptor.reset();
        hybridSort = spy(new HybridSort<>(5, COMPARATOR));
    }

    @AfterEach
    public void checkIllegalMethods() {
        IllegalMethodsCheck.checkMethods();
    }

    @ParameterizedTest
    @JsonClasspathSource(value = "H4_MergeSortTests.json", data = "bubbleSortCallTest")
    public void testBubbleSortCall(@Property("values") List<Integer> values,
                                   @Property("left") Integer left,
                                   @Property("right") Integer right,
                                   @Property("k") Integer k,
                                   @Property("calls") Boolean calls) {

        Context context = contextBuilder()
            .subject("HybridSort#mergeSort()")
            .add("values", values)
            .add("left", left)
            .add("right", right)
            .add("k", k)
            .build();

        hybridSort.setK(k);

        @SuppressWarnings("unchecked")
        ArgumentCaptor<SortList<Integer>> sortListCaptor = ArgumentCaptor.forClass(SortList.class);
        ArgumentCaptor<Integer> leftCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> rightCaptor = ArgumentCaptor.forClass(Integer.class);

        doNothing().when(hybridSort).bubbleSort(sortListCaptor.capture(), leftCaptor.capture(), rightCaptor.capture());
        doNothing().when(hybridSort).merge(any(), anyInt(), anyInt(), anyInt());
        doCallRealMethod().doNothing().when(hybridSort).mergeSort(any(), anyInt(), anyInt());

        SortList<Integer> sortList = new ArraySortList<>(values);

        hybridSort.mergeSort(sortList, left, right);

        if (calls) {
            checkVerify(() -> verify(hybridSort, never()).merge(any(), anyInt(), anyInt(), anyInt()), context,
                "merge() was called when it should not have been.");

            checkVerify(() -> verify(hybridSort, times(1)).mergeSort(any(), anyInt(), anyInt()), context,
                "mergeSort() was called when it should not have been.");

            checkVerify(() -> verify(hybridSort, times(1)).bubbleSort(any(), anyInt(), anyInt()), context,
                "bubbleSort() was not called exactly once when it should have been.");

            assertSame(sortList, sortListCaptor.getValue(), context, result -> "bubbleSort() was called with the wrong SortList.");
            assertEquals(left, leftCaptor.getValue(), context, result -> "bubbleSort() was called with the wrong left index.");
            assertEquals(right, rightCaptor.getValue(), context, result -> "bubbleSort() was called with the wrong right index.");
        } else {
            checkVerify(() -> verify(hybridSort, never()).bubbleSort(any(), anyInt(), anyInt()), context,
                "bubbleSort() was called when it should not have been.");
        }
    }

    @ParameterizedTest
    @JsonClasspathSource(value = "H4_MergeSortTests.json", data = "mergeSortTest")
    public void testMergeSortRecursion(@Property("values") List<Integer> values,
                                       @Property("left") Integer left,
                                       @Property("right") Integer right,
                                       @Property("k") Integer k,
                                       @Property("calls") Boolean calls,
                                       @Property("middle") Integer middle) {

        Context context = contextBuilder()
            .subject("HybridSort#mergeSort()")
            .add("values", values)
            .add("left", left)
            .add("right", right)
            .add("k", k)
            .add("middle", middle)
            .build();

        hybridSort.setK(k);

        @SuppressWarnings("unchecked")
        ArgumentCaptor<SortList<Integer>> mergeSortListCaptor = ArgumentCaptor.forClass(SortList.class);
        ArgumentCaptor<Integer> mergeSortLeftCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> mergeSortRightCaptor = ArgumentCaptor.forClass(Integer.class);

        @SuppressWarnings("unchecked")
        ArgumentCaptor<SortList<Integer>> sortListCaptor = ArgumentCaptor.forClass(SortList.class);
        ArgumentCaptor<Integer> mergeLeftCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> mergeMiddleCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> mergeRightCaptor = ArgumentCaptor.forClass(Integer.class);

        doNothing().when(hybridSort).bubbleSort(any(), anyInt(), anyInt());
        doNothing().when(hybridSort).merge(sortListCaptor.capture(), mergeLeftCaptor.capture(), mergeMiddleCaptor.capture(), mergeRightCaptor.capture());
        doCallRealMethod().doNothing().when(hybridSort).mergeSort(mergeSortListCaptor.capture(), mergeSortLeftCaptor.capture(), mergeSortRightCaptor.capture());

        SortList<Integer> sortList = new ArraySortList<>(values);

        hybridSort.mergeSort(sortList, left, right);

        if (calls) {
            checkVerify(() -> verify(hybridSort, never()).bubbleSort(any(), anyInt(), anyInt()), context,
                "mergeSort() was called when it should not have been.");
            checkVerify(() -> verify(hybridSort, times(1)).merge(any(), anyInt(), anyInt(), anyInt()), context,
                "merge() was not called exactly once when it should have been.");
            checkVerify(() -> verify(hybridSort, times(3)).mergeSort(any(), anyInt(), anyInt()), context,
                "mergeSort() was not called exactly twice when it should have been.");

            assertSame(sortList, sortListCaptor.getValue(), context, result -> "merge() was called with the wrong SortList.");
            assertEquals(left, mergeLeftCaptor.getValue(), context, result -> "merge() was called with the wrong left index.");
            assertEquals(right, mergeRightCaptor.getValue(), context, result -> "merge() was called with the wrong right index.");

            assertSame(sortList, mergeSortListCaptor.getAllValues().get(1), context, result -> "mergeSort() was called with the wrong SortList at the first call.");
            assertSame(sortList, mergeSortListCaptor.getAllValues().get(2), context, result -> "mergeSort() was called with the wrong SortList at the second call.");

            boolean leftFirst = mergeSortLeftCaptor.getAllValues().get(1).equals(left);

            if (leftFirst) {
                assertEquals(left, mergeSortLeftCaptor.getAllValues().get(1), context, result -> "mergeSort() was called with the wrong left index at the first call.");
                assertEquals(middle, mergeSortRightCaptor.getAllValues().get(1), context, result -> "mergeSort() was called with the wrong right index at the first call.");

                assertEquals(middle + 1, mergeSortLeftCaptor.getAllValues().get(2), context, result -> "mergeSort() was called with the wrong left index at the second call.");
                assertEquals(right, mergeSortRightCaptor.getAllValues().get(2), context, result -> "mergeSort() was called with the wrong right index at the second call.");
            } else {
                assertEquals(middle + 1, mergeSortLeftCaptor.getAllValues().get(1), context, result -> "mergeSort() was called with the wrong left index at the first call.");
                assertEquals(right, mergeSortRightCaptor.getAllValues().get(1), context, result -> "mergeSort() was called with the wrong right index at the first call.");

                assertEquals(left, mergeSortLeftCaptor.getAllValues().get(2), context, result -> "mergeSort() was called with the wrong left index at the second call.");
                assertEquals(middle, mergeSortRightCaptor.getAllValues().get(2), context, result -> "mergeSort() was called with the wrong middle index at the second call.");
            }
        } else {
            checkVerify(() -> verify(hybridSort, never()).merge(any(), anyInt(), anyInt(), anyInt()), context, "merge() was called when it should not have been.");
            checkVerify(() -> verify(hybridSort, times(1)).mergeSort(any(), anyInt(), anyInt()), context, "mergeSort() was called when it should not have been.");
        }
    }

    @ParameterizedTest
    @JsonClasspathSource(value = "H4_MergeSortTests.json", data = "alreadyMergedTest")
    public void testAlreadyMerged(@Property("values") List<Integer> values,
                                  @Property("left") Integer left,
                                  @Property("right") Integer right) {

        checkMerging(values, left, left, right, values);
    }

    @ParameterizedTest
    @JsonClasspathSource(value = "H4_MergeSortTests.json", data = "mergingTwoItemsTest")
    public void testMergingTwoItems(@Property("values") List<Integer> values,
                                    @Property("left") Integer left,
                                    @Property("right") Integer right,
                                    @Property("expected") List<Integer> expected) {

        checkMerging(values, left, left, right, expected);
    }

    @ParameterizedTest
    @JsonClasspathSource(value = "H4_MergeSortTests.json", data = "mergingThreeItemsTest")
    public void testMergingThreeItems(@Property("values") List<Integer> values,
                                      @Property("left") Integer left,
                                      @Property("right") Integer right,
                                      @Property("middle") Integer middle,
                                      @Property("expected") List<Integer> expected) {

        checkMerging(values, left, middle, right, expected);
    }

    @ParameterizedTest
    @JsonClasspathSource(value = "H4_MergeSortTests.json", data = "mergingMultipleItemsTest")
    public void testMergingMultipleItems(@Property("values") List<Integer> values,
                                         @Property("left") Integer left,
                                         @Property("right") Integer right,
                                         @Property("middle") Integer middle,
                                         @Property("expected") List<Integer> expected) {

        checkMerging(values, left, middle, right, expected);
    }

    private void checkMerging(List<Integer> values, int left, int middle, int right, List<Integer> expected) {

        SortList<Integer> sortList = new ArraySortList<>(values);
        hybridSort.merge(sortList, left, middle, right);

        Context context = contextBuilder()
            .subject("HybridSort#merge()")
            .add("values", values)
            .add("left", left)
            .add("middle", middle)
            .add("right", right)
            .add("expected", expected)
            .add("actual", sortList)
            .build();

        for (int i = 0; i < values.size(); i++) {
            int finalI = i;
            assertEquals(expected.get(i), sortList.get(i), context, result -> "sortList contains wrong value at index %d.".formatted(finalI));
        }
    }

    private void checkVerify(Runnable verifier, Context context, String msg) {
        try {
            verifier.run();
        } catch (MockitoAssertionError e) {
            fail(context, result -> msg + " Original error message:\n" + e.getMessage());
        } catch (Exception e) {
            fail(context, result -> "Unexpected Exception:\n" + e.getMessage());
        }
    }

}
