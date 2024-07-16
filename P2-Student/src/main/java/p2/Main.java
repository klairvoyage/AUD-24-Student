package p2;

import javafx.application.Application;
import p2.binarytree.AutoComplete;
import p2.gui.MyApplication;
import p2.gui.TreeStyle;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Main entry point in executing the program.
 */
public class Main {

    /**
     * Main entry point in executing the program.
     *
     * <h2>GUI Guide:</h2>
     * <p>
     * After starting the gui you can load an empty red-black or simple binary search tree by clicking the respective
     * buttons. When entering a string representation of a tree used in the test, that tree will be loaded instead.
     * <p>
     * When a tree is loaded, you can view it in the center. On the top right you can enter input values and execute
     * the respective operation by clicking the buttons below.
     * <ul>
     *     <li>Inserts: Invokes insert with the value given in "Value".</li>
     *     <li>In Order: Invokes inOrder with the value given in "Value" as the start node, "Max" as the maximum amount
     *     of values to return and {@code x -> x <= "Limit"} as the predicate.</li>
     *     <li>Find Next: Invokes findNext with the value given in "Value" as the start node, "Max" as the maximum amount
     *      of values to return and {@code x -> x <= "Limit"} as the predicate.
     *      <li>Join: Invokes join with the value given in "Value" as the join key, and "Join Tree" as other tree to
     *      join with.
     * </ul>
     * <p>
     * When the "Animate" checkbox at the bottom left is selected, the program will stop after each invocation of
     * {@code {get,set}{Left,Right,Parent}} and {@code setColor} and highlight the respective nodes. You can then continue the
     * animation by clicking the "Next Step" button at the bottom left. When stopped, the current stack trace and the
     * last performed operation is shown at the top right.
     * <p>
     * You can change the appearance and colors of the tree in the class {@link TreeStyle}.
     *
     * @param args program arguments
     */
    public static void main(String[] args) {

        // Uncomment the following line to run the AutoComplete example for task H3 d)
        // autoCompleteExample();

        Application.launch(MyApplication.class, args);
    }

    private static void autoCompleteExample() {
        String fileName = "words_alpha.txt";
        String prefix = "z";
        int max = 10;

        System.out.println(makeGreen("\n=== AutoComplete Example ==="));

        long lineCount;
        try (Stream<String> stream = Files.lines(Paths.get(Objects.requireNonNull(Main.class.getResource(fileName)).toURI()))) {
            lineCount = stream.count();
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }

        System.out.println(makeGreen("->") + " Input file: " + makeYellow(fileName)  + " with " + makeYellow(Long.toString(lineCount)) + " words");
        System.out.println(makeGreen("->") + " Prefix: " + makeYellow(prefix));
        System.out.println(makeGreen("->") + " Max: " + makeYellow(Integer.toString(max)));

        System.out.println(makeGreen("->") + " Running using " + makeYellow("Red-Black Tree") + ":");
        runAutoComplete(fileName, prefix, max, true);

        System.out.println(makeGreen("->") + " Running using " + makeYellow("Simple Binary Tree") + ":");
        runAutoComplete(fileName, prefix, max, false);

        System.out.println(makeGreen("============================"));
    }

    private static void runAutoComplete(String fileName, String prefix, int max, boolean useRBTree) {

        String running = "<Runnning>";

        System.out.print(makeGreen("    ->") + " Initialization time: ");
        System.out.print(makeRed(running));

        AutoComplete acRBTree = new AutoComplete(fileName, useRBTree);

        for (int i = 0; i < running.length(); i++) {
            System.out.print("\b");
        }

        System.out.printf(makeYellow("%.2fms\n".formatted(acRBTree.getInitializationTime() / 1000000d)));


        System.out.printf(makeGreen("    ->") + " Computation time:    ");
        System.out.print(makeRed(running));

        List<String> result = acRBTree.autoComplete(prefix, max);

        for (int i = 0; i < running.length(); i++) {
            System.out.print("\b");
        }

        System.out.printf(makeYellow("%.2fms\n".formatted(acRBTree.getLastComputationTime() / 1000000d)));
        System.out.printf(makeGreen("    ->") + " Results:             " + makeYellow("%s\n".formatted(result)));
    }

    private static String makeYellow(String str) {
        return "\u001B[33m" + str + "\u001B[0m";
    }

    private static String makeGreen(String str) {
        return "\u001B[32m" + str + "\u001B[0m";
    }

    private static String makeRed(String str) {
        return "\u001B[31m" + str + "\u001B[0m";
    }
}
