package com.mob.shop.gui.themes.defaultt;


import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mob.shop.ShopSDK;
import com.mob.shop.datatype.PreOrderStatus;
import com.mob.shop.datatype.builder.OrderInfoBuilder;
import com.mob.shop.datatype.builder.OrderPayBuilder;
import com.mob.shop.datatype.builder.PreOrderInfoBuilder;
import com.mob.shop.datatype.entity.BaseBuyingItem;
import com.mob.shop.datatype.entity.BaseCoupon;
import com.mob.shop.datatype.entity.Order;
import com.mob.shop.datatype.entity.OrderCommodity;
import com.mob.shop.datatype.entity.OrderCoupon;
import com.mob.shop.datatype.entity.PreOrder;
import com.mob.shop.datatype.entity.ShippingAddr;
import com.mob.shop.gui.R;
import com.mob.shop.gui.SGUIOperationCallback;
import com.mob.shop.gui.pages.OrderComfirmPage;
import com.mob.shop.gui.pages.SelectUseCouponsPage;
import com.mob.shop.gui.pages.ShippingAddressPage;
import com.mob.shop.gui.pages.dialog.ProgressDialog;
import com.mob.shop.gui.themes.defaultt.components.OrderProductView;
import com.mob.shop.gui.themes.defaultt.components.OrderShippingAddressView;
import com.mob.shop.gui.themes.defaultt.components.TitleView;
import com.mob.shop.gui.utils.MoneyConverter;
import com.mob.shop.gui.utils.PayUtils;
import com.mob.tools.FakeActivity;
import com.mob.tools.utils.ResHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 订单确认页
 */
public class OrderComfirmPageAdapter extends DefaultThemePageAdapter<OrderComfirmPage> implements View.OnClickListener {
    private static final String TAG = OrderComfirmPageAdapter.class.getSimpleName();
    public static final String EXTRA_PREORDER = "extra_preOrder";
    private ListView listView;
    private TitleView titleView;
    private OrderComfirmAdapter adapter;
    private PreOrder preOrder;
    private OrderShippingAddressView orderShippingAddressView;
    private OrderComfirmPage page;
    private LinearLayout llAdd;
    private TextView tvFreight;
    private TextView tvProductMoney;
    private TextView tvActualMoney;
    private TextView tvCoupon;
    private TextView tvCommit;
    private EditText etBuyerMessage;
    private LinearLayout llCoupon;
    private ShippingAddr shippingAddr;
    private PreOrderInfoBuilder preOrderInfoBuilder;
    private ProgressDialog pd;

    @Override
    public void onCreate(OrderComfirmPage page, Activity activity) {
        super.onCreate(page, activity);
        this.page = page;
        View view = LayoutInflater.from(page.getContext()).inflate(R.layout.shopsdk_default_page_orderfirm, null);
        activity.setContentView(view);
        titleView = (TitleView) view.findViewById(R.id.titleView);
        titleView.initTitleView(page, "shopsdk_default_comfirm_order", null, null, true);
        listView = (ListView) view.findViewById(R.id.listView);
        tvActualMoney = (TextView) view.findViewById(R.id.tvActualMoney);
        tvCommit = (TextView) view.findViewById(R.id.tvCommit);
        View header = LayoutInflater.from(page.getContext()).inflate(R.layout.shopsdk_default_page_orderfirm_header,
                null);
        View footer = LayoutInflater.from(page.getContext()).inflate(R.layout.shopsdk_default_page_orderfirm_footer,
                null);

        tvFreight = (TextView) footer.findViewById(R.id.tvFreight);
        tvProductMoney = (TextView) footer.findViewById(R.id.tvProductMoney);
        tvCoupon = (TextView) footer.findViewById(R.id.tvCoupon);
        etBuyerMessage = (EditText) footer.findViewById(R.id.etBuyerMessage);
        llCoupon = (LinearLayout) footer.findViewById(R.id.llCoupon);

        llAdd = (LinearLayout) header.findViewById(R.id.llAdd);
        orderShippingAddressView = (OrderShippingAddressView) header.findViewById(R.id.orderShippingAddressView);
        orderShippingAddressView.setIvMore(true);
        orderShippingAddressView.setOnClickListener(this);
        llAdd.setOnClickListener(this);
        tvCommit.setOnClickListener(this);
        llCoupon.setOnClickListener(this);

        /* 必须在setAdapter之前执行addHeaderView方法，否则会出现以下异常：
         * java.lang.IllegalStateException: Cannot add header view to list -- setAdapter has already been called.
		 */
        listView.addHeaderView(header);
        listView.addFooterView(footer);

        adapter = new OrderComfirmAdapter();
        listView.setAdapter(adapter);

        initPreOrder();
    }

    private void showPd() {
        if (pd == null) {
            pd = new ProgressDialog.Builder(page.getContext(), page.getTheme()).show();
        } else {
            if (pd.isShowing()) {
                pd.dismiss();
            }
            pd.show();
        }
    }

    private void dissmissPd() {
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
    }

    private void initPreOrder() {
        preOrder = page.getPreOrder();
        if (preOrder != null) {
            adapter.setList(preOrder.getOrderCommodityList());

            tvProductMoney.setText("￥" + MoneyConverter.conversionString(preOrder.getTotalMoney()));
            tvActualMoney.setText(MoneyConverter.conversionString(preOrder.getPaidMoney()));
            ArrayList<Long> coupons = new ArrayList<Long>();
            if (preOrder.getCouponsList() == null || preOrder.getCouponsList().size() == 0) {
                tvCoupon.setText(getString("shopsdk_default_hint_coupon"));
            } else {
                tvCoupon.setText("￥" + MoneyConverter.conversionString(preOrder.getTotalCoupon()));
                for (BaseCoupon baseCoupon : preOrder.getCouponsList()) {
                    coupons.add(baseCoupon.getCouponId());
                }
            }
            checkPreOrderStatus();

            List<BaseBuyingItem> list = new ArrayList<BaseBuyingItem>();
            List<OrderCommodity> orderCommodityList = preOrder.getOrderCommodityList();
            if (orderCommodityList != null) {
                for (OrderCommodity cartCommodity : orderCommodityList) {
                    BaseBuyingItem baseBuyingItem = new BaseBuyingItem();
                    baseBuyingItem.setCommodityId(cartCommodity.getCommodityId());
                    baseBuyingItem.setCount(cartCommodity.getCount());
                    list.add(baseBuyingItem);
                }
            }
            if (preOrderInfoBuilder == null) {
                preOrderInfoBuilder = new PreOrderInfoBuilder(shippingAddr == null ? 0 : shippingAddr
                        .getShippingAddrId(), preOrder.getTotalMoney(), preOrder.getTotalCount(), coupons, true, list);
            }

            if (preOrder.getStatus() != PreOrderStatus.NORMAL) {
                tvCommit.setBackgroundResource(ResHelper.getBitmapRes(page.getContext(),
                        "shopsdk_default_rectangle_deep_grey_bg"));
                tvCommit.setEnabled(false);
            } else {
                tvCommit.setBackgroundResource(ResHelper.getBitmapRes(page.getContext(), "shopsdk_default_title_bg"));
                tvCommit.setEnabled(true);
            }
        }
    }

    private void checkPreOrderStatus() {
        if (preOrder.getStatus() == PreOrderStatus.SHIPPING_ADDR_ABNORMAL) {
            llAdd.setVisibility(View.VISIBLE);
            orderShippingAddressView.setVisibility(View.GONE);
            tvFreight.setText("-");
        } else {
            shippingAddr = preOrder.getShippingAddrInfo();
            orderShippingAddressView.initView(preOrder.getShippingAddrInfo(), true);
            orderShippingAddressView.setVisibility(View.VISIBLE);
            llAdd.setVisibility(View.GONE);
            tvFreight.setText(MoneyConverter.conversionMoneyStr(preOrder.getTotalFreight()));
        }
    }

    @Override
    public void onClick(View v) {
        if (ShopSDK.getUser().isAnonymous()) {
            if (getPage().getCallback() != null) {
                getPage().getCallback().login();
            }
            return;
        }
        if (v.getId() == R.id.orderShippingAddressView || v.getId() == R.id.llAdd) {
            ShippingAddressPage addressPage = new ShippingAddressPage(getPage().getTheme(), shippingAddr == null ? 0 : shippingAddr.getShippingAddrId());
            addressPage.showForResult(getPage().getContext(), null, new FakeActivity() {
                @Override
                public void onResult(HashMap<String, Object> data) {
                    super.onResult(data);
                    if (data != null) {
                        if (data.containsKey("newAddr")) {
                            Object newAddr = data.get("newAddr");
                            if (newAddr instanceof ShippingAddr) {
                                shippingAddr = (ShippingAddr) newAddr;
                                orderShippingAddressView.initView(shippingAddr, true);
                                if (orderShippingAddressView.getVisibility() != View.VISIBLE) {
                                    orderShippingAddressView.setVisibility(View.VISIBLE);
                                    llAdd.setVisibility(View.GONE);
                                }
                                preOrderInfoBuilder.shippingAddrId = shippingAddr.getShippingAddrId();

                            }
                        } else {
                            preOrderInfoBuilder.shippingAddrId = 0;
                        }
                        preOrder();

                    }
                }
            });
        } else if (v.getId() == R.id.tvCommit) {
            createOrder();
        } else if (v.getId() == R.id.llCoupon) {
            selectCoupon();
        }
    }

    private void preOrder() {
        if (ShopSDK.getUser().isAnonymous()) {
            if (page.getCallback() != null) {
                page.getCallback().login();
            }
            return;
        }
        if (preOrder == null) {
            return;
        }
        if (preOrderInfoBuilder == null) {
            return;
        }
        if (shippingAddr == null) {
            page.toastMessage(ResHelper.getStringRes(page.getContext(),
                    "shopsdk_default_select_shipping_address_hint"));
            return;
        }
        showPd();
        ShopSDK.preOrder(preOrderInfoBuilder, new SGUIOperationCallback<PreOrder>(page.getCallback()) {
            @Override
            public void onSuccess(PreOrder data) {
                super.onSuccess(data);
                dissmissPd();
                page.setPreOrder(data);
                preOrder = data;
                initPreOrder();
            }

            @Override
            public void onFailed(Throwable t) {
                dissmissPd();
                super.onFailed(t);
            }

            @Override
            public boolean onResultError(Throwable t) {
                page.toastMessage(ResHelper.getStringRes(page.getContext(), "shopsdk_default_precreate_order_failed"));
                return super.onResultError(t);
            }
        });
    }

    private void createOrder() {
        if (preOrder == null) {
            return;
        }
        showPd();
        final OrderInfoBuilder orderInfoBuilder = new OrderInfoBuilder(preOrder, etBuyerMessage.getText().toString().trim(), null);
        ShopSDK.createOrder(orderInfoBuilder, new SGUIOperationCallback<Order>(page.getCallback()) {
            @Override
            public void onSuccess(final Order data) {
                super.onSuccess(data);
                dissmissPd();
                if (data == null) {
                    return;
                }
                getPage().setResult(new HashMap<String, Object>());

                OrderPayBuilder orderPayBuilder = new OrderPayBuilder();
                orderPayBuilder.orderId = data.getOrderId();
                orderPayBuilder.paidMoney = data.getPaidMoney();
                if (data.getPaidMoney() <= 0) {
                    orderPayBuilder.freeOfCharge = true;
                    orderPayBuilder.payWay = null;
                } else {
                    orderPayBuilder.freeOfCharge = false;
                }
                PayUtils.getInstance().payOrder(page, orderPayBuilder, true);
            }

            @Override
            public void onFailed(Throwable t) {
                dissmissPd();
                super.onFailed(t);
            }

            @Override
            public boolean onResultError(Throwable t) {
                page.toastMessage(ResHelper.getStringRes(page.getContext(), "shopsdk_default_order_failed"));
                return super.onResultError(t);
            }
        });
    }

    private void selectCoupon() {
        SelectUseCouponsPage selectUseCouponsPage = new SelectUseCouponsPage(page.getTheme());
        Intent i = new Intent();
        i.putExtra(EXTRA_PREORDER, preOrder);
        selectUseCouponsPage.showForResult(page.getContext(), i, new FakeActivity() {
            @Override
            public void onResult(HashMap<String, Object> data) {
                super.onResult(data);
                ArrayList<OrderCoupon> selected = new ArrayList<OrderCoupon>();
                if (data != null) {
                    selected = (ArrayList<OrderCoupon>) data.get(SelectUseCouponsPageAdapter.RESULT_SELECTED_COUPONS);
                }
                if (selected == null) {
                    return;
                }
                ArrayList<Long> coupons = new ArrayList<Long>();
                for (OrderCoupon orderCoupon : selected) {
                    coupons.add(orderCoupon.getCouponId());
                }
                preOrderInfoBuilder.couponList = coupons;
                preOrderInfoBuilder.enableCoupon = coupons.size() == 0 ? false : true;
                preOrder();
            }
        });
    }

    @Override
    public void onDestroy(OrderComfirmPage page, Activity activity) {
        super.onDestroy(page, activity);
        if (pd != null) {
            if (pd.isShowing()) {
                pd.dismiss();
            }
            pd = null;
        }
    }

    private class OrderComfirmAdapter extends BaseAdapter {
        private List<OrderCommodity> list = new ArrayList<OrderCommodity>();

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
            if (list != null) {
                this.list = list;
            }
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder vh;
            if (convertView == null) {
                OrderProductView orderProductView = new OrderProductView(parent.getContext());
                AbsListView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                orderProductView.setLayoutParams(lp);
                convertView = orderProductView;
                vh = new ViewHolder();
                vh.orderProductView = orderProductView;
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            OrderCommodity orderCommodity = list.get(position);
            vh.orderProductView.initData(orderCommodity.getImgUrl(), orderCommodity.getProductName(), String.valueOf
                    (orderCommodity.getCount()), orderCommodity.getPropertyDescribe(), MoneyConverter.conversionString
                    (orderCommodity.getCurrentCost()), false, orderCommodity.getStatus(), position + 1 == list.size()
                    ? true : false);
            return convertView;
        }

        class ViewHolder {
            private OrderProductView orderProductView;
        }
    }
}
