package com.mob.shop.gui.themes.defaultt.components;


import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mob.shop.datatype.entity.ShippingAddr;
import com.mob.shop.gui.R;

public class OrderShippingAddressView extends RelativeLayout {
	private TextView tvShippingName;
	private TextView tvShippingPhone;
	private TextView tvShippingAddress;
	private ImageView ivMore;

	public OrderShippingAddressView(Context context) {
		this(context, null);
	}

	public OrderShippingAddressView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public OrderShippingAddressView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	private void init(Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.shopsdk_default_view_ordershippingaddress, null);
		addView(view);
		tvShippingName = (TextView) view.findViewById(R.id.tvName);
		tvShippingPhone = (TextView) view.findViewById(R.id.tvPhone);
		tvShippingAddress = (TextView) view.findViewById(R.id.tvAddress);
		ivMore = (ImageView) view.findViewById(R.id.ivMore);
		ivMore.setVisibility(GONE);
	}

	public void initView(String shippingName, String shippingPhone, String shippingAddress, boolean isMore) {
		tvShippingName.setText(shippingName);
		if(!TextUtils.isEmpty(shippingPhone)){
			if(shippingPhone.length()>=11){
				tvShippingPhone.setText(shippingPhone.substring(0, 3) + "****" + shippingPhone.substring(8));
			}else if(shippingPhone.length()>=7){
				tvShippingPhone.setText(shippingPhone.substring(0, 3) + "****" + shippingPhone.substring(4));
			}
		}
		tvShippingAddress.setText(shippingAddress);
		if (isMore) {
			ivMore.setVisibility(VISIBLE);
		}
	}

	public void initView(ShippingAddr shippingAddr, boolean isMore) {
		if (shippingAddr != null) {
			tvShippingName.setText(shippingAddr.getRealName());
			tvShippingPhone.setText(shippingAddr.getTelephone());
			tvShippingAddress.setText(shippingAddr.getShippingAddress());
			if(!TextUtils.isEmpty(shippingAddr.getTelephone())){
				if(shippingAddr.getTelephone().length()>=11){
					tvShippingPhone.setText(shippingAddr.getTelephone().substring(0, 3) + "****" + shippingAddr.getTelephone().substring(8));
				}else if(shippingAddr.getTelephone().length()>=7){
					tvShippingPhone.setText(shippingAddr.getTelephone().substring(0, 3) + "****" + shippingAddr.getTelephone().substring(4));
				}
			}
		}
		if (isMore) {
			ivMore.setVisibility(VISIBLE);
		} else{
			ivMore.setVisibility(GONE);
		}
	}

	public void setIvMore(boolean isMore) {
		if (isMore) {
			ivMore.setVisibility(VISIBLE);
		}
	}

}
