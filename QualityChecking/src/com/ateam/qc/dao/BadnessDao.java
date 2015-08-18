package com.ateam.qc.dao;

import java.util.List;

import android.content.Context;

import com.ateam.qc.model.Badness;
import com.team.hbase.dao.HBaseDao;

public class BadnessDao extends HBaseDao {

	public BadnessDao(Context context) {
		super(context);
	}
	
	public void save(Badness badness){
		mDb.save(badness);
	}
	
	public List<Badness> query(){
		return mDb.findAll(Badness.class);
	}
	
	public List<Badness> queryByGroupName(String groupName){
		return mDb.findAllByWhere(Badness.class, "groupName = '"+groupName+"'");
	}
	
	public void delete(int id){
		mDb.deleteById(Badness.class, id);
	}
	
	public void deleteByIds(List<Integer> ids){
		if(ids!=null){
			for (int id : ids) {
				delete(id);
			}
		}
	}
	
	public void update(Badness b){
		mDb.update(b);
	}
	
	public boolean isExist(String name){
		List<Badness> bs=mDb.findAllByWhere(Badness.class, "name = '"+name+"'");
		if(bs!=null&&bs.size()>0){
			return true;
		}
		return false;
	}
}
