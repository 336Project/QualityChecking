package com.ateam.qc.utils;

import java.util.Calendar;

import com.ateam.qc.R;
import com.ateam.qc.widget.DateArrayAdapter;
import com.ateam.qc.widget.DateNumericAdapter;


import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.NumericWheelAdapter;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * 
 * @author 魏天武
 * @version 
 * @create_date 2015-1-5 下午2:07:01
 */
public class AddTimeDialog extends Dialog {
	
	private Context mContext;
	private WheelView mWheelYear;// 年份滚轮
	private WheelView mWheelMonth;// 月份滚轮
	private WheelView mWheelDay;// 日期滚轮
	private static DateNumericAdapter mYearAdapter;
	private WheelView mWheelHour;// 小时滚轮
	private WheelView mWheelMin;// 分钟滚轮
	private Button mBtnSure;
	private TextView mTvTitle;
	private TextView mTvShowFindTime;
	protected OnAddTimeChangeListener dateTemeChangeListener;
	
	private StringBuffer mDateTime;//
	private int mTimeType;
	private int mCurYear;
	private boolean mHasHours=false;
	private String mRbType="";//农历还是新历参数  ， 1是新历  ，0是农历
	
	/***
	 * 时间选择监听器接口
	 */
	public interface OnAddTimeChangeListener{
		/**
		 *
		 *@param year 返回选择的年
		 *@param month 返回选择的月
		 */
		public void onConfirmDatetime(AddTimeDialog dialog,
				String year,String month,String day,String hour,String mins,
				String type,
				TextView text);
	}
	public AddTimeDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public AddTimeDialog(Context context, int theme,OnAddTimeChangeListener onAddTimeChangeListener,int timeType) {
		super(context, theme);
		mTimeType=timeType;
		mContext=context;
		this.dateTemeChangeListener = onAddTimeChangeListener;
	}
	
	public AddTimeDialog(Context context, int theme,OnAddTimeChangeListener onAddTimeChangeListener,
			int timeType,String rbType) {
		super(context, theme);
		mRbType=rbType;
		mTimeType=timeType;
		mContext=context;
		this.dateTemeChangeListener = onAddTimeChangeListener;
	}
	
	public AddTimeDialog(Context context, int theme,OnAddTimeChangeListener dateTemeChangeListener,boolean hasHours) {
		super(context, theme);
		this.mHasHours=hasHours;
		this.dateTemeChangeListener = dateTemeChangeListener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		LayoutParams params=new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		View view=getLayoutInflater().inflate(R.layout.dialog_adddatetime, null);
		this.setContentView(view,params);
		//setContentView(
		initViews();
		if(mTimeType==SysFinal.TIME_ADD_TIME){
//			findTimeView();
		}else if(mTimeType==SysFinal.TIME_FIND_TIME){
			findTimeView();
		}else if(mTimeType==SysFinal.TIME_MONTH_DAY){
			toGetMonthAndDay();
		}
		setupViewsListener();
		setDateTime();
	}
	/***
	 * 初始化控件
	 */
	protected void initViews(){
		mTvShowFindTime=(TextView)findViewById(R.id.tv_showFindTime);
		mTvTitle=(TextView)findViewById(R.id.tv_title);
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
//		mWheelMonth.addChangingListener(listener);
		// year
		mCurYear = calendar.get(Calendar.YEAR);
		int minYear=SysFinal.FIRST_YEAR;
		int maxYear=SysFinal.LAST_YEAR;
		int index=mCurYear-minYear;
		mYearAdapter=new DateNumericAdapter(getContext(),minYear , maxYear, index);
		mWheelYear.setViewAdapter(mYearAdapter);
		mWheelYear.setCurrentItem(index);
		mWheelYear.addChangingListener(listener);
		// day
		updateDays(getContext(), mWheelYear, mWheelMonth, mWheelDay);
		mWheelDay.setCurrentItem(calendar.get(Calendar.DAY_OF_MONTH) - 1);
		//hour  min
		mWheelHour.setCurrentItem(calendar.get(Calendar.HOUR_OF_DAY));
		mWheelMin.setCurrentItem(calendar.get(Calendar.MINUTE));
		/***
		 * end 日期控件相关
		 */
	}
	
	/**
	 * 更改为显示小时分钟
	 */
	@SuppressWarnings("deprecation")
	private void toGetMonthAndDay(){
		mWheelYear.setVisibility(View.GONE);
		mWheelMonth.setVisibility(View.VISIBLE);
		mWheelDay.setVisibility(View.VISIBLE);
		mWheelHour.setVisibility(View.GONE);
		mWheelMin.setVisibility(View.GONE);
	}
	
	/**
	 * 更改为显示小时分钟
	 */
	@SuppressWarnings("deprecation")
	private void findTimeView(){
//		mRgXingNong.setOrientation(RadioGroup.VERTICAL);
		mWheelYear.setVisibility(View.VISIBLE);
		mWheelMonth.setVisibility(View.VISIBLE);
		mWheelDay.setVisibility(View.VISIBLE);
		mWheelHour.setVisibility(View.GONE);
		mWheelMin.setVisibility(View.GONE);
		mTvShowFindTime.setVisibility(View.VISIBLE);
		mTvTitle.setText("选择查询时间");
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
					datetime = intYear + "-" + fixZoer(intMonth)
							+ "-" + fixZoer(intDay); 
				}else{
					datetime = intYear + "-" + fixZoer(intMonth)
							+ "-" + fixZoer(intDay)
						+ " "
						+ fixZoer(intHour) + ":"
						+ fixZoer(intMins) + ":00";
				}
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
		day.setViewAdapter(new DateNumericAdapter(context, 1, 31, calendar.get(Calendar.DAY_OF_MONTH) - 1));
		int curDay = Math.min(maxDays, day.getCurrentItem() + 1);
		day.setCurrentItem(curDay - 1, true);
	}
	
}
