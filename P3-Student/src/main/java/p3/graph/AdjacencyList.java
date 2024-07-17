package p3.graph;

import p3.solver.MSTCalculator;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import static org.tudalgo.algoutils.student.Student.crash;

/**
 * A representation of a directed graph using an array of linked lists.
 * <p>
 * The array is accessed with the indices of the nodes. The list at index {@code i} contains the indices of the nodes
 * the node with index {@code i} is connected to.
 *
 * @see AdjacencyRepresentation
 */
public class AdjacencyList implements AdjacencyRepresentation {

    /**
     * A factory that creates an empty {@link AdjacencyList} with the given initial size.
     */
    public static final AdjacencyRepresentation.Factory FACTORY = AdjacencyList::new;

    /**
     * The underlying array that stores the adjacencyList.
     */
    private LinkedList<Integer>[] adjacencyList;

    /**
     * Creates a new {@link AdjacencyList} with the given size.
     * <p>
     * Initially, the adjacencyList is empty, i.e., no connections between nodes exist.
     *
     * @param size The amount of nodes in the graph.
     */
    @SuppressWarnings("unchecked")
    public AdjacencyList(int size) {
        adjacencyList = new LinkedList[size];

        for (int i = 0; i < size; i++) {
            adjacencyList[i] = new LinkedList<>();
        }
    }

    /**
     * Adds a directed edge from the 'from' vertex to the 'to' vertex.
     *
     * @param from the starting vertex
     * @param to the ending vertex
     * @throws IndexOutOfBoundsException if the vertex indices are out of bounds
     */
    @Override
    public void addEdge(int from, int to) {
        //TODO: H1 b) - remove if implemented
        if (from < 0 || from >= adjacencyList.length || to < 0 || to >= adjacencyList.length)
            throw new IndexOutOfBoundsException();
        adjacencyList[from].add(to); // Add the edge to the adjacency list, if indices were valid
    }

    /**
     * Checks if there is a directed edge from the 'from' vertex to the 'to' vertex.
     *
     * @param from the starting vertex
     * @param to the ending vertex
     * @return true if there is an edge from 'from' to 'to', false otherwise
     * @throws IndexOutOfBoundsException if the vertex indices are out of bounds
     */
    @Override
    public boolean hasEdge(int from, int to) {
        //TODO: H1 b) - remove if implemented
        if (from < 0 || from >= adjacencyList.length || to < 0 || to >= adjacencyList.length)
            throw new IndexOutOfBoundsException();
        return adjacencyList[from].contains(to); // Check if edge exists in the adjacency list, if indices were valid
    }

    /**
     * Returns a set of indices of all vertices adjacent to the given vertex.
     *
     * @param index the vertex whose adjacent vertices are to be found
     * @return a set of indices of adjacent vertices
     * @throws IndexOutOfBoundsException if the vertex index is out of bounds
     */
    @Override
    public Set<Integer> getAdjacentIndices(int index) {
        //TODO: H1 b) - remove if implemented
        if (index < 0 || index >= adjacencyList.length) throw new IndexOutOfBoundsException();
        return new HashSet<>(adjacencyList[index]); // Return the set of adjacent vertices
    }

    @Override
    public int size() {
        return adjacencyList.length;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void grow() {
        LinkedList<Integer>[] newAdjacencyList = new LinkedList[adjacencyList.length + 1];

        System.arraycopy(adjacencyList, 0, newAdjacencyList, 0, adjacencyList.length);

        newAdjacencyList[adjacencyList.length] = new LinkedList<>();

        adjacencyList = newAdjacencyList;
    }

    /**
     * A factory for creating new instances of {@link AdjacencyList}.
     */
    interface Factory {

        /**
         * Create a new instance of {@link AdjacencyList} for the given graph.
         *
         * @param graph the graph to calculate the MST for.
         * @param <N>   The type of the nodes in the graph.
         * @return a new instance of {@link MSTCalculator}.
         */
        <N> MSTCalculator<N> create(Graph<N> graph);
    }
}
