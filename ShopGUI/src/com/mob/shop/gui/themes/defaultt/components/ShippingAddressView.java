package com.mob.shop.gui.themes.defaultt.components;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mob.shop.datatype.entity.ShippingAddr;
import com.mob.shop.gui.R;

public class ShippingAddressView extends RelativeLayout {
	private TextView tvShippingName;
	private TextView tvShippingPhone;
	private TextView tvShippingAddress;
	private TextView tvHint;

	public ShippingAddressView(Context context) {
		this(context, null);
	}

	public ShippingAddressView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ShippingAddressView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	private void init(Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.shopsdk_default_view_shippingaddress, null);
		addView(view);
		tvShippingName = (TextView) view.findViewById(R.id.tvName);
		tvShippingPhone = (TextView) view.findViewById(R.id.tvPhone);
		tvShippingAddress = (TextView) view.findViewById(R.id.tvAddress);
		tvHint = (TextView) view.findViewById(R.id.tvHint);
		tvHint.setVisibility(GONE);
	}

	public void initView(String shippingName, String shippingPhone, String shippingAddress, boolean isHint) {
		tvShippingName.setText(shippingName);
		tvShippingPhone.setText(shippingPhone);
		tvShippingAddress.setText(shippingAddress);
		setHint(isHint);
	}

	public void initView(ShippingAddr shippingAddr) {
		if (shippingAddr != null) {
			tvShippingName.setText(shippingAddr.getRealName());
			tvShippingPhone.setText(shippingAddr.getTelephone());
			tvShippingAddress.setText(shippingAddr.getShippingAddress());
			setHint(shippingAddr.isDefaultAddr());
		}
	}

	public void setHint(boolean isHint) {
		if (isHint) {
			tvHint.setVisibility(VISIBLE);
		}else{
			tvHint.setVisibility(GONE);
		}
	}

}
