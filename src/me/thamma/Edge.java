package me.thamma;

import javafx.scene.paint.Color;

public class Edge {

	private int id, from, to, length;
	private Color color;

	public Edge(int id, int from, int to, int lenght, Color color) {
		this.id = id;
		this.from = from;
		this.to = to;
		this.length = lenght;
		this.color = color;
	}

	public Color getColor() {
		return this.color;
	}

	public int getId() {
		return id;
	}

	public int getFrom() {
		return from;
	}

	public int getTo() {
		return to;
	}

	public int getLength() {
		return length;
	}

}
