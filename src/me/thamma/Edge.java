package me.thamma;

import javafx.scene.paint.Color;

public class Edge {

	private int id, length;
	private Color color;
	boolean tunnel;

	public Edge(int id, int lenght, Color color, boolean tunnel) {
		this.id = id;
		this.length = lenght;
		this.color = color;
		this.tunnel = tunnel;
	}

	public boolean isTunnel() {
		return this.tunnel;
	}

	public Color getColor() {
		return this.color;
	}

	public int getId() {
		return id;
	}

	public int getLength() {
		return length;
	}

}
