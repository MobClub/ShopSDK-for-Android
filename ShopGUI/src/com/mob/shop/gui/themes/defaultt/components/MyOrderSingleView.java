package com.mob.shop.gui.themes.defaultt.components;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mob.shop.datatype.entity.OrderCommodity;
import com.mob.shop.gui.R;
import com.mob.shop.gui.utils.MoneyConverter;
import com.mob.tools.gui.AsyncImageView;
import com.mob.tools.utils.ResHelper;

public class MyOrderSingleView extends LinearLayout {
	private AsyncImageView ivLogo;
	private TextView tvName;
	private TextView tvMoney;
	private TextView tvAttribute;
	private TextView tvCount;
	private Context context;
	private int ivWidth;

	public MyOrderSingleView(Context context) {
		this(context, null);
	}

	public MyOrderSingleView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MyOrderSingleView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	private void init(Context context) {
		this.context = context;
		View view = LayoutInflater.from(context).inflate(R.layout.shopsdk_default_view_single_myorder, null);
		ivLogo = (AsyncImageView) view.findViewById(R.id.ivLogo);
		tvName = (TextView) view.findViewById(R.id.tvName);
		tvMoney = (TextView) view.findViewById(R.id.tvMoney);
		tvAttribute = (TextView) view.findViewById(R.id.tvAttribute);
		tvCount = (TextView) view.findViewById(R.id.tvCount);
		addView(view);
		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) ivLogo.getLayoutParams();
		ivWidth = lp.width;
	}

	public void setData(OrderCommodity orderCommodity) {
		if(ivWidth ==0){
			RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) ivLogo.getLayoutParams();
			ivWidth = lp.width;
		}
		ivLogo.setCompressOptions(ivWidth,ivWidth,80,10240L);
		ivLogo.execute(orderCommodity.getImgUrl().getSrc(), ResHelper.getColorRes(context, "order_bg"));
		tvName.setText(orderCommodity.getProductName());
		tvMoney.setText(MoneyConverter.conversionMoneyStr(orderCommodity.getCurrentCost()));
		tvAttribute.setText(orderCommodity.getPropertyDescribe());
		tvCount.setText("x" + orderCommodity.getCount());
	}

}
