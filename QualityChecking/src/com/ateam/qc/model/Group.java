package com.ateam.qc.model;

import net.tsz.afinal.annotation.sqlite.Table;
/**
 * 组别
 * @author Helen
 * 2015-6-18上午9:18:00
 */
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
