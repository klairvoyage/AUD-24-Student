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
import p3.implementation.TestGraph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static p3.util.AssertionUtil.assertEquals;
import static p3.util.AssertionUtil.assertMapEquals;
import static p3.util.AssertionUtil.assertNotNull;
import static p3.util.AssertionUtil.assertSetEquals;
import static p3.util.AssertionUtil.fail;
import static p3.util.ReflectionUtil.setKeys;
import static p3.util.ReflectionUtil.setPredecessors;
import static p3.util.ReflectionUtil.setRemainingNodes;

@TestForSubmission
public class PrimCalculatorTest extends P3_TestBase {

    @Override
    public String getTestedClassName() {
        return "PrimMSTCalculator";
    }

    @Override
    public List<String> getOptionalParams() {
        return List.of("nodes", "edges", "root", "node", "remainingNodes", "predecessors", "keys",
                "expectedPredecessors", "expectedKeys", "expectedRemainingNodes", "expected", "expectedProcessNodeOrder",
                "expectedEdges", "mayReturnAny");
    }

    @ParameterizedTest
    @JsonParameterSetTest(value = "prim/extractMin.json")
    public void testExtractMin(JsonParameterSet params) throws ReflectiveOperationException {
        PrimMSTCalculator<Integer> calculator = createCalculator(params);
        Context.Builder<?> context = createContext(params, "extractMin");

        Integer actual = callObject(calculator::extractMin, context, "extractMin");

        context.add("actual", actual);
        context.add("actual remainingNodes", calculator.remainingNodes);

        if (params.getBoolean("mayReturnAny")) {
            assertNotNull(actual, context, "extractMin should not return null");
            Set<Integer> expectedSet = new HashSet<>(params.get("remainingNodes"));
            expectedSet.remove(actual);
            assertSetEquals(expectedSet, calculator.remainingNodes, context, "remainingNodes");
        } else {
            assertEquals(params.getInt("expected"), actual, context, "extractMin did not return the expected node");
            assertSetEquals(new HashSet<>(params.get("expectedRemainingNodes")), calculator.remainingNodes, context, "remainingNodes");
        }

        assertMapsCorrect(params, calculator, context);
    }

    private PrimMSTCalculator<Integer> createCalculator(JsonParameterSet params) throws ReflectiveOperationException {
        return createCalculator(params, false);
    }

    private PrimMSTCalculator<Integer> createCalculator(JsonParameterSet params, boolean spy) throws ReflectiveOperationException {
        List<Integer> nodes = params.get("nodes");
        Set<Edge<Integer>> edges = getEdges(params);

        Graph<Integer> graph = new TestGraph<>(nodes, edges);
        PrimMSTCalculator<Integer> calculator = spy ? spy(new PrimMSTCalculator<>(graph)) : new PrimMSTCalculator<>(graph);

        Set<Integer> remainingNodes = params.availableKeys().contains("remainingNodes") ? new HashSet<>(params.get("remainingNodes")) : new HashSet<>();

        setPredecessors(calculator, createPredecessorMap(params, "predecessors"));
        setKeys(calculator, createKeysMap(params, "keys"));
        setRemainingNodes(calculator, remainingNodes);

        return calculator;
    }

    private void assertMapsCorrect(JsonParameterSet params, PrimMSTCalculator<Integer> calculator, Context.Builder<?> context) {
        context.add("actual predecessors", calculator.predecessors);
        context.add("actual keys", calculator.keys);

        Map<Integer, Integer> expectedPredecessors = createPredecessorMap(params,
                params.availableKeys().contains("expectedPredecessors") ? "expectedPredecessors" : "predecessors");
        Map<Integer, Integer> expectedKeys = createKeysMap(params,
                params.availableKeys().contains("expectedKeys") ? "expectedKeys" : "keys");

        assertMapEquals(expectedPredecessors, calculator.predecessors, context, "predecessors");
        assertMapEquals(expectedKeys, calculator.keys, context, "keys");
    }

}
