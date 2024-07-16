package p2.binarytree;

/**
 * A node in a {@link SimpleBinarySearchTree}.
 *
 * @param <T> the type of the key in the node.
 * @see SimpleBinarySearchTree
 * @see AbstractBinaryNode
 */
public class BSTNode<T extends Comparable<T>> extends AbstractBinaryNode<T, BSTNode<T>> {

    public BSTNode(T key) {
        super(key);
    }

    @Override
    public String toString() {
        return "BSTNode{" +
            "key=" + getKey() +
            '}';
    }
}
