package com.ateam.qc.dao;

import java.util.List;

import android.content.Context;

import com.ateam.qc.model.ExcelPictureItem;
import com.team.hbase.dao.HBaseDao;

public class ExcelPictureItemDao extends HBaseDao {

	public ExcelPictureItemDao(Context context) {
		super(context);
	}
	
	public void save(ExcelPictureItem pro){
		mDb.save(pro);
	}
	
	public List<ExcelPictureItem> query(){
		return mDb.findAll(ExcelPictureItem.class, "id ASC");
	}
	
	public void deleteAll(){
		mDb.deleteAll(ExcelPictureItem.class);
	}
	
	public void delete(int id){
		mDb.deleteById(ExcelPictureItem.class, id);
	}
	
}
