package me.thamma.gui;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import me.thamma.game.Graph;
import me.thamma.game.TrackKind;

public class MainGui extends Application {

	public static Map<TrackKind, Integer> hand;
	public FlowPane handPane;

	public static void main(String[] args) {
		// Setup hand cards
		Map<TrackKind, Integer> a = new HashMap<TrackKind, Integer>();
		for (TrackKind k : TrackKind.values()) {
			a.put(k, k.ordinal() + 1);
		}
		// make hand cards a sorted Map
		MainGui.hand = new TreeMap<TrackKind, Integer>(a);
		// launch GUI
		launch(args);

	}

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Gui something something");
		primaryStage.setWidth(java.awt.Toolkit.getDefaultToolkit().getScreenSize().getWidth());
		primaryStage.setHeight(java.awt.Toolkit.getDefaultToolkit().getScreenSize().getHeight());
		// TODO: Add Fullscreen option
		primaryStage.setFullScreen(true);
		GridPane grid = new GridPane();

		grid.setId("pane");
		grid.setPadding(new Insets(10, 10, 10, 10));
		grid.setVgap(8);
		grid.setHgap(10);

		handPane = new FlowPane();
		handPane.setOrientation(Orientation.HORIZONTAL);
		handPane.setAlignment(Pos.CENTER);

		Pane canvasPane = new Pane();
		canvasPane.resize(760, 400);

		GridPane.setConstraints(canvasPane, 0, 0);
		GridPane.setConstraints(handPane, 0, 1);
		// TODO: Remove Grid lines (for debug purposes)
		grid.gridLinesVisibleProperty().set(true);
		Scene scene = new Scene(grid, 200, 200);

		updateHand();
		Graph g = new Graph("src/me/thamma/resources/campus.map");

		g.draw(canvasPane);

		grid.getChildren().addAll(handPane, canvasPane);
		scene.getStylesheets().addAll(this.getClass().getResource("../resources/style.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void updateHand() {
		handPane.getChildren().clear();
		for (TrackKind k : hand.keySet()) {
			GuiCard card = new GuiCard(k, hand.get(k));
			Pane pane = card.drawPane();
			card.highLight(false);
			pane.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
				card.highLight(true);
			});
			pane.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
				card.highLight(false);
			});

			handPane.getChildren().add(pane);
			FlowPane.setMargin(pane, new Insets(6, 6, 6, 6));
		}
	}
}
