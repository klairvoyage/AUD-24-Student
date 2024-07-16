package p3.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.tudalgo.algoutils.student.Student.crash;

/**
 * A mutable, directed, weighted graph that uses an {@link AdjacencyRepresentation} to store the graph.
 *
 * @param <N> the type of the nodes in this graph.
 * @see Graph
 * @see MutableGraph
 * @see AdjacencyRepresentation
 */
public class AdjacencyGraph<N> implements MutableGraph<N> {

    /**
     * The {@link AdjacencyRepresentation} that stores the graph.
     */
    private final AdjacencyRepresentation representation;

    /**
     * A map that associates each node with its weight.
     */
    private final Map<N, Map<N, Integer>> weights = new HashMap<>();

    /**
     * A map from nodes to their indices in the adjacency matrix.
     * Every node in the graph is mapped to a distinct index in the range [0, {@link #representation}.size() -1].
     * This map is the inverse of {@link #indexToNode}.
     */
    private final Map<N, Integer> nodeToIndex = new HashMap<>();

    /**
     * A map from indices in the adjacency matrix to the nodes they represent.
     * Every index in the range [0, {@link #representation}.size() -1] is mapped to a distinct node in the graph.
     * This map is the inverse of {@link #nodeToIndex}.
     */
    private final Map<Integer, N> indexToNode = new HashMap<>();

    /**
     * Constructs a new {@link AdjacencyGraph} which initially contains the given nodes and edges.
     * <p>
     * It uses the object created by the given representationFactory to store the graph.
     *
     * @param nodes                 the initial set of nodes.
     * @param edges                 the initial set of edges.
     * @param representationFactory a factory that creates an {@link AdjacencyRepresentation} with the given size.
     */
    public AdjacencyGraph(Set<N> nodes, Set<Edge<N>> edges, AdjacencyRepresentation.Factory representationFactory) {
        representation = representationFactory.create(nodes.size());

        //TODO: H1 c) - remove if implemented
        for (N node : nodes) { // Add initial nodes (w/ a unique index on an interval)
            nodeToIndex.put(node, nodeToIndex.size());
            indexToNode.put(nodeToIndex.size()-1, node);
        }
        for (Edge<N> edge : edges) addEdge(edge); // Add initial edges
    }

    /**
     * Adds a new node to the graph.
     *
     * @param node the node to add
     */
    @Override
    public void addNode(N node) {
        //TODO: H1 c) - remove if implemented
        if (!nodeToIndex.containsKey(node)) { // Check if the node already exists
            // Assign a new index to the node
            int newIndex = nodeToIndex.size();
            nodeToIndex.put(node, newIndex);
            indexToNode.put(newIndex, node);
            // Grow the representation to accommodate the new node
            if (representation.size() <= newIndex) representation.grow();
        }
    }

    /**
     * Adds a new edge to the graph.
     *
     * @param edge the edge to add
     */
    @Override
    public void addEdge(Edge<N> edge) {
        //TODO: H1 c) - remove if implemented
        N from = edge.from();
        N to = edge.to();
        // Ensure both nodes exist in the graph
        if (!nodeToIndex.containsKey(to) || !nodeToIndex.containsKey(from)) throw new IllegalArgumentException();
        // Add the edge to the representation
        representation.addEdge(nodeToIndex.get(from), nodeToIndex.get(to));

        // Store the weight of the edge
        weights.computeIfAbsent(from, x -> new HashMap<>())
            .put(to, edge.weight());

        // ALTERNATIVELY:
        // Check if 'from' node is in the map
        if (!weights.containsKey(from)) weights.put(from, new HashMap<>()); // If not, add new empty map for 'from' node
        weights.get(from)
            .put(to, edge.weight()); // Now add the 'to' node and its edge weight to the inner map
    }

    @Override
    public Set<N> getNodes() {
        return nodeToIndex.keySet();
    }

    @Override
    public Set<Edge<N>> getEdges() {
        Set<Edge<N>> set = new HashSet<>();

        for (N node : nodeToIndex.keySet()) {
            set.addAll(getOutgoingEdges(node));
        }

        return set;
    }

    /**
     * Gets all outgoing edges from the specified node.
     *
     * @param node the node
     * @return a set of outgoing edges
     */
    @Override
    public Set<Edge<N>> getOutgoingEdges(N node) {
        //TODO: H1 c) - remove if implemented
        checkNode(node); // Check if node exists in the graph
        Set<Edge<N>> outgoingEdges = new HashSet<>(); // Create a set to store the outgoing edges
        // Get all adjacent nodes and creates edges
        for (int toIndex : representation.getAdjacentIndices(nodeToIndex.get(node)))
            outgoingEdges.add(Edge.of(node, indexToNode.get(toIndex), getWeight(node, indexToNode.get(toIndex))));
        return outgoingEdges;
    }

    @Override
    public Set<Edge<N>> getIngoingEdges(N node) {
        checkNode(node);

        Set<Edge<N>> set = new HashSet<>();

        for (N fromNode : nodeToIndex.keySet()) {
            if (representation.hasEdge(nodeToIndex.get(fromNode), nodeToIndex.get(node))) {
                set.add(Edge.of(fromNode, node, getWeight(fromNode, node)));
            }
        }

        return set;
    }

    /**
     * Gets the edge between the specified nodes.
     *
     * @param from the starting node
     * @param to the ending node
     * @return the edge, or null if no edge exists
     */
    @Override
    public Edge<N> getEdge(N from, N to) {
        //TODO: H1 c) - remove if implemented
        checkNode(from); checkNode(to); // Check if both nodes exist in the graph
        // Check if the edge exists in the representation
        if (!representation.hasEdge(nodeToIndex.get(from), nodeToIndex.get(to))) return null;
        // Returns the edge with its weight
        return Edge.of(from, to, getWeight(from, to));
    }

    /**
     * Calculates the weight of the edge that starts at the node {@code from} and ends at the node {@code to}.
     *
     * @param from the node the edge starts at.
     * @param to   the node the edge ends at.
     * @return the weight of the edge that starts at the node {@code from} and ends at the node {@code to}, or {@code null}
     * if there is no such edge.
     */
    private Integer getWeight(N from, N to) {
        return weights.get(from).get(to);
    }

    private void checkNode(N node) {
        if (!nodeToIndex.containsKey(node)) {
            throw new IllegalArgumentException("Node %s is not part of this graph".formatted(node));
        }
    }
}
