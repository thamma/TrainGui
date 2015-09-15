package me.thamma.gui;

import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import me.thamma.game.TrackKind;

public class GuiCard {

	private TrackKind kind;
	private int amount;
	private Rectangle rect;

	public GuiCard(TrackKind kind, int amount) {
		this.kind = kind;
		this.amount = amount;
	}

	public Pane drawPane() {
		return drawPane(70, 100);
	}

	public Pane drawPane(int w, int h) {
		Canvas canvas = new Canvas(w, h);
		Pane p = new Pane();
		p.getChildren().add(canvas);
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
		p.getChildren().addAll(rect, l);
		return p;
	}

	public void highLight(boolean highlight) {
		if (highlight) {
			rect.setStroke(Color.WHITE);
		} else {
			rect.setStroke(Color.BLACK);
		}
	}

	public GuiCard(TrackKind kind) {
		this(kind, 0);
	}

	public void setAmount(int i) {
		this.amount = i;
	}

}
