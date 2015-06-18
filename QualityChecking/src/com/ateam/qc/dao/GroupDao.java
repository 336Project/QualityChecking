package com.ateam.qc.dao;

import java.util.List;

import android.content.Context;

import com.ateam.qc.model.Group;
import com.team.hbase.dao.HBaseDao;

public class GroupDao extends HBaseDao {

	public GroupDao(Context context) {
		super(context);
	}
	
	public void save(Group group){
		mDb.save(group);
	}
	
	public List<Group> query(){
		return mDb.findAll(Group.class);
	}
	
	public void delete(int id){
		mDb.deleteById(Group.class, id);
	}
	
	public void deleteByIds(List<Integer> ids){
		if(ids!=null){
			for (int id : ids) {
				delete(id);
			}
		}
	}
	public boolean isExist(String name){
		List<Group> groups=mDb.findAllByWhere(Group.class, "name = '"+name+"'");
		if(groups!=null&&groups.size()>0){
			return true;
		}
		return false;
	}
}
