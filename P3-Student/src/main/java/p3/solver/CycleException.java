package p3.solver;

/**
 * An exception used by the {@link BellmanFordPathCalculator} and {@link TopologicalSort} class to indicate that a
 * (negative) cycle in the graph was found.
 */
public class CycleException extends RuntimeException {

    /**
     * Creates a new exception with the given message.
     *
     * @param message the message of the exception.
     */
    public CycleException(String message) {
        super(message);
    }
}
