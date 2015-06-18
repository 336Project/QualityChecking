package com.ateam.qc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ateam.qc.dao.GroupDao;
import com.ateam.qc.model.Group;
import com.team.hbase.activity.HBaseActivity;
import com.team.hbase.adapter.HBaseAdapter;
import com.team.hbase.adapter.ViewHolder;
import com.team.hbase.widget.HAutoCompleteTextView;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
/**
 * 组别设置
 * @author Helen
 * 2015-6-17下午2:46:32
 */
public class SettingGroupActivity extends HBaseActivity {
	private ListView mListView;
	private List<Group> mDatas;
	private HAutoCompleteTextView mEditGroupName;
	private GroupDao mGroupDao;
	private GroupAdapter mAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getRightIcon().setVisibility(View.GONE);
		getLeftIcon().setImageResource(R.drawable.back);
		setBaseContentView(R.layout.activity_setting_group);
		setActionBarTitle("组别设置");
		init();
	}
	
	/**
	 * 初始化
	 */
	private void init() {
		mListView=(ListView) findViewById(R.id.listView);
		mEditGroupName=(HAutoCompleteTextView) findViewById(R.id.et_group_name);
		mGroupDao=new GroupDao(this);
		mDatas=mGroupDao.query();
		if(mDatas==null||mDatas.size()==0){
			mDatas=new ArrayList<Group>();
			showMsg(this, R.string.empty_data);
		}
		mAdapter=new GroupAdapter(this, mDatas);
		mListView.setAdapter(mAdapter);
	}
	/**
	 * 添加组别
	 */
	public void addGroup(View view){
		String name=mEditGroupName.getText().toString();
		if(TextUtils.isEmpty(name)){
			showMsg(this, "请输入组别名称");
			return;
		}
		if(mGroupDao.isExist(name)){
			showMsg(this, "该组别已存在，请重新输入");
			return;
		}
		Group group=new Group();
		group.setName(name);
		mGroupDao.save(group);
		mDatas.clear();
		mDatas.addAll(mGroupDao.query());
		mAdapter.notifyDataSetChanged();
	}
	/**
	 * 删除组别
	 */
	public void deleteGroup(View view){
		boolean[] isChecked=mAdapter.getHasChecked();
		for (int i = 0; i < isChecked.length; i++) {
			if(isChecked[i]){
				mGroupDao.delete(mDatas.get(i).getId());
			}
		}
		mDatas.clear();
		mDatas.addAll(mGroupDao.query());
		mAdapter.setHasChecked(new boolean[mDatas.size()]);
		mAdapter.notifyDataSetChanged();
	}
	
	private class GroupAdapter extends HBaseAdapter<Group>{
		private boolean[] mHasChecked;
		public GroupAdapter(Context c, List<Group> datas) {
			super(c, datas);
			mHasChecked=new boolean[getCount()];
		}

		@Override
		public void convert(final ViewHolder holder, Group bean,final int position) {
			if(mHasChecked.length!=getCount()){  
	            boolean[] hasCheckedCache=mHasChecked.clone();  
	            mHasChecked=new boolean[getCount()];  
	            for(int i=0;i<hasCheckedCache.length;i++){  
	                mHasChecked[i]=hasCheckedCache[i];  
	            }  
	        }
			
			((TextView)holder.getView(R.id.tv_group_name)).setText(bean.getName());
			final CheckBox cb=holder.getView(R.id.cb_group);
			holder.getConvertView().setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					boolean isChecked=!cb.isChecked();
					cb.setChecked(isChecked);
					mHasChecked[position]=isChecked;
				}
			});
			cb.setChecked(mHasChecked[position]);
		}

		@Override
		public int getResId() {
			return R.layout.adapter_setting_group;
		}

		public boolean[] getHasChecked() {
			return mHasChecked;
		}

		public void setHasChecked(boolean[] mHasChecked) {
			this.mHasChecked = mHasChecked;
		}
		
	}
}
