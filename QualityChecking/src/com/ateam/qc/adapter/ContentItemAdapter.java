package com.ateam.qc.adapter;

import com.ateam.qc.R;
import com.ateam.qc.widget.CicleAddAndSubView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ContentItemAdapter extends BaseAdapter{
	
	private Context mContext;

	public ContentItemAdapter(Context mContext){
		this.mContext = mContext;
	}
	@Override
	public int getCount() {
		return 5;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View inflate=null;
		Viewholder viewholder=null;
		if(convertView==null){
			LayoutInflater layoutInflater = LayoutInflater.from(mContext);
			inflate = layoutInflater.inflate(R.layout.content_body_item, null);
			TextView tvItemDescription = (TextView) inflate.findViewById(R.id.tv_item_description);
			CicleAddAndSubView asViewCheck = (CicleAddAndSubView) inflate.findViewById(R.id.asViewCheck);
			viewholder=new Viewholder();
			viewholder.setTvItemDescriptionTextView(tvItemDescription);
			viewholder.setAsViewCheck(asViewCheck);
			inflate.setTag(viewholder);
		}
		else{
			inflate=convertView;
			viewholder = (Viewholder) inflate.getTag();
		}
		viewholder.getTvItemDescriptionTextView().setText("飞翔荷兰人");
		viewholder.getAsViewCheck().setNum(5);
		return inflate;
	}
	class Viewholder{
		public TextView tvItemDescriptionTextView;
		public CicleAddAndSubView asViewCheck;

		public CicleAddAndSubView getAsViewCheck() {
			return asViewCheck;
		}

		public void setAsViewCheck(CicleAddAndSubView asViewCheck) {
			this.asViewCheck = asViewCheck;
		}

		public TextView getTvItemDescriptionTextView() {
			return tvItemDescriptionTextView;
		}

		public void setTvItemDescriptionTextView(TextView tvItemDescriptionTextView) {
			this.tvItemDescriptionTextView = tvItemDescriptionTextView;
		}
	}
}
