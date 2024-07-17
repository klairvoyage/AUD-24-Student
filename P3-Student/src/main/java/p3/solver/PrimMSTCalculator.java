package p3.solver;

import p3.graph.Edge;
import p3.graph.Graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.tudalgo.algoutils.student.Student.crash;

/**
 * An implementation of the {@link MSTCalculator} interface that uses the Prim algorithm to calculate the minimum
 * spanning tree of a {@link Graph}.
 *
 * @param <N> the type of the nodes in the graph.
 */
public class PrimMSTCalculator<N> implements MSTCalculator<N> {

    /**
     * Factory for creating new instances of {@link PrimMSTCalculator}.
     */
    public static final Factory FACTORY = PrimMSTCalculator::new;

    /**
     * The graph to calculate the MST for.
     */
    protected final Graph<N> graph;

    /**
     * Stores the current predecessor for each node.
     */
    protected final Map<N, N> predecessors;

    /**
     * Stores the weight of the edge from the current predecessor of each node to that node.
     */
    protected final Map<N, Integer> keys;

    /**
     * Stores the nodes that have not yet been visited by the algorithm.
     */
    protected final Set<N> remainingNodes;

    /**
     * Creates a new {@link PrimMSTCalculator} for the given graph.
     *
     * @param graph the graph to calculate the MST for.
     */
    public PrimMSTCalculator(Graph<N> graph) {
        this.graph = graph;
        this.predecessors = new HashMap<>();
        this.keys = new HashMap<>();
        this.remainingNodes = new HashSet<>();
    }

    @Override
    public Graph<N> calculateMST(N root) {
        //TODO: H4 e) - remove if implemented
        init(root); // Initialize the Prim's algorithm with the root node
        while (!remainingNodes.isEmpty()) {
            // Extract the node with the smallest key & process the extracted node
            N node = extractMin();
            processNode(node);
        }
        // Calculate and return the edges of the minimum spanning tree
        Set<Edge<N>> mstEdges = calculateMSTEdges();
        return Graph.of(graph.getNodes(), mstEdges);
    }

    /**
     * Processes the current node with the prim algorithm
     *
     * @param node current node processed by the algorithm
     */
    protected void processNode(N node) {
        //TODO: H4 c) - remove if implemented
        for (Edge<N> edge : graph.getOutgoingEdges(node)) {  // Iterate over all outgoing edges of the current node
            N adj = edge.to();
            int weight = edge.weight();
            // If the adjacent node is still in the remaining nodes and the edge weight is less than the current key
            if (remainingNodes.contains(adj) && weight < keys.get(adj)) {
                // Update the predecessor and key for the adjacent node
                predecessors.put(adj, node);
                keys.put(adj, weight);
            }
        }
    }

    /**
     * Initializes the fields before executing the prim algorithm
     *
     * @param root the root node of the calculated mst.
     */
    protected void init(N root) {
        //TODO: H4 a) - remove if implemented
        for (N node : graph.getNodes()) {
            keys.put(node, Integer.MAX_VALUE); // Set distance to infinity
            predecessors.put(node, null); // Set predecessor to null
            remainingNodes.add(node); // Add to the set of remaining nodes
        }
        keys.put(root, Integer.MIN_VALUE); // Set the key for the root to the smallest value to start the algorithm

    }

    /**
     * Extracts the node with the smallest key from the remaining nodes and removes it from the set.
     *
     * @return the node in the remaining nodes set with the smallest key.
     */
    protected N extractMin() {
        //TODO: H4 b) - remove if implemented
        N minNode = null;
        int minValue = Integer.MAX_VALUE;
        for (N node : remainingNodes) { // Iterate over all remaining nodes to find the node with the smallest key
            Integer keyValue = keys.get(node);
            if (keyValue != null && keyValue </*=*/ minValue) {
                minValue = keyValue;
                minNode = node;
            }
        }
        // Remove the node with the smallest key from the remaining nodes
        if (minNode != null) remainingNodes.remove(minNode);
        return minNode;
    }

    /**
     * Calculates the edges of the minimum spanning tree using the previously calculated information in the predecessors
     * and keys maps.
     *
     * @return the edges of the minimum spanning tree
     */
    protected Set<Edge<N>> calculateMSTEdges() {
        //TODO: H4 d) - remove if implemented
        Set<Edge<N>> mstEdges = new HashSet<>(); // Create a set to store the MST edges
        // Iterate over all nodes to construct the MST edges
        for (N node : predecessors.keySet()) {
            N pred = predecessors.get(node);
            // Add the edge from the predecessor to the current node with the corresponding weight
            if (pred != null) mstEdges.add(Edge.of(pred, node, keys.get(node)));
        }
        return mstEdges;
    }
}
