package p3.graph;

import java.util.HashSet;
import java.util.Set;

/**
 * A directed, weighted, immutable graph with nodes of type {@code N}.
 * <p>
 * A graph is a set of nodes and a set of weighted {@linkplain Edge Edges}.
 * Each {@link Edge} connects two nodes and has a weight.
 * An {@link Edge} from node {@code A} to node {@code B} is different from an {@link Edge} from node {@code B} to
 * node {@code A}. The existence of one of them does not imply the existence of the other (the graph is directed).
 *
 * <p>
 * A graph is in general not guaranteed to be mutable. For mutable graphs, see {@link MutableGraph}.
 *
 * @param <N> the type of the nodes in this graph.
 */
public interface Graph<N> {

    /**
     * Returns all nodes in this graph.
     *
     * @return a set of all nodes in this graph.
     */
    Set<N> getNodes();

    /**
     * Returns all edges in this graph.
     *
     * @return a set of all edges in this graph.
     */
    Set<Edge<N>> getEdges();

    /**
     * Returns all edges that start at the given node. If there are no such edges, an empty set is returned.
     * <p>
     * For every edge {@code e} in the returned set, {@code e.from()} will return the given node.
     *
     * @param node the node to get the outgoing edges for.
     * @return a set of all edges that start at the given node.
     * @throws IllegalArgumentException if the given node is not in the graph.
     */
    Set<Edge<N>> getOutgoingEdges(N node);

    /**
     * Returns all edges that end at the given node. If there are no such edges, an empty set is returned.
     * <p>
     * For every edge {@code e} in the returned set, {@code e.to()} will return the given node.
     *
     * @param node the node to get the ingoing edges for.
     * @return a set of all edges that end at the given node.
     * @throws IllegalArgumentException if the given node is not in the graph.
     */
    Set<Edge<N>> getIngoingEdges(N node);

    /**
     * Returns all adjacent edges at the given node.
     * <p>
     * For every edge {@code e} in the returned set, {@code e.to()} or {@code e.from()} will return the given node.
     *
     * @param node the node to get the adjacent edges for.
     * @return a set of all edges that are adjacent to the given node.
     */
    default Set<Edge<N>> getAllAdjacentEdges(N node) {
        Set<Edge<N>> adjacentEdges = getOutgoingEdges(node);
        adjacentEdges.addAll(getIngoingEdges(node));
        return adjacentEdges;
    }

    /**
     * Returns all adjacent nodes at the given node
     * <p>
     * For every node {@code n} in the returned set, there exists an edge {@code e} such that {@code e.from() == node}
     * and {@code e.to() == n}.
     *
     * @param node the node to get the adjacent nodes for.
     * @return a set of all nodes that are adjacent to the given node.
     */
    default Set<N> getAdjacentNodes(N node) {
        Set<N> adjacentNodes = new HashSet<>();

        for (Edge<N> outgoingEdge : getOutgoingEdges(node)) {
            adjacentNodes.add(outgoingEdge.to());
        }

        return adjacentNodes;
    }

    /**
     * Returns the edge that starts at the given node and ends at the other given node.
     *
     * @param from the node that the edge starts at.
     * @param to   the node that the edge ends at.
     * @return the edge that starts at the given node and ends at the other given node or {@code null} if no such edge exists.
     * @throws IllegalArgumentException if at least one of the nodes is not in the graph.
     */
    Edge<N> getEdge(N from, N to);

    /**
     * Creates a new {@link Graph} with the given nodes and edges.
     *
     * @param nodes the nodes in the graph.
     * @param edges the edges in the graph.
     * @param <N>   the type of the nodes in the graph.
     * @return a new, immutable graph with the given nodes and edges.
     */
    static <N> Graph<N> of(Set<N> nodes, Set<Edge<N>> edges) {
        return new BasicGraph<>(nodes, edges);
    }

    /**
     * Creates a new {@link Graph} that does not contain any edges or nodes.
     *
     * @param <N> the type of the nodes in the graph.
     * @return an empty, mutable graph.
     */
    static <N> Graph<N> empty() {
        return Graph.of(Set.of(), Set.of());
    }
}
