package me.thamma.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.thamma.game.Edge;
import me.thamma.game.Mission;
import me.thamma.game.Node;
import me.thamma.game.TrackKind;

public class MapParser {

	private List<Node> nodes;
	private List<Edge> edges;
	private Map<TrackKind, Integer> cards;
	private List<Mission> missions;
	int maxTracks;

	public MapParser(String filename) {
		this(FileUtils.loadFile(filename));
	}

	public MapParser(List<String> l) {
		this.cards = new HashMap<TrackKind, Integer>();
		this.edges = new ArrayList<Edge>();
		this.nodes = new ArrayList<Node>();
		this.missions = new ArrayList<Mission>();
		int mode = 0;
		int offset = 0;
		try {
			for (int i = 0; i < l.size(); i++) {
				String s = l.get(i);
				if (s.equalsIgnoreCase("Limits:")) {
					mode = 0;
					offset = i + 1;
				} else if (s.equalsIgnoreCase("Staedte:")) {
					mode = 1;
					offset = i + 1;
				} else if (s.equalsIgnoreCase("Strecken:")) {
					mode = 2;
					offset = i + 1;
				} else if (s.equalsIgnoreCase("Auftraege:")) {
					mode = 3;
					offset = i + 1;
				} else {
					String[] args = s.split(",");
					switch (mode) {
					case 0: {
						// initialize stack of cards
						// initialize maxTracks
						if (args[0].equalsIgnoreCase("MaxTracks")) {
							this.maxTracks = Integer.parseInt(args[1]);
						} else {
							TrackKind kind = TrackKind.valueOf(args[0]);
							int amount = Integer.parseInt(args[1]);
							this.cards.put(kind, amount);
						}
					}
						break;
					case 1: {
						// build nodes
						String name = args[0];
						int x = Integer.parseInt(args[1]);
						int y = Integer.parseInt(args[2]);
						Node node = new Node(name, i - offset, x, y);
						//add node to graph
						this.nodes.add(node);
					}
						break;
					case 2: {
						// build edges
						int cityid1 = Integer.parseInt(args[0]);
						int cityid2 = Integer.parseInt(args[1]);
						int length = Integer.parseInt(args[2]);
						TrackKind kind = TrackKind.valueOf(args[3]);
						boolean tunnel = args[4].equals("T");
						Edge edge = new Edge(i - offset, length, kind.getColor(), tunnel);
						// invariant: nodes are already built!
						// linked registering of the edges in nodes
						this.nodes.get(cityid1).appendEdge(edge, this.nodes.get(cityid2));
						this.nodes.get(cityid2).appendEdge(edge, this.nodes.get(cityid1));
						// add edge to graph
						this.edges.add(edge);

					}
						break;
					case 3: {
						// build missions
						int points = Integer.parseInt(args[0]);
						int node1 = Integer.parseInt(args[1]);
						int node2 = Integer.parseInt(args[2]);
						Mission mission = new Mission(points, node1, node2);
						this.missions.add(mission);
					}
						break;
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Could not parse");
			System.exit(1);
		}
	}

	public List<Node> getNodes() {
		return this.nodes;
	}

	public List<Edge> getEdges() {
		return edges;
	}

	public Map<TrackKind, Integer> getCards() {
		return cards;
	}

	public List<Mission> getQuests() {
		return missions;
	}

}
