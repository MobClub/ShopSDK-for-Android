package com.mob.shop.gui.themes.defaultt;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mob.shop.ShopSDK;
import com.mob.shop.datatype.OrderOperator;
import com.mob.shop.datatype.OrderStatus;
import com.mob.shop.datatype.entity.OrderCommodity;
import com.mob.shop.datatype.entity.OrderDetail;
import com.mob.shop.datatype.entity.Product;
import com.mob.shop.datatype.entity.ShippingAddr;
import com.mob.shop.gui.R;
import com.mob.shop.gui.SGUIOperationCallback;
import com.mob.shop.gui.pages.ApplyRefundPage;
import com.mob.shop.gui.pages.CommodityDetailShowPage;
import com.mob.shop.gui.pages.OrderDetailPage;
import com.mob.shop.gui.pages.RefundDetailPage;
import com.mob.shop.gui.pages.dialog.ProgressDialog;
import com.mob.shop.gui.themes.defaultt.components.OrderActionView;
import com.mob.shop.gui.themes.defaultt.components.OrderProductView;
import com.mob.shop.gui.themes.defaultt.components.OrderShippingAddressView;
import com.mob.shop.gui.themes.defaultt.components.TitleView;
import com.mob.shop.gui.utils.MoneyConverter;
import com.mob.shop.gui.utils.Utils;
import com.mob.tools.FakeActivity;
import com.mob.tools.utils.ResHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.mob.shop.datatype.CommodityStatus.REFUNDING;
import static com.mob.shop.datatype.CommodityStatus.REFUND_COMPLETE;

/**
 * 订单详情页
 */
public class OrderDetailPageAdapter extends DefaultThemePageAdapter<OrderDetailPage> {
	private ListView listView;
	private OrderDetailAdapter adapter;
	private OrderDetailPage page;
	private OrderShippingAddressView shippingAddressView;
	private TextView tvFreight;
	private TextView tvCouponDeduction;
	private TextView tvCommodityMoney;
	private TextView tvOrderNo;
	private TextView tvOrderTime;
	private TextView tvActualMoney;
	private TextView tvStatus;
	private OrderActionView orderActionView;
	private TextView tvTimeCount;
	private ImageView ivStatus;
	private OrderDetail orderDetail;
	private View header;
	private View footer;
	private ProgressDialog pd;

	@Override
	public void onCreate(OrderDetailPage page, Activity activity) {
		super.onCreate(page, activity);
		this.page = page;
		View view = LayoutInflater.from(page.getContext()).inflate(R.layout.shopsdk_default_page_orderdetail, null);
		activity.setContentView(view);

		findView(view);
		initView();
		initListener();

		getOrderDetail();
	}

	private void findView(View view) {
		TitleView titleView = (TitleView) view.findViewById(R.id.titleView);
		titleView.initTitleView(page, "shopsdk_default_order_detail", null, null, true);
		tvActualMoney = (TextView) view.findViewById(R.id.tvActualMoney);
		orderActionView = (OrderActionView) view.findViewById(R.id.orderActionView);
		listView = (ListView) view.findViewById(R.id.listView);

		header = LayoutInflater.from(page.getContext()).inflate(R.layout.shopsdk_default_page_orderdetail_header,
				null);
		tvStatus = (TextView) header.findViewById(R.id.tvStatus);
		tvTimeCount = (TextView) header.findViewById(R.id.tvTimeCount);
		ivStatus = (ImageView) header.findViewById(R.id.ivStatus);
		shippingAddressView = (OrderShippingAddressView) header.findViewById(R.id.shippingAddressView);
		shippingAddressView.setIvMore(false);

		footer = LayoutInflater.from(page.getContext()).inflate(R.layout.shopsdk_default_page_orderdetail_footer,
				null);
		tvFreight = (TextView) footer.findViewById(R.id.tvFreight);
		tvCouponDeduction = (TextView) footer.findViewById(R.id.tvCouponDeduction);
		tvCommodityMoney = (TextView) footer.findViewById(R.id.tvCommodityMoney);
		tvOrderNo = (TextView) footer.findViewById(R.id.tvOrderNo);
		tvOrderTime = (TextView) footer.findViewById(R.id.tvOrderTime);
	}

	private void initView() {
		/* 必须在setAdapter之前执行addHeaderView方法，否则会出现以下异常：
		 * java.lang.IllegalStateException: Cannot add header view to list -- setAdapter has already been called.
		 */
		listView.addHeaderView(header);
		listView.addFooterView(footer);
		adapter = new OrderDetailAdapter();
		listView.setAdapter(adapter);
	}

	private void initListener() {
		orderActionView.setOnOrderOperatorListener(new OrderActionView.OnOrderOperatorListener() {
			@Override
			public void onOrderOperator(OrderOperator operator) {
				changeOrder(operator);
			}
		});
	}

	private void getOrderDetail() {
		pd = new ProgressDialog.Builder(page.getContext(), page.getTheme()).show();
		ShopSDK.getOrderDetail(page.getOrderId(), new SGUIOperationCallback<OrderDetail>(page.getCallback()) {
			@Override
			public void onSuccess(OrderDetail data) {
				super.onSuccess(data);
				if (pd != null && pd.isShowing()) {
					pd.dismiss();
				}
				orderDetail = data;
				initOrderDetail();
			}

			@Override
			public boolean onResultError(Throwable t) {
				if (pd != null && pd.isShowing()) {
					pd.dismiss();
				}
				page.toastMessage(ResHelper.getStringRes(page.getContext(), "shopsdk_default_order_detail_failed"));
				return super.onResultError(t);
			}
		});
	}

	private void initOrderDetail() {
		if (orderDetail != null) {
			ShippingAddr shippingAddr = orderDetail.getShippingAddrInfo();
			if (shippingAddr != null) {
				shippingAddressView.initView(shippingAddr, false);
			}
			adapter.setList(orderDetail.getOrderCommodityList());
			tvFreight.setText("￥" + MoneyConverter.conversionString(orderDetail.getTotalFreight()));
			tvCouponDeduction.setText("-￥" + MoneyConverter.conversionString(orderDetail.getTotalCoupon()));
			tvCommodityMoney.setText("￥" + MoneyConverter.conversionString(orderDetail.getPaidMoney()));
			tvOrderNo.setText(String.valueOf(orderDetail.getOrderId()));
			tvOrderTime.setText(ResHelper.longToTime(orderDetail.getCreateAt(), 0));
			tvActualMoney.setText("￥" + MoneyConverter.conversionString(orderDetail.getPaidMoney()));
			notifyOrderStatus();
		}
	}

	private void notifyOrderStatus(){
		orderStatus(orderDetail.getStatus());
		orderActionView.setOrderStatus(page,orderDetail);
		if (orderDetail.getStatus() != OrderStatus.CREATED_AND_WAIT_FOR_PAY && orderDetail.getStatus() !=
				OrderStatus.CLOSED) {
			adapter.setRefundable(true);
		} else {
			adapter.setRefundable(false);
		}

		if (orderDetail.getStatus() != OrderStatus.COMPLETED && orderDetail.getStatus() != OrderStatus.CLOSED) {
			String format = getString("shopsdk_default_count_down");
			tvTimeCount.setText(String.format(format, Utils.getCountDownTime(orderDetail.getExpiration(), Utils
					.Accuracy.SECOND)));
			tvTimeCount.setVisibility(View.VISIBLE);
		} else {
			tvTimeCount.setVisibility(View.INVISIBLE);
		}
	}

	private void orderStatus(OrderStatus orderStatus) {
		if (orderStatus == null) {
			return;
		}
		switch (orderStatus) {
			case CREATED_AND_WAIT_FOR_PAY:
				tvStatus.setText(getString("shopsdk_default_wating_pay"));
				ivStatus.setImageResource(getBitmap("shopsdk_default_waiting"));
				break;
			case PAID_AND_WAIT_FOR_DILIVER:
				tvStatus.setText(getString("shopsdk_default_unSeed_order"));
				ivStatus.setImageResource(getBitmap("shopsdk_default_waiting"));
				break;
			case DILIVERED_AND_WAIT_FOR_RECEIPT:
				tvStatus.setText(getString("shopsdk_default_unShipping_order"));
				ivStatus.setImageResource(getBitmap("shopsdk_default_waiting"));
				break;
			case COMPLETED:
				tvStatus.setText(getString("shopsdk_default_completed_order"));
				break;
			case CLOSED:
				tvStatus.setText(getString("shopsdk_default_order_close"));
				ivStatus.setImageResource(getBitmap("shopsdk_default_order_close"));
				break;
		}
	}

	private void changeOrder(final OrderOperator operator) {
		if (orderDetail == null) {
			return;
		}
		ShopSDK.modifyOrderStatus(orderDetail.getOrderId(), operator, new SGUIOperationCallback<Void>(page.getCallback()) {
			@Override
			public void onSuccess(Void data) {
				super.onSuccess(data);
				page.setResult(new HashMap<String, Object>());
				switch (operator) {
					case DELETE:
						page.toastMessage(ResHelper.getStringRes(page.getContext(),
								"shopsdk_default_order_delete_success"));
						finish();
						break;
					case CANCEL:
						page.toastMessage(ResHelper.getStringRes(page.getContext(),
								"shopsdk_default_order_cancel_success"));
						orderDetail.setStatus(OrderStatus.CLOSED);
						notifyOrderStatus();
						break;
					case CONFIRM:
						page.toastMessage(ResHelper.getStringRes(page.getContext(),
								"shopsdk_default_order_comfirm_success"));
						orderDetail.setStatus(OrderStatus.COMPLETED);
						notifyOrderStatus();
						break;
				}
			}

			@Override
			public boolean onResultError(Throwable t) {
				switch (operator) {
					case DELETE:
						page.toastMessage(ResHelper.getStringRes(page.getContext(),
								"shopsdk_default_order_delete_failed"));
						break;
					case CANCEL:
						page.toastMessage(ResHelper.getStringRes(page.getContext(),
								"shopsdk_default_order_cancel_failed"));
						break;
					case CONFIRM:
						page.toastMessage(ResHelper.getStringRes(page.getContext(),
								"shopsdk_default_order_comfirm_failed"));
						break;
				}
				return super.onResultError(t);
			}
		});
	}

	@Override
	public void onDestroy(OrderDetailPage page, Activity activity) {
		super.onDestroy(page, activity);
		if (pd != null) {
			if (pd.isShowing()) {
				pd.dismiss();
			}
			pd = null;
		}
	}

	private class OrderDetailAdapter extends BaseAdapter {
		private List<OrderCommodity> list = new ArrayList<OrderCommodity>();
		private boolean isRefundable;

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		public void setList(List<OrderCommodity> list) {
			if (list == null) {
				return;
			}
			this.list.addAll(list);
			notifyDataSetChanged();
		}

		public void setRefundable(boolean refundable) {
			isRefundable = refundable;
			notifyDataSetChanged();
		}

		public View getView(int position, View convertView, final ViewGroup parent) {
			final ViewHolder vh;
			if (convertView == null) {
				OrderProductView orderProductView = new OrderProductView(parent.getContext());
				AbsListView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
						AbsListView.LayoutParams.WRAP_CONTENT);
				orderProductView.setLayoutParams(lp);
				convertView = orderProductView;
				vh = new ViewHolder();
				vh.orderProductView = orderProductView;
				convertView.setTag(vh);
			} else {
				vh = (ViewHolder) convertView.getTag();
			}

			final OrderCommodity orderCommodity = list.get(position);
			vh.orderProductView.initData(orderCommodity.getImgUrl(), orderCommodity.getProductName(), String.valueOf
					(orderCommodity.getCount()), orderCommodity.getPropertyDescribe(), MoneyConverter.conversionString
					(orderCommodity.getCurrentCost()), isRefundable, orderCommodity.getStatus(), false);
			vh.orderProductView.setOnActionListener(new OrderProductView.OnActionListener() {
				@Override
				public void refund() {
					if (orderCommodity.getStatus() == REFUNDING || orderCommodity.getStatus() == REFUND_COMPLETE) {
						RefundDetailPage refundDetailPage = new RefundDetailPage(getPage().getTheme(), orderCommodity
								.getOrderCommodityId());
						refundDetailPage.show(getPage().getContext(), null);
					} else {
						ApplyRefundPage applyRefundPage = new ApplyRefundPage(getPage().getTheme(), orderCommodity
								.getOrderCommodityId());
						applyRefundPage.showForResult(getPage().getContext(), null, new FakeActivity() {
							@Override
							public void onResult(HashMap<String, Object> data) {
								super.onResult(data);
								if (data == null) {
									return;
								}
								orderCommodity.setStatus(REFUNDING);
								notifyDataSetChanged();
							}
						});
					}
				}
			});
			convertView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Product product = new Product();
					product.setProductId(orderCommodity.getProductId());
					CommodityDetailShowPage commodityDetailShowPage = new CommodityDetailShowPage(page.getTheme(),
							product);
					commodityDetailShowPage.show(page.getContext(), null);
				}
			});
			return convertView;
		}

		class ViewHolder {
			private OrderProductView orderProductView;
		}
	}


}
