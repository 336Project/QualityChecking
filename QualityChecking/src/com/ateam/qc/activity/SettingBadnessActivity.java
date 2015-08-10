package com.ateam.qc.activity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;

import com.ateam.qc.R;
import com.ateam.qc.application.MyApplication;
import com.ateam.qc.dao.BadnessDao;
import com.ateam.qc.dao.GroupDao;
import com.ateam.qc.dao.ProjectDao;
import com.ateam.qc.model.Badness;
import com.ateam.qc.model.Group;
import com.ateam.qc.model.Project;
import com.team.hbase.activity.HBaseActivity;
import com.team.hbase.adapter.HBaseAdapter;
import com.team.hbase.adapter.ViewHolder;
import com.team.hbase.widget.HAutoCompleteTextView;

/**
 * 不良状况设置
 * @author Helen
 * 2015-6-18上午9:52:03
 */
public class SettingBadnessActivity extends HBaseActivity {
	private ListView mListView;
	private List<Badness> mDatas;
	private BadnessAdapter mAdapter;
	private BadnessDao mBadnessDao;
	
	private MyApplication mApplication;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getRightIcon().setVisibility(View.GONE);
		getLeftIcon().setImageDrawable(getResources().getDrawable(R.drawable.back));
		setBaseContentView(R.layout.activity_setting_badness);
		setActionBarTitle("不良状况设置");
		init();
		initApplication();
	}

	private void initApplication() {
		mApplication = (MyApplication)getApplication();
	}

	private void init() {
		mListView=(ListView) findViewById(R.id.listView);
		mBadnessDao=new BadnessDao(this);
		mDatas=mBadnessDao.query();
		if(mDatas==null||mDatas.size()==0){
			mDatas=new ArrayList<Badness>();
			showMsg(this, R.string.empty_data);
		}
		mAdapter=new BadnessAdapter(this, mDatas);
		mListView.setAdapter(mAdapter);
	}
	/**
	 * 添加项目
	 */
	public void addBadness(View view){
		AlertDialog.Builder builder=new Builder(this);
		builder.setCancelable(false);
		builder.setTitle("添加不良状况");
		View v=LayoutInflater.from(this).inflate(R.layout.item_dialog_add_badness, null);
		
		//组别
		final Spinner groupSpinner=(Spinner) v.findViewById(R.id.sp_group);
		ArrayList<String> groupList = new ArrayList<String>();
		GroupDao groupDao = new GroupDao(this);
		List<Group> groups = groupDao.query();
		if(groups!=null){
			for (Group group : groups) {
				groupList.add(group.getName());
			}
		}
		ArrayAdapter<String> groupAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, groupList);
		groupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		groupSpinner.setAdapter(groupAdapter);
		
		final HAutoCompleteTextView editContent = (HAutoCompleteTextView) v.findViewById(R.id.et_content);
		builder.setView(v);
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dismiss(dialog, true);
			}
		});
		builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String content=editContent.getText().toString();
				String groupName = groupSpinner.getSelectedItem().toString();
				if(TextUtils.isEmpty(groupName)){
					showMsg(SettingBadnessActivity.this, "组别不能为空");
					dismiss(dialog, false);
				}else if(TextUtils.isEmpty(content)){
					showMsg(SettingBadnessActivity.this, "内容不能为空");
					dismiss(dialog, false);
				}else{
					save(content,groupName);
					dismiss(dialog, true);
					showMsg(SettingBadnessActivity.this, "添加成功");
				}
				
			}
		});
		builder.create().show();
		
	}
	/**
	 * 删除项目
	 */
	public void deleteBadness(View view){
		boolean[] isChecked=mAdapter.getHasChecked();
		for (int i = 0; i < isChecked.length; i++) {
			if(isChecked[i]){
				mBadnessDao.delete(mDatas.get(i).getId());
			}
		}
		notifyDataSetChanged();
		mApplication.setRefreshProject(true);
		showMsg(SettingBadnessActivity.this, "删除成功");
	}
	/**
	 * 修改 
	 **/
	public void updateBadness(View view){
		boolean[] isChecked=mAdapter.getHasChecked();
		int count = 0;
		Badness b = null;
		for (int i = 0; i < isChecked.length; i++) {
			if(isChecked[i]){
				count++;
				if(count > 1){
					showMsg(this, "一次只能修改一项");
					return;
				}
				b = mDatas.get(i);
			}
		}
		if(isChecked.length == 0 || b == null){
			showMsg(this, "请选中项再操作");
			return;
		}
		final Badness currBad = b;
		
		AlertDialog.Builder builder=new Builder(this);
		builder.setCancelable(false);
		builder.setTitle("修改项目");
		View v=LayoutInflater.from(this).inflate(R.layout.item_dialog_add_badness, null);
		
		//组别
		final Spinner groupSpinner=(Spinner) v.findViewById(R.id.sp_group);
		ArrayList<String> groupList = new ArrayList<String>();
		GroupDao groupDao = new GroupDao(this);
		List<Group> groups = groupDao.query();
		if(groups!=null){
			for (Group group : groups) {
				groupList.add(group.getName());
			}
		}
		ArrayAdapter<String> groupAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, groupList);
		groupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		groupSpinner.setAdapter(groupAdapter);
		
		final HAutoCompleteTextView editContent = (HAutoCompleteTextView) v.findViewById(R.id.et_content);
		editContent.setText(currBad.getName());
		builder.setView(v);
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dismiss(dialog, true);
			}
		});
		builder.setPositiveButton("修改", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String groupName=groupSpinner.getSelectedItem().toString();
				String content=editContent.getText().toString();
				if(TextUtils.isEmpty(groupName)){
					showMsg(SettingBadnessActivity.this, "组别不能为空");
					dismiss(dialog, false);
				}else if(TextUtils.isEmpty(content)){
					showMsg(SettingBadnessActivity.this, "内容不能为空");
					dismiss(dialog, false);
				}else{
					currBad.setGroupName(groupName);
					currBad.setName(content);
					mBadnessDao.update(currBad);
					dismiss(dialog, true);
					showMsg(SettingBadnessActivity.this, "修改成功");
					notifyDataSetChanged();
					mApplication.setRefreshProject(true);
				}
				
			}
		});
		builder.create().show();
	}
	
	private void dismiss(DialogInterface dialog,boolean isDismiss){
		try{
			Field field=dialog.getClass().getSuperclass().getDeclaredField("mShowing");
			field.setAccessible(true);
			field.set(dialog, isDismiss);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 保存
	 */
	private void save(String content,String groupName){
		Badness b=new Badness();
		b.setName(content);
		b.setGroupName(groupName);
		mBadnessDao.save(b);
		notifyDataSetChanged();
		mApplication.setRefreshProject(true);
	}
	/**
	 * 刷新列表
	 */
	private void notifyDataSetChanged(){
		mDatas.clear();
		mDatas.addAll(mBadnessDao.query());
		mAdapter.setHasChecked(new boolean[mDatas.size()]);
		mAdapter.notifyDataSetChanged();
	}
	/**
	 * 适配器
	 * @author Helen
	 * 2015-6-18上午10:13:21
	 */
	private class BadnessAdapter extends HBaseAdapter<Badness>{
		private boolean[] mHasChecked;
		public BadnessAdapter(Context c, List<Badness> datas) {
			super(c, datas);
			mHasChecked=new boolean[getCount()];
		}

		@Override
		public void convert(ViewHolder holder, Badness bean,final int position) {
			if(mHasChecked.length!=getCount()){  
	            boolean[] hasCheckedCache=mHasChecked.clone();  
	            mHasChecked=new boolean[getCount()];  
	            for(int i=0;i<hasCheckedCache.length;i++){  
	                mHasChecked[i]=hasCheckedCache[i];  
	            }  
	        }
			holder.getTextView(R.id.tv_badness_content).setText(bean.getName());
			holder.getTextView(R.id.tv_badness_group).setText(bean.getGroupName());
			final CheckBox cb=holder.getCheckBox(R.id.cb_badness);
			cb.setText(bean.getId()+"");
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
			return R.layout.adapter_setting_badness;
		}
		
		public boolean[] getHasChecked() {
			return mHasChecked;
		}

		public void setHasChecked(boolean[] mHasChecked) {
			this.mHasChecked = mHasChecked;
		}
		
	}
}
