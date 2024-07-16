package p2.binarytree;

import org.junit.jupiter.params.ParameterizedTest;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSetTest;
import p2.binarytree.implementation.TestRBNode;

import java.util.function.Consumer;

import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.call;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.contextBuilder;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.fail;

@TestForSubmission
public class RBTreeCheckerTest extends P2_TestBase {

    @ParameterizedTest
    @JsonParameterSetTest(value = "RBTreeChecker_Rule1.json", customConverters = "customConverters")
    public void testRule1(JsonParameterSet params) {
        testRule(params, 1, RBTreeChecker::checkRule1);
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "RBTreeChecker_Rule2.json", customConverters = "customConverters")
    public void testRule2(JsonParameterSet params) {
        testRule(params, 2, RBTreeChecker::checkRule2);
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "RBTreeChecker_Rule3.json", customConverters = "customConverters")
    public void testRule3(JsonParameterSet params) {
        testRule(params, 3, RBTreeChecker::checkRule3);
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "RBTreeChecker_Rule4.json", customConverters = "customConverters")
    public void testRule4(JsonParameterSet params) {
        testRule(params, 4, RBTreeChecker::checkRule4);
    }

    private void testRule(JsonParameterSet params, int rule, Consumer<RBTree<Integer>> checkRule) {
        RBTree<Integer> tree = params.get("RBTree");
        boolean valid = params.getBoolean("valid");

        Context context = contextBuilder()
            .subject("RBTreeChecker#checkRule" + rule)
            .add("tree", treeToString(tree))
            .add("valid", valid)
            .build();

        try {
            TestRBNode.startCounting();
            checkRule.accept(tree);

            if (!valid) {
                fail(context, result -> "No exception was thrown despite the tree being invalid");
            }
        } catch (RBTreeException e) {
            if (valid) {
                fail(context, result -> "An InvalidColorException was thrown despite the tree being valid. Exception message: " + e.getMessage());
            }
        } catch (AssertionError e) {
            throw e;
        } catch (Throwable e) {
            call(() -> {
                throw e;
            }, context, result -> "An unexpected exception was thrown.");
        } finally {
            TestRBNode.stopCounting();
        }

        checkLinear((TestRBNode<Integer>) tree.getRoot(), context);
        assertTreeUnchanged(params.get("RBTree"), tree, context);
    }

    private void checkLinear(TestRBNode<Integer> node, Context context) {

        if (node == null) return;

        if (node.getLeftCount() > 1) {
            fail(context, result -> "The getLeft method was called more than once on a node with key " + node.getKey() + ". Actual count: " + node.getLeftCount());
        }

        if (node.getRightCount() > 1) {
            fail(context, result -> "The getRight method was called more than once on a node with key " + node.getKey() + ". Actual count: " + node.getRightCount());
        }

        checkLinear((TestRBNode<Integer>) node.getLeft(), context);
        checkLinear((TestRBNode<Integer>) node.getRight(), context);
    }

}
