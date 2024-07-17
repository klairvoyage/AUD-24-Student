package p3.solver;

import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSetTest;
import p3.P3_TestBase;
import p3.graph.Edge;
import p3.graph.Graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.ObjIntConsumer;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static p3.util.AssertionUtil.assertEquals;
import static p3.util.AssertionUtil.assertFalse;
import static p3.util.AssertionUtil.assertMapEquals;
import static p3.util.AssertionUtil.assertSame;
import static p3.util.AssertionUtil.assertTrue;

@TestForSubmission
public class DFSTest extends P3_TestBase {

    @Override
    public String getTestedClassName() {
        return "DFS";
    }

    @Override
    public List<String> getOptionalParams() {
        return List.of("nodes", "edges", "current", "colors", "time", "cyclic", "discoveryTimes", "finishingTimes", "predecessors",
            "expectedVisitedCount", "expectedColors", "expectedDiscoveryTimes", "expectedFinishingTimes", "expectedPredecessors", "hasCycle");
    }

    @SuppressWarnings("unchecked")
    @ParameterizedTest
    @JsonParameterSetTest(value = "dfs/traverse.json")
    public void testTraverse(JsonParameterSet params) {
        List<Integer> nodes = params.get("nodes");

        DFS<Integer> dfs = createDFS(params, true);

        ArgumentCaptor<ObjIntConsumer<Integer>> visitedConsumerCaptor = ArgumentCaptor.forClass(ObjIntConsumer.class);
        ArgumentCaptor<Integer> visitedNodeCaptor = ArgumentCaptor.forClass(Integer.class);

        doNothing().when(dfs).init();
        doNothing().when(dfs).visit(visitedConsumerCaptor.capture(), visitedNodeCaptor.capture());

        Context.Builder<?> context = createContext(params, "traverse");
        ObjIntConsumer<Integer> emptyConsumer = (node, time) -> {
        };

        call(() -> dfs.traverse(emptyConsumer), context, "traverse");

        InOrder inorder = inOrder(dfs);

        checkVerify(() -> inorder.verify(dfs).init(), context, "init() should be called exactly once");

        int expectedVisitedCount = params.getInt("expectedVisitedCount");
        checkVerify(() -> inorder.verify(dfs, times(expectedVisitedCount)).visit(any(), any()), context,
            "visit() should be called exactly %d times after init()".formatted(expectedVisitedCount));

        Map<Integer, DFS.Color> colors = nodeListToMap(params, "colors", value -> DFS.Color.valueOf((String) value));
        for (int node : nodes) {
            if (colors.get(node) == DFS.Color.WHITE) {
                assertTrue(visitedNodeCaptor.getAllValues().contains(node), context, "visit should be invoked with the node %d".formatted(node));
            }
        }

        for (int i = 0; i < expectedVisitedCount; i++) {
            assertSame(emptyConsumer, visitedConsumerCaptor.getAllValues().get(i), context, "The consumer passed to visit() should be the same as the one passed to traverse()");
        }

        assertMapEquals(colors, dfs.colors, context, "colors");
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "dfs/visitNoOutgoingEdges.json")
    public void testVisitNoOutgoingEdges(JsonParameterSet params) {
        testVisit(params);
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "dfs/visit.json")
    public void testVisit(JsonParameterSet params) {
        DFS<Integer> dfs = createDFS(params);
        Context.Builder<?> context = createContext(params, "visit");

        Map<Integer, Integer> consumerTimes = new HashMap<>();
        ObjIntConsumer<Integer> consumer = consumerTimes::put;

        call(() -> dfs.visit(consumer, params.getInt("current")), context, "visit");

        context.add("actual colors", dfs.colors);
        context.add("actual time", dfs.time);
        context.add("actual discoveryTimes", dfs.discoveryTimes);
        context.add("actual finishTimes", dfs.finishTimes);
        context.add("actual predecessors", dfs.predecessors);

        Map<Integer, DFS.Color> expectedColors = nodeListToMap(params, "expectedColors", value -> DFS.Color.valueOf((String) value));
        Map<Integer, Integer> expectedDiscoveryTimes = mapToNodeMap(params, "expectedDiscoveryTimes", value -> (Integer) value);
        Map<Integer, Integer> expectedFinishingTimes = mapToNodeMap(params, "expectedFinishingTimes", value -> (Integer) value);
        Map<Integer, Integer> expectedPredecessors = createPredecessorMap(params, "expectedPredecessors");

        assertMapEquals(expectedColors, dfs.colors, context, "colors");
        assertMapEquals(expectedDiscoveryTimes, dfs.discoveryTimes, context, "discoveryTimes");
        assertMapEquals(expectedFinishingTimes, dfs.finishTimes, context, "finishTimes");
        assertMapEquals(expectedPredecessors, dfs.predecessors, context, "predecessors");
        assertMapEquals(expectedFinishingTimes, consumerTimes, context, "consumer values");
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "dfs/cyclic.json")
    public void testCyclic(JsonParameterSet params) {
        DFS<Integer> dfs = createDFS(params);
        Context.Builder<?> context = createContext(params, "isCyclic");

        ObjIntConsumer<Integer> emptyConsumer = (node, time) -> {
        };

        call(() -> dfs.traverse(emptyConsumer), context, "isCyclic");

        context.add("actual cyclic", dfs.isCyclic());

        assertEquals(params.get("hasCycle"), dfs.isCyclic(), context,
            "The method isCyclic() did not return the correct value after calling traverse()");
    }

    private DFS<Integer> createDFS(JsonParameterSet params) {
        return createDFS(params, false);
    }

    private DFS<Integer> createDFS(JsonParameterSet params, boolean spy) {
        List<Integer> nodes = params.get("nodes");

        Set<Edge<Integer>> edges = getEdges(params);

        DFS<Integer> dfs = spy ? spy(new DFS<>(Graph.of(new HashSet<>(nodes), edges))) : new DFS<>(Graph.of(new HashSet<>(nodes), edges));

        if (params.availableKeys().contains("colors")) {
            dfs.colors.putAll(nodeListToMap(params, "colors", value -> DFS.Color.valueOf((String) value)));
        }

        if (params.availableKeys().contains("time")) {
            dfs.time = params.getInt("time");
        }

        if (params.availableKeys().contains("cyclic")) {
            dfs.cyclic = params.getBoolean("cyclic");
        }

        if (params.availableKeys().contains("discoveryTimes")) {
            List<Integer> discoveryTimes = params.get("discoveryTimes");
            for (int i = 0; i < discoveryTimes.size(); i++) {
                dfs.discoveryTimes.put(nodes.get(i), discoveryTimes.get(i));
            }
        }

        if (params.availableKeys().contains("finishTimes")) {
            List<Integer> finishTimes = params.get("finishTimes");
            for (int i = 0; i < finishTimes.size(); i++) {
                dfs.finishTimes.put(nodes.get(i), finishTimes.get(i));
            }
        }

        if (params.availableKeys().contains("predecessors")) {
            List<Integer> predecessors = params.get("predecessors");
            for (int i = 0; i < predecessors.size(); i++) {
                dfs.predecessors.put(nodes.get(i), predecessors.get(i));
            }
        }

        return dfs;
    }

}
