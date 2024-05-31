package p2.binarytree;

import org.junit.jupiter.params.ParameterizedTest;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSetTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertEquals;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertNotNull;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.call;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.contextBuilder;

@TestForSubmission
public class JoinTest extends P2_TestBase {

    @ParameterizedTest
    @JsonParameterSetTest(value = "Join_SameHeight.json", customConverters = "customConverters")
    public void testJoinSameHeight(JsonParameterSet params) {
        testJoin(params);
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "Join_RightGreater.json", customConverters = "customConverters")
    public void testJoinRightGreater(JsonParameterSet params) {
        testJoin(params);
    }

    private void testJoin(JsonParameterSet params) {

        RBTree<Integer> leftRBTree = spy(params.<RBTree<Integer>>get("leftRBTree"));
        RBTree<Integer> rightRBTree = params.get("rightRBTree");
        Integer joinKey = params.get("joinKey");
        RBTree<Integer> expectedRBTree = params.get("expectedRBTree");

        Context.Builder<?> context = contextBuilder()
            .subject("RBTree#join")
            .add("leftRBTree (this)", leftRBTree.toString())
            .add("rightRBTree (other)", rightRBTree.toString())
            .add("joinKey", joinKey)
            .add("expectedRBTree", expectedRBTree.toString())
            .add("fixColorsAfterInsertion", "disabled");

        doAnswer(invocation -> {
            AbstractBinarySearchTree<?, ?> actualTree = (AbstractBinarySearchTree<?, ?>) invocation.getMock();

            context.add("fixColorsAfterInsertion actual RBTree", actualTree.toString());

            assertTreeEquals(expectedRBTree, actualTree, context.build(),
                "fixColorsAfterInsertion has been invoked on an incorrect tree");

            RBNode<Integer> joinNode = invocation.getArgument(0);

            assertNotNull(joinNode, context.build(), result -> "The node passed to fixColorsAfterInsertion should not be null");
            assertEquals(joinKey, joinNode.getKey(), context.build(),
                result -> "The key of the node passed to fixColorsAfterInsertion is not correct");

            return null;
        }).when(leftRBTree).fixColorsAfterInsertion(any());

        try {
            call(() -> leftRBTree.join(rightRBTree, joinKey), context.build(), result -> "join should not throw an exception");
        } catch (Throwable e) {
            if (e.getCause() instanceof AssertionError ae) throw ae;
            throw e;
        }

        context.add("actualRBTree", leftRBTree.toString());

        assertTreeEquals(expectedRBTree, leftRBTree, context.build(), "The joined (left) tree is not correct");

        checkVerify(() -> verify(leftRBTree, times(1)).fixColorsAfterInsertion(any()), context.build(),
            "fixColorsAfterInsertion should be called exactly once on the left tree");

    }

}
