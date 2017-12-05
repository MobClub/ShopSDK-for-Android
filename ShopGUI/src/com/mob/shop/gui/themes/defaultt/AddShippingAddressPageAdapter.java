package com.mob.shop.gui.themes.defaultt;


import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.mob.shop.OperationCallback;
import com.mob.shop.ShopSDK;
import com.mob.shop.datatype.builder.ShippingAddrBuilder;
import com.mob.shop.datatype.entity.City;
import com.mob.shop.datatype.entity.District;
import com.mob.shop.datatype.entity.Province;
import com.mob.shop.datatype.entity.ShippingAddr;
import com.mob.shop.gui.R;
import com.mob.shop.gui.SGUIOperationCallback;
import com.mob.shop.gui.pages.AddShippingAddressPage;
import com.mob.shop.gui.pages.dialog.ProgressDialog;
import com.mob.shop.gui.pickers.SingleValuePicker;
import com.mob.shop.gui.themes.defaultt.components.SingleChoiceView;
import com.mob.shop.gui.themes.defaultt.components.TitleView;
import com.mob.shop.gui.utils.SGUISPDB;
import com.mob.tools.utils.ResHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 添加/修改收货地址页
 */
public class AddShippingAddressPageAdapter extends DefaultThemePageAdapter<AddShippingAddressPage> implements View
        .OnClickListener {
    private TextView tvAdd;
    private EditText etShippingName;
    private EditText etPhone;
    private TextView tvArea;
    private EditText etAddress;
    private ToggleButton togglebutton;
    private AddShippingAddressPage page;
    private SingleChoiceView llShippingArea;
    private Province province;
    private City city;
    private District district;
    private ShippingAddr shippingAddr;
    private LinearLayout llShippingHint;
    private OperationCallback<ShippingAddr> shippingAddrOperationCallback;
    private ProgressDialog pd;

    @Override
    public void onCreate(AddShippingAddressPage page, final Activity activity) {
        super.onCreate(page, activity);
        this.page = page;
        View view = LayoutInflater.from(page.getContext()).inflate(R.layout.shopsdk_default_page_addshippingaddress,
                null);
        activity.setContentView(view);

        TitleView titleView = (TitleView) view.findViewById(R.id.titleView);
        titleView.initTitleView(page, "shopsdk_default_create_shippingaddress", null, null, true);

        tvAdd = (TextView) view.findViewById(R.id.tvAdd);
        final View line = view.findViewById(R.id.line);
        etShippingName = (EditText) view.findViewById(R.id.etShippingName);
        etPhone = (EditText) view.findViewById(R.id.etPhone);
        etAddress = (EditText) view.findViewById(R.id.etAddress);
        togglebutton = (ToggleButton) view.findViewById(R.id.togglebutton);
        llShippingArea = (SingleChoiceView) view.findViewById(R.id.llShippingArea);
        llShippingHint = (LinearLayout) view.findViewById(R.id.llShippingHint);

        tvAdd.setOnClickListener(this);

        RelativeLayout rlRoot = (RelativeLayout) view.findViewById(R.id.rlRoot);
        rlRoot.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int
                    oldRight, int oldBottom) {
                //获取屏幕高度
                int screenHeight = activity.getWindowManager().getDefaultDisplay().getHeight();
                //阀值设置为屏幕高度的1/3
                int keyHeight = screenHeight / 3;
                if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
                    tvAdd.setVisibility(View.GONE);
                    line.setVisibility(View.GONE);
                } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
                    tvAdd.setVisibility(View.VISIBLE);
                    line.setVisibility(View.VISIBLE);
                }
            }
        });

        llShippingArea.setPage(page);
        llShippingArea.showNext();
        int resId = ResHelper.getStringRes(activity, "shopsdk_default_shippingArea");
        llShippingArea.setTitle(activity.getString(resId));

        llShippingArea.setOnSelectionChangeListener(new SingleChoiceView.OnSelectionChangeListener() {
            @Override
            public void onSelectionsChange() {
                SingleValuePicker.Choice[] choice = llShippingArea.getSelections();
                if (choice != null && choice.length == 3) {
                    province = choice[0] == null ? null : (Province) choice[0].ext;
                    city = choice[1] == null ? null : (City) choice[1].ext;
                    district = choice[2] == null ? null : (District) choice[2].ext;
                }
            }
        });

        shippingAddr = page.getShippingAddr();
        if (shippingAddr != null) {
            etShippingName.setText(shippingAddr.getRealName());
            etPhone.setText(shippingAddr.getTelephone());
            etAddress.setText(shippingAddr.getShippingAddress());
            togglebutton.setChecked(shippingAddr.isDefaultAddr());
            province = shippingAddr.getProvince();
            city = shippingAddr.getCity();
            district = shippingAddr.getDistrict();
            llShippingHint.setVisibility(View.GONE);
            titleView.setTitle("shopsdk_default_modify_shipping_address");
        }

        getAreaData();

        initOperationCallback();
//		initShippingAddress();
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

    private void getAreaData() {
        showPd();
        ShopSDK.getArea(new SGUIOperationCallback<List<Province>>(page.getCallback()) {
            @Override
            public void onSuccess(List<Province> data) {
                super.onSuccess(data);
                SGUISPDB.setArea(data);
                setArea(data);
                initPro();
            }

            @Override
            public void onFailed(Throwable t) {
                dissmissPd();
                super.onFailed(t);
            }

            @Override
            public boolean onResultError(Throwable t) {
                getPage().toastMessage(getString("shopsdk_default_province_info_failed"));
                return super.onResultError(t);
            }
        });
    }

    private void setArea(List<Province> list) {
        ArrayList<SingleValuePicker.Choice> choices = SingleChoiceView.createChoice(Province.class, list);
        llShippingArea.setChoices(choices);
        ShippingAddr shippingAddr = page.getShippingAddr();
        if (shippingAddr != null) {
            Province p = shippingAddr.getProvince();
            City c = shippingAddr.getCity();
            District d = shippingAddr.getDistrict();
            for (int i = 0; i < list.size(); i++) {
                Province province = list.get(i);
                if (province.getName().equals(p.getName())) {
                    List<City> cities = province.getChildren();
                    for (int j = 0; j < cities.size(); j++) {
                        City city = cities.get(j);
                        if (city.getName().equals(c.getName())) {
                            List<District> districts = city.getChildren();
                            for (int k = 0; k < districts.size(); k++) {
                                District district = districts.get(k);
                                if (district.getName().equals(d.getName())) {
                                    llShippingArea.setSelections(new int[]{i, j, k});
                                    return;
                                }
                            }
                        }

                    }
                }
            }
        }
        int resId = ResHelper.getStringRes(page.getContext(), "shopsdk_default_select_shipping_address_hint");
        llShippingArea.setTvChoiceHint(page.getContext().getString(resId));
    }

    private void initPro() {
        if (shippingAddr == null) {
            dissmissPd();
            return;
        }
        Province province = shippingAddr.getProvince();
        City city = shippingAddr.getCity();
        District district = shippingAddr.getDistrict();
        ArrayList<SingleValuePicker.Choice> choices = llShippingArea.getChoices();
        if (province != null) {
            int[] sel = new int[3];
            for (int co = 0; co < choices.size(); co++) {
                if (province.getId() == ((Province) choices.get(co).ext).getId()) {
                    sel[0] = co;
                    choices = choices.get(co).children;
                    if (province != null) {
                        for (int p = 0; p < choices.size(); p++) {
                            if (city.getId() == ((City) choices.get(p).ext).getId()) {
                                sel[1] = p;
                                choices = choices.get(p).children;
                                if (district != null) {
                                    for (int ci = 0; ci < choices.size(); ci++) {
                                        if (district.getId() == ((District) choices.get(ci).ext).getId()) {
                                            sel[2] = ci;
                                            break;
                                        }
                                    }
                                }
                                break;
                            }
                        }
                    }
                    break;
                }
            }
            llShippingArea.setSelections(sel);
            dissmissPd();
        }
    }

    private ShippingAddrBuilder createShippingAddrBuidler() {
        if (TextUtils.isEmpty(etShippingName.getText().toString())) {
            getPage().toastMessage(ResHelper.getStringRes(getPage().getContext(),
                    "shopsdk_default_tip_shippingname_empty"));
            return null;
        }
        if (TextUtils.isEmpty(etPhone.getText().toString())) {
            getPage().toastMessage(ResHelper.getStringRes(getPage().getContext(),
                    "shopsdk_default_tip_shippingphone_empty"));
            return null;
        }
        if (TextUtils.isEmpty(etAddress.getText().toString())) {
            getPage().toastMessage(ResHelper.getStringRes(getPage().getContext(),
                    "shopsdk_default_tip_shippingaddress_empty"));
            return null;
        }

        ShippingAddrBuilder shippingAddrBuilder = new ShippingAddrBuilder(etShippingName.getText().toString(), etPhone
                .getText().toString(), togglebutton.isChecked(), etAddress.getText().toString().trim(), province.getId(),
                city.getId(), district.getId());
        return shippingAddrBuilder;
    }

    private void initOperationCallback() {
        shippingAddrOperationCallback = new SGUIOperationCallback<ShippingAddr>(page.getCallback()) {
            @Override
            public void onSuccess(ShippingAddr data) {
                super.onSuccess(data);
                if (pd != null && pd.isShowing()) {
                    pd.dismiss();
                }
                if (shippingAddr == null) {
                    getPage().toastMessage(getString("shopsdk_default_add_shipping_address_success"));
                } else {
                    getPage().toastMessage(getString("shopsdk_default_modify_shipping_address_success"));
                }
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("newAddr", data);
                getPage().setResult(map);
                finish();
            }

            @Override
            public boolean onResultError(Throwable t) {
                if (pd != null && pd.isShowing()) {
                    pd.dismiss();
                }
                if (shippingAddr == null) {
                    getPage().toastMessage(getString("shopsdk_default_add_shipping_address_failed"));
                } else {
                    getPage().toastMessage(getString("shopsdk_default_modify_shipping_address_failed"));
                }
                return super.onResultError(t);
            }
        };
    }

    private void addShippingAddress(ShippingAddrBuilder shippingAddrBuilder) {
        ShopSDK.createShippingAddr(shippingAddrBuilder, shippingAddrOperationCallback);
    }

    private void modifyShippingAddress(ShippingAddrBuilder shippingAddrBuilder) {
        ShopSDK.modifyShippingAddr(shippingAddr.getShippingAddrId(), shippingAddrBuilder,
                shippingAddrOperationCallback);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvAdd) {
            ShippingAddrBuilder shippingAddrBuilder = createShippingAddrBuidler();
            if (shippingAddrBuilder == null) {
                return;
            }
            showPd();
            if (shippingAddr == null) {
                addShippingAddress(shippingAddrBuilder);
            } else {
                modifyShippingAddress(shippingAddrBuilder);
            }
        }
    }

    @Override
    public void onDestroy(AddShippingAddressPage page, Activity activity) {
        super.onDestroy(page, activity);
        if (pd != null) {
            if (pd.isShowing()) {
                pd.dismiss();
            }
            pd = null;
        }
    }
}
