package com.ateam.qc.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.ateam.qc.R;
import com.ateam.qc.image.ImagePagerActivity;
import com.ateam.qc.model.Excel;
import com.ateam.qc.model.ExcelItem;
import com.ateam.qc.model.ExcelSave;
import com.team.hbase.adapter.HBaseAdapter;
import com.team.hbase.adapter.ViewHolder;

/**
 * 历史数据列表
 * @author 魏天武
 * @version 
 * @create_date 2015-6-20上午1:16:15
 */
public class HistoryDetailAdapter extends HBaseAdapter<ExcelItem> {

	private Context context;
	public HistoryDetailAdapter(Context c, List<ExcelItem> datas) {
		super(c, datas);
		// TODO Auto-generated constructor stub
		context=c;
	}

	@Override
	public void convert(ViewHolder holder, final ExcelItem bean, final int position) {
		// TODO Auto-generated method stub
		((TextView)holder.getView(R.id.tv_objectContent)).setText("项目内容:"+bean.getPorjectName());
		((TextView)holder.getView(R.id.tv_type)).setText("型号:"+bean.getSizeName());
		((TextView)holder.getView(R.id.tv_selectNum)).setText("检查数量:"+bean.getCheckNum());
		((TextView)holder.getView(R.id.tv_checkNum)).setText("查核次数:"+bean.getExamineNum());
		((TextView)holder.getView(R.id.tv_noPassNum)).setText("不合格数量:"+bean.getUnqualifiedNum());
		((TextView)holder.getView(R.id.tv_NGNum)).setText("NG数:"+bean.getNgNum());
		((TextView)holder.getView(R.id.tv_dealWay)).setText("处理方式:"+bean.getProcessMode());
		holder.getView(R.id.btn_lookPhoto).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, ImagePagerActivity.class);
				Log.e("", "bean.getPicture()"+bean.getPicture());
				String[] urls = new String[] {"file://"+bean.getPicture()};
				intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
				intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, 0);
				context.startActivity(intent);
			}
		});
	}

	@Override
	public int getResId() {
		// TODO Auto-generated method stub
		return R.layout.item_history_detail_list;
	}
	
}
