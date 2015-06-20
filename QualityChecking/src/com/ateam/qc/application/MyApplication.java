package com.ateam.qc.application;

import com.ateam.qc.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.app.Application;

public class MyApplication extends Application{
	private boolean refreshGroup;
	private boolean refreshProject;
	private boolean refreshSize;
	
	public boolean isRefreshGroup() {
		return refreshGroup;
	}
	public void setRefreshGroup(boolean refreshGroup) {
		this.refreshGroup = refreshGroup;
	}
	public boolean isRefreshProject() {
		return refreshProject;
	}
	public void setRefreshProject(boolean refreshProject) {
		this.refreshProject = refreshProject;
	}
	public boolean isRefreshSize() {
		return refreshSize;
	}
	public void setRefreshSize(boolean refreshSize) {
		this.refreshSize = refreshSize;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		DisplayImageOptions defaultOptions = new DisplayImageOptions
				.Builder()
				.showImageForEmptyUri(R.drawable.empty_photo) 
				.showImageOnFail(R.drawable.empty_photo) 
				.cacheInMemory(true)
				.cacheOnDisc(true)
				.build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration
				.Builder(getApplicationContext())
				.defaultDisplayImageOptions(defaultOptions)
				.discCacheSize(50 * 1024 * 1024)
				.discCacheFileCount(100)
				.writeDebugLogs()
				.build();
		ImageLoader.getInstance().init(config);
	}
}
