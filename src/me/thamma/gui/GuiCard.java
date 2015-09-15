package me.thamma.gui;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import me.thamma.game.TrackKind;

public class GuiCard extends Pane {

	private TrackKind kind;
	private int amount;
	private Rectangle rect;

	public GuiCard(double w, double h, TrackKind kind, int amount) {
		super();
		this.kind = kind;
		this.amount = amount;
		this.resize(w, h);
		rect = null;
		if (this.kind != TrackKind.ALL) {
			rect = new Rectangle(w, h, this.kind.getColor());
		} else {
			rect = new Rectangle(w, h,
					new LinearGradient(0f, 1f, 1f, 0f, true, CycleMethod.NO_CYCLE,
							new Stop[] { new Stop(0, Color.web("#f8bd55")), new Stop(0.14, Color.web("#c0fe56")),
									new Stop(0.28, Color.web("#5dfbc1")), new Stop(0.43, Color.web("#64c2f8")),
									new Stop(0.57, Color.web("#be4af7")), new Stop(0.71, Color.web("#ed5fc2")),
									new Stop(0.85, Color.web("#ef504c")), new Stop(1, Color.web("#f2660f")), }));

		}
		Label l = new Label("" + this.amount);
		l.setTextFill(kind.getColor().invert());
		l.relocate(w - 20, h - 20);
		this.getChildren().addAll(rect, l);
	}

	public void highLight(boolean highlight) {
		if (highlight) {
			rect.setStroke(Color.WHITE);
		} else {
			rect.setStroke(Color.BLACK);
		}
	}

	public void setAmount(int i) {
		this.amount = i;
	}

}
