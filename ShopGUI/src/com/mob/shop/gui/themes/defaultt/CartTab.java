package com.mob.shop.gui.themes.defaultt;


import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mob.shop.ShopSDK;
import com.mob.shop.biz.ShopLog;
import com.mob.shop.datatype.CommodityStatus;
import com.mob.shop.datatype.builder.PreOrderInfoBuilder;
import com.mob.shop.datatype.entity.BaseBuyingItem;
import com.mob.shop.datatype.entity.CartCommodity;
import com.mob.shop.datatype.entity.CartCommodityItem;
import com.mob.shop.datatype.entity.Commodity;
import com.mob.shop.datatype.entity.PreOrder;
import com.mob.shop.datatype.entity.Product;
import com.mob.shop.gui.R;
import com.mob.shop.gui.SGUIOperationCallback;
import com.mob.shop.gui.base.Page;
import com.mob.shop.gui.pages.CommodityDetailShowPage;
import com.mob.shop.gui.pages.OrderComfirmPage;
import com.mob.shop.gui.pages.dialog.ChoiceProductAttrDialog;
import com.mob.shop.gui.pages.dialog.OKCancelDialog;
import com.mob.shop.gui.tabs.Tab;
import com.mob.shop.gui.themes.defaultt.components.CountView;
import com.mob.shop.gui.themes.defaultt.components.DefaultRTRListAdapter;
import com.mob.shop.gui.themes.defaultt.components.TitleView;
import com.mob.shop.gui.utils.MoneyConverter;
import com.mob.tools.FakeActivity;
import com.mob.tools.gui.AsyncImageView;
import com.mob.tools.gui.PullToRequestView;
import com.mob.tools.utils.ResHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 购物车Tab
 */
public class CartTab implements Tab, View.OnClickListener {
    private static final String TAG = "CartTab";
    private TitleView titleView;
    private Page page;
    private CheckBox checkBox;
    private TextView tvMoney;
    private TextView tvTotal;
    private TextView tvDelete;
    private TextView tvSettlement;
    private LinearLayout llCart;
    private View bLine;
    private PullToRequestView listView;
    private CartAdapter cartAdapter;
    private int checkedCount = 0;
    private boolean isBack = false;
    private String totalMoney = "0";

    public CartTab(Page page, boolean isBack) {
        this.page = page;
        this.isBack = isBack;
    }

    @Override
    public String getSelectedIconResName() {
        return "shopsdk_default_orange_cart";
    }

    @Override
    public String getUnselectedIconResName() {
        return "shopsdk_default_grey_cart";
    }

    @Override
    public String getTitleResName() {
        return "shopsdk_default_cart";
    }

    @Override
    public String getSelectedTitleColor() {
        return "select_tab";
    }

    @Override
    public String getUnselectedTitleColor() {
        return "unselect_tab";
    }

    @Override
    public View getTabView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.shopsdk_default_tab_cart, null);
        findView(view);
        initView();
        initListener();
        return view;
    }

    private void findView(View view) {
        listView = (PullToRequestView) view.findViewById(R.id.listView);
        checkBox = (CheckBox) view.findViewById(R.id.checkBox);
        tvMoney = (TextView) view.findViewById(R.id.tvMoney);
        tvTotal = (TextView) view.findViewById(R.id.tvTotal);
        tvDelete = (TextView) view.findViewById(R.id.tvDelete);
        tvSettlement = (TextView) view.findViewById(R.id.tvSettlement);
        titleView = (TitleView) view.findViewById(R.id.titleView);
        llCart = (LinearLayout) view.findViewById(R.id.llCart);
        bLine = view.findViewById(R.id.bLine);
    }

    private void initView() {
        titleView.initTitleView(page, "shopsdk_default_cart", "shopsdk_default_edit", new RightClickListener(),
                isBack, false);
        cartAdapter = new CartAdapter(listView, page);
        cartAdapter.setEmptyRes(page.getContext(), "shopsdk_default_empty_cart_img", "shopsdk_default_empty_cart_tip");
        listView.setAdapter(cartAdapter);
        listView.performPullingDown(true);
    }

    private void initListener() {
        cartAdapter.setOnTotalChangeListener(new OnTotalChangeListener() {
            @Override
            public void onTotalChange(String money, int count) {
                checkedCount = count;
                totalMoney = money;
                tvMoney.setText("￥" + totalMoney);
                String format = page.getContext().getString(ResHelper.getStringRes(page.getContext(),
                        "shopsdk_default_goto_settlement"));
                tvSettlement.setText(String.format(format, count));
                if (cartAdapter.getCount() == 0 && bLine.getVisibility() == View.VISIBLE) {
                    bLine.setVisibility(View.GONE);
                    llCart.setVisibility(View.GONE);
                    setEdit();
                }

                if (count == cartAdapter.getCount() - cartAdapter.getInvalidCount() && !checkBox.isChecked()) {
                    checkBox.setChecked(true);
                } else if (count != cartAdapter.getCount() - cartAdapter.getInvalidCount() && checkBox.isChecked()) {
                    checkBox.setChecked(false);
                }
            }
        });
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartAdapter.selectAll(checkBox.isChecked());
            }
        });
        tvDelete.setOnClickListener(this);
        tvSettlement.setOnClickListener(this);
    }

    @Override
    public void onSelected() {
        ShopLog.getInstance().d(TAG, "onSelected");
    }

    @Override
    public void onUnselected() {
        ShopLog.getInstance().d(TAG, "onUnselected");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvDelete) {
            OKCancelDialog.Builder builder = new OKCancelDialog.Builder(page.getContext(), page.getTheme());
            int resId = ResHelper.getStringRes(page.getContext(), "shopsdk_default_delete_cart_tip");
            builder.setMessage(String.format(page.getContext().getString(resId), String.valueOf(cartAdapter
                    .getCheckList().size())));
            builder.noPadding();
            builder.setOnClickListener(new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if (which == DialogInterface.BUTTON_POSITIVE) {
                        cartAdapter.delete();
                    }
                    dialog.dismiss();
                }
            });
            builder.show();
        } else if (v.getId() == R.id.tvSettlement) {
            if (!ShopSDK.getUser().isAnonymous()) {
                preOrder();
            } else {
                if (page.getCallback() != null) {
                    page.getCallback().login();
                }
            }
        }
    }

    private void preOrder() {
        final PreOrderInfoBuilder preOrderInfoBuilder = new PreOrderInfoBuilder();
        preOrderInfoBuilder.shippingAddrId = 0;
        preOrderInfoBuilder.totalMoney = MoneyConverter.toCent(totalMoney);
        preOrderInfoBuilder.enableCoupon = true;
        List<BaseBuyingItem> list = new ArrayList<BaseBuyingItem>();
        List<CartCommodity> cartCommodityList = cartAdapter.getCheckList();
        if (cartCommodityList != null) {
            for (CartCommodity cartCommodity : cartCommodityList) {
                BaseBuyingItem baseBuyingItem = new BaseBuyingItem();
                baseBuyingItem.setCommodityId(cartCommodity.getCommodityId());
                baseBuyingItem.setCount(cartCommodity.getCount());
                list.add(baseBuyingItem);
            }
        }
        preOrderInfoBuilder.totalCount = list.size();
        preOrderInfoBuilder.orderCommodityList = list;
        ShopSDK.preOrder(preOrderInfoBuilder, new SGUIOperationCallback<PreOrder>(page.getCallback()) {
            @Override
            public void onSuccess(PreOrder data) {
                super.onSuccess(data);
                OrderComfirmPage orderComfirmPage = new OrderComfirmPage(page.getTheme(), data, page.getCallback());
                orderComfirmPage.showForResult(page.getContext(), null, new FakeActivity() {
                    @Override
                    public void onResult(HashMap<String, Object> data) {
                        super.onResult(data);
                        if (data != null) {
                            cartAdapter.getList().removeAll(cartAdapter.getCheckList());
                            cartAdapter.clearCartList();
                        }
                    }
                });
            }

            @Override
            public boolean onResultError(Throwable t) {
                page.toastMessage(ResHelper.getStringRes(page.getContext(), "shopsdk_default_preorder_failed_title"));
                return super.onResultError(t);
            }
        });
    }

    private class RightClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v instanceof TextView) {
                String edit = page.getContext().getResources().getString(ResHelper.getStringRes(page.getContext(),
                        "shopsdk_default_edit"));
                String complete = page.getContext().getResources().getString(ResHelper.getStringRes(page.getContext(),
                        "shopsdk_default_complete"));
                if (edit.equals(((TextView) v).getText().toString())) {
                    setUnedit();
                } else if (complete.equals(((TextView) v).getText().toString())) {
                    setEdit();
                }
            }

        }
    }

    private void setUnedit() {
        titleView.setRight("shopsdk_default_complete");
        tvDelete.setVisibility(View.VISIBLE);
        tvSettlement.setVisibility(View.GONE);
        tvTotal.setVisibility(View.GONE);
        tvMoney.setVisibility(View.GONE);
        cartAdapter.edit(true);
    }

    private void setEdit() {
        cartAdapter.edit(false);
    }

    private void updateEdit() {
        titleView.setRight("shopsdk_default_edit");
        tvDelete.setVisibility(View.GONE);
        tvSettlement.setVisibility(View.VISIBLE);
        tvTotal.setVisibility(View.VISIBLE);
        tvMoney.setVisibility(View.VISIBLE);
    }

    private class CartAdapter extends DefaultRTRListAdapter {
        private List<CartCommodity> list = new ArrayList<CartCommodity>();
        private boolean edit;
        private boolean isModifyCount = false;
        private Page page;
        private boolean selectAll;
        private List<CartCommodity> checkList = new ArrayList<CartCommodity>();
        private HashMap<Long, CartCommodity> checkMap = new HashMap<Long, CartCommodity>();
        private List<CartCommodity> modifyList = new ArrayList<CartCommodity>();
        private HashMap<Integer, CartCommodity> modifyMap = new HashMap<Integer, CartCommodity>();
        private HashMap<Long, Integer> initCountMap = new HashMap<Long, Integer>();
        private HashMap<Long, CartCommodity> listMap = new HashMap<Long, CartCommodity>();
        private int invalidCount = 0;

        private OnTotalChangeListener onTotalChangeListener;

        public CartAdapter(PullToRequestView view, Page page) {
            super(view);
            this.page = page;
            getListView().setDividerHeight(0);
        }

        @Override
        protected void onRequest(boolean firstPage) {
            clearCartList();
            getCartList();
        }

        @Override
        public View getView(final int i, View convertView, ViewGroup viewGroup) {
            final ViewHolder vh;
            if (convertView == null) {
                convertView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shopsdk_default_item_cart,
                        null);
                vh = new ViewHolder();
                vh.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
                vh.iv = (AsyncImageView) convertView.findViewById(R.id.iv);
                vh.tvName = (TextView) convertView.findViewById(R.id.tvName);
                vh.tvAttribute = (TextView) convertView.findViewById(R.id.tvAttribute);
                vh.sAttribute = (TextView) convertView.findViewById(R.id.sAttribute);
                vh.tvPrice = (TextView) convertView.findViewById(R.id.tvPrice);
                vh.tvCount = (TextView) convertView.findViewById(R.id.tvCount);
                vh.countView = (CountView) convertView.findViewById(R.id.countView);
                vh.llEdit = (RelativeLayout) convertView.findViewById(R.id.llEdit);
                vh.rlOrder = (RelativeLayout) convertView.findViewById(R.id.rlOrder);
                vh.tvInvalid = (TextView) convertView.findViewById(R.id.tvInvalid);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }

            final CartCommodity cartCommodity = list.get(i);
            vh.tvName.setText(cartCommodity.getProductName());
            ViewGroup.LayoutParams lp = vh.iv.getLayoutParams();
            vh.iv.setCompressOptions(lp.width,lp.width,80,10240L);
            vh.iv.execute(cartCommodity.getImgUrl().getSrc(), ResHelper.getColorRes(viewGroup.getContext(),
                    "order_bg"));
            vh.tvPrice.setText("￥" + MoneyConverter.conversionString(cartCommodity.getCurrentCost()));
            if (TextUtils.isEmpty(cartCommodity.getPropertyDescribe())) {
                vh.sAttribute.setVisibility(View.INVISIBLE);
                vh.sAttribute.setEnabled(false);
            } else {
                vh.sAttribute.setText(cartCommodity.getPropertyDescribe());
                vh.sAttribute.setVisibility(View.VISIBLE);
                vh.sAttribute.setEnabled(true);
            }
            vh.tvCount.setText("x" + cartCommodity.getCount());
            vh.countView.setCounts(cartCommodity.getCount(), cartCommodity.getUsableStock(), 1, true);
            if (cartCommodity.getStatus() == CommodityStatus.NORMAL) {
                vh.checkBox.setVisibility(View.VISIBLE);
                vh.tvInvalid.setVisibility(View.GONE);
                vh.tvName.setTextColor(0xFF000000);
                vh.tvAttribute.setTextColor(0xFF000000);
                vh.tvAttribute.setText(cartCommodity.getPropertyDescribe());
            } else {
                vh.checkBox.setVisibility(View.GONE);
                vh.tvInvalid.setVisibility(View.VISIBLE);
                vh.tvName.setTextColor(0xFF999999);
                vh.tvAttribute.setTextColor(0xFF999999);
                if (cartCommodity.getStatus() == CommodityStatus.OFF_SHELF) {
                    vh.tvAttribute.setText(ResHelper.getStringRes(viewGroup.getContext(), "shopsdk_default_off_shelf"));
                } else if (cartCommodity.getStatus() == CommodityStatus.OUT_OF_STOCK) {
                    vh.tvAttribute.setText(ResHelper.getStringRes(viewGroup.getContext(), "shopsdk_default_out_of_stock"));
                } else {
                    vh.tvAttribute.setText(ResHelper.getStringRes(viewGroup.getContext(), "shopsdk_default_unavailable"));
                }
            }

            vh.countView.setOnCountClickListener(new CountView.OnCountClickListener() {
                @Override
                public void onFailed(String msg) {
                    page.toastMessage(msg);
                }

                @Override
                public void changeCount(int count) {
                    isModifyCount = true;
                    cartCommodity.setCount(count);
                    if (modifyMap.containsKey(i)) {
                        modifyList.remove(modifyMap.get(i));
                        modifyList.add(cartCommodity);
                        modifyMap.put(i, cartCommodity);
                    } else {
                        modifyMap.put(i, cartCommodity);
                        modifyList.add(cartCommodity);
                    }
                    totalChange();
                }
            });

            if (edit) {
                vh.llEdit.setVisibility(View.VISIBLE);
                vh.rlOrder.setVisibility(View.GONE);
            } else {
                vh.llEdit.setVisibility(View.GONE);
                vh.rlOrder.setVisibility(View.VISIBLE);
            }

            if (selectAll) {
                vh.checkBox.setChecked(true);
            } else {
                if (checkMap.containsKey(cartCommodity.getCartCommodityId())) {
                    vh.checkBox.setChecked(true);
                } else {
                    vh.checkBox.setChecked(false);
                }
            }
            vh.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkMap.containsKey(cartCommodity.getCartCommodityId())) {
                        vh.checkBox.setChecked(false);
                        selectAll = false;
                        checkList.remove(cartCommodity);
                        checkMap.remove(cartCommodity.getCartCommodityId());
                    } else {
                        vh.checkBox.setChecked(true);
                        checkList.add(cartCommodity);
                        checkMap.put(cartCommodity.getCartCommodityId(), cartCommodity);
                    }

                    totalChange();
                }
            });
            vh.sAttribute.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(cartCommodity.getPropertyDescribe())) {
                        getProductAttr(cartCommodity, vh.sAttribute, i);
                    }
                }
            });

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cartCommodity.getStatus() == CommodityStatus.NORMAL) {
                        Product product = new Product();
                        product.setProductId(cartCommodity.getProductId());
                        CommodityDetailShowPage commodityDetailShowPage = new CommodityDetailShowPage(page.getTheme(),
                                product);
                        commodityDetailShowPage.show(page.getContext(), null);
                    }
                }
            });
            convertView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    OKCancelDialog.Builder builder = new OKCancelDialog.Builder(page.getContext(), page.getTheme());
                    int resId = ResHelper.getStringRes(page.getContext(), "shopsdk_default_delete_cart_tip");
                    builder.setMessage(String.format(page.getContext().getString(resId), String.valueOf(1)));
                    builder.noPadding();
                    builder.setOnClickListener(new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == DialogInterface.BUTTON_POSITIVE) {
                                List<Long> invaldList = new ArrayList<Long>();
                                invaldList.add(cartCommodity.getCartCommodityId());
                                deleteCart(invaldList, false);
                            }
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                    return false;
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
            return list.get(i);
        }

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }

        public int getInvalidCount() {
            return invalidCount;
        }

        public List<CartCommodity> getList() {
            return list;
        }

        public void edit(boolean edit) {
            if (!edit && isModifyCount) {
                List<CartCommodityItem> items = new ArrayList<CartCommodityItem>();
                for (CartCommodity cartCommodity : modifyList) {
                    CartCommodityItem item = new CartCommodityItem();
                    item.setCartCommodityId(cartCommodity.getCartCommodityId());
                    item.setCount(cartCommodity.getCount());
                    items.add(item);
                }
                modifyCartCount(items);
            } else if (!edit && !isModifyCount) {
                updateEdit();
                modifyList.clear();
                this.edit = edit;
                notifyDataSetChanged();
            } else {
                modifyList.clear();
                this.edit = edit;
                notifyDataSetChanged();
            }
        }

        public void selectAll(boolean selectAll) {
            this.selectAll = selectAll;
            checkList.clear();
            checkMap.clear();
            if (selectAll) {
                for (int i = 0; i < list.size(); i++) {
                    CartCommodity cartCommodity = list.get(i);
                    if (cartCommodity.getStatus() == CommodityStatus.NORMAL) {
                        checkMap.put(cartCommodity.getCartCommodityId(), cartCommodity);
                        checkList.add(cartCommodity);
                    }
                }
            }
            totalChange();
            notifyDataSetChanged();
        }

        public void delete() {
            List<Long> deletes = new ArrayList<Long>();
            for (CartCommodity cartCommodity : checkList) {
                deletes.add(cartCommodity.getCartCommodityId());
            }
            deleteCart(deletes, true);
        }

        private void deleteCart(final List<Long> deletes, final boolean isCheckDelete) {
            ShopSDK.deleteFromShoppingCart(deletes, new SGUIOperationCallback<Void>(page.getCallback()) {
                @Override
                public void onSuccess(Void data) {
                    super.onSuccess(data);
                    for (Long l : deletes) {
                        if (initCountMap.containsKey(l)) {
                            initCountMap.remove(l);
                        }
                        if (listMap.containsKey(l)) {
                            CartCommodity cartCommodity = listMap.get(l);
                            if (cartCommodity.getStatus() != CommodityStatus.NORMAL) {
                                invalidCount -= 1;
                            }
                            listMap.remove(l);
                        }

                    }
                    if (isCheckDelete) {
                        list.removeAll(checkList);
                        if (list.size() == 0) {
                            checkList.clear();
                            checkMap.clear();
                            modifyMap.clear();
                            modifyList.clear();
                            initCountMap.clear();
                            listMap.clear();
                            invalidCount = 0;
                            selectAll = false;
                            isModifyCount = false;
                            edit = false;
                            clearCartList();
                        } else {
                            clearCheckList();
                            totalChange();
                            notifyDataSetChanged();
                        }
                    } else {
                        onRequest(false);
                    }
                }

                @Override
                public boolean onResultError(Throwable t) {
                    page.toastMessage(ResHelper.getStringRes(page.getContext(), "shopsdk_default_modify_model"));
                    return super.onResultError(t);
                }
            });
        }

        private void modifyCartCount(List<CartCommodityItem> cartCommodityItemList) {
            ShopSDK.modifyShoppingCartItemAmount(cartCommodityItemList, new SGUIOperationCallback<Void>(page
                    .getCallback()) {
                @Override
                public void onSuccess(Void data) {
                    super.onSuccess(data);
                    edit = false;
                    updateEdit();
                    notifyDataSetChanged();
                }

                @Override
                public boolean onResultError(Throwable t) {
                    page.toastMessage(ResHelper.getStringRes(page.getContext(),
                            "shopsdk_default_modify_cart_count_failed"));
                    return super.onResultError(t);
                }
            });
        }

        public List<CartCommodity> getCheckList() {
            return checkList;
        }

        public void clearCartList() {
            clearCheckList();

            modifyMap.clear();
            modifyList.clear();
            initCountMap.clear();
            listMap.clear();
            invalidCount = 0;
            selectAll = false;
            isModifyCount = false;
            edit = false;

            totalChange();
            notifyDataSetChanged();
        }

        private void clearCheckList() {
            checkList.clear();
            checkMap.clear();
        }

        private void totalChange() {
            if (onTotalChangeListener != null) {
                String money = "0";
                for (CartCommodity item : checkList) {
                    if (money.contains(".")) {
                        money = String.valueOf(MoneyConverter.conversionDouble(MoneyConverter.toCent(money) + item
                                .getCurrentCost() * item.getCount()));
                    } else if (MoneyConverter.conversionString(item.getCurrentCost()).contains(".")) {
                        money = String.valueOf(MoneyConverter.conversionDouble(item.getCurrentCost()) * item.getCount
                                () + Integer.parseInt(money));
                    } else {
                        money = String.valueOf(MoneyConverter.conversionInt(item.getCurrentCost()) * item.getCount() +
                                Integer.parseInt(money));
                    }
                }
                onTotalChangeListener.onTotalChange(money, checkList.size());
            }
        }

        public void setOnTotalChangeListener(OnTotalChangeListener onTotalChangeListener) {
            this.onTotalChangeListener = onTotalChangeListener;
            totalChange();
        }

        private void getCartList() {
            ShopSDK.getShoppingCartItems(new SGUIOperationCallback<List<CartCommodity>>(page.getCallback()) {
                @Override
                public void onSuccess(List<CartCommodity> data) {
                    getParent().lockPullingUp();
                    if (data != null && data.size() > 0) {
                        bLine.setVisibility(View.VISIBLE);
                        llCart.setVisibility(View.VISIBLE);
                        checkBox.setChecked(false);
                    } else {
                        bLine.setVisibility(View.GONE);
                        llCart.setVisibility(View.GONE);
                    }
                    list = data;
                    invalidCount = 0;
                    if (list != null) {
                        for (CartCommodity cartCommodity : list) {
                            initCountMap.put(cartCommodity.getCartCommodityId(), cartCommodity.getCount());
                            listMap.put(cartCommodity.getCartCommodityId(), cartCommodity);
                            if (cartCommodity.getStatus() != CommodityStatus.NORMAL) {
                                invalidCount += 1;
                            }
                        }
                    }
                    notifyDataSetChanged();
                }

                @Override
                public void onFailed(Throwable t) {
                    super.onFailed(t);
                    getParent().stopPulling();
                }

                @Override
                public boolean onResultError(Throwable t) {
                    if (list.size() == 0) {
                        notifyDataSetChanged();
                    }
                    return false;
                }
            });

        }

        private void getProductAttr(final CartCommodity cartCommodity, final TextView tv, final int index) {
            ShopSDK.getProductDetail(cartCommodity.getProductId(), new SGUIOperationCallback<Product>(page.getCallback()) {
                @Override
                public void onSuccess(final Product data) {
                    super.onSuccess(data);
                    if (data != null && data.getProductPropertyList() != null && data.getProductPropertyList().size()
                            != 0) {
                        final ChoiceProductAttrDialog.Builder builder = new ChoiceProductAttrDialog.Builder(page
                                .getContext(), page.getTheme());
                        builder.setProduct(data);
                        builder.setOnDismissListener(new ChoiceProductAttrDialog.Builder.OnDismissListener() {
                            @Override
                            public void onDismiss(String selectStr, Commodity commodity, int count) {
                                if (commodity != null) {
                                    addCart(cartCommodity.getCartCommodityId(), commodity, count);
                                }
                            }
                        });
                        builder.show();
                    }
                }

                @Override
                public boolean onResultError(Throwable t) {
                    return super.onResultError(t);
                }
            });


        }

        private void addCart(final long deleteCommodityId, final Commodity addCommodity, final int count) {
            if (addCommodity == null) {
                return;
            }
            ShopSDK.addIntoShoppingCart(addCommodity.getCommodityId(), count, new SGUIOperationCallback<Long>(page
                    .getCallback()) {
                @Override
                public void onSuccess(Long data) {
                    super.onSuccess(data);
                    List<Long> list = new ArrayList<Long>();
                    list.add(deleteCommodityId);
                    deleteCart(list, false);
                }

                @Override
                public boolean onResultError(Throwable t) {
                    page.toastMessage(ResHelper.getStringRes(page.getContext(), "shopsdk_default_modify_model"));
                    return super.onResultError(t);
                }
            });
        }


        class ViewHolder {
            private CheckBox checkBox;
            private AsyncImageView iv;
            private TextView tvName;
            private TextView tvAttribute;
            private TextView sAttribute;
            private TextView tvPrice;
            private TextView tvCount;
            private CountView countView;
            private RelativeLayout rlOrder;
            private RelativeLayout llEdit;
            private TextView tvInvalid;
        }
    }

    interface OnTotalChangeListener {
        void onTotalChange(String money, int count);
    }
}
