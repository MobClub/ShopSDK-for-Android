package com.mob.shop.gui.themes.defaultt.components;


import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mob.shop.gui.R;
import com.mob.shop.gui.base.Page;
import com.mob.tools.utils.ResHelper;

public class TitleView extends RelativeLayout implements View.OnClickListener {
	private ImageView left;
	private ImageView ivRight;
	private TextView right;
	private TextView title;
	private Page page;

	public TitleView(Context context) {
		this(context, null);
	}

	public TitleView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TitleView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	private void init(Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.shopsdk_default_view_title, null);
		addView(view);
		left = (ImageView) view.findViewById(R.id.left);
		ivRight = (ImageView) view.findViewById(R.id.ivRight);
		right = (TextView) view.findViewById(R.id.right);
		title = (TextView) view.findViewById(R.id.title);
		right.setVisibility(INVISIBLE);
		ivRight.setVisibility(GONE);
		left.setOnClickListener(this);

	}

	public void initTitleView(Page page, String restName, String resrName, OnClickListener listener, boolean isBack) {
		setPage(page);
		setTitle(restName);
		setRight(false, resrName, listener);
		if (!isBack) {
			setNoBack();
		}
	}

	public void initTitleView(Page page, String restName, String resrName, OnClickListener listener, boolean isBack,
			boolean isRightIcon) {
		setPage(page);
		setTitle(restName);
		setRight(isRightIcon, resrName, listener);
		if (!isBack) {
			setNoBack();
		}
	}

	public void setTitle(String resName) {
		if (TextUtils.isEmpty(resName)) {
			return;
		}
		title.setText(page.getContext().getResources().getString(ResHelper.getStringRes(page.getContext(), resName)));
	}

	public void setRight(String resName, OnClickListener listener) {
		setRight(false, resName, listener);
	}

	public void setRight(boolean isIcon, String resName, OnClickListener listener) {
		if (TextUtils.isEmpty(resName)) {
			return;
		}

		setRight(isIcon, resName);

		if (isIcon) {
			ivRight.setOnClickListener(listener);
		} else if (listener != null) {
			right.setOnClickListener(listener);
		}

	}

	public void setRight(String resName) {
		setRight(false, resName);
	}

	public void setRight(boolean isIcon, String resName) {
		if (TextUtils.isEmpty(resName)) {
			return;
		}
		if (isIcon) {
			right.setVisibility(GONE);
			ivRight.setVisibility(VISIBLE);
			ivRight.setImageResource(ResHelper.getBitmapRes(page.getContext(), resName));
		} else {
			right.setVisibility(VISIBLE);
			ivRight.setVisibility(GONE);
			right.setText(page.getContext().getResources().getString(ResHelper.getStringRes(page.getContext(),
					resName)));
		}
	}

	public void setNoBack() {
		left.setVisibility(INVISIBLE);
	}

	public void setLeftClick(OnClickListener onClickListener) {
		left.setOnClickListener(onClickListener);
	}

	public void setPage(Page page) {
		this.page = page;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.left) {
			if (page != null) {
				page.finish();
			}
		}
	}


}
