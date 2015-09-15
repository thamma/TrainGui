package me.thamma.game;

import static me.thamma.gui.GuiEvents.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import me.thamma.utils.FileUtils;
import me.thamma.utils.MapParser;

public class Graph implements Iterable<Node> {

	private MapParser parser;

	public Graph(String filename) {
		this(FileUtils.loadFile(filename));
	}

	public Graph(List<String> lines) {
		this.parser = new MapParser(lines);
	}

	@Override
	public Iterator<Node> iterator() {
		return this.parser.getNodes().iterator();
	}

	public List<Edge> getEdges() {
		return this.parser.getEdges();
	}

	public List<Node> getNodes() {
		return this.parser.getNodes();
	}
	private Coordinate minCoord, maxCoord;

	/**
	 * 
	 * @return The Coordinate(min(x), min(y))
	 * @see Graph.setupMinMax()
	 */
	public Coordinate getMinCoordinate() {
		if (minCoord == null)
			setupMinMax();
		return minCoord;
	}

	/**
	 * 
	 * @return The Coordinate(max(x), max(y))
	 * @see Graph.setupMinMax()
	 */
	public Coordinate getMaxCoordinate() {
		if (maxCoord == null)
			setupMinMax();
		return maxCoord;
	}

	/**
	 * Initializes the min and max coordinates if not initialized yet
	 */
	private void setupMinMax() {
		double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE, maxX = 0, maxY = 0;
		for (Node n : this) {
			minX = Math.min(n.getCoordinate().getX(), minX);
			minY = Math.min(n.getCoordinate().getY(), minY);
			maxX = Math.max(n.getCoordinate().getX(), maxX);
			maxY = Math.max(n.getCoordinate().getY(), maxY);
		}
		this.minCoord = new Coordinate(minX, minY);
		this.maxCoord = new Coordinate(maxX, maxY);
	}

	public final int radius = 25;
	public final int padding = 40;
	public final Font font = new Font("Arial", radius);
	public final double gap = radius + 15;

	/**
	 * 
	 * @param overlay
	 *            The parental pane to add the nodes to
	 */
	public void draw(Pane overlay) {
		// draw nodes and edges externally
		// draw edges first to put them into background
		drawEdges(overlay);
		drawNodes(overlay);
	}

	/**
	 * 
	 * @param overlay
	 *            The pane the nodes should be drawn on
	 */
	private void drawNodes(Pane overlay) {
		for (Node node : this) {
			// arithmetics
			double x = node.getCanvasCoordinate(this, overlay, padding).getX();
			double y = node.getCanvasCoordinate(this, overlay, padding).getY();
			// build node
			Circle nodeIcon = new Circle(radius);
			nodeIcon.relocate(x - radius, y - radius);
			nodeIcon.setOnMouseEntered(event -> nodeHoverOn(nodeIcon, node));
			nodeIcon.setOnMouseExited(event -> nodeHoverOff(nodeIcon, node));
			nodeIcon.setOnMouseClicked(event -> nodeClick(nodeIcon, node));
			// build label
			Label nodeLabel = new Label(node.getName());
			nodeLabel.setFont(font);
			nodeLabel.relocate(x - radius, y - radius - 20);
			// add to overlay
			overlay.getChildren().addAll(nodeIcon, nodeLabel);
		}
	}

	/**
	 * 
	 * @param overlay
	 *            The pane to draw the edge on
	 * @param node1
	 *            A node of the edge
	 * @param node2
	 *            A node of the edge
	 * @param edge
	 *            The edge object
	 */
	private void drawSingleEdge(Pane overlay, Node node1, Edge edge, Node node2) {
		double x1 = node1.getCanvasCoordinate(this, overlay, padding).getX(),
				x2 = node1.getCanvasCoordinate(this, overlay, padding).getY(),
				y1 = node2.getCanvasCoordinate(this, overlay, padding).getX(),
				y2 = node2.getCanvasCoordinate(this, overlay, padding).getY();
		Line edgeLine = new Line(x1, x2, y1, y2);
		edgeLine.setStrokeWidth(radius / 2);
		edgeLine.setStroke(edge.getColor());
		//
		Line bgLine = new Line(x1, x2, y1, y2);
		bgLine.toBack();
		bgLine.setVisible(false);
		bgLine.setStroke(edge.getColor().invert());
		bgLine.setStrokeWidth(radius / 2 + 2);
		edgeLine.setOnMouseClicked(event -> java.awt.Toolkit.getDefaultToolkit().beep());
		edgeLine.setOnMouseEntered(event -> edgeHoverOn(edgeLine, edge, bgLine));
		edgeLine.setOnMouseExited(event -> edgeHoverOff(edgeLine, edge, bgLine));
		double dash = calculateDashLength(edge, overlay, node1, node2);
		if (edge.getLength() > 0 && dash > 0)
			edgeLine.getStrokeDashArray().addAll(dash, gap);
		overlay.getChildren().addAll(edgeLine, bgLine);
	}

	/**
	 * 
	 * @param edge
	 *            The edge to calculate the dash length of
	 * @param overlay
	 *            the pane to calculate the canvas coordinate on
	 * @param node1
	 *            A node of the edge
	 * @param node2
	 *            A node of the edge
	 * @return
	 */
	private double calculateDashLength(Edge edge, Pane overlay, Node node1, Node node2) {
		double nodeDistance = node1.getCanvasCoordinate(this, overlay, padding)
				.getDistance(node2.getCanvasCoordinate(this, overlay, padding));
		double out = (nodeDistance - (edge.getLength() - 1) * gap) / Double.valueOf(edge.getLength());
		// if Edge.getLength() is too high, out turns negative
		return (out > 0 ? out : 0);
	}

	/**
	 * 
	 * @param overlay
	 *            The Pane to draw the edges on
	 */
	private void drawEdges(Pane overlay) {
		// remember the already drawn edges whilst visiting each node
		List<Edge> drawn = new ArrayList<Edge>();
		for (Node n : this) {
			for (Edge edge : n.getOutgoing().keySet()) {
				if (!drawn.contains(edge)) {
					drawSingleEdge(overlay, n, edge, n.getOutgoing().get(edge));
					drawn.add(edge);
				}
			}
		}
	}
}