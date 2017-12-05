package com.mob.shop.gui.themes.defaultt;


import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.mob.shop.ShopSDK;
import com.mob.shop.datatype.OrderOperator;
import com.mob.shop.datatype.OrderStatus;
import com.mob.shop.datatype.builder.OrderListQuerier;
import com.mob.shop.datatype.entity.Order;
import com.mob.shop.datatype.entity.OrderCommodity;
import com.mob.shop.gui.R;
import com.mob.shop.gui.SGUIOperationCallback;
import com.mob.shop.gui.base.Page;
import com.mob.shop.gui.base.PageAdapter;
import com.mob.shop.gui.pages.OrderDetailPage;
import com.mob.shop.gui.pages.SearchOrderResultPage;
import com.mob.shop.gui.themes.defaultt.components.DefaultRTRListAdapter;
import com.mob.shop.gui.themes.defaultt.components.MyOrderSingleView;
import com.mob.shop.gui.themes.defaultt.components.MyOrderView;
import com.mob.shop.gui.themes.defaultt.components.OrderActionView;
import com.mob.shop.gui.themes.defaultt.components.TitleView;
import com.mob.tools.gui.PullToRequestView;
import com.mob.tools.utils.ResHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 搜索结果页
 */
public class SearchOrderResultPageAdapter extends PageAdapter<SearchOrderResultPage> {
	private PullToRequestView listView;
	private SearchOrdersAdapter adapter;
	private TitleView titleView;

	@Override
	public void onCreate(SearchOrderResultPage page, Activity activity) {
		super.onCreate(page, activity);
		Window window = activity.getWindow();
		window.setBackgroundDrawable(new ColorDrawable(0x50000000));
		View view = LayoutInflater.from(page.getContext()).inflate(R.layout.shopsdk_default_page_searchorderresult,
				null);
		activity.setContentView(view);

		titleView = (TitleView) view.findViewById(R.id.titleView);
		listView = (PullToRequestView) view.findViewById(R.id.listView);
		titleView.initTitleView(page, "shopsdk_default_search_result", null, null, true);

		adapter = new SearchOrdersAdapter(page, listView);
		adapter.setEmptyRes(page.getContext(), "shopsdk_default_empty_order_img", "shopsdk_default_empty_order_tip");
		adapter.setKeywords(page.getSearch());
		listView.setAdapter(adapter);
		listView.performPullingDown(true);
	}

	private static class SearchOrdersAdapter extends DefaultRTRListAdapter {
		private List<Order> orders = new ArrayList<Order>();
		private Page page;
		private static final int PAGE_SIZE = 20;
		private int pageIndex = 1;
		private int totalCount = 0;
		private List<OrderCommodity> orderCommodities = new ArrayList<OrderCommodity>();
		private HashMap<Long, Long> dividOrderCommodity = new HashMap<Long, Long>();
		private HashMap<Long, Integer> dividOrderInfoCommodity = new HashMap<Long, Integer>();
		private HashMap<Long, Long> orderCommoditiesOrderId = new HashMap<Long, Long>();
		private String Keywords = "";

		public SearchOrdersAdapter(Page page, PullToRequestView view) {
			super(view);
			this.page = page;
			getListView().setDividerHeight(0);
		}

		public void setKeywords(String keywords) {
			Keywords = keywords;
		}

		@Override
		protected void onRequest(boolean firstPage) {
			if (firstPage) {
				pageIndex = 1;
			}
			getOrders();
		}

		@Override
		public int getItemViewType(int position) {
			OrderCommodity orderCommodity = orderCommodities.get(position);
			if (!dividOrderCommodity.containsKey(orderCommodity.getOrderCommodityId())) {
				return 1;
			}
			return 0;
		}

		@Override
		public int getViewTypeCount() {
			return 2;
		}

		@Override
		public View getView(int i, View convertView, final ViewGroup viewGroup) {
			int type = getItemViewType(i);
			OrderCommodity orderCommodity = orderCommodities.get(i);
			final long orderId = orderCommoditiesOrderId.get(orderCommodity.getOrderCommodityId());
			switch (type) {
				case 0:
					ViewHolder2 vh2 = null;
					if (convertView == null) {
						convertView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout
								.shopsdk_default_item_status_myorder, null);
						vh2 = new ViewHolder2();
						vh2.myOrderView = (MyOrderView) convertView.findViewById(R.id.myOrderView);
						convertView.setTag(vh2);
					} else {
						vh2 = (ViewHolder2) convertView.getTag();
					}

					final int orderIndex = dividOrderInfoCommodity.get(orderCommodity.getOrderCommodityId());
					final Order order = orders.get(orderIndex);
					if (i == 0 || getItemViewType(i - 1) == 0) {
						vh2.myOrderView.setData(page, order, orderCommodity, true);
					} else {
						vh2.myOrderView.setData(page, order, orderCommodity, false);
					}
					vh2.myOrderView.setOnOrderOperatorListener(new OrderActionView.OnOrderOperatorListener() {
						@Override
						public void onOrderOperator(OrderOperator operator) {
							changeOrder(orderIndex, order.getOrderId(), operator);
						}
					});
					break;
				case 1:
					ViewHolder1 vh1 = null;
					if (convertView == null) {
						convertView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout
								.shopsdk_default_item_myorder, null);
						vh1 = new ViewHolder1();
						vh1.myOrderSingleView = (MyOrderSingleView) convertView.findViewById(R.id.myOrderSingleView);
						convertView.setTag(vh1);
					} else {
						vh1 = (ViewHolder1) convertView.getTag();
					}
					vh1.myOrderSingleView.setData(orderCommodity);
					break;
			}

			convertView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					OrderDetailPage orderDetailPage = new OrderDetailPage(page.getTheme(), orderId);
					orderDetailPage.show(viewGroup.getContext(), null);
				}
			});

			return convertView;
		}

		@Override
		public long getItemId(int i) {
			return i;
		}

		@Override
		public Object getItem(int i) {
			return orderCommodities.get(i);
		}

		@Override
		public int getCount() {
			return totalCount;
		}

		private void getOrders() {
			OrderListQuerier querier = new OrderListQuerier(PAGE_SIZE, pageIndex, OrderStatus.ALL, Keywords);
			ShopSDK.getOrders(querier, new SGUIOperationCallback<List<Order>>(page.getCallback()) {
				@Override
				public void onSuccess(List<Order> data) {
					super.onSuccess(data);
					getParent().stopPulling();
					if (pageIndex == 1) {
						orders.clear();
						orderCommodities.clear();
						dividOrderCommodity.clear();
						dividOrderInfoCommodity.clear();
						orderCommoditiesOrderId.clear();
						totalCount = 0;
					}

					if (data == null || data.isEmpty()) {
						getParent().lockPullingUp();
					} else {
						pageIndex++;
						getParent().releasePullingUpLock();
					}

					orders.addAll(data);
					reworkOrder(data);
				}

				@Override
				public boolean onResultError(Throwable t) {
					getParent().stopPulling();
					return super.onResultError(t);
				}
			});
		}

		private void changeOrder(final int orderIndex, long orderId, final OrderOperator operator) {
			ShopSDK.modifyOrderStatus(orderId, operator, new SGUIOperationCallback<Void>(page.getCallback()) {
				@Override
				public void onSuccess(Void data) {
					super.onSuccess(data);
					switch (operator) {
						case DELETE:
							orderCommodities.clear();
							dividOrderCommodity.clear();
							dividOrderInfoCommodity.clear();
							orderCommoditiesOrderId.clear();
							totalCount = 0;
							orders.remove(orderIndex);
							reworkOrder(orders);
							break;
						case CANCEL:
							orders.get(orderIndex).setStatus(OrderStatus.CLOSED);
							notifyDataSetChanged();
							break;
						case CONFIRM:
							orders.get(orderIndex).setStatus(OrderStatus.COMPLETED);
							notifyDataSetChanged();
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


		private void reworkOrder(List<Order> orders) {
			for (int i = 0; i < orders.size(); i++) {
				Order order = orders.get(i);
				totalCount += order.getOrderCommodityList().size();
				orderCommodities.addAll(order.getOrderCommodityList());
				List<OrderCommodity> orderCommodityList = order.getOrderCommodityList();
				if (orderCommodityList != null && orderCommodityList.size() > 0) {
					for (int j = 0; j < orderCommodityList.size(); j++) {
						OrderCommodity orderCommodity = orderCommodityList.get(j);
						orderCommoditiesOrderId.put(orderCommodity.getOrderCommodityId(), order.getOrderId());
						if (j + 1 == orderCommodityList.size()) {
							dividOrderCommodity.put(orderCommodity.getOrderCommodityId(), orderCommodity
									.getOrderCommodityId());
							dividOrderInfoCommodity.put(orderCommodity.getOrderCommodityId(), i);
						}
					}
				}
			}
			notifyDataSetChanged();
		}

		private class ViewHolder1 {
			private MyOrderSingleView myOrderSingleView;
		}

		private class ViewHolder2 {
			private MyOrderView myOrderView;
		}
	}
}
