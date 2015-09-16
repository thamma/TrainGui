package me.thamma.gui;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class PlayerCard extends Pane {

	private String name;
	private int id;
	private int cards;
	private int missions;
	private int points;
	private Rectangle rect;

	public PlayerCard(String name, int id, int cards, int missions) {
		super();
		this.name = name;
		this.id = id;
		this.cards = cards;
		this.missions = missions;
		rect = new Rectangle(240, 100);
		rect.setArcWidth(20);
		rect.setArcHeight(20);
		int hash = Math.abs(name.hashCode());
		rect.setFill(Color.rgb(Math.abs(hash % 255), Math.abs(hash * 7 % 255), Math.abs(hash * 19 % 255)));
		Label nameLabel = new Label(this.name + "  (#" + this.id + ")");
		Label cardsLabel = new Label(this.cards + " handcards");
		Label missionsLabel = new Label(this.missions + " mission cards");
		Label pointsLabel = new Label(this.points + " points");
		nameLabel.relocate(20, 15);
		pointsLabel.relocate(20, 35);
		cardsLabel.relocate(20, 55);
		missionsLabel.relocate(20, 75);

		this.getChildren().addAll(rect, nameLabel, pointsLabel, cardsLabel, missionsLabel);
	}

	public String getName() {
		return this.name;
	}

}
