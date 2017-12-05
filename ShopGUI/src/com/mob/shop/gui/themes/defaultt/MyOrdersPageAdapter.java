package com.mob.shop.gui.themes.defaultt;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.mob.shop.ShopSDK;
import com.mob.shop.datatype.OrderOperator;
import com.mob.shop.datatype.OrderStatus;
import com.mob.shop.datatype.builder.OrderListQuerier;
import com.mob.shop.datatype.entity.Order;
import com.mob.shop.datatype.entity.OrderCommodity;
import com.mob.shop.gui.R;
import com.mob.shop.gui.SGUIOperationCallback;
import com.mob.shop.gui.base.Page;
import com.mob.shop.gui.pages.MyOrdersPage;
import com.mob.shop.gui.pages.OrderDetailPage;
import com.mob.shop.gui.pages.SearchOrderPage;
import com.mob.shop.gui.themes.defaultt.components.DefaultRTRListAdapter;
import com.mob.shop.gui.themes.defaultt.components.MyOrderSingleView;
import com.mob.shop.gui.themes.defaultt.components.MyOrderView;
import com.mob.shop.gui.themes.defaultt.components.OrderActionView;
import com.mob.shop.gui.themes.defaultt.components.ScrolledPullToRequestView;
import com.mob.shop.gui.themes.defaultt.components.TabsView;
import com.mob.shop.gui.themes.defaultt.components.TitleView;
import com.mob.tools.FakeActivity;
import com.mob.tools.gui.MobViewPager;
import com.mob.tools.gui.PullToRequestView;
import com.mob.tools.gui.ViewPagerAdapter;
import com.mob.tools.utils.ResHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 全部订单页
 */
public class MyOrdersPageAdapter extends DefaultThemePageAdapter<MyOrdersPage> {
	private String[] tabs = new String[]{"shopsdk_default_all", "shopsdk_default_unpay_order",
			"shopsdk_default_unSeed_order", "shopsdk_default_unShipping_order", "shopsdk_default_completed_order"};
	private OrderStatus[] tabStatus = new OrderStatus[]{OrderStatus.ALL, OrderStatus.CREATED_AND_WAIT_FOR_PAY,
			OrderStatus.PAID_AND_WAIT_FOR_DILIVER, OrderStatus.DILIVERED_AND_WAIT_FOR_RECEIPT, OrderStatus.COMPLETED};
	private MobViewPager mobViewPage;

	@Override
	public void onCreate(final MyOrdersPage page, Activity activity) {
		super.onCreate(page, activity);
		View view = LayoutInflater.from(page.getContext()).inflate(R.layout.shopsdk_default_act_myorder, null);
		activity.setContentView(view);

		TitleView titleView = (TitleView) view.findViewById(R.id.titleView);
		titleView.initTitleView(page, "shopsdk_default_myorder", "shopsdk_default_search", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SearchOrderPage searchOrderPage = new SearchOrderPage(page.getTheme());
				searchOrderPage.show(page.getContext(), null);
			}
		}, true, true);

		final TabsView tabView = (TabsView) view.findViewById(R.id.tabView);
		tabView.setDataRes(page.getContext(), tabs);
		tabView.setOnTabClickListener(new TabsView.OnTabClickListener() {
			@Override
			public void onTabClick(int index) {
				mobViewPage.scrollToScreen(index, true, true);
			}
		});

		mobViewPage = (MobViewPager) view.findViewById(R.id.MobViewPage);
		OrdersViewPagerAdapter viewPagerAdapter = new OrdersViewPagerAdapter(page, tabStatus);
		mobViewPage.setAdapter(viewPagerAdapter);
		viewPagerAdapter.setScreenChangeListener(new OrdersViewPagerAdapter.ScreenChangeListener() {
			@Override
			public void onScreenChange(int currentScreen, int lastScreen) {
				tabView.changeTab(currentScreen);
			}
		});

		mobViewPage.post(new Runnable() {
			@Override
			public void run() {
				tabView.performClick(page.getStatus());
			}
		});
	}

	private static class OrdersViewPagerAdapter extends ViewPagerAdapter {

		private OrderStatus[] tabStatus;
		private MyOrdersPage page;
		private ScreenChangeListener screenChangeListener;
		private HashMap<String, ScrolledPullToRequestView> viewMap = new HashMap<String, ScrolledPullToRequestView>();

		public OrdersViewPagerAdapter(MyOrdersPage page, OrderStatus[] tabStatus) {
			this.page = page;
			this.tabStatus = tabStatus;
		}

		public void setScreenChangeListener(ScreenChangeListener screenChangeListener) {
			this.screenChangeListener = screenChangeListener;
		}

		public int getCount() {
			if (tabStatus != null) {
				return tabStatus.length;
			}
			return 5;
		}

		public void onScreenChange(int currentScreen, int lastScreen) {
			super.onScreenChange(currentScreen, lastScreen);
			if (screenChangeListener != null) {
				screenChangeListener.onScreenChange(currentScreen, lastScreen);
			}
		}

		public View getView(int index, View convertView, ViewGroup parent) {
			ScrolledPullToRequestView pullToRequestView = viewMap.get(String.valueOf(tabStatus[index].getValue()));
			if (pullToRequestView == null) {
				ScrolledPullToRequestView requestView = createPanel(parent.getContext(), tabStatus[index]);
				convertView = requestView;
				viewMap.put(String.valueOf(tabStatus[index].getValue()), requestView);
			} else {
				convertView = pullToRequestView;
			}
			return convertView;
		}

		//创建下拉刷新列表
		private ScrolledPullToRequestView createPanel(Context context, OrderStatus orderStatus) {
			ScrolledPullToRequestView requestView = new ScrolledPullToRequestView(context);
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			requestView.setLayoutParams(lp);
			MyOrdersAdapter pullRequestAdapter = new MyOrdersAdapter(page, requestView, orderStatus);
			pullRequestAdapter.setEmptyRes(context, "shopsdk_default_empty_order_img",
					"shopsdk_default_empty_order_tip");
			requestView.setAdapter(pullRequestAdapter);
			requestView.performPullingDown(true);
			return requestView;
		}

		/**
		 * 页面滑动监听
		 */
		public interface ScreenChangeListener {
			void onScreenChange(int currentScreen, int lastScreen);
		}

		private static class MyOrdersAdapter extends DefaultRTRListAdapter {
			private List<Order> orders = new ArrayList<Order>();
			private Page page;
			private static final int PAGE_SIZE = 20;
			private int pageIndex = 1;
			private OrderStatus orderStatus;
			private int totalCount = 0;
			private List<OrderCommodity> orderCommodities = new ArrayList<OrderCommodity>();
			private HashMap<Long, Long> dividOrderCommodity = new HashMap<Long, Long>();
			private HashMap<Long, Integer> dividOrderInfoCommodity = new HashMap<Long, Integer>();
			private HashMap<Long, Long> orderCommoditiesOrderId = new HashMap<Long, Long>();

			public MyOrdersAdapter(Page page, PullToRequestView view, OrderStatus orderStatus) {
				super(view);
				this.page = page;
				this.orderStatus = orderStatus;
				getListView().setDividerHeight(0);
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
							vh1.myOrderSingleView = (MyOrderSingleView) convertView.findViewById(R.id
									.myOrderSingleView);
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
						orderDetailPage.showForResult(viewGroup.getContext(), null, new FakeActivity() {
							@Override
							public void onResult(HashMap<String, Object> data) {
								super.onResult(data);
								if (data != null) {
									onRequest(true);
								}
							}
						});
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
				OrderListQuerier querier = new OrderListQuerier(PAGE_SIZE, pageIndex, orderStatus, "");
				ShopSDK.getOrders(querier, new SGUIOperationCallback<List<Order>>(page.getCallback()) {
					@Override
					public void onSuccess(List<Order> data) {
						super.onSuccess(data);
						if (pageIndex == 1) {
							orders.clear();
							orderCommodities.clear();
							dividOrderCommodity.clear();
							dividOrderInfoCommodity.clear();
							orderCommoditiesOrderId.clear();
							totalCount = 0;
						}

						orders.addAll(data);
						reworkOrder(data);

						if (data == null || data.isEmpty()) {
							getParent().lockPullingUp();
						} else {
							pageIndex++;
							getParent().releasePullingUpLock();
						}
					}

					@Override
					public void onFailed(Throwable t) {
						super.onFailed(t);
						getParent().stopPulling();
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
					if (order != null && order.getOrderCommodityList() != null) {
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
}
