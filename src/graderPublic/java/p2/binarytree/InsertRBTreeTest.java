package p2.binarytree;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertSame;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.call;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.contextBuilder;

@TestForSubmission
public class InsertRBTreeTest extends P2_TestBase {

    @SuppressWarnings("unchecked")
    @Test
    public void testInsertRBTree() {
        final int value = 1;

        RBTree<Integer> tree = spy(new RBTree<>());

        ArgumentCaptor<Integer> createNodeCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<RBNode<Integer>> insertNodeCaptor = ArgumentCaptor.forClass(RBNode.class);
        ArgumentCaptor<RBNode<Integer>> insertInitialPXCaptor = ArgumentCaptor.forClass(RBNode.class);
        ArgumentCaptor<RBNode<Integer>> fixColorsNodeCaptor = ArgumentCaptor.forClass(RBNode.class);

        doCallRealMethod().when(tree).insert(anyInt());

        RBNode<Integer> node = new RBNode<>(value, Color.RED);

        doReturn(node).when(tree).createNode(createNodeCaptor.capture());
        doNothing().when(tree).insert(insertNodeCaptor.capture(), insertInitialPXCaptor.capture());
        doNothing().when(tree).fixColorsAfterInsertion(fixColorsNodeCaptor.capture());

        Context context = contextBuilder()
            .subject("RBTree#insert(T)")
            .add("value", value)
            .build();

        call(() -> tree.insert(value), context, result -> "insert(T) threw an exception");

        checkVerify(() -> verify(tree, times(1)).createNode(anyInt()), context,
            "createNode was not called exactly once");
        checkVerify(() -> verify(tree, times(1)).insert(any(), any()), context,
            "insert(N, N) was not called exactly once");
        checkVerify(() -> verify(tree, times(1)).fixColorsAfterInsertion(any()), context,
            "fixColorsAfterInsertion was not called exactly once");

        assertSame(value, createNodeCaptor.getValue(), context,
            result -> "createNode was not called with the correct value");
        assertSame(node, insertNodeCaptor.getValue(), context,
            result -> "insert(N, N) was not called with the correct node for the first parameter");
        assertSame(tree.sentinel, insertInitialPXCaptor.getValue(), context,
            result -> "insert(N, N) was not called with the correct node for the second parameter");
        assertSame(node, fixColorsNodeCaptor.getValue(), context,
            result -> "fixColorsAfterInsertion was not called with the correct node");
    }

}
