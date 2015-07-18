package com.ateam.qc.activity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.ateam.qc.R;
import com.ateam.qc.R.id;
import com.ateam.qc.R.layout;
import com.ateam.qc.adapter.ContentItemAdapter;
import com.ateam.qc.application.MyApplication;
import com.ateam.qc.dao.ExcelDao;
import com.ateam.qc.dao.ExcelItemDao;
import com.ateam.qc.dao.GroupDao;
import com.ateam.qc.dao.ProjectDao;
import com.ateam.qc.dao.SizeDao;
import com.ateam.qc.model.Excel;
import com.ateam.qc.model.ExcelItem;
import com.ateam.qc.model.ExcelSave;
import com.ateam.qc.model.Group;
import com.ateam.qc.model.Project;
import com.ateam.qc.model.Size;
import com.ateam.qc.utils.ExportExcel;
import com.ateam.qc.utils.MyToast;
import com.ateam.qc.widget.ExcelItemLinearLayout;
import com.team.hbase.utils.FileUtil;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	private DrawerLayout drawerLayout;
	private ArrayAdapter<String> adapter;
	private ListView lvContentBody;
	private View viewHead;
	private GroupDao mGroupDao;
	private List<Group> mGroups;

	private ArrayList<ExcelItem> mExcelItems = new ArrayList<ExcelItem>();
	private ProjectDao mProjectDao;
	private List<Project> mProjects;
	private String[] mGroupStrings;
	private Spinner mSpinnerGroup;
	private LinearLayout mLinearlayoutForm;
	private HashMap<Integer, ExcelItem> mExcelItemDatas = new HashMap<Integer, ExcelItem>();
	private ArrayList<ExcelItemLinearLayout> mExcelItemLinearLayouts = new ArrayList<ExcelItemLinearLayout>();
	private SizeDao mSizeDao;
	private List<Size> mSizes;
	private String[] mSizeStrings;
	private ArrayAdapter<String> mSizeAdapter;
	private MyApplication mApplication;
	private TextView tvTime;
	private EditText etFanhao;
	public static final int REQUEST_CODE_CAMERA=1002;
	private String creatTime;
	private String excelName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
	}

	private void initView() {
		TextView tvTitlename = (TextView) findViewById(R.id.tv_titlename);
		tvTitlename.setText("表格录入");
		RelativeLayout rlRight = (RelativeLayout) findViewById(R.id.rl_right);
		rlRight.setVisibility(View.INVISIBLE);
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
		excelName = "表"+creatTime;
		tvTime.setText(excelName);
		etFanhao = (EditText) findViewById(R.id.et_fanhao);
		initSpinnerGroup();
	}

	/**
	 * 表体
	 */
	private void initExcelBody() {
		initSpinnerSize();
		mProjectDao = new ProjectDao(this);
		mProjects = mProjectDao.query();
		for (Project project : mProjects) {
			ExcelItemLinearLayout excelItemLinearLayout = new ExcelItemLinearLayout(
					this,MainActivity.this);
			excelItemLinearLayout.getTvItemDescription().setText(
					project.getContent());
			excelItemLinearLayout.setId(project.getId());
			excelItemLinearLayout.getSpSize().setAdapter(mSizeAdapter);
			excelItemLinearLayout.setPhotoFileName(tvTime.getText().toString().trim()+project.getId()+".png");
//			final Project finalProject =project;
//			excelItemLinearLayout.getTvTakePhoto().setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//					FileUtil.getInstance().createSDDir(SAVED_IMAGE_DIR_PATH);
//					String fileName = "h2h3" + ".jpg";
//					File file = FileUtil.getInstance().createFileInSDCard(
//							SAVED_IMAGE_DIR_PATH, fileName);
//					Uri fileUri = Uri.fromFile(file);
//					intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
//					startActivityForResult(intent, finalProject.getId());
//				}
//			});
			
			mExcelItemLinearLayouts.add(excelItemLinearLayout);	
			mLinearlayoutForm.addView(excelItemLinearLayout,
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Uri uri=null;
		if (resultCode == RESULT_OK){
				if (data != null && data.getData() != null) {
					uri = data.getData();
					Log.e("1", "1");
				}
				if (uri == null) {
					Log.e("2", "2");
//					uri = fileUri;
				}
		}
		if(uri!=null){
			Log.e("3", "3");
		}else{
			//Toast.makeText(this, "获取图片失败，请选择一张图片", Toast.LENGTH_SHORT).show();
		}
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
	}

	@Override
	protected void onPause() {
		super.onPause();

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
			
			excelItem.setSize(mSizes.get(excelItemLinearLayout.getSize()));
			
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
		if (mApplication.isRefreshGroup()) {
			findAllGroups();
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
				
				ExcelItem excelItem = mExcelItemDatas.get(excelItemLinearLayout.getId());
				int sizeIndexOf=mSizes.indexOf(excelItem.getSize());
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
		mProjectDao = new ProjectDao(this);
		mProjects = mProjectDao.query();
		mLinearlayoutForm.removeAllViews();
		mExcelItemLinearLayouts.clear();
		for (Project project : mProjects) {
			ExcelItemLinearLayout excelItemLinearLayout = new ExcelItemLinearLayout(
					this,MainActivity.this);
			excelItemLinearLayout.getTvItemDescription().setText(
					project.getContent());
			excelItemLinearLayout.setId(project.getId());
			excelItemLinearLayout.getSpSize().setAdapter(mSizeAdapter);
			excelItemLinearLayout.setPhotoFileName(tvTime.getText().toString().trim()+project.getId()+".png");
			
			if (mExcelItemDatas.containsKey(project.getId())) {
				ExcelItem excelItem = mExcelItemDatas.get(project.getId());
				excelItemLinearLayout.getAsViewCheck().setNum(
						excelItem.getCheckNum());
				excelItemLinearLayout.getAsViewUnqualified().setNum(
						excelItem.getUnqualifiedNum());
				excelItemLinearLayout.getAsViewExamine().setNum(
						excelItem.getExamineNum());
				excelItemLinearLayout.getAsViewNg()
						.setNum(excelItem.getNgNum());
				excelItemLinearLayout.getEtProcessMode().setText(
						excelItem.getProcessMode());
				int sizeIndexOf = mSizes.indexOf(excelItem
						.getSize());
				if (sizeIndexOf > -1) {
					excelItemLinearLayout.getSpSize().setSelection(sizeIndexOf);
				}
			}
			mExcelItemLinearLayouts.add(excelItemLinearLayout);
			mLinearlayoutForm.addView(excelItemLinearLayout,
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		}
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
		default:
			break;
		}
	}
	private void saveExcel() {
		if(mProjects.size()==0){
			MyToast.showShort(this, "暂无需保存数据!");
			return;
		}
		ExcelDao excelDao=new ExcelDao(this);
		ExcelItemDao excelItemDao=new ExcelItemDao(this);
		
		SharedPreferences flowId=getSharedPreferences("flowId", Activity.MODE_PRIVATE);
		int mFlowId=flowId.getInt("flowId", 0);
		ExcelSave excelSave = new ExcelSave();
		excelSave.setFlowId(mFlowId);
		excelSave.setTime(creatTime);
		excelSave.setMyGroup(mGroups.get(mSpinnerGroup.getSelectedItemPosition()).getName());
		excelSave.setFanHao(etFanhao.getText().toString().trim());
		excelDao.save(excelSave);
		
		Excel excel = new Excel();
		excel.setFlowId(mFlowId);
		excel.setTime(creatTime);
		excel.setGroup(mGroups.get(mSpinnerGroup.getSelectedItemPosition()).getName());
		excel.setFanHao(etFanhao.getText().toString().trim());
		
		ArrayList<ExcelItem> excelItemsList = new ArrayList<ExcelItem>();
		
		for (int i = 0; i < mExcelItemLinearLayouts.size(); i++) {
			ExcelItemLinearLayout excelItemLinearLayout = mExcelItemLinearLayouts.get(i);
			ExcelItem excelItem = new ExcelItem();
			excelItem.setFlowId(mFlowId);
			excelItem.setProject(mProjects.get(i));
			excelItem.setPorjectName(mProjects.get(i).getContent());
			excelItem.setCheckNum(excelItemLinearLayout.getAsViewCheck()
					.getNum());
			excelItem.setUnqualifiedNum(excelItemLinearLayout
					.getAsViewUnqualified().getNum());
			excelItem.setExamineNum(excelItemLinearLayout.getAsViewExamine()
					.getNum());
			excelItem.setNgNum(excelItemLinearLayout.getAsViewNg().getNum());
			excelItem.setProcessMode(excelItemLinearLayout.getEtProcessMode()
					.getText().toString().trim());
			excelItem.setSize(mSizes.get(excelItemLinearLayout.getSize()));
			excelItem.setSizeName(mSizes.get(excelItemLinearLayout.getSize()).getName());
			excelItem.setPicturePath(excelItemLinearLayout.getPhotoFileName());
			excelItem.setPicture(excelItemLinearLayout.getPath());
			excelItem.setTime(creatTime);
			excelItem.setMyGroup(mGroups.get(mSpinnerGroup.getSelectedItemPosition()).getName());
			excelItem.setFanHao(etFanhao.getText().toString().trim());
			excelItemsList.add(excelItem);
			excelItemDao.save(excelItem);
		}
		excel.setExcelItemsList(excelItemsList);
		ExportExcel.export(this, excel,excelName);
		Editor edit = flowId.edit();
		mFlowId=mFlowId+1;
		edit.putInt("flowId", mFlowId);
		edit.commit();
	}

	public String formatTime() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyyMMdd");
		return simpleDateFormat.format(date);
	}
}
