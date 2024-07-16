package p2.binarytree;

/**
 * A base implementation for a {@link BinaryNode} in a {@link AbstractBinarySearchTree}.
 *
 * @param <T> the type of the key in the node.
 * @param <N> the concrete type of the node, e.g., {@link BSTNode} or {@link RBNode}.
 * @see BinaryNode
 * @see AbstractBinarySearchTree
 */
public abstract class AbstractBinaryNode<T extends Comparable<T>, N extends AbstractBinaryNode<T, N>> implements BinaryNode<T> {

    /**
     * The key of the node.
     */
    private final T key;

    /**
     * The left child of the node. Can be {@code null} if the node has no left child.
     */
    private N left;

    /**
     * The right child of the node. Can be {@code null} if the node has no right child.
     */
    private N right;

    /**
     * The parent of the node. Can be {@code null} if the node has no parent.
     */
    private N parent;

    /**
     * Creates a new node with the given key.
     *
     * @param key the key of the node.
     */
    public AbstractBinaryNode(T key) {
        this.key = key;
    }

    @Override
    public T getKey() {
        return key;
    }

    @Override
    public N getLeft() {
        return left;
    }

    @Override
    public boolean hasLeft() {
        return left != null;
    }

    /**
     * Sets the left child of the node to the given node.
     *
     * @param left the new left child of the node.
     */
    protected void setLeft(N left) {
        this.left = left;
    }

    @Override
    public N getRight() {
        return right;
    }

    @Override
    public boolean hasRight() {
        return right != null;
    }

    /**
     * Sets the right child of the node to the given node.
     *
     * @param right the new right child of the node.
     */
    protected void setRight(N right) {
        this.right = right;
    }

    @Override
    public N getParent() {
        return parent;
    }

    /**
     * Sets the parent of the node to the given node.
     *
     * @param parent the new parent of the node.
     */
    protected void setParent(N parent) {
        this.parent = parent;
    }

    /**
     * Creates a string representation of the node and its children and appends it to the given builder.
     * It has the format {@code [left,key,right]}, where {@code left} and {@code right} are the string
     * representations of the left and right children, respectively. If a child is {@code null}, it is represented
     * by an empty string. Additional information can be appended by overriding this method.
     *
     * @param builder the builder to append the string representation to.
     */
    protected void buildString(StringBuilder builder) {
        builder.append("[");

        if (getLeft() != null) {
            if (getLeft() == this) {
                builder.append("<left child is reference to itself>");
            } else {
                getLeft().buildString(builder);
            }
        }

        builder.append(",").append(getKey()).append(",");

        if (getRight() != null) {
            if (getRight() == this) {
                builder.append("<right child is reference to itself>");
            } else {
                getRight().buildString(builder);
            }
        }

        builder.append("]");
    }
}
