package me.thamma.game;

import javafx.scene.paint.Color;

public enum TrackKind {

	ALL(Color.GREY), BLACK(Color.BLACK), RED(Color.RED), GREEN(Color.GREEN), BLUE(Color.BLUE), YELLOW(
			Color.YELLOW), ORANGE(Color.ORANGE), VIOLET(Color.VIOLET), WHITE(Color.WHITE);

	private Color color;

	TrackKind(Color c) {
		this.color = c;
	}

	public Color getColor() {
		return this.color;
	}

}