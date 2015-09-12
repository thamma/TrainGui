package me.thamma;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;

public class Graph implements Iterable<Node> {

	private List<Node> nodes;
	private List<Edge> edges;

	public Graph(List<String> nodes, List<String> edges) {
		this.nodes = new ArrayList<Node>();
		for (int i = 0; i < nodes.size(); i++) {
			String[] node = nodes.get(i).split(",");
			this.nodes.add(new Node(node[0], i, Integer.valueOf(node[1]), Integer.valueOf(node[2])));
		}
		this.edges = new ArrayList<Edge>();
		for (int i = 0; i < edges.size(); i++) {
			String[] edge = edges.get(i).split(",");
			System.out.println(i + " " + TrackKind.valueOf(edge[3]));
			this.edges.add(new Edge(i, Integer.parseInt(edge[0]), Integer.parseInt(edge[1]), Integer.parseInt(edge[2]),
					TrackKind.valueOf(edge[3]).getColor()));
		}
	}

	public static Graph loadGraph(String filename) {
		List<String> in = FileUtils.loadFile(filename);
		List<String> edges = new ArrayList<String>();
		List<String> nodes = new ArrayList<String>();
		while (!in.get(0).equalsIgnoreCase("Staedte:"))
			in.remove(0);
		in.remove(0);// remove "Staedte"
		while (!in.get(0).equalsIgnoreCase("Strecken:"))
			nodes.add(in.remove(0));
		in.remove(0);// remove Strecken
		while (!in.get(0).equalsIgnoreCase("Auftraege:"))
			edges.add(in.remove(0));
		return new Graph(nodes, edges);
	}

	@Override
	public Iterator<Node> iterator() {
		return nodes.iterator();
	}

	public List<Edge> getEdges() {
		return this.edges;
	}

	public List<Node> getNodes() {
		return this.nodes;
	}

	private Coordinate minCoord, maxCoord;

	public Coordinate getMinCoordinate() {
		if (minCoord == null)
			setupMinMax();
		return minCoord;
	}

	public Coordinate getMaxCoordinate() {
		if (maxCoord == null)
			setupMinMax();
		return maxCoord;
	}

	private void setupMinMax() {
		double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE, maxX = 0, maxY = 0;
		for (Node n : this) {
			minX = Math.min(n.getCoordinate().getX(), minX);
			minY = Math.min(n.getCoordinate().getY(), minY);
			maxX = Math.max(n.getCoordinate().getX(), maxX);
			maxY = Math.max(n.getCoordinate().getY(), maxY);
		}
		this.minCoord = new Coordinate(minX, minY);
		this.maxCoord = new Coordinate(maxX, maxY);
	}

	public void draw(Pane overlay, GraphicsContext gc, Canvas canvas) {
		int radius = 8;
		int padding = 80;

		for (Edge e : this.getEdges()) {
			Node from = this.getNodes().get(e.getFrom());
			Node to = this.getNodes().get(e.getTo());
			Line l = new Line(from.getCanvasCoordinate(this, canvas, padding).getX(),
					from.getCanvasCoordinate(this, canvas, padding).getY(),
					to.getCanvasCoordinate(this, canvas, padding).getX(),
					to.getCanvasCoordinate(this, canvas, padding).getY());
			l.setStrokeWidth(radius/2);
			l.setStroke(e.getColor());
			// l.setStyle("-fx-border-color: white;");
			double gap = radius + 15;
			double length = from.getCanvasCoordinate(this, canvas, padding)
					.getDistance(to.getCanvasCoordinate(this, canvas, padding));
			double a = (length - (e.getLength() - 1) * gap) / Double.valueOf(e.getLength());
			if (e.getLength() > 0) {
				l.getStrokeDashArray().addAll(a, gap);
			}
			overlay.getChildren().add(l);
			// gc.setStroke(e.getColor());
			// gc.setLineWidth(radius);
			// gc.strokePolygon(
			// new double[] { from.getCanvasCoordinate(this, canvas,
			// padding).getX(),
			// to.getCanvasCoordinate(this, canvas, padding).getX() },
			// new double[] { from.getCanvasCoordinate(this, canvas,
			// padding).getY(),
			// to.getCanvasCoordinate(this, canvas, padding).getY() },
			// 2);

		}
		gc.setFill(Color.BLACK);
		for (Node n : this) {
			double x = n.getCanvasCoordinate(this, canvas, padding).getX();
			double y = n.getCanvasCoordinate(this, canvas, padding).getY();
			Circle node = new Circle(radius);
			node.relocate(x - radius, y - radius);
			node.addEventHandler(MouseEvent.MOUSE_CLICKED, arg0 -> {
				System.out.println("CALLED" + node.getLayoutX());
			});
			Label l = new Label(n.getName());
			l.setFont(new Font("Arial", 12));
			l.relocate(x - radius, y - radius - 20);
			overlay.getChildren().addAll(node, l);
			// gc.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);
			// gc.setTextAlign(TextAlignment.CENTER);
			// gc.setFont(new Font("Arial", 16));
			// gc.fillText(n.getName(), x, y - 15);
		}
	}

}
