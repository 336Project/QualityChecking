package com.ateam.qc.widget;

import java.util.Calendar;

import com.ateam.qc.R;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.NumericWheelAdapter;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;

public class DateTimeDialog extends Dialog {
	
	private WheelView mWheelYear;// 年份滚轮
	private WheelView mWheelMonth;// 月份滚轮
	private WheelView mWheelDay;// 日期滚轮
	private static DateNumericAdapter mYearAdapter;
	private WheelView mWheelHour;// 小时滚轮
	private WheelView mWheelMin;// 分钟滚轮
	private int mCurYear;
	private boolean mHasHours=false;
	private Button mBtnSure;
	
	private StringBuffer mDateTime;
	
	protected OnDateTimeChangeListener dateTemeChangeListener;
	/***
	 * 时间选择监听器接口
	 */
	public interface OnDateTimeChangeListener{
		/***
		 * 确认时间监听回调函数
		 * @param datetime
		 */
		public void onConfirmDatetime(String datetime);
	}
	public DateTimeDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public DateTimeDialog(Context context, int theme,OnDateTimeChangeListener dateTemeChangeListener) {
		super(context, theme);
		this.dateTemeChangeListener = dateTemeChangeListener;
	}
	
	public DateTimeDialog(Context context, int theme,OnDateTimeChangeListener dateTemeChangeListener,boolean hasHours) {
		super(context, theme);
		this.mHasHours=hasHours;
		this.dateTemeChangeListener = dateTemeChangeListener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		LayoutParams params=new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		View view=getLayoutInflater().inflate(R.layout.dialog_datetime, null);
		this.setContentView(view,params);
		//setContentView(
		initViews();
		setupViewsListener();
		setDateTime();
	}
	/***
	 * 初始化控件
	 */
	protected void initViews(){
		mBtnSure = (Button) findViewById(R.id.btnSure);
		Calendar calendar = Calendar.getInstance();
		mWheelYear = (WheelView) findViewById(R.id.year);
		mWheelMonth = (WheelView) findViewById(R.id.month);
		mWheelDay = (WheelView) findViewById(R.id.day);
		mWheelHour = (WheelView) findViewById(R.id.hour);
		mWheelHour.setViewAdapter(new NumericWheelAdapter(getContext(), 0, 23));
		mWheelHour.setCyclic(true);
		mWheelMin = (WheelView) findViewById(R.id.mins);
		mWheelMin.setViewAdapter(new NumericWheelAdapter(getContext(), 0, 59, "%02d"));
		mWheelMin.setCyclic(true);
		if(mHasHours){
			mWheelHour.setVisibility(View.VISIBLE);
			mWheelMin.setVisibility(View.VISIBLE);
		}
		OnWheelChangedListener listener = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				if ((wheel.getId() == R.id.year || wheel.getId() == R.id.month || wheel.getId() == R.id.day)) {
					updateDays(getContext(), mWheelYear, mWheelMonth, mWheelDay);
				}
			}
		};
		// month
		int curMonth = calendar.get(Calendar.MONTH);
		String months[] = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" };
		mWheelMonth.setViewAdapter(new DateArrayAdapter(getContext(), months, curMonth));
		mWheelMonth.setCurrentItem(curMonth);
		mWheelMonth.addChangingListener(listener);
		// year
		mCurYear = calendar.get(Calendar.YEAR);
		int minYear=mCurYear-5;
		int maxYear=mCurYear+1;
		int index=mCurYear-minYear;
		mYearAdapter=new DateNumericAdapter(getContext(),minYear , maxYear, index);
		mWheelYear.setViewAdapter(mYearAdapter);
		mWheelYear.setCurrentItem(index);
		mWheelYear.addChangingListener(listener);
		// day
		updateDays(getContext(), mWheelYear, mWheelMonth, mWheelDay);
		mWheelDay.setCurrentItem(calendar.get(Calendar.DAY_OF_MONTH) - 1);
		/***
		 * end 日期控件相关
		 */
	}
	/***
	 * 设置监听器
	 */
	private void setupViewsListener(){
		mBtnSure.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String intYear =  mYearAdapter.getItemText(mWheelYear.getCurrentItem()).toString();//mWheelYear.getCurrentItem() + mCurYear;
				int intMonth = mWheelMonth.getCurrentItem() + 1;
				int intDay = mWheelDay.getCurrentItem() + 1;
				int intHour = mWheelHour.getCurrentItem();
				int intMins = mWheelMin.getCurrentItem();
				String datetime="";
				if(!mHasHours){
					datetime = intYear+ fixZoer(intMonth)+ fixZoer(intDay); 
				}else{
					datetime = intYear + "-" + fixZoer(intMonth)
							+ "-" + fixZoer(intDay)
						+ " "
						+ fixZoer(intHour) + ":"
						+ fixZoer(intMins) + ":00";
				}
				dateTemeChangeListener.onConfirmDatetime(datetime);
				DateTimeDialog.this.dismiss();
			}
		});
	}
	/***
	 * 设置时间
	 */
	private void setDateTime(){
		if(mDateTime==null){
			mDateTime = new StringBuffer();
		}
		mDateTime.setLength(0);
	}
	/***
	 * 数字转为字符串，小于10的数字前补0
	 * 
	 * @param num
	 *            要转换的数字
	 * @return 转换后的字符串
	 */
	public String fixZoer(int num) {
		return "" + (num < 10 ? ("0" + num) : num);
	}
	
	public static void updateDays(Context context,WheelView year, WheelView month, WheelView day) {
		Calendar calendar = Calendar.getInstance();
		int currY=Integer.parseInt(mYearAdapter.getItemText(year.getCurrentItem()).toString());
		calendar.set(Calendar.YEAR, currY);
		calendar.set(Calendar.MONTH, month.getCurrentItem());

		int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		day.setViewAdapter(new DateNumericAdapter(context, 1, maxDays, calendar.get(Calendar.DAY_OF_MONTH) - 1));
		int curDay = Math.min(maxDays, day.getCurrentItem() + 1);
		day.setCurrentItem(curDay - 1, true);
	}
}
