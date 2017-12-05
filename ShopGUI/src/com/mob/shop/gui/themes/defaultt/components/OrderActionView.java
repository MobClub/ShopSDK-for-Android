package com.mob.shop.gui.themes.defaultt.components;


import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mob.shop.datatype.OrderOperator;
import com.mob.shop.datatype.builder.OrderPayBuilder;
import com.mob.shop.datatype.entity.Order;
import com.mob.shop.gui.R;
import com.mob.shop.gui.base.Page;
import com.mob.shop.gui.pages.AppraisePage;
import com.mob.shop.gui.pages.TransportMsgPage;
import com.mob.shop.gui.pages.dialog.OKCancelDialog;
import com.mob.shop.gui.utils.PayUtils;
import com.mob.tools.utils.ResHelper;

public class OrderActionView extends LinearLayout {
    private TextView tvActionLeft;
    private TextView tvActionCenter;
    private TextView tvActionRight;
    private Context context;
    private Page page;
    private MyOrderViewClickListener myOrderViewClickListener;
    private OnOrderOperatorListener onOrderOperatorListener;
    private Order order;
    private OKCancelDialog.Builder builder;

    public OrderActionView(Context context) {
        this(context, null);
    }

    public OrderActionView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OrderActionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.shopsdk_default_view_orderaction, null);
        addView(view);
        tvActionLeft = (TextView) view.findViewById(R.id.tvActionLeft);
        tvActionCenter = (TextView) view.findViewById(R.id.tvActionCenter);
        tvActionRight = (TextView) view.findViewById(R.id.tvActionRight);
        myOrderViewClickListener = new MyOrderViewClickListener();
    }

    public void setOrderStatus(Page page, Order order) {
        this.page = page;
        this.order = order;
        switch (order.getStatus()) {
            case CREATED_AND_WAIT_FOR_PAY:
            case ALL:
                setAction("", null, "shopsdk_default_cancel_order", myOrderViewClickListener, "shopsdk_default_pay",
                        myOrderViewClickListener);
                break;
            case PAID_AND_WAIT_FOR_DILIVER:
                setAction("", null, "", null, "", null);
                break;
            case DILIVERED_AND_WAIT_FOR_RECEIPT:
                setAction("", null, "shopsdk_default_check_logistics", myOrderViewClickListener,
                        "shopsdk_default_comfirm_shipping", myOrderViewClickListener);
                break;
            case COMPLETED:
                if (order.getCommentCount() == 0) {
                    setAction("shopsdk_default_evaluate", myOrderViewClickListener, "shopsdk_default_check_logistics",
                            myOrderViewClickListener, "shopsdk_default_delete_order", myOrderViewClickListener);
                } else {
                    setAction("", null, "shopsdk_default_check_logistics", myOrderViewClickListener,
                            "shopsdk_default_delete_order", myOrderViewClickListener);
                }
                break;
            case CLOSED:
                setAction("", null, "", null, "shopsdk_default_delete_order", myOrderViewClickListener);
                break;
        }
    }

    private void setAction(String leftRes, OnClickListener leftListener, String centerRes, OnClickListener
            centerListener, String rightRes, OnClickListener rightListener) {
        setTvAction(tvActionLeft, leftRes, leftListener);
        setTvAction(tvActionCenter, centerRes, centerListener);
        setTvAction(tvActionRight, rightRes, rightListener);
    }

    private void setTvAction(TextView tv, String txtRes, OnClickListener onClickListener) {
        if (TextUtils.isEmpty(txtRes)) {
            tv.setVisibility(GONE);
        } else {
            tv.setText(ResHelper.getStringRes(context, txtRes));
            tv.setVisibility(VISIBLE);
        }
        tv.setOnClickListener(onClickListener);
    }

    class MyOrderViewClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            if (v instanceof TextView) {
                String txt = ((TextView) v).getText().toString();
                String cancelOrder = context.getResources().getString(ResHelper.getStringRes(context,
                        "shopsdk_default_cancel_order"));
                final String payOrder = context.getResources().getString(ResHelper.getStringRes(context,
                        "shopsdk_default_pay"));
                String checkLogistics = context.getResources().getString(ResHelper.getStringRes(context,
                        "shopsdk_default_check_logistics"));
                String comfirmShipping = context.getResources().getString(ResHelper.getStringRes(context,
                        "shopsdk_default_comfirm_shipping"));
                String evaluate = context.getResources().getString(ResHelper.getStringRes(context,
                        "shopsdk_default_evaluate"));
                String deleteOrder = context.getResources().getString(ResHelper.getStringRes(context,
                        "shopsdk_default_delete_order"));
                if (cancelOrder.equals(txt)) {
                    int resId = ResHelper.getStringRes(page.getContext(), "shopsdk_default_cancel_order_tip");
                    showQueryDialog(resId, OrderOperator.CANCEL);
                } else if (payOrder.equals(txt)) {
                    OrderPayBuilder orderPayBuilder = new OrderPayBuilder();
                    orderPayBuilder.orderId = order.getOrderId();
                    orderPayBuilder.paidMoney = order.getPaidMoney();
                    if (order.getPaidMoney() <= 0) {
                        orderPayBuilder.freeOfCharge = true;
                    } else {
                        orderPayBuilder.freeOfCharge = false;
                    }
                    PayUtils.getInstance().payOrder(page, orderPayBuilder, false);
                } else if (checkLogistics.equals(txt)) {
                    TransportMsgPage transportMsgPage = new TransportMsgPage(page.getTheme(), order.getTransportId());
                    transportMsgPage.show(page.getContext(), null);
                } else if (comfirmShipping.equals(txt)) {
                    int resId = ResHelper.getStringRes(page.getContext(), "shopsdk_default_query_shipping_tip");
                    showQueryDialog(resId, OrderOperator.CONFIRM);
                } else if (evaluate.equals(txt)) {
                    AppraisePage appraisePage = new AppraisePage(page.getTheme(), order);
                    appraisePage.show(page.getContext(), null);
                } else if (deleteOrder.equals(txt)) {
                    int resId = ResHelper.getStringRes(page.getContext(), "shopsdk_default_delete_order_tip");
                    showQueryDialog(resId, OrderOperator.DELETE);
                }
            }
        }
    }

    private void showQueryDialog(int resId, final OrderOperator orderOperator) {
        if (builder == null) {
            builder = new OKCancelDialog.Builder(page.getContext(), page.getTheme());
            builder.noPadding();
        }
        builder.setMessage(String.format(page.getContext().getString(resId), String.valueOf(1)));
        builder.setOnClickListener(new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    orderOperator(orderOperator);
                }
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void orderOperator(OrderOperator orderOperator) {
        if (onOrderOperatorListener != null) {
            onOrderOperatorListener.onOrderOperator(orderOperator);
        }
    }

    public void setOnOrderOperatorListener(OnOrderOperatorListener onOrderOperatorListener) {
        this.onOrderOperatorListener = onOrderOperatorListener;
    }

    public interface OnOrderOperatorListener {
        void onOrderOperator(OrderOperator operator);
    }
}
