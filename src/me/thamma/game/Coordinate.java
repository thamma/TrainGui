package me.thamma.game;

public class Coordinate {
	private double x;
	private double y;

	public Coordinate(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Coordinate(int x, int y) {
		this(Double.valueOf(x), Double.valueOf(y));
	}

	/**
	 * 
	 * @param remote
	 *            The coordinate calculating the distance to
	 * @return The Cartesian distance
	 */
	public double getDistance(Coordinate remote) {
		return this.sub(remote).getLength();
	}

	public double getX() {
		return this.x;
	}

	public double getY() {
		return this.y;
	}

	public Coordinate add(Coordinate remote) {
		return new Coordinate(this.x + remote.getX(), this.y + remote.getY());
	}

	public Coordinate sub(Coordinate remote) {
		return this.add(remote.multiply(-1.0));
	}

	public Coordinate multiply(Double d) {
		return new Coordinate(this.x * d, this.y * d);
	}

	public Coordinate normalize() {
		return this.clone().multiply(1 / this.getLength());
	}

	public Coordinate normalize(double d) {
		return this.normalize().multiply(d);
	}

	public double getLength() {
		return Math.sqrt(this.x * this.x + this.y * this.y);
	}

	@Override
	public Coordinate clone() {
		return new Coordinate(this.x, this.y);
	}

	@Override
	public String toString() {
		return "(" + this.x + "," + this.y + ")";
	}
}