package p2.binarytree;

import p2.Main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.tudalgo.algoutils.student.Student.crash;

/**
 * A simple implementation of an auto-complete system using a {@link BinarySearchTree}.
 * <p>
 * It works by storing a list of common words in a {@link BinarySearchTree} and then searching for words that start with the given
 * prefix in the tree. It is recommended to use a red-black tree for performance reasons.
 */
public class AutoComplete {

    /**
     * The time it took to initialize the {@link BinarySearchTree} in nanoseconds.
     */
    private long initializationTime;


    /**
     * The time it took to compute the last autocomplete suggestion in nanoseconds.
     */
    private long lastComputationTime = -1;

    /**
     * The {@link BinarySearchTree} used to store and retrieve the set of possible words than can be used.
     */
    private final BinarySearchTree<String> searchTree;

    /**
     * Creates a new {@link AutoComplete} instance that uses the given {@link BinarySearchTree} to search for
     * possible suggestions.
     *
     * @param searchTree The {@link BinarySearchTree} to use for searching for suggestions.
     */
    public AutoComplete(BinarySearchTree<String> searchTree) {
        this.searchTree = searchTree;
    }

    /**
     * Creates a new {@link AutoComplete} instance that uses the words in the given File to search for possible suggestions.
     * <p>
     * It uses an {@link RBTree} internally to store the used words.
     *
     * @param fileName The name of the file that contains a list of all words that are supposed to be used.
     */
    public AutoComplete(String fileName) {
        this(fileName, true);
    }

    /**
     * Creates a new {@link AutoComplete} instance that uses the words in the given File to search for possible suggestions.
     *
     * @param useRBTree If, {@code true}, a {@link RBTree} is internally used to store the words. Otherwise, a
     *                  {@link SimpleBinarySearchTree} is used.
     * @param fileName  The name of the file that contains a list of all words that are supposed to be used.
     */
    public AutoComplete(String fileName, boolean useRBTree) {
        searchTree = useRBTree ? new RBTree<>() : new SimpleBinarySearchTree<>();
        readFile(fileName);
    }

    private void readFile(String fileName) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
            Objects.requireNonNull(Main.class.getResourceAsStream(fileName))))) {

            String line;
            long startTime = System.nanoTime();

            while ((line = br.readLine()) != null) {
                searchTree.insert(line);
            }

            initializationTime = System.nanoTime() - startTime;

        } catch (IOException e) {
            throw new RuntimeException("Failed to read words from file " + fileName, e);
        }
    }

    /**
     * Returns a list of suggestions to complete the given prefix string.
     * <p>
     * It returns at most {@code max} suggestions. All returned suggestions start with the given prefix.
     *
     * @param prefix the prefix to complete.
     * @param max    the maximum number of suggestions to return.
     * @return a list of suggestions to complete the given prefix string.
     */
    public List<String> autoComplete(String prefix, int max) {
        long startTime = System.nanoTime();

        List<String> result = new ArrayList<>();

        BinaryNode<String> prefixNode = prefixSearch(prefix);

        if (prefixNode == null) return List.of();

        searchTree.findNext(prefixNode, result, max, str -> str.startsWith(prefix));

        lastComputationTime = System.nanoTime() - startTime;

        return result;
    }

    /**
     * Finds the smallest node in the tree that starts with the given prefix or {@code null} if no such node exists.
     *
     * @param prefix the prefix to search for.
     * @return the smallest node in the tree that starts with the given prefix.
     */
    public BinaryNode<String> prefixSearch(String prefix) {
        //TODO: H3 c) - remove if implemented
        return prefixSearchRecursive(searchTree.getRoot(), prefix, null); // Start recursive search from root
    }

    /**
     * Recursively searches for the smallest node that starts with the given prefix or {@code null} if no such node exists.
     *
     * @param node the current node.
     * @param prefix the prefix to search for.
     * @param bestMatch the current best match.
     * @return the smallest node in the tree that starts with the given prefix.
     */
    private BinaryNode<String> prefixSearchRecursive(BinaryNode<String> node, String prefix, BinaryNode<String> bestMatch) {
        if (node == null) return bestMatch; // Stop Condition: if the node is null, return the best match found so far
        // Check if the current node's key starts with the prefix
        if (node.getKey().startsWith(prefix)) {
            // If it does, update the best match to the current node
            bestMatch = node;
            // Continue searching in the left subtree to find the smallest node
            return prefixSearchRecursive(node.getLeft(), prefix, bestMatch);
        }
        // If the current node's key is greater than the prefix, search in the left subtree
        else if (node.getKey().compareTo(prefix) > 0) return prefixSearchRecursive(node.getLeft(), prefix, bestMatch);
        // If the current node's key is less than the prefix, search in the right subtree
        else return prefixSearchRecursive(node.getRight(), prefix, bestMatch);
    }

    /**
     * @return the {@link BinarySearchTree} that is internally used to store and search for possible suggestions.
     */
    public BinarySearchTree<String> getSearchTree() {
        return searchTree;
    }

    /**
     * @return the time it took to initialize the {@link BinarySearchTree} in nanoseconds.
     */
    public long getInitializationTime() {
        return initializationTime;
    }

    /**
     * @return the time it took to compute the last autocomplete suggestion in nanoseconds.
     */
    public long getLastComputationTime() {
        return lastComputationTime;
    }
}
