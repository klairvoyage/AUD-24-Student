package p1;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junitpioneer.jupiter.json.JsonClasspathSource;
import org.junitpioneer.jupiter.json.Property;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import p1.comparator.CountingComparator;
import p1.transformers.MethodInterceptor;

import java.util.Comparator;

import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertEquals;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.contextBuilder;

@TestForSubmission
public class CountingComparatorTest {

    private final static Comparator<Integer> DELEGATE = Comparator.naturalOrder();

    private static CountingComparator<Integer> countingComparator;

    @BeforeEach
    public void setup() {
        MethodInterceptor.reset();
        countingComparator = new CountingComparator<>(DELEGATE);
    }

    @AfterEach
    public void checkIllegalMethods() {
        IllegalMethodsCheck.checkMethods();
    }

    @Test
    public void checkIllegalMethodsUntested() {
        MethodInterceptor.reset();
        CountingComparator<Integer> countingComparator = new CountingComparator<>(DELEGATE);
        //noinspection ResultOfMethodCallIgnored
        countingComparator.compare(5, 4);
        IllegalMethodsCheck.checkMethods();
    }

    @ParameterizedTest
    @JsonClasspathSource(value = "H2_CountingComparatorTests.json", data = "compareTest")
    public void testCompare(@Property("value1") Integer value1, @Property("value2") Integer value2) {
        Context context = contextBuilder()
            .subject("CountingComparator#compare()")
            .add("value1", value1)
            .add("value2", value2)
            .add("delegate", "natural_order")
            .build();

        assertEquals(DELEGATE.compare(value1, value2), countingComparator.compare(value1, value2), context,
            result -> "The methode compare() should return the same value as the delegate.");
    }

}
