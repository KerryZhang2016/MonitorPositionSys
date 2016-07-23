package com.bupt.monitorpositionsys.db;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class Path extends DataSupport{
	private int id;
	private String date;
	private List<PathDetail> pathDetails = new ArrayList<PathDetail>();
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public List<PathDetail> getPathDetails() {
		return DataSupport.where("path_id = ?", String.valueOf(id)).find(PathDetail.class);  
	}
	public void setPathDetails(List<PathDetail> pathDetails) {
		this.pathDetails = pathDetails;
	}

	
}
