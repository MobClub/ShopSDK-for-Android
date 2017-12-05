package com.mob.shop.gui.themes.defaultt.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.mob.shop.datatype.entity.ExpressCompany;
import com.mob.shop.gui.R;

import java.util.List;


/**
 * Created by yjin on 2017/11/6.
 */

public class ExpressStdReferAdapter extends BaseAdapter implements SectionIndexer {
	private Context context;
	private boolean isSort;
	private List<ExpressCompany> list;
	private ExpressCompany expressCompanyOne;

	public ExpressStdReferAdapter(Context context, boolean isSort, List<ExpressCompany> list) {
		this.context = context;
		this.isSort = isSort;
		this.list = list;
	}

	public void setExpressCompany(ExpressCompany expressCompany){
		this.expressCompanyOne = expressCompany;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int i) {
		return list.get(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		ViewHolder viewHolder = null;
		ExpressCompany expressCompany = list.get(i);
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(context).inflate(R.layout.shopsdk_default_express_item_std_list,null);
			viewHolder.tvLetter = (TextView) view.findViewById(R.id.cateLog);
			viewHolder.bumen = (TextView) view.findViewById(R.id.textView_collewgue_fragment_bumen);
			viewHolder.selectIcon = (ImageView)view.findViewById(R.id.selectedExpressIcon);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		if (isSort) {
			int section = getSectionForPosition(i);

			if (i == getPositionForSection(section)) {
				viewHolder.tvLetter.setVisibility(View.VISIBLE);
				viewHolder.tvLetter.setText(expressCompany.getInitial());
			} else {
				viewHolder.tvLetter.setVisibility(View.GONE);
			}
		}
		if(expressCompanyOne != null){
			if(expressCompanyOne.getAbbreviation().equals(expressCompany.getAbbreviation())){
				viewHolder.selectIcon.setVisibility(View.VISIBLE);
			} else {
				viewHolder.selectIcon.setVisibility(View.GONE);
			}
		}
		viewHolder.bumen.setText(expressCompany.getAbbreviation());
		return view;
	}


	public static class ViewHolder {
		public TextView tvLetter;
		public TextView bumen;
		public ImageView selectIcon;
	}


	@Override
	public Object[] getSections() {
		return null;
	}

	@Override
	public int getPositionForSection(int section) {
		for(int i= 0;i<getCount();i++){
			String sortStr = list.get(i).getInitial();
			if(!TextUtils.isEmpty(sortStr)){
				char firstChar = sortStr.toUpperCase().charAt(0);
				if(firstChar == section){
					return i;
				}
			}

		}
		return -1;
	}

	public void updateListView(List<ExpressCompany> List){
		this.list = list;
		notifyDataSetChanged();
	}

	@Override
	public int getSectionForPosition(int i) {
		if(!TextUtils.isEmpty(list.get(i).getInitial())){
			return list.get(i).getInitial().charAt(0);
		} else {
			return 0;
		}

	}

	private String getAlpha(String str){
		String sortStr = str.trim().substring(0,1).toUpperCase();
		if(sortStr.matches("[A-Z]")){
			return sortStr;
		} else {
			return "#";
		}
	}
}
