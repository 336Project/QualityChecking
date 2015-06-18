package com.ateam.qc.model;

import net.tsz.afinal.annotation.sqlite.Table;
/**
 * 型号
 * @author Helen
 * 2015-6-18上午9:18:08
 */
@Table(name=Size.TABLE_SIZE)
public class Size{
	public static final String TABLE_SIZE="tb_size";
	
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
