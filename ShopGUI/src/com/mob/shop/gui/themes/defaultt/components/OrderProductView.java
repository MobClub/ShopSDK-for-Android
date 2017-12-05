package com.mob.shop.gui.themes.defaultt.components;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mob.shop.datatype.CommodityStatus;
import com.mob.shop.datatype.entity.ImgUrl;
import com.mob.shop.gui.R;
import com.mob.tools.gui.AsyncImageView;
import com.mob.tools.utils.ResHelper;


public class OrderProductView extends RelativeLayout {
	private AsyncImageView iv;
	private TextView tvName;
	private TextView tvCount;
	private TextView tvAttribute;
	private TextView tvPrice;
	private TextView tvRefund;
	private View line;
	private Context context;
	private OnActionListener onActionListener;


	public OrderProductView(Context context) {
		this(context, null);
	}

	public OrderProductView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public OrderProductView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	private void init(Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.shopsdk_default_view_order, null);
		addView(view);
		this.context = context;

		iv = (AsyncImageView) view.findViewById(R.id.iv);
		tvName = (TextView) view.findViewById(R.id.tvName);
		tvCount = (TextView) view.findViewById(R.id.tvCount);
		tvAttribute = (TextView) view.findViewById(R.id.tvAttribute);
		tvPrice = (TextView) view.findViewById(R.id.tvPrice);
		tvRefund = (TextView) view.findViewById(R.id.tvRefund);
		line = view.findViewById(R.id.line);
		tvRefund.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (onActionListener != null) {
					onActionListener.refund();
				}
			}
		});
	}

	public void initData(ImgUrl imgUrl, String name, String count, String attribute, String price, boolean isReundable, CommodityStatus
			commodityStatus, boolean isLast) {
		iv.execute(imgUrl.getSrc(), ResHelper.getBitmapRes(context, "order_bg"));
		tvName.setText(name);
		tvCount.setText("x" + count);
		tvAttribute.setText(attribute);
		tvPrice.setText("￥" + price);
		setRefundable(isReundable);
		switch (commodityStatus){
			case REFUNDING:
				tvRefund.setText(context.getString(ResHelper.getStringRes(context,"shopsdk_default_refunding")));
				break;
			case REFUND_COMPLETE:
				tvRefund.setText(context.getString(ResHelper.getStringRes(context,"shopsdk_default_refund_complete")));
				break;
		}
		if (isLast) {
			setLast(isLast);
		}
	}

	public void setRefundable(boolean isRefundable) {
		if (isRefundable) {
			tvRefund.setVisibility(VISIBLE);
		} else {
			tvRefund.setVisibility(GONE);
		}
	}

	public void setLast(boolean isLast) {
		if (isLast) {
			line.setVisibility(GONE);
		} else {
			line.setVisibility(VISIBLE);
		}
	}


	public void setOnActionListener(OnActionListener onActionListener) {
		this.onActionListener = onActionListener;
	}

	public interface OnActionListener {
		void refund();
	}
}
