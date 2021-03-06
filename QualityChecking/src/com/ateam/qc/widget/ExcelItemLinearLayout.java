package com.ateam.qc.widget;

import java.io.File;
import java.net.URI;

import com.ateam.qc.R;
import com.ateam.qc.constant.Constant;
import com.ateam.qc.image.ImagePagerActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.team.hbase.utils.FileUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class ExcelItemLinearLayout extends LinearLayout {
	private CicleAddAndSubView asViewCheck;
	private CicleAddAndSubView asViewUnqualified;
	private CicleAddAndSubView asViewExamine;
	private CicleAddAndSubView asViewNg;
	private EditText etProcessMode;
	/**
	 * Spinner
	 */
	private Spinner spSize;
	private Spinner spProject;
	private Spinner spBadness;
	/**
	 * Spinner适配器的position
	 */
	private int size;
	private int projectSize;
	private int badnessSize;

	public static final int REQUEST_CODE_CAMERA = 1002;
	private Activity activity;
	private TextView tvTakePhoto;
	private TextView tvLookPhoto;
	private String photoFileName;
	private Context context;
	private Uri fileUri;
	private String path;
	

	public TextView getTvTakePhoto() {
		return tvTakePhoto;
	}

	public void setTvTakePhoto(TextView tvTakePhoto) {
		this.tvTakePhoto = tvTakePhoto;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
	public int getProjectSize() {
		return projectSize;
	}

	public void setProjectSize(int projectSize) {
		this.projectSize = projectSize;
	}
	public Spinner getSpSize() {
		return spSize;
	}
	public void setSpSize(Spinner spSize) {
		this.spSize = spSize;
	}
	public int getBadnessSize() {
		return badnessSize;
	}
	public Spinner getSpProject() {
		return spProject;
	}
	public void setSpProject(Spinner spProject) {
		this.spProject = spProject;
	}
	public Spinner getSpBadness() {
		return spBadness;
	}
	public void setSpBadness(Spinner spBadness) {
		this.spBadness = spBadness;
	}
	public EditText getEtProcessMode() {
		return etProcessMode;
	}
	public void setEtProcessMode(EditText etProcessMode) {
		this.etProcessMode = etProcessMode;
	}

	public CicleAddAndSubView getAsViewExamine() {
		return asViewExamine;
	}

	public void setAsViewExamine(CicleAddAndSubView asViewExamine) {
		this.asViewExamine = asViewExamine;
	}

	public CicleAddAndSubView getAsViewNg() {
		return asViewNg;
	}

	public void setAsViewNg(CicleAddAndSubView asViewNg) {
		this.asViewNg = asViewNg;
	}

	public CicleAddAndSubView getAsViewUnqualified() {
		return asViewUnqualified;
	}

	public void setAsViewUnqualified(CicleAddAndSubView asViewUnqualified) {
		this.asViewUnqualified = asViewUnqualified;
	}

	public CicleAddAndSubView getAsViewCheck() {
		return asViewCheck;
	}

	public void setAsViewCheck(CicleAddAndSubView asViewCheck) {
		this.asViewCheck = asViewCheck;
	}
	public ExcelItemLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ExcelItemLinearLayout(Context context, final Activity activity) {
		super(context);
		this.context = context;
		this.activity = activity;
		init();
	}

	private void init() {
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		LayoutParams paramsChild = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		paramsChild.weight = 1.0f;
		paramsChild.setMargins(dip2px(getContext(), 8),
				dip2px(getContext(), 8), dip2px(getContext(), 8),
				dip2px(getContext(), 8));
		setLayoutParams(params);
		LayoutInflater layoutInflater = LayoutInflater.from(getContext());
		View view = layoutInflater.inflate(R.layout.content_body_item, null);
		asViewCheck = (CicleAddAndSubView) view.findViewById(R.id.asView_check);
		asViewUnqualified = (CicleAddAndSubView) view.findViewById(R.id.asView_unqualified);
		asViewExamine = (CicleAddAndSubView) view.findViewById(R.id.asView_examine);
		asViewNg = (CicleAddAndSubView) view.findViewById(R.id.asView_ng);
		etProcessMode = (EditText) view.findViewById(R.id.et_process_mode);
		
		spProject = (Spinner) view.findViewById(R.id.sp_project);
		spProject.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				setProjectSize(position);
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		spSize = (Spinner) view.findViewById(R.id.sp_size);
		spSize.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				setSize(arg2);
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		// 不良状况
		spBadness = (Spinner) view.findViewById(R.id.sp_badness);
		spBadness.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				setBadnessSize(arg2);
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		
		
		tvTakePhoto = (TextView) view.findViewById(R.id.tv_take_photo);
		tvTakePhoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				FileUtil.getInstance().createSDDir(Constant.SAVED_IMAGE_DIR_PATH);
				String fileName = getPhotoFileName();
				File file = FileUtil.getInstance().createFileInSDCard(
						Constant.SAVED_IMAGE_DIR_PATH, fileName);
				fileUri = Uri.fromFile(file);
				path = fileUri.getPath();
				Log.e("path", "path:"+path);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
				activity.startActivityForResult(intent, REQUEST_CODE_CAMERA);
			}
		});
		tvLookPhoto = (TextView) view.findViewById(R.id.tv_look_photo);
		tvLookPhoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, ImagePagerActivity.class);
				String[] urls = new String[] {"file://"+path};
				intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
				intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, 0);
				activity.startActivity(intent);
			}
		});
		addView(view, paramsChild);
	}

	protected void setBadnessSize(int badnessSize) {
		this.badnessSize=badnessSize;
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public String getPhotoFileName() {
		return photoFileName;
	}

	public void setPhotoFileName(String photoFileName) {
		this.photoFileName = photoFileName;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
}
