package me.thamma.game;

import static me.thamma.gui.GuiEvents.edgeClick;
import static me.thamma.gui.GuiEvents.edgeHoverOff;
import static me.thamma.gui.GuiEvents.edgeHoverOn;
import static me.thamma.gui.GuiEvents.nodeClick;
import static me.thamma.gui.GuiEvents.nodeHoverOff;
import static me.thamma.gui.GuiEvents.nodeHoverOn;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

	public final int radius = 20;
	public final int padding = 80;
	public final Font font = new Font("Arial", 20);
	public final double gap = radius + 15;
	private List<javafx.scene.Node> foreground;

	/**
	 * 
	 * @param overlay
	 *            The parental pane to add the nodes to
	 */
	public void draw(Pane overlay) {
		assert(overlay != null);
		this.foreground = new ArrayList<javafx.scene.Node>();
		// draw nodes and edges externally
		// draw edges first to put them into background
		drawEdges(overlay);
		drawNodes(overlay);
		// apply fore/background patch to fix wether a Node should be in that
		// layer
		updateForeground();
	}

	/**
	 * internally called to force Nodes to foreground after the gui was built
	 */
	private void updateForeground() {
		for (javafx.scene.Node n : foreground)
			n.toFront();
	}

	/**
	 * 
	 * @param node
	 *            the node to force to foreground
	 */
	private void forceToForeground(javafx.scene.Node node) {
		forceToForeground(node, false);
	}

	/**
	 * 
	 * @param node
	 *            the node to force to foregorund
	 * @param force
	 *            wether to grant privilege to the object
	 */
	private void forceToForeground(javafx.scene.Node node, boolean force) {
		if (force) {
			this.foreground.add(0, node);
		} else {
			this.foreground.add(node);
		}
	}

	/**
	 * 
	 * @param overlay
	 *            The pane the nodes should be drawn on
	 */
	private void drawNodes(Pane overlay) {
		assert(overlay != null);
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
			nodeLabel.relocate(x - radius - node.getName().length(), y - radius - font.getSize());
			forceToForeground(nodeLabel, true);
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
		assert(overlay != null);
		assert(node1 != null);
		assert(node2 != null);
		assert(edge != null);
		// calculate Node coordinates into absolute canvas coordinate
		Coordinate lower = new Coordinate(node1.getCanvasCoordinate(this, overlay, padding).getX(),
				node1.getCanvasCoordinate(this, overlay, padding).getY());
		Coordinate upper = new Coordinate(node2.getCanvasCoordinate(this, overlay, padding).getX(),
				node2.getCanvasCoordinate(this, overlay, padding).getY());
		// main connection line
		Line edgeLine = new Line(lower.getX(), lower.getY(), upper.getX(), upper.getY());
		edgeLine.setStrokeWidth(radius / 2);
		edgeLine.setStroke(edge.getColor());
		// tunnel highlighting
		if (edge.isTunnel()) {
			drawTunnelEntrances(overlay, lower, upper, edge);
			// Line tunnelLine = new Line(lower.getX(), lower.getY(),
			// upper.getX(), upper.getY());
			// tunnelLine.setStrokeWidth(radius * 3 / 2);
			// tunnelLine.setStroke(Color.BLACK);
			// tunnelLine.toBack();
			// overlay.getChildren().addAll(tunnelLine);
		}
		// secondary edge, used for highlighting of edge
		Line bgLine = new Line(lower.getX(), lower.getY(), upper.getX(), upper.getY());
		bgLine.setVisible(false);
		bgLine.setStroke(edge.getColor().invert());
		// bigger radius to be shown behind the actual edge
		bgLine.setStrokeWidth(radius / 2 + 2);
		edgeLine.setOnMouseClicked(event -> edgeClick(edgeLine, edge, bgLine));
		edgeLine.setOnMouseEntered(event -> edgeHoverOn(edgeLine, edge, bgLine));
		edgeLine.setOnMouseExited(event -> edgeHoverOff(edgeLine, edge, bgLine));
		double dash = calculateDashLength(edge, overlay, node1, node2);
		if (edge.getLength() > 0 && dash > 0)
			edgeLine.getStrokeDashArray().addAll(dash, gap);
		overlay.getChildren().addAll(edgeLine, bgLine);
	}

	/**
	 * 
	 * @param overlay
	 *            The pane to add the tunnel entrances to
	 * @param lower
	 *            the lower coordinate of the overlaying pane
	 * @param upper
	 *            the upper coordinate of the overlaying pane
	 * @param edge
	 *            the edge to draw the tunnel entrances of
	 */
	private void drawTunnelEntrances(Pane overlay, Coordinate lower, Coordinate upper, Edge edge) {
		assert(edge.isTunnel());
		double size = 2 * radius;
		double scale = 2 * radius;
		Coordinate atLower = lower.add(upper.sub(lower).normalize(scale));
		Coordinate atUpper = upper.add(lower.sub(upper).normalize(scale));
		// double rad = 4;
		// Circle lowerCircle = new Circle(rad);
		// Circle upperCircle = new Circle(rad);
		// lowerCircle.relocate(atLower.getX() - rad, atLower.getY() - rad);
		// upperCircle.relocate(atUpper.getX() - rad, atUpper.getY() - rad);
		// lowerCircle.setFill(Color.WHITE);
		// upperCircle.setFill(Color.WHITE);
		// this.forceForeground(lowerCircle);
		// this.forceForeground(upperCircle);
		Image img = new Image("/me/thamma/resources/tunnel.gif");
		ImageView entrance1 = new ImageView(img);
		ImageView entrance2 = new ImageView(img);
		entrance1.setFitHeight(size);
		entrance1.setFitWidth(size);
		entrance2.setFitHeight(size);
		entrance2.setFitWidth(size);
		entrance1.relocate(atLower.getX() - size / 2, atLower.getY() - size / 2);
		entrance2.relocate(atUpper.getX() - size / 2, atUpper.getY() - size / 2);
		this.forceToForeground(entrance1);
		this.forceToForeground(entrance2);
		overlay.getChildren().addAll(entrance1, entrance2);
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
		for (Node n : this)
			for (Edge edge : n.getOutgoing().keySet())
				if (!drawn.contains(edge)) {
					drawSingleEdge(overlay, n, edge, n.getOutgoing().get(edge));
					drawn.add(edge);
				}
	}
}