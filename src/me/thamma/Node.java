package me.thamma;

import javafx.scene.canvas.Canvas;

public class Node {

	private Coordinate coord;
	private int id;
	private String name;

	public Node(String name, int id, int x, int y) {
		this.id = id;
		this.coord = new Coordinate(x, y);
		this.name = name;
	}

	public Coordinate getCoordinate() {
		return this.coord;
	}

	public int getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public Coordinate getCanvasCoordinate(Graph g, Canvas canvas, double padding) {
		double minX = g.getMinCoordinate().getX(), minY = g.getMinCoordinate().getY(),
				maxX = g.getMaxCoordinate().getX(), maxY = g.getMaxCoordinate().getY();
		double x = padding + (canvas.getWidth() - 2 * padding) * (this.getCoordinate().getX() - minX) / (maxX - minX);
		double y = padding + (canvas.getHeight() - 2 * padding) * (this.getCoordinate().getY() - minY) / (maxY - minY);
		return new Coordinate(x, y);
	}

}
