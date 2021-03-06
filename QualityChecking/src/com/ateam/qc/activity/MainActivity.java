package com.ateam.qc.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ateam.qc.R;
import com.ateam.qc.application.MyApplication;
import com.ateam.qc.dao.BadnessDao;
import com.ateam.qc.dao.ExcelDao;
import com.ateam.qc.dao.ExcelItemDao;
import com.ateam.qc.dao.GroupDao;
import com.ateam.qc.dao.ProjectDao;
import com.ateam.qc.dao.SizeDao;
import com.ateam.qc.model.Badness;
import com.ateam.qc.model.Excel;
import com.ateam.qc.model.ExcelItem;
import com.ateam.qc.model.ExcelSave;
import com.ateam.qc.model.Group;
import com.ateam.qc.model.Project;
import com.ateam.qc.model.Size;
import com.ateam.qc.utils.ExportExcel;
import com.ateam.qc.utils.MyToast;
import com.ateam.qc.widget.ExcelItemLinearLayout;
import com.team.hbase.widget.dialog.CustomProgressDialog;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

	private static final int SUCCESS=0;
	private static final int FAIL=1;
	private DrawerLayout drawerLayout;
	private CustomProgressDialog mProgress;

	private GroupDao mGroupDao;
	private List<Group> mGroups;

	private ProjectDao mProjectDao;
	private List<Project> mProjects;

	private List<Badness> mBadnesses;

	private Spinner mSpinnerGroup;
	private LinearLayout mLinearlayoutForm;
	private HashMap<Integer, ExcelItem> mExcelItemDatas = new HashMap<Integer, ExcelItem>();
	private ArrayList<ExcelItemLinearLayout> mExcelItemLinearLayouts = new ArrayList<ExcelItemLinearLayout>();
	private SizeDao mSizeDao;
	private List<Size> mSizes;
	/**
	 * Spinner适配器需要的数组对象 1.Group 2.Project 3.Size 4.Badness
	 */
	private String[] mGroupStrings;
	private String[] mProjectStrings;
	private String[] mSizeStrings;
	private String[] mBadnessStrings;
	private Excel excel;

	/**
	 * Spinner适配器
	 */
	private ArrayAdapter<String> adapter;
	private ArrayAdapter<String> mSizeAdapter;
	private ArrayAdapter<String> mProjectAdapter;
	private ArrayAdapter<String> mBadnessAdapter;

	private MyApplication mApplication;
	private TextView tvTime;
	private EditText etFanhao;
	public static final int REQUEST_CODE_CAMERA = 1002;
	private String creatTime;
	private String excelName;
	private int currentGroupPosition = 0;
	private boolean isPause=false;
	/**
	 * 项目序号
	 */
	private int projectNum = 0;
	/**
	 * 项目选择计数器，第一次选择，即初始化时不执行
	 */
	private int groupSelectNum=0;
	
	Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			mProgress.stopAndDismiss();
			if(msg.what==MainActivity.SUCCESS){
				Toast.makeText(MainActivity.this, "生成excel成功！", Toast.LENGTH_SHORT)
				.show();
			}else{
				Toast.makeText(MainActivity.this, "生成excel失败！", Toast.LENGTH_SHORT)
				.show();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
	}

	private void initView() {
		mProgress=new CustomProgressDialog(this, "导出中...");
		TextView tvTitlename = (TextView) findViewById(R.id.tv_titlename);
		tvTitlename.setText("表格录入");
		RelativeLayout rlRight = (RelativeLayout) findViewById(R.id.rl_right);
		rlRight.setVisibility(View.INVISIBLE);
		findViewById(R.id.tv_add).setOnClickListener(this);
		findViewById(R.id.tv_input).setOnClickListener(this);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		findViewById(R.id.rl_left).setOnClickListener(this);
		findViewById(R.id.ll_set).setOnClickListener(this);
		findViewById(R.id.ll_history).setOnClickListener(this);

		initExcelHead();
		initExcelBody();
	}

	/**
	 * 表头
	 */
	private void initExcelHead() {
		mLinearlayoutForm = (LinearLayout) findViewById(R.id.ll_form);
		tvTime = (TextView) findViewById(R.id.tv_time);
		creatTime = formatTime();
		excelName = "表" + creatTime;
		tvTime.setText(excelName);
		etFanhao = (EditText) findViewById(R.id.et_fanhao);
		initSpinnerGroup();
	}

	/**
	 * 初始化表体
	 */
	private void initExcelBody() {
		initSpinnerSize();
		initSpinnerProject();
		initSpinnerBadness();
		mProjectDao = new ProjectDao(this);
		mProjects = mProjectDao.query();
		// for (Project project : mProjects) {
		// ExcelItemLinearLayout excelItemLinearLayout = new
		// ExcelItemLinearLayout(
		// this,MainActivity.this);
		// excelItemLinearLayout.getTvItemDescription().setText(
		// project.getContent());
		// excelItemLinearLayout.setId(project.getId());
		// excelItemLinearLayout.getSpSize().setAdapter(mSizeAdapter);
		// excelItemLinearLayout.setPhotoFileName(tvTime.getText().toString().trim()+project.getId()+".png");
		// // final Project finalProject =project;
		// // excelItemLinearLayout.getTvTakePhoto().setOnClickListener(new
		// OnClickListener() {
		// // @Override
		// // public void onClick(View v) {
		// // Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// // FileUtil.getInstance().createSDDir(SAVED_IMAGE_DIR_PATH);
		// // String fileName = "h2h3" + ".jpg";
		// // File file = FileUtil.getInstance().createFileInSDCard(
		// // SAVED_IMAGE_DIR_PATH, fileName);
		// // Uri fileUri = Uri.fromFile(file);
		// // intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
		// // startActivityForResult(intent, finalProject.getId());
		// // }
		// // });
		//
		// mExcelItemLinearLayouts.add(excelItemLinearLayout);
		// mLinearlayoutForm.addView(excelItemLinearLayout,
		// LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		// }

		ExcelItemLinearLayout excelItemLinearLayout = new ExcelItemLinearLayout(
				this, MainActivity.this);
		excelItemLinearLayout.setId(projectNum);
		excelItemLinearLayout.getSpSize().setAdapter(mSizeAdapter);
		excelItemLinearLayout.getSpProject().setAdapter(mProjectAdapter);
		excelItemLinearLayout.getSpBadness().setAdapter(mBadnessAdapter);

		excelItemLinearLayout.setPhotoFileName(fomatTimeByTime()+ excelItemLinearLayout.getId() + ".png");

		mExcelItemLinearLayouts.add(excelItemLinearLayout);
		mLinearlayoutForm.addView(excelItemLinearLayout,
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		
		// for (Project project : mProjects) {
		// ExcelItemLinearLayout excelItemLinearLayout = new
		// ExcelItemLinearLayout(
		// this,MainActivity.this);
		// excelItemLinearLayout.getTvItemDescription().setText(
		// project.getContent());
		// excelItemLinearLayout.setId(project.getId());
		// excelItemLinearLayout.getSpSize().setAdapter(mSizeAdapter);
		// excelItemLinearLayout.setPhotoFileName(tvTime.getText().toString().trim()+project.getId()+".png");
		//
		// mExcelItemLinearLayouts.add(excelItemLinearLayout);
		// mLinearlayoutForm.addView(excelItemLinearLayout,
		// LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		// }
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Uri uri = null;
		if (resultCode == RESULT_OK) {
			if (data != null && data.getData() != null) {
				uri = data.getData();
				Log.e("1", "1");
			}
			if (uri == null) {
				Log.e("2", "2");
				// uri = fileUri;
			}
		}
		if (uri != null) {
			Log.e("3", "3");
		} else {
			// Toast.makeText(this, "获取图片失败，请选择一张图片",
			// Toast.LENGTH_SHORT).show();
		}
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}
	/**
	 * 初始化项目内容
	 */
	private void initSpinnerProject() {
		if (mGroups.size() > 0) {
			int selectedItemPosition = mSpinnerGroup.getSelectedItemPosition();
			Group group = mGroups.get(selectedItemPosition);
			mProjectDao = new ProjectDao(this);
			mProjects = mProjectDao.queryByGroupName(group.getName());
		}
		if (mProjects == null || mProjects.size() == 0) {
			mProjects = new ArrayList<Project>();
		}
		mProjectStrings = new String[mProjects.size()];
		for (int i = 0; i < mProjects.size(); i++) {
			mProjectStrings[i] = new String(mProjects.get(i).getShortName());
		}
		mProjectAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, mProjectStrings);
		mProjectAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	}

	/**
	 * 初始化型号
	 */
	private void initSpinnerSize() {
		mSizeDao = new SizeDao(this);
		mSizes = mSizeDao.query();
		if (mSizes == null || mSizes.size() == 0) {
			mSizes = new ArrayList<Size>();
		}
		mSizeStrings = new String[mSizes.size()];
		for (int i = 0; i < mSizes.size(); i++) {
			mSizeStrings[i] = new String(mSizes.get(i).getName());
		}
		mSizeAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, mSizeStrings);
		mSizeAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	}

	/**
	 * 初始化不良状况
	 */
	private void initSpinnerBadness() {
		if (mGroups.size() > 0) {
			int selectedItemPosition = mSpinnerGroup.getSelectedItemPosition();
			Group group = mGroups.get(selectedItemPosition);
			BadnessDao badnessDao = new BadnessDao(this);
			mBadnesses = badnessDao.queryByGroupName(group.getName());
		}
		if (mBadnesses == null || mBadnesses.size() == 0) {
			mBadnesses = new ArrayList<Badness>();
		}
		mBadnessStrings = new String[mBadnesses.size()];
		for (int i = 0; i < mBadnesses.size(); i++) {
			mBadnessStrings[i] = new String(mBadnesses.get(i).getName());
		}
		mBadnessAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, mBadnessStrings);
		mBadnessAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	}

	/**
	 * 初始化表头的组别
	 */
	private void initSpinnerGroup() {
		mSpinnerGroup = (Spinner) findViewById(R.id.spinner1);

		mGroupDao = new GroupDao(this);
		mGroups = mGroupDao.query();
		if (mGroups == null || mGroups.size() == 0) {
			mGroups = new ArrayList<Group>();
		}
		mGroupStrings = new String[mGroups.size()];
		for (int i = 0; i < mGroups.size(); i++) {
			mGroupStrings[i] = new String(mGroups.get(i).getName());
		}
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, mGroupStrings);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinnerGroup.setAdapter(adapter);
		mSpinnerGroup.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if(groupSelectNum==0||isPause==true){
					groupSelectNum+=1;
					isPause=false;
				}
				else{
					projectNum=0;
					mLinearlayoutForm.removeAllViews();
					mExcelItemDatas = new HashMap<Integer, ExcelItem>();
					mExcelItemLinearLayouts = new ArrayList<ExcelItemLinearLayout>();
					findAllBadness();
					findAllProject();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}

	@Override
	protected void onPause() {
		super.onPause();
		isPause=true;
		for (ExcelItemLinearLayout excelItemLinearLayout : mExcelItemLinearLayouts) {
			ExcelItem excelItem = new ExcelItem();
			excelItem.setCheckNum(excelItemLinearLayout.getAsViewCheck()
					.getNum());
			excelItem.setUnqualifiedNum(excelItemLinearLayout
					.getAsViewUnqualified().getNum());
			excelItem.setExamineNum(excelItemLinearLayout.getAsViewExamine()
					.getNum());
			excelItem.setNgNum(excelItemLinearLayout.getAsViewNg().getNum());
			excelItem.setProcessMode(excelItemLinearLayout.getEtProcessMode()
					.getText().toString().trim());
			if (mProjects.size() != 0) {
				excelItem.setProject(mProjects.get(excelItemLinearLayout
						.getProjectSize()));
			}
			if (mSizes.size() != 0) {
				excelItem.setSize(mSizes.get(excelItemLinearLayout.getSize()));
			}
			if (mBadnesses.size() != 0) {
				excelItem.setBadness(mBadnesses.get(excelItemLinearLayout
						.getBadnessSize()));
			}
			mExcelItemDatas.put(excelItemLinearLayout.getId(), excelItem);
		}
	}

	/**
	 * 刷新各项内容
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mApplication = (MyApplication) getApplication();
		Log.e("重启", "重启");
		Log.e("onResume", "onResume:"+mExcelItemLinearLayouts.size());
		if (mApplication.isRefreshGroup()) {
			findAllGroups();
			findAllBadness();
			mApplication.setRefreshGroup(false);
		}
		if (mApplication.isRefreshSize()) {
			findAllSize();
			mApplication.setRefreshSize(false);
		}
		if (mApplication.isRefreshProject()) {
			findAllProject();
			mApplication.setRefreshProject(false);
		}

		if (mApplication.isRefreshBadness()) {
			findAllBadness();
			mApplication.setRefreshBadness(false);
		}
	}

	private void findAllBadness() {
		initSpinnerBadness();
		for (ExcelItemLinearLayout excelItemLinearLayout : mExcelItemLinearLayouts) {

			excelItemLinearLayout.getSpBadness().setAdapter(mBadnessAdapter);
			ExcelItem excelItem = mExcelItemDatas.get(excelItemLinearLayout
					.getId());

			int sizeIndexOf = mBadnesses.indexOf(excelItem.getBadness());
			if (sizeIndexOf > -1) {
				excelItemLinearLayout.getSpBadness().setSelection(sizeIndexOf);
			}
		}

	}

	private void findAllGroups() {
		mGroupDao = new GroupDao(this);
		mGroups = mGroupDao.query();
		if (mGroups == null || mGroups.size() == 0) {
			mGroups = new ArrayList<Group>();
		}
		mGroupStrings = new String[mGroups.size()];
		for (int i = 0; i < mGroups.size(); i++) {
			mGroupStrings[i] = new String(mGroups.get(i).getName());
		}
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, mGroupStrings);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinnerGroup.setAdapter(adapter);
	}

	/**
	 * 刷新型号
	 */
	private void findAllSize() {
		initSpinnerSize();
		if (!mApplication.isRefreshProject()) {
			for (ExcelItemLinearLayout excelItemLinearLayout : mExcelItemLinearLayouts) {
				excelItemLinearLayout.getSpSize().setAdapter(mSizeAdapter);

				ExcelItem excelItem = mExcelItemDatas.get(excelItemLinearLayout
						.getId());
				int sizeIndexOf = mSizes.indexOf(excelItem.getSize());
				if (sizeIndexOf > -1) {
					excelItemLinearLayout.getSpSize().setSelection(sizeIndexOf);
				}
			}
		}
	}

	/**
	 * 获取项目表中的数据，并且刷新里列表
	 */
	private void findAllProject() {
		initSpinnerProject();
		Iterator iter = mExcelItemDatas.entrySet().iterator();
		HashMap<Integer, ExcelItem> mTempExcelItemDatas = new HashMap<Integer, ExcelItem>();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			Integer key = (Integer) entry.getKey();
			ExcelItem excelItem = (ExcelItem) entry.getValue();	
			for (Project project : mProjects) {
				if (excelItem.getProject() != null
						&& project.getId() == excelItem.getProject().getId()) {
					mTempExcelItemDatas.put(key, excelItem);
					break;
				}

			}
		}

		mExcelItemDatas = mTempExcelItemDatas;
		mLinearlayoutForm.removeAllViews();
		mExcelItemLinearLayouts.clear();

		Iterator iterator = mExcelItemDatas.entrySet().iterator();
		Log.e("iterator.hasNext()", "iterator.hasNext():" + iterator.hasNext());
		while (iterator.hasNext()) {
			Map.Entry entry = (Map.Entry) iterator.next();
			Integer key = (Integer) entry.getKey();
			ExcelItem excelItem = (ExcelItem) entry.getValue();

			ExcelItemLinearLayout excelItemLinearLayout = new ExcelItemLinearLayout(
					this, MainActivity.this);
			excelItemLinearLayout.setId(key);

			excelItemLinearLayout.getSpProject().setAdapter(mProjectAdapter);
			int projectIndexOf = mProjects.indexOf(excelItem.getProject());
			if (projectIndexOf > -1) {
				excelItemLinearLayout.getSpProject().setSelection(
						projectIndexOf);
			}
			excelItemLinearLayout.getSpSize().setAdapter(mSizeAdapter);
			int sizeIndexOf = mSizes.indexOf(excelItem.getSize());
			if (sizeIndexOf > -1) {
				excelItemLinearLayout.getSpSize().setSelection(sizeIndexOf);
			}

			excelItemLinearLayout.setPhotoFileName(fomatTimeByTime()
					+ key + ".png");
			excelItemLinearLayout.getAsViewCheck().setNum(
					excelItem.getCheckNum());
			excelItemLinearLayout.getAsViewUnqualified().setNum(
					excelItem.getUnqualifiedNum());
			excelItemLinearLayout.getAsViewExamine().setNum(
					excelItem.getExamineNum());
			excelItemLinearLayout.getAsViewNg().setNum(excelItem.getNgNum());
			excelItemLinearLayout.getEtProcessMode().setText(
					excelItem.getProcessMode());

			mExcelItemLinearLayouts.add(excelItemLinearLayout);
			mLinearlayoutForm.addView(excelItemLinearLayout,
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		}

		// for (Project project : mProjects) {
		// ExcelItemLinearLayout excelItemLinearLayout = new
		// ExcelItemLinearLayout(
		// this, MainActivity.this);
		// excelItemLinearLayout.getTvItemDescription().setText(
		// project.getContent());
		// excelItemLinearLayout.setId(project.getId());
		// excelItemLinearLayout.getSpSize().setAdapter(mSizeAdapter);
		// excelItemLinearLayout.setPhotoFileName(tvTime.getText().toString()
		// .trim()
		// + project.getId() + ".png");
		//
		// if (mExcelItemDatas.containsKey(project.getId())) {
		// ExcelItem excelItem = mExcelItemDatas.get(project.getId());
		// excelItemLinearLayout.getAsViewCheck().setNum(
		// excelItem.getCheckNum());
		// excelItemLinearLayout.getAsViewUnqualified().setNum(
		// excelItem.getUnqualifiedNum());
		// excelItemLinearLayout.getAsViewExamine().setNum(
		// excelItem.getExamineNum());
		// excelItemLinearLayout.getAsViewNg()
		// .setNum(excelItem.getNgNum());
		// excelItemLinearLayout.getEtProcessMode().setText(
		// excelItem.getProcessMode());
		// int sizeIndexOf = mSizes.indexOf(excelItem.getSize());
		// if (sizeIndexOf > -1) {
		// excelItemLinearLayout.getSpSize().setSelection(sizeIndexOf);
		// }
		// }
		// mExcelItemLinearLayouts.add(excelItemLinearLayout);
		// mLinearlayoutForm.addView(excelItemLinearLayout,
		// LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		// }
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
			// 测试保存使用数据
			saveExcel();
			break;
		case R.id.tv_add:
			addProject();
			break;
		default:
			break;
		}
	}

	/**
	 * 添加新的项目编辑模块
	 */
	private void addProject() {
		projectNum += 1;
		ExcelItemLinearLayout excelItemLinearLayout = new ExcelItemLinearLayout(
				this, MainActivity.this);

		excelItemLinearLayout.setId(projectNum);
		if(mProjectAdapter!=null){
			excelItemLinearLayout.getSpProject().setAdapter(mProjectAdapter);
		}
		if(mSizeAdapter!=null){
			excelItemLinearLayout.getSpSize().setAdapter(mSizeAdapter);
		}
		if(mBadnessAdapter!=null){
			excelItemLinearLayout.getSpBadness().setAdapter(mBadnessAdapter);
		}
		excelItemLinearLayout.setPhotoFileName(fomatTimeByTime()+ projectNum + ".png");
		
		mExcelItemLinearLayouts.add(excelItemLinearLayout);
		mLinearlayoutForm.addView(excelItemLinearLayout,LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		
		Log.e("mExcelItemLinearLayouts", "mExcelItemLinearLayouts:"+mExcelItemLinearLayouts.size());
	}

	private void saveExcel() {
		if (mProjects.size() == 0) {
			MyToast.showShort(this, "暂无需保存数据!");
			return;
		}
		mProgress.show();
		ExcelDao excelDao = new ExcelDao(this);
		ExcelItemDao excelItemDao = new ExcelItemDao(this);
		SharedPreferences flowId = getSharedPreferences("flowId",Activity.MODE_PRIVATE);
		int mFlowId = flowId.getInt("flowId", 0);
		ExcelSave excelSave = new ExcelSave();
		excelSave.setFlowId(mFlowId);
		excelSave.setTime(creatTime);
		excelSave.setMyGroup(mGroups.get(
				mSpinnerGroup.getSelectedItemPosition()).getName());
		excelSave.setFanHao(etFanhao.getText().toString().trim());
		excelDao.save(excelSave);

		excel = new Excel();
		excel.setFlowId(mFlowId);
		excel.setTime(creatTime);
		excel.setGroup(mGroups.get(mSpinnerGroup.getSelectedItemPosition())
				.getName());
		excel.setFanHao(etFanhao.getText().toString().trim());

		ArrayList<ExcelItem> excelItemsList = new ArrayList<ExcelItem>();

		for (int i = 0; i < mExcelItemLinearLayouts.size(); i++) {
			ExcelItemLinearLayout excelItemLinearLayout = mExcelItemLinearLayouts.get(i);
			
			ExcelItem excelItem = new ExcelItem();
			excelItem.setFlowId(mFlowId);
			excelItem.setProject(mProjects.get(excelItemLinearLayout.getProjectSize()));
			excelItem.setPorjectName(mProjects.get(excelItemLinearLayout.getProjectSize()).getShortName());
			excelItem.setCheckNum(excelItemLinearLayout.getAsViewCheck().getNum());
			excelItem.setUnqualifiedNum(excelItemLinearLayout.getAsViewUnqualified().getNum());
			excelItem.setExamineNum(excelItemLinearLayout.getAsViewExamine().getNum());
			excelItem.setNgNum(excelItemLinearLayout.getAsViewNg().getNum());
			
			if(mBadnesses.size()>0){
				excelItem.setBadness(mBadnesses.get(excelItemLinearLayout.getBadnessSize()));
				excelItem.setSelectBadnessName(mBadnesses.get(excelItemLinearLayout.getBadnessSize()).getName());
			}
			excelItem.setProcessMode(excelItemLinearLayout.getEtProcessMode()
					.getText().toString().trim());
			
			excelItem.setSize(mSizes.get(excelItemLinearLayout.getSize()));
			excelItem.setSizeName(mSizes.get(excelItemLinearLayout.getSize())
					.getName());
			excelItem.setPicturePath(excelItemLinearLayout.getPhotoFileName());
			excelItem.setPicture(excelItemLinearLayout.getPath());
			excelItem.setTime(creatTime);
			excelItem.setMyGroup(mGroups.get(
					mSpinnerGroup.getSelectedItemPosition()).getName());
			excelItem.setFanHao(etFanhao.getText().toString().trim());
			excelItemsList.add(excelItem);
			excelItemDao.save(excelItem);
		}
		excel.setExcelItemsList(excelItemsList);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				ExportExcel.export(MainActivity.this, excel, excelName,handler);
			}
		}).start();
		Editor edit = flowId.edit();
		mFlowId = mFlowId + 1;
		edit.putInt("flowId", mFlowId);
		edit.commit();
	}

	public static String formatTime() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
		return simpleDateFormat.format(date);
	}
	
	public String fomatTimeByTime(){
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd HHmmss");
		return simpleDateFormat.format(date);
	}
}
