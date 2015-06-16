package com.ateam.qc;

import com.ateam.qc.fragment.MenuLeftFragment;
import com.ateam.qc.fragment.MenuLeftFragment.MenuDelegate;
import com.ateam.qc.fragment.HistoryFragment;
import com.ateam.qc.fragment.SettingFragment;
import com.team.hbase.activity.HBaseActivity;
import com.team.hbase.utils.AppManager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;

/**
 * 主界面
 * @author Helen
 * 2015-6-16上午9:41:45
 */
public class MainActivity extends HBaseActivity implements MenuDelegate{
	private DrawerLayout mDrawerLayout;
	private FragmentManager mFragmentManager;
	private Fragment mCurrFragment;//当前片段
	private SettingFragment mSettingFragment;
	private HistoryFragment mHistoryFragment;
	
	private long mCurrTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContentView(R.layout.activity_main);
        getLeftIcon().setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				switchDrawer();
			}
		});
        initView();
    }
    
	private void initView() {
		mDrawerLayout=(DrawerLayout) findViewById(R.id.drawer_layout);
		mFragmentManager=getSupportFragmentManager();
		MenuLeftFragment menuFtagment=(MenuLeftFragment) mFragmentManager.findFragmentById(R.id.fragment_left_menu);
		if(menuFtagment!=null){
			menuFtagment.setDelegate(this);
		}
		mSettingFragment=new SettingFragment();
		mFragmentManager.beginTransaction().add(R.id.frame_content, mSettingFragment,"Setting").commit();
		mCurrFragment=mSettingFragment;
		setActionBarTitle("设置");
	}
	/**
	 * 打开菜单
	 */
	private void openMenu(){
		if(mDrawerLayout!=null){
			mDrawerLayout.openDrawer(Gravity.LEFT);
		}
	}
	/**
	 * 关闭菜单
	 */
	private void closeMenu(){
		if(mDrawerLayout!=null){
			mDrawerLayout.closeDrawer(Gravity.LEFT);
		}
	}
	/**
	 * 菜单开关转换
	 */
	private void switchDrawer(){
		if(mDrawerLayout!=null){
			if(mDrawerLayout.isDrawerOpen(Gravity.LEFT)){
				closeMenu();
			}else{
				openMenu();
			}
		}
	}
	/**
	 * 片段切换
	 * @param fragment
	 */
	private void switchContent(Fragment fragment){
		if(fragment!=mCurrFragment){
			if(fragment.isAdded()){
				mFragmentManager.beginTransaction().hide(mCurrFragment).show(fragment).commit();
			}else{
				mFragmentManager.beginTransaction().hide(mCurrFragment).add(R.id.frame_content, fragment).commit();
			}
			mCurrFragment=fragment;
		}
		closeMenu();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_MENU){
			switchDrawer();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void onBackPressed() {
		if(System.currentTimeMillis()-mCurrTime>2000){
			mCurrTime=System.currentTimeMillis();
			showMsg(MainActivity.this, "再按一次退出程序");
		}else{
			AppManager.getInstance().ExitApp();
		}
	}
	@Override
	public void openSetting() {
		if(mSettingFragment==null){
			mSettingFragment=new SettingFragment();
		}
		setActionBarTitle("设置");
		switchContent(mSettingFragment);
	}
	
	@Override
	public void opentHistory() {
		if(mHistoryFragment==null){
			mHistoryFragment=new HistoryFragment();
		}
		setActionBarTitle("历史");
		switchContent(mHistoryFragment);
	}
}
