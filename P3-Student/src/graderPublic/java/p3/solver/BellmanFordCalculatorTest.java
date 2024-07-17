package p3.solver;

import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.ArgumentCaptor;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSetTest;
import p3.P3_TestBase;
import p3.graph.Edge;
import p3.graph.Graph;
import p3.implementation.TestGraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.spy;
import static p3.util.AssertionUtil.assertEquals;
import static p3.util.AssertionUtil.assertMapEquals;
import static p3.util.AssertionUtil.assertSame;
import static p3.util.AssertionUtil.assertTrue;
import static p3.util.AssertionUtil.fail;
import static p3.util.ReflectionUtil.setDistances;
import static p3.util.ReflectionUtil.setPredecessors;

@TestForSubmission
public class BellmanFordCalculatorTest extends P3_TestBase {

    @Override
    public String getTestedClassName() {
        return "BellmanFordPathCalculator";
    }

    @Override
    public List<String> getOptionalParams() {
        return List.of("nodes", "edges", "start", "end", "predecessors", "distances", "hasNegativeCycle", "expected", "expectedPredecessors", "expectedDistances");
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "bellmanford/relax.json")
    public void testRelax(JsonParameterSet params) throws ReflectiveOperationException {
        Edge<Integer> edge = Edge.of(params.getInt("from"), params.getInt("to"), params.getInt("weight"));

        BellmanFordPathCalculator<Integer> calculator = createCalculator(params);
        Context.Builder<?> context = createContext(params, "relax", Map.of("edge to relax", edge));

        call(() -> calculator.relax(edge), context, "relax");

        assertMapsCorrect(params, calculator, context);
    }

    @SuppressWarnings("unchecked")
    @ParameterizedTest
    @JsonParameterSetTest(value = "bellmanford/processGraph.json")
    public void testProcessGraph(JsonParameterSet params) throws ReflectiveOperationException {
        List<Integer> nodes = params.get("nodes");
        Set<Edge<Integer>> edges = getEdges(params);

        BellmanFordPathCalculator<Integer> calculator = createCalculator(params, true);

        ArgumentCaptor<Edge<Integer>> edgeCaptor = ArgumentCaptor.forClass(Edge.class);
        doNothing().when(calculator).relax(edgeCaptor.capture());

        Context.Builder<?> context = createContext(params, "processGraph");

        call(calculator::processGraph, context, "processGraph");

        int expectedCount = edges.size() * (nodes.size() - 1);
        assertEquals(expectedCount, edgeCaptor.getAllValues().size(), context, "The relax method should be called (nodes.size - 1) * edges.size times");

        List<Edge<Integer>> currentIterationEdges = new ArrayList<>();
        for (int i = 0; i < expectedCount; i++) {
            currentIterationEdges.add(edgeCaptor.getAllValues().get(i));

            if (currentIterationEdges.size() == edges.size()) {
                for (Edge<Integer> edge : edges) {
                    int iteration = i / edges.size();
                    assertTrue(currentIterationEdges.contains(edge), context, "The edges in iteration %d (relax invocation %d to %d) do not contain edge %s"
                            .formatted(iteration, iteration * edges.size(), (iteration + 1) * edges.size(), edge));
                }
            }
        }

        assertTrue(calculator.predecessors.isEmpty(), context, "The predecessors map should not change");
        assertTrue(calculator.distances.isEmpty(), context, "The distances map should not change");
    }

    private BellmanFordPathCalculator<Integer> createCalculator(JsonParameterSet params) throws ReflectiveOperationException {
        return createCalculator(params, false);
    }

    private BellmanFordPathCalculator<Integer> createCalculator(JsonParameterSet params, boolean spy) throws ReflectiveOperationException {
        List<Integer> nodes = params.get("nodes");
        Set<Edge<Integer>> edges = params.availableKeys().contains("edges") ? getEdges(params) : Set.of();

        Graph<Integer> graph = new TestGraph<>(nodes, edges);
        BellmanFordPathCalculator<Integer> calculator = spy ? spy(new BellmanFordPathCalculator<>(graph)) : new BellmanFordPathCalculator<>(graph);

        if (params.availableKeys().contains("predecessors")) {
            setPredecessors(calculator, createPredecessorMap(params, "predecessors"));
        }
        if (params.availableKeys().contains("distances")) {
            setDistances(calculator, createDistanceMap(params, "distances"));
        }

        return calculator;
    }

    private void assertMapsCorrect(JsonParameterSet params, BellmanFordPathCalculator<Integer> calculator, Context.Builder<?> context) {
        context.add("actual predecessors", calculator.predecessors.toString());
        context.add("actual distances", calculator.distances.toString());

        assertMapEquals(createPredecessorMap(params, "expectedPredecessors"), calculator.predecessors, context, "predecessor");
        assertMapEquals(createDistanceMap(params, "expectedDistances"), calculator.distances, context, "distance");
    }

}
