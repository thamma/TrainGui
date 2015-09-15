package me.thamma;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Gui something something");
		primaryStage.setWidth(800);
		primaryStage.setHeight(600);
		primaryStage.setFullScreen(true);
		Button btn = new Button();
		btn.setText("Say 'Hello World'");
		GridPane grid = new GridPane();

		Rectangle rect = new Rectangle(80, 80);
		rect.setOnMouseEntered(e -> System.out.println(""));

		grid.setId("pane");
		grid.setPadding(new Insets(10, 10, 10, 10));
		grid.setVgap(8);
		grid.setHgap(10);

		FlowPane flow = new FlowPane();
		flow.setOrientation(Orientation.HORIZONTAL);

		Button buttonA = new Button("Do something A");
		buttonA.setMinHeight(103);
		buttonA.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> System.out.println(buttonA.getWidth()));
		Button buttonB = new Button("Do something B");
		buttonB.setMinHeight(103);

		flow.getChildren().addAll(buttonA, buttonB);
		FlowPane.setMargin(buttonA, new Insets(10, 10, 10, 10));
		Canvas canvas = new Canvas(760, 400);

		Pane canvasPane = new Pane();
		canvasPane.resize(760, 400);
		canvasPane.getChildren().addAll(canvas);

		GridPane.setConstraints(canvasPane, 0, 0);
		GridPane.setConstraints(flow, 0, 1);
		grid.gridLinesVisibleProperty().set(true);
		Scene scene = new Scene(grid, 200, 200);

		Graph g = Graph.loadGraph("src/me/thamma/minimap.map");

		g.draw(canvasPane);

		grid.getChildren().addAll(flow, canvasPane);
		scene.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}