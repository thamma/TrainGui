package me.thamma;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class GuiEvents {

	public static void nodeHoverOn(Circle node, Node n) {
		node.setStroke(Color.WHITE);
	}

	public static void nodeHoverOff(Circle node, Node n) {
		node.setStroke(Color.BLACK);
	}

	public static void nodeClick(Circle node, Node n) {

	}

	public static void edgeHoverOn(Line l, Edge e, Line temp) {
		temp.setVisible(true);
		temp.toBack();
	}

	public static void edgeHoverOff(Line l, Edge e, Line temp) {
		temp.setVisible(false);
		l.setVisible(true);
	}

	public static void edgeClick(Line l, Edge e, Line temp) {

	}

}
