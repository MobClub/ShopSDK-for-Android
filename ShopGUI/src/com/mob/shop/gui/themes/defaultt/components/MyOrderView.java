package com.mob.shop.gui.themes.defaultt.components;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mob.shop.datatype.entity.Order;
import com.mob.shop.datatype.entity.OrderCommodity;
import com.mob.shop.gui.R;
import com.mob.shop.gui.base.Page;
import com.mob.shop.gui.utils.MoneyConverter;
import com.mob.tools.gui.AsyncImageView;
import com.mob.tools.utils.ResHelper;

public class MyOrderView extends LinearLayout {
	private AsyncImageView ivLogo;
	private TextView tvName;
	private TextView tvMoney;
	private TextView tvAttribute;
	private TextView tvCount;
	private TextView tvFreight;
	private TextView tvActualMoney;
	private TextView tvTotalCount;
	private TextView tvStatus;
	private OrderActionView orderActionView;
	private RelativeLayout rlView;
	private Context context;
	private int ivWidth;

	public MyOrderView(Context context) {
		this(context, null);
	}

	public MyOrderView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MyOrderView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	private void init(Context context) {
		this.context = context;
		View view = LayoutInflater.from(context).inflate(R.layout.shopsdk_default_view_myorder, null);
		rlView = (RelativeLayout) view.findViewById(R.id.rlView);
		ivLogo = (AsyncImageView) view.findViewById(R.id.ivLogo);
		tvName = (TextView) view.findViewById(R.id.tvName);
		tvMoney = (TextView) view.findViewById(R.id.tvMoney);
		tvAttribute = (TextView) view.findViewById(R.id.tvAttribute);
		tvFreight = (TextView) view.findViewById(R.id.tvFreight);
		tvActualMoney = (TextView) view.findViewById(R.id.tvActualMoney);
		tvTotalCount = (TextView) view.findViewById(R.id.tvTotalCount);
		tvCount = (TextView) view.findViewById(R.id.tvCount);
		tvStatus = (TextView) view.findViewById(R.id.tvStatus);
		orderActionView = (OrderActionView) view.findViewById(R.id.orderActionView);
		addView(view);
		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) ivLogo.getLayoutParams();
		ivWidth = lp.width;
	}

	public void setData(Page page, Order order, OrderCommodity orderCommodity, boolean isTop) {
		if(ivWidth ==0){
			RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) ivLogo.getLayoutParams();
			ivWidth = lp.width;
		}
		ivLogo.setCompressOptions(ivWidth, ivWidth, 80, 10240L);
		ivLogo.execute(orderCommodity.getImgUrl().getSrc(), ResHelper.getColorRes(context, "order_bg"));
		tvName.setText(orderCommodity.getProductName());
		tvMoney.setText("￥" + MoneyConverter.conversionString(orderCommodity.getCurrentCost()));
		tvAttribute.setText(orderCommodity.getPropertyDescribe());
		String fFormat = context.getResources().getString(ResHelper.getStringRes(context,
				"shopsdk_default_order_freight"));
		String cFormat = context.getResources().getString(ResHelper.getStringRes(context,
				"shopsdk_default_buy_count"));
		tvFreight.setText(String.format(fFormat, MoneyConverter.conversionString(order.getTotalFreight())));
		tvActualMoney.setText(MoneyConverter.conversionMoneyStr(order.getPaidMoney()));
		tvTotalCount.setText(String.format(cFormat, order.getTotalCount()));
		tvCount.setText("x" + orderCommodity.getCount());
		orderActionView.setOrderStatus(page, order);

		switch (order.getStatus()) {
			case CREATED_AND_WAIT_FOR_PAY:
			case ALL:
				tvStatus.setText(ResHelper.getStringRes(context, "shopsdk_default_unpay_order"));
				break;
			case PAID_AND_WAIT_FOR_DILIVER:
				tvStatus.setText(ResHelper.getStringRes(context, "shopsdk_default_unSeed_order"));
				break;
			case DILIVERED_AND_WAIT_FOR_RECEIPT:
				tvStatus.setText(ResHelper.getStringRes(context, "shopsdk_default_send_order"));
				break;
			case COMPLETED:
				tvStatus.setText(ResHelper.getStringRes(context, "shopsdk_default_completed_order"));
				break;
			case CLOSED:
				tvStatus.setText(ResHelper.getStringRes(context, "shopsdk_default_canceled"));
				break;
		}

		rlView.setPadding(0, ResHelper.dipToPx(context, isTop ? 10 : 5), 0, 0);
	}

	public void setOnOrderOperatorListener(OrderActionView.OnOrderOperatorListener onOrderOperatorListener) {
		if (onOrderOperatorListener != null) {
			orderActionView.setOnOrderOperatorListener(onOrderOperatorListener);
		}
	}
}
