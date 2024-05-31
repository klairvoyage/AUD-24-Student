package p2.binarytree;

import java.util.function.Function;

/**
 * Utility class for parsing strings that represent either red-black tree or a binary search tree.
 */
@SuppressWarnings("unused")
public class TreeParser {

    /**
     * Parses a red-black tree from a string.
     *
     * @param input     The string representation of the tree.
     * @param keyParser The function used to convert the string representation of the key to its actual value.
     * @return The parsed red-black tree.
     */
    public static <T extends Comparable<T>> RBTree<T> parseRBTree(String input, Function<String, T> keyParser) {
        return parseRBTree(input, keyParser, new RBTree<>());
    }

    /**
     * Parses a red-black tree from a string.
     *
     * @param input     The string representation of the tree.
     * @param keyParser The function used to convert the string representation of the key to its actual value.
     * @param tree      An empty tree instance used to store the created tree.
     * @return The parsed red-black tree.
     */
    public static <T extends Comparable<T>, TR extends RBTree<T>> TR parseRBTree(
        String input,
        Function<String, T> keyParser,
        TR tree) {
        return parse(input, keyParser, tree, true);
    }

    /**
     * Parses a binary search tree from a string.
     *
     * @param input     The string representation of the tree.
     * @param keyParser The function used to convert the string representation of the key to its actual value.
     * @return The parsed binary search tree.
     */
    public static <T extends Comparable<T>> SimpleBinarySearchTree<T> parseBST(String input, Function<String, T> keyParser) {
        return parseBST(input, keyParser, new SimpleBinarySearchTree<>());
    }

    /**
     * Parses a binary search tree from a string.
     *
     * @param input     The string representation of the tree.
     * @param keyParser The function used to convert the string representation of the key to its actual value.
     * @param tree      An empty tree instance used to store the created tree.
     * @return The parsed binary search tree.
     */
    public static <T extends Comparable<T>, TR extends SimpleBinarySearchTree<T>> TR parseBST(
        String input,
        Function<String, T> keyParser,
        TR tree) {
        return parse(input, keyParser, tree, false);
    }

    @SuppressWarnings("unchecked")
    private static <T extends Comparable<T>,
        N extends AbstractBinaryNode<T, N>,
        TR extends AbstractBinarySearchTree<T, N>> TR parse(
        String input,
        Function<String, T> keyParser,
        TR tree,
        boolean rb) {

        StringReader reader = new StringReader(input);

        N node = null;

        if (!input.equals("[]")) node = parseNode(reader, keyParser, tree, rb);

        tree.root = node;

        if (rb) {
            if (tree.root != null) tree.root.setParent((N) ((RBTree<T>) tree).sentinel);
        }

        return tree;
    }

    @SuppressWarnings("unchecked")
    private static <T extends Comparable<T>, N extends AbstractBinaryNode<T, N>> N parseNode(
        StringReader reader,
        Function<String, T> keyParser,
        AbstractBinarySearchTree<T, N> tree,
        boolean rbNode) {

        reader.accept('[');

        N left = null;
        N right = null;
        Color color = null;

        //left
        if (reader.peek() == '[') left = parseNode(reader, keyParser, tree, rbNode);
        reader.accept(',');

        //value
        T value = keyParser.apply(reader.readUntil(','));
        reader.accept(',');

        //color
        if (rbNode) {
            String colorString = reader.readUntil(',');

            color = switch (colorString) {
                case "R" -> Color.RED;
                case "B" -> Color.BLACK;
                case "null" -> null;
                default -> throw new IllegalArgumentException("Invalid color: " + colorString);
            };

            reader.accept(',');
        }

        //right
        if (reader.peek() == '[') right = parseNode(reader, keyParser, tree, rbNode);

        reader.accept(']');

        N node = tree.createNode(value);

        if (rbNode) ((RBNode<T>) node).setColor(color);

        node.setLeft(left);
        node.setRight(right);

        if (left != null) left.setParent(node);
        if (right != null) right.setParent(node);

        return node;
    }

    private static class StringReader {

        private final String input;
        private int index;

        public StringReader(String input) {
            this.input = input;
        }

        public boolean hasNext() {
            skipWhitespace();
            return index < input.length();
        }

        public char next() {
            skipWhitespace();
            return input.charAt(index++);
        }

        public char peek() {
            skipWhitespace();
            return input.charAt(index);
        }

        public void skipWhitespace() {
            while (index < input.length() && Character.isWhitespace(input.charAt(index))) {
                index++;
            }
        }

        public void accept(char expected) {
            char next = next();
            if (next != expected) {
                throw new IllegalArgumentException("Expected: `" + expected + "`, found: `" + next + "`");
            }
        }

        public String readUntil(char delimiter) {
            StringBuilder builder = new StringBuilder();
            while (hasNext() && peek() != delimiter) {
                builder.append(next());
            }
            return builder.toString();
        }

    }

}
