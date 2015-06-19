package com.ateam.qc.dao;

import java.util.List;

import android.content.Context;

import com.ateam.qc.model.Excel;
import com.ateam.qc.model.Project;
import com.team.hbase.dao.HBaseDao;

public class ExcelDao extends HBaseDao {

	public ExcelDao(Context context) {
		super(context);
	}
	
	public void save(Excel pro){
		mDb.save(pro);
	}
	
	public List<Excel> query(){
		return mDb.findAll(Excel.class, "no ASC");
	}
	public void delete(int id){
		mDb.deleteById(Excel.class, id);
	}
	
	public void deleteByIds(List<Integer> ids){
		if(ids!=null){
			for (int id : ids) {
				delete(id);
			}
		}
	}
	
	public boolean isExist(String no){
		List<Excel> pros=mDb.findAllByWhere(Excel.class, "no = '"+no+"'");
		if(pros!=null&&pros.size()>0){
			return true;
		}
		return false;
	}
}
