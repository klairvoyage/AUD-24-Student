package p2.binarytree;

/**
 * An exception used by the {@link RBTreeChecker} class to indicate that an RBTree is not valid.
 */
public class RBTreeException extends RuntimeException {

    /**
     * Creates a new exception with the given message.
     *
     * @param message the message of the exception.
     */
    public RBTreeException(String message) {
        super(message);
    }
}
