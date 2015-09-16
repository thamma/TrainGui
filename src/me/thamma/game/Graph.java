package me.thamma.game;

import static me.thamma.gui.GuiEvents.edgeHoverOff;
import static me.thamma.gui.GuiEvents.edgeHoverOn;
import static me.thamma.gui.GuiEvents.nodeClick;
import static me.thamma.gui.GuiEvents.nodeHoverOff;
import static me.thamma.gui.GuiEvents.nodeHoverOn;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import me.thamma.utils.FileUtils;

public class Graph implements Iterable<Node> {

	private List<Node> nodes;
	private List<Edge> edges;

	public Graph(List<String> nodes, List<String> edges) {
		this.nodes = new ArrayList<Node>();
		for (int i = 0; i < nodes.size(); i++) {
			String[] args = nodes.get(i).split(",");
			String name = args[0];
			int x = Integer.valueOf(args[1]);
			int y = Integer.valueOf(args[2]);
			this.nodes.add(new Node(name, i, x, y));
		}
		this.edges = new ArrayList<Edge>();
		for (int i = 0; i < edges.size(); i++) {
			String[] args = edges.get(i).split(",");
			// TODO: add newly constructed edge to 'edges'
			// TODO: register edge in both cities
			int cityid1 = Integer.parseInt(args[0]);
			int cityid2 = Integer.parseInt(args[1]);
			int length = Integer.parseInt(args[2]);
			TrackKind kind = TrackKind.valueOf(args[3]);
			boolean tunnel = false;
			if (args.length == 5)
				tunnel = (args[4].equals("T"));
			Edge edge = new Edge(i, length, kind.getColor(), tunnel);
			// linked registering of the edges
			this.nodes.get(cityid1).appendEdge(edge, this.nodes.get(cityid2));
			this.nodes.get(cityid2).appendEdge(edge, this.nodes.get(cityid1));
			// add edge to graph
			this.edges.add(edge);
		}
	}

	public static Graph loadGraph(String filename) {
		List<String> in = FileUtils.loadFile(filename);
		List<String> edges = new ArrayList<String>();
		List<String> nodes = new ArrayList<String>();
		while (!in.get(0).equalsIgnoreCase("Staedte:"))
			in.remove(0);
		in.remove(0);// remove "Staedte"
		while (!in.get(0).equalsIgnoreCase("Strecken:"))
			nodes.add(in.remove(0));
		in.remove(0);// remove Strecken
		while (!in.get(0).equalsIgnoreCase("Auftraege:"))
			edges.add(in.remove(0));
		return new Graph(nodes, edges);
	}

	@Override
	public Iterator<Node> iterator() {
		return nodes.iterator();
	}

	public List<Edge> getEdges() {
		return this.edges;
	}

	public List<Node> getNodes() {
		return this.nodes;
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
