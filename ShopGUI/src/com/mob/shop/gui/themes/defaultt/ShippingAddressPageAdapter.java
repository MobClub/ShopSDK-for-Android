package com.mob.shop.gui.themes.defaultt;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mob.shop.ShopSDK;
import com.mob.shop.datatype.entity.ShippingAddr;
import com.mob.shop.gui.R;
import com.mob.shop.gui.SGUIOperationCallback;
import com.mob.shop.gui.pages.AddShippingAddressPage;
import com.mob.shop.gui.pages.ShippingAddressManagePage;
import com.mob.shop.gui.pages.ShippingAddressPage;
import com.mob.shop.gui.pages.dialog.ProgressDialog;
import com.mob.shop.gui.themes.defaultt.components.ShippingAddressView;
import com.mob.shop.gui.themes.defaultt.components.TitleView;
import com.mob.tools.FakeActivity;
import com.mob.tools.utils.ResHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 收货地址列表页
 */
public class ShippingAddressPageAdapter extends DefaultThemePageAdapter<ShippingAddressPage> implements View
        .OnClickListener {
    private ListView listView;
    private TextView tvAdd;
    private ShippingAddressPage page;
    private ShippingAddressAdapter adapter;
    private long orderAddrId;
    private boolean isRefresh = true;
    private ProgressDialog pd;

    @Override
    public void onCreate(final ShippingAddressPage page, final Activity activity) {
        super.onCreate(page, activity);
        this.page = page;
        View view = LayoutInflater.from(page.getContext()).inflate(R.layout.shopsdk_default_page_shipingaddress, null);
        activity.setContentView(view);

        TitleView titleView = (TitleView) view.findViewById(R.id.titleView);
        titleView.initTitleView(page, "shopsdk_default_shippingaddress", "shopsdk_default_manage", new View
                .OnClickListener() {
            @Override
            public void onClick(View v) {
                ShippingAddressManagePage mPage = new ShippingAddressManagePage(page.getTheme(), adapter.getList());
                mPage.showForResult(page.getContext(), null, new FakeActivity() {
                    @Override
                    public void onResult(HashMap<String, Object> data) {
                        super.onResult(data);
                        if (data != null) {
                            getShippingAddrs();
                        }
                    }
                });
            }
        }, true);
        titleView.setLeftClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRefresh){
                    page.setResult(new HashMap<String, Object>());
                }
                finish();
            }
        });

        listView = (ListView) view.findViewById(R.id.listView);

        tvAdd = (TextView) view.findViewById(R.id.tvAdd);
        tvAdd.setOnClickListener(this);

        adapter = new ShippingAddressAdapter();
        listView.setAdapter(adapter);
        getShippingAddrs();

        orderAddrId = page.getShippingId();
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
                page.toastMessage(ResHelper.getStringRes(page.getContext(), "shopsdk_default_shipping_addr_failed"));
                return super.onResultError(t);
            }
        });
    }

    @Override
    public void onDestroy(ShippingAddressPage page, Activity activity) {
        super.onDestroy(page, activity);
        if (pd != null) {
            if (pd.isShowing()) {
                pd.dismiss();
            }
            pd = null;
        }
    }

    private class ShippingAddressAdapter extends BaseAdapter {
        private List<ShippingAddr> list = new ArrayList<ShippingAddr>();
        private int hintPosition = -1;

        public ShippingAddressAdapter() {
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

        public void setList(List<ShippingAddr> list) {
            this.list = list;
            isRefresh = true;
            notifyDataSetChanged();
        }

        public List<ShippingAddr> getList() {
            return list;
        }

        public void addShippingAddr(ShippingAddr shippingAddr) {
            if (list == null) {
                list = new ArrayList<ShippingAddr>();
            }
            if (shippingAddr.isDefaultAddr() && hintPosition >= 0) {
                list.get(hintPosition).setDefaultAddr(false);
            }
            list.add(shippingAddr);
            isRefresh = true;
            notifyDataSetChanged();
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            ViewHolder vh = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout
                        .shopsdk_default_item_shippingaddress, null);
                vh = new ViewHolder();
                vh.shippingAddressView = (ShippingAddressView) convertView.findViewById(R.id.shippingAddressView);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }

            final ShippingAddr shippingAddr = list.get(position);
            if(shippingAddr.getShippingAddrId() == orderAddrId){
                isRefresh = false;
            }

            if (shippingAddr.isDefaultAddr()) {
                hintPosition = position;
            }
            vh.shippingAddressView.initView(shippingAddr);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("newAddr", shippingAddr);
                    getPage().setResult(map);
                    finish();
                }
            });
            return convertView;
        }


        private class ViewHolder {
            private ShippingAddressView shippingAddressView;
        }
    }

}
