package me.thamma;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

}
