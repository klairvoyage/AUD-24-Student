package p2.gui;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.List;

/**
 * A box for displaying information about the current state of the animation represented by an {@link AnimationState}.
 * It automatically updates its content when the state changes.
 */
public class InfoBox extends VBox {

    /**
     * Constructs a new info box.
     *
     * @param state The state of the animation to display.
     */
    public InfoBox(AnimationState state) {
        super(5);

        setPadding(new Insets(5));
        setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, null, null)));
        setMaxWidth(300);

        getChildren().addAll(
            createStackTraceTableView(state.getStackTrace()),
            createExecutingLabel(state.getExecuting()),
            createOperationLabel(state.getOperation())
        );
    }

    private Label createOperationLabel(StringProperty operation) {
        Label label = new Label();
        label.textProperty().bind(new SimpleStringProperty("Last Operation: ").concat(operation));
        return label;
    }

    private Label createExecutingLabel(StringProperty executing) {
        Label label = new Label();
        label.textProperty().bind(new SimpleStringProperty("Currently Executing: ").concat(executing));
        return label;
    }

    private TableView<StackTraceElement> createStackTraceTableView(ObservableList<StackTraceElement> stackTrace) {

        TableView<StackTraceElement> tableView = new TableView<>(stackTrace);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_LAST_COLUMN);

        tableView.maxWidth(290);

        TableColumn<StackTraceElement, Integer> indexColumn = new TableColumn<>("#");
        indexColumn.setCellValueFactory(data -> new SimpleIntegerProperty(stackTrace.indexOf(data.getValue()) + 1).asObject());
        indexColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.1));

        TableColumn<StackTraceElement, String> nameColumn = new TableColumn<>("Class");
        nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getClassName().substring(data.getValue().getClassName().lastIndexOf('.') + 1)));
        nameColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.35));

        TableColumn<StackTraceElement, String> methodColumn = new TableColumn<>("Method");
        methodColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMethodName()));
        methodColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.35));

        TableColumn<StackTraceElement, Integer> lineColumn = new TableColumn<>("Line");
        lineColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getLineNumber()).asObject());
        lineColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.2));

        tableView.getColumns().addAll(List.of(indexColumn, nameColumn, methodColumn, lineColumn));

        tableView.setFixedCellSize(25);
        tableView.prefHeightProperty().bind(Bindings.size(tableView.getItems()).multiply(tableView.getFixedCellSize()).add(30));

        return tableView;
    }

}
