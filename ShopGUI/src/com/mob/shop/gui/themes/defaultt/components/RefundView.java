package com.mob.shop.gui.themes.defaultt.components;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mob.shop.datatype.entity.Refund;
import com.mob.shop.gui.R;
import com.mob.shop.gui.base.Page;
import com.mob.shop.gui.pages.RefundDetailPage;
import com.mob.shop.gui.utils.MoneyConverter;
import com.mob.tools.gui.AsyncImageView;
import com.mob.tools.utils.ResHelper;

public class RefundView extends LinearLayout {
	private AsyncImageView ivLogo;
	private TextView tvName;
	private TextView tvMoney;
	private TextView tvRefundMoney;
	private TextView tvAttribute;
	private TextView tvCount;
	private TextView tvStatus;
	private TextView tvActionRight;
	private RelativeLayout rlView;
	private Context context;
	private Page page;
	private Refund refund;
	private int ivWidth;

	public RefundView(Context context) {
		this(context, null);
	}

	public RefundView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public RefundView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	private void init(Context context) {
		this.context = context;
		View view = LayoutInflater.from(context).inflate(R.layout.shopsdk_default_view_refund, null);
		rlView = (RelativeLayout) view.findViewById(R.id.rlView);
		ivLogo = (AsyncImageView) view.findViewById(R.id.ivLogo);
		tvName = (TextView) view.findViewById(R.id.tvName);
		tvMoney = (TextView) view.findViewById(R.id.tvMoney);
		tvAttribute = (TextView) view.findViewById(R.id.tvAttribute);
		tvRefundMoney = (TextView) view.findViewById(R.id.tvRefundMoney);
		tvCount = (TextView) view.findViewById(R.id.tvCount);
		tvStatus = (TextView) view.findViewById(R.id.tvStatus);
		tvActionRight = (TextView) view.findViewById(R.id.tvActionRight);
		addView(view);
		tvActionRight.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (refund != null) {
					RefundDetailPage detailPage = new RefundDetailPage(page.getTheme(), refund.getOrderCommodityId());
					detailPage.show(page.getContext(), null);
				}
			}
		});
		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) ivLogo.getLayoutParams();
		ivWidth = lp.width;
	}

	public void setRefundData(Page page, Refund refund) {
		this.page = page;
		this.refund = refund;
		if(ivWidth ==0){
			RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) ivLogo.getLayoutParams();
			ivWidth = lp.width;
		}
		ivLogo.setCompressOptions(ivWidth, ivWidth, 80, 10240L);
		ivLogo.execute(refund.getImgUrl().getSrc(), ResHelper.getColorRes(context, "order_bg"));
		tvName.setText(refund.getProductName());
		tvMoney.setText(MoneyConverter.conversionMoneyStr(refund.getPaidMoney()));
		String fomat = page.getContext().getString(ResHelper.getStringRes(page.getContext(),
				"shopsdk_default_refund_money_key"));
		tvRefundMoney.setText(String.format(fomat, MoneyConverter.conversionMoneyStr(refund.getRefundFee())));
		tvAttribute.setText(refund.getPropertyDescribe());
		tvCount.setText("x"+String.valueOf(refund.getOrderCommodityCount()));
		String refundType = refund.getRefundType().getDesc();
		tvStatus.setText(refundType + "," + refund.getStatus().getDesc());
	}
}
