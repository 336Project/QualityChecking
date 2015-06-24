package com.ateam.qc.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.ateam.qc.R;
import com.ateam.qc.dao.ExcelItemDao;
import com.ateam.qc.model.ExcelItem;
import com.ateam.qc.utils.ExportExcelByDate;
import com.ateam.qc.utils.MyToast;
import com.ateam.qc.widget.DateTimeDialog;
import com.ateam.qc.widget.DateTimeDialog.OnDateTimeChangeListener;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class ExportByTimeActivity extends Activity implements OnClickListener{

    private EditText mEtBeginTime;
	private EditText mEtEndTime;
	private Button mBtnExport;
	
	private ArrayList<ExcelItem> mFindDatas;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_export_by_time);
        initView();
    }


    private void initView(){
    	mEtBeginTime=(EditText)findViewById(R.id.et_beginTime);
    	mEtBeginTime.setOnClickListener(this);
    	mEtBeginTime.setFocusable(false);
    	mEtBeginTime.setFocusableInTouchMode(false);
    	mEtEndTime=(EditText)findViewById(R.id.et_endTime);
    	mEtEndTime.setOnClickListener(this);
    	mEtEndTime.setFocusable(false);
    	mEtEndTime.setFocusableInTouchMode(false);
    	mBtnExport=(Button)findViewById(R.id.btn_export);
    	mBtnExport.setOnClickListener(this);
    }


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_export:
			exportByTime();
			break;
		case R.id.et_beginTime:
			showDateListener(mEtBeginTime);
			break;
		case R.id.et_endTime:
			showDateListener(mEtEndTime);
			break;

		default:
			break;
		}
	}
	
	private void exportByTime(){
		ExcelItemDao mDao=new ExcelItemDao(this);
		if(mEtBeginTime.getText().toString().equals("")||mEtEndTime.getText().toString().equals("")){
			MyToast.showShort(this, "请输入开始结束时间");
		}else{
			mFindDatas=(ArrayList<ExcelItem>) mDao.findByTime(mEtBeginTime.getText().toString()+"000000", mEtEndTime.getText().toString()+"235959");
			if(mFindDatas.size()==0){
				MyToast.showShort(this, "该时间段没有数据");
			}else{
				ExportExcelByDate.export(this, mFindDatas, formatTime()+"汇总表");
			}
			finish();
		}
	}
	
	/**
	 * 点击日历按钮后进行的监听操作
	 * 
	 * @param showDate1
	 *            :显示日历的按钮控件对象
	 * @param editText
	 *            :进行显示选中时间的 EditText对象
	 */
	private void showDateListener(final EditText editText) {
		Dialog dialog = new DateTimeDialog(
				ExportByTimeActivity.this,
				R.style.my_dialog_theme,
				new OnDateTimeChangeListener() {

					@Override
					public void onConfirmDatetime(String datetime) {
						editText.setText(datetime);
					}
				});
		dialog.show();
	}
	
	public String formatTime() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyyMMddHHmm");
		return simpleDateFormat.format(date);
	}
}
