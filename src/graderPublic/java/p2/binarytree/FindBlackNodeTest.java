package p2.binarytree;

import org.junit.jupiter.params.ParameterizedTest;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSetTest;

import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertEquals;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.callObject;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.contextBuilder;

@TestForSubmission
public class FindBlackNodeTest extends P2_TestBase {

    @ParameterizedTest
    @JsonParameterSetTest(value = "FindBlackNode_Smallest_Simple.json", customConverters = "customConverters")
    public void testFindSmallestBlackNodeSimple(JsonParameterSet params) {
        testFindBlackNode(params, true);
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "FindBlackNode_Smallest_Complex.json", customConverters = "customConverters")
    public void testFindSmallestBlackNodeComplex(JsonParameterSet params) {
        testFindBlackNode(params, true);
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "FindBlackNode_Greatest.json", customConverters = "customConverters")
    public void testFindGreatestBlackNode(JsonParameterSet params) {
        testFindBlackNode(params, false);
    }

    private void testFindBlackNode(JsonParameterSet params, boolean findSmallest) {
        RBTree<Integer> tree = params.get("RBTree");
        int totalBlackHeight = params.getInt("totalBlackHeight");
        int targetBlackHeight = params.getInt("targetBlackHeight");
        int expected = params.get("expectedNode");

        Context.Builder<?> context = contextBuilder()
            .subject("RBTree#findBlackNodeWithBlackHeight")
            .add("rb tree", tree.toString())
            .add("target black height", targetBlackHeight)
            .add("total black height", totalBlackHeight)
            .add("find smallest", findSmallest)
            .add("expected node", expected);

        RBNode<Integer> actual = callObject(() -> tree.findBlackNodeWithBlackHeight(targetBlackHeight, totalBlackHeight, findSmallest),
            context.build(), result -> "findBlackNodeWithBlackHeight should not throw an exception");

        context.add("actual node", actual.getKey());

        assertEquals(expected, actual.getKey(), context.build(), result -> "The key of the returned node is not correct");
    }

}
