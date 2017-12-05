package com.mob.shop.gui.themes.defaultt;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.mob.shop.ShopSDK;
import com.mob.shop.datatype.builder.ShippingAddrBuilder;
import com.mob.shop.datatype.entity.ShippingAddr;
import com.mob.shop.gui.R;
import com.mob.shop.gui.SGUIOperationCallback;
import com.mob.shop.gui.base.PageAdapter;
import com.mob.shop.gui.pages.AddShippingAddressPage;
import com.mob.shop.gui.pages.ShippingAddressManagePage;
import com.mob.shop.gui.pages.dialog.ProgressDialog;
import com.mob.shop.gui.themes.defaultt.components.ShippingAddressView;
import com.mob.shop.gui.themes.defaultt.components.TitleView;
import com.mob.shop.gui.utils.MultiClickListener;
import com.mob.tools.FakeActivity;
import com.mob.tools.utils.ResHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 收货地址管理页
 */
public class ShippingAddressManagePageAdapter extends PageAdapter<ShippingAddressManagePage> implements View
        .OnClickListener {
    private ListView listView;
    private TextView tvAdd;
    private ShippingAddressManagePage page;
    private ManageShippingAddressAdapter adapter;
    private int hintAddr = -1;
    private ProgressDialog pd;

    @Override
    public void onCreate(final ShippingAddressManagePage page, Activity activity) {
        super.onCreate(page, activity);
        this.page = page;
        View view = LayoutInflater.from(page.getContext()).inflate(R.layout
                .shopsdk_default_page_manage_shipingaddress, null);
        activity.setContentView(view);

        TitleView titleView = (TitleView) view.findViewById(R.id.titleView);
        titleView.initTitleView(page, "shopsdk_default_shippingaddress", null, null, true);
        titleView.setLeftClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hintAddr != adapter.getHintPosition()) {
                    page.setResult(new HashMap<String, Object>());
                    page.finish();
                } else {
                    page.finish();
                }
            }
        });

        listView = (ListView) view.findViewById(R.id.listView);

        tvAdd = (TextView) view.findViewById(R.id.tvAdd);
        tvAdd.setOnClickListener(this);

        adapter = new ManageShippingAddressAdapter(listView);
        listView.setAdapter(adapter);
        getShippingAddrs();
    }

    @Override
    public void onClick(View v) {
        if (ShopSDK.getUser().isAnonymous()) {
            if (getPage().getCallback() != null) {
                getPage().getCallback().login();
            }
            return;
        }
        if (v.getId() == R.id.tvAdd) {
            AddShippingAddressPage addShippingAddressPage = new AddShippingAddressPage(getPage().getTheme(), null);
            addShippingAddressPage.showForResult(getPage().getContext(), null, new FakeActivity() {

                @Override
                public void onResult(HashMap<String, Object> data) {
                    super.onResult(data);
                    if (data != null) {
                        Object newAddr = data.get("newAddr");
                        if (newAddr instanceof ShippingAddr) {
                            adapter.addShippingAddr((ShippingAddr) newAddr);
                        }
                    }
                }
            });
        }
    }

    private void getShippingAddrs() {
        List<ShippingAddr> list = page.getList();
        if (list != null || list.size() != 0) {
            adapter.setList(list);
            return;
        }

        pd = new ProgressDialog.Builder(page.getContext(), page.getTheme()).show();
        ShopSDK.getShippingAddrs(new SGUIOperationCallback<List<ShippingAddr>>(page.getCallback()) {
            @Override
            public void onSuccess(List<ShippingAddr> data) {
                super.onSuccess(data);
                if(pd != null && pd.isShowing()){
                    pd.dismiss();
                }
                adapter.setList(data);
            }

            @Override
            public boolean onResultError(Throwable t) {
                if(pd != null && pd.isShowing()){
                    pd.dismiss();
                }
                return super.onResultError(t);
            }
        });
    }

    @Override
    public void onDestroy(ShippingAddressManagePage page, Activity activity) {
        super.onDestroy(page, activity);
        if (pd != null) {
            if (pd.isShowing()) {
                pd.dismiss();
            }
            pd = null;
        }
    }

    private class ManageShippingAddressAdapter extends BaseAdapter {
        private List<ShippingAddr> list = new ArrayList<ShippingAddr>();
        private int hintPosition = 0;
        private ListView listView;

        public ManageShippingAddressAdapter(ListView listView) {
            this.listView = listView;
        }

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public int getHintPosition() {
            return hintPosition;
        }

        public void setList(List<ShippingAddr> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        public void addShippingAddr(ShippingAddr shippingAddr) {
            if (list == null) {
                list = new ArrayList<ShippingAddr>();
            }
            list.add(shippingAddr);
            if (shippingAddr.isDefaultAddr()) {
                list.get(hintPosition).setDefaultAddr(false);
            }
            notifyDataSetChanged();
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            ViewHolder vh = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout
                        .shopsdk_default_item_manage_shippingaddress, null);
                vh = new ViewHolder();
                vh.shippingAddressView = (ShippingAddressView) convertView.findViewById(R.id.shippingAddressView);
                vh.tvDelete = (TextView) convertView.findViewById(R.id.tvDelete);
                vh.tvEdit = (TextView) convertView.findViewById(R.id.tvEdit);
                vh.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }

            final ShippingAddr shippingAddr = list.get(position);
            vh.shippingAddressView.initView(shippingAddr);
            vh.shippingAddressView.setHint(false);
            if (shippingAddr.isDefaultAddr()) {
                hintPosition = position;
//                if (hintAddr == -1) {
//                    hintAddr = hintPosition;
//                }
                vh.checkBox.setChecked(true);
            } else {
                vh.checkBox.setChecked(false);
            }

            vh.tvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ShopSDK.getUser().isAnonymous()) {
                        if (getPage().getCallback() != null) {
                            getPage().getCallback().login();
                        }
                        return;
                    }
                    deletaShippingAddr(position, shippingAddr.getShippingAddrId());
                }
            });
            vh.tvEdit.setOnClickListener(new MultiClickListener() {

                @Override
                public void onMultiClick(View v) {
                    if (ShopSDK.getUser().isAnonymous()) {
                        if (getPage().getCallback() != null) {
                            getPage().getCallback().login();
                        }
                        return;
                    }
                    AddShippingAddressPage addShippingAddressPage = new AddShippingAddressPage(getPage().getTheme(),
                            shippingAddr);
                    addShippingAddressPage.showForResult(parent.getContext(), null, new FakeActivity() {
                        @Override
                        public void onResult(HashMap<String, Object> data) {
                            super.onResult(data);
                            if (data != null) {
                                list.remove(position);
                                Object newAddr = data.get("newAddr");
                                if (newAddr instanceof ShippingAddr) {
                                    addShippingAddr((ShippingAddr) newAddr);
                                }
                            }
                        }
                    });
                }
            });
            final ViewHolder finalVh = vh;
            vh.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position != hintPosition) {
                        View view = listView.getChildAt(hintPosition - listView.getFirstVisiblePosition());
                        if (view != null) {
                            ViewHolder vh1 = (ViewHolder) view.getTag();
                            vh1.checkBox.setChecked(false);
                        }
                        finalVh.checkBox.setChecked(true);
                        modifyShippingAddr(hintPosition, position, shippingAddr, true);
                        hintPosition = position;
                    } else {
                        finalVh.checkBox.setChecked(true);
                    }
                }
            });
            return convertView;
        }

        private void deletaShippingAddr(final int position, long shippingAddrId) {
            ShopSDK.deleteShippingAddr(shippingAddrId, new SGUIOperationCallback<Void>(page.getCallback()) {
                @Override
                public void onSuccess(Void data) {
                    super.onSuccess(data);
                    page.toastMessage(ResHelper.getStringRes(getPage().getContext(),
                            "shopsdk_default_delete_address_success"));
                    list.remove(position);
                    notifyDataSetChanged();
                }

                @Override
                public boolean onResultError(Throwable t) {
                    page.toastMessage(ResHelper.getStringRes(getPage().getContext(),
                            "shopsdk_default_delete_shipping_address_failed"));
                    return super.onResultError(t);
                }
            });

        }

        private void modifyShippingAddr(final int prePosition, final int position, ShippingAddr shippingAddr, boolean isDefaultAddr) {
            ShippingAddrBuilder builder = new ShippingAddrBuilder(shippingAddr.getRealName(), shippingAddr
                    .getTelephone(), isDefaultAddr, shippingAddr.getStreet(), shippingAddr.getProvince().getId(),
                    shippingAddr.getCity().getId(), shippingAddr.getDistrict().getId());
            ShopSDK.modifyShippingAddr(shippingAddr.getShippingAddrId(), builder, new SGUIOperationCallback<ShippingAddr>(page.getCallback()) {
                @Override
                public void onSuccess(ShippingAddr data) {
                    super.onSuccess(data);
                    page.toastMessage(ResHelper.getStringRes(getPage().getContext(),
                            "shopsdk_default_set_default_shipping_address_success"));
                    list.get(position).setDefaultAddr(data.isDefaultAddr());
                    if (prePosition <= list.size() - 1) {
                        list.get(prePosition).setDefaultAddr(false);
                    }
                    notifyDataSetChanged();
                }

                @Override
                public boolean onResultError(Throwable t) {
                    page.toastMessage(ResHelper.getStringRes(getPage().getContext(),
                            "shopsdk_default_set_default_shipping_address_failed"));
                    return super.onResultError(t);
                }
            });
        }


        private class ViewHolder {
            private ShippingAddressView shippingAddressView;
            private TextView tvDelete;
            private TextView tvEdit;
            private CheckBox checkBox;
        }
    }

}
