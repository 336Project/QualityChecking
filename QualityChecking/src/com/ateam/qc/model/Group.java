package com.ateam.qc.model;

import net.tsz.afinal.annotation.sqlite.Table;
@Table(name=Group.TABLE_GROUP)
public class Group{
	public static final String TABLE_GROUP="tb_group";
	
	private int id;
	private String name;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
