package com.ateam.qc.dao;

import java.util.List;

import android.content.Context;

import com.ateam.qc.model.Size;
import com.team.hbase.dao.HBaseDao;

public class SizeDao extends HBaseDao {

	public SizeDao(Context context) {
		super(context);
	}
	
	public void save(Size size){
		mDb.save(size);
	}
	
	public List<Size> query(){
		return mDb.findAll(Size.class);
	}
	
	public void delete(int id){
		mDb.deleteById(Size.class, id);
	}
	
	public void deleteByIds(List<Integer> ids){
		if(ids!=null){
			for (int id : ids) {
				delete(id);
			}
		}
	}
	
	public boolean isExist(String name){
		List<Size> sizes=mDb.findAllByWhere(Size.class, "name = '"+name+"'");
		if(sizes!=null&&sizes.size()>0){
			return true;
		}
		return false;
	}
}
