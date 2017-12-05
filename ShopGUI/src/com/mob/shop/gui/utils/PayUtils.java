package com.mob.shop.gui.utils;

import android.content.DialogInterface;

import com.mob.shop.OperationCallback;
import com.mob.shop.datatype.PayResult;
import com.mob.shop.datatype.builder.OrderPayBuilder;
import com.mob.shop.gui.base.Page;
import com.mob.shop.gui.pages.OrderDetailPage;
import com.mob.shop.gui.pages.PaySuccessPage;
import com.mob.shop.gui.pages.dialog.PaySelectDialog;
import com.mob.shop.gui.pages.dialog.ProgressDialog;
import com.mob.shop.gui.pay.PayManager;
import com.mob.tools.utils.ResHelper;


public class PayUtils {
    private Page page;
    private static volatile PayUtils instance = null;
    private ProgressDialog pd;

    private void PayUtils() {
    }

    public static PayUtils getInstance() {
        if (instance == null) {
            synchronized (PayUtils.class) {
                if (instance == null) {
                    instance = new PayUtils();
                }
            }
        }
        return instance;
    }

    public void payOrder(final Page page, final OrderPayBuilder orderPayBuilder, final boolean goDetail) {
        try {
            if (orderPayBuilder == null || page == null) {
                return;
            }
            this.page = page;
            PayManager.getInstance().setOperationCallback(new OperationCallback<PayResult>() {
                @Override
                public void onSuccess(PayResult payResult) {
                    super.onSuccess(payResult);
                    dissmissPd();
                    if (payResult == PayResult.SUCCESS) {
                        PaySuccessPage paySuccessPage = new PaySuccessPage(page.getTheme(), orderPayBuilder.orderId);
                        paySuccessPage.show(page.getContext(), null);
                        page.toastMessage(ResHelper.getStringRes(page.getContext(),
                                "shopsdk_default_pay_success"));
                        page.finish();
                    } else if(payResult == PayResult.CANCELED){
                        page.toastMessage(ResHelper.getStringRes(page.getContext(),
                                "shopsdk_default_pay_cancel"));
                        if (goDetail) {
                            gotoOrderDetial(orderPayBuilder.orderId);
                        }
                    } else {
                        page.toastMessage(ResHelper.getStringRes(page.getContext(),
                                "shopsdk_default_pay_failed"));
                        if (goDetail) {
                            gotoOrderDetial(orderPayBuilder.orderId);
                        }
                    }
                }

                @Override
                public void onFailed(Throwable t) {
                    super.onFailed(t);
                    dissmissPd();
                    page.toastMessage(ResHelper.getStringRes(page.getContext(),
                            "shopsdk_default_pay_failed"));
                    if (goDetail) {
                        gotoOrderDetial(orderPayBuilder.orderId);
                    }
                }
            });

            if (orderPayBuilder.freeOfCharge) {
                PayManager.getInstance().pay(page.getContext(), page.getTheme(), orderPayBuilder);
            } else {
                PaySelectDialog.Builder builder = new PaySelectDialog.Builder(page.getContext(), page.getTheme());
                builder.setPayEntity(orderPayBuilder);
                PaySelectDialog paySelectDialog = builder.create();
                paySelectDialog.setPayEntity(orderPayBuilder);
                paySelectDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        showPd();
                    }
                });
                paySelectDialog.setPayCancelError(new PaySelectDialog.PayCancelError() {
                    @Override
                    public void onCancel() {
                        if (goDetail) {
                            gotoOrderDetial(orderPayBuilder.orderId);
                        }
                    }

                    @Override
                    public void onError() {
                        page.toastMessage(ResHelper.getStringRes(page.getContext(),
                                "shopsdk_default_pay_failed"));
                        if (goDetail) {
                            gotoOrderDetial(orderPayBuilder.orderId);
                        }
                    }

                });
                paySelectDialog.show();
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    private void gotoOrderDetial(long orderId) {
        if (page == null) {
            return;
        }
        OrderDetailPage orderDetailPage = new OrderDetailPage(page.getTheme(), orderId);
        orderDetailPage.show(page.getContext(), null);
        page.finish();
    }

    public void showPd() {
        if(page == null){
            return;
        }
        if (pd == null) {
            pd = new ProgressDialog.Builder(page.getContext(), page.getTheme()).show();
        } else {
            if (pd.isShowing()) {
                pd.dismiss();
            }
            pd.show();
        }
    }

    public void dissmissPd() {
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
        pd = null;
    }
}
