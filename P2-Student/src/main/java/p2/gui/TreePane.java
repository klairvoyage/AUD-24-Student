package p2.gui;

import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;
import p2.binarytree.AbstractBinarySearchTree;
import p2.binarytree.BinaryNode;
import p2.binarytree.RBNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static p2.gui.TreeStyle.ADD_DASHES;
import static p2.gui.TreeStyle.BLACK_NODE_COLOR;
import static p2.gui.TreeStyle.DEFAULT_EDGE_COLOR;
import static p2.gui.TreeStyle.DEFAULT_NODE_COLOR;
import static p2.gui.TreeStyle.EDGE_STROKE_WIDTH;
import static p2.gui.TreeStyle.GRID_FIVE_TICKS_WIDTH;
import static p2.gui.TreeStyle.GRID_LINE_COLOR;
import static p2.gui.TreeStyle.GRID_TEN_TICKS_WIDTH;
import static p2.gui.TreeStyle.HIGHLIGHT_COLOR;
import static p2.gui.TreeStyle.NODE_DIAMETER;
import static p2.gui.TreeStyle.NODE_DISTANCE;
import static p2.gui.TreeStyle.NODE_STROKE_WIDTH;
import static p2.gui.TreeStyle.NODE_VERTICAL_DISTANCE;
import static p2.gui.TreeStyle.RED_NODE_COLOR;
import static p2.gui.TreeStyle.TEXT_COLOR;

/**
 * A {@link Pane} that displays an {@link AbstractBinarySearchTree}.
 * <p>
 * It allows to zoom in and out, drag the displayed tree, center it, and highlights nodes and edges.
 */
public class TreePane extends Pane {

    private static final double SCALE_IN = 1.1;
    private static final double SCALE_OUT = 1 / SCALE_IN;
    private static final double MAX_SCALE = 10000;
    private static final double MIN_SCALE = 3;

    private final AtomicReference<Point2D> lastPoint = new AtomicReference<>();
    private Affine transformation = new Affine();

    private final Text positionText = new Text();

    private final Map<BinaryNode<?>, LabeledNode> nodes = new HashMap<>();
    private final Map<BinaryNode<?>, Location> nodeLocations = new HashMap<>();

    private final Map<Edge, Line> edges = new HashMap<>();
    private final Map<BinaryNode<?>, Map<BinaryNode<?>, Edge>> nodesToEdge = new HashMap<>();

    private final List<Line> grid = new ArrayList<>();

    private boolean centered = true;

    /**
     * Creates a new, empty {@link TreePane}.
     */
    public TreePane() {
        this(null);
    }

    /**
     * Creates a new {@link TreePane} that initially displays the tree with the given root node.
     *
     * @param root The root node of the tree to display.
     */
    public TreePane(BinaryNode<?> root) {
        // avoid division by zero when scale = 1
        transformation.appendScale(MIN_SCALE, MIN_SCALE);

        initListeners();
        drawGrid();
        drawPositionText();
        positionText.setFill(TEXT_COLOR);

        if (root != null) setTree(root);
    }

    /**
     * Sets the tree to display to the given one.
     *
     * @param root The root node of the tree to display.
     */
    public void setTree(BinaryNode<?> root) {
        clear();
        Map<BinaryNode<?>, Double> xOffsets = new HashMap<>();
        calculateXOffsets(root, xOffsets, 0);

        addTree(root, 0, xOffsets);

        for (LabeledNode node : nodes.values()) {
            node.ellipse().toFront();
            node.text().toFront();
        }

        redrawMap();
        center();
    }

    private void addTree(BinaryNode<?> root, int depth, Map<BinaryNode<?>, Double> xOffsets) {
        if (root == null) {
            return;
        }

        Location rootLocation = new Location(xOffsets.get(root), depth * NODE_VERTICAL_DISTANCE);
        addNode(root, rootLocation);

        if (root.hasLeft()) {
            addTree(root.getLeft(), depth + 1, xOffsets);
            addEdge(root, root.getLeft());
        }

        if (root.hasRight()) {
            addTree(root.getRight(), depth + 1, xOffsets);
            addEdge(root, root.getRight());
        }
    }

    // --- Edge Handling --- //

    /**
     * Checks if an {@linkplain Edge edge} exists between the given source and target {@linkplain BinaryNode nodes}.
     *
     * @param source The source node of the edge.
     * @param target The target node of the edge.
     * @return {@code true} if an edge exists between the given nodes, {@code false} otherwise.
     */
    public boolean containsEdge(BinaryNode<?> source, BinaryNode<?> target) {
        return nodesToEdge.get(source) != null && nodesToEdge.get(source).get(target) != null;
    }

    /**
     * Highlights the given {@linkplain Edge edge} by changing its color and stroke settings.
     *
     * @param source The source node of the edge.
     * @param target The target node of the edge.
     */
    public void highlightEdge(BinaryNode<?> source, BinaryNode<?> target) {
        setEdgeColor(source, target, HIGHLIGHT_COLOR);
        if (ADD_DASHES) setEdgeDash(source, target, 50, 10.0);
    }

    /**
     * Updates the color used to draw the given {@linkplain Edge edge}.
     *
     * @param color The new color.
     * @throws IllegalArgumentException If the given {@linkplain Edge edge} is not part of this {@link TreePane}.
     */
    public void setEdgeColor(BinaryNode<?> source, BinaryNode<?> target, Color color) {
        getEdgeLine(source, target).setStroke(color);
    }

    /**
     * Updates the dashing settings of the stroke used to draw the given {@linkplain Edge edge}.
     *
     * @param source     The source node of the edge.
     * @param target     The target node of the edge.
     * @param dashLength The length of the individual dashes.
     * @param gapLength  The length of the gaps between the dashes.
     * @throws IllegalArgumentException If the given {@linkplain Edge edge} is not part of this {@link TreePane}.
     */
    public void setEdgeDash(BinaryNode<?> source, BinaryNode<?> target, double dashLength, double gapLength) {
        getEdgeLine(source, target).getStrokeDashArray().setAll(dashLength, gapLength);
    }

    /**
     * Resets the color used to draw the given {@linkplain Edge edge} to the default color ({@link TreeStyle#DEFAULT_EDGE_COLOR})
     * and removes any dashing settings of the stroke.
     *
     * @throws IllegalArgumentException If the given {@linkplain Edge edge} is not part of this {@link TreePane}.
     */
    public void resetEdge(BinaryNode<?> source, BinaryNode<?> target) {
        Line line = getEdgeLine(source, target);
        line.getStrokeDashArray().clear();
        line.setStroke(DEFAULT_EDGE_COLOR);
    }

    /**
     * Updates the position of all {@linkplain Edge edges} on this {@link TreePane}.
     */
    public void redrawEdges() {
        for (Edge edge : edges.keySet()) {
            redrawEdge(edge);
        }
    }

    private void redrawEdge(Edge edge) {
        Point2D transformedPointA = transform(getLocation(edge.source()));
        Point2D transformedPointB = transform(getLocation(edge.target()));

        Line line = edges.get(edge);

        line.setStartX(transformedPointA.getX());
        line.setStartY(transformedPointA.getY());

        line.setEndX(transformedPointB.getX());
        line.setEndY(transformedPointB.getY());
    }

    // --- Node Handling --- //

    /**
     * Checks if the given node exists within this treePane.
     *
     * @param node The node to check.
     * @return {@code true} if the node exists within this treePane, {@code false} otherwise.
     */
    public boolean containsNode(BinaryNode<?> node) {
        return nodes.containsKey(node);
    }

    /**
     * Highlights the given node by changing its color and stroke settings.
     *
     * @param node The node to highlight.
     */
    public void highlightNode(BinaryNode<?> node) {
        setNodeStrokeColor(node, HIGHLIGHT_COLOR);
        if (ADD_DASHES) setNodeDash(node, 10.0, 10.0);
    }

    /**
     * Updates the color used to draw the circumference of given node.
     *
     * @param node  The node to update.
     * @param color The new color.
     * @throws IllegalArgumentException If the given node is not part of this {@link TreePane}.
     */
    public void setNodeStrokeColor(BinaryNode<?> node, Color color) {
        getLabeledNode(node).setStrokeColor(color);
    }

    /**
     * Updates the dashing settings of the stroke used to draw the given {@linkplain Edge edge}.
     *
     * @param node       The node to update.
     * @param dashLength The length of the individual dashes.
     * @param gapLength  The length of the gaps between the dashes.
     * @throws IllegalArgumentException If the given {@linkplain Edge edge} is not part of this {@link TreePane}.
     */
    public void setNodeDash(BinaryNode<?> node, double dashLength, double gapLength) {
        getLabeledNode(node).ellipse().getStrokeDashArray().setAll(dashLength, gapLength);
    }


    /**
     * Resets the color used to draw the given node to the default color ({@link TreeStyle#DEFAULT_NODE_COLOR})
     * and removes any dashing settings of the stroke.
     *
     * @param node The node to update.
     * @throws IllegalArgumentException If the given node is not part of this {@link TreePane}.
     */
    public void resetNode(BinaryNode<?> node) {
        LabeledNode labeledNode = getLabeledNode(node);
        labeledNode.ellipse().getStrokeDashArray().clear();
        labeledNode.setStrokeColor(getNodeColor(node));
    }

    /**
     * Updates the position of all nodes on this {@link TreePane}.
     */
    public void redrawNodes() {
        for (BinaryNode<?> node : nodes.keySet()) {
            redrawNode(node);
        }
    }

    /**
     * Updates the position of the given node.
     *
     * @param node The node to update.
     * @throws IllegalArgumentException If the given node is not part of this {@link TreePane}.
     */
    public void redrawNode(BinaryNode<?> node) {
        if (!nodes.containsKey(node)) {
            throw new IllegalArgumentException("The given node is not part of this TreePane");
        }

        Point2D transformedMidPoint = transform(midPoint(node));

        LabeledNode labeledNode = nodes.get(node);

        labeledNode.ellipse().setCenterX(transformedMidPoint.getX());
        labeledNode.ellipse().setCenterY(transformedMidPoint.getY());

        labeledNode.text().setX(transformedMidPoint.getX() - labeledNode.text().getLayoutBounds().getWidth() / 2.0);
        labeledNode.text().setY(transformedMidPoint.getY() + labeledNode.text().getLayoutBounds().getHeight() / 4.0);
    }

    // --- Other Util --- //

    /**
     * Removes all components from this {@link TreePane}.
     */
    public void clear() {
        nodes.clear();
        nodeLocations.clear();
        nodesToEdge.clear();
        edges.clear();

        getChildren().clear();
    }

    /**
     * Updates the position of all components on this {@link TreePane}.
     */
    public void redrawMap() {
        redrawEdges();
        redrawNodes();
    }

    /**
     * Tries to center this {@link TreePane} as good as possible such that each node is visible while keeping the zoom factor as high as possible.
     */
    public void center() {

        if (getHeight() == 0.0 || getWidth() == 0.0) {
            return;
        }

        if (nodes.isEmpty()) {
            transformation.appendScale(20, 20);
            redrawGrid();
            return;
        }

        double maxX = nodeLocations.values().stream().mapToDouble(Location::x).max().orElse(0);

        double maxY = nodeLocations.values().stream().mapToDouble(Location::y).max().orElse(0);

        double minX = nodeLocations.values().stream().mapToDouble(Location::x).min().orElse(0);

        double minY = nodeLocations.values().stream().mapToDouble(Location::y).min().orElse(0);

        if (minX == maxX) {
            minX = minX - 1;
            maxX = maxX + 1;
        }

        if (minY == maxY) {
            minY = minY - 1;
            maxY = maxY + 1;
        }

        double widthPadding = (maxX - minX) / 10;
        double heightPadding = (maxY - minY) / 10;

        double width = maxX - minX + 2 * widthPadding;
        double height = maxY - minY + 2 * heightPadding;

        Affine inverse = new Affine();

        inverse.appendTranslation(minX - widthPadding, minY - heightPadding);
        inverse.appendScale(width / getWidth(), height / getHeight());

        try {
            transformation = inverse.createInverse();
        } catch (NonInvertibleTransformException e) {
            throw new IllegalStateException("transformation is not invertible");
        }

        redrawGrid();
        redrawMap();

        centered = true;
    }

    /**
     * Increases the zoom of the current treePane, i.e. the size of the covered area is decreased.
     */
    public void zoomIn() {
        zoom(getWidth() / 2.0, getHeight() / 2.0, SCALE_IN);
    }

    /**
     * Decreases the zoom of the current treePane, i.e. the size of the covered area is increased.
     */
    public void zoomOut() {
        zoom(getWidth() / 2.0, getHeight() / 2.0, SCALE_OUT);
    }


    // --- Private Methods --- //

    private void initListeners() {

        setOnMouseDragged(event -> {
                Point2D point = new Point2D(event.getX(), event.getY());
                Point2D diff = getDifference(point, lastPoint.get());

                transformation.appendTranslation(diff.getX() / getTransformScaleX(), diff.getY() / getTransformScaleY());

                redrawMap();
                redrawGrid();
                updatePositionText(point);

                lastPoint.set(point);

                centered = false;
            }
        );

        setOnScroll(event -> {
            if (event.getDeltaY() == 0) {
                return;
            }

            zoom(event.getX(), event.getY(), event.getDeltaY() > 0 ? SCALE_IN : SCALE_OUT);

            centered = false;
        });

        setOnMouseMoved(event -> {
            Point2D point = new Point2D(event.getX(), event.getY());
            lastPoint.set(point);
            updatePositionText(point);
        });

        widthProperty().addListener((obs, oldValue, newValue) -> {
            setClip(new Rectangle(0, 0, getWidth(), getHeight()));

            if (getWidth() != 0.0 && getHeight() != 0.0) {
                redrawGrid();
                redrawMap();
                if (centered) center();
            }

            drawPositionText();
        });

        heightProperty().addListener((obs, oldValue, newValue) -> {
            setClip(new Rectangle(0, 0, getWidth(), getHeight()));

            if (getWidth() != 0.0 && getHeight() != 0.0) {
                redrawGrid();
                redrawMap();
                if (centered) center();
            }

            drawPositionText();
        });
    }

    private void zoom(double x, double y, double scale) {
        if (((getTransformScaleX() < MIN_SCALE || getTransformScaleY() < MIN_SCALE) && scale < 1)
            || ((getTransformScaleX() > MAX_SCALE || getTransformScaleY() > MAX_SCALE) && scale > 1)) {
            return;
        }

        Point2D point = inverseTransform(x, y);
        transformation.appendScale(scale, scale, point.getX(), point.getY());

        redrawMap();
        redrawGrid();
    }

    private void addEdge(BinaryNode<?> source, BinaryNode<?> target) {

        Edge edge = new Edge(source, target);

        edges.put(edge, drawEdge(source, target));
        nodesToEdge.computeIfAbsent(source, k -> new HashMap<>()).put(target, edge);
    }

    private Line drawEdge(BinaryNode<?> source, BinaryNode<?> target) {
        Location a = getLocation(source);
        Location b = getLocation(target);

        Point2D transformedA = transform(a);
        Point2D transformedB = transform(b);

        Line line = new Line(transformedA.getX(), transformedA.getY(), transformedB.getX(), transformedB.getY());

        line.setStroke(DEFAULT_EDGE_COLOR);
        line.setStrokeWidth(EDGE_STROKE_WIDTH);

        getChildren().add(line);

        return line;
    }

    private void addNode(BinaryNode<?> node, Location location) {
        nodeLocations.put(node, location);
        nodes.put(node, drawNode(node));
    }

    private LabeledNode drawNode(BinaryNode<?> node) {
        Point2D transformedPoint = transform(getLocation(node));

        Ellipse ellipse = new Ellipse(transformedPoint.getX(), transformedPoint.getY(), NODE_DIAMETER, NODE_DIAMETER);

        Color color = getNodeColor(node);
        ellipse.setFill(color);
        ellipse.setStroke(color);
        ellipse.setStrokeWidth(NODE_STROKE_WIDTH);

        setMouseTransparent(false);

        Text text = new Text(transformedPoint.getX(), transformedPoint.getY(), node.getKey().toString());
        text.setStroke(TEXT_COLOR);

        getChildren().addAll(ellipse, text);

        return new LabeledNode(ellipse, text);
    }

    private Color getNodeColor(BinaryNode<?> node) {
        if (node instanceof RBNode<?> rbNode) {
            return rbNode.getColor() == null ? DEFAULT_NODE_COLOR : rbNode.isRed() ? RED_NODE_COLOR : BLACK_NODE_COLOR;
        } else {
            return DEFAULT_NODE_COLOR;
        }
    }

    private void drawGrid() {

        int stepX = (int) (getTransformScaleX() / 2);
        int stepY = (int) (getTransformScaleY() / 2);

        if (stepX < 1 || stepY < 1) {
            return;
        }

        int offsetX = (int) getTransformTranslateX();
        int offsetY = (int) getTransformTranslateY();

        // Vertical Lines
        for (int i = 0, x = offsetX % (stepX * 5); x <= getWidth(); i++, x += stepX) {
            Float strokeWidth = getStrokeWidth(i, offsetX % (stepX * 10) > stepX * 5);
            if (strokeWidth == null) continue;
            Line line = new Line(x, 0, x, getHeight());
            line.setStrokeWidth(strokeWidth);
            line.setStroke(GRID_LINE_COLOR);
            getChildren().add(line);
            grid.add(line);
        }

        // Horizontal Lines
        for (int i = 0, y = offsetY % (stepY * 5); y <= getHeight(); i++, y += stepY) {
            Float strokeWidth = getStrokeWidth(i, offsetY % (stepY * 10) > stepY * 5);
            if (strokeWidth == null) continue;

            var line = new Line(0, y, getWidth(), y);
            line.setStrokeWidth(strokeWidth);
            line.setStroke(GRID_LINE_COLOR);
            getChildren().add(line);
            grid.add(line);
        }
    }

    private Float getStrokeWidth(int i, boolean inverted) {
        float strokeWidth;
        if (i % 10 == 0) {
            strokeWidth = inverted ? GRID_TEN_TICKS_WIDTH : GRID_FIVE_TICKS_WIDTH;
        } else if (i % 5 == 0) {
            strokeWidth = inverted ? GRID_FIVE_TICKS_WIDTH : GRID_TEN_TICKS_WIDTH;
        } else {
            return null;
        }
        return strokeWidth;
    }

    private Edge getEdge(BinaryNode<?> source, BinaryNode<?> target) {
        if (nodesToEdge.get(source) == null || nodesToEdge.get(source).get(target) == null) {
            throw new IllegalArgumentException("The given edge is not part of this TreePane");
        }
        return nodesToEdge.get(source).get(target);
    }

    private Line getEdgeLine(BinaryNode<?> source, BinaryNode<?> target) {
        return edges.get(getEdge(source, target));
    }

    private LabeledNode getLabeledNode(BinaryNode<?> node) {
        LabeledNode labeledNode = nodes.get(node);

        if (labeledNode == null) {
            throw new IllegalArgumentException("The given node is not part of this TreePane");
        }

        return labeledNode;
    }

    private Location getLocation(BinaryNode<?> node) {
        return nodeLocations.getOrDefault(node, Location.ORIGIN);
    }

    private Point2D locationToPoint2D(Location location) {
        return new Point2D(location.x(), location.y());
    }

    private Point2D getDifference(Point2D p1, Point2D p2) {
        return new Point2D(p1.getX() - p2.getX(), p1.getY() - p2.getY());
    }

    private Point2D midPoint(Location location) {
        return new Point2D(location.x(), location.y());
    }

    private Point2D midPoint(BinaryNode<?> node) {
        return midPoint(getLocation(node));
    }

    private void redrawGrid() {
        getChildren().removeAll(grid);
        grid.clear();
        drawGrid();
    }

    private void drawPositionText() {
        positionText.setX(getWidth() - positionText.getLayoutBounds().getWidth());
        positionText.setY(getHeight());
        positionText.setText("(-, -)");
        if (!getChildren().contains(positionText)) {
            getChildren().add(positionText);
        }
    }

    private void updatePositionText(Point2D point) {
        Point2D inverse = inverseTransform(point);
        positionText.setText("(%d, %d)".formatted((int) inverse.getX(), (int) inverse.getY()));
        positionText.setX(getWidth() - positionText.getLayoutBounds().getWidth());
        positionText.setY(getHeight());
    }

    private Point2D inverseTransform(Point2D point) {
        return inverseTransform(point.getX(), point.getY());
    }

    private Point2D inverseTransform(double x, double y) {
        try {
            return transformation.inverseTransform(x, y);
        } catch (NonInvertibleTransformException e) {
            throw new IllegalStateException("transformation is not invertible");
        }
    }

    private Point2D transform(Point2D point) {
        return transformation.transform(point);
    }

    private Point2D transform(Location location) {
        return transformation.transform(locationToPoint2D(location));
    }

    private double getTransformScaleX() {
        return transformation.getMxx();
    }

    private double getTransformScaleY() {
        return transformation.getMyy();
    }

    private double getTransformTranslateX() {
        return transformation.getTx();
    }

    private double getTransformTranslateY() {
        return transformation.getTy();
    }

    private double calculateXOffsets(BinaryNode<?> root, Map<BinaryNode<?>, Double> xOffsets, double currentOffset) {

        if (root == null) {
            return currentOffset;
        }

        double leftDiff = NODE_DISTANCE;

        if (root.hasLeft()) {
            currentOffset = calculateXOffsets(root.getLeft(), xOffsets, currentOffset) + NODE_DISTANCE;
            leftDiff = currentOffset - xOffsets.get(root.getLeft());
        }

        xOffsets.put(root, currentOffset);

        if (root.hasRight()) {
            currentOffset = calculateXOffsets(root.getRight(), xOffsets, currentOffset + leftDiff);
        }

        if (root.hasLeft() && root.hasRight()) {
            double leftOffset = xOffsets.get(root.getLeft());
            double rightOffset = xOffsets.get(root.getRight());
            xOffsets.put(root, leftOffset + (rightOffset - leftOffset) / 2.0);
        }

        return currentOffset;
    }

    private record Edge(BinaryNode<?> source, BinaryNode<?> target) {

    }

    private record LabeledNode(Ellipse ellipse, Text text) {

        public void setStrokeColor(Color strokeColor) {
            ellipse.setStroke(strokeColor);
        }

    }

    /**
     * A data class for storing a location in a 2D plane.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     */
    private record Location(double x, double y) {

        public static final Location ORIGIN = new Location(0, 0);

    }
}
