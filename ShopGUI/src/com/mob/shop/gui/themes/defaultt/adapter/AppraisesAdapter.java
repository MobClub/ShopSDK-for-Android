package com.mob.shop.gui.themes.defaultt.adapter;


import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mob.shop.datatype.entity.Comment;
import com.mob.shop.gui.R;
import com.mob.shop.gui.themes.defaultt.components.ExactGridView;

import java.util.ArrayList;
import java.util.List;

public class AppraisesAdapter extends BaseAdapter {
	private List<Comment> comments;

	public AppraisesAdapter(List<Comment> comments) {
		this.comments = comments;
		if (comments == null) {
			comments = new ArrayList<Comment>();
		}
	}

	@Override
	public View getView(int i, View convertView, final ViewGroup viewGroup) {
		ViewHolder vh = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shopsdk_default_item_appraise,
					null);
			vh = new ViewHolder();
			vh.tvName = (TextView) convertView.findViewById(R.id.tvName);
			vh.tvAccess = (TextView) convertView.findViewById(R.id.tvAccess);
			vh.tvAttr = (TextView) convertView.findViewById(R.id.tvAttr);
			vh.ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBar);
			vh.accessImgView = (ExactGridView) convertView.findViewById(R.id.accessImgView);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		Comment comment = comments.get(i);
		if (comment.isAnonymity()) {
			String buyerName = comment.getBuyerName();
			if(!TextUtils.isEmpty(buyerName) && buyerName.length()>1){
				vh.tvName.setText(buyerName.substring(0,1)+"***"+buyerName.substring(buyerName.length()-1));
			}
		} else {
			vh.tvName.setText(comment.getBuyerName());
		}
		vh.tvAccess.setText(comment.getComment());
		vh.tvAttr.setText(comment.getPropertyDescribe());
		vh.ratingBar.setRating(comment.getCommentStars().getValue());
		AppraiseImgAdapter imgAdapter = new AppraiseImgAdapter(comment.getCommentImgUrls());
		vh.accessImgView.setAdapter(imgAdapter);
		return convertView;
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public Object getItem(int i) {
		return comments.get(i);
	}

	@Override
	public int getCount() {
		return comments == null ? 0 : comments.size();
	}

	private class ViewHolder {
		private TextView tvName;
		private RatingBar ratingBar;
		private TextView tvAccess;
		private ExactGridView accessImgView;
		private TextView tvAttr;
	}
}
