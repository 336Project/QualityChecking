package com.ateam.qc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ateam.qc.adapter.ContentItemAdapter;
import com.ateam.qc.dao.GroupDao;
import com.ateam.qc.dao.ProjectDao;
import com.ateam.qc.model.ExcelItem;
import com.ateam.qc.model.Group;
import com.ateam.qc.model.Project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity implements OnClickListener{

    private DrawerLayout drawerLayout;
    private static final String[] ITEMS = {"Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6"};
	private ArrayAdapter<String> adapter;
	private ListView lvContentBody;
	private View viewHead;
	private GroupDao mGroupDao;
	private List<Group> mDatas;
	private ArrayList<ExcelItem> mExcelItems=new ArrayList<ExcelItem>();
	private ProjectDao mProjectDao;
	private List<Project> mProjects;
	private ContentItemAdapter mAdapter;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }
	private void initView() {
		findViewById(R.id.tv_input).setOnClickListener(this);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		findViewById(R.id.rl_left).setOnClickListener(this);
		findViewById(R.id.ll_set).setOnClickListener(this);
		findViewById(R.id.ll_history).setOnClickListener(this);
		//1.单头
//		TextView tvTime = (TextView) findViewById(R.id.tv_time);
//		tvTime.setText(formatTime());
//		spChooseGroup = (MaterialSpinner) findViewById(R.id.sp_choose_group);
//		initSpinnerNoHintNoFloatingLabel();
		initListViewHead();
		//2.单体
		initListView();
	}
	private void initListViewHead() {
		viewHead = LayoutInflater.from(this).inflate(R.layout.content_head, null);
		TextView tvTime = (TextView) viewHead.findViewById(R.id.tv_time);
		tvTime.setText(formatTime());
		initSpinnerNoHintNoFloatingLabel();
	}
	private void initSpinnerNoHintNoFloatingLabel() {
		
		mGroupDao=new GroupDao(this);
		mDatas=mGroupDao.query();
		if(mDatas==null||mDatas.size()==0){
			mDatas=new ArrayList<Group>();
//			showMsg(this, R.string.empty_data);
		}
		String[] mGroupString = new String[mDatas.size()];
		for (int i = 0; i < mDatas.size(); i++) {
			mGroupString[i]=new String(mDatas.get(i).getName());
		}
		
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mGroupString);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner spinner1 = (Spinner) viewHead.findViewById(R.id.spinner1);
		spinner1.setAdapter(adapter);
	}
	private void initListView() {
		lvContentBody = (ListView) findViewById(R.id.lv_content_body);
		lvContentBody.addHeaderView(viewHead);
//		if(mDatas==null||mDatas.size()==0){
//			mDatas=new ArrayList<Project>();
//			showMsg(this, R.string.empty_data);
//		}
		mAdapter=new ContentItemAdapter(this,mExcelItems);
		lvContentBody.setAdapter(mAdapter);
		findAllData();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		findAllData();
	}
	
	/**
	 * 获取项目表中的数据，并且刷新里列表
	 */
	private void findAllData(){
		mProjectDao=new ProjectDao(this);
		mProjects=mProjectDao.query();
		for (int i = 0; i < mProjects.size(); i++) {
			ExcelItem excelItem = new ExcelItem();
			excelItem.setProject(mProjects.get(i));
			mExcelItems.add(excelItem);
		}
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.rl_left:
			drawerLayout.openDrawer(Gravity.LEFT);
			break;
		case R.id.ll_set:
			intent.setClass(MainActivity.this, SetActivity.class);
			startActivity(intent);
			break;
		case R.id.ll_history:
			intent.setClass(MainActivity.this, HistoryActivity.class);
			startActivity(intent);
			break;
		case R.id.tv_input:
			//测试保存使用数据
//			a
			break;
		default:
			break;
		}
	}
	public String formatTime(){
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
		return simpleDateFormat.format(date);
	}

}
