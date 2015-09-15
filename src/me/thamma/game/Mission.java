package me.thamma.game;

public class Mission {

	private int points;
	private int cityId1;
	private int cityId2;

	public Mission(int points, int cityId1, int cityId2) {
		this.points = points;
		this.cityId1 = cityId1;
		this.cityId2 = cityId2;
	}

	public int getPoints() {
		return this.points;
	}

	public int getCityId1() {
		return this.cityId1;
	}

	public int getCityId2() {
		return this.cityId2;
	}

}