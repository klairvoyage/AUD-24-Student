package p2.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Stores the current state of the animation, e.g., the last performed operation, the current executing operation, and the
 * current stack trace
 */
public class AnimationState {

    private final StringProperty operation = new SimpleStringProperty();
    private final StringProperty executing = new SimpleStringProperty();
    private final StringProperty exception = new SimpleStringProperty();
    private final ObservableList<StackTraceElement> stackTrace = FXCollections.observableArrayList();

    public StringProperty getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation.set(operation);
    }

    public StringProperty getExecuting() {
        return executing;
    }

    public void setExecuting(String executing) {
        this.executing.set(executing);
    }

    public StringProperty getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception.set(exception.getMessage());
    }

    public ObservableList<StackTraceElement> getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(StackTraceElement[] stackTrace) {
        this.stackTrace.setAll(stackTrace);
    }

    public void clear() {
        operation.set("<no operation>");
        stackTrace.clear();
    }
}
