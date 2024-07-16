package p2.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * A pane for controlling the animation.
 * It, for example, contains a button for stepping through the animation and a button for centering the graph.
 */
public class ControlBox<T extends Comparable<T>> extends HBox {

    private final Button nextStepButton;

    /**
     * Constructs a new control box.
     */
    public ControlBox(Stage primaryStage, BinaryTreeAnimationScene<T> animationScene) {
        super(5);

        setPadding(new Insets(5));
        setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, null, null)));

        Button backButton = new Button("Back");
        nextStepButton = new Button("Next Step");
        Button zoomOutButton = new Button("Zoom Out");
        Button zoomInButton = new Button("Zoom In");
        Button centerButton = new Button("Center Tree");
        Button printTreeButton = new Button("Print Tree");
        CheckBox animationCheckBox = new CheckBox("Animate");

        animationCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                animationScene.getAnimation().turnOnAnimation();
            } else {
                animationScene.getAnimation().turnOffAnimation();
            }
        });

        animationCheckBox.setSelected(true);

        getChildren().addAll(backButton, nextStepButton, centerButton, zoomInButton, zoomOutButton, printTreeButton, animationCheckBox);
        setAlignment(Pos.CENTER_LEFT);

        backButton.setOnAction(event -> {
            animationScene.stopAnimation();
            MyApplication.currentScene = null;
            primaryStage.setScene(new LoadTreeScene(primaryStage));
        });

        nextStepButton.setDisable(true);
        nextStepButton.setOnAction(event -> {
            synchronized (animationScene.getAnimation()) {
                animationScene.getAnimation().notify();
            }
        });

        centerButton.setOnAction(event -> animationScene.getTreePane().center());
        zoomInButton.setOnAction(event -> animationScene.getTreePane().zoomIn());
        zoomOutButton.setOnAction(event -> animationScene.getTreePane().zoomOut());

        printTreeButton.setOnAction(event -> System.out.println("Current Tree: " + animationScene.getAnimation().toString()));
    }

    public void enableNextStepButton() {
        nextStepButton.setDisable(false);
    }

    public void disableNextStepButton() {
        nextStepButton.setDisable(true);
    }

}
