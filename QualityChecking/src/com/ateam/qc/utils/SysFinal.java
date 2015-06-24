package com.ateam.qc.utils;

/**
 * 一些静态变量
 * @author 魏天武
 * @version 
 * @create_date 2014-12-24 上午9:29:30
 */
public class SysFinal {
	
	//日历参数
	public static String TYPE_XINLI="1";//标识新历
	public static String TYPE_NONGLI="0";//标识农历
	
	public static int FIRST_YEAR=1901;//能查询到最早的年
	public static int FIRST_MONTH=3;//能查询到的最早的月
	public static int LAST_YEAR=2099;//能查询到的最晚的年
	public static int LAST_MONTH=12;//能查询到的最晚的月
	
	//菜单参数
	public static int FIND_MENERY=0;//查询保存的记录
	public static int DELETE_RILI=1;//删除保存的日历缓存信息
	
	//dialog使用参数
	public static int TIME_HOUR_MIN=0;//显示的时间选择器是小时分钟
	public static int TIME_YEAR_MONTH=1;//显示的时间选择器是年月
	public static int TIME_ADD_TIME=2;//添加时间
	public static int TIME_FIND_TIME=3;//查询时间
	public static int TIME_MONTH_DAY=4;//显示选择器的月日
}
