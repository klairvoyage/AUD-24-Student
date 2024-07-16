package p2.gui;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main entry point in executing the GUI.
 */
public class MyApplication extends Application {

    /**
     * The current {@link BinaryTreeAnimationScene} that is displayed. This is used to stop the animation when the
     * application is closed.
     */
    public static BinaryTreeAnimationScene<?> currentScene = null;

    @Override
    public void start(Stage primaryStage) {

        LoadTreeScene loadTreeScene = new LoadTreeScene(primaryStage);

        primaryStage.setScene(loadTreeScene);
        primaryStage.setTitle("Binary Tree Animation");

        primaryStage.show();
    }

    @Override
    public void stop() {
        if (currentScene != null) currentScene.stopAnimation();
    }

}
