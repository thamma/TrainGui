package me.thamma;

import java.awt.Toolkit;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
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
		// primaryStage.setFullScreen(true);
		Button btn = new Button();
		btn.setText("Say 'Hello World'");
		GridPane grid = new GridPane();

		grid.setId("pane");
		grid.setPadding(new Insets(10, 10, 10, 10));
		grid.setVgap(8);
		grid.setHgap(10);

		FlowPane flow = new FlowPane();
		flow.setOrientation(Orientation.HORIZONTAL);

		Button buttonA = new Button("Do something A");
		buttonA.setMinHeight(103);
		// buttonA.addEventHandler(MouseEvent.MOUSE_CLICKED, e ->
		// System.out.println(buttonA.getWidth()));
		Button buttonB = new Button("Do something B");
		buttonB.setMinHeight(103);

		flow.getChildren().addAll(buttonA, buttonB);
		FlowPane.setMargin(buttonA, new Insets(10, 10, 10, 10));
		Canvas canvas = new Canvas(760, 400);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		// drawShapes(gc, canvas);

		GridPane.setConstraints(canvas, 0, 0);
		GridPane.setConstraints(flow, 0, 1);
		grid.gridLinesVisibleProperty().set(true);
		Scene scene = new Scene(grid, 200, 200);

		Graph g = Graph.loadGraph("minimap.map");
		
		Graph g2 = new Graph(new ArrayList<String>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -6780336939975063126L;

			{
				add("Oberhausen,1,1");
				add("Essen,3,1");
				add("Bochum,5,1");
				add("Wuppertal,4,5");
				add("Hagen,7,3");
				add("Dortmund,7,0");
			}
		}, new ArrayList<String>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 5638778913293461956L;

			{
				add("0,1,2,ALL");
				add("1,2,2,ALL");
				add("2,4,3,BLACK");
				add("3,4,3,RED");
				add("3,1,4,GREEN");
				add("0,3,2,BLUE");
				add("5,2,2,YELLOW");
				add("5,4,2,ORANGE");

			}
		});
		draw(gc, canvas, g);

		grid.getChildren().addAll(flow, canvas);
		scene.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private void drawShapes(GraphicsContext gc, Canvas canvas) {
		gc.setFill(Color.GREEN);
		gc.setStroke(Color.BLUE);
		gc.setLineWidth(5);
		gc.strokeLine(40, 10, 10, 40);
		gc.fillOval(10, 60, 30, 30);
		gc.strokeOval(60, 60, 30, 30);
		gc.fillRoundRect(110, 60, 30, 30, 10, 10);
		gc.strokeRoundRect(160, 60, 30, 30, 10, 10);
		gc.fillArc(10, 110, 30, 30, 45, 240, ArcType.OPEN);
		gc.fillArc(60, 110, 30, 30, 45, 240, ArcType.CHORD);
		gc.fillArc(110, 110, 30, 30, 45, 240, ArcType.ROUND);
		gc.strokeArc(10, 160, 30, 30, 45, 240, ArcType.OPEN);
		gc.strokeArc(60, 160, 30, 30, 45, 240, ArcType.CHORD);
		gc.strokeArc(110, 160, 30, 30, 45, 240, ArcType.ROUND);
		gc.fillPolygon(new double[] { 10, 40, 10, 40 }, new double[] { 210, 210, 240, 240 }, 4);
		gc.strokePolygon(new double[] { 60, 90, 60, 90 }, new double[] { 210, 210, 240, 240 }, 4);
		gc.strokePolyline(new double[] { 110, 140, 110, 140 }, new double[] { 210, 210, 240, 240 }, 4);
	}

	public void draw(GraphicsContext gc, Canvas canvas, Graph g) {
		int radius = 8;
		int padding = 80;
		// gc.strokePolygon(new double[] { 0, 100 }, new double[] { 0, 100 },
		// 2);
		for (Edge e : g.getEdges()) {
			Node from = g.getNodes().get(e.getFrom());
			Node to = g.getNodes().get(e.getTo());
			gc.setStroke(e.getColor());
			gc.setLineWidth(radius);

			gc.strokePolygon(
					new double[] { from.getCanvasCoordinate(g, canvas, padding).getX(),
							to.getCanvasCoordinate(g, canvas, padding).getX() },
					new double[] { from.getCanvasCoordinate(g, canvas, padding).getY(),
							to.getCanvasCoordinate(g, canvas, padding).getY() },
					2);
		}
		gc.setFill(Color.BLACK);
		for (Node n : g) {
			double x = n.getCanvasCoordinate(g, canvas, padding).getX();
			double y = n.getCanvasCoordinate(g, canvas, padding).getY();
			gc.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);
			gc.setTextAlign(TextAlignment.CENTER);
			gc.setFont(new Font("Comic Sans MS", 20));
			gc.fillText(n.getName(), x, y - 15);
		}

	}

}