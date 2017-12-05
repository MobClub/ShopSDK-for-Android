package com.mob.shop.gui.themes.defaultt;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.shop.OperationCallback;
import com.mob.shop.ShopSDK;
import com.mob.shop.datatype.entity.RefundDescription;
import com.mob.shop.datatype.entity.RefundDetail;
import com.mob.shop.gui.R;
import com.mob.shop.gui.SGUIOperationCallback;
import com.mob.shop.gui.pages.OrderDetailPage;
import com.mob.shop.gui.pages.RefundDetailPage;
import com.mob.shop.gui.themes.defaultt.components.TitleView;

import java.util.List;

/**
 * 退款详情页.
 */

public class RefundDetailPageAdapter extends DefaultThemePageAdapter<RefundDetailPage> implements View.OnClickListener {
	private View view;
	private TitleView titleView;
	private LinearLayout containerLl;
	private TextView progressTv;
	private TextView orderIdTv;
	private TextView orderDetailTv;
	private TextView csTv;
	private RefundDetail refundDetail;

	@Override
	public void onCreate(RefundDetailPage page, Activity activity) {
		super.onCreate(page, activity);
		view = LayoutInflater.from(activity).inflate(R.layout.shopsdk_default_page_refund_detail,null);
		activity.setContentView(view);

		initView();
		getRefundDetail();
	}

	private void initView(){
		titleView = (TitleView)view.findViewById(R.id.shopsdk_refund_detail_titleview);
		titleView.initTitleView(getPage(), "shopsdk_default_refund_detail_title", null, null, true);
		containerLl = (LinearLayout)view.findViewById(R.id.shopsdk_refund_detail_container_ll);
		progressTv = (TextView)view.findViewById(R.id.shopsdk_refund_detail_progress_tv);
		orderIdTv = (TextView)view.findViewById(R.id.shopsdk_refund_detail_order_id);
		orderDetailTv = (TextView)view.findViewById(R.id.shopsdk_refund_detail_order_detail_tv);
		orderDetailTv.setOnClickListener(this);
		csTv = (TextView)view.findViewById(R.id.shopsdk_refund_detail_custom_service_tv);
		csTv.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		int which = v.getId();
		if (which == R.id.shopsdk_refund_detail_order_detail_tv) {
			gotoOrderDetailPage();
		} else if (which == R.id.shopsdk_refund_detail_custom_service_tv) {
			// TODO 客服
//			toastMessage("Custom Service");
		}
	}

	private void getRefundDetail() {
		ShopSDK.getRefundDetail(getPage().getOrderCommodityId(), new SGUIOperationCallback<RefundDetail>(getPage().getCallback()) {
			@Override
			public void onSuccess(RefundDetail data) {
				super.onSuccess(data);
				refundDetail = data;
				if (refundDetail != null) {
					progressTv.setText(refundDetail.getStatus().getDesc());
					orderIdTv.setText(String.valueOf(refundDetail.getOrderId()));
					updateContainerUI(refundDetail.getDescirbeList());
				}
			}

			@Override
			public boolean onResultError(Throwable t) {
				getPage().toastNetworkError();
				return super.onResultError(t);
			}
		});
	}

	private void updateContainerUI(List<RefundDescription> descList) {
		if (descList != null && !descList.isEmpty()) {
			// Remove all RefundBaseViews except the first view (containerLl originally has one child which displays 'process' & 'orderId')
			int childCount = this.containerLl.getChildCount();
			if (childCount > 1) {
				this.containerLl.removeViews(1, this.containerLl.getChildCount() - 1);
			}
			RefundBaseView refundView;
			for (int i = 0; i < descList.size(); i ++) {
				switch (descList.get(i).getStatus()) {
					case APPLYING: {
						refundView = new RefundApplying(this, descList.get(i));
						break;
					}
					case SELLER_AGREED_AND_WAIT_FOR_DELIVERY: {
						refundView = new RefundAgreed(this, descList.get(i));
						break;
					}
					case SELLER_DISAGREED: {
						refundView = new RefundDisagreed(this, descList.get(i));
						break;
					}
					case DELIVERING: {
						refundView = new RefundDelivering(this, descList.get(i));
						break;
					}
					case SUCCEEDED: {
						refundView = new RefundSucceeded(this, descList.get(i));
						break;
					}
					case CLOSED: {
						refundView = new RefundClosed(this, descList.get(i));
						break;
					}
					default: {
						refundView = new RefundApplying(this, descList.get(i));
						break;
					}
				}
				if (i == 0) {
					refundView.setExpiration(refundDetail.getExpiration());
				}
				refundView.setOrderCommodityId(refundDetail.getOrderCommodityId());
				refundView.setOnRequestRefundDetailListener(new RefundBaseView.OnRequestRefundDetailListener() {
					@Override
					public void onRequestRefundDetail() {
						getRefundDetail();
					}
				});
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				params.setMargins(0,30,0,0);
				this.containerLl.addView(refundView, params);
			}
		}
	}

	private void gotoOrderDetailPage() {
		OrderDetailPage orderDetailPage = new OrderDetailPage(getPage().getTheme(), refundDetail.getOrderId());
		orderDetailPage.show(getPage().getContext(), null);
	}
}
