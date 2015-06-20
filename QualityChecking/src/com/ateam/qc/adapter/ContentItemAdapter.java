package com.ateam.qc.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.ateam.qc.R;
import com.ateam.qc.model.ExcelItem;
import com.ateam.qc.widget.CicleAddAndSubView;
import com.ateam.qc.widget.CicleAddAndSubView.OnNumChangeListener;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

public class ContentItemAdapter extends BaseAdapter{
	
	private Context mContext;
	private ArrayList<ExcelItem> excelItems;
	private HashMap<Integer, String> contentString =new HashMap<Integer, String>();

	public ContentItemAdapter(Context mContext,ArrayList<ExcelItem> excelItems){
		this.mContext = mContext;
		this.excelItems = excelItems;
	}
	@Override
	public int getCount() {
		return excelItems.size();
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
		final ExcelItem excelItem = excelItems.get(position);
		if(convertView==null){
			LayoutInflater layoutInflater = LayoutInflater.from(mContext);
			inflate = layoutInflater.inflate(R.layout.content_body_item, null);
			TextView tvItemDescription = (TextView) inflate.findViewById(R.id.tv_item_description);
			CicleAddAndSubView asViewCheck = (CicleAddAndSubView) inflate.findViewById(R.id.asView_check);
			CicleAddAndSubView asViewUnqualified = (CicleAddAndSubView) inflate.findViewById(R.id.asView_unqualified);
			CicleAddAndSubView asViewExamine = (CicleAddAndSubView) inflate.findViewById(R.id.asView_examine);
			CicleAddAndSubView asViewNg = (CicleAddAndSubView) inflate.findViewById(R.id.asView_ng);
			EditText etProcessMode = (EditText) inflate.findViewById(R.id.et_process_mode);
			
			viewholder=new Viewholder();
			viewholder.setTvItemDescriptionTextView(tvItemDescription);
			viewholder.setAsViewCheck(asViewCheck);
			viewholder.setAsViewUnqualified(asViewUnqualified);
			viewholder.setAsViewExamine(asViewExamine);
			viewholder.setAsViewNg(asViewNg);
			viewholder.setEtProcessMode(etProcessMode);
			
			inflate.setTag(viewholder);
		}
		else{
			inflate=convertView;
			viewholder = (Viewholder) inflate.getTag();
		}
		viewholder.getTvItemDescriptionTextView().setText(excelItem.getProject().getContent());
		
		viewholder.getAsViewCheck().setOnNumChangeListener(new OnNumChangeListener() {
			@Override
			public void onNumChange(View view, int stype, int num) {
				excelItem.setCheckNum(num);
			}
		});
		viewholder.getAsViewCheck().setNum(excelItem.getCheckNum());
		
		viewholder.getAsViewUnqualified().setOnNumChangeListener(new OnNumChangeListener() {
			@Override
			public void onNumChange(View view, int stype, int num) {
				excelItem.setUnqualifiedNum(num);
			}
		});
		viewholder.getAsViewUnqualified().setNum(excelItem.getUnqualifiedNum());
		
		viewholder.getAsViewExamine().setOnNumChangeListener(new OnNumChangeListener() {
			@Override
			public void onNumChange(View view, int stype, int num) {
				excelItem.setExamineNum(num);
			}
		});
		viewholder.getAsViewExamine().setNum(excelItem.getExamineNum());
		
		viewholder.getAsViewNg().setOnNumChangeListener(new OnNumChangeListener() {
			@Override
			public void onNumChange(View view, int stype, int num) {
				excelItem.setNgNum(num);
			}
		});
		final int ppp=position;
		viewholder.getAsViewNg().setNum(excelItem.getNgNum());
		
		viewholder.getEtProcessMode().addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				excelItem.setProcessMode(s.toString());
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
		viewholder.getEtProcessMode().setText(excelItem.getProcessMode());
		
		
		return inflate;
	}
	class Viewholder{
		public TextView tvItemDescriptionTextView;
		private CicleAddAndSubView asViewCheck;
		private CicleAddAndSubView asViewUnqualified;
		private CicleAddAndSubView asViewExamine;
		private CicleAddAndSubView asViewNg;
		private EditText etProcessMode;

		public EditText getEtProcessMode() {
			return etProcessMode;
		}

		public void setEtProcessMode(EditText etProcessMode) {
			this.etProcessMode = etProcessMode;
		}

		public CicleAddAndSubView getAsViewNg() {
			return asViewNg;
		}

		public void setAsViewNg(CicleAddAndSubView asViewNg) {
			this.asViewNg = asViewNg;
		}

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

		public CicleAddAndSubView getAsViewUnqualified() {
			return asViewUnqualified;
		}

		public void setAsViewUnqualified(CicleAddAndSubView asViewUnqualified) {
			this.asViewUnqualified = asViewUnqualified;
		}

		public CicleAddAndSubView getAsViewExamine() {
			return asViewExamine;
		}

		public void setAsViewExamine(CicleAddAndSubView asViewExamine) {
			this.asViewExamine = asViewExamine;
		}
	}
}
