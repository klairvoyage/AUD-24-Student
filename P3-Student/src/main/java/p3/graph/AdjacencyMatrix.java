package p3.graph;

import java.util.HashSet;
import java.util.Set;

import static org.tudalgo.algoutils.student.Student.crash;

/**
 * A light wrapper around a 2D array of boolean that represents an adjacency matrix.
 * <p>
 * The adjacency matrix is a square matrix that represents a directed graph. The entry at index (i, j) is {@code true}, iff
 * a connection from the node with index {@code i} to the node with index {@code j} exists. Otherwise, the entry is
 * {@code false}.
 *
 * @see AdjacencyRepresentation
 */
public class AdjacencyMatrix implements AdjacencyRepresentation {

    /**
     * A factory that creates an empty {@link AdjacencyMatrix} with the given initial size.
     */
    public static final Factory FACTORY = AdjacencyList::new;

    /**
     * The underlying array that stores the adjacency matrix.
     */
    private boolean[][] matrix;

    /**
     * Constructs a new {@link AdjacencyMatrix} with the given size.
     * <p>
     * Initially, the matrix is empty, i.e., no connections between nodes exist.
     *
     * @param size The amount of nodes in the graph.
     */
    public AdjacencyMatrix(int size) {
        matrix = new boolean[size][size];
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
        //TODO: H1 a) - remove if implemented
        if (from < 0 || from >= matrix.length || to < 0 || to >= matrix.length) throw new IndexOutOfBoundsException();
        matrix[from][to] = true; // Set the entry in the matrix to true, if the indices were valid
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
        //TODO: H1 a) - remove if implemented
        if (from < 0 || from >= matrix.length || to < 0 || to >= matrix.length) throw new IndexOutOfBoundsException();
        return matrix[from][to]; // Return whether the edge exists, if the indices were valid
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
        //TODO: H1 a) - remove if implemented
        if (index < 0 || index >= matrix.length) throw new IndexOutOfBoundsException(); // Check if the index is valid
        Set<Integer> adjacentIndices = new HashSet<>(); // Create a set to store the adjacent vertices
        // Iterate through the columns of the row corresponding to the index to find adjacent vertices
        for (int i=0; i<matrix.length; i++) if (hasEdge(index, i)) adjacentIndices.add(i);
        return adjacentIndices;
    }

    @Override
    public int size() {
        return matrix.length;
    }

    @Override
    public void grow() {

        // Create a new matrix with one more row and column
        int newSize = matrix.length + 1;
        boolean[][] newMatrix = new boolean[newSize][newSize];

        // Copy the old matrix into the new matrix
        for (int i = 0; i < matrix.length; i++) {
            System.arraycopy(matrix[i], 0, newMatrix[i], 0, matrix[i].length);
        }

        matrix = newMatrix;
    }
}
