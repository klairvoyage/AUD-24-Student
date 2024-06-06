package p2.binarytree;

import com.fasterxml.jackson.databind.JsonNode;
import org.sourcegrade.jagr.api.testing.extension.JagrExecutionCondition;
import org.tudalgo.algoutils.tutor.general.annotation.SkipAfterFirstFailedTest;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertEquals;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertNotNull;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertNull;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertSame;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.fail;

@SkipAfterFirstFailedTest
public abstract class P2_TestBase {

    public static final boolean IS_RUN_WITH_JAGR = !new JagrExecutionCondition().evaluateExecutionCondition(null).isDisabled();

    @SuppressWarnings("unused")
    public static final Map<String, Function<JsonNode, ?>> customConverters = Map.ofEntries(
        Map.entry("RBTree", JSONConverters::toIntegerRBTree),
        Map.entry("bst", JSONConverters::toIntegerBinarySearchTree),
        Map.entry("expectedBST", JSONConverters::toIntegerBinarySearchTree),
        Map.entry("expectedRBTree", JSONConverters::toIntegerRBTree),
        Map.entry("autocomplete", JSONConverters::toAutoComplete),
        Map.entry("expectedList", JSONConverters::toIntegerList),
        Map.entry("leftRBTree", JSONConverters::toIntegerRBTree),
        Map.entry("rightRBTree", JSONConverters::toIntegerRBTree)
    );

    public String treeToString(BinarySearchTree<?> tree) {
        if (IS_RUN_WITH_JAGR) {
            return tree.toString().replace("[", "&lsqb;").replace("]", "&rsqb;");
        }

        return tree.toString();
    }

    public void testForBSTAndRBTree(JsonParameterSet params, BiConsumer<BinarySearchTree<Integer>, String> test) {

        boolean testPerformed = false;

        if (params.getRootNode().has("bst")) {

            test.accept(params.get("bst"), "BinarySearchTree");
            testPerformed = true;
        }
        if (params.getRootNode().has("RBTree")) {
            test.accept(params.get("RBTree"), "RBTree");
            testPerformed = true;
        }

        if (!testPerformed) {
            throw new IllegalArgumentException("Internal error: No tree found in the parameter set");
        }
    }

    public void checkVerify(Runnable verifier, Context context, String msg) {
        try {
            verifier.run();
        } catch (AssertionError e) {
            fail(context, result -> msg + " Original error message:\n" + e.getMessage());
        } catch (Exception e) {
            fail(context, result -> "Unexpected Exception:\n" + e.getMessage());
        }
    }

    public void assertTreeUnchanged(RBTree<?> expected, RBTree<?> actual, Context context) {
        if (expected.getRoot() != null)
            assertNodeUnchanged(expected.getRoot(), actual.getRoot(), actual.sentinel, context);
    }

    public void assertTreeUnchanged(SimpleBinarySearchTree<?> expected, SimpleBinarySearchTree<?> actual, Context context) {
        if (expected.getRoot() != null) assertNodeUnchanged(expected.getRoot(), actual.getRoot(), null, context);
    }

    public void assertNodeUnchanged(BinaryNode<?> expected, BinaryNode<?> actual, BinaryNode<?> parent, Context context) {

        assertNotNull(actual, context, result -> "The tree should not change. The node with key %s should not be null".formatted(expected.getKey()));

        assertEquals(expected.getKey(), actual.getKey(), context, result -> "The tree should not change. The key of the node with key %s is not correct".formatted(expected.getKey()));

        if (expected.getLeft() != null) assertNodeUnchanged(expected.getLeft(), actual.getLeft(), actual, context);
        else
            assertNull(actual.getLeft(), context, result -> "The tree should not change. The left child of the node with key %s should be null".formatted(expected.getKey()));

        if (expected.getRight() != null) assertNodeUnchanged(expected.getRight(), actual.getRight(), actual, context);
        else
            assertNull(actual.getRight(), context, result -> "The tree should not change. The right child of the node with key %s should be null".formatted(expected.getKey()));

        assertSame(parent, actual.getParent(), context, result -> "The tree should not change. The parent of the node with key %s should be the node with key %s".formatted(expected.getKey(), parent.getKey()));

    }

    public void assertTreeEquals(AbstractBinarySearchTree<?, ?> expected, AbstractBinarySearchTree<?, ?> actual, Context context, String message) {
        assertRootCorrect(expected.getRoot(), actual.getRoot(), context, message);
        assertRootParentCorrect(actual, context, message);

        if (expected.getRoot() != null) {
            if (expected.getRoot().getLeft() != null)
                assertNodeCorrect(expected.getRoot().getLeft(), actual.getRoot().getLeft(), actual.getRoot(), context, message);
            if (expected.getRoot().getRight() != null)
                assertNodeCorrect(expected.getRoot().getRight(), actual.getRoot().getRight(), actual.getRoot(), context, message);
        }

    }

    private void assertRootCorrect(BinaryNode<?> expectedRoot, BinaryNode<?> actualRoot, Context context, String message) {

        if (expectedRoot == null) {
            assertNull(actualRoot, context, result -> message + ". The root of the tree should be null");
        } else {
            assertNotNull(actualRoot, context, result -> message + ". The root of the tree should not be null");
            assertEquals(expectedRoot.getKey(), actualRoot.getKey(), context, result -> message + ". The key of the root is not correct");
        }

        assert expectedRoot != null;
        if (expectedRoot.getLeft() == null) {
            assertNull(actualRoot.getLeft(), context, result -> message + ". The left child of the root should be null");
        } else {
            assertNotNull(actualRoot.getLeft(), context, result -> message + ". The left child of the root should not be null");
        }

        if (expectedRoot.getRight() == null) {
            assertNull(actualRoot.getRight(), context, result -> message + ". The right child of the root should be null");
        } else {
            assertNotNull(actualRoot.getRight(), context, result -> message + ". The right child of the root should not be null");
        }
    }

    private void assertRootParentCorrect(AbstractBinarySearchTree<?, ?> actual, Context context, String message) {
        if (actual instanceof RBTree<?> rbTree) {
            assertSame(rbTree.sentinel, rbTree.getRoot().getParent(), context, result -> message + ". The parent of the root should be the sentinel node");
        } else {
            assertNull(actual.getRoot().getParent(), context, result -> message + ". The parent of the root should be null");
        }
    }

    private void assertNodeCorrect(BinaryNode<?> expected, BinaryNode<?> actual, BinaryNode<?> parent, Context context, String message) {

        StringBuilder nodeDescriptionViaParent = new StringBuilder();

        if (parent.getLeft() == actual) {
            nodeDescriptionViaParent.append("left child");
        } else {
            nodeDescriptionViaParent.append("right child");
        }

        nodeDescriptionViaParent.append(" of the node with key ").append(parent.getKey());

        assertEquals(expected.getKey(), actual.getKey(), context, result -> message + ". The key of the %s is not correct".formatted(nodeDescriptionViaParent));

        String nodeDescription = "node with key %s".formatted(actual.getKey());

        if (expected.getLeft() == null) {
            assertNull(actual.getLeft(), context, result -> message + ". The left child of the %s should be null".formatted(nodeDescription));
        } else {
            assertNotNull(actual.getLeft(), context, result -> message + ". The left child of the %s should not be null".formatted(nodeDescription));
            assertNodeCorrect(expected.getLeft(), actual.getLeft(), actual, context, message);
        }

        if (expected.getRight() == null) {
            assertNull(actual.getRight(), context, result -> message + ". The right child of the %s should be null".formatted(nodeDescription));
        } else {
            assertNotNull(actual.getRight(), context, result -> message + ". The right child of the %s should not be null".formatted(nodeDescription));
            assertNodeCorrect(expected.getRight(), actual.getRight(), actual, context, message);
        }

        assertSame(parent, actual.getParent(), context, result -> message + ". The parent of the %s should be the node with key %s".formatted(nodeDescription, parent.getKey()));
    }
}
