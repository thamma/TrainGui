package me.thamma.game;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.layout.Pane;

public class Node {

	private Coordinate coord;
	private int id;
	private String name;
	private Map<Edge, Node> outgoing;

	public Node(String name, int id, int x, int y) {
		this(name, id, new Coordinate(x, y));
	}

	public Node(String name, int id, Coordinate coord) {
		this.id = id;
		this.coord = coord;
		this.name = name;
		this.outgoing = new HashMap<Edge, Node>();
	}

	/**
	 * 
	 * @return The Map<Edge, Node> of the outgoing edges
	 */
	public Map<Edge, Node> getOutgoing() {
		return this.outgoing;
	}

	/**
	 * 
	 * @return The coordinate given in the constructor
	 */
	public Coordinate getCoordinate() {
		return this.coord;
	}

	/**
	 * 
	 * @param edge
	 *            The edge leading to the node
	 * @param node
	 *            The node the edge leads to
	 */
	public void appendEdge(Edge edge, Node node) {
		this.outgoing.put(edge, node);
	}

	public int getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	/**
	 * 
	 * @param g
	 *            The graph the node lies in, to determine the minimum and
	 *            maximum coordinates
	 * @param overlay
	 *            The pane in which the node should be displayed
	 * @param padding
	 *            The padding of the pane
	 * @return The coordinate of the node on the canvas
	 */
	public Coordinate getCanvasCoordinate(Graph g, Pane overlay, double padding) {
		double minX = g.getMinCoordinate().getX(), minY = g.getMinCoordinate().getY();
		double maxX = g.getMaxCoordinate().getX(), maxY = g.getMaxCoordinate().getY();
		// arithmetic magic happening
		double x = padding + (overlay.getWidth() - 2 * padding) * (this.getCoordinate().getX() - minX) / (maxX - minX);
		double y = padding + (overlay.getHeight() - 2 * padding) * (this.getCoordinate().getY() - minY) / (maxY - minY);
		return new Coordinate(x, y);
	}

}
