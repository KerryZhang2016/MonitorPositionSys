package com.bupt.monitorpositionsys.db;

import org.litepal.crud.DataSupport;

public class PathDetail extends DataSupport{

	private int id;
	private Path path;
	private double latitude;
	private double longtitude;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Path getPath() {
		return path;
	}
	public void setPath(Path path) {
		this.path = path;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongtitude() {
		return longtitude;
	}
	public void setLongtitude(double longtitude) {
		this.longtitude = longtitude;
	}
	
	
}
