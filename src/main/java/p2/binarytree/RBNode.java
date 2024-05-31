package p2.binarytree;

/**
 * A node in a {@link RBTree}.
 * Additionally, to the normal properties of a {@link AbstractBinaryNode}, it also has a {@link Color},
 * i.e. it can be red or black.
 *
 * @param <T> the type of the key in the node.
 * @see RBTree
 * @see AbstractBinaryNode
 */
public class RBNode<T extends Comparable<T>> extends AbstractBinaryNode<T, RBNode<T>> {

    /**
     * The color of the node.
     */
    private Color color;

    /**
     * Creates a new node with the given key and color.
     *
     * @param key   the key of the node.
     * @param color the color of the node.
     */
    public RBNode(T key, Color color) {
        super(key);
        this.color = color;
    }


    //--- methods for color management ---//


    /**
     * Returns the color of the node.
     *
     * @return the color of the node.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Checks whether the node is red.
     *
     * @return {@code true} if the node is red, {@code false} otherwise.
     */
    public boolean isRed() {
        return color == Color.RED;
    }

    /**
     * Checks whether the node is black.
     *
     * @return {@code true} if the node is black, {@code false} otherwise.
     */
    public boolean isBlack() {
        return color == Color.BLACK;
    }

    /**
     * Sets the color of the node.
     *
     * @param color the new color of the node.
     */
    protected void setColor(Color color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "RBNode{" +
            "key=" + getKey() +
            ", color=" + color +
            '}';
    }

    /**
     * Creates a string representation of the node and its children and appends it to the given builder.
     * It has the format {@code [left,key,color,right]}, where {@code left} and {@code right} are the string
     * representations of the left and right children, respectively. If a child is {@code null}, it is represented
     * by an empty string. The color is represented by {@code "R"} for red, {@code "B"} for black and {@code "null"}
     * for {@code null}.
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

        builder.append(",")
            .append(getKey())
            .append(",")
            .append(color == Color.RED ? "R" : color == Color.BLACK ? "B" : "null")
            .append(",");

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
