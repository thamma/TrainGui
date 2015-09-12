package me.thamma;

public class Coordinate {
	private double x;
	private double y;

	public Coordinate(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public double getDistance(Coordinate remote) {
		double dx = this.x - remote.x;
		double dy = this.y - remote.y;
		return Math.sqrt(dx * dx + dy * dy);
	}

	public double getX() {
		return this.x;
	}

	public double getY() {
		return this.y;
	}

	@Override
	public String toString() {
		return "(" + this.x + "," + this.y + ")";
	}
}
