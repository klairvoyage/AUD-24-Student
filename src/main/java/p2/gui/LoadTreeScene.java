package p2.gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import p2.binarytree.TreeParser;

import java.util.function.Function;

/**
 * The initial scene for loading a {@link SimpleBinarySearchTreeAnimation} or a {@link RBTreeAnimation} from a string
 * representation of the tree.
 */
public class LoadTreeScene extends Scene {

    /**
     * Constructs a new load tree scene.
     *
     * @param primaryStage The stage used to display the scene.
     */
    public LoadTreeScene(Stage primaryStage) {
        super(new BorderPane());
        BorderPane root = (BorderPane) getRoot();

        root.setPrefSize(500, 150);

        VBox vBox = new VBox();

        Text description = new Text("Enter string representation of tree or leave empty for empty tree:");

        TextField textField = new TextField();
        textField.setMaxWidth(description.getLayoutBounds().getWidth());

        Button loadRBTreeButton = new Button("Load Red-Black Tree");
        Button loadBSTButton = new Button("Load Simple Binary Search Tree");

        HBox buttonBox = new HBox(loadRBTreeButton, loadBSTButton);
        buttonBox.setSpacing(5);

        loadRBTreeButton.setOnAction(event -> {
            if (textField.getText().isEmpty()) {
                loadScene(primaryStage, new RBTreeAnimation<Integer>(), Integer::parseInt);
            } else if (textField.getText().chars().anyMatch(Character::isDigit)) {
                Function<String, Integer> inputParser = Integer::parseInt;
                loadScene(primaryStage, TreeParser.parseRBTree(textField.getText(), inputParser, new RBTreeAnimation<>()), inputParser);
            } else {
                Function<String, String> inputParser = Function.identity();
                loadScene(primaryStage, TreeParser.parseRBTree(textField.getText(), inputParser, new RBTreeAnimation<>()), inputParser);
            }
        });

        loadBSTButton.setOnAction(event -> {
            if (textField.getText().isEmpty()) {
                loadScene(primaryStage, new SimpleBinarySearchTreeAnimation<Integer>(), Integer::parseInt);
            } else if (textField.getText().chars().anyMatch(Character::isDigit)) {
                Function<String, Integer> inputParser = Integer::parseInt;
                loadScene(primaryStage, TreeParser.parseBST(textField.getText(), inputParser, new SimpleBinarySearchTreeAnimation<>()), inputParser);
            } else {
                Function<String, String> inputParser = Function.identity();
                loadScene(primaryStage, TreeParser.parseBST(textField.getText(), inputParser, new SimpleBinarySearchTreeAnimation<>()), inputParser);
            }
        });

        vBox.getChildren().addAll(description, textField, buttonBox);

        root.setCenter(vBox);

        Text title = new Text("Load Binary Tree");
        title.setFont(new Font(40));
        root.setTop(title);

        BorderPane.setAlignment(title, Pos.CENTER);
        vBox.setAlignment(Pos.CENTER);
        buttonBox.setAlignment(Pos.CENTER);
    }

    private <T extends Comparable<T>> void loadScene(Stage primaryStage, AnimatedBinaryTree<T> tree, Function<String, T> inputParser) {
        BinaryTreeAnimationScene<T> animationScene = new BinaryTreeAnimationScene<>(primaryStage, tree, inputParser);

        MyApplication.currentScene = animationScene;

        tree.init(animationScene);

        primaryStage.setScene(animationScene);
        primaryStage.show();
    }

}
