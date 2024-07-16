package p1;

import org.opentest4j.AssertionFailedError;
import org.sourcegrade.jagr.api.rubric.Criterion;
import org.sourcegrade.jagr.api.rubric.GradeResult;
import org.sourcegrade.jagr.api.rubric.Grader;
import org.sourcegrade.jagr.api.rubric.JUnitTestRef;
import org.sourcegrade.jagr.api.rubric.Rubric;
import org.sourcegrade.jagr.api.rubric.RubricProvider;
import org.sourcegrade.jagr.api.testing.RubricConfiguration;
import p1.transformers.MethodInterceptorTransformer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings("unused")
public class P1_RubricProvider implements RubricProvider {

    private static Criterion createUntestedCriterion(String shortDescription, Callable<Method> illegalMethodCheck) {

        StringBuilder comment = new StringBuilder("Not graded by public grader");

        if (illegalMethodCheck != null) {
            try {
                Method method = illegalMethodCheck.call();
                Object instance = method.getDeclaringClass().getDeclaredConstructor().newInstance();
                method.invoke(instance);
            } catch (InvocationTargetException e) {
                if (e.getCause() instanceof AssertionFailedError) {
                    comment.append("\n").append(e.getCause().getMessage());
                } else {
                    comment.append("\nMethod threw an exception. Could not check whether illegal methods have been used. Exception: ").append(e.getCause().getMessage());
                }
            } catch (Exception ignored) {
            }
        }

        return Criterion.builder()
            .shortDescription(shortDescription)
            .grader((testCycle, criterion) ->
                GradeResult.of(criterion.getMinPoints(), criterion.getMaxPoints(), comment.toString()))
            .maxPoints(1)
            .build();
    }

    @SafeVarargs
    private static Criterion createCriterion(String shortDescription, int maxPoints, Callable<Method>... methodReferences) {

        if (methodReferences.length == 0) {
            return Criterion.builder()
                .shortDescription(shortDescription)
                .maxPoints(maxPoints)
                .build();
        }

        Grader.TestAwareBuilder graderBuilder = Grader.testAwareBuilder();

        for (Callable<Method> reference : methodReferences) {
            graderBuilder.requirePass(JUnitTestRef.ofMethod(reference));
        }

        return Criterion.builder()
            .shortDescription(shortDescription)
            .grader(graderBuilder
                .pointsFailedMin()
                .pointsPassedMax()
                .build())
            .maxPoints(maxPoints)
            .build();
    }

    private static Criterion createParentCriterion(String task, String shortDescription, Criterion... children) {
        return Criterion.builder()
            .shortDescription("H" + task + " | " + shortDescription)
            .addChildCriteria(children)
            .build();
    }

    public static final Criterion H1_1_1 = createUntestedCriterion("Die Methode [[[compare]]] der Klasse CardComparator funktioniert vollständig korrekt.",
        () -> CardComparatorTest.class.getMethod("checkIllegalMethods"));

    public static final Criterion H1_1 = createParentCriterion("1 a)", "CardComparator", H1_1_1);

    public static final Criterion H1_2_1 = createCriterion("Die Methode [[[compare]]] der Klasse CountingComparator funktioniert korrekt", 1,
        () -> CountingComparatorTest.class.getMethod("testCompare", Integer.class, Integer.class));

    public static final Criterion H1_2_2 = createUntestedCriterion("Die Methoden [[[reset]]] und [[[getComparisonsCount]]] der Klasse CountingComparator funktionieren vollständig korrekt",
        () -> CountingComparatorTest.class.getMethod("checkIllegalMethodsUntested"));

    public static final Criterion H1_2 = createParentCriterion("1 b)", "CountingComparator", H1_2_1, H1_2_2);

    public static final Criterion H2_1_1 = createUntestedCriterion("Die Methode [[[bubbleSort]]] der Klasse HybridSort funktioniert korrekt wenn die Eingabe bereits sortiert ist",
        () -> BubbleSortTest.class.getMethod("checkIllegalMethods"));

    public static final Criterion H2_1_2 = createUntestedCriterion("Die Methode [[[bubbleSort]]] der Klasse HybridSort funktioniert korrekt wenn die Eingabe leer ist oder nur ein Element enthält",
        () -> BubbleSortTest.class.getMethod("checkIllegalMethods"));

    public static final Criterion H2_1_3 = createCriterion("Die Methode [[[bubbleSort]]] der Klasse HybridSort funktioniert korrekt wenn die Eingabe zwei Elemente enthält", 1,
        () -> BubbleSortTest.class.getMethod("testTwoItems", List.class));

    public static final Criterion H2_1_4 = createCriterion("Die Methode [[[bubbleSort]]] der Klasse HybridSort funktioniert korrekt wenn die Eingabe mehr als zwei Elemente enthält", 1,
        () -> BubbleSortTest.class.getMethod("testMultipleItems", List.class));

    public static final Criterion H2_1_5 = createUntestedCriterion("Die Methode [[[bubbleSort]]] der Klasse HybridSort verändert nur Werte im Indexbereich [left, right]", null);

    public static final Criterion H2_1_6 = createCriterion("Die Methode [[[bubbleSort]]] der Klasse HybridSort verwendet die korrekten Lese- und Schreiboperationen in der korrekten Reihenfolge.\nHinweis: Die public Tests testen dieses Kriterium nur für den Fall, dass [[[left = 0]]] und [[[right = array.length - 1]]].", 1,
        () -> BubbleSortTest.class.getMethod("testOperationOrder", List.class, Integer.class, Integer.class, List.class));

    public static final Criterion H2_1 = createParentCriterion("2 a)", "BubbleSort", H2_1_1, H2_1_2, H2_1_3, H2_1_4, H2_1_5, H2_1_6);

    public static final Criterion H2_2_1 = createCriterion("Die Methode [[[mergeSort]]] der Klasse HybridSort ruft, wenn notwendig, die Methode [[[bubbleSort]]] mit den korrekten Werten auf", 1,
        () -> MergeSortTests.class.getMethod("testBubbleSortCall", List.class, Integer.class, Integer.class, Integer.class, Boolean.class));

    public static final Criterion H2_2_2 = createCriterion("Die Methode [[[mergeSort]]] der Klasse HybridSort ruft, wenn notwendig, die Methode [[[merge]]] und sich selber mit den korrekten Werten auf", 1,
        () -> MergeSortTests.class.getMethod("testMergeSortRecursion", List.class, Integer.class, Integer.class, Integer.class, Boolean.class, Integer.class));

    public static final Criterion H2_2_3 = createCriterion("Die Methode [[[merge]]] der Klasse HybridSort funktioniert korrekt wenn die Eingabe bereits gemerged ist", 1,
        () -> MergeSortTests.class.getMethod("testAlreadyMerged", List.class, Integer.class, Integer.class));

    public static final Criterion H2_2_4 = createCriterion("Die Methode [[[merge]]] der Klasse HybridSort funktioniert korrekt wenn die Eingabe nicht gemerged ist und zwei Elemente enthält", 1,
        () -> MergeSortTests.class.getMethod("testMergingTwoItems", List.class, Integer.class, Integer.class, List.class));

    public static final Criterion H2_2_5 = createCriterion("Die Methode [[[merge]]] der Klasse HybridSort funktioniert korrekt wenn die Eingabe nicht gemerged ist und drei Elemente enthält", 1,
        () -> MergeSortTests.class.getMethod("testMergingThreeItems", List.class, Integer.class, Integer.class, Integer.class, List.class));

    public static final Criterion H2_2_6 = createCriterion("Die Methode [[[merge]]] der Klasse HybridSort funktioniert vollständig korrekt", 1,
        () -> MergeSortTests.class.getMethod("testAlreadyMerged", List.class, Integer.class, Integer.class),
        () -> MergeSortTests.class.getMethod("testMergingTwoItems", List.class, Integer.class, Integer.class, List.class),
        () -> MergeSortTests.class.getMethod("testMergingThreeItems", List.class, Integer.class, Integer.class, Integer.class, List.class),
        () -> MergeSortTests.class.getMethod("testMergingMultipleItems", List.class, Integer.class, Integer.class, Integer.class, List.class));

    public static final Criterion H2_2 = createParentCriterion("2 b)", "MergeSort", H2_2_1, H2_2_2, H2_2_3, H2_2_4, H2_2_5, H2_2_6);

    public static final Criterion H2_3_1 = createCriterion("Die Methode [[[optimize]]] der Klasse HybridOptimizer ruft die [[[sort]]] Methode mit korrekten Werten in der richtigen Reihenfolge auf", 1,
        () -> HybridOptimizerTest.class.getMethod("testSortCall", List.class, List.class, List.class, int.class));

    public static final Criterion H2_3_2 = createUntestedCriterion("Die Methode [[[optimize]]] der Klasse HybridOptimizer funktioniert korrekt wenn die Messwerte nicht monoton sind und es nur ein Minimum gibt",
        () -> HybridOptimizerTest.class.getMethod("checkIllegalMethods"));

    public static final Criterion H2_3_3 = createUntestedCriterion("Die Methode [[[optimize]]] der Klasse HybridOptimizer funktioniert korrekt wenn die Messwerte streng monoton fallend sind",
        () -> HybridOptimizerTest.class.getMethod("checkIllegalMethods"));

    public static final Criterion H2_3_4 = createUntestedCriterion("Die Methode [[[optimize]]] der Klasse HybridOptimizer funktioniert korrekt wenn die Messwerte monoton fallend sind",
        () -> HybridOptimizerTest.class.getMethod("checkIllegalMethods"));

    public static final Criterion H2_3_5 = createUntestedCriterion("Die Methode [[[optimize]]] der Klasse HybridOptimizer funktioniert korrekt wenn die Messwerte nicht monoton sind und es mehrere Minima gibt",
        () -> HybridOptimizerTest.class.getDeclaredMethod("checkIllegalMethods"));

    public static final Criterion H2_3 = createParentCriterion("2 c)", "HybridOptimizer", H2_3_1, H2_3_2, H2_3_3, H2_3_4, H2_3_5);

    public static final Criterion H3_1_1 = createCriterion("Die Methode [[[extractIndex]]] der Klasse LatinStringIndexExtractor funktioniert korrekt wenn die Position innerhalb des Indexbereiches des String liegt und das Zeichen ein gültiger Buchstabe ist", 2,
        () -> LatinStringIndexExtractorTest.class.getDeclaredMethod("testExtractValidIndex", String.class, int.class, int.class));

    public static final Criterion H3_1_2 = createCriterion("Die Methode [[[extractIndex]]] der Klasse LatinStringIndexExtractor funktioniert korrekt wenn die Position innerhalb des Indexbereiches des String liegt und das Zeichen kein gültiger Buchstabe ist", 1,
        () -> LatinStringIndexExtractorTest.class.getDeclaredMethod("testExtractInvalidChar", String.class, int.class));

    public static final Criterion H3_1 = createParentCriterion("3 a)", "LatinStringIndexExtractor", H3_1_1, H3_1_2);

    public static final Criterion H3_2_1 = createUntestedCriterion("Die Methode [[[putBucket]]] der Klasse RadixSort funktioniert vollständig korrekt",
        () -> RadixSortTest.class.getMethod("checkIllegalMethodsUntested"));

    public static final Criterion H3_2_2 = createCriterion("Die Methode [[[sort]]] der Klasse RadixSort ruft die Methode putBucket in der korrekten Reihenfolge mit den korrekten Werten auf wenn maxInputLength 1 ist", 1,
        () -> RadixSortTest.class.getMethod("testPutBucketCallSingle", List.class, List.class, List.class));

    public static final Criterion H3_2_3 = createUntestedCriterion("Die Methode [[[sort]]] der Klasse RadixSort ruft die Methode putBucket in der korrekten Reihenfolge mit den korrekten Werten auf wenn maxInputLength größer als 1 ist",
        () -> RadixSortTest.class.getMethod("checkIllegalMethodsUntested"));

    public static final Criterion H3_2_4 = createUntestedCriterion("Die Methode [[[sort]]] der Klasse RadixSort schreibt die Werte aus den buckets an die korrekten Stellen in die zu sortierende Liste wenn es nur einen Bucket mit einem Eintrag gibt und maxInputLength 1 ist",
        () -> RadixSortTest.class.getMethod("checkIllegalMethodsUntested"));

    public static final Criterion H3_2_5 = createUntestedCriterion("Die Methode [[[sort]]] der Klasse RadixSort schreibt die Werte aus den buckets an die korrekten Stellen in die zu sortierende Liste wenn es nur einen Bucket mit mehreren Einträgen gibt und maxInputLength 1 ist",
        () -> RadixSortTest.class.getMethod("checkIllegalMethodsUntested"));

    public static final Criterion H3_2_6 = createCriterion("Die Methode [[[sort]]] der Klasse RadixSort schreibt die Werte aus den buckets an die korrekten Stellen in die zu sortierende Liste wenn es mehrere Buckets mit jeweils nur einem Eintrag gibt und maxInputLength 1 ist", 1,
        () -> RadixSortTest.class.getMethod("testOneBucketSort", List.class, List.class, List.class));

    public static final Criterion H3_2_7 = createUntestedCriterion("Die Methode [[[sort]]] der Klasse RadixSort funktioniert vollständig korrekt",
        () -> RadixSortTest.class.getMethod("checkIllegalMethodsUntested"));

    public static final Criterion H3_2 = createParentCriterion("3 b)", "RadixSort", H3_2_1, H3_2_2, H3_2_3, H3_2_4, H3_2_5, H3_2_6, H3_2_7);

    public static final Criterion H1 = createParentCriterion("1", "Comparators", H1_1, H1_2);
    public static final Criterion H2 = createParentCriterion("2", "Hybrid-Sort", H2_1, H2_2, H2_3);
    public static final Criterion H3 = createParentCriterion("3", "Radix-Sort", H3_1, H3_2);

    public static final Rubric RUBRIC = Rubric.builder()
        .title("P1")
        .addChildCriteria(H1)
        .addChildCriteria(H2)
        .addChildCriteria(H3)
        .build();

    @Override
    public Rubric getRubric() {
        return RUBRIC;
    }

    @Override
    public void configure(RubricConfiguration configuration) {
        configuration.addTransformer(new MethodInterceptorTransformer());
    }
}
