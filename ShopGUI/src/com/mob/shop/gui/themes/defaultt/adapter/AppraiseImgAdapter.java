package com.mob.shop.gui.themes.defaultt.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.mob.shop.datatype.entity.ImgUrl;
import com.mob.shop.gui.R;
import com.mob.tools.gui.AsyncImageView;
import com.mob.tools.utils.ResHelper;

import java.util.ArrayList;

public class AppraiseImgAdapter extends BaseAdapter {
	ArrayList<ImgUrl> imgs = null;

	public AppraiseImgAdapter(ArrayList<ImgUrl> imgs) {
		if (imgs == null) {
			imgs = new ArrayList<ImgUrl>();
		}
		this.imgs = imgs;
	}

	@Override
	public int getCount() {
		return imgs.size();
	}

	@Override
	public Object getItem(int i) {
		return imgs.get(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		ViewHolder vh = null;
		if (view == null) {
			view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout
					.shopsdk_default_item_appraise_img, null);
			vh = new ViewHolder();
			vh.iv = (AsyncImageView) view.findViewById(R.id.iv);
			view.setTag(vh);
		} else {
			vh = (ViewHolder) view.getTag();
		}
		vh.iv.execute(imgs.get(i).getSrc(), ResHelper.getColorRes(viewGroup.getContext(), "order_bg"));
		return view;
	}

	private class ViewHolder {
		AsyncImageView iv;
	}
}
