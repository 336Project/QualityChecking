package com.ateam.qc.model;

import java.util.ArrayList;

import android.app.ListActivity;

public class Excel {
	private int id; //ExcelId
	private ArrayList<ExcelItem> excelItemsList = new ArrayList<ExcelItem>(); //记录列表
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public ArrayList<ExcelItem> getExcelItemsList() {
		return excelItemsList;
	}
	public void setExcelItemsList(ArrayList<ExcelItem> excelItemsList) {
		this.excelItemsList = excelItemsList;
	}
	
}
