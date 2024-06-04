package p2.gui;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import p2.binarytree.BinaryNode;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A scene for displaying an {@link AnimatedBinaryTree}.
 */
public class BinaryTreeAnimationScene<T extends Comparable<T>> extends Scene {

    private final BorderPane root;

    private final TreePane treePane = new TreePane();
    private final OperationBox<T> operationBox;
    private final InfoBox infoBox;
    private final ControlBox<T> controlBox;

    private final AnimationState animationState = new AnimationState();

    private final AnimatedBinaryTree<T> animation;
    private Thread animationThread;

    private BinaryNode<T> lastSource;
    private BinaryNode<T> lastTarget;

    /**
     * Constructs a new animation scene.
     */
    public BinaryTreeAnimationScene(Stage primaryStage, AnimatedBinaryTree<T> animation, Function<String, T> inputParser) {
        super(new BorderPane());
        root = (BorderPane) getRoot();

        this.animation = animation;

        root.setPrefSize(700, 700);

        root.setCenter(treePane);

        controlBox = new ControlBox<>(primaryStage, this);
        root.setBottom(controlBox);

        infoBox = new InfoBox(animationState);

        operationBox = new OperationBox<>(this, inputParser);
        root.setRight(operationBox);

        animation.runWithoutAnimation(() -> treePane.setTree(animation.getRoot()));
    }

    public void refresh(BinaryNode<T> source, BinaryNode<T> target) {
        resetOldHighlight();

        lastSource = source;
        lastTarget = target;

        if (treePane.containsNode(source)) treePane.highlightNode(source);

        if (treePane.containsEdge(source, target)) treePane.highlightEdge(source, target);
        else if (treePane.containsEdge(target, source)) treePane.highlightEdge(target, source);
    }

    public void refresh(BinaryNode<T> node) {
        resetOldHighlight();

        lastSource = node;
        lastTarget = null;

        if (treePane.containsNode(node)) treePane.highlightNode(node);
    }

    private void resetOldHighlight() {
        if (treePane.containsNode(lastSource)) treePane.resetNode(lastSource);

        if (treePane.containsEdge(lastSource, lastTarget)) treePane.resetEdge(lastSource, lastTarget);
        else if (treePane.containsEdge(lastTarget, lastSource)) treePane.resetEdge(lastTarget, lastSource);
    }

    public void stopAnimation() {
        if (animationThread != null) {
            animation.finishWithNextStep();
            synchronized (animation) {
                animation.notify();
            }
        }
        root.setRight(operationBox);
    }

    public void startAnimation(Consumer<AnimatedBinaryTree<T>> animation) {
        stopAnimation();

        this.animation.disableFinishWithNextStep();

        animationState.clear();
        if (this.animation.isAnimating()) {
            root.setRight(infoBox);
            controlBox.enableNextStepButton();
        }

        animationThread = new Thread(() -> {
            try {
                animation.accept(this.animation);
                Platform.runLater(() -> {
                    root.setRight(operationBox);
                    this.animation.runWithoutAnimation(() -> treePane.setTree(this.animation.getRoot()));
                    operationBox.clearInputs();
                });
            } catch (RuntimeException e) {
                animationState.setException(e);
                animationState.setStackTrace(e.getStackTrace());
                Platform.runLater(infoBox::showException);
            }

            Platform.runLater(controlBox::disableNextStepButton);
        });

        animationThread.start();
    }

    public TreePane getTreePane() {
        return treePane;
    }

    public AnimatedBinaryTree<T> getAnimation() {
        return animation;
    }

    public AnimationState getAnimationState() {
        return animationState;
    }
}
