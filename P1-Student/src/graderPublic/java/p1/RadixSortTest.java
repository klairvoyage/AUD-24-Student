package p1;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junitpioneer.jupiter.json.JsonClasspathSource;
import org.junitpioneer.jupiter.json.Property;
import org.mockito.ArgumentCaptor;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import p1.sort.ArraySortList;
import p1.sort.SortList;
import p1.sort.radix.IntegerIndexExtractor;
import p1.sort.radix.RadixSort;
import p1.transformers.MethodInterceptor;

import java.util.List;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertEquals;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertTrue;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.call;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.contextBuilder;

@SuppressWarnings("DuplicatedCode")
@TestForSubmission
public class RadixSortTest {
    @BeforeEach
    public void setup() {
        MethodInterceptor.reset();
    }

    @AfterEach
    public void checkIllegalMethods() {
        IllegalMethodsCheck.checkMethods();
    }

    @Test
    public void checkIllegalMethodsUntested() {
        MethodInterceptor.reset();
        RadixSort<Integer> radixSort = spy(new RadixSort<>(1, new IntegerIndexExtractor(1)));
        radixSort.sort(new ArraySortList<>(List.of(5, 4, 3, 2, 1)));
        IllegalMethodsCheck.checkMethods();
    }

    @ParameterizedTest
    @JsonClasspathSource(value = "H7_RadixSortTests.json", data = "multipleItemsTest")
    public void testOneBucketSort(@Property("values") List<Integer> values, @Property("expected") List<Integer> expected, @Property("position") List<Integer> position) {
        testSorting(values, expected, position, 1);
    }

    @ParameterizedTest
    @JsonClasspathSource(value = "H7_RadixSortTests.json", data = "putBucketTest")
    public void testPutBucketCallSingle(@Property("values") List<Integer> values, @Property("position") List<Integer> position, @Property("expected") List<Integer> expected) {
        RadixSort<Integer> radixSort = spy(new RadixSort<>(10, new IntegerIndexExtractor(10)));
        radixSort.setMaxInputLength(1);

        SortList<Integer> sortList = new ArraySortList<>(values);
        Context context = contextBuilder()
            .subject("RadixSort#putBucket()")
            .add("values", values)
            .add("actual", sortList)
            .add("expected", expected)
            .build();
        call(() -> radixSort.sort(sortList), contextBuilder()
                .subject("RadixSort#sort()")
                .add("values", values)
                .build(),
            result -> "sort() should not throw an exception.");

        ArgumentCaptor<Integer> valueCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> positionCaptor = ArgumentCaptor.forClass(Integer.class);
        doNothing().when(radixSort).putBucket(valueCaptor.capture(), positionCaptor.capture());
        doCallRealMethod().doNothing().when(radixSort).sort(any());

        radixSort.sort(sortList);

        List<Integer> capturedValues = valueCaptor.getAllValues();
        List<Integer> capturedPositions = positionCaptor.getAllValues();
        for (int i = 0; i < values.toArray().length; i++) {
            int finalI = i;
            assertEquals(expected.get(i), capturedValues.get(i), context, result -> "putBucket() was called with the wrong value at index %d.".formatted(finalI));
            assertEquals(position.get(i), capturedPositions.get(i), context, result -> "putBucket() was called with the wrong position value at index %d.".formatted(finalI));
        }
    }


    private void testSorting(List<Integer> values, List<Integer> expected, List<Integer> position, Integer maxInputLength) {
        RadixSort<Integer> radixSort = new RadixSort<>(1000, new IntegerIndexExtractor(1000));
        radixSort.setMaxInputLength(maxInputLength);
        SortList<Integer> sortList = new ArraySortList<>(values);

        Context context = contextBuilder()
            .subject("RadixSort#sort()")
            .add("values", values)
            .add("actual", sortList)
            .add("expected", expected)
            .build();

        call(() -> radixSort.sort(sortList), contextBuilder()
                .subject("RadixSort#sort()")
                .add("values", values)
                .build(),
            result -> "sort() should not throw an exception.");


        assertTrue(isSorted(sortList, expected), context,
            result -> "The sortList should be sorted after calling sort().");
    }

    private boolean isSorted(SortList<Integer> sortList, List<Integer> expected) {
        for (int i = 0; i < expected.size(); i++) {
            if (!Objects.equals(sortList.get(i), expected.get(i))) {
                return false;
            }
        }

        return true;
    }
}


